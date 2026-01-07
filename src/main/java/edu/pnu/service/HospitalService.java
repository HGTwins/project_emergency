package edu.pnu.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import edu.pnu.dto.BasicInfoDto;
import edu.pnu.dto.DeptCountDto;
import edu.pnu.dto.TypeCountDto;
import edu.pnu.dto.MedicalInfoSearch;
import edu.pnu.dto.MedicalSggSearch;
import edu.pnu.dto.MedicalChartSearch;
import edu.pnu.dto.MedicalScoreSearch;
import edu.pnu.persistence.DeptRepository;
import edu.pnu.persistence.OperationInfoRepository;
import edu.pnu.persistence.BasicInfoRepository;
import edu.pnu.persistence.SidoRepository;
import edu.pnu.persistence.SigunguRepository;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class HospitalService {
	private final SidoRepository sidoRepo;
	private final SigunguRepository sigunguRepo;
	private final BasicInfoRepository basicRepo;
	private final DeptRepository deptRepo;
	private final OperationInfoRepository operRepo;
	
	// 시도명 프론트에 전달
	public List<String> getListAllSidoNames() {
		return sidoRepo.getListAllSidoNames();
	}
	
	// 시군구명 프론트에 전달
	public List<String> getListSigunguNamesBySidoName(String sidoName) {
		return sigunguRepo.getListSigunguNamesBySidoName(sidoName);
	}
	
	// 병원 조회
	public Page<BasicInfoDto> getHospitalInfo(MedicalSggSearch mss) {
		Pageable pageable = PageRequest.of(mss.getPage(), mss.getSize());
		// 전체 병원
		if (mss.getSidoName() == null && mss.getSigunguName() == null) {
			return basicRepo.findAll(pageable).map(BasicInfoDto::from);
		// 시도별 병원
		} else if (mss.getSidoName() != null && mss.getSigunguName() == null) {
			return basicRepo.getPageBySidoName(mss.getSidoName(), pageable)
					.map(BasicInfoDto::from);
		// 시도 및 시군구별 병원
		} else {
			return basicRepo.getPageBySidoNameAndSigunguName(mss.getSidoName(), mss.getSigunguName(), pageable)
					.map(BasicInfoDto::from);
		}
	}
	
	// 하나만 조회
	public BasicInfoDto findById(Long hospitalId) {
		return basicRepo.findById(hospitalId)
				.map(BasicInfoDto::from)
				.orElseThrow(() -> new IllegalArgumentException("해당 병원 없음"));
	}

	// 위치로 조회
	public List<BasicInfoDto> getListByLocation(MedicalInfoSearch mis){
	
		int limit = switch(mis.getLevel()) {
			case 1, 2 -> 1000;	// 전국 수준
			case 3, 4 -> 500;	// 시도
			default -> 200;		// 시군구
		};
		
		Pageable pageable = PageRequest.of(0, limit);

		return basicRepo.getListByLocation(mis.getSwLat(), mis.getNeLat(), mis.getSwLng(), mis.getNeLng(), pageable)
				.stream()
				.map(BasicInfoDto::from)
				.toList();
	}
	
	// 병원 수 (스코어 카드)
	public Long getCountHospital(MedicalScoreSearch mss) {
		// 전체
		if (mss.getSidoName() == null && mss.getSigunguName() == null) {
			return basicRepo.count();
		// 시도별
		} else if (mss.getSidoName() != null && mss.getSigunguName() == null) {
			return basicRepo.getCountBySidoName(mss.getSidoName());
		// 시군구별
		} else {
			return basicRepo.getCountBySidoNameAndSigunguName(mss.getSidoName(), mss.getSigunguName());
		}
	}
	
	// 필수 의료 수 (스코어 카드)
	public Long getCountEssential(MedicalScoreSearch mes){
		// 전체
		if (mes.getSidoName() == null && mes.getSigunguName() == null) {
			return deptRepo.getCountByAllEssential();
		// 시도별
		} else if (mes.getSidoName() != null && mes.getSigunguName() == null) {
			return deptRepo.getCountByEssentialAndSidoName(mes.getSidoName());
		// 시군구별
		} else {
			return deptRepo.getCountByEssentialAndSidoNameAndSigunguName(mes.getSidoName(), mes.getSigunguName());
		}
	}
	
	// 일요일/공휴일 진료 병원 수 (스코어 카드)
	public Long getCountHolidayOpen(MedicalScoreSearch mes) {
		// 전체
		if (mes.getSidoName() == null && mes.getSigunguName() == null) {
			return operRepo.getCountAllByHolidayOpen();
		// 시도별
		} else if (mes.getSidoName() != null && mes.getSigunguName() == null) {
			return operRepo.getCountByHolidayOpenAndSidoName(mes.getSidoName());
		// 시군구별
		} else {
			return operRepo.getCountByHolidayOpenAndSidoNameAndSigunguName(mes.getSidoName(), mes.getSigunguName());
		}
	}
	
	// 야간 진료 병원 수 (스코어 카드)
	public Long getCountNightOpen(MedicalScoreSearch mes) {
		// 전체
		if (mes.getSidoName() == null && mes.getSigunguName() == null) {
			return operRepo.getCountAllByNightOpen();
		// 시도별
		} else if (mes.getSidoName() != null && mes.getSigunguName() == null) {
			return operRepo.getCountByNightOpenAndSidoName(mes.getSidoName());
		// 시군구별
		} else {
			return operRepo.getCountByNightOpenAndSidoNameAndSigunguName(mes.getSidoName(), mes.getSigunguName());
		}
	}
	
	// 병원 유형 수 (차트)
	public List<TypeCountDto> getTopNWithOthersByType(MedicalChartSearch mcs) {
		List<TypeCountDto> typeList;
		// 전체
		if (mcs.getSidoName() == null && mcs.getSigunguName() == null) {
			typeList =  basicRepo.getListByType();
		// 시도별
		} else if (mcs.getSidoName() != null && mcs.getSigunguName() == null) {
			typeList =  basicRepo.getListByTypeAndSidoName(mcs.getSidoName());
		// 시군구별
		} else {
			typeList =  basicRepo.getListByTypeAndSidoNameAndSigunguName(mcs.getSidoName(), mcs.getSigunguName());
		}
		
		// 1. count 기준 모든 유형 내림차순 정렬
		typeList.sort((a, b) -> b.getCount().compareTo(a.getCount()));
		
		// 2. 상위 N개
		List<TypeCountDto> topList =  typeList.stream().limit(mcs.getTopN()).toList();
		
		// 3. 기타 합산
		long othersCount = typeList.stream().skip(mcs.getTopN()).mapToLong(TypeCountDto::getCount).sum();
		
		// 4. 기타 항목 있으면 topList에 추가
		if (othersCount > 0) {
			List<TypeCountDto> result = new ArrayList<>(topList);
			result.add(new TypeCountDto("기타", othersCount));
			return result;
		}
		
		return topList;
	}
	
	// 진료 과목 수 (차트)
	public List<DeptCountDto> getTopNWithOthersByDept(MedicalChartSearch mcs) {
		List<DeptCountDto> deptList;
		// 전체
		if (mcs.getSidoName() == null && mcs.getSigunguName() == null) {
			deptList =  deptRepo.getListByDept();
		// 시도별
		} else if (mcs.getSidoName() != null && mcs.getSigunguName() == null) {
			deptList =  deptRepo.getListByDeptAndSidoName(mcs.getSidoName());
		// 시군구별
		} else {
			deptList = deptRepo.getListByDeptAndSidoNameAndSigunguName(mcs.getSidoName(), mcs.getSigunguName());
		}
		
		deptList.sort((a, b) -> b.getCount().compareTo(a.getCount()));			
		List<DeptCountDto> topList =  deptList.stream().limit(mcs.getTopN()).toList();
		long othersCount = deptList.stream().skip(mcs.getTopN()).mapToLong(DeptCountDto::getCount).sum();

		if (othersCount > 0) {
			List<DeptCountDto> result = new ArrayList<>(topList);
			result.add(new DeptCountDto("기타", othersCount));
			return result;
		}
		
		return topList;
	}
}
