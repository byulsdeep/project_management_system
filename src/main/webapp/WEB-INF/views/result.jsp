<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>:: PMS Result ::</title>
<style>
	@import url("/res/css/common.css");
</style>
<link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.1.1/css/all.min.css">
<link rel="preconnect" href="https://fonts.gstatic.com" />
<link href="https://fonts.googleapis.com/css2?family=Poppins&display=swap" rel="stylesheet" />
<script src="/res/js/common.js"></script>
<script>
function init(){
	const message = "${message}";
	if(message != "") alert(message);
}
</script>
</head>
<body onLoad="init()">
<br><br><br>
<section>  
  <article>
  	<div>
  		<div id="projectDiv">
  		</div>
  	</div>
  	<div>
  		<div id="inviteDiv">	
  		</div>
  	</div>
  	<div>
  		<div id="newInvite">
  		</div>
  	</div>
  	<div>
  		<div id="eeeee">			
  		</div>
  	</div>  	
  	<div>
  		<div id="fffff">			
  		</div>
  	</div>   	
  	<div>
  		<div id="progressBar">			
  		</div>
  	</div>     
  </article>
</section>
<footer>
  <p>공지사항</p>
</footer>
<nav>
  <ul>
    <br>
    <li><i id="default" class="fa-solid fa-chalkboard-user" onClick="moveMain()" ></i></li>
    <br>
    <li><i id="default" class="fa-solid fa-bell" onClick="moveAlert()" ></i></li>
    <br>
    <li><i id="default" class="fa-solid fa-wrench" onClick="moveMgr()" ></i></li>
    <br>
    <li><i id="highlight" class="fa-solid fa-user" onClick="moveMyPage()" ></i></li>
  </ul>
</nav>
<header>
  <span>
	${accessInfo.pmbName}(${accessInfo.mlvName})님 어서오세요&nbsp&nbsp&nbsp&nbsp소속: [${accessInfo.claName}]&nbsp&nbsp&nbsp&nbsp접속시간: [${accessInfo.date}]
	<input type="button" name="accessOut" value="로그아웃" onClick="logOut()" />
  </span>
</header>
<i id="newProject" class="fa-solid fa-folder-plus" onClick="moveProject()" ></i>
<form name="form" method="post"></form>	
</body>
</html>