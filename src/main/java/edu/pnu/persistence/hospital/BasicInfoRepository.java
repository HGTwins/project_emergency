package edu.pnu.persistence.hospital;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import edu.pnu.domain.hospital.BasicInfo;

public interface BasicInfoRepository extends JpaRepository<BasicInfo, String> {

	@Query("SELECT b FROM BasicInfo b " +
		   "JOIN SigunguCode s ON b.sidoCode = s.sidoCode " +
		   "WHERE s.sigunguName = :sigunguName")
	List<BasicInfo> findBySigunguName(@Param("sigunguName") String sigunguName);
}