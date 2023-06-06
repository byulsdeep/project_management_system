package com.pms.main;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import com.pms.beans.AulB;
import com.pms.beans.AuthB;
import com.pms.beans.JobB;
import com.pms.beans.MethodB;
import com.pms.beans.MoJoB;
import com.pms.beans.MouB;
import com.pms.beans.ProListB;
import com.pms.beans.ProMemB;
import com.pms.beans.ProjectB;
import com.pms.services.Main;
import com.pms.services.Management;
import com.pms.services.Project;

@RestController
public class APIController {
	@Autowired
	private Project pro;
	@Autowired
	private Main main;
	@Autowired
	private Management mgr;
	
	@PostMapping("/InsProject")
	public List<AuthB> insProject(Model model, @ModelAttribute ProjectB pb) {
		System.out.println("InsProject");	
		System.out.println(pb.getProjectCode());
		System.out.println(pb.getProjectName());
		System.out.println(pb.getProjectComment());
		System.out.println(pb.getStartDate());
		System.out.println(pb.getEndDate());
		System.out.println(pb.getIsVisible());
		model.addAttribute(pb);	
		this.pro.backController(1, model);
		
		return (List<AuthB>)model.getAttribute("MemberList");
	}	
	@PostMapping("/GetEmailList")
	public List<AuthB> getEmailList(Model model) {
		System.out.println("GetEmailList");	

		this.pro.backController(5, model);
		
		return (List<AuthB>)model.getAttribute("EmailList");
	}	

	@RequestMapping(value = "/IsInvited", method = RequestMethod.POST)
	public List<AulB> isInvited(Model model, @ModelAttribute AuthB ab) {
		System.out.println("IsInvited");
		model.addAttribute(ab);
		
		this.main.backController(1, model);
		
		return (List<AulB>)model.getAttribute("InviteList");
	}
	
	@RequestMapping(value = "/IsInvited2", method = RequestMethod.POST)
	public List<AulB> isInvited2(Model model, @ModelAttribute AuthB ab) {
		System.out.println("IsInvited");
		model.addAttribute(ab);
		
		this.main.backController(2, model);
		
		return (List<AulB>)model.getAttribute("SentList");
	}
	
	@RequestMapping(value = "/GetProjectList", method = RequestMethod.POST)
	public List<ProListB> getProjectList(Model model, @ModelAttribute AuthB ab) {
		System.out.println("GetProjectList");
		model.addAttribute(ab);
		
		this.main.backController(3, model);
		
		return (List<ProListB>)model.getAttribute("ProjectList");
	}
	@RequestMapping(value = "/GetFullProjectList", method = RequestMethod.POST)
	public List<ProListB> getFullProjectList(Model model, @ModelAttribute AuthB ab) {
		System.out.println("GetFullProjectList");
		model.addAttribute(ab);
		
		this.main.backController(5, model);
		
		return (List<ProListB>)model.getAttribute("ProjectList");
	}
	
	@RequestMapping(value = "/GetProjectMembers", method = RequestMethod.POST)
	public String getProjectMembers(Model model, @ModelAttribute ProjectB pb) {
		System.out.println("GetProjectMembers");
		model.addAttribute(pb);
		System.out.println(pb.getProjectCode());
		this.main.backController(4, model);
		
		return (String) model.getAttribute("ProjectMembers");
	}
	
	@RequestMapping(value = "/GetHoonList", method = RequestMethod.POST)
	public List<ProMemB> getHoonList(Model model, @ModelAttribute ProjectB pb) {
		System.out.println("GetHoonList");
		System.out.println(pb.getProjectCode());
		model.addAttribute(pb);
		this.pro.backController(4, model);
		
		return (List<ProMemB>)model.getAttribute("hoonList");
	}
	
	@PostMapping("/InviteMore")
	public String inviteMember(Model model, @ModelAttribute ProjectB pb) {
		System.out.println("InviteMore");	
		
		model.addAttribute(pb);

		pro.backController(6, model);
	
		return "management";
	}
	@PostMapping("/GetProjectDetail")
	public List<ProjectB> getProjectDetail(Model model, @ModelAttribute ProjectB pb) {
		System.out.println("GetProjectDetail");	
		model.addAttribute(pb);

		mgr.backController(1, model);
	
		return (List<ProjectB>)model.getAttribute("projectDetail");
	}
	
	//module
	@PostMapping("/UpdModule")
	public List<MouB> updModule(Model model, @ModelAttribute MouB mb) {
		System.out.println("UpdModule");	
		model.addAttribute(mb);

		mgr.backController(2, model);
	
		return (List<MouB>)model.getAttribute("moduleList");
	}
	@PostMapping("/DelModule")
	public List<MouB> delModule(Model model, @ModelAttribute MouB mb) {
		System.out.println("DelModule");	
		model.addAttribute(mb);

		mgr.backController(3, model);
	
		return (List<MouB>)model.getAttribute("moduleList");
	}
	@PostMapping("/InsModule")
	public List<MouB> insModule(Model model, @ModelAttribute MouB mb) {
		System.out.println("InsModule");	
		model.addAttribute(mb);

		mgr.backController(4, model);
	
		return (List<MouB>)model.getAttribute("moduleList");
	}
	
	//job
	@PostMapping("/UpdJob")
	public List<JobB> updJob(Model model, @ModelAttribute JobB jb) {
		System.out.println("UpdJob");	
		model.addAttribute(jb);
		mgr.backController(5, model);
		return (List<JobB>)model.getAttribute("jobList");
	}
	@PostMapping("/DelJob")
	public List<JobB> delJob(Model model, @ModelAttribute JobB jb) {
		System.out.println("DelJob");	
		model.addAttribute(jb);
		mgr.backController(6, model);
		return (List<JobB>)model.getAttribute("jobList");
	}
	@PostMapping("/InsJob")
	public List<JobB> insJob(Model model, @ModelAttribute JobB jb) {
		System.out.println("InsJob");	
		model.addAttribute(jb);
		mgr.backController(7, model);
		return (List<JobB>)model.getAttribute("jobList");
	}
	
	//MoJo
	@PostMapping("/UpdMoJo")
	public List<MoJoB> updMoJo(Model model, @ModelAttribute MoJoB mj) {
		System.out.println("UpdMoJo");	
		model.addAttribute(mj);
		mgr.backController(8, model);
		return (List<MoJoB>)model.getAttribute("mojoList");
	}
	@PostMapping("/DeleteMoJo")
	public List<MoJoB> deleteMoJo(Model model, @ModelAttribute MoJoB mj) {
		System.out.println("DeleteMoJo");	
		model.addAttribute(mj);
		mgr.backController(9, model);
		return (List<MoJoB>)model.getAttribute("mojoList");
	}
	@PostMapping("/InsMoJo")
	public List<MoJoB> insMoJo(Model model, @ModelAttribute MoJoB mj) {
		System.out.println("InsMoJo");	
		model.addAttribute(mj);
		mgr.backController(10, model);
		return (List<MoJoB>)model.getAttribute("mojoList");
	}
	
	//method
	@PostMapping("/UpdMethod")
	public List<MethodB> updMethod(Model model, @ModelAttribute MethodB mt) {
		System.out.println("UpdMethod");	
		model.addAttribute(mt);
		mgr.backController(11, model);
		return (List<MethodB>)model.getAttribute("methodList");
	}
	@PostMapping("/DelMethod")
	public List<MethodB> delMethod(Model model, @ModelAttribute MethodB mt) {
		System.out.println("DelMethod");	
		model.addAttribute(mt);
		mgr.backController(12, model);
		return (List<MethodB>)model.getAttribute("methodList");
	}
	@PostMapping("/InsMethod")
	public List<MethodB> insMethod(Model model, @ModelAttribute MethodB mt) {
		System.out.println("InsMethod");	
		model.addAttribute(mt);
		mgr.backController(13, model);
		return (List<MethodB>)model.getAttribute("methodList");
	}
	
	@PostMapping("/GetMethodsOnMJ")
	public List<MethodB> getMethodsOnMJ(Model model, @ModelAttribute MethodB mt) {
		System.out.println("GetMethodsOnMJ");	
		model.addAttribute(mt);
		mgr.backController(14, model);
		
		for(MethodB me:(List<MethodB>)model.getAttribute("methodsOnMJ")) {
			System.out.println("GetMethodsOnMJ" + me.getMethodState());
		}
		
		return (List<MethodB>)model.getAttribute("methodsOnMJ");
	}
	@PostMapping("/GetMethodsOnMJMC")
	public List<MethodB> getMethodsOnMJMC(Model model, @ModelAttribute MethodB mt) {
		System.out.println("GetMethodsOnMJMC");	
		model.addAttribute(mt);
		mgr.backController(15, model);
		
		for(MethodB me:(List<MethodB>)model.getAttribute("methodsOnMJMC")) {
			System.out.println("GetMethodsOnMJMC" + me.getMethodState());
		}
		
		return (List<MethodB>)model.getAttribute("methodsOnMJMC");
	}
	@PostMapping("/BF")
	public List<MethodB> BF(Model model, @ModelAttribute MethodB mt) {
		System.out.println("BF");	
		model.addAttribute(mt);
		mgr.backController(16, model);
		return (List<MethodB>)model.getAttribute("methods");
	}
	@PostMapping("/IN")
	public List<MethodB> IN(Model model, @ModelAttribute MethodB mt) {
		System.out.println("IN");	
		model.addAttribute(mt);
		mgr.backController(17, model);
		return (List<MethodB>)model.getAttribute("methods");
	}
	@PostMapping("/CP")
	public List<MethodB> CP(Model model, @ModelAttribute MethodB mt) {
		System.out.println("CP");	
		model.addAttribute(mt);
		mgr.backController(18, model);
		return (List<MethodB>)model.getAttribute("methods");
	}
	@PostMapping("/GetMethodsOnMC")
	public List<MethodB> getMethodsOnMC(Model model, @ModelAttribute MethodB mt) {
		System.out.println("GetMethodsOnMC");	
		model.addAttribute(mt);
		mgr.backController(19, model);
		
		for(MethodB me:(List<MethodB>)model.getAttribute("methods")) {
			System.out.println("GetMethodsOnMC" + me.getMethodState());
		}
		
		return (List<MethodB>)model.getAttribute("methods");
	}
}
