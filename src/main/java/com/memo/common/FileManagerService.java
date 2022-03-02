package com.memo.common;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

@Component //스프링빈-> autowired로 실행가능
public class FileManagerService {

	public final static String FILE_UPLOAD_PATH = "C:\\JiaLEE\\6_spring-project\\memo\\imagePath/"; //상수일때는 대문자로 메소드명, 꼭 마지막 /로!
	
	// 파일을 받아서 경로로 리턴한다.
	public String savefile(String userLoginId, MultipartFile file) { //사용자에 따라 user별으의 폴더가 생긴다.
		// 파일 디렉토리 경로 예: uiop9900_4257864557/sun.png (timestamp -> int 변환)
		// 파일명이 겹치지않게 하기위해서 현재시간을 경로에 붙여준다.
		
		String directoryName = userLoginId + "_" + System.currentTimeMillis() + "/"; //uiop9900_4257864557/
		String filePath = FILE_UPLOAD_PATH + directoryName;
		//D:\\이지아\\6_spring-project\\memo\\workspace\\images/  uiop9900_4257864557/ 이거때문에 /붙인것임
		
		// 디렉초리 만들기(폴더 만들기)
		File directory = new File(filePath);//기본제공하는 file기본 class
		 if (directory.mkdir() == false) { //디렉토리를 진짜 만들어낸다. -> return boolean
			 return null; // 디렉토리 생성시 실패하면 null을 리턴
		 } 
		 
		 // 파일 업로드 : byte단위로 업로드 한다.
		 try {
			byte[] bytes = file.getBytes();  //requst에서 넘어온 file임
			Path path = Paths.get(filePath + file.getOriginalFilename()); // getOriginalFilename()는 input에 올린 파일명이다.(한글이면 작동이 안될수 있다.)
			Files.write(path, bytes);
			
			// 이미지 url을 리턴한다. (WebMvcConfig에서 매핑한 이미지 path)
			// 예) http://localhost:8080/images/uiop9900_4257864557/sun.png
			return "/images/" + directoryName + file.getOriginalFilename(); //directoryName에 이미 / 들어가있음
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return null;
	}
	
	
	// 삭제하는 메소드
	// input: 경로값(imagePath), output:void
	public void deleteFile(String imagePath) {
		// imagePath의 /images/uiop9900_4257864557/sun.png의 /images/를 제거한 path를 실제 저장경로 뒤에 붙인다.
		Path path = Paths.get(FILE_UPLOAD_PATH + imagePath.replace("/images/", ""));
		
		if (Files.exists(path)) { //이미지 파일이 있으면 삭제 - 없는 거 예외처리
			try {
				Files.delete(path);
			} catch (IOException e) {
				e.printStackTrace();// 로깅해준다.
			}
		}
		//디렉토리(폴더) 삭제
		path = path.getParent(); //이미지의 부모 = 폴더
		if (Files.exists(path)) { //폴더가 있으면 삭제한다.
			try {
				Files.delete(path);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	
}




