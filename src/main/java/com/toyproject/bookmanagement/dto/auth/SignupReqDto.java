package com.toyproject.bookmanagement.dto.auth;

import javax.validation.constraints.Email;
import javax.validation.constraints.Pattern;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import com.toyproject.bookmanagement.entity.User;

import lombok.Data;

/*
 * 정규식 ^: 정규식 시작을 의미, $:정규식 끝을 의미, 일치하는 것이 없어도 다음으로 넘어감 
	 (?=) : 그룹 정규식 , 앞쪽에 있는 것들과 일치하는지를 확인 
 *  *은 있거나 없거나를 의미 = 모든 글자가 여러개 있거나 없거나를 의미
 *  ?=.*[A-Za-z]) = 전체글자 중에서 A 부터 Z까지를 의미
 *  ?=.*\\d = 0~9까지, 모든 숫자를 의미
 *  ?=.*[@$!%*#?&] = 특수문자, 이중에 하나를 포함해야한다
 *  [A-Za-z\\d@$!%*#?&]= 내가 허용한 문자 및 숫자들 , 이것외에 다른 것들은 안받음 
 *  {8,16}  = 8자 이상 16자 미만으로 작성 
 */

@Data
public class SignupReqDto {

	@Email // 이메일 형식이 아니면 받아들이지 않음
	private String email;

	@Pattern(regexp = "^(?=.*[A-Za-z])(?=.*\\d)(?=.*[@$!%*#?&])[A-Za-z\\d@$!%*#?&]{8,16}$", message = "비밀번호는 영문자, 숫자, 특수문자를 포함하여 8~ 16자로 작성") // ->정규식
	private String password;

	@Pattern(regexp = "^[가-힣]{2,7}$", message="이름은 한글이름만 작성 가능합니다") // 한글외에는 받지 않겠다, 글자는 2~7글자까지 허용하겠다는 것을 의미
	private String name;
	
	public User toEntity() {
		
		return  User.builder()
				.email(email)
				.password(new BCryptPasswordEncoder().encode(password))
				.name(name)
				.build();
	}

}
