package com.pms.beans;

import lombok.Data;

@Data
public class AuthB {
	private String message;
	private String pmbCode;
	private String pmbPassword;
	private String pmbName;
	private String email;
	
	private String date;
	
	private int action;
	
	private String mlvCode;
	private String mlvName;
	private String claCode;
	private String claName;
	
	private String publicIp;
	private String privateIp;
	
	private String authCode;
}

