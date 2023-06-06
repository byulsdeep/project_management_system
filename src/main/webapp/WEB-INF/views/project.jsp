<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>:: PMS Project ::</title>
<style>
	@import url("/res/css/common.css");
</style>
<link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.1.1/css/all.min.css">
<link rel="preconnect" href="https://fonts.gstatic.com" />
<link href="https://fonts.googleapis.com/css2?family=Poppins&display=swap" rel="stylesheet" />
<script src="/res/js/common.js"></script>
<script>
let box = [];
let projectCode;
let projectName;
let projectComment;
let startDate;
let endDate;
let oldTable;
let savedTable;
let toSendList = [];

function init(){
	lightBoxCtl('프로젝트 생성', true);
	
	let cbody = document.getElementById("cbody");
	cbody.innerHTML = "";
	
	//projectCode 0
	//"2022-07-13 11:31:09"
	const today = new Date();
	const year = today.getFullYear();
	const month = today.getMonth()+1;
	const day = today.getDate();
	const hour = today.getHours();
	const minute = today.getMinutes();
	const second = today.getSeconds();	
	const date = String(year).substr(2) + String((month<10)?("0" + month):month) + String((day<10)?("0" + day):day);
	const dateHyphen = String(year) + "-" + String((month<10)?("0" + month):month) + "-" + String((day<10)?("0" + day):day);
	const time = String((hour<10)?("0" + hour):hour) + String((minute<10)?("0" + minute):minute) + String((second<10)?("0" + second):second);
	
	box.push(createInput("hidden", "projectCode", null, null, date + time + ${accessInfo.pmbCode}, null));
	
	//projectName 1
	box.push(createInput("text", "projectName", "프로젝트 이름", "box", "BDGames 재고관리 시스템 개발", null));
	
	//projectComment 2
	box.push(document.createElement("textarea"));
	box[2].setAttribute("name", "projectComment");
	box[2].setAttribute("placeholder", "프로젝트 설명");
	box[2].setAttribute("class", "box");
	box[2].setAttribute("rows", "2");
	box[2].setAttribute("cols", "60");
	box[2].innerText = "BDGames 게임 리테일 회사를 위한 평범한 재고관리 시스템";
	
	box.push(document.createElement("br"));
	
	//startDate 5
	box.push(document.createElement("label"));
	box[4].innerHTML = "프로젝트 시작일:&nbsp";
	
	box.push(createInput("date", "startDate", null, null, dateHyphen, null));
	
	box.push(document.createElement("br"));
	//endDate 9
	box.push(document.createElement("label"));
	box[7].innerHTML = "프로젝트 종료일:&nbsp";
	
	box.push(createInput("date", "endDate", null, null, "2022-12-31", null));
	
	//isVisible 11
	box.push(createSelect("isVisible"));
	
	let yesVisible = createOption("T");
	yesVisible.innerText = "공개";
	box[9].appendChild(yesVisible);
	let noVisible = createOption("F");
	noVisible.innerText = "비공개";
	box[9].appendChild(noVisible);
	
	//submit button 12
	box.push(createInput("button", "button", null, "btn button", "등록", null));
	box[10].addEventListener("click", function(){
		insProject();
		});
	
	for(i=0; i<box.length; i++) {
		cbody.appendChild(box[i]);
	}
	
	let exit = createInput("button", "button", null, "btn button", "취소", null);
	exit.addEventListener("click", function(){
		closeCanvas();
	});
	cbody.appendChild(exit);
	
	let form = document.getElementsByName("form")[0];
	let canvas = document.getElementById("canvas");
	form.appendChild(canvas);
	
	const message = "${message}";
	if(message != "") alert(message);
}


function insProject(){
	let clientData = "";
	
	for(i=0; i<box.length-1; i++) {	
		if(box[i].name != null) {	
			clientData += (i==0? "":"&");
			clientData += box[i].name + "=" + box[i].value;
		}
	}
	
	postAjaxJson("InsProject", clientData, "callBack");
	lightBoxCtl(null, false);
	
}

function callBack(ajaxData) {
	
	const memberList = JSON.parse(ajaxData);
	
	if(memberList[0].message != "생성실패") {
		createLeft(ajaxData);
		createMiddle(memberList);
	} else {
		alert(memberList[0].message);
	}
}



function sendMail(memberInfo) {
	let pmbCode = [];
	let email = [];
	
	for(i=0; i<toSendList.length; i++) {
		pmbCode.push(toSendList[i][0]);
		email.push(toSendList[i][4]);
	}
	
	let clientData = "projectCode=" + projectCode + "&projectName=" + projectName + "&projectComment=" + projectComment + "&startDate=" + startDate + "&endDate=" + endDate; 
	
	for(i=0; i<toSendList.length; i++) {
		clientData += "&projectMembers[" + i + "].pmbCode=" + toSendList[i][0];
		clientData += "&projectMembers[" + i + "].pmbName=" + toSendList[i][1];
		clientData += "&projectMembers[" + i + "].mlvName=" + toSendList[i][2];
		clientData += "&projectMembers[" + i + "].claName=" + toSendList[i][3];
		clientData += "&projectMembers[" + i + "].email=" + toSendList[i][4];
	}

	postAjaxJson("InviteMembers", clientData, "getMessage");
}

function getMessage(ajaxData) {
	alert(ajaxData);
}



function createMiddle(memberList) {
	let middle = document.getElementById("middle");
	let table = "<table id='memberList'><tr><th>코드</th><th>이름</th><th>등급</th><th>반</th><th>이메일</th></tr>";
	for(i=0; i<memberList.length; i++) {
		table += "<tr class='middle' onClick='sendRight(\"" + memberList[i].pmbCode + ":" + memberList[i].pmbName + ":" + memberList[i].mlvName + ":" + memberList[i].claName + ":" + memberList[i].email + "\")'><td value='" + memberList[i].pmbCode + "'>" + memberList[i].pmbCode + "</td>"
				+ "<td value='" + memberList[i].pmbName + "'>" + memberList[i].pmbName + "</td>"
				+ "<td value='" + memberList[i].mlvName + "'>" + memberList[i].mlvName + "</td>"
				+ "<td value='" + memberList[i].claName + "'>" + memberList[i].claName + "</td>"
				+ "<td value='" + memberList[i].email + "'>" + memberList[i].email + "</td></tr>"
	}
	table += "</table>";
	
	middle.innerHTML = table;
	table = "<table id='oldTable'><tr><th>코드</th><th>이름</th><th>등급</th><th>반</th><th>이메일</th></tr>";
	for(i=0; i<memberList.length; i++) {
		table += "<tr class='middle' onClick='sendRight(\"" + memberList[i].pmbCode + ":" + memberList[i].pmbName + ":" + memberList[i].mlvName + ":" + memberList[i].claName + ":" + memberList[i].email + "\")'><td value='" + memberList[i].pmbCode + "'>" + memberList[i].pmbCode + "</td>"
				+ "<td value='" + memberList[i].pmbName + "'>" + memberList[i].pmbName + "</td>"
				+ "<td value='" + memberList[i].mlvName + "'>" + memberList[i].mlvName + "</td>"
				+ "<td value='" + memberList[i].claName + "'>" + memberList[i].claName + "</td>"
				+ "<td value='" + memberList[i].email + "'>" + memberList[i].email + "</td></tr>"
	}
	table += "</table>";
	
	oldTable = table;
}



function sendRight(memberInfo) {
	toSendList.push(memberInfo.split(":"));
	
	
	let right = document.getElementById("right");
	
	let name = (memberInfo.split(":"))[1];
	let email = (memberInfo.split(":"))[4];
	
	if(savedTable == null) {
		table = "<table id='emailList'><tr><th>이름</th><th>이메일</th></tr>";
		table += "<tr class='right' onClick='sendLeft(\"" + memberInfo + "\")' ><td value='" + name + "'>" + name + "</td>"
				+ "<td value='" + email + "'>" + email + "</td></tr>";	
		savedTable = table;		
	} else {
		savedTable += "<tr class='right' onClick='sendLeft(\"" + memberInfo + "\")' ><td value='" + name + "'>" + name + "</td>"
		+ "<td value='" + email + "'>" + email + "</td></tr>";
		table = savedTable;
	} 
	table += "</table>";
	right.innerHTML = table;
	
	let memberList = document.getElementById("memberList");
	let memberRows = document.getElementById("memberList").rows;

	for(i=0; i< memberRows.length; i++) {
		if(memberList.rows.item(i).cells[1].innerText == name) {
			memberList.rows.item(i).remove();
		}
	}
	
	let button = createInput("button", "button", null, "btn button", "send e-mail", null);	
	right.appendChild(button);
	button.addEventListener("click", function(){
		sendMail(memberInfo);
		});
}

function sendLeft(memberInfo) {
	memberArray = memberInfo.split(":");
	
	let name = memberArray[1];
	let email = memberArray[4];
	
	let emailList = document.getElementById("emailList");
	let emailRows = document.getElementById("emailList").rows;
	
	for(i=0; i < emailRows.length; i++) {
		if(emailList.rows.item(i).cells[0].innerText == name) {
			emailList.rows.item(i).remove();
		}
	}
	
	let memberList = document.getElementById("memberList");
	let memberRows = document.getElementById("memberList").rows;
	let count = 0;
	let row = document.createElement("tr");
	let cell = [];
	let cellText = [];
	

	let canvas = document.getElementById("canvas");
	canvas.innerHTML = oldTable;
	table = document.getElementById("oldTable");

	let oldRows = table.rows;
	
	for(i=0; i < oldRows.length; i++) {
		if(table.rows.item(i).cells[1].innerText != name) {
			count++;
		} 
	}
	
	if(count !=0) {				
		 for (j = 0; j < memberArray.length; j++) {
			cell[j] = document.createElement("td");
		     cellText[j] = document.createTextNode(memberArray[j]);

		      cell[j].appendChild(cellText[j]);
		      row.appendChild(cell[j]);
		    }
	}
	
	row.setAttribute("class", "middle");
	row.addEventListener("click", function() {
		sendRight(memberInfo);
	});
	memberList.appendChild(row);
}

function createLeft(ajaxData) {
	let left = document.getElementById("left");
	let proInfo = [];
	for(i=0; i<box.length-1; i++) {
		if(box[i].name != null) {
			if(box[i].value == "T") { 
				proInfo.push("공개");
			} else if(box[i].value == "F") {
				proInfo.push("비공개");
			} else {
				proInfo.push(box[i].value);
			}		
		}
	}
	
	//name
	let b = document.createElement("b");
	b.innerText = proInfo[1];
	left.appendChild(b);
	//code
	left.appendChild(document.createElement("br"));
	left.appendChild(document.createElement("br"));
	left.innerText += proInfo[0];
	
	projectCode = proInfo[0];
	projectName = proInfo[1];
	projectComment = proInfo[2];
	startDate = proInfo[3];
	endDate = proInfo[4];
	
	//start
	left.appendChild(document.createElement("br"));
	let b2 = document.createElement("b");
	b2.innerText = "시작일: ";
	left.appendChild(b2);
	left.innerText += proInfo[3];
	//end
	left.appendChild(document.createElement("br"));
	let b3 = document.createElement("b");
	b3.innerText = "종료일: ";
	left.appendChild(b3);
	left.innerText += proInfo[4];
	//vis
	left.appendChild(document.createElement("br"));
	left.appendChild(document.createElement("br"));
	let b4 = document.createElement("b");
	b4.innerText = "공개여부: ";
	left.appendChild(b4);
	left.innerText += proInfo[5];
	//comment
	left.appendChild(document.createElement("br"));
	left.appendChild(document.createElement("br"));
	let b5 = document.createElement("b");
	b5.innerText = proInfo[2];
	left.appendChild(b5);
}



</script>
</head>
<body onLoad="init()">
<br><br><br>
<section>  
  <article id="article">
  	<div id="left"></div>
  	<div id="middle"></div>
  	<div id="right"></div>
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