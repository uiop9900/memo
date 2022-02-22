<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>

<div id="signInBox">
	<form id="loginForm" action="/user/sign_in" method="post">
		<div class="input-group">
			<div class="input-group-prepend">
				<span class="input-group-text">ID</span>
			</div>
			<input type="text" id="loginId" name="loginId" class="form-control" placeholder="Username" >
		</div>
		<div class="input-group mt-2">
			<div class="input-group-prepend">
				<span class="input-group-text">PW</span>
			</div>
			<input type="password" id="password" name="password" class="form-control" placeholder="Password">
		</div>
		<div class="mt-3">
			<button type="submit" class="btn btn-danger btn-block">로그인</button>
		</div>
		<div class="mt-3">
			<a class="btn btn-secondary btn-block" href="/user/sign_up_view">회원가입</a>
		</div>
	</form>
</div>


<script>
$(document).ready(function(e){
	$("#loginForm").on('submit', function(e){
		 e.preventDefault();
		//일단 submit기능을 중단시킨 다음에 나중에 아래 코드를 통해 submit을 한다. $(this)[0].submit();
		
		//validation
		let loginId = $("#loginId").val().trim();
		let password = $("#password").val();

		if (loginId == "") {
			alert("아이디를 입력하세요.")
			return false;
		}
		
		if (password == "") {
			alert("비밀번호를 입력하세요.");
			return false;
		}
		
		
		//ajax를 호출
		let url = $(this).attr('action'); //form 태그의 action 주소를 가지고 옴
		let params = $(this).serialize(); // form태그에 있는 name 값들을 쿼리스트링으로 구성
		
		$.post(url, params)
		.done(function(data){
			if (data.result == 'success') {
				location.href="/post/post_list_view"; // 로그인이 성공하면 글목록으로 이동
			} else {
				alert(errorMessage);
				location.reload();
			}
		});
		
	});
});

</script>
