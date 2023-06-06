package com.pms.main;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ChatMessage {
	private int id;
	private int roomId;
	private String writer;
	private String body;
	
	
}
