package edu.pnu.controller;

import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import edu.pnu.dto.MedicalInfoSearch;
import edu.pnu.dto.MedicalSggSearch;
import edu.pnu.dto.MedicalChartSearch;
import edu.pnu.dto.MedicalScoreSearch;
import edu.pnu.service.HospitalService;
import edu.pnu.util.JWTUtil;
import lombok.RequiredArgsConstructor;

// 데이터 처리 컨트롤러

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class HospitalApiController {
	private final HospitalService hospitalService;
	
	// oauth2 로그인 시 token 프론트에 전달
	@PostMapping("/jwtcallback")
	public ResponseEntity<?> apiCallback(@CookieValue String jwtToken) {
		return ResponseEntity.ok().header(HttpHeaders.AUTHORIZATION, JWTUtil.prefix + jwtToken).build();
	}
	
	// 시도명 프론트에 전달
	@GetMapping("/sidoName")
	public ResponseEntity<?> sidoName() {
		return ResponseEntity.ok(hospitalService.getListAllSidoNames());
	}
	
	// 시군구명 프론트에 전달
	@GetMapping("/sigunguName")
	public ResponseEntity<?> sigunguName(@RequestParam String sidoName) {
		return ResponseEntity.ok(hospitalService.getListSigunguNamesBySidoName(sidoName));
	}
	
	// 시도 및 시군구명으로 병원 조회
	@GetMapping("/medicalSigungu")
	public ResponseEntity<?> medicalSigungu(MedicalSggSearch mss) {
		return ResponseEntity.ok(hospitalService.getHospitalInfo(mss));
	}

	// 위치로 병원 조회
	@GetMapping("/medicalInfo")
	public ResponseEntity<?> medicalInfo(MedicalInfoSearch mis) {
		return ResponseEntity.ok(hospitalService.getListByLocation(mis));
	}

	// 하나만 조회
	@GetMapping("/medicalId")
	public ResponseEntity<?> medicalCareEncCode(@RequestParam Long hospitalId) {
		return ResponseEntity.ok(hospitalService.findById(hospitalId));
	}
	
	// 병원 수 (스코어 카드)
	@GetMapping("/medicalCountHospital")
	public ResponseEntity<?> medicalCountHospital(MedicalScoreSearch mes) {
		return ResponseEntity.ok(hospitalService.getCountHospital(mes));
	}
	
	// 필수 의료 수 (스코어 카드)
	@GetMapping("/medicalEssential")
	public ResponseEntity<?> medicalEssential(MedicalScoreSearch mes) {
		return ResponseEntity.ok(hospitalService.getCountEssential(mes));
	}
	
	// 일요일/공휴일 진료 병원 수 (스코어 카드)
	@GetMapping("/medicalHoliday")
	public ResponseEntity<?> medicalHoliday(MedicalScoreSearch mes) {
		return ResponseEntity.ok(hospitalService.getCountHolidayOpen(mes));
	}
	
	// 야간 진료 병원 수 (스코어 카드)
	@GetMapping("/medicalNight")
	public ResponseEntity<?> medicalNight(MedicalScoreSearch mes) {
		return ResponseEntity.ok(hospitalService.getCountNightOpen(mes));
	}
	
	// 병원 유형 수 (차트)
	@GetMapping("/medicalType")  
	public ResponseEntity<?> medicalType(MedicalChartSearch mcs) {
		return ResponseEntity.ok(hospitalService.getTopNWithOthersByType(mcs));
	}
		
	// 진료 과목 수 (차트)
	@GetMapping("/medicalDept")
	public ResponseEntity<?> medicalDept(MedicalChartSearch mcs) {
		return ResponseEntity.ok(hospitalService.getTopNWithOthersByDept(mcs));
	}
	
	// 상세 페이지
}
