package com.memo.user;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.memo.common.EncryptUtils;
import com.memo.user.bo.UserBO;
import com.memo.user.model.User;

@RestController
@RequestMapping("/user") //절대 주소가 겹치면 안된다.
public class UserRestController {

	@Autowired
	private UserBO userBO;
	
	/**
	 * 회원가입 - 아이디 중복 확인
	 * @param loginId
	 * @return
	 */
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
	
	/**
	 * 회원가입 기능 - ajax로 호출
	 * @param loginId
	 * @param password
	 * @param name
	 * @param email
	 * @return
	 */
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
	
	
	@PostMapping("/sign_in")
	public Map<String, Object> signIn(
			@RequestParam("loginId") String loginId,
			@RequestParam("password") String password,
			HttpServletRequest request
			) {
		
		// 비밀번호 암호화
		String encryptPassword = EncryptUtils.md5(password);
		
		// loginId, 암호화 비번으로 DB에서 select
		User user = userBO.getUserbyLoginIdPassword(loginId, encryptPassword);
		
		Map<String, Object> result = new HashMap<>();
		result.put("result", "success");
		
		// 로그인이 성공하면 세션에 User정보를 담는다.
		if (user != null) {
			//로그인 - 세션에 저장(로그인 상태 유지한다.)
			HttpSession session = request.getSession();
			session.setAttribute("userLoginId", user.getLoginId()); //key, 값 - 기억해야할 값을 저장해두기 
			//-> 정말 필요한것만 저장 sessin의 id값으로 user의 다른값들을 db에서 꺼내는 식으로 쓴다. 모든 값들을 다 넣으면 안된다.
			session.setAttribute("userId", user.getId());
			session.setAttribute("userName", user.getName()); //어디에서든 가져다 사용할수있다.
		} else {
			// 로그인 실패 - 에러
			result.put("errorMessage", "존재하지 않는 사용자입니다. 관리자에게 문의해주세요.");
		}
		
		// 결과 json 리턴
		
		return result;
	}
	
	
	
	
	
	
}
