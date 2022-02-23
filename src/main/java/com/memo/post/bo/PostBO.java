package com.memo.post.bo;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.memo.post.dao.PostDAO;
import com.memo.post.model.Post;

@Service
public class PostBO {
	
	@Autowired
	private PostDAO postDAO;
	
	public List<Post> getPostList() {
		return postDAO.selectPostList();
	}
	
	public void addPost(int userId, String userLoginId, String subject, String content, MultipartFile file) {//Bo입장으로 생각하기 -> userId 필수임 => int
		String imagePath = null; //최종적으로 db에 들어갈 imagePath, file이 null값일수있다.
		
		if (file != null) {
			
		}
		
		// insert DAO
	}
	
}
