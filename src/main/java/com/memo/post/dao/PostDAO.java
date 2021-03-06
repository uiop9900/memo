package com.memo.post.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;
import org.springframework.web.multipart.MultipartFile;

import com.memo.post.model.Post;

@Repository
public interface PostDAO {
	public List<Post> selectPostListByUserId(
			@Param("userId") int userId, 
			@Param("direction") String direction, //direction이 null이면 첫페이지
			@Param("standardId") Integer standardId, 
			@Param("limit") int limit);
	
	public int selectPostByUserIdAndSort(
			@Param("userId")int userId, 
			@Param("sort") String sort);
	
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
	
	public int deletePostByUserIdPostId( //순서 주의
			@Param("postId") int postId, 
			@Param("userId") int userId);
}
