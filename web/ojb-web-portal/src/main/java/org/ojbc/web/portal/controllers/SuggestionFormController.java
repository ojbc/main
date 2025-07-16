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
import org.ojbc.web.model.suggestionForm.SuggestionFormRequest;
import org.ojbc.web.portal.controllers.dto.SuggestionFormCommand;
import org.ojbc.web.portal.controllers.helpers.UserSession;
import org.ojbc.web.portal.services.SamlService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Profile;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/suggestion")
@Profile("suggestionForm")
public class SuggestionFormController {

    @Value("${suggestionFormEmailFrom:test@localhost}")
    String emailFrom;
    
    @Value("${suggestionFormResultsPage:portal/suggestionForm::suggestionConfirmationContent}")
    String suggestionFormResultsPage;

    @Value("${suggestionFormEmailRecipient:test@localhost}")
    String suggestionFormEmailRecipient;
    
	@Resource
	UserSession userSession;

	@Resource
	SamlService samlService;
	
	@Value("${suggestionFormLandingPage:portal/suggestionForm::suggestionFormContent}")
	String suggestionFormLandingPage;
	
	@Resource(name="suggestionProblemList")
	Map<String, String> suggestionProblemList;

	@Value("${suggestionFormSubject:OJBC Portal Email Suggestion}")
	String suggestionFormSubject;

	
	@Resource(name="urgencyList")
	Map<String, String> urgencyList;

    /*
     * If ojbcMailSenderBean is defined in cfg file, use the property value, 
     * Otherwise, use the default bean "mockMailSender"
     */

	@Resource (name="${ojbcMailSenderBean:mockMailSender}")
    JavaMailSender ojbcMailSender;	
	
	private final Log log = LogFactory.getLog(this.getClass());
	
	@RequestMapping(value = "suggestionForm", method = RequestMethod.GET)
	public String searchForm(@RequestParam(required = false) boolean resetForm,
	        Map<String, Object> model) {

		SuggestionFormCommand suggestionFormCommand = new SuggestionFormCommand();
		model.put("suggestionFormCommand", suggestionFormCommand);

		return suggestionFormLandingPage;
	}
	
	@RequestMapping(value = "submitEmail", method = RequestMethod.POST)
	public String submitEmail(HttpServletRequest request,  @ModelAttribute SuggestionFormCommand suggestionFormCommand,
	        BindingResult errors, Map<String, Object> model) {
		
		log.info("Entering submit email method with suggestion form command object: " + suggestionFormCommand.toString());
		
		SuggestionFormRequest suggestionFormRequest = suggestionFormCommand.getSuggestionFormRequest();
		
		String emailMessageBody = createEmailBody(suggestionFormRequest);
		
        // takes input from e-mail form
        String recipientAddress = suggestionFormEmailRecipient;
        String subject = suggestionFormSubject;
         
        // creates a simple e-mail object
        SimpleMailMessage email = new SimpleMailMessage();
        email.setTo(recipientAddress);
        email.setSubject(subject);
        email.setText(emailMessageBody);
        email.setFrom(emailFrom); 
        
        // sends the e-mail
        ojbcMailSender.send(email);
		
		return suggestionFormResultsPage;
	}
	
	protected String createEmailBody(SuggestionFormRequest suggestionFormRequest) {

		StringBuilder sb = new StringBuilder();
		
		sb.append("Suggestion/Problem: " + suggestionFormRequest.getSuggestionProblem() + "\n\n");
		sb.append("Urgency: " + suggestionFormRequest.getUrgency() + "\n\n");
		sb.append("User agency: " + suggestionFormRequest.getUserAgency() + "\n\n");
		sb.append("Email: " + suggestionFormRequest.getUserEmail() + "\n\n");
		sb.append("User Name: " + suggestionFormRequest.getUserName() + "\n\n");
		sb.append("User Phone: " + suggestionFormRequest.getUserPhone() + "\n\n");
		sb.append("User Feedback: " + suggestionFormRequest.getUserFeedback() + "\n\n");
		
		return sb.toString();
		
	}

	@ModelAttribute("suggestionProblemList")
	public Map<String, String> getSuggestionProblemList() {
		return suggestionProblemList;
	}

	@ModelAttribute("urgencyList")
	public Map<String, String> getUrgencyList() {
		return urgencyList;
	}
	
}
