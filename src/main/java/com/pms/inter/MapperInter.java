package com.pms.inter;

import java.util.HashMap;
import java.util.List;

import com.pms.beans.AulB;
import com.pms.beans.AuthB;
import com.pms.beans.JobB;
import com.pms.beans.MethodB;
import com.pms.beans.MoJoB;
import com.pms.beans.MouB;
import com.pms.beans.ProListB;
import com.pms.beans.ProMemB;
import com.pms.beans.ProjectB;

public interface MapperInter {
	/* auth */
	public String isMemberr(AuthB ab);
	public String isAccess(AuthB ab);
	public int insAsl(AuthB ab);
	public AuthB getAccessInfo(AuthB ab);
	
	/* signup */
	public String getMaxPmbCode();
	public List<AuthB> getLevelList();
	public List<AuthB> getClassList();
	public int regMember(AuthB ab);

	/* project */
	public int insProject(ProjectB pb);
	public List<AuthB> getMemberList(AuthB ab);
	public int insProjectMembers(ProjectB pb);
	public int insOneByOne(ProMemB pm);
	
	/* new shit */
	public int insAul(AulB aul);
	public AulB isPrm(AuthB ab);
	
	/* newer shit */
	public AulB receivedInvitationInfo(AuthB ab);
	public AulB sentInvitationInfo(AuthB ab);
	
	/* MYSHIT */
	public String isInvited(AuthB ab);
	public int refusal(ProMemB pm);
	public String getInviteDate(AulB aul);
	public int refusal2(AulB aul);
	public int insSelf(ProMemB pm);
	public List<ProListB> getProjectList(AuthB ab);
	public List<ProListB> getFullProjectList(AuthB ab);
	public String getProjectMembers(ProjectB pb);
	
	public List<ProjectB> getProjectHoon(ProjectB pro);
	public List<ProjectB> getProjectMembers2(ProMemB pb);
	
	public List<ProjectB> getProjectDetail(ProjectB pro);
	
	public List<ProMemB> getProjectMemberss(HashMap map);
	public List<MouB> getModuleList(HashMap map);
	public List<JobB> getJobList(HashMap map);
	public List<MoJoB> getMoJoList(HashMap map);
	public List<MethodB> getMethodList(HashMap map);
	
	public int updModule(MouB mb);
	public int delModule(MouB mb);
	public int insModule(MouB mb);
	
	public int updJobs(JobB jb);
	public int delJobs(JobB mb);
	public int insJobs(JobB mb);
	
	public int updMoJo(MoJoB mj);
	public int delMoJo(MoJoB mj);
	public int insMoJo(MoJoB mj);
	
	public int updMethods(MethodB mt);
	public int delMethods(MethodB mt);
	public int insMethods(MethodB mt);
	
	public List<MethodB> getMethodsOnMC(MethodB mt);
	public List<MethodB> getMethodsOnMJ(MethodB mt);
	public List<MethodB> getMethodsOnMJMC(MethodB mt);
	
	public int BF(MethodB mt);
	public int IN(MethodB mt);
	public int CP(MethodB mt);

	
}
