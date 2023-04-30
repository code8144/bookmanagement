package com.toyproject.bookmanagement.aop;

import java.util.HashMap;
import java.util.Map;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.BindingResult;

import com.toyproject.bookmanagement.exception.CustomException;

@Aspect
@Component
public class ValidationAop {

	@Pointcut("@annotation(com.toyproject.bookmanagement.aop.annotation.ValidAspect)")
	private void pointCut() {
	}

	@Around("pointCut()")
	public Object around(ProceedingJoinPoint joinPoint) throws Throwable {

		Object[] args = joinPoint.getArgs(); //재사용을 하기위해서 변수에 담아줌 
		BindingResult bindingResult = null;

		for (Object arg : args) { // 반복을 돌려서 bindingResult 객체찾기, 1번째 arg는 dto 2번때 arg는 binding
			if (arg.getClass() == BeanPropertyBindingResult.class) {
				bindingResult = (BeanPropertyBindingResult) arg; // 다운캐스팅

			}
		}
		// bindingresult에 에러에 대한 정보들이 담겨있음 
		if (bindingResult.hasErrors()) {
			Map<String, String> errorMap = new HashMap<>(); //비어있는 맵을 만들어서 key, value를 가지고 띄워줄 예외 메세지 구현 
			bindingResult.getFieldErrors().forEach(error -> {
				errorMap.put(error.getField(), error.getDefaultMessage());
			});
			throw new CustomException("Validation Failed", errorMap);

		}
		return joinPoint.proceed();
	}

}
