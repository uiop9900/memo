package com.memo.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import com.memo.interceptor.PermissionInterceptor;

@Configuration
public class WebMvcConfig implements WebMvcConfigurer {
	
	@Autowired
	private PermissionInterceptor interceptor;
	
	/**
	 * 웹의 이미지 주소와 실제 파일 경로를 매핑해주는 설정
	 */
	@Override
	public void addResourceHandlers(ResourceHandlerRegistry registry) {
		registry
		.addResourceHandler("/images/**") // http://localhost:8080/images/uiop9900_4257864557/sun.png
		.addResourceLocations("file:///C:\\JiaLEE\\6_spring-project\\memo\\imagePath/"); // 실제 파일 저장 위치
	}
	
	@Override
	public void addInterceptors(InterceptorRegistry registry) {
		registry
		.addInterceptor(interceptor)
		.addPathPatterns("/**") //**아래 디렉토리까지 확인, 모든 path를 다 검사한다.(손주까지)
		.excludePathPatterns("/static/**", "/error", "/user/sign_out"); //권한 검사 하지 않는 path(예외), 로그아웃할때는 예외해줘야한다.
		
	}
	
}
