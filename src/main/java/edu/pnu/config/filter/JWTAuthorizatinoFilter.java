package edu.pnu.config.filter;

import java.io.IOException;
import java.util.Optional;

import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.web.filter.OncePerRequestFilter;

import edu.pnu.domain.board.Member;
import edu.pnu.persistence.board.MemberRepository;
import edu.pnu.util.JWTUtil;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class JWTAuthorizatinoFilter extends OncePerRequestFilter {
	private final MemberRepository memberRepo;
	
	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {
		
		// 요청 헤더에서 jwt 얻기
		String jwtToken = request.getHeader(HttpHeaders.AUTHORIZATION);
		System.out.println("Authorization = " + request.getHeader("Authorization"));
		if (jwtToken == null || !jwtToken.startsWith(JWTUtil.prefix)) {
			// 없거나 Bearer 시작 아니면 필터 그냥 통과
			filterChain.doFilter(request, response);
			return;
		}
		// 토큰에서 username 추출
		String username = JWTUtil.getClaim(jwtToken, JWTUtil.usernameClaim);
		Optional<Member> opt = memberRepo.findById(username);
		// 토큰에서 얻은 username으로 db 검색
		if (!opt.isPresent()) {
			// 사용자 없으면 필터 그냥 통과
			filterChain.doFilter(request, response);
			return;
		}
		Member member = opt.get();
		// UserDetails 타입 객체 생성
		User user = new User(member.getUsername(), member.getPassword(),
				AuthorityUtils.createAuthorityList(member.getRole().toString()));
		// 인증 객체 생성
		Authentication auth = new UsernamePasswordAuthenticationToken(user, null, user.getAuthorities());
		// SecurityContext에 등록
		SecurityContextHolder.getContext().setAuthentication(auth);
		System.out.println(SecurityContextHolder.getContext().getAuthentication());
		// SecurityFilterChain의 다음 필터로 이동
		filterChain.doFilter(request, response);
	}
}
