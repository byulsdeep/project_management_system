package com.pms.beans;

import java.util.List;

import lombok.Data;

@Data
public class ProjectB {
	private String projectCode;
	private String projectName;
	private String projectComment;
	private String startDate;
	private String endDate;
	private String isVisible;
	private List<ProMemB> projectMembers;
	private List<ProMemB> projectMemberss;
	private List<MouB> modules;
	private List<JobB> jobs;
	private List<MoJoB> mojos;
	private List<MethodB> methods;
}
