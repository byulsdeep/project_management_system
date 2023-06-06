package com.pms.beans;

import java.util.List;

import lombok.Data;

@Data
public class ProListB {
	private String projectCode;
	private String projectName;
	private String projectComment;
	private String startDate;
	private String endDate;
	private String isVisible;
	
	private String pmbCode;
	private String manager;
	private String managerName;
	private String position;
	
	private String pmbName;
	private String mlvCode;
	private String claCode;
	
	private String mlvName;
	private String claName;

	private List<ProjectB> projectBean;
}
