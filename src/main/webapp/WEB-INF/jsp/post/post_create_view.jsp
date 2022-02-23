<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<div class="d-flex justify-content-center my-5">
	<div class="w-50">
		<h2>게시판 글쓰기</h2>
		<input type="text" id="subject" class="form-control" placeholder="제목을 입력해주세요.">
		<textarea id="content" class="form-control mt-2 mb-4" rows="10" placeholder="내용을 입력해주세요."></textarea>

		<div class="clearfix">
			<input id="file" type="file" class="float-right" accept=".jpg,.png,.gif,.jpeg"> <%--여기서 한번 썼기에 아래에서 clearfix쓰고 다시 쓴다. --%>
		</div>
	
		<div class="mt-3">
			<button type="button" id="postListBtn" class="btn btn-dark float-left">목록</button>
			
			<div class="float-right">
				<button type="button" id="clearBtn" class="btn btn-secondary">모두 지우기</button>
				<button type="button" id="saveBtn" class="btn btn-primary ml-3">저장</button>
			</div>
		</div>
	</div>
</div>

<script>
$(document).ready(function(e){
	//목록 버튼 클릭-> 글 목록으로 이동
	$("#postListBtn").on('click', function(e){
		location.href="/post/post_list_view"; //button으로 만들게 이렇게 화면을 전환할수도 있다
	});
	
	// 모두 지우기
	$("#clearBtn").on('click', function(e){
		// 제목과 내용 부분을 빈칸으로 만든다.
		$("#subject").val("");
		$("#content").val("");
	})
	
	// 글 내용 저장
	$("#saveBtn").on('click', function(e){
		
		//validation - subject만 not null
		let subject = $("#subject").val().trim();
		
		if (subject == "") {
			alert("제목을 입력하세요.");
			return;
		}		
		
		let content = $("#content").val();
		
		// 파일이 업로드 된 경우, 확장자 체크(not null)
		let file = $("#file").val(); //이 자체가 파일이름, 파일 경로만 가지고 오는 것임
		if (file != "") {
			let ext = file.split('.').pop().toLowerCase(); // 파일경로는 .으로 나누고 확장자가 있는 마지막 문자열을 가지고 온 후 모두 소문자로 변경 
			// -> stack용어, pop: 제일상단을 뺀다.쌓는건 push, extension(확장자)의 축약 -> ext
			if ($.inArray(ext, ['jpg', 'gif', 'png', 'jpeg']) == -1) {//array이 안에 포함되어있나?/ 파라1: 보고자하는 확장자, 파라2: array -> 없으면 -1 반환한다.
				alert("gif, png, jpg, jepg 파일만 업로드 할 수 있습니다.");
				$("#file").val(""); // 파일을 비운다.
			}
		}		
		
		// form태그를 자바스크립트에서 만든다. (파일은 무조건 form태그를 써야한다.)
		let formData = new FormData(); //객체 -> form태그 만들어짐
		formData.append("subject", subject); //name속성으로 key값한다.
		formData.append("content", content);
		formData.append("file", $("#file")[0].files[0]); // $("#file")[0]: 첫번째 input file 태그를 의미, files[0]는 업로드된 첫번째 파일을 의미
		//아까 변수 file에 저장한건 그냥 파일명임, 파일이 아니다.  ->  null값이 들어갈수도 있음
		
		// ajax form 데이터 전송
		$.ajax({
			type: "post"
			, url: "/post/create"
			, data: formData //file은 쿼리스트링으로 넣을수없다 -> serialize못하고 위와 같이 한다.
			, enctype: "multipart/form-data" // file업로드를 위한 필수 설정 
			, processData: false //  file업로드를 위한 필수 설정
			, contentType: false // file업로드를 위한 필수 설정
			// String으로 넘기는게 아니고 파일을 남기는 것이기에 String이 아니라고 false하는 것임 -> requets
			, success: function(data) {
				if (data.result == 'success') {
					alert("메모가 성공적으로 업로드 되었습니다.")
					location.href="/post/post_list_view";
				}
			}
			, error: function(e) {
				alert("메모 저장에 실패했습니다. 관리자에게 문의하세요.");ㄴ
			}
		});
		
		
	});
	
});



</script>



