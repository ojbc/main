/*
 * Unless explicitly acquired and licensed from Licensor under another license, the contents of
 * this file are subject to the Reciprocal Public License ("RPL") Version 1.5, or subsequent
 * versions as allowed by the RPL, and You may not copy or use this file in either source code
 * or executable form, except in compliance with the terms and conditions of the RPL
 *
 * All software distributed under the RPL is provided strictly on an "AS IS" basis, WITHOUT
 * WARRANTY OF ANY KIND, EITHER EXPRESS OR IMPLIED, AND LICENSOR HEREBY DISCLAIMS ALL SUCH
 * WARRANTIES, INCLUDING WITHOUT LIMITATION, ANY WARRANTIES OF MERCHANTABILITY, FITNESS FOR A
 * PARTICULAR PURPOSE, QUIET ENJOYMENT, OR NON-INFRINGEMENT. See the RPL for specific language
 * governing rights and limitations under the RPL.
 *
 * http://opensource.org/licenses/RPL-1.5
 *
 * Copyright 2012-2017 Open Justice Broker Consortium
 */
package org.ojbc.web.portal.controllers;

import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.ojbc.util.camel.security.saml.SAMLTokenUtils;
import org.ojbc.util.model.saml.SamlAttribute;
import org.ojbc.web.model.otp.OTPFormCommand;
import org.ojbc.web.portal.services.OTPServiceMemoryImpl;
import org.ojbc.web.portal.services.SamlService;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.w3c.dom.Element;

@Controller
@RequestMapping("/otp")
public class OTPController {

	@Resource
	SamlService samlService;
	
	@Resource
	OTPServiceMemoryImpl otpService;

	private final Log log = LogFactory.getLog(this.getClass());
	
	@GetMapping(value = "/inputForm")
	public String searchForm(HttpServletRequest request,
	        Map<String, Object> model) throws Exception {

		//We clear the authentication to force the portal details source method
		//to be invoked.  Otherwise it thinks pre-auth is complete.
		SecurityContextHolder.getContext().setAuthentication(null);
		
		OTPFormCommand otpFormCommand = new OTPFormCommand();
		model.put("otpFormCommand", otpFormCommand);
		
		//Send OTP as soon as the input form is shown
		String userEmail = getUserEmail(request);
		
		try {
			otpService.generateOTP(userEmail);
		} catch (Exception e) {
			log.error("Unable to generate OTP, " + e);
			
			model.put("otpInfoMessageError", "There was an error and your One-Time Password could not be generated. Please report this issue to the help desk.");
			
			return "otp/inputForm";
		}
		
		model.put("otpInfoMessage", "Your One-Time Password (OTP) was sent to " + userEmail + ".  Please enter the OTP above.  If you did not receive the OTP, please click the 'Request New OTP' link below.");		
		
		return "otp/inputForm";
	}

	private String getUserEmail(HttpServletRequest request) {
		Element samlAssertion = samlService.getSamlAssertion(request);
		
		String userEmail = SAMLTokenUtils.getAttributeValue(samlAssertion, SamlAttribute.EmailAddressText);
		return userEmail;
	}
	
	@PostMapping(value = "/requestOtp")
	public String requestOtp(HttpServletRequest request, @ModelAttribute OTPFormCommand otpFormCommand, Map<String, Object> model) throws Exception {

		log.info("Entering function to request OTP.");
		
		String userEmail = getUserEmail(request);		
		try {
			otpService.generateOTP(userEmail);
		} catch (Exception e) {
			log.error("Unable to generate OTP, " + e);
			
			model.put("otpInfoMessageError", "There was an error and your One-Time Password could not be generated. Please report this issue to the help desk.");
			
			return "otp/inputForm";
		}
		
		model.put("otpInfoMessage", "Your One-Time Password sent to: " + userEmail);
		
		log.info("About to return view");
		
		return "otp/inputForm";

	}

	@PostMapping(value = "submitOtp")
	public String submitOtp(HttpServletRequest request, @ModelAttribute OTPFormCommand otpFormCommand, 
			Map<String, Object> model) throws Exception {
	
		log.info("Entering function to confirm OTP.");
		String oneTimePassword = otpFormCommand.getOtpRequest().getOneTimePassword();
		
		String userEmail = getUserEmail(request);
		
		if (otpService.confirmOTP(userEmail, oneTimePassword))
		{
			log.info("OTP confirmed for: " + userEmail);
			return "redirect:/";
		}	
		else
		{
			log.warn("OTP Denied for: " + userEmail);
			model.put("otpInfoMessageError", "Your One-Time Password was invalid.  Please try again or request new One-Time Password.");
			return "otp/inputForm";	
		}

	}

}
