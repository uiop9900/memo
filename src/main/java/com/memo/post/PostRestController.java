package com.memo.post;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.memo.post.bo.PostBO;
import com.memo.post.model.Post;

@RequestMapping("/post")
@RestController
public class PostRestController {

	@Autowired
	private PostBO postBO;
	
	//테스트용 컨트롤러
	@RequestMapping("/posts")
	public List<Post> posts(){
		List<Post> postList = postBO.getPostList();
		return postList;
		
	}
	
	@PostMapping("/create")
	public Map<String, Object> create(
			@RequestParam("subject") String subject,
			@RequestParam(value="content", required = false) String content,
			@RequestParam(value="file", required = false) MultipartFile file, //파일 통째로 객체로 넘어온다.
			HttpServletRequest request ){
		
		Map<String, Object> result = new HashMap<>();
		result.put("result", "success");

		// 글쓴이 정보를 가져온다. (세션에서)
		HttpSession session = request.getSession();
		Integer userId = (Integer) session.getAttribute("userId"); //아직 권한검사 안하기 떄문에 null값이 있을수도 있다. 그래서 Integer로 캐스팅, int안됨(null값 못받음)
		String loginId = (String) session.getAttribute("userLoginId");
		
		if (userId == null) { //로그인 되어있지않음 -> 로그인이 필수임
			result.put("result", "error");
			result.put("errorMessage", "로그인 후 사용가능합니다.");
			return result;
		}

		// 로그인된 상태 - userId, userLoginId, subject, content, file 보낸다. -> insert요청 (userLoginId는 Bo는 필요하지만 DAO는 안필요해서 DAO까지는 안가도 된다)
		
		
		return result;
	}
	
}
