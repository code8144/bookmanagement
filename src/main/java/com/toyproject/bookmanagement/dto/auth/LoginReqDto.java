package com.toyproject.bookmanagement.dto.auth;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

import lombok.Data;

@Data
public class LoginReqDto {
	
	@Email // 이메일 형식이 아니면 받아들이지 않음
	@NotBlank
	private String email;

	@Pattern(regexp = "^(?=.*[A-Za-z])(?=.*\\d)(?=.*[@$!%*#?&])[A-Za-z\\d@$!%*#?&]{8,16}$", message = "비밀번호는 영문자, 숫자, 특수문자를 포함하여 8~ 16자로 작성") // ->정규식
	private String password;
	
	

}
