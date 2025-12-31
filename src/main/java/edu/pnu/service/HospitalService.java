package edu.pnu.service;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import edu.pnu.dto.BasicInfoDTO;
import edu.pnu.persistence.HospitalRepository;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class HospitalService {
	private final HospitalRepository basicInfoRepo;
	
	// 시도 및 시군구로 조회
	public Page<BasicInfoDTO> findBySidoNameAndSigunguName(String sidoName, String sigunguName, int page, int size){
		Pageable pageable = PageRequest.of(page, size);
		return basicInfoRepo.findBySidoNameAndSigunguName(sidoName, sigunguName, pageable)
				.map(BasicInfoDTO::from);
	}
	
	// 전부 조회
	public Page<BasicInfoDTO> findAll(int page, int size){
		Pageable pageable = PageRequest.of(page, size);
		return basicInfoRepo.findAll(pageable)
				.map(BasicInfoDTO::from);
	}
	
	// 하나만 조회
	public BasicInfoDTO findById(String careEncCode) {
		return basicInfoRepo.findById(careEncCode)
				.map(BasicInfoDTO::from)
				.orElseThrow(() -> new IllegalArgumentException("해당 병원 없음"));
	}

	// 위치로 조회
	public List<BasicInfoDTO> findByLocation(double swLat, double neLat, double swLng, double neLng, int level){
	
		int limit = switch(level) {
			case 1, 2 -> 1000;
			case 3, 4 -> 500;
			default -> 200;
		};
		
		Pageable pageable = PageRequest.of(0, limit);

		return basicInfoRepo.findByLocation(swLat, neLat, swLng, neLng, pageable)
				.stream()
				.map(BasicInfoDTO::from)
				.toList();
	}
	
	// 전체 병원 수
	public long countAllHospitals() {
		return basicInfoRepo.countAllHospitals();
	}
	
	// 시도별 병원 수 
	public long countHospitalsBySidoName(String sidoName) {
		return basicInfoRepo.countHospitalsBySidoName(sidoName);
	}
	
	// 시군구별 병원 수
	public long countHospitalsBySigunguName(String sidoName, String sigunguName) {
		return basicInfoRepo.countHospitalsBySigunguName(sidoName, sigunguName);
	}
	
	// 전체 병원 유형
	
	// 시군구별 병원 유형
	
	// 전체 진료 과목
	
	// 시군구별 진료 과목
}
