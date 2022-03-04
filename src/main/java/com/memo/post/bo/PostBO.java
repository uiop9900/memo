package com.memo.post.bo;

import java.io.IOException;
import java.util.Collections;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.memo.common.FileManagerService;
import com.memo.post.dao.PostDAO;
import com.memo.post.model.Post;

@Service
public class PostBO {
	
	//logback의 설정으로 사용가능하다. -import는 무조건 slf4j로 해야 오류 안난다.
	//private Logger logger = LoggerFactory.getLogger(PostBO.class);  둘이 동일코드이며 import는 무조건 slf4j로 해야한다.
	private Logger logger = LoggerFactory.getLogger(this.getClass()); 

	
	@Autowired
	private PostDAO postDAO;
	
	@Autowired
	private FileManagerService fileManager;
	
	private static final int POST_MAX_SIZE = 3;
	
	public List<Post> getPostListByUserId(int userId, Integer prevId, Integer nextId ) {
		// 10 9 8 | 7 6 5 | 4 3 2 | 1
		
		// 예를 들어 7 6 5 페이지에서
		// 1) 다음 눌렀을때 : nextId-5  => 5보다 작은 3개 => 4 3 2 DESC
		// 2) 이전 눌렀을때 : prevId-7 => 7보다 큰 3개 -> 8 9 10 ASC -> 코드안에서 데이터를 reverse 시킨다. 
		//    limit을 걸면 최댓값으로부터 3개의 수가 나온다.7보다 큰 3개 -> 120 119 118 -> ASC로 가져와야 한다.
		// 3) 첫 페이지로 들어왔을때 -> 10 9 8 DESC
		
		String direction = null;
		Integer standardId = null; //3번이 있어서 nullable
		
		if (nextId != null) { // 1) 다음 클릭
			direction = "next";
			standardId = nextId;
	
			return postDAO.selectPostListByUserId(userId, direction, standardId, POST_MAX_SIZE);
		
		} else if (prevId != null) { //2) 이전 클릭
			direction = "prev";
			standardId = prevId;
			
			List<Post> postList = postDAO.selectPostListByUserId(userId, direction, standardId, POST_MAX_SIZE);
			// 7보다 큰 3개 8 9 10이 나오므로 List를 reverse 정렬 시킨다.
			Collections.reverse(postList); //바뀌어져 있음
			return postList;
		}
		
		// 3) 첫페이지로 들어왔을때
		return postDAO.selectPostListByUserId(userId, direction, standardId, POST_MAX_SIZE);
	}
	
	
	public boolean isLastPage(int userId, int nextId) {
		//ASC limit 1, 최솟값
		// 아래 자체가 boolean
		return nextId == postDAO.selectPostByUserIdAndSort(userId, "ASC");
	}
	
	public boolean isFirstPage(int userId, int prevId) {
		//DESC limit 1, 최댓값
		return prevId == postDAO.selectPostByUserIdAndSort(userId, "DESC");
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
	
	public int updatePost(int postId, String loginId, int userId, 
			String subject, String content, MultipartFile file) {
		
		//db부화가 많이 일어나기때문에-> cash를 두면 select가 가볍다.
		logger.error("에러로깅테스트"); //찍히기만 하지 중지되지 않는다.
		
		//postId에 해당하는 게시글이 있는지 DB에서 가져와본다. -> select로 검증
		Post post = getPostByPostId(postId);
		if (post == null) { //server의 validation -> 없으면 update안해도 된다.
			logger.error("[update post] 수정 할 메모가 존재하지 않습니다." + postId); //로깅(기록해놓는다.)
			return 0; //이런일이 일어나면 안된다-> 기록해야한다. -> 로깅해야한다.
		}
		// getPostByPostId(int postId) - BO를 부른다:기본이 되는 메소드는 DB에 가깝고 더 아래이다.
		// postDAO.selectPostByPostId(postId) - DAO를 부른다: db에 가까운 값
		
		// 파일이 있는 지 확인 후, 있으면 서버에 업로드하고 imagePath를 받는다.
		// 파일이 없으면 수정하지 않는다.
		String imagePath = null;
		
		if (file != null) { //이미지가 있는 경우
			imagePath = fileManager.savefile(loginId, file); //컴퓨터에 파일 업로드 후 url Path를 얻어낸다.
			
			// 새로 업로드된 이미지가 '성공'하면 기존 이미지 삭제 -> 메모리 잡아먹고 있어서 삭제해야함(단, 기존이미지 있을 경우에)
			if (post.getImagePath() != null && imagePath != null) { //시존의 이미지가 있으면서 새로 업로드된 이미지가 성공했을떄
				// 기존 이미지 삭제 - fileManager에서 삭제하는 코드를 만든다.
				try {
					fileManager.deleteFile(post.getImagePath());//기존의 path이미지를 넣어줘야 한다.
				} catch (IOException e) {
					logger.error("[update post] 이미지 삭제 실패" +post.getId() + ", " + post.getImagePath()); //로깅
					//logger.error("[update post] 이미지 삭제 실패 {}, {} ", post.getId() , post.getImagePath()); //로깅
					
				} 
			}
		}
		
		// DB에서 update한다. - update하는 BO를 또 하나 만들어서 여기로 가지고 올수있다. -> 그렇겐 안한다. 이게 다 하나의 update과정
		
		return postDAO.updatePostByUserIdPostId(userId, postId, subject, content, imagePath);
	}
	
	public int deletePostByPostIdUserId(int postId, int userId) {
		//삭제 전에 게시글을 먼저 가져와본다. (imagePath가 있을수있기때문에)
		Post post = getPostByPostId(postId);
		
		if (post == null) {
			logger.warn("[delete post] 삭제한 메모가 존재하지 않습니다.");
			return 0;
		}
		
		//imagePath가 있는 경우, 파일 삭제
		//삭제시에는 내가 어떤 것들을 삭제하는가, 글만 지우나? 사진도 지우나?
		if (post.getImagePath() != null) {
			//기존 이미지 삭제
			try {
				fileManager.deleteFile(post.getImagePath());
			} catch (IOException e) {
				logger.error("[delete post] 이미지 삭제 실패" +post.getId() + ", " + post.getImagePath()); 
			}
		}
			
		//DB에서 post 삭제
		
		return postDAO.deletePostByUserIdPostId(postId, userId);
	}
	
	
	
	
	
}
