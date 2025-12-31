package edu.pnu.controller;

import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import edu.pnu.dto.MedicalInfoSearch;
import edu.pnu.dto.MedicalSggSearch;
import edu.pnu.service.HospitalService;
import lombok.RequiredArgsConstructor;

// 데이터 처리 컨트롤러

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class HospitalApiController {
	private final HospitalService hospitalService;

	// 전체 or 시군구로 조회 + 전체 병원 수 및 시군구별 병원 수
	@GetMapping("/medicalSigungu")
	public ResponseEntity<?> medicalDetails(MedicalSggSearch mss) {
		if (mss.getSidoName() == null || mss.getSigunguName() == null) {
			return ResponseEntity.ok(
					Map.of(
						"list", hospitalService.findAll(mss.getPage(), mss.getSize()),
						"totalCount", hospitalService.countAllHospitals()
					));
		}
		return ResponseEntity.ok(
				Map.of(
					"list", hospitalService.findBySidoNameAndSigunguName(
					        mss.getSidoName(), mss.getSigunguName(), mss.getPage(), mss.getSize()),
					"totalCount", hospitalService.countAllHospitals(),
					"sigunguCount", hospitalService.countHospitalsBySigunguName(mss.getSidoName(), mss.getSigunguName())
				));
	}

	// 위치로 조회
	@GetMapping("/medicalInfo")
	public ResponseEntity<?> medicalInfo(MedicalInfoSearch mis, int level) {
		return ResponseEntity.ok
				(Map.of(
					"list", hospitalService.findByLocation(
							mis.getSwLat(), mis.getNeLat(), mis.getSwLng(), mis.getNeLng(), level),
					"totalCount", hospitalService.countAllHospitals()
				));
	}

	// 하나만 조회
	@GetMapping("/medicalCareEncCode")
	public ResponseEntity<?> medicalCareEncCode(@RequestParam String careEncCode) {
		return ResponseEntity.ok(hospitalService.findById(careEncCode));
	}
}
