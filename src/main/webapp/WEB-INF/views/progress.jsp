<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>:: PMS Progress ::</title>
<style>
	@import url("/res/css/common.css");
</style>
<link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.1.1/css/all.min.css">
<link rel="preconnect" href="https://fonts.gstatic.com" />
<link href="https://fonts.googleapis.com/css2?family=Poppins&display=swap" rel="stylesheet" />
<script src="/res/js/common.js"></script>
<script>
let projectCode;
let selectedMJ = [];

function init(){
	const message = "${message}";
	if(message != "") alert(message);
	projectCode = document.getElementById("projectCode0").innerText;
}
function changeProject() {
	let form = document.getElementsByName("form")[0];
	const abc = document.getElementsByName("projectCode")[0];
	form.appendChild(abc);
	form.action = "ProgressMgr";
	form.submit();
}
function getMethodsOnMJMC(mc) {
	if(selectedMJ[0] == null || selectedMJ[0] == "") {
		clientData = "";
		clientData += "projectCode=" + projectCode;
		clientData += "&mcCode=" + mc;
		postAjaxJson("GetMethodsOnMC", clientData, "gotMCMethods");
	} else {
		clientData = "";
		clientData += "projectCode=" + projectCode;
		clientData += "&moduleCode=" + selectedMJ[0] + "&jobCode=" + selectedMJ[1];
		clientData += "&mcCode=" + mc;
		postAjaxJson("GetMethodsOnMJMC", clientData, "gotMCMethods");
	}
}
function getMethodsOnMJ(obj) {
	let hoe = obj.children[0].value;
	let arr = hoe.split(":");
	let gimotti = document.getElementsByName("gimotti")[0].value;       
	
	selectedMJ = arr;
	
	clientData = "";
	clientData += "projectCode=" + projectCode;
	clientData += "&moduleCode=" + arr[0] + "&jobCode=" + arr[1];

	postAjaxJson("GetMethodsOnMJ", clientData, "gotMethods");
}
function gotMethods(ajaxData) {
	let methods = JSON.parse(ajaxData);
	createMethods(methods);
	createMVC(methods);
}
function gotMCMethods(ajaxData) {
	let methods = JSON.parse(ajaxData);
	createMethods(methods);
}
function createMVC(methods) {
	let bfct = 0; let bfmo = 0; let bfrd = 0; let bfvi = 0;
	let inct = 0; let inmo = 0; let inrd = 0; let invi = 0;
	let cpct = 0; let cpmo = 0; let cprd = 0; let cpvi = 0;
	let ct = 0; let mo = 0; let rd = 0; let vi = 0; let el = 0;
	if(methods != null && methods != "") {
		for(i = 0; i < methods.length; i++) {
			if(methods[i].mcCode == "CT") {
				ct++;
			} else if (methods[i].mcCode == "MO") {
				mo++;
			} else if (methods[i].mcCode == "RD") {
				rd++;
			} else if (methods[i].mcCode == "VI") {
				vi++;
			} else {
				el++;
			}		
			if(methods[i].methodState == "BF" && methods[i].mcCode == "CT") {
				bfct++;
			} else if(methods[i].methodState == "BF" && methods[i].mcCode == "MO") {
				bfmo++;
			} else if(methods[i].methodState == "BF" && methods[i].mcCode == "RD") {
				bfrd++;
			} else if(methods[i].methodState == "BF" && methods[i].mcCode == "VI") {
				bfvi++;
			} else if(methods[i].methodState == "IN" && methods[i].mcCode == "CT") {
				inct++;
			} else if(methods[i].methodState == "IN" && methods[i].mcCode == "MO") {
				inmo++;
			} else if(methods[i].methodState == "IN" && methods[i].mcCode == "RD") {
				inrd++;
			} else if(methods[i].methodState == "IN" && methods[i].mcCode == "VI") {
				invi++;
			} else if(methods[i].methodState == "CP" && methods[i].mcCode == "CT") {
				cpct++;
			} else if(methods[i].methodState == "CP" && methods[i].mcCode == "MO") {
				cpmo++;
			} else if(methods[i].methodState == "CP" && methods[i].mcCode == "RD") {
				cprd++;
			} else if(methods[i].methodState == "CP" && methods[i].mcCode == "VI") {
				cpvi++;
			}
		}		
	}
	let ctDiv = document.createElement("div");
	if(cpct == ct && cpct != 0) {
		ctDiv.innerHTML = "<div class=\"btn cp\" onClick=\"getMethodsOnMJMC('CT')\"> CONTROLLER"
			+ "<br>개발전: " + bfct + "<br>개발진행중: " + inct + "<br>개발완료: " + cpct + " / " + ct + ""
			+ "</div>\r\n";
	} else if(inct > 0) {
		ctDiv.innerHTML = "<div class=\"btn in\" onClick=\"getMethodsOnMJMC('CT')\"> CONTROLLER"
			+ "<br>개발전: " + bfct + "<br>개발진행중: " + inct + "<br>개발완료: " + cpct + " / " + ct + ""
			+ "</div>\r\n";
	} else {
		ctDiv.innerHTML = "<div class=\"btn button\" onClick=\"getMethodsOnMJMC('CT')\"> CONTROLLER"
			+ "<br>개발전: " + bfct + "<br>개발진행중: " + inct + "<br>개발완료: " + cpct + " / " + ct + ""
			+ "</div>\r\n";
	}
	let moDiv = document.createElement("div");
	if(cpmo == mo && cpmo !=0) {
		moDiv.innerHTML = "<div class=\"btn cp\" onClick=\"getMethodsOnMJMC('MO')\"> SERVICES"
		+ "<br>개발전: " + bfmo + "<br>개발진행중: " + inmo + "<br>개발완료: " + cpmo + " / " + mo + ""
		+ "</div>\r\n";
	} else if(inmo > 0) {
		moDiv.innerHTML = "<div class=\"btn in\" onClick=\"getMethodsOnMJMC('MO')\"> SERVICES"
			+ "<br>개발전: " + bfmo + "<br>개발진행중: " + inmo + "<br>개발완료: " + cpmo + " / " + mo + ""
			+ "</div>\r\n";
	} else {
		moDiv.innerHTML = "<div class=\"btn button\" onClick=\"getMethodsOnMJMC('MO')\"> SERVICES"
			+ "<br>개발전: " + bfmo + "<br>개발진행중: " + inmo + "<br>개발완료: " + cpmo + " / " + mo + ""
			+ "</div>\r\n";
	}
	let rdDiv = document.createElement("div");
	if(cprd == rd && cprd != 0) {
		rdDiv.innerHTML = "<div class=\"btn cp\" onClick=\"getMethodsOnMJMC('RD')\"> DAO"
		+ "<br>개발전: " + bfrd + "<br>개발진행중: " + inrd + "<br>개발완료: " + cprd + " / " + rd + ""
		+ "</div>\r\n";
	} else if(inrd > 0) {
		rdDiv.innerHTML = "<div class=\"btn in\" onClick=\"getMethodsOnMJMC('RD')\"> DAO"
			+ "<br>개발전: " + bfrd + "<br>개발진행중: " + inrd + "<br>개발완료: " + cprd + " / " + rd + ""
			+ "</div>\r\n";
	} else {
		rdDiv.innerHTML = "<div class=\"btn button\" onClick=\"getMethodsOnMJMC('RD')\"> DAO"
			+ "<br>개발전: " + bfrd + "<br>개발진행중: " + inrd + "<br>개발완료: " + cprd + " / " + rd + ""
			+ "</div>\r\n";
	}
	let viDiv = document.createElement("div");
	if(cpvi == vi && cpvi != 0) {
		viDiv.innerHTML = "<div class=\"btn cp\" onClick=\"getMethodsOnMJMC('VI')\"> VIEW"
		+ "<br>개발전: " + bfvi + "<br>개발진행중: " + invi + "<br>개발완료: " + cpvi + " / " + vi + ""
		+ "</div>";
	} else if(invi > 0) {
		viDiv.innerHTML = "<div class=\"btn in\" onClick=\"getMethodsOnMJMC('VI')\"> VIEW"
			+ "<br>개발전: " + bfvi + "<br>개발진행중: " + invi + "<br>개발완료: " + cpvi + " / " + vi + ""
			+ "</div>";
	} else {
		viDiv.innerHTML = "<div class=\"btn button\" onClick=\"getMethodsOnMJMC('VI')\"> VIEW"
			+ "<br>개발전: " + bfvi + "<br>개발진행중: " + invi + "<br>개발완료: " + cpvi + " / " + vi + ""
			+ "</div>";
	}
	let mainDiv = document.getElementById("newInvite");
	mainDiv.innerHTML = "";
	mainDiv.appendChild(ctDiv);
	mainDiv.appendChild(moDiv);
	mainDiv.appendChild(rdDiv);
	mainDiv.appendChild(viDiv);
}
function createMethods(methods) {
	let mainDiv = document.getElementById("eeeee");
	mainDiv.innerHTML = "";
	let subDiv = [];
	let input = [];
	let button = [];
	let up = [];
	if(methods != null && methods != "") {
		for(i = 0; i < methods.length; i++) {
			if(methods[i].methodState == "CP") {
				subDiv[i] = createDiv("", "stn cp", methods[i].methodName, "");
				button[i] = createDiv(null, null, "<input class=\"cp stn\" type=\"button\" value=\"상태변경\" onClick=\"changeState(" + i + ")\">", null);
				up[i] = createDiv(null, null, "<input class=\"cp stn\" type=\"button\" value=\"파일추가\" onClick=\"upload(" + i + ")\">", null);
			} else if(methods[i].methodState == "IN") {
				subDiv[i] = createDiv("", "stn in", methods[i].methodName, "");
				button[i] = createDiv(null, null, "<input class=\"in stn\" type=\"button\" value=\"상태변경\" onClick=\"changeState(" + i + ")\">", null);
				up[i] = createDiv(null, null, "<input class=\"in stn\" type=\"button\" value=\"파일추가\" onClick=\"upload(" + i + ")\">", null);
			} else {
				subDiv[i] = createDiv("", "stn button", methods[i].methodName, "");
				button[i] = createDiv(null, null, "<input class=\"button stn\" type=\"button\" value=\"상태변경\" onClick=\"changeState(" + i + ")\">", null);
				up[i] = createDiv(null, null, "<input class=\"button stn\" type=\"button\" value=\"파일추가\" onClick=\"upload(" + i + ")\">", null);
			}
			input[i] = createInput("hidden", "", "", "", methods[i].moduleCode + ":" + methods[i].jobCode + ":" + methods[i].methodCode + ":" + methods[i].mcCode, null);
			input[i].setAttribute("id", "method" +i);
			subDiv[i].appendChild(input[i]);
			subDiv[i].appendChild(button[i]);
			subDiv[i].appendChild(up[i]);
			mainDiv.appendChild(subDiv[i]);
		}
		let newAng = createInput("hidden", "ang", "", "", methods.length, null);
		mainDiv.appendChild(newAng);
	} else mainDiv.innerHTML = "NULL";
}
function upload(num) {
	lightBoxCtl('파일추가', true);
	let cbody = document.getElementById("cbody");
	cbody.innerHTML = "";
	let box = [];
	box.push(createInput("text", "fileName", "파일이름", "box", null, null));
	box.push(createInput("text", "fileLocation", "파일위치", "box", null, null));
	box.push(createInput("button", "button", null, "btn button", "확인", null));	
	box[2].addEventListener("click", function(){
		sendFile(num, box[0].value, box[1].value);
		});
	box.push(createInput("button", "button", null, "btn button", "취소", null));	
	box[3].addEventListener("click", function(){
		closeCanvas();
		});
	for(i=0; i<box.length; i++) {
		cbody.appendChild(box[i]);
	}
}
function sendFile(num, name, location) {
	closeCanvas();
	let fileDiv = document.getElementById("fffff");
	let div = document.createElement("div");
	div.innerHTML = "<div class=\"newThumb\">파일이름: " + name + "<br>파일경로: " + location + "</div>"
	fileDiv.appendChild(div);
}

function changeState(num) {
	lightBoxCtl('상태변경', true);
	let cbody = document.getElementById("cbody");
	cbody.innerHTML = "";
	let box = [];
	box.push(createInput("button", "button", null, "btn button", "개발전", null));	
	box[0].addEventListener("click", function(){
		changeBF(num);
		});
	box.push(createInput("button", "button", null, "btn button", "개발진행중", null));	
	box[1].addEventListener("click", function(){
		changeIN(num);
		});
	box.push(createInput("button", "button", null, "btn button", "개발완료", null));	
	box[2].addEventListener("click", function(){
		changeCP(num);
		});
	box.push(createInput("button", "button", null, "btn button", "취소", null));	
	box[3].addEventListener("click", function(){
		closeCanvas();
		});
	for(i=0; i<box.length; i++) {
		cbody.appendChild(box[i]);
	}
}	
function changeBF(num) {
	let method = document.getElementById("method" + num);
	let hoe = method.value;
	let arr = hoe.split(":");
	clientData = "";
	clientData += "projectCode=" + projectCode;
	clientData += "&moduleCode=" + arr[0] + "&jobCode=" + arr[1];
	clientData += "&methodCode=" + arr[2] + "&mcCode=" + arr[3];
	clientData += "&methodState=" + "BF";
	postAjaxJson("BF", clientData, "gotMethods");
	closeCanvas();
}
function changeIN(num) {
	let method = document.getElementById("method" + num);
	let hoe = method.value;
	let arr = hoe.split(":");
	clientData = "";
	clientData += "projectCode=" + projectCode;
	clientData += "&moduleCode=" + arr[0] + "&jobCode=" + arr[1];
	clientData += "&methodCode=" + arr[2] + "&mcCode=" + arr[3];
	clientData += "&methodState=" + "IN";
	postAjaxJson("IN", clientData, "gotMethods");
	closeCanvas();
}
function changeCP(num) {
	let method = document.getElementById("method" + num);
	let hoe = method.value;
	let arr = hoe.split(":");
	clientData = "";
	clientData += "projectCode=" + projectCode;
	clientData += "&moduleCode=" + arr[0] + "&jobCode=" + arr[1];
	clientData += "&methodCode=" + arr[2] + "&mcCode=" + arr[3];
	clientData += "&methodState=" + "CP";	
	postAjaxJson("CP", clientData, "gotMethods");
	closeCanvas();
}
</script>
</head>
<body onLoad="init()">
<br><br><br>
<section>  
  <article>
   	<div>
		<div id="projectDiv">
			<div class="projectThumbOn">
				<select name="projectCode">
					${projectOptions}
				</select>
				<button onClick="changeProject()" class="btn button">선택</button>
			</div>
			${projectDetail}
		</div>
  	</div>
  	<div>
  		<div id="inviteDiv">	
  		${actionName}
  		</div>
  	</div>
  	<div>
  		<div id="newInvite">
  		${mvcStyle}
  		</div>
  	</div>
  	<div>
  		<div id="eeeee">
  		${methodName}		
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
<form name="form" method="post"></form>	
</body>
</html>