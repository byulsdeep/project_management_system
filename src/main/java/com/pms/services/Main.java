package com.pms.services;

import java.io.UnsupportedEncodingException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.List;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;

import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;
import org.springframework.web.servlet.ModelAndView;

import com.pms.beans.AulB;
import com.pms.beans.AuthB;
import com.pms.beans.ProListB;
import com.pms.beans.ProMemB;
import com.pms.beans.ProjectB;
import com.pms.inter.ServicesRule;
import com.pms.utils.Encryption;
import com.pms.utils.ProjectUtils;

@Service
public class Main implements ServicesRule{

	@Autowired
	private SqlSessionTemplate session;
	@Autowired
	private Encryption enc;
	@Autowired 
	private ProjectUtils pu;
	@Autowired
	private Authentication auth;

	public Main() {}

	public void backController(int serviceCode, ModelAndView mav) {
		if(auth.isSession()) {
			System.out.println("Main/backController");
			switch(serviceCode) {
			case 0:
				this.mainCtl(mav);
				break;
			case 1:
				this.refusal(mav);
				break;
			}
		} else {
			mav.setViewName("home");
		}
	}

	public void backController(int serviceCode, Model model) {
		if(auth.isSession()) {
			System.out.println("Main/backController");
			switch(serviceCode) {
			case 1: 
				this.isInvited(model);
				break;
			case 2: 
				this.isInvited2(model);
				break;	
			case 3: 
				this.getProjectList(model);
				break;	
			case 4: 
				this.getProjectMembers(model);
				break;		
			case 5:
				this.getFullProjectList(model);
				break;	
			}
		}
	}

	private void mainCtl(ModelAndView mav) {
		System.out.println("Main/mainCtl");
		String page = "main";
		mav.setViewName(page);
	}

	private void isInvited(Model model) {		
		System.out.println("Main/IsInvited");
		List<AulB> inviteList = null;
		AuthB ab = (AuthB)model.getAttribute("authB");

		try {
			ab.setPmbCode(((AuthB)this.pu.getAttribute("accessInfo")).getPmbCode());
		} catch (Exception e) {e.printStackTrace();}

		if(this.convertToBool(Integer.parseInt(this.session.selectOne("isInvited", ab)))) {
			System.out.println("invited=true");
			inviteList = this.session.selectList("receivedInvitationInfo", ab);
			
			for(AulB aul : inviteList) {
				try {
					aul.setSenderName(this.enc.aesDecode(aul.getSenderName(), aul.getSender()));
				} catch (InvalidKeyException | UnsupportedEncodingException | NoSuchAlgorithmException
						| NoSuchPaddingException | InvalidAlgorithmParameterException | IllegalBlockSizeException
						| BadPaddingException e) {e.printStackTrace();}
			}
		}
				
		model.addAttribute("InviteList", inviteList);

	}
	private void isInvited2(Model model) {		
		System.out.println("Main/IsInvited2");
		List<AulB> sentList = null;
		AuthB ab = (AuthB)model.getAttribute("authB");

		try {
			ab.setPmbCode(((AuthB)this.pu.getAttribute("accessInfo")).getPmbCode());
		} catch (Exception e) {e.printStackTrace();}		
			sentList = this.session.selectList("sentInvitationInfo", ab);
			
			for(AulB aul : sentList) {
				try {
					aul.setRecipientName(this.enc.aesDecode(aul.getRecipientName(), aul.getRecipient()));
				} catch (InvalidKeyException | UnsupportedEncodingException | NoSuchAlgorithmException
						| NoSuchPaddingException | InvalidAlgorithmParameterException | IllegalBlockSizeException
						| BadPaddingException e) {e.printStackTrace();}
			}	
		model.addAttribute("SentList", sentList);
	}
	
	private void getProjectList(Model model) {		
		System.out.println("Main/getProjectList");
		List<ProListB> projectList = null;
		AuthB ab = (AuthB)model.getAttribute("authB");

		try {
			ab.setPmbCode(((AuthB)this.pu.getAttribute("accessInfo")).getPmbCode());
		} catch (Exception e) {e.printStackTrace();}		
			projectList = this.session.selectList("getProjectList", ab);
			
			for(ProListB pl : projectList) {
				try {
					System.out.println(pl);
					pl.setManagerName(this.enc.aesDecode(pl.getManagerName(), pl.getManager()));
				} catch (InvalidKeyException | UnsupportedEncodingException | NoSuchAlgorithmException
						| NoSuchPaddingException | InvalidAlgorithmParameterException | IllegalBlockSizeException
						| BadPaddingException e) {e.printStackTrace();}
			}	
		model.addAttribute("ProjectList", projectList);
	}
	
	private void getFullProjectList(Model model) {		
		System.out.println("Main/getFullProjectList");
		List<ProListB> projectList = null;
		AuthB ab = (AuthB)model.getAttribute("authB");

		try {
			ab.setPmbCode(((AuthB)this.pu.getAttribute("accessInfo")).getPmbCode());
		} catch (Exception e) {e.printStackTrace();}		
			projectList = this.session.selectList("getFullProjectList", ab);
			
			for(ProListB pl : projectList) {
				try {
					pl.setManagerName(this.enc.aesDecode(pl.getManagerName(), pl.getManager()));
				} catch (InvalidKeyException | UnsupportedEncodingException | NoSuchAlgorithmException
						| NoSuchPaddingException | InvalidAlgorithmParameterException | IllegalBlockSizeException
						| BadPaddingException e) {e.printStackTrace();}
			}	
		model.addAttribute("ProjectList", projectList);
	}
	
	private void refusal(ModelAndView mav) {
		System.out.println("Main/refusal");
		String page = "main";
		AuthB ab = (AuthB)mav.getModel().get("authB");
		ProMemB pm = new ProMemB();
		AulB aul = new AulB();
		String key = "BDGames";
		
		
		try {
			AuthB session = ((AuthB)this.pu.getAttribute("accessInfo"));
			String authCode = (this.enc.aesDecode((ab.getAuthCode()), key));
			String split[] = authCode.split(":");
			String projectCode = split[0];
			//String email = split[1];
			String inviteDate = split[2];
			String sender = split[3];
			
			pm.setIsAccept("RF");
			pm.setProjectCode(projectCode);
			pm.setPmbCode(session.getPmbCode());	
			this.session.update("updPrm", pm);
			System.out.println("updPrm check");	

			aul.setAuthResult("AU");
			aul.setInviteDate(inviteDate);
			aul.setSender(sender);
			aul.setRecipient(session.getPmbCode());		
			this.session.update("updAul", aul);
			System.out.println("updAul check");	
			
		} catch (Exception e) {e.printStackTrace();}
		

		this.session.update("updAul", aul);
		
		mav.setViewName(page);
	}
	
	private void getProjectMembers(Model model) {		
		System.out.println("Main/getProjectMembers");
		String count = "0";
		ProjectB pb = (ProjectB)model.getAttribute("projectB");
		
			count = this.session.selectOne("getProjectMembers", pb);
			
		model.addAttribute("ProjectMembers", count);
	}
	
	private boolean convertToBool(int result) {
		return result >= 1 ? true : false;
	}
}
