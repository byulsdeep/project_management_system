package com.pms.beans;

import lombok.Data;

@Data
public class AulB {
	private String sender;
	private String senderName;
	private String recipient;
	private String recipientName;
	private String inviteDate;
	private String expireDate;
	private String authResult;
	private String authResultName;
	private String projectCode;
	
	/* myshit */
	private String projectName;
}
