package com.pms.main;

import java.util.Iterator;
import java.util.Locale;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import com.pms.beans.AuthB;
import com.pms.beans.ProMemB;
import com.pms.beans.ProjectB;
import com.pms.services.Authentication;
import com.pms.services.Main;
import com.pms.services.Management;
import com.pms.services.MyPage;
import com.pms.services.Notification;
import com.pms.services.Project;

import lombok.extern.log4j.Log4j;

@Controller
@Log4j
public class HomeController {
	@Autowired
	Authentication auth;
	@Autowired
	Main main;
	@Autowired
	Project pro;
	@Autowired
	Notification alert;
	@Autowired
	Management mgr;
	@Autowired
	MyPage my;
	
	private static final Logger logger = LoggerFactory.getLogger(HomeController.class);
	@RequestMapping(value = "/", method = RequestMethod.GET)
	public String home(Locale locale) {
		logger.info("Welcome home! The client locale is {}.", locale);	
		return "landing";
	}
	@RequestMapping(value = "/First", method = RequestMethod.GET)
	public ModelAndView home(HttpServletRequest req, ModelAndView mav, @ModelAttribute AuthB ab) {
		ab.setPrivateIp(req.getRemoteAddr());
		mav.addObject(ab);
		
		System.out.println("First");		
		this.auth.backController(-1, mav);
		return mav;
	}
	@RequestMapping(value = "/Access", method = RequestMethod.POST)
	public ModelAndView access(HttpServletRequest req, ModelAndView mav, @ModelAttribute AuthB ab) {
		System.out.println("Access");
		ab.setPrivateIp(req.getRemoteAddr());
		mav.addObject(ab);
		auth.backController(1, mav);
		
		return mav;
	}
	@RequestMapping(value = "/IsMember", method = RequestMethod.POST)
	public ModelAndView isMember(ModelAndView mav, @ModelAttribute AuthB ab) {
		System.out.println("IsMember");	
		mav.addObject(ab);
		this.auth.backController(4, mav);
		
		return mav;
	}
	@PostMapping("/InviteMembers")
	public ProjectB inviteMember(ModelAndView mav, @ModelAttribute ProjectB pb) {
		System.out.println("InviteMembers");		
		mav.addObject(pb);
		pro.backController(2,  mav);
		return (ProjectB) mav.getModel().get(pb);
	}
	
	@PostMapping("/Refusal")
	public ModelAndView refusal(ModelAndView mav, @ModelAttribute AuthB ab) {
		System.out.println("Refusal");	
		mav.addObject(ab);
		main.backController(1,  mav);
		return mav;
	}
	@RequestMapping(value = "/AccessOut", method = RequestMethod.POST)
	public ModelAndView accessOut(HttpServletRequest req, ModelAndView mav, @ModelAttribute AuthB ab) {
		System.out.println("AccessOut");
		ab.setPrivateIp(req.getRemoteAddr());	
		mav.addObject(ab);
		auth.backController(3, mav);		
		return mav;
	}
	@RequestMapping(value = "/MoveSignUp", method = RequestMethod.POST)
	public ModelAndView moveSignUp(ModelAndView mav) {
		System.out.println("MoveSignUp");
		this.auth.backController(0, mav);
		return mav;
	}
	
	@RequestMapping(value = "/MoveMain", method = RequestMethod.POST)
	public ModelAndView moveMain(HttpServletRequest req, ModelAndView mav, @ModelAttribute AuthB ab) {
		System.out.println("MoveMain");
		ab.setPrivateIp(req.getRemoteAddr());
		mav.addObject(ab);
		this.main.backController(0, mav);
		return mav;
	}
	@RequestMapping(value = "/MoveProject", method = RequestMethod.POST)
	public ModelAndView moveNewProject(HttpServletRequest req, ModelAndView mav, @ModelAttribute AuthB ab) {
		System.out.println("MoveProject");
		ab.setPrivateIp(req.getRemoteAddr());
		mav.addObject(ab);
		this.pro.backController(0, mav);
		return mav;
	}
	@RequestMapping(value = "/MoveAlert", method = RequestMethod.POST)
	public ModelAndView moveAlert(HttpServletRequest req, ModelAndView mav, @ModelAttribute AuthB ab) {
		System.out.println("MoveAlert");
		ab.setPrivateIp(req.getRemoteAddr());
		mav.addObject(ab);
		this.alert.backController(0, mav);
		return mav;
	}	
	@RequestMapping(value = "/MoveMgr", method = RequestMethod.POST)
	public ModelAndView moveMgr(HttpServletRequest req, ModelAndView mav, @ModelAttribute AuthB ab) {
		System.out.println("MoveMgr");
		ab.setPrivateIp(req.getRemoteAddr());
		mav.addObject(ab);
		this.pro.backController(3, mav);
		return mav;
	}	
	@RequestMapping(value = "/MoveMyPage", method = RequestMethod.POST)
	public ModelAndView moveMyPage(HttpServletRequest req, ModelAndView mav, @ModelAttribute AuthB ab) {
		System.out.println("MoveMyPage");
		ab.setPrivateIp(req.getRemoteAddr());
		mav.addObject(ab);
		this.my.backController(0, mav);
		return mav;
	}
	@RequestMapping(value = "/RegMember", method = RequestMethod.POST)
	public ModelAndView regMember(HttpServletRequest req, ModelAndView mav, @ModelAttribute AuthB ab) {
		System.out.println("RegMember");
		ab.setPrivateIp(req.getRemoteAddr());
		mav.addObject(ab);
		auth.backController(2, mav);	
		return mav;
	}
	@RequestMapping(value = "/ProgressMgr", method = RequestMethod.POST)
	public ModelAndView progressMgr(ModelAndView mav, @ModelAttribute ProjectB pb) {
		System.out.println("ProgressMgr");
		System.out.println(pb.getProjectCode());
		mav.addObject(pb);
		mgr.backController(1, mav);	
		return mav;
	}
	@RequestMapping(value = "/ResultMgr", method = RequestMethod.POST)
	public ModelAndView resultMgr(ModelAndView mav, @ModelAttribute ProjectB pb) {
		System.out.println("ResultMgr");
		System.out.println(pb.getProjectCode());
		mav.addObject(pb);
		mgr.backController(2, mav);	
		return mav;
	}
	
	
/* -------------------------------------------------------------------------------------------------------------------------------------------------------------- */
	
	/*@GetMapping("/chat")
	public void chat(Model model) {
		
		CustomUser user = (CustomUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		
		
		log.info("==================================");
		log.info("@ChatController, GET Chat / Username : " + user.getUsername());
		
		model.addAttribute("userid", user.getUsername());
	}
	
/* -------------------------------------------------------------------------------------------------------------------------------------------------------------- */

	@RequestMapping(value = "/RequestParam", method = RequestMethod.POST)
	public String access1(@RequestParam("pmbCode") String pmbCode) {
		System.out.println("@RequestParam---getParameter({})");	
		System.out.println(pmbCode);	
		return "home";
	}
	@PostMapping("/PostMapping")
	public String access2(@RequestParam("pmbCode") String pmbCode) {
		System.out.println("@PostMapping");	
		System.out.println(pmbCode);	
		return "home";
	}
	@RequestMapping(value = "/Map", method = RequestMethod.POST)
	public String access3(@RequestParam Map<String, String> clientData) {
		System.out.println("@RequestParam---Map");	
		System.out.println(clientData.get("pmbCode"));
		System.out.println(clientData.get("pmbPassword"));	
		Iterator<String> itr = clientData.keySet().iterator();
		System.out.println("@RequestParam---Map-hasNext");	
		while(itr.hasNext()) {
			String key = (String)itr.next();
			System.out.println(key + " : " + clientData.get(key)); 
		}	
		Iterator<String> itrtr = clientData.keySet().iterator();
		System.out.println("@RequestParam---Map-forLoop");	
		for(int i=0; i < clientData.size(); i++) {
			String key = (String)itrtr.next();
			System.out.println(key + " : " + clientData.get(key));
		}	
		System.out.println("@RequestParam---Map-short-hand_forLoop");	
		for(String key : clientData.keySet()) {
			System.out.println(key + " : " + clientData.get(key));
		}	
		Iterator<Map.Entry<String, String>> it = clientData.entrySet().iterator();	
		System.out.println("@RequestParam---Map-Entry");	
		while(it.hasNext()) {
			Map.Entry<String, String> entry = it.next();
			System.out.println(entry.getKey() + " : " + entry.getValue());
		}		
		return "home";
	}	
	@RequestMapping(value = "/Access/{pmbCode}/{pmbPassword}", method = RequestMethod.GET)
	public String access4(@PathVariable("storeCode") String stCode) {
		System.out.println("@PathVariable---/jobCode/{}/{}/{}");
		System.out.println(stCode);		
		return "home";
	}	
	@GetMapping("/GetMapping")
	public String access5(@RequestParam("pmbCode") String stCode) {
		System.out.println("@GetMapping");
		System.out.println(stCode);		
		return "home";
	}	
}
