package org.ojbc.web.consentmanagement;

import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class ConsentManagementViewController {
	
	@RequestMapping("/cm")
	public String cm(Model model, HttpServletRequest request) {
		// todo: make sure request has the saml stuff in the header
		return "index";
	}

}
