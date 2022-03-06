package com.memo.post;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.CollectionUtils;
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
	public String postListView(
			@RequestParam(value="prevId", required=false) Integer prevIdParam, //이전과 다음 그 둘 중 하나만 들어오기때문에 null허용 
			@RequestParam(value="nextId", required=false) Integer nextParam,
			Model model, 
			HttpServletRequest request) {
		//글쓴이 정보를 가져오기 위해 세션에서 userId를 꺼낸다.
		HttpSession session = request.getSession();
		int userId = (int)session.getAttribute("userId"); 
		
		// TODO: 글목록 DB에서 가지고 오기
		List<Post> postList = postBO.getPostListByUserId(userId, prevIdParam, nextParam);
		
		int prevId = 0;
		int nextId = 0;
		if (CollectionUtils.isEmpty(postList) == false) { //postList가 없는 경우의 에러방지, null체크까지 한다. -> empty체크때 제일 안전
			//post하나가 들어오기때문에 getId해야한다.
			prevId = postList.get(0).getId(); //리스트 중 가장 앞쪽( 제일 큰값) id
			nextId = postList.get(postList.size() - 1).getId(); // 리스트 중 가장 뒷쪽(제일 작은 값)id, 마지막 인덱스 -> 마지막값은 3이하 일수도 있기떄문에 size로 체크한다.
		
			// 이전이나 다음이 없는 경우 nextId, prevId를 0으로 세팅한다.
			
			// 마지막 페이지(다음방향의 끝:1) => nextId = 0
			if (postBO.isLastPage(userId, nextId)) { //내가 쓴 글중에서
				nextId = 0;
			}
			// 첫 페이지(이전방향의 끝: 제일 최신) => prevId = 0
			if (postBO.isFirstPage(userId, prevId)) {
				prevId = 0;
			}
		}
		
		
		
		//글목록들이 나와야 하기에 model에 넣어서 보낸다. -> api 필요없음
		model.addAttribute("postList", postList); //너무 리스트가 커지면 한꺼번에 가져오기 힘들다 -> 페이징
		model.addAttribute("prevId", prevId);
		model.addAttribute("nextId", nextId);
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
