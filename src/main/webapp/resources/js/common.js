let jsonData;

function lightBoxCtl(title, disp) {
    let canvas = document.getElementById("canvas");
    let header = document.getElementById("cheader");
    header.innerText = title;
    canvas.style.display = disp ? "block" : "none";
}
function closeCanvas(){
	let canvas = document.getElementById("canvas");
	canvas.style.display = "none";
}
function createInput(type, name, placeholder, classs, value, isRead) {
    let input = document.createElement("input");
    input.setAttribute("type", type);
    input.setAttribute("name", name);
    input.setAttribute("placeholder", placeholder);
    input.setAttribute("class", classs);
    if(value != null) {input.setAttribute("value", value);}
    if(isRead != null) input.setAttribute("readOnly", isRead);
    return input;
}
function createSelect(name) {
    let select = document.createElement("select");
    select.setAttribute("name", name);
    return select;
}
function createOption(value) {
    let option = document.createElement("option");
    option.setAttribute("value", value);
    return option;
}
function createHidden(name, value){
	let input = document.createElement("input");
	input.setAttribute("type", "hidden");
	input.setAttribute("name", name);
	input.setAttribute("value", value);	
	return input;
}
function createDiv(id, classs, content, value) {
	let cDiv = document.createElement("div");
	cDiv.setAttribute("id", id);
	cDiv.setAttribute("class", classs);
	cDiv.innerHTML = content;
	cDiv.setAttribute("value", value);	
	return cDiv; 
}
function createTextarea(name, rows, cols, classs){
	let textArea = document.createElement("textarea");
	textArea.setAttribute("name", name);
	textArea.setAttribute("rows", rows);
	textArea.setAttribute("cols", cols);
	textArea.setAttribute("class", classs);
	return textArea;
}
function isFormat(input, type) {
    const cap = /[A-Z]/;
    const lower = /[a-z]/;
    const num = /[0-9]/;
    const spChar = /[!@#$%^&*]/;

    let result;
    let count = 0;

    if (cap.test(input)) count++;
    if (lower.test(input)) count++;
    if (num.test(input)) count++;
    if (spChar.test(input)) count++;

    if (type) {
        result = count >= 3 ? true : false;
    } else {
        result = count == 0 ? true : false;
    }

    return result;
}
function isCharLength(input, min, max) {
    let result = false;
    if (max != null) {
        if (input.length >= min && input.length <= max)
            result = true;
    } else {
        if (input.length >= min)
            result = true;
    }
    return result;
}
/* AJAX :: GET */
function getAjaxJson(jobCode, clientData, fn) {
    const ajax = new XMLHttpRequest();
    const action = (clientData != "") ? (jobCode + "?" + clientData) : jobCode;
    ajax.onreadystatechange = function () {
        if (ajax.readyState == 4 && ajax.status == 200) {
            window[fn](ajax.responseText);
        }
    };
    ajax.open('get', action);
    ajax.send();
}
/* AJAX :: POST */
function postAjaxJson(jobCode, clientData, fn) {
    const ajax = new XMLHttpRequest();

    ajax.onreadystatechange = function () {
        if (ajax.readyState == 4 && ajax.status == 200) {
            window[fn](ajax.responseText);
        }
    };
    ajax.open('post', jobCode);
    ajax.setRequestHeader("Content-Type", "application/x-www-form-urlencoded");
    ajax.send(clientData);
}
/* Public IP Saving */
function setPublicIp(ajaxData) {
    jsonData = JSON.parse(ajaxData);
    //alert("CommonJS : " + jsonData.ip);
}
function logOut() {
	let form = document.getElementsByName("form")[0];
	form.action = "AccessOut";
	form.submit();
}
function moveMain() {
	let form = document.getElementsByName("form")[0];
	form.action = "MoveMain";
	form.submit();
}
function moveAlert() {
	let form = document.getElementsByName("form")[0];
	form.action = "MoveAlert";
	form.submit();
}
function moveMgr() {
	let form = document.getElementsByName("form")[0];
	form.action = "MoveMgr";
	form.submit();
}
function moveMyPage() {
	let form = document.getElementsByName("form")[0];
	form.action = "MoveMyPage";
	form.submit();
}
function moveProject() {
	let form = document.getElementsByName("form")[0];
	form.action = "MoveProject";
	form.submit();
}