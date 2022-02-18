package com.memo.post;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.memo.post.bo.PostBO;
import com.memo.post.model.Post;

@RestController
public class PostRestController {

	@Autowired
	private PostBO postBO;
	
	//http:/localhost:8080/posts
	@RequestMapping("/posts")
	public List<Post> posts(){
		
		List<Post> postList = postBO.getPostList();
		
		return postList;
		
	}
}
