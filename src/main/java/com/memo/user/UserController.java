package com.memo.user;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/user")
public class UserController {

	@RequestMapping("/sign_up_view")
	public String signUpView(Model model) {
		//뷰에서 어떤 값을 넣어주냐에 따라 화면이 달라진다.
		model.addAttribute("viewName", "user/sign_up");
		
		return "template/layout";
	}
	
	@RequestMapping("/sign_in_view")
	public String singInView(Model model) {
		
		model.addAttribute("viewName", "user/sign_in");
		
		return "template/layout";
	}
	
}
