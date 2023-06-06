package com.pms.main;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class ChatService {
	@Autowired
	ChatDao chatDao;
	
	public void addMessage(int roomId, String writer, String body) {
		log.info("ChatService/addMessage()");

		chatDao.addMessage(roomId, writer, body);
	}

	public List getMessages() {
		log.info("ChatService/getMessages()");

		return chatDao.getMessages();
	}

	public List getMessagesFrom(int roomId, int from) {
		log.info("ChatService/getMessagesFrom()");
		
		return chatDao.getMessagesFrom(roomId, from);

	}

	public void clearAllMessages() {
		chatDao.clearAllMessages();
		
	}
}
