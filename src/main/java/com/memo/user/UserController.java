package com.memo.user;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/user")
public class UserController {

	/**
	 * 회원가입 화면
	 * @param model
	 * @return
	 */
	@RequestMapping("/sign_up_view")
	public String signUpView(Model model) {
		//뷰에서 어떤 값을 넣어주냐에 따라 화면이 달라진다.
		model.addAttribute("viewName", "user/sign_up");
		
		return "template/layout";
	}
	
	/**
	 * 로그인 화면
	 * @param model
	 * @return
	 */
	@RequestMapping("/sign_in_view")
	public String singInView(Model model) {
		
		model.addAttribute("viewName", "user/sign_in");
		
		return "template/layout";
	}
	
	
	/**
	 * form을 이용한 data이동(사용X)
	 * @param loginId
	 * @param name
	 * @param password
	 * @param email
	 * @return
	 */
	//form을 이용해서 redirect한다.
	@PostMapping("/sign_up_for_submit")
	public String signUpForSubmit(	//로그인 화면으로 리다이렉트 하는 게 이 Controller의 역할.
			@RequestParam("loginId") String loginId,
			@RequestParam("name") String name,
			@RequestParam("password") String password,
			@RequestParam("email") String email
			) { 
		// TODO: DB insert
		
		return "redirect:/user/sigh_in_view";
	}
	
	@RequestMapping("/sign_out")
	public String signOut(HttpServletRequest request) {
		// 로그아웃 - 세션에 있는 키값들을 모두 지운다.
		HttpSession session = request.getSession();
		session.removeAttribute("userLoginId");
		session.removeAttribute("userId");
		session.removeAttribute("userName");
		
		// 리다이렉트 -> 로그인화면
		return "redirect:/user/sign_in_view";
		
	}
	
}
