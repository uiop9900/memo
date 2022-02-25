package com.memo.post;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.memo.post.bo.PostBO;
import com.memo.post.model.Post;

@Controller
@RequestMapping("/post")
public class PostController {

	@Autowired
	private PostBO postBO;
	
	/**
	 * 게시글쓰기 화면
	 * @param model
	 * @return
	 */
	@RequestMapping("/post_create_view")
	public String postCreateView(Model model) {
		
		model.addAttribute("viewName", "post/post_create_view");
		
		return "template/layout";
	}
	
	
	/**
	 * 글목록 화면
	 * @param model
	 * @return
	 */
	@RequestMapping("/post_list_view")
	public String postListView(Model model, HttpServletRequest request) {
		//글쓴이 정보를 가져오기 위해 세션에서 userId를 꺼낸다.
		HttpSession session = request.getSession();
		int userId = (int)session.getAttribute("userId"); 
		
		// TODO: 글목록 DB에서 가지고 오기
		List<Post> postList = postBO.getPostListByUserId(userId);
		
		//글목록들이 나와야 하기에 model에 넣어서 보낸다. -> api 필요없음
		model.addAttribute("postList", postList); //너무 리스트가 커지면 한꺼번에 가져오기 힘들다 -> 페이징
		model.addAttribute("viewName", "post/post_list_view");
		
		return "template/layout";
	}
	
	
	@RequestMapping("/post_detail_view")
	public String postDetailView(
			@RequestParam("postId") int postId,
			Model model) {
		
		//postId에 해당하는 글 하나를 가지고 온다.(userId까지 해서 가지고 와도 된다 - 더블체크)
		Post post = postBO.getPostByPostId(postId);
		
		model.addAttribute("post", post);
		model.addAttribute("viewName", "post/post_detail_view");
		return "template/layout";
	}
	
} 
