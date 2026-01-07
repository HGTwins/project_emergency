package edu.pnu.persistence;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import edu.pnu.domain.hospital.OperationInfo;

public interface OperationInfoRepository extends JpaRepository<OperationInfo, Long> {
	// 전체 일요일/공휴일 진료 병원 수
	@Query("""
			SELECT COUNT(o)
			FROM OperationInfo o
			WHERE o.closedHoliday IS NOT NULL OR o.closedSunday IS NOT NULL
			""")
	Long getCountAllByHolidayOpen();

	// 시도별 일요일/공휴일 진료 병원 수
	@Query("""
			SELECT COUNT(o)
			FROM OperationInfo o
			JOIN o.basicInfo b
			JOIN b.sidoCode sd
			WHERE sd.sidoName = :sidoName
			AND (o.closedHoliday IS NOT NULL OR o.closedSunday IS NOT NULL)
			""")
	Long getCountByHolidayOpenAndSidoName(@Param("sidoName") String sidoName);

	// 시군구별 일요일/공휴일 진료 병원 수
	@Query("""
			SELECT COUNT(o)
			FROM OperationInfo o
			JOIN o.basicInfo b
			JOIN b.sidoCode sd
			JOIN b.sigunguCode sg
			WHERE sd.sidoName = :sidoName AND sg.sigunguName = :sigunguName
			AND (o.closedHoliday IS NOT NULL OR o.closedSunday IS NOT NULL)
			""")
	Long getCountByHolidayOpenAndSidoNameAndSigunguName(@Param("sidoName") String sidoName,
			@Param("sigunguName") String sigunguName);

	// 전체 야간 진료 병원 수
	@Query("""
			SELECT COUNT(o)
			FROM OperationInfo o
			WHERE o.endMonday >= '18:01'
			OR o.endTuesday >= '18:01'
			OR o.endWednesday >= '18:01'
			OR o.endThursday >= '18:01'
			OR o.endFriday >= '18:01'
			OR o.endSaturday >= '18:01'
			OR o.endSunday >= '18:01'
			""")
	Long getCountAllByNightOpen();

	// 시도별 일요일/공휴일 진료 병원 수
	@Query("""
			SELECT COUNT(o)
			FROM OperationInfo o
			JOIN o.basicInfo b
			JOIN b.sidoCode sd
			WHERE sd.sidoName = :sidoName
			AND (o.endMonday >= '18:01'
			OR o.endTuesday >= '18:01'
			OR o.endWednesday >= '18:01'
			OR o.endThursday >= '18:01'
			OR o.endFriday >= '18:01'
			OR o.endSaturday >= '18:01'
			OR o.endSunday >= '18:01')
			""")
	Long getCountByNightOpenAndSidoName(@Param("sidoName") String sidoName);

	// 시군구별 일요일/공휴일 진료 병원 수
	@Query("""
			SELECT COUNT(o)
			FROM OperationInfo o
			JOIN o.basicInfo b
			JOIN b.sidoCode sd
			JOIN b.sigunguCode sg
			WHERE sd.sidoName = :sidoName AND sg.sigunguName = :sigunguName
			AND (o.endMonday >= '18:01'
			OR o.endTuesday >= '18:01'
			OR o.endWednesday >= '18:01'
			OR o.endThursday >= '18:01'
			OR o.endFriday >= '18:01'
			OR o.endSaturday >= '18:01'
			OR o.endSunday >= '18:01')
			""")
	Long getCountByNightOpenAndSidoNameAndSigunguName(
				@Param("sidoName") String sidoName, @Param("sigunguName") String sigunguName);
}
