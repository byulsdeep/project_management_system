<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>채팅방</title>

<script src="https://cdnjs.cloudflare.com/ajax/libs/jquery/3.6.0/jquery.min.js" referrerpolicy="no-referrer"></script>
<script>
let Chat__roomId = parseInt('${param.roomId}');
</script>
<script>
function Chat__addMessage(writer, body) {
	$.post(
			'/chat/doAddMessage',
			{
				roomId:Chat__roomId,
				writer:writer,
				body:body
			},
			function(data) {

			},
			'json'
				);
}

function Chat__writeMessage(chatMessage) {
	let html = chatMessage.writer + ' : ' + chatMessage.body;	
	$('.chat-messages').prepend('<div>' + html + '</div>');
}

let Chat__lastLoadedMessageId = 0;

function Chat__loadNewMessages() {
	$.get(
		'./getMessagesFrom', 	
		{
			roomId:Chat__roomId,
			from:Chat__lastLoadedMessageId + 1
		},
		function(data) {
			for (i = 0; i < data.messages.length; i++) {
				Chat__writeMessage(data.messages[i]);
				
				Chat__lastLoadedMessageId = data.messages[i].id;
			}
		},
		'json'
	);
	
}

//setInterval(Chat__loadNewMessages, 1000);

function submitChatMessageForm(form) {
	form.writer.value = form.writer.value.trim();
	
	if(form.writer.value.length == 0 ) {
		alert('작성자를 입력해주세요.');
		form.writer.focus();
		
		return false;
	}
	
	if(form.body.value.length == 0 ) {
		alert('내용을 입력해주세요.');
		form.body.focus();
		
		return false;
	}
	
	let writer = form.writer.value;
	let body = form.body.value;
	
	form.body.value = '';
	form.body.focus();
	
	Chat__addMessage(writer, body);
}
</script>
</head>
<body>
	<h1>${roomId}번방${param.roomId}</h1>
	
	<form onsubmit="submitChatMessageForm(this); return false;">
		<div>
			<input type="text" name="writer" placeholder="작성자" autocomplete="off">
		</div>
		
		<div>
			<input type="text" name="body" placeholder="내용" autocomplete="off">
		</div>
		<div>
			<input type="submit" value="작성">
		</div>
	</form>
	
	<div class="chat-messages"></div>
</body>
</html>