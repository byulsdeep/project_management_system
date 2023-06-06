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
<style>
	body {
 background-image: url("res/images/rainbowBlur.jpg");
}
</style>
<script src="/res/js/common.js"></script>
<script>
// const to = setTimeout(init, 1000);
init();
function init() {
	getAjaxJson("https://api.ipify.org", "format=json", "callServer");
	
}
function callServer(ajaxData) {
	jsonData = JSON.parse(ajaxData);
	const publicIp = "publicIp=" + jsonData.ip;
	location.href = "http://localhost/First?" + publicIp;
}
</script>
</head>
<body>
	<div class="center">
		<img src="res/images/handshake.png">
		<h1>Project Management System</h1>
	</div>
</body>
</html>