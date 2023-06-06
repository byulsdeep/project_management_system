package com.pms.services;

import java.io.UnsupportedEncodingException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
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
public class Project implements ServicesRule{

	@Autowired
	private SqlSessionTemplate session;
	@Autowired
	private Encryption enc;
	@Autowired 
	private ProjectUtils pu;
	@Autowired
	private Authentication auth;
	@Autowired
	private JavaMailSenderImpl mail;

	public Project() {}

	public void backController(int serviceCode, ModelAndView mav) {
		if(auth.isSession()) {
			System.out.println("Project/backController");
			switch(serviceCode) {
			case 0:
				this.mainCtl(mav);
				break;
			case 2:
				this.regProjectMembersCtl(mav);
				break;
			case 3:
				this.ang(mav);
				break;
			}
		} else {
			mav.setViewName("home");
		}
	}
	public void backController(int serviceCode, Model model) {
		if(auth.isSession()) {
			System.out.println("Project/backController");
			switch(serviceCode) {
			case 1:
				this.regProjectCtl(model);
				break;
			case 4: 
				this.aang(model);
				break;
			case 5: 
				this.aaang(model);
				break;
			case 6:
				this.aaaang(model);
				break;
			}
		}
	}
	private void mainCtl(ModelAndView mav) {
		System.out.println("Project/mainCtl");
		String page = "project";
		mav.setViewName(page);
	}

	@Transactional
	private void regProjectCtl(Model model) {
		System.out.println("Project/regProjectCtl");
		List<AuthB> memberList = null;
		AuthB au = null;
		ProjectB pb = ((ProjectB)model.getAttribute("projectB"));

		if(this.convertToBool(this.session.insert("insProject", pb))) {
			System.out.println("insert success");
			memberList = this.session.selectList("getMemberList");

			for(AuthB ab : memberList) {
				try {
					ab.setPmbName(this.enc.aesDecode(ab.getPmbName(), ab.getPmbCode()));
					ab.setEmail(this.enc.aesDecode(ab.getEmail(), ab.getPmbCode()));
				} catch (InvalidKeyException | UnsupportedEncodingException | NoSuchAlgorithmException
						| NoSuchPaddingException | InvalidAlgorithmParameterException | IllegalBlockSizeException
						| BadPaddingException e) {e.printStackTrace();}
			}
		} else {
			System.out.println("insert fail");
			memberList = new ArrayList<AuthB>();
			au = new AuthB();
			au.setMessage("생성실패");
			memberList.add(au);
		}		
		model.addAttribute("MemberList", memberList);

	}

	@Transactional
	private void regProjectMembersCtl(ModelAndView mav) {
		System.out.println("regProjectMembersCtl");
		ProjectB pb = ((ProjectB)mav.getModel().get("projectB"));
		AuthB session = null;
		try {
			session = (AuthB)this.pu.getAttribute("accessInfo");
		} catch (Exception e1) {e1.printStackTrace();}

		String page = "project";
		mav.setViewName(page);

		int result =  this.session.update("insProjectMembers", pb);
		System.out.println(result);

		ProMemB pm = new ProMemB();
		pm.setProjectCode(pb.getProjectCode());
		pm.setPmbCode(session.getPmbCode());
		this.session.insert("insSelf", pm);

		String subject = "[" + pb.getProjectName() + "] 프로젝트 초대장";

		String from = "byulzdeep@outlook.com";
		String[] to = new String [(pb.getProjectMembers().size())];
		String[] authCode = new String [(pb.getProjectMembers().size())];
		String[] content = new String [(pb.getProjectMembers().size())];
		String[] inviteDate = new String [(pb.getProjectMembers().size())];
		String projectCode = pb.getProjectCode();
		String key = "BDGames";

		MimeMessage mime = mail.createMimeMessage();
		MimeMessageHelper helper = new MimeMessageHelper(mime,"UTF-8");
		try {
			helper.setFrom(from);
			helper.setSubject(subject);
		} catch (MessagingException e1) {e1.printStackTrace();}

		AulB aul;

		for(int i=0; i < to.length; i++) {
			to[i] = pb.getProjectMembers().get(i).getEmail();

			aul = new AulB();
			aul.setSender(session.getPmbCode());
			aul.setRecipient(pb.getProjectMembers().get(i).getPmbCode());
			aul.setProjectCode(projectCode);
			this.session.insert("insAul", aul);
			inviteDate[i] = this.session.selectOne("getInviteDate", aul);

			try {
				authCode[i] = this.enc.aesEncode(projectCode + ":" + to[i] + ":" + inviteDate[i] + ":" + aul.getSender(), key);
				content[i] = "<!DOCTYPE html>\r\n"
						+ "<html lang=\"en\">\r\n"
						+ "<head>\r\n"
						+ "    <meta charset=\"UTF-8\">\r\n"
						+ "    <meta http-equiv=\"X-UA-Compatible\" content=\"IE=edge\">\r\n"
						+ "    <meta name=\"viewport\" content=\"width=device-width, initial-scale=1.0\">\r\n"
						+ "    <title>Document</title>\r\n"
						+ "</head>\r\n"
						+ "<style>\r\n"
						+ "    .center { \r\n"
						+ "			position:absolute; \r\n"
						+ "			top:50%; \r\n"
						+ "			left:50%; \r\n"
						+ "			transform:translate(-50%, -50%);		 \r\n"
						+ "		   	text-align:center; \r\n"
						+ "		   	}\r\n"
						+ "</style>\r\n"
						+ "<body>\r\n"
						+ "    <div class=\"center\">\r\n"
						+ "        <img src=\"https://blogger.googleusercontent.com/img/a/AVvXsEhlvbsCOUntQY8Dh6NyZ3Wr0-RPlFMOpHLI5SdQOEWUb51fneTu3i4ER8NX0CfDVSQrltk_tnr1o52NTl2sL6iEq3D9c49Y7Py78NrcA2SUt7eMP0kI690xiy9wY_Cm0hLbcGjnZ7faAp2nHcVbcip4pVQ9-HoYnTzTEENAcXo1qHpQlu2cpqtDV-KKtQ\">\r\n"
						+ "        <h2>\r\n"
						+ pb.getProjectMembers().get(i).getPmbName()
						+ "님을 "
						+ "[" + pb.getProjectName()
						+ "] 프로젝트에 초대합니다.   \r\n"
						+ "        </h2>\r\n"
						+ "        <h3>\r\n"
						+ pb.getProjectComment()
						+ "        </h3>\r\n"
						+ "        <h2>\r\n"
						+ "            프로젝트에 참가하시려면 아래 주소에서 로그인 후 인증해주세요"
						+ "        </h2>\r\n"
						+ "        <h2>\r\n"
						+ "            192.168.0.66"
						+ "        </h2>\r\n"
						+ "        <h2>\r\n"
						+ "            인증코드: [ "
						+ authCode[i] + " ]"
						+ "        </h2>\r\n"
						+ "    </div>\r\n"
						+ "</body>\r\n"
						+ "</html>";
				helper.setTo(to[i]);
				helper.setText(content[i], true);
				mail.send(mime);

			} catch (InvalidKeyException | UnsupportedEncodingException | NoSuchAlgorithmException
					| NoSuchPaddingException | InvalidAlgorithmParameterException | IllegalBlockSizeException
					| BadPaddingException | MessagingException e) {e.printStackTrace();}
		}
	}

	private void ang(ModelAndView mav) {
		System.out.println("Project/backController/ang");	
		//mav.addObject("hoonList", hoonList);
		mav.setViewName("management");
	}

	private void aang(Model model) {
		System.out.println("Project/backController/aang");
		ProjectB pb = (ProjectB)model.getAttribute("projectB");

		List<ProjectB> hoonList = null;
		AuthB session = null;

		try {
			session = (AuthB)this.pu.getAttribute("accessInfo");
		} catch (Exception e) {e.printStackTrace();}

		hoonList = this.session.selectList("getProjectHoon", session);
		List<ProMemB> ajaxHoonList = new ArrayList<ProMemB>();

		for(int i=0; i < hoonList.size(); i++) {
			if(hoonList.get(i).getProjectCode().equals(pb.getProjectCode())) {
				for(int j=0; j < hoonList.get(i).getProjectMembers().size(); j++) {
					try {
						hoonList.get(i).getProjectMembers().get(j).setPmbName(enc.aesDecode(hoonList.get(i).getProjectMembers().get(j).getPmbName(), hoonList.get(i).getProjectMembers().get(j).getPmbCode()));
						hoonList.get(i).getProjectMembers().get(j).setEmail(enc.aesDecode(hoonList.get(i).getProjectMembers().get(j).getEmail(), hoonList.get(i).getProjectMembers().get(j).getPmbCode()));
					} catch (InvalidKeyException | UnsupportedEncodingException | NoSuchAlgorithmException
							| NoSuchPaddingException | InvalidAlgorithmParameterException | IllegalBlockSizeException
							| BadPaddingException e) {e.printStackTrace();}	

					ProMemB pm = new ProMemB();
					pm.setPmbCode(hoonList.get(i).getProjectMembers().get(j).getPmbCode());
					pm.setPosition(hoonList.get(i).getProjectMembers().get(j).getPosition());
					pm.setIsAccept(hoonList.get(i).getProjectMembers().get(j).getIsAccept());
					pm.setPmbName(hoonList.get(i).getProjectMembers().get(j).getPmbName());
					pm.setEmail(hoonList.get(i).getProjectMembers().get(j).getEmail());
					pm.setMlvName(hoonList.get(i).getProjectMembers().get(j).getMlvName());
					pm.setClaName(hoonList.get(i).getProjectMembers().get(j).getClaName());

					ajaxHoonList.add(pm);
				}			
			}
		}

		model.addAttribute("hoonList", ajaxHoonList);
	}

	@Transactional
	private void aaang(Model model) {
		System.out.println("Project/aaang");
		List<AuthB> memberList = null;
		AuthB au = null;

		memberList = this.session.selectList("getMemberList");

		for(AuthB ab : memberList) {
			try {
				ab.setPmbName(this.enc.aesDecode(ab.getPmbName(), ab.getPmbCode()));
				ab.setEmail(this.enc.aesDecode(ab.getEmail(), ab.getPmbCode()));
			} catch (InvalidKeyException | UnsupportedEncodingException | NoSuchAlgorithmException
					| NoSuchPaddingException | InvalidAlgorithmParameterException | IllegalBlockSizeException
					| BadPaddingException e) {e.printStackTrace();}
		}

		model.addAttribute("EmailList", memberList);
	}

	@Transactional
	private void aaaang(Model model) {
		System.out.println("aaaang");
		ProjectB pb = ((ProjectB)model.getAttribute("projectB"));
		AuthB session = null;
		try {
			session = (AuthB)this.pu.getAttribute("accessInfo");
		} catch (Exception e1) {e1.printStackTrace();}

		for(int i = 0; i < pb.getProjectMembers().size(); i++) {
			pb.getProjectMembers().get(i).setProjectCode(pb.getProjectCode());
			try {
				this.session.insert("insOneByOne", pb.getProjectMembers().get(i));
			} catch(Exception e) {
				d("caught one");
			}	
		}

		String subject = "[" + pb.getProjectName() + "] 프로젝트 초대장";

		String from = "byulzdeep@outlook.com";
		String[] to = new String [(pb.getProjectMembers().size())];
		String[] authCode = new String [(pb.getProjectMembers().size())];
		String[] content = new String [(pb.getProjectMembers().size())];
		String[] inviteDate = new String [(pb.getProjectMembers().size())];
		String projectCode = pb.getProjectCode();
		String key = "BDGames";

		MimeMessage mime = mail.createMimeMessage();
		MimeMessageHelper helper = new MimeMessageHelper(mime,"UTF-8");
		try {
			helper.setFrom(from);
			helper.setSubject(subject);
		} catch (MessagingException e1) {e1.printStackTrace();}

		AulB aul;

		for(int i=0; i < to.length; i++) {
			to[i] = pb.getProjectMembers().get(i).getEmail();

			aul = new AulB();
			aul.setSender(session.getPmbCode());
			aul.setRecipient(pb.getProjectMembers().get(i).getPmbCode());
			aul.setProjectCode(projectCode);
			this.session.insert("insAul", aul);
			inviteDate[i] = this.session.selectOne("getInviteDate", aul);

			try {
				authCode[i] = this.enc.aesEncode(projectCode + ":" + to[i] + ":" + inviteDate[i] + ":" + aul.getSender(), key);
				content[i] = "<!DOCTYPE html>\r\n"
						+ "<html lang=\"en\">\r\n"
						+ "<head>\r\n"
						+ "    <meta charset=\"UTF-8\">\r\n"
						+ "    <meta http-equiv=\"X-UA-Compatible\" content=\"IE=edge\">\r\n"
						+ "    <meta name=\"viewport\" content=\"width=device-width, initial-scale=1.0\">\r\n"
						+ "    <title>Document</title>\r\n"
						+ "</head>\r\n"
						+ "<style>\r\n"
						+ "    .center { \r\n"
						+ "			position:absolute; \r\n"
						+ "			top:50%; \r\n"
						+ "			left:50%; \r\n"
						+ "			transform:translate(-50%, -50%);		 \r\n"
						+ "		   	text-align:center; \r\n"
						+ "		   	}\r\n"
						+ "</style>\r\n"
						+ "<body>\r\n"
						+ "    <div class=\"center\">\r\n"
						+ "        <img src=\"https://blogger.googleusercontent.com/img/a/AVvXsEhlvbsCOUntQY8Dh6NyZ3Wr0-RPlFMOpHLI5SdQOEWUb51fneTu3i4ER8NX0CfDVSQrltk_tnr1o52NTl2sL6iEq3D9c49Y7Py78NrcA2SUt7eMP0kI690xiy9wY_Cm0hLbcGjnZ7faAp2nHcVbcip4pVQ9-HoYnTzTEENAcXo1qHpQlu2cpqtDV-KKtQ\">\r\n"
						+ "        <h2>\r\n"
						+ pb.getProjectMembers().get(i).getPmbName()
						+ "님을 "
						+ "[" + pb.getProjectName()
						+ "] 프로젝트에 초대합니다.   \r\n"
						+ "        </h2>\r\n"
						+ "        <h3>\r\n"
						+ pb.getProjectComment()
						+ "        </h3>\r\n"
						+ "        <h2>\r\n"
						+ "            프로젝트에 참가하시려면 아래 주소에서 로그인 후 인증해주세요"
						+ "        </h2>\r\n"
						+ "        <h2>\r\n"
						+ "            192.168.0.66"
						+ "        </h2>\r\n"
						+ "        <h2>\r\n"
						+ "            인증코드: [ "
						+ authCode[i] + " ]"
						+ "        </h2>\r\n"
						+ "    </div>\r\n"
						+ "</body>\r\n"
						+ "</html>";
				helper.setTo(to[i]);
				helper.setText(content[i], true);
				mail.send(mime);

			} catch (InvalidKeyException | UnsupportedEncodingException | NoSuchAlgorithmException
					| NoSuchPaddingException | InvalidAlgorithmParameterException | IllegalBlockSizeException
					| BadPaddingException | MessagingException e) {e.printStackTrace();}
		}
	}
	private void insProject() {}

	private List<AuthB> getMemberList() {return null;}

	private boolean convertToBool(int result) {
		return result >= 1 ? true : false;
	}
	private void d(String a) {
		System.out.println(a);
	}
}
