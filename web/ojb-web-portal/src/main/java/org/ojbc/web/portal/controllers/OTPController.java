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
import org.ojbc.util.xml.XmlUtils;
import org.ojbc.web.portal.controllers.dto.OTPFormCommand;
import org.ojbc.web.portal.services.OTPService;
import org.ojbc.web.portal.services.SamlService;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.w3c.dom.Element;

@Controller
@RequestMapping("/otp/*")
public class OTPController {

	@Resource
	SamlService samlService;
	
	@Resource (name="${otpServiceBean:OTPServiceMemoryImpl}")
	OTPService otpService;

	private final Log log = LogFactory.getLog(this.getClass());
	
	@RequestMapping(value = "inputForm", method = RequestMethod.GET)
	public String searchForm(HttpServletRequest request,
	        Map<String, Object> model) throws Exception {

		//We clear the authentication to force the portal details source method
		//to be invoked.  Otherwise it thinks pre-auth is complete.
		SecurityContextHolder.getContext().setAuthentication(null);
		
		OTPFormCommand otpFormCommand = new OTPFormCommand();
		model.put("otpFormCommand", otpFormCommand);
		
		//Send OTP as soon as the input form is shown
		Element samlAssertion = samlService.getSamlAssertion(request);
		
		String userEmail = XmlUtils.xPathStringSearch(samlAssertion, "/saml2:Assertion/saml2:AttributeStatement[1]/saml2:Attribute[@Name='gfipm:2.0:user:EmailAddressText']/saml2:AttributeValue/text()");
		
		otpService.generateOTP(userEmail);
		
		model.put("otpInfoMessage", "Your One-Time Password (OTP) was sent to " + userEmail + ".  Please enter the OTP above.  If you did not receive the OTP, please click the 'Request New OTP' below.");		
		
		return "otp/inputForm";
	}
	
	@RequestMapping(value = "requestOtp", method = RequestMethod.POST)
	public String requestOtp(HttpServletRequest request, @ModelAttribute("otpFormCommand") OTPFormCommand otpFormCommand, Map<String, Object> model) throws Exception {

		log.info("Entering function to request OTP.");
		
		Element samlAssertion = samlService.getSamlAssertion(request);
		
		String userEmail = XmlUtils.xPathStringSearch(samlAssertion, "/saml2:Assertion/saml2:AttributeStatement[1]/saml2:Attribute[@Name='gfipm:2.0:user:EmailAddressText']/saml2:AttributeValue/text()");
		
		otpService.generateOTP(userEmail);
		
		model.put("otpInfoMessage", "Your One-Time Password sent to: " + userEmail);
		
		log.info("About to return view");
		
		return "otp/inputForm";

	}

	@RequestMapping(value = "submitOtp", method = RequestMethod.POST)
	public String submitOtp(HttpServletRequest request, @ModelAttribute("otpFormCommand") OTPFormCommand otpFormCommand, Map<String, Object> model) throws Exception {
	
		log.info("Entering function to confirm OTP.");
		
		Element samlAssertion = samlService.getSamlAssertion(request);
		
		String oneTimePassword = otpFormCommand.getOtpRequest().getOneTimePassword();
		
		String userEmail = XmlUtils.xPathStringSearch(samlAssertion, "/saml2:Assertion/saml2:AttributeStatement[1]/saml2:Attribute[@Name='gfipm:2.0:user:EmailAddressText']/saml2:AttributeValue/text()");
		
		if (otpService.confirmOTP(userEmail, oneTimePassword))
		{
			log.info("OTP confirmed for: " + userEmail);
			return "redirect:/portal/index";
		}	
		else
		{
			log.warn("OTP Denied for: " + userEmail);
			model.put("otpInfoMessageError", "Your One-Time Password was invalid.  Please try again or request new One-Time Password.");
			return "otp/inputForm";	
		}

	}

}
