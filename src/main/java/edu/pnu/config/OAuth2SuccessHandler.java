package edu.pnu.config;

import java.io.IOException;
import java.util.Map;

import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;

import edu.pnu.util.JWTUtil;
import jakarta.servlet.http.HttpServletResponse;

public class OAuth2SuccessHandler extends SimpleUrlAuthenticationSuccessHandler {
	// OAuth2 인증 정보에서 필요한 정보(provider, email)을 추출하는 메서드
	@SuppressWarnings("unchecked")
	Map<String, String> getUserInfo(Authentication authentication) {
		OAuth2AuthenticationToken oAuth2Token = (OAuth2AuthenticationToken)authentication;
		
		String provider = oAuth2Token.getAuthorizedClientRegistrationId();
		// System.out.println("[OAuth2SuccessHandler]Provider:" + provider);
		
		OAuth2User user = (OAuth2User)oAuth2Token.getPrincipal();
		String email = "unknown";
		
		if (provider.equalsIgnoreCase("naver")) {
			email = (String)((Map<String, Object>)user.getAttribute("response")).get("email");
		} else if (provider.equalsIgnoreCase("google")) {
			email = (String)user.getAttributes().get("email");
		} 
		// System.out.println("[OAuth2SucccessHandler]email:" + email);
		return Map.of("provider", provider, "email", email);
	}
	
	// JWT를 추가하는 메서드
	void sendJWTtoClient(HttpServletResponse response, String token) throws IOException {
		// System.out.println("[OAuth2SuccessHandler]token:" + token);
		String pureToken = token.replaceAll(JWTUtil.prefix, "").trim();
	    
	    // 쿠키를 굽지 않고, URL 파라미터로 토큰을 던져 주기
	    String targetUrl = "http://localhost:3000/callback?token=" + pureToken;
	    
	    response.sendRedirect(targetUrl);
	}
}
