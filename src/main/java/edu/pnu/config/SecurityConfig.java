package edu.pnu.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

import lombok.RequiredArgsConstructor;

@Configuration
@RequiredArgsConstructor
public class SecurityConfig {
	
	@Bean
	PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}
	
	@Bean
	SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
		// csrf 보호 비활성화
		http.csrf(csrf -> csrf.disable());
		
		// 접근 권한 -> 인가
		http.authorizeHttpRequests(auth -> auth
				.requestMatchers("/medicalInfo/**").authenticated()
				.requestMatchers("/medicalSigungu/**").authenticated()
				.anyRequest().permitAll());
		
		// 로그인 시
		http.formLogin(form -> form.loginPage("/").loginProcessingUrl("/login").defaultSuccessUrl("/medicalInfo").permitAll());
		
		// 로그아웃 시
		http.logout(logout -> logout.invalidateHttpSession(true).deleteCookies("JSESSIONID")
				.logoutSuccessUrl("/").permitAll());
		
		return http.build();
	}
}
