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
				<img src="${post.imagePath}" alt="upload image" class="image-area" width="150" height="150">
			</div>
		</c:if>
		
	
		<div class="mt-3">
			<button type="button" id="postDeleteBtn" class="btn btn-secondary float-left" data-post-id="${post.id}">삭제</button>
			
			<div class="float-right">
				<button type="button" id="postListBtn" class="btn btn-dark">목록</button>
				<button type="button" id="saveBtn" class="btn btn-primary ml-3" data-post-id="${post.id}">저장</button>
			</div>
		</div>
	</div>
</div>

<script>
$(document).ready(function(){
	
	//목록 버튼 클릭
	$("#postListBtn").on('click', function(){
		location.href="/post/post_list_view"	
	});
	
	//수정
	$("#saveBtn").on('click', function(){
		//a태그면 preventDefalut로 화면 올라가는거 막기
		//validation check
		
		let subject = $("#subject").val().trim();
		if (subject == ""){
			alert("제목을 입력해주세요.");
			return;
		}
		
		let content = $("#content").val(); //nullable
		
		//file의 확장자 체크
		let file = $("#file").val(); //이 자체가 파일이름, 파일 경로만 가지고 오는 것임
		if (file != "") {
			let ext = file.split('.').pop().toLowerCase(); 
			if ($.inArray(ext, ['jpg', 'gif', 'png', 'jpeg']) == -1) {
				alert("gif, png, jpg, jepg 파일만 업로드 할 수 있습니다.");
				$("#file").val(""); // 파일을 비운다.
				return;
			}
		}		
		
		
		//폼태그 객체를 자바스크립트에서 만든다.
		let formData = new FormData();
		let postId = $(this).data('post-id');
		formData.append("postId", postId);
		formData.append("subject", subject);
		formData.append("content", content);
		formData.append("file", $("#file")[0].files[0]);
		
		//AJAX
		$.ajax({
			type: "PUT"
			, url: "/post/update"
			, data: formData
			, enctype: "multipart/form-data" //파일 업로드를 위한 필수설정
			, processData: false //파일 업로드를 위한 필수설정
			, contentType: false //파일 업로드를 위한 필수설정
			, success: function(data) {
				if(data.result == 'success') {
					alert("수정이 완료되었습니다.");
					location.reload(); //새로고침
				} else {
					alert(data.errorMessage);
				}
			}
			, error: function(e) {
				alert("메모저장에 실패했습니다. 관리자에게 문의해주세요.");
			}
		});
		
	});
	
	
	//삭제
	$("#postDeleteBtn").on('click', function(e){
		let postId = $(this).data("post-id");

		$.ajax({
			type:"delete"
			, url: "/post/delete"
			, data: {'postId':postId}
			, success: function(data) {
				if (data.result == 'success') {
					alert("삭제성공");
					location.href="/post/post_list_view"
				} else {
					alert(data.errorMessage);
				}
			}
			, error: function(e) {
				alert("메모를 삭제하는데 실패했습니다. 관리자에게 문의해주세요.");
			}
		});
	
	});
});
</script>



