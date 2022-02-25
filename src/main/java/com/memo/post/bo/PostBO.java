package com.memo.post.bo;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.memo.common.FileManagerService;
import com.memo.post.dao.PostDAO;
import com.memo.post.model.Post;

@Service
public class PostBO {
	
	@Autowired
	private PostDAO postDAO;
	
	@Autowired
	private FileManagerService fileManager;
	
	public List<Post> getPostListByUserId(int userId) {
		return postDAO.selectPostListByUserId(userId);
	}
	
	public Post getPostByPostId(int postId) {
		return postDAO.selectPostByPostId(postId);
	}
	
	public void addPost(int userId, String userLoginId, String subject, String content, MultipartFile file) {//Bo입장으로 생각하기 -> userId 필수임 => int
		String imagePath = null; //최종적으로 db에 들어갈 imagePath, file이 null값일수있다.
		
		if (file != null) {
			// TODO: 파일매니저 서비스  input: MultipartFile  output: 이미지의 주소 -> 실패하면 null이 반환
			imagePath = fileManager.savefile(userLoginId, file);
			
		}
		
		// insert DAO
		postDAO.insertPost(userId, subject, content, imagePath);
		
	}
	
	
}
