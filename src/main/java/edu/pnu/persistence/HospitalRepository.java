package edu.pnu.persistence;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import edu.pnu.domain.hospital.BasicInfo;
import edu.pnu.dto.HospitalTypeCountDTO;

public interface HospitalRepository extends JpaRepository<BasicInfo, String> {
	
	// 시도명 및 시군구명으로 조회
	@Query(value = """
		    SELECT *
		    FROM basic_info b
		    JOIN sido_code sd ON b.sido_code = sd.sido_code
		    JOIN sigungu_code s ON b.sido_code = s.sido_code
		    WHERE sd.sido_name = :sidoName AND s.sigungu_name = :sigunguName
		""", nativeQuery = true)
	Page<BasicInfo> findBySidoNameAndSigunguName(@Param("sidoName") String sidoName,
												@Param("sigunguName") String sigunguName,
												Pageable pageable);
	
	// 위치로 조회
	@Query("SELECT b FROM BasicInfo b " +
            "JOIN Offset o ON b.careEncCode = o.careEncCode " +
            "WHERE o.latitude BETWEEN :swLat AND :neLat " +
            "AND o.longitude BETWEEN :swLng AND :neLng")
    List<BasicInfo> findByLocation(@Param("swLat") double swLat, @Param("neLat") double neLat,
    								@Param("swLng") double swLng, @Param("neLng") double neLng,
    								Pageable pageable);
	
	// 전체 병원 수
	@Query("SELECT COUNT(b) FROM BasicInfo b")
	long countAllHospitals();
	
	// 시도별 병원 수 
	@Query(value = """
		    SELECT COUNT(*)
		    FROM basic_info b
		    JOIN sido_code s ON b.sido_code = s.sido_code
		    WHERE s.sido_name = :sidoName
		""", nativeQuery = true)
	long countHospitalsBySidoName(@Param("sidoName") String sidoName);
	
	// 시군구별 병원 수 
	@Query(value = """
		    SELECT COUNT(*)
		    FROM basic_info b
		    JOIN sido_code sd ON b.sido_code = sd.sido_code
		    JOIN sigungu_code s ON b.sido_code = s.sido_code
		    WHERE sd.sido_name = :sidoName
		      AND s.sigungu_name = :sigunguName
		""", nativeQuery = true)
	long countHospitalsBySigunguName(@Param("sidoName") String sidoName, @Param("sigunguName") String sigunguName);
	
	// 전체 병원 유형
	@Query("SELECT new edu.pnu.dto.HospitalTypeCountDTO(t.typeCode, COUNT(b))"
			+ "FROM BasicInfo b " 
			+ "JOIN b.typeCode t GROUP BY t.typeCode")
	List<HospitalTypeCountDTO> countAllByType();
	
	// 시군구별 병원 유형
	@Query("SELECT new edu.pnu.dto.HospitalTypeCountDTO(t.typeCode, COUNT(b))"
			+ "FROM BasicInfo b " 
			+ "JOIN b.typeCode t "
			+ "JOIN SigunguCode s ON b.sidoCode = s.sidoCode "
			+ "WHERE s.sigunguName = :sigunguName "
			+ "GROUP BY t.typeCode")
	List<HospitalTypeCountDTO> countByTypeAndSigunguName(@Param("sigunguName") String sigunguName);
	
	// 전체 진료 과목
	
	// 시군구별 진료 과목
}