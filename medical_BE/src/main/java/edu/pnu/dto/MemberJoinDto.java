package edu.pnu.dto;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MemberJoinDto {
	@NotBlank(message = "아이디는 필수 입력 항목입니다.")
	@Pattern(regexp = "^[a-z0-9]{5,20}$", message = "아이디는 영문 소문자와 숫자 조합으로 5~20자여야 합니다.")
	private String username;
	
	@NotBlank(message = "비밀번호는 필수 입력 항목입니다.")
	@Pattern(regexp = "^(?=.*[a-zA-Z])(?=.*[!@#$%^*+=-])(?=.*[0-9]).{8,20}$", 
    message = "비밀번호는 영문, 숫자, 특수문자를 포함하여 8~20자여야 합니다.")
	private String password;
	
	@NotBlank(message = "닉네임은 필수 입력 항목입니다.")
	private String alias;
}
