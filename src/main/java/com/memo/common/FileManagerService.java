package com.memo.common;

import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

@Component //스프링빈
public class FileManagerService {

	
	public final static String FILE_UPLOAD_PATH = "D:\\이지아\\6_spring-project\\memo\\workspace\\images/"; //상수일때는 대문자로 메소드명, 꼭 마지막 /로!
	
	// 파일을 받아서 경로로 리턴한다.
	public String savefile(String userLoginId, MultipartFile file) { //사용자에 따라 user별으의 폴더가 생긴다.
		// 파일 디렉토리 경로 예: uiop9900_4257864557/sun.png (timestamp -> int 변환)
		// 파일명이 겹치지않게 하기위해서 현재시간을 경로에 붙여준다.
		
		String directoryName = userLoginId + "_" + System.currentTimeMillis() + "/"; 
		String filePath = FILE_UPLOAD_PATH + directoryName;
		//D:\\이지아\\6_spring-project\\memo\\workspace\\images/  uiop9900_4257864557/sun.png 이거때문에 /붙인것임
		
		return "";
	}
	
	
}
