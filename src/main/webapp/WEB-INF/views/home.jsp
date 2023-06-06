<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>:: PMS :: Project Management System</title>
<style>
	@import url("/res/css/common.css");
</style>
<link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.1.1/css/all.min.css">
<link rel="preconnect" href="https://fonts.gstatic.com" />
<link href="https://fonts.googleapis.com/css2?family=Poppins&display=swap" rel="stylesheet" />
<script src="/res/js/common.js"></script>
<script>
function init() {
	getAjaxJson("https://api.ipify.org", "format=json", "setPublicIp");
	lightBoxCtl('PMS Access', true);
	let cbody = document.getElementById("cbody");
	let box = [];
	                   //type      name      placeholder  class   value
	box.push(createInput("text", "pmbCode", "회원코드", "box", "20230601", null));
	box.push(createInput("password", "pmbPassword", "패스워드", "box", "A1234!", null));	
	box.push(createInput("button", "button", null, "btn button", "Sign In", null));
	box.push(createInput("button", "button2", null, "btn button", "Sign Up", null));	
	
	for(i=0; i<box.length; i++) {
		cbody.appendChild(box[i]);
	}
	
	box[2].addEventListener("click", function(){
		accessCtl();
		});
	
	box[3].addEventListener("click", function(){
		moveSignUp();
		});
		
	let form = document.getElementsByName("clientData")[0];
	let canvas = document.getElementById("canvas");
	form.appendChild(canvas);
	
	const message = "${message}";
	if(message != "") alert(message);
}
function accessCtl(){
	console.log("ACCESS : " + jsonData.ip);
	const accessInfo = [];
	accessInfo.push(document.getElementsByName("pmbCode"));
	accessInfo.push(document.getElementsByName("pmbPassword"));
	
	/* storeCode.length == 10 */ /* contains space 추가 */
	if(accessInfo[0][0].value.length == 0){
		accessInfo[0][0].focus();
		return;
	}
	if(accessInfo[1][0].value.length == 0) {
		accessInfo[1][0].focus();
		return;
	}
	
	/* Server 요청 : request << form */
	let form = document.getElementsByName("clientData")[0];
	form.appendChild(createInput("hidden", "publicIp", null, null, jsonData.ip, null));
	form.action = "Access";
	form.submit();
}
function moveSignUp() {
	const form = document.getElementsByName("clientData")[0];
	form.action = "MoveSignUp";
	form.submit();
}
</script>
</head>
<body onLoad="init()" >

	<!-- Light Box Start -->
	<div id="canvas" class="canvas">
		<div id="light-box" class="light-box">
			<div id="image-zone" class="lightbox image"></div>
			<div id="content-zone" class="lightbox content">
				<div id="cheader"></div>
				<div id="cbody"></div>			
				<div id="cfoot"></div>
			</div>
		</div>	
	</div>
	<!-- Light Box End -->
	<form name="clientData" method="post">

	</form> 	
	
	
</body>
</html>

<!-- 학습목표
 Request Part : FrontEnd
 	- 사용자 데이터의 유효성 판단
 	- 서버 요청
 	  -- 요청방식, 요청 서비스코드, 요청 데이터
 Request Part : BackEnd - Controller
 	- RequestMethod.POST, RequestMethod.GET
 	- @RequestMapping (value={"", ""}, method= {"", ""})
 	- @ModelAttribute >> Client Data >> Bean Field Name
 Request Part : BackEnd - Service
 	- 서비스 요청의 패턴화 >> backController
 	  -- 서비스 코드 인식
 	- Database 와의 연동 >> Interface >> mapper >> db
 	
 Response Part: BackEnd >> Service
 	- 서버데이터 >> Model.addAttribute 
 	- Client Page >> View("page")
 	 >> ModelAndView
 	- return ModelAndView
 	
 Response Part: BackEnd >> Controller
 	- result ModelAndView
 
 Response Part : FrontEnd: main.jsp
 			  
 -->
