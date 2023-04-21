package com.toyproject.bookmanagement.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebMvcConfig implements WebMvcConfigurer{	// WebMvcConfigurer가 CrossOrigin의 어노테이션 역할을 대신해줌(각각 CrossOrigin을 달아 줄 필요X)
	@Override
	public void addCorsMappings(CorsRegistry registry) {
		registry.addMapping("/**")
			.allowedMethods("*")
			.allowedOrigins("*");
//			.allowedOrigins("http://localhost:3000");
			
	}
}
