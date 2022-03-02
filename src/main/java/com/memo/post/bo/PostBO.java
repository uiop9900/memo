package com.memo.post.bo;

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
				fileManager.deleteFile(post.getImagePath()); //기존의 path이미지를 넣어줘야 한다.
			}
		}
		
		// DB에서 update한다. - update하는 BO를 또 하나 만들어서 여기로 가지고 올수있다. -> 그렇겐 안한다. 이게 다 하나의 update과정
		
		return postDAO.updatePostByUserIdPostId(userId, postId, subject, content, imagePath);
	}
	
}
