package com.pms.services;

import java.io.UnsupportedEncodingException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;

import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;
import org.springframework.web.servlet.ModelAndView;

import com.pms.beans.AuthB;
import com.pms.beans.JobB;
import com.pms.beans.MethodB;
import com.pms.beans.MoJoB;
import com.pms.beans.MouB;
import com.pms.beans.ProMemB;
import com.pms.beans.ProjectB;
import com.pms.inter.ServicesRule;
import com.pms.utils.Encryption;
import com.pms.utils.ProjectUtils;

@Service
public class Management implements ServicesRule{	
	@Autowired
	private SqlSessionTemplate session;
	@Autowired 
	private ProjectUtils pu;
	@Autowired
	private Authentication auth;
	@Autowired
	private Encryption enc;
	
	public Management() {}

	public void backController(int serviceCode, ModelAndView mav) {
		if(auth.isSession()) {
		System.out.println("Management/backController");
			switch(serviceCode) {
			case 0:
				this.mainCtl(mav);
				break;
			case 1:
				this.progressMgr(mav);
				break;
			case 2:
				this.resultMgr(mav);
				break;	
			}
		} else {
			mav.setViewName("home");
		}
	}
	
	public void backController(int serviceCode, Model model) {
		if(auth.isSession()) {
			System.out.println("Management/backController");
			switch(serviceCode) {
			case 1:
				this.getProjectDetail(model);
				break;
			case 2:
				this.updModule(model);
				break;
			case 3:
				this.delModule(model);
				break;
			case 4:
				this.insModule(model);
				break;
			case 5:
				this.updJob(model);
				break;
			case 6:
				this.delJob(model);
				break;
			case 7:
				this.insJob(model);
				break;
			case 8:
				this.updMoJo(model);
				break;
			case 9:
				this.deleteMoJo(model);
				break;
			case 10:
				this.insMoJo(model);
				break;
			case 11:
				this.updMethod(model);
				break;
			case 12:
				this.delMethod(model);
				break;
			case 13:
				this.insMethod(model);
				break;
			case 14:
				this.getMethodsOnMJ(model);
				break;	
			case 15:
				this.getMethodsOnMJMC(model);
				break;
			case 16:
				this.BF(model);
				break;	
			case 17:
				this.IN(model);
				break;	
			case 18:
				this.CP(model);
				break;	
			case 19:
				this.getMethodsOnMC(model);
				break;	
			}
		}
	}
	private void getMethodsOnMC(Model model) {
		MethodB mt = (MethodB)model.getAttribute("methodB");
		List<MethodB> list = session.selectList("getMethodsOnMC", mt);
		model.addAttribute("methods", list);
	}
	private void BF(Model model) {
		System.out.println("BACKBF");	
		MethodB mt = (MethodB)model.getAttribute("methodB");
		session.update("BF", mt);
		List<MethodB> list = session.selectList("getMethodList", mt);
		model.addAttribute("methods", list);
	}
	private void IN(Model model) {
		System.out.println("BACKIN");	

		MethodB mt = (MethodB)model.getAttribute("methodB");
		session.update("IN", mt);
		List<MethodB> list = session.selectList("getMethodList", mt);
		model.addAttribute("methods", list);
	}
	private void CP(Model model) {
		System.out.println("BACKCP");	

		MethodB mt = (MethodB)model.getAttribute("methodB");
		session.update("CP", mt);
		List<MethodB> list = session.selectList("getMethodList", mt);
		model.addAttribute("methods", list);
	}
	private void getMethodsOnMJ(Model model) {
		MethodB mt = (MethodB)model.getAttribute("methodB");
		List<MethodB> list = session.selectList("getMethodsOnMJ", mt);
		model.addAttribute("methodsOnMJ", list);
	}
	private void getMethodsOnMJMC(Model model) {
		MethodB mt = (MethodB)model.getAttribute("methodB");
		List<MethodB> list = session.selectList("getMethodsOnMJMC", mt);
		model.addAttribute("methodsOnMJMC", list);
	}
	/*private void getProjectDetail(Model model) {
		System.out.println("Management/getProjectDetail");
		List<ProjectB> projectDetail = null;
		ProjectB pb = (ProjectB)model.getAttribute("projectB");
		projectDetail = this.session.selectList("getProjectDetail", pb);
		
		for(int i = 0; i < projectDetail.get(0).getMojos().size(); i++) {
			try {
				projectDetail.get(0).getMojos().get(i).setPmbName(enc.aesDecode(projectDetail.get(0).getMojos().get(i).getPmbName(), projectDetail.get(0).getMojos().get(i).getPmbCode()));
			} catch (InvalidKeyException | UnsupportedEncodingException | NoSuchAlgorithmException
					| NoSuchPaddingException | InvalidAlgorithmParameterException | IllegalBlockSizeException
					| BadPaddingException e) {e.printStackTrace();}
		}
		model.addAttribute("projectDetail", projectDetail);
	}*/
	
	private void progressMgr(ModelAndView mav) {
		System.out.println("Management/progressMgr");
		mav.setViewName("progress");
		ProjectB pb = (ProjectB)mav.getModel().get("projectB");
		AuthB ab = null;
		try {ab = (AuthB)pu.getAttribute("accessInfo");} catch (Exception e1) {}
		
		List<ProjectB> projectDetail = this.session.selectList("getProjectDetail", pb);	
		List<ProjectB> fullProject = this.session.selectList("getProjectHoon", ab);
		for(int i = 0; i < projectDetail.get(0).getMojos().size(); i++) {
			try {
				projectDetail.get(0).getMojos().get(i).setPmbName(enc.aesDecode(projectDetail.get(0).getMojos().get(i).getPmbName(), projectDetail.get(0).getMojos().get(i).getPmbCode()));
			} catch (InvalidKeyException | UnsupportedEncodingException | NoSuchAlgorithmException
					| NoSuchPaddingException | InvalidAlgorithmParameterException | IllegalBlockSizeException
					| BadPaddingException e) {e.printStackTrace();}
		}
		mav.addObject("projectDetail", makeProjectList(projectDetail.get(0)));
		mav.addObject("actionName", makeActionList(projectDetail.get(0)));
		mav.addObject("mvcStyle", makeMvcStyle(projectDetail.get(0)));
		mav.addObject("methodName", makeMethodName(projectDetail.get(0)));
		mav.addObject("projectOptions", makeProjectOptions(fullProject));
	}
	
	private String makeProjectOptions(List<ProjectB> list) {
		StringBuffer sb = new StringBuffer();
		for(ProjectB pb: list) {
			sb.append("<option value=\"" + pb.getProjectCode() + "\"> " + pb.getProjectName() + " </option>");
		}
		return sb.toString();
	}
	
	private String makeMethodName(ProjectB pb) {
		StringBuffer sb = new StringBuffer();
		String st;
		int i = 0;
		for(MethodB mt: pb.getMethods()) {		
				st = mt.getModuleCode() + ":" + mt.getJobCode() + ":" + mt.getMethodCode() + ":" + mt.getMcCode();			
			if(mt.getMethodState().equals("CP")) {
				sb.append("<div class=\"stn cp\" \">" + mt.getMethodName() + ""			
						+ "<input id=\"method" + i + "\" type=\"hidden\" value=\"" + st + "\">"
						+ "<div><input class=\"cp stn\" type=\"button\" value=\"상태변경\" onClick=\"changeState(" + i + ")\"></div>"
						+ "<div><input class=\"cp stn\" type=\"button\" value=\"파일추가\" onClick=\"upload(" + i + ")\"></div>"
								+ "</div>");
			} else if (mt.getMethodState().equals("IN")) {
				sb.append("<div class=\"stn in\" \">" + mt.getMethodName() + ""			
						+ "<input id=\"method" + i + "\" type=\"hidden\" value=\"" + st + "\">"
						+ "<div><input class=\"in stn\" type=\"button\" value=\"상태변경\" onClick=\"changeState(" + i + ")\"></div>"
						+ "<div><input class=\"in stn\" type=\"button\" value=\"파일추가\" onClick=\"upload(" + i + ")\"></div>"
								+ "</div>");
			} else {
				sb.append("<div class=\"stn button\" \">" + mt.getMethodName() + ""			
						+ "<input id=\"method" + i + "\" type=\"hidden\" value=\"" + st + "\">"
						+ "<div><input class=\"button stn\" type=\"button\" value=\"상태변경\" onClick=\"changeState(" + i + ")\"></div>"
						+ "<div><input class=\"button stn\" type=\"button\" value=\"파일추가\" onClick=\"upload(" + i + ")\"></div>"
								+ "</div>");
			}	
			i++;	
		}
		sb.append("<input type=\"hidden\" name=\"ang\" value=\"" + i + "\">");
		return sb.toString();
	}
	private String makeMvcStyle(ProjectB pb) {
		StringBuffer sb = new StringBuffer();
		int bfct = 0; int bfmo = 0; int bfrd = 0; int bfvi = 0;
		int inct = 0; int inmo = 0; int inrd = 0; int invi = 0;
		int cpct = 0; int cpmo = 0; int cprd = 0; int cpvi = 0;
		int ct = 0; int mo = 0; int rd = 0; int vi = 0; int el = 0;
		for(int i = 0; i < pb.getMethods().size(); i++) {
			if(pb.getMethods().get(i).getMcCode().equals("CT")) {
				ct++;
			} else if (pb.getMethods().get(i).getMcCode().equals("MO")) {
				mo++;
			} else if (pb.getMethods().get(i).getMcCode().equals("RD")) {
				rd++;
			} else if (pb.getMethods().get(i).getMcCode().equals("VI")) {
				vi++;
			} else {
				el++;
			}
			
			if(pb.getMethods().get(i).getMethodState().equals("BF") && pb.getMethods().get(i).getMcCode().equals("CT")) {
				bfct++;
			} else if(pb.getMethods().get(i).getMethodState().equals("BF") && pb.getMethods().get(i).getMcCode().equals("MO")) {
				bfmo++;
			} else if(pb.getMethods().get(i).getMethodState().equals("BF") && pb.getMethods().get(i).getMcCode().equals("RD")) {
				bfrd++;
			} else if(pb.getMethods().get(i).getMethodState().equals("BF") && pb.getMethods().get(i).getMcCode().equals("VI")) {
				bfvi++;
			} else if(pb.getMethods().get(i).getMethodState().equals("IN") && pb.getMethods().get(i).getMcCode().equals("CT")) {
				inct++;
			} else if(pb.getMethods().get(i).getMethodState().equals("IN") && pb.getMethods().get(i).getMcCode().equals("MO")) {
				inmo++;
			} else if(pb.getMethods().get(i).getMethodState().equals("IN") && pb.getMethods().get(i).getMcCode().equals("RD")) {
				inrd++;
			} else if(pb.getMethods().get(i).getMethodState().equals("IN") && pb.getMethods().get(i).getMcCode().equals("VI")) {
				invi++;
			} else if(pb.getMethods().get(i).getMethodState().equals("CP") && pb.getMethods().get(i).getMcCode().equals("CT")) {
				cpct++;
			} else if(pb.getMethods().get(i).getMethodState().equals("CP") && pb.getMethods().get(i).getMcCode().equals("MO")) {
				cpmo++;
			} else if(pb.getMethods().get(i).getMethodState().equals("CP") && pb.getMethods().get(i).getMcCode().equals("RD")) {
				cprd++;
			} else if(pb.getMethods().get(i).getMethodState().equals("CP") && pb.getMethods().get(i).getMcCode().equals("VI")) {
				cpvi++;
			}
		}
		if(cpct == ct && cpct != 0) {
			sb.append("		   	<div class=\"btn cp\" onClick=\"getMethodsOnMJMC('CT')\"> CONTROLLER"
					+ "<br>개발전: " + bfct + "<br>개발진행중: " + inct + "<br>개발완료: " + cpct + " / " + ct + ""
					+ "</div>\r\n");
		} else if(inct > 0) {
			sb.append("		   	<div class=\"btn in\" onClick=\"getMethodsOnMJMC('CT')\"> CONTROLLER"
					+ "<br>개발전: " + bfct + "<br>개발진행중: " + inct + "<br>개발완료: " + cpct + " / " + ct + ""
					+ "</div>\r\n");		
		} else {
			sb.append("		   	<div class=\"btn button\" onClick=\"getMethodsOnMJMC('CT')\"> CONTROLLER"
					+ "<br>개발전: " + bfct + "<br>개발진행중: " + inct + "<br>개발완료: " + cpct + " / " + ct + ""
					+ "</div>\r\n");
		}
		
		if(cpmo == mo && cpmo != 0) {
			sb.append("  		<div class=\"btn cp\" onClick=\"getMethodsOnMJMC('MO')\"> SERVICES"
					+ "<br>개발전: " + bfmo + "<br>개발진행중: " + inmo + "<br>개발완료: " + cpmo + " / " + mo + ""
					+ "</div>\r\n");
		} else if(inmo > 0) {
			sb.append("  		<div class=\"btn in\" onClick=\"getMethodsOnMJMC('MO')\"> SERVICES"
					+ "<br>개발전: " + bfmo + "<br>개발진행중: " + inmo + "<br>개발완료: " + cpmo + " / " + mo + ""
					+ "</div>\r\n");
		} else {
			sb.append("  		<div class=\"btn button\" onClick=\"getMethodsOnMJMC('MO')\"> SERVICES"
					+ "<br>개발전: " + bfmo + "<br>개발진행중: " + inmo + "<br>개발완료: " + cpmo + " / " + mo + ""
					+ "</div>\r\n");
		}

		if(cprd == rd && cprd != 0) {
			sb.append("  		<div class=\"btn cp\" onClick=\"getMethodsOnMJMC('RD')\"> DAO"
					+ "<br>개발전: " + bfrd + "<br>개발진행중: " + inrd + "<br>개발완료: " + cprd + " / " + rd + ""
					+ "</div>\r\n");
		} else if(inrd > 0) {
			sb.append("  		<div class=\"btn in\" onClick=\"getMethodsOnMJMC('RD')\"> DAO"
					+ "<br>개발전: " + bfrd + "<br>개발진행중: " + inrd + "<br>개발완료: " + cprd + " / " + rd + ""
					+ "</div>\r\n");
		} else {
			sb.append("  		<div class=\"btn button\" onClick=\"getMethodsOnMJMC('RD')\"> DAO"
					+ "<br>개발전: " + bfrd + "<br>개발진행중: " + inrd + "<br>개발완료: " + cprd + " / " + rd + ""
					+ "</div>\r\n");
		}
		
		if(cpvi == vi && cpvi != 0) {
			sb.append("  		<div class=\"btn cp\" onClick=\"getMethodsOnMJMC('VI')\"> VIEW"
					+ "<br>개발전: " + bfvi + "<br>개발진행중: " + invi + "<br>개발완료: " + cpvi + " / " + vi + ""
					+ "</div>");
		} else if(invi > 0) {
			sb.append("  		<div class=\"btn in\" onClick=\"getMethodsOnMJMC('VI')\"> VIEW"
					+ "<br>개발전: " + bfvi + "<br>개발진행중: " + invi + "<br>개발완료: " + cpvi + " / " + vi + ""
					+ "</div>");
		} else {
			sb.append("  		<div class=\"btn button\" onClick=\"getMethodsOnMJMC('VI')\"> VIEW"
					+ "<br>개발전: " + bfvi + "<br>개발진행중: " + invi + "<br>개발완료: " + cpvi + " / " + vi + ""
					+ "</div>");
		}
		return sb.toString();
	}
	private String makeActionList(ProjectB pb) {
		StringBuffer sb = new StringBuffer();
		String st;
		int i = 0;
		AuthB ab = null;
		try {ab = (AuthB)pu.getAttribute("accessInfo");} catch (Exception e) {e.printStackTrace();}
		for(MoJoB mb: pb.getMojos()) {
			st = mb.getModuleCode() + ":" + mb.getJobCode();
			if(mb.getPmbCode().equals(ab.getPmbCode())) {
				sb.append("<div onClick=\"getMethodsOnMJ(this)\" class=\"stn my\">" + mb.getModuleName() + " " + mb.getJobName() + ""
						+ "<input id=\"action" + i + "\" type=\"hidden\" value=\"" + st + "\"></div>");
			} else {
				sb.append("<div onClick=\"getMethodsOnMJ(this)\" class=\"stn button\">" + mb.getModuleName() + " " + mb.getJobName() + ""
						+ "<input id=\"action" + i + "\" type=\"hidden\" value=\"" + st + "\"></div>");
			}
			i++;
		}	
		sb.append("<input type=\"hidden\" name=\"gimotti\" value=\"" + i + "\">");
		return sb.toString();
	}
	
	private String makeProjectList(ProjectB pb) {
		StringBuffer sb = new StringBuffer();
		String st = null;
		sb.append("  			<div id=\"subProjectList\" class=\"null\" value=\"null\">\r\n");	
		for(int i = pb.getProjectMemberss().size() - 1; i >= 0; i--) {
			if(!pb.getProjectMemberss().get(i).getIsAccept().equals("AC")) {
				pb.getProjectMemberss().remove(i);
			}
		}
		sb.append("  				<div id=\"projectThumb0\" class=\"projectThumbOn\" value=\"null\">\r\n"
				+ "  					<div id=\"projectName\" class=\"bigAss\" value=\"null\">"+ pb.getProjectName() +"</div>\r\n"
				+ "  					<div id=\"projectCode0\" class=\"projectCode\" value=\"null\">" + pb.getProjectCode() + "</div>\r\n");		
		sb.append("  					<div id=\"memberCount0\" class=\"memberCount\" value=\"null\">멤버수 :" + pb.getProjectMemberss().size() + "</div>\r\n");
		for(int i = 0; i < pb.getProjectMemberss().size(); i++) {
			try {
				if(pb.getProjectMemberss().get(i).getPosition().equals("MG")) {
									st = "<div id=\"managerName0\" class=\"managerName\" value=\"null\">매니저 :" + enc.aesDecode(pb.getProjectMemberss().get(i).getPmbName(), pb.getProjectMemberss().get(i).getPmbCode()) + "</div>\r\n";
					break;
				} else {
									st = "<div id=\"managerName0\" class=\"managerName\" value=\"null\">매니저 : 없음 </div>\r\n";
				}
			} catch (InvalidKeyException | UnsupportedEncodingException | NoSuchAlgorithmException
					| NoSuchPaddingException | InvalidAlgorithmParameterException | IllegalBlockSizeException
					| BadPaddingException e) {e.printStackTrace();}
		}
		sb.append(st);
		sb.append("  					<div id=\"period0\" class=\"period\" value=\"null\">기간: " + pb.getStartDate().substring(0,10) + " ~ " + pb.getEndDate().substring(0,10) + "</div>\r\n"
				+ "  					<div id=\"projectComment0\" class=\"projectComment\" value=\"null\">상세: " + pb.getProjectComment() + "</div>\r\n"
				+ "  					<div id=\"projectComment0\" class=\"projectComment\" value=\"null\">모듈수: " + pb.getModules().size() + "</div>\r\n"
				+ "  					<div id=\"projectComment0\" class=\"projectComment\" value=\"null\">잡수: " + pb.getJobs().size() + "</div>\r\n"
				+ "  					<div id=\"projectComment0\" class=\"stn button\" value=\"null\">액션수: " + pb.getMojos().size() + "</div>\r\n");
		AuthB ab = null;
		try {ab = (AuthB)pu.getAttribute("accessInfo");} catch (Exception e) {e.printStackTrace();}
		int myMJ = 0;
		for(int i = 0; i < pb.getMojos().size(); i++) {
			if(pb.getMojos().get(i).getPmbCode().equals(ab.getPmbCode())) {
				myMJ++;
			}
		}
		sb.append("  					<div id=\"projectComment0\" class=\"stn my\" value=\"null\">내액션수: " + myMJ + "</div>\r\n"
				+ "  					<div id=\"projectComment0\" class=\"projectComment\" value=\"null\">메소드수: " + pb.getMethods().size() + "</div>\r\n"
				+ "  				</div>\r\n");
		sb.append( "			</div>");
		return sb.toString();
	}
	private void resultMgr(ModelAndView mav) {
		System.out.println("Management/resultMgr");
		mav.setViewName("result");
	}
	
	private void updModule(Model model) {
		HashMap<String, String> map = new HashMap<String, String>();
		MouB mb = (MouB)model.getAttribute("mouB");
		this.session.update("updModule", mb);
		map.put("projectCode", mb.getProjectCode());
		List<MouB> moduleList = this.session.selectList("getModuleList", map);
		model.addAttribute("moduleList", moduleList);
	}
	private void delModule(Model model) {
		HashMap<String, String> map = new HashMap<String, String>();
		MouB mb = (MouB)model.getAttribute("mouB");
		this.session.update("delModule", mb);
		map.put("projectCode", mb.getProjectCode());
		List<MouB> moduleList = this.session.selectList("getModuleList", map);
		model.addAttribute("moduleList", moduleList);
	}
	private void insModule(Model model) {
		HashMap<String, String> map = new HashMap<String, String>();
		MouB mb = (MouB)model.getAttribute("mouB");
		this.session.update("insModule", mb);
		map.put("projectCode", mb.getProjectCode());
		List<MouB> moduleList = this.session.selectList("getModuleList", map);
		model.addAttribute("moduleList", moduleList);
	}
	private void updJob(Model model) {
		HashMap<String, String> map = new HashMap<String, String>();
		JobB jb = (JobB)model.getAttribute("jobB");
		this.session.update("updJobs", jb);
		map.put("projectCode", jb.getProjectCode());
		List<MouB> jobList = this.session.selectList("getJobList", map);
		model.addAttribute("jobList", jobList);
	}
	private void delJob(Model model) {
		HashMap<String, String> map = new HashMap<String, String>();
		JobB jb = (JobB)model.getAttribute("jobB");
		this.session.update("delJobs", jb);
		map.put("projectCode", jb.getProjectCode());
		List<MouB> jobList = this.session.selectList("getJobList", map);
		model.addAttribute("jobList", jobList);
	}
	private void insJob(Model model) {
		HashMap<String, String> map = new HashMap<String, String>();
		JobB jb = (JobB)model.getAttribute("jobB");
		this.session.update("insJobs", jb);
		map.put("projectCode", jb.getProjectCode());
		List<MouB> jobList = this.session.selectList("getJobList", map);
		model.addAttribute("jobList", jobList);
	}
	private void updMoJo(Model model) {
		HashMap<String, String> map = new HashMap<String, String>();
		MoJoB mj = (MoJoB)model.getAttribute("moJoB");
		this.session.update("updMoJo", mj);
		map.put("projectCode", mj.getProjectCode());
		List<MoJoB> mojoList = this.session.selectList("getMoJoList", map);
		model.addAttribute("mojoList", mojoList);
	}
	private void deleteMoJo(Model model) {
		HashMap<String, String> map = new HashMap<String, String>();
		MoJoB mj = (MoJoB)model.getAttribute("moJoB");
		this.session.update("delMoJo", mj);
		map.put("projectCode", mj.getProjectCode());
		List<MoJoB> mojoList = this.session.selectList("getMoJoList", map);
		model.addAttribute("mojoList", mojoList);
	}
	private void insMoJo(Model model) {
		HashMap<String, String> map = new HashMap<String, String>();
		MoJoB mj = (MoJoB)model.getAttribute("moJoB");
		this.session.update("insMoJo", mj);
		map.put("projectCode", mj.getProjectCode());
		List<MoJoB> mojoList = this.session.selectList("getMoJoList", map);
		model.addAttribute("mojoList", mojoList);
	}
	private void updMethod(Model model) {
		HashMap<String, String> map = new HashMap<String, String>();
		MethodB mt = (MethodB)model.getAttribute("methodB");
		this.session.update("updMethods", mt);
		map.put("projectCode", mt.getProjectCode());
		List<MethodB> methodList = this.session.selectList("getMethodList", map);
		model.addAttribute("methodList", methodList);
	}
	private void delMethod(Model model) {
		HashMap<String, String> map = new HashMap<String, String>();
		MethodB mt = (MethodB)model.getAttribute("methodB");
		this.session.update("delMethods", mt);
		map.put("projectCode", mt.getProjectCode());
		List<MethodB> methodList = this.session.selectList("getMethodList", map);
		model.addAttribute("methodList", methodList);
	}
	private void insMethod(Model model) {
		HashMap<String, String> map = new HashMap<String, String>();
		MethodB mt = (MethodB)model.getAttribute("methodB");
		this.session.update("insMethods", mt);
		map.put("projectCode", mt.getProjectCode());
		List<MethodB> methodList = this.session.selectList("getMethodList", map);
		model.addAttribute("methodList", methodList);
	}
	
	private void getProjectDetail(Model model) {
		System.out.println("Management/getProjectDetail");
		List<ProjectB> projectDetail = null;
		ProjectB pb = (ProjectB)model.getAttribute("projectB");
		projectDetail = this.session.selectList("getProjectDetail", pb);
		
		for(int i = 0; i < projectDetail.get(0).getMojos().size(); i++) {
			try {
				projectDetail.get(0).getMojos().get(i).setPmbName(enc.aesDecode(projectDetail.get(0).getMojos().get(i).getPmbName(), projectDetail.get(0).getMojos().get(i).getPmbCode()));
			} catch (InvalidKeyException | UnsupportedEncodingException | NoSuchAlgorithmException
					| NoSuchPaddingException | InvalidAlgorithmParameterException | IllegalBlockSizeException
					| BadPaddingException e) {e.printStackTrace();}
		}
		model.addAttribute("projectDetail", projectDetail);
	}
	
	private void mainCtl(ModelAndView mav) {
		System.out.println("Management/mainCtl");
		
		List<ProjectB> hoonList = null;
		AuthB session = null;
		
		try {
			session = (AuthB)this.pu.getAttribute("accessInfo");
		} catch (Exception e) {e.printStackTrace();}
		
		AuthB ab = (AuthB)mav.getModel().get("authB");
		
		hoonList = this.session.selectList("getProjectHoon", session);
		
		
		mav.addObject("hoonList", hoonList);
		mav.setViewName("management");
	}
	
	
}
