<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<div id="signUpBox">
	<table class="table table-bordered">
	 <tr>
	 	<th class="table-danger">*아이디</th>
	 	<td class="d-flex">
	 		<input type="text" class="form-control col-8 mr-3" placeholder="userName">
	 		<button type="button" class="btn btn-primary col-3">중복확인</button>
	 	</td>
	 </tr>
	 <tr>
	 	<th class="table-danger">*비밀번호</th>
	 	<td>
	 		<input type="password" class="form-control" placeholder="password">
	 	</td>
	 </tr>
	 	 <tr>
	 	<th class="table-danger">*비밀번호 확인</th>
	 	<td>
	 		<input type="password" class="form-control" placeholder="password">
	 	</td>
	 </tr>
	 	 <tr>
	 	<th class="table-danger">*이름</th>
	 	<td>
	 		<input type="text" class="form-control" placeholder="이름을 입력하세요.">
	 	</td>
	 </tr>
	 	 <tr>
	 	<th class="table-danger">*이메일</th>
	 	<td>
	 		<input type="text" class="form-control" placeholder="이메일을 입력하세요.">
	 	</td>
	 </tr>
	</table>
	
	<div class="d-flex justify-content-center mt-4">
		<button type="button" class="btn btn-danger btn-block col-8">회원가입</button>	
	</div>
</div>