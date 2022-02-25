<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<div class="d-flex justify-content-center my-5">
	<div class="w-50">
		<h2>게시판 글 상세/수정</h2>
		<input type="text" id="subject" class="form-control" placeholder="제목을 입력해주세요." value="${post.subject}">
		<textarea id="content" class="form-control mt-2 mb-4" rows="10" placeholder="내용을 입력해주세요.">${post.content}</textarea>

		<div class="clearfix">
			<input id="file" type="file" class="float-right" accept=".jpg,.png,.gif,.jpeg"> <%--여기서 한번 썼기에 아래에서 clearfix쓰고 다시 쓴다. --%>
		</div>
		
		<%--이미지가 있을때만 이미지 영역추가 --%>
		<c:if test="${not empty post.imagePath}">
			<div class="mb-3">
				<img src="${post.imagePath}" alt="upload image" class="image-area" width="400" height="100">
			</div>
		</c:if>
		
	
		<div class="mt-3">
			<button type="button" id="postDeleteBtn" class="btn btn-secondary float-left">삭제</button>
			
			<div class="float-right">
				<button type="button" id="postListBtn" class="btn btn-dark">목록</button>
				<button type="button" id="saveBtn" class="btn btn-primary ml-3">저장</button>
			</div>
		</div>
	</div>
</div>

<script>

</script>



