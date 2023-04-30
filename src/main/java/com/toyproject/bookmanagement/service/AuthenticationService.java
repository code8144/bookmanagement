package com.toyproject.bookmanagement.service;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.toyproject.bookmanagement.dto.auth.JwtRespDto;
import com.toyproject.bookmanagement.dto.auth.LoginReqDto;
import com.toyproject.bookmanagement.dto.auth.PrincipalRespDto;
import com.toyproject.bookmanagement.dto.auth.SignupReqDto;
import com.toyproject.bookmanagement.entity.Authority;
import com.toyproject.bookmanagement.entity.User;
import com.toyproject.bookmanagement.exception.CustomException;
import com.toyproject.bookmanagement.exception.ErrorMap;
import com.toyproject.bookmanagement.repository.UserRepository;
import com.toyproject.bookmanagement.security.JwtTokenProvider;

import io.jsonwebtoken.Claims;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AuthenticationService implements UserDetailsService {

	private final UserRepository userRepository;
	private final AuthenticationManagerBuilder authenticationManagerBuilder;
	private final JwtTokenProvider jwtTokenProvider;

	public void checkDuplicatedEmail(String email) {
		if (userRepository.findUserByEmail(email) != null) {
			throw new CustomException("Duplicated Email", ErrorMap.builder().put("email", "사용중인 이메일입니다").build());

		}
	}

	public void signup(SignupReqDto SignupReqDto) {
		User userEntity = SignupReqDto.toEntity();
		userRepository.saveUser(userEntity);
		userRepository.saveAuthority(Authority.builder().userId(userEntity.getUserId()).roleId(1).build());

	}
	//매니저가 알아볼수 있도록 만들어준다
	public JwtRespDto signin(LoginReqDto loginReqDto) {
		UsernamePasswordAuthenticationToken authenticationToken = new  UsernamePasswordAuthenticationToken(loginReqDto.getEmail(), loginReqDto.getPassword());
		
		Authentication authentication = authenticationManagerBuilder.getObject().authenticate(authenticationToken); // 암호화 안된 비밀번호
		
		return jwtTokenProvider.generateToken(authentication);
		
	}
	
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException{
		User userEntity = userRepository.findUserByEmail(username);
		
		if(userEntity == null) {
			throw new CustomException("로그인 실패", ErrorMap.builder().put("email", "사용자 정보를 확인하세요").build());
		}
		return userEntity.toPrincipal();
	}
	
	public boolean authenticated(String accessToken) {
		return jwtTokenProvider.validateToken(jwtTokenProvider.getToken(accessToken));
	}
	public PrincipalRespDto getPrincipal(String accessToken) {
		Claims claims = jwtTokenProvider.getClaims(jwtTokenProvider.getToken(accessToken));
		User userEntity = userRepository.findUserByEmail(claims.getSubject()); // subject =email
		
		return PrincipalRespDto.builder()
					.userId(userEntity.getUserId())
					.email(userEntity.getEmail())
					.name(userEntity.getName())
					.authorities((String)claims.get("auth"))  
					.build();
	}


}
//메니저가 데이터베이스에서 가져온 정보와 우리가 넣어준 정보를 비교하면 principal객체를 authentication객체로 변환해서 넣어줌 이것은 토큰을 만들때 넣어줌
// accesstoken을 만들때 authentication getname은 authentication principal에서 들고옴 ->subject에다가 등록
// claims를 가지고 올때 getsubject에서 email을 들고옴 
