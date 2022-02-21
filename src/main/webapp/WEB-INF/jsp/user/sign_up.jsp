<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>

<div id="signUpBox">
	<form id="signUpForm" method="post" action="/user/sign_up"><%--리다이렉트로 로그인화면으로 넘어감 --%>
	<table class="table table-bordered">
	 <tr>
	 	<th class="table-danger">*아이디</th>
	 	<td>
	 		<div class="d-flex">
		 		<input type="text" id="loginId" name="loginId" class="form-control col-8 mr-3" placeholder="userName">
		 		<button type="button" id="LoginIdCheckBtn" class="btn btn-primary col-3">중복확인</button>
	 		</div>
	 		<div id="idCheckLength" class="small text-danger d-none">ID를 4자 이상 입력해주세요.</div>
	 		<div id="idCheckDuplicated" class="small text-danger d-none">이미 사용중인 ID입니다.</div>
	 		<div id="idCheckOk" class="small text-success d-none">사용가능한 ID 입니다.</div>
	 		
	 	</td>
	 </tr>
	 <tr>
	 	<th class="table-danger">*비밀번호</th>
	 	<td>
	 		<input type="password" id="password" name="password" class="form-control" placeholder="password">
	 	</td>
	 </tr>
	 	 <tr>
	 	<th class="table-danger">*비밀번호 확인</th>
	 	<td>
	 		<input type="password" id="confirmPassword" name="confirmPassword" class="form-control" placeholder="password">
	 	</td>
	 </tr>
	 	 <tr>
	 	<th class="table-danger">*이름</th>
	 	<td>
	 		<input type="text" id="name" class="form-control" name="name" placeholder="이름을 입력하세요.">
	 	</td>
	 </tr>
	 	 <tr>
	 	<th class="table-danger">*이메일</th>
	 	<td>
	 		<input type="text" id="email" class="form-control" name="email" placeholder="이메일을 입력하세요.">
	 	</td>
	 </tr>
	</table>
	
	<div class="d-flex justify-content-center mt-4">
		<button type="submit" id="signUpBtn" class="btn btn-danger btn-block col-8">회원가입</button>	
	</div>
	</form>

<script>
$(document).ready(function(e){
	
	//아이디 중복확인
	$("#LoginIdCheckBtn").on("click", function(e){
		let loginId = $("#loginId").val().trim();
		
		// 상황 문구 안보이게 모두 초기화
		$("#idCheckLength").addClass('d-none');
		$("#idCheckDuplicated").addClass('d-none');
		$("#idCheckOk").addClass('d-none');
		
		
		if (loginId.length < 4) {
			//아이디가 4자 미만일때 경고 문구 노출하고 끝낸다.
			$("#idCheckLength").removeClass('d-none');
			return;
		}
		
		//AJAX - 중복확인
		$.ajax({
			//type: "" 불분명하면 그냥 안쓰면 디폴트가 GET임
			url: "/user/is_duplicated_id"
			, data: {"loginId":loginId}
			, success: function(data){
				if (data.result == true) {
					// 중복인 경우 -> 사용 불가인 아이디
					$("#idCheckDuplicated").removeClass('d-none');
				} else if (data.result == false ){ 
					// 아닌 경우 -> 사용이 가능한 아이디
					$("#idCheckOk").removeClass('d-none');
				} else {
					// 에러 -> 서버의 에러
				}
			}
			, error: function(e) {
				// 통신실패 에러
				alert("아이디 중복확인에 실패했습니다. 관리자에게 문의해주세요.");
			}
		});
	})
	
	// 회원 가입
	$("#signUpForm").on("submit", function(e){
		e.preventDefault(); //서브밋 기능 중단
		
		//validation check
		let loginId = $("#loginId").val().trim();
		if (loginId == "") {
			alert("id를 입력해주세요.");
			return false;
		}
		
		let password = $("#password").val();
		let confirmPassword = $("#confirmPassword").val();
	
		if (password == "" || confirmPassword == ""){
			alert("비밀번호를 입력하세요.");
			return false; // submit함수에서는 return false임
		}

		if (password != confirmPassword) {
			alert("비밀번호가 일치하지 않습니다.");
			// text의 값을 초기화한다.
			$("#password").val('');
			$("#confirmPassword").val(''); //val에 값넣으면 값을 setting한다.
			return false;
		}
		
		let name = $("#name").val().trim();
		let email = $("#email").val().trim();
		
		if (name == "") {
			alert("이름을 입력하세요.");
			return false;
		}
		
		if (email == "") {
			alert("이메일을 입력하세요.");
			return false;
		}
		
		// 아이디 중복확인이 되었는지 확인
		// idCheckOk <div> 클래스의 d-none이 있는 경우 -> 성공이 아님 -> return 시킴
		if ( $("#idCheckOk").hasClass('d-none') ) {
			alert("아이디 중복확인을 다시 해주세요.");
			return false;
		}
	
		// submit
		// 1. form 서브밋 -> 응답이 화면이 됨 (Controller에서 만들어야 한다.)
		// 2. ajax 서브밋 -> 응답은 데이터가 됨 (RestController에서 만든다.)

		// 1. form 서브밋
		//$(this)[0].submit(); //꼭 [0]을 넣어야한다.
		
		// 2. ajax 서브밋
		let url = $(this).attr('action'); //form태그에 있는 action 주소를 가져오는 법
		
		let params = $(this).serialize(); // form태그에 들어있는 값을 한번에 보낼 수 있게 구성한다.(name 속성) : form태그는 name속성을 이용한다!
		// console.log(params); //쿼리스트링의 모습으로 직렬화된다.
		
		// 여기의 url은 절대 view주소 아니다. data를 주고 받아야해서 잘 확인!
		$.post(url, params) //주소, requestParameter
		.done(function(data){
			if (data.result == 'success') {
				alert("회원가입을 환영합니다. 로그인을 해주세요.");
				location.href="/user/sign_in_view" // 여긴 view로 넘어가는게 맞아서 view의 url인지 확인!
			} else {
				alert("회원가입에 실패했습니다. 다시 시도해주세요.");
				location.reload();
			}
		});
		
	});
	
}); 


</script>

</div>