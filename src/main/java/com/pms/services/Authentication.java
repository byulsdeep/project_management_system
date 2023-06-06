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
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.servlet.ModelAndView;

import com.pms.beans.AulB;
import com.pms.beans.AuthB;
import com.pms.beans.Points;
import com.pms.beans.ProMemB;
import com.pms.beans.ProjectB;
import com.pms.inter.MapperInter;
import com.pms.inter.ServicesRule;
import com.pms.utils.Encryption;
import com.pms.utils.ProjectUtils;

@Service
public class Authentication implements ServicesRule{

	@Autowired
	private SqlSessionTemplate session;
	@Autowired
	private Encryption enc;
	@Autowired 
	private ProjectUtils pu;

	public Authentication() {}

	public void backController(int serviceCode, ModelAndView mav) {
		System.out.println("Authentication/backController");
		switch(serviceCode) {
		case -1:
			this.entrance(mav);
			break;
		case 0:
			this.moveSignUp(mav);
			break;
		case 1:
			this.accessCtl(mav);
			break;
		case 2: 
			this.regMember(mav);
			break;
		case 3:
			this.accessOutCtl(mav);
			break;	
		case 4: 
			this.isMember(mav);
			break;
		default:
		}
	}
	
	public void backController(int serviceCode, Model model) {
	}
	
	private void entrance(ModelAndView mav) {
		String page = "home";
		System.out.println("entrance");
		AuthB session;

		try {
			if((session = (AuthB)this.pu.getAttribute("accessInfo")) != null) {
				page = "main";
			}
		} catch (Exception e) {e.printStackTrace();}

		mav.setViewName(page);
	}

	private void accessCtl(ModelAndView mav) {
		System.out.println("accessCtl");
		AuthB session;
		try {
			if((session = (AuthB)this.pu.getAttribute("accessInfo")) != null) {
				mav.setViewName("main");
			} else {
				this.accessCtl2(mav);
			}
		} catch (Exception e) {e.printStackTrace();}
	}

	@Transactional(isolation=Isolation.READ_COMMITTED, propagation=Propagation.REQUIRED)
	private void accessCtl2(ModelAndView mav) {
		System.out.println("accessCtl2");
		System.out.println("Public IP:" + ((AuthB)mav.getModel().get("authB")).getPublicIp());
		System.out.println("Private IP:" + ((AuthB)mav.getModel().get("authB")).getPrivateIp());

		String page = "home";
		String message = null;

		AuthB ab = (AuthB)mav.getModel().get("authB");

		if(this.enc.matches(ab.getPmbPassword(), this.session.selectOne("isMemberr", ab))) {
			String dbData = (String)this.session.selectOne("isAccess", ab);
			if(dbData != null) {
				ab.setAction(-1);
				this.session.insert("insAsl", ab);
				System.out.println("force LogOut");	
			}

			ab.setAction(1);	
			
			if(this.convertToBool(this.session.insert("insAsl",ab))){
				
				AuthB session = (AuthB)this.session.selectList("getAccessInfo", ab).get(0);
				System.out.println(session);
				try {
					session.setPmbName(this.enc.aesDecode(session.getPmbName(), session.getPmbCode()));
					session.setEmail(this.enc.aesDecode(session.getEmail(), session.getPmbCode()));
					this.pu.setAttribute("accessInfo", session);
					page = "main";
					message = "Success";

					System.out.println("loginSuccess");
				} catch (Exception e) {e.printStackTrace();}
			} else {
				message = "핑계";
			}
		}		

		mav.addObject("message", message);	
		mav.setViewName(page);
	}

	/*
	 * 세션 확인
	 *  T 접속 상태 확인
	 *  	T 강제로그아웃
	 * 		F 다음
	 *  F 다음
	 * 		
	 * 로그 작성
	 * 	T 포인트 적립/삭감
	 *  F 관리자 호출
	 * 세션 생성
	 * 로그인완료
	 * 
	 * 로그아웃
	 * 세션 확인
	 *  T 접속 상태 확인
	 * 		T 로그 작성
	 * 		세션 삭제
	 * */
	
	/*포인트 테이블
	- 시간 단위

	전날 주말/공휴일 확인
	전날 로그아웃 여부확인 

	적립/삭감 판단
	- 0900 ~ 16:50 T/F
	- 주말 T/F
	- 공휴일 T/F

	적립: 
	퇴실
	- 17:50 + 1시간
	- 18:50 + 2시간
	- 19:50 + 3시간
	- 20:50 + 4시간

	외출:

	삭감: 
	입실
	- 09:01 -1시간
	- 10:01 -2시간
	- 11:01 -3시간
	- 12:01 -4시간
	- 13:01 -5시간
	- 14:01 -6시간
	- 15:01 -7시간
	- 16:01 -8시간

	로그아웃 안했을 경우
	로그인 시 강제 로그아웃하면서 포인트 계산

	09:00 ~ 21:00 ~ 다음날 09:00 -> 12시간 + 12시간 -> */
	
	@Transactional
	private void accessOutCtl(ModelAndView mav) {
		String page = "redirect:/";
		AuthB session;

		try {
			session = ((AuthB)this.pu.getAttribute("accessInfo"));
			System.out.println("gotAccessInfo");
			if(session != null) {
				String dbData = (String)this.session.selectOne("isAccess", session);
				if(dbData != null) {
					session.setAction(-1);
					if(!this.convertToBool(this.session.insert("insAsl", session))) {
					}
					this.pu.removeAttribute("accessInfo");
				}
			}
		} catch (Exception e1) {e1.printStackTrace();}

		mav.setViewName(page);
	}

	@Transactional(readOnly = true)
	private void moveSignUp(ModelAndView mav) {
		String page = "signup";

		mav.addObject("pmbCode", this.session.selectOne("getMaxPmbCode"));

		mav.addObject("level", this.makeSelectHtml(this.session.selectList("getLevelList"), true, "Role"));
		mav.addObject("classs", this.makeSelectHtml(this.session.selectList("getClassList"), false, "Class"));

		mav.setViewName(page);
	}

	private String makeSelectHtml(List<AuthB> list, boolean type, String title) {
		StringBuffer sb = new StringBuffer();
		sb.append("<select name='" + (type? "mlvCode":"claCode")+ "'>");
		//sb.append("<option disabled selected>" + title + "</option>");
		for(AuthB ab: list) {
			sb.append("<option value='" + (type?ab.getMlvCode():ab.getClaCode()) 
					+ "'>" + (type?ab.getMlvName():ab.getClaName()) + "</option>");
		}
		sb.append("</select>");

		return sb.toString();
	}

	@Transactional(propagation=Propagation.REQUIRED)
	private void regMember(ModelAndView mav) {
		System.out.println("regMember");
		AuthB ab = ((AuthB)mav.getModel().get("authB"));

		System.out.println("Public IP:" + ab.getPublicIp());
		System.out.println("Private IP:" + ab.getPrivateIp());

		String page = "signup", message = "system error";

		ab.setPmbCode(this.session.selectOne("getMaxPmbCode"));
		System.out.println(ab.getEmail());
		try {
			ab.setPmbName(this.enc.aesEncode(ab.getPmbName(), ab.getPmbCode()));
			ab.setEmail(this.enc.aesEncode(ab.getEmail(), ab.getPmbCode()));
			ab.setPmbPassword(this.enc.encode(ab.getPmbPassword()));
		} catch (InvalidKeyException | UnsupportedEncodingException | NoSuchAlgorithmException | NoSuchPaddingException
				| InvalidAlgorithmParameterException | IllegalBlockSizeException | BadPaddingException e1) {e1.printStackTrace();
		}

		if(this.convertToBool(this.session.insert("regMember", ab))) {
			page = "home";
			try {
				message = "Reigistry success\\nYour member code is:\\n" + ab.getPmbCode()
						+ "\\nYour email is:\\n" + this.enc.aesDecode(ab.getEmail(), ab.getPmbCode());
			} catch (InvalidKeyException | UnsupportedEncodingException | NoSuchAlgorithmException
					| NoSuchPaddingException | InvalidAlgorithmParameterException | IllegalBlockSizeException
					| BadPaddingException e) {e.printStackTrace();}
		}

		mav.addObject("message", message);	
		mav.setViewName(page);
	}
	
	/* 인증코드 검사 -> 수락 버튼 띄워주기 */
	private void isMember(ModelAndView mav) {
		System.out.println("isMember");	
		AuthB ab = (AuthB)mav.getModel().get("authB");
		ProMemB pm;
		AulB aul;
		String page = "main";
		String message = "";
		String key = "BDGames";
		
		try {
			AuthB session = ((AuthB)this.pu.getAttribute("accessInfo"));		
			String authCode = (this.enc.aesDecode((ab.getAuthCode()), key));
			String split[] = authCode.split(":");
			String projectCode = split[0];
			String email = split[1];
			String inviteDate = split[2];
			String sender = split[3];
					
			if(session.getEmail().equals(email)) {
				System.out.println("authCode accepted");	
					message = "인증성공";
					mav.addObject("message", message);
					
					pm = new ProMemB();
					pm.setIsAccept("AC");
					pm.setProjectCode(projectCode);
					pm.setPmbCode(session.getPmbCode());				
					this.session.update("updPrm", pm);
					System.out.println("updPrm check");	
					
					aul = new AulB();
					aul.setAuthResult("AU");
					aul.setInviteDate(inviteDate);
					aul.setSender(sender);
					aul.setRecipient(session.getPmbCode());	
					aul.setProjectCode(projectCode);
					this.session.update("updAul", aul);
					System.out.println("updAul check");	
					
					mav.setViewName(page);
			} else {
				System.out.println("authCode denied");	
				message = "인증실패";
				
				aul = new AulB();
				aul.setAuthResult("NN");
				aul.setInviteDate(inviteDate);
				aul.setSender(sender);
				aul.setRecipient(session.getPmbCode());		
				this.session.update("updAul", aul);
				System.out.println("updAul check");	
				mav.setViewName("main");
			}
		} catch (Exception e) {e.printStackTrace();}		
	}

	private boolean convertToBool(int result) {
		return result >= 1 ? true : false;
	}
	
	boolean isSession() {
		boolean isSession = false;
		AuthB session;	
		try {
			isSession = ((session = (AuthB)this.pu.getAttribute("accessInfo")) != null) ? true : false;		
		} catch (Exception e) {e.printStackTrace();}
		return isSession;
	}
}

