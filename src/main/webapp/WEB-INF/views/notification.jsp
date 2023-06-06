<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>:: PMS Notifications ::</title>
<style>
	@import url("/res/css/common.css");
</style>
<link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.1.1/css/all.min.css">
<link rel="preconnect" href="https://fonts.gstatic.com" />
<link href="https://fonts.googleapis.com/css2?family=Poppins&display=swap" rel="stylesheet" />
<script src="/res/js/common.js"></script>
<script src="https://cdnjs.cloudflare.com/ajax/libs/jquery/3.6.0/jquery.min.js" referrerpolicy="no-referrer"></script>
<script src="https://cdn.jsdelivr.net/npm/sockjs-client@1/dist/sockjs.min.js"></script>
<script type="text/javascript">

//전송 버튼 누르는 이벤트
$("#button-send").on("click", function(e) {
	sendMessage();
	$('#msg').val('')
});

var sock = new SockJS('http://localhost:80/Chat');
sock.onmessage = onMessage;
sock.onclose = onClose;
sock.onopen = onOpen;

function sendMessage() {
	sock.send($("#msg").val());
}
//서버에서 메시지를 받았을 때
function onMessage(msg) {
	
	var data = msg.data;
	var sessionId = null; //데이터를 보낸 사람
	var message = null;
	
	var arr = data.split(":");
	
	for(var i=0; i<arr.length; i++){
		console.log('arr[' + i + ']: ' + arr[i]);
	}
	
	var cur_session = '${userid}'; //현재 세션에 로그인 한 사람
	console.log("cur_session : " + cur_session);
	
	sessionId = arr[0];
	message = arr[1];
	
    //로그인 한 클라이언트와 타 클라이언트를 분류하기 위함
	if(sessionId == cur_session){
		
		var str = "<div class='col-6'>";
		str += "<div class='alert alert-secondary'>";
		str += "<b>" + sessionId + " : " + message + "</b>";
		str += "</div></div>";
		
		$("#msgArea").append(str);
	}
	else{
		
		var str = "<div class='col-6'>";
		str += "<div class='alert alert-warning'>";
		str += "<b>" + sessionId + " : " + message + "</b>";
		str += "</div></div>";
		
		$("#msgArea").append(str);
	}
	
}
//채팅창에서 나갔을 때
function onClose(evt) {
	
	var user = '${pr.username}';
	var str = user + " 님이 퇴장하셨습니다.";
	
	$("#msgArea").append(str);
}
//채팅창에 들어왔을 때
function onOpen(evt) {
	
	var user = '${pr.username}';
	var str = user + "님이 입장하셨습니다.";
	
	$("#msgArea").append(str);
}

</script>
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
	<div class="container">
	<div class="col-6">
		<label><b>채팅방</b></label>
	</div>
	<div>
		<div id="msgArea" class="col">
		
		</div>
		<div class="col-6">
		<div class="input-group mb-3">
			<input type="text" id="msg" class="form-control" aria-label="Recipient's username" aria-describedby="button-addon2">
			<div class="input-group-append">
				<button class="btn btn-outline-secondary" type="button" id="button-send">전송</button>
			</div>
		</div>
		</div>
	</div>
	<div class="col-6">
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
    <li><i id="highlight" class="fa-solid fa-bell" onClick="moveAlert()" ></i></li>
    <br>
    <li><i id="default" class="fa-solid fa-wrench" onClick="moveMgr()" ></i></li>
    <br>
    <li><i id="default" class="fa-solid fa-user" onClick="moveMyPage()" ></i></li>
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