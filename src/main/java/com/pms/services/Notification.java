package com.pms.services;

import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;
import org.springframework.web.servlet.ModelAndView;

import com.pms.beans.AuthB;
import com.pms.inter.ServicesRule;
import com.pms.utils.Encryption;
import com.pms.utils.ProjectUtils;

@Service
public class Notification implements ServicesRule{

	@Autowired
	private SqlSessionTemplate session;
	@Autowired 
	private ProjectUtils pu;
	@Autowired
	private Authentication auth;
	
	public Notification() {}
	
	public void backController(int serviceCode, ModelAndView mav) {
		if(auth.isSession()) {
		System.out.println("Notification/backController");
			switch(serviceCode) {
			case 0:
				this.mainCtl(mav);
				break;
			}
		} else {
			mav.setViewName("home");
		}
	}
	
	public void backController(int serviceCode, Model model) {}
	
	private void mainCtl(ModelAndView mav) {
		System.out.println("Notification/mainCtl");
		String page = "notification";
		mav.setViewName(page);;
	}
}
