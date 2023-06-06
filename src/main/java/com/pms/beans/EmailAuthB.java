package com.pms.beans;

import lombok.Data;

@Data
public class EmailAuthB {
	private String inviteDate;
	private String sender;
	private String recipient;
	private String acceptCode;
	private String aulResultCode;
	private String emailCode;
}
