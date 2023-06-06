package com.pms.main;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.pms.beans.AuthB;
import com.pms.utils.Encryption;
import com.pms.utils.ProjectUtils;

import lombok.extern.slf4j.Slf4j;
//http://localhost/chat/room?roomId=1
@Controller
@Slf4j
public class ChatController {
	@Autowired
	private SqlSessionTemplate session;
	@Autowired
	private Encryption enc;
	@Autowired 
	private ProjectUtils pu;
	@Autowired
	ChatService chatService;
	@RequestMapping("/chat/room")
	public String showRoom(int roomId, Model model) {
		log.info("ChatController/showRoom()");
		model.addAttribute("roomId", roomId);
		return "chat/room";
	}
	@RequestMapping("/chat/clearAllMessages")
	@ResponseBody
	public Map clearAllMessages() {
		log.info("ChatController/clearAllMessages()");

		chatService.clearAllMessages();
		Map rs = new HashMap<>();
		rs.put("resultCode", "S-1");
		rs.put("msg", "모든 메시지들을 삭제 하였습니다.");
		return rs;
	}
	@RequestMapping("/chat/doAddMessage")
	@ResponseBody
	public Map doAddMessage(int roomId, String writer, String body) {
		log.info("ChatController/doAddMessage()");
		Map rs = new HashMap<>();
		chatService.addMessage(roomId, writer, body);
		rs.put("resultCode", "S-1");
		rs.put("msg", "채팅 메시지가 추가되었습니다.");	
		return rs;
	}
	@RequestMapping("/chat/getMessages")
	@ResponseBody
	public List getMessages() {
		log.info("ChatController/getMessages()");
		return chatService.getMessages();
	}
	@RequestMapping("/chat/getMessagesFrom")
	@ResponseBody
	public Map getMessagesFrom(int roomId, int from) {
		log.info("ChatController/getMessagesFrom()");
		List<ChatMessage> messages = chatService.getMessagesFrom(roomId, from);
		Map rs = new HashMap<>();	
		rs.put("resultCode",  "S-1");
		rs.put("msg",  "새 메시지들을 가져왔습니다.");
		rs.put("messages",  messages);
		return rs;
	}
/*------------------------------------------------------------------*/
	@GetMapping("/Chat")
	public void chat(Model model) {
		log.info("ChatController/chat()");
		AuthB ab = null;
		try {
			ab = (AuthB)pu.getAttribute("accessInfo");
			if(ab != null) {
				log.info("ab != null");
				ab.setPmbName(enc.aesDecode(ab.getPmbName(), ab.getPmbCode()));
				log.info("==================================");
				log.info("@ChatController, GET Chat / Username : " + ab.getPmbName());
			}
		} catch (Exception e) {e.printStackTrace();}
		model.addAttribute("userid", ab.getPmbName());
	}
}
