package com.memo.post.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;
import org.springframework.web.multipart.MultipartFile;

import com.memo.post.model.Post;

@Repository
public interface PostDAO {
	public List<Post> selectPostListByUserId(int userId);
	
	public Post selectPostByPostId(int postId);
	
	public void insertPost(
			@Param("userId") int userId, //필수값: 누가 썼는지
			@Param("subject") String subject, 
			@Param("content") String content, 
			@Param("imagePath") String imagePath);
	
	public int updatePostByUserIdPostId( // 이 두개의 조건이 맞아야 update한다. 더블체크를 위해 두개로 설정
			@Param("userId") int userId, 
			@Param("postId") int postId, 
			@Param("subject") String subject, 
			@Param("content") String content, 
			@Param("imagePath") String imagePath);
}
