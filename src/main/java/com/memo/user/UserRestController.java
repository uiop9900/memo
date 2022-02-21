package com.memo.user;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.memo.common.EncryptUtils;
import com.memo.user.bo.UserBO;

@RestController
@RequestMapping("/user") //절대 주소가 겹치면 안된다.
public class UserRestController {

	@Autowired
	private UserBO userBO;
	
	@RequestMapping("/is_duplicated_id")
	public Map<String, Object> idDuplicatedId(
			@RequestParam("loginId") String loginId
			) {
		
		Map<String, Object> result = new HashMap<>();
		// 1. count로 가져와서 값이 있으면 true
		// 2. 객체를 가지고 와서 비어있는지 아닌지
		boolean existLoginId = userBO.existLoginId(loginId);
		result.put("result", existLoginId); //id가 이미 존재하면 true
		
		return result;
	}
	
	
	@PostMapping("/sign_up")
	public Map<String, Object> signUp(	
			@RequestParam("loginId") String loginId,
			@RequestParam("password") String password,
			@RequestParam("name") String name,
			@RequestParam("email") String email
			) { 
		
		//비밀번호 암호화
		String encryptPassword = EncryptUtils.md5(password);
		
		// TODO: DB insert
		int row = userBO.addUser(loginId, encryptPassword, name, email);
		
		Map<String, Object> result = new HashMap<>();
		result.put("result", "success");

		if (row < 1) {
			result.put("result", "error");
		}
		
		return result;
	}
	
}
