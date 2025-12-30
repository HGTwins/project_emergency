package edu.pnu.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

// 페이지(html) 처리 컨트롤러

@Controller
public class MedicalController {
	// 로그인
	@GetMapping("/")
	public String index() {
		return "index";
	}

	@GetMapping("/medicalSigungu")
	public String medicalSigunguPage() {
		return "medicalSigungu";
	}
}
