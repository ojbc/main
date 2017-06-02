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

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.junit.Before;
import org.junit.Test;
import org.ojbc.web.impl.MockMailSender;
import org.ojbc.web.model.suggestionForm.SuggestionFormRequest;
import org.ojbc.web.portal.controllers.dto.SuggestionFormCommand;
import org.ojbc.web.portal.controllers.helpers.UserSession;
import org.ojbc.web.portal.services.SamlService;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.validation.BindingResult;

public class SuggestionFormControllerTest {
	private SuggestionFormController unit;
	private SuggestionFormCommand suggestionFormCommand;
	private Map<String, Object> model;
	
	private BindingResult errors;

	private UserSession userSession;
	private HttpServletRequest servletRequest;
	private SamlService samlService;
	private MockMailSender mockMailSender;

	private final Log log = LogFactory.getLog(this.getClass());

	@Before
	public void setup() {
		suggestionFormCommand = new SuggestionFormCommand();
		model = new HashMap<String, Object>();
		errors = mock(BindingResult.class);
		userSession = mock(UserSession.class);
		samlService = mock(SamlService.class);
		mockMailSender= mock(MockMailSender.class);
		
		unit = new SuggestionFormController();

		unit.userSession = userSession;
		unit.samlService = samlService;
		unit.ojbcMailSender = mockMailSender;
		unit.suggestionFormResultsPage="portal/suggestionConfirmation";
		unit.suggestionFormEmailRecipient="test@localhost";
		unit.suggestionFormLandingPage="portal/suggestionForm";
		
		SuggestionFormRequest suggestionFormRequest = new SuggestionFormRequest();
		
		suggestionFormRequest.setSuggestionProblem("suggestionProblem");
		suggestionFormRequest.setUrgency("urgency");
		suggestionFormRequest.setUserAgency("agency");
		suggestionFormRequest.setUserEmail("email");
		suggestionFormRequest.setUserFeedback("feedback");
		suggestionFormRequest.setUserName("username");
		suggestionFormRequest.setUserPhone("phone");
		
		suggestionFormCommand.setSuggestionFormRequest(suggestionFormRequest);
		
	}

	@Test
	public void searchFormReturnsCorrectViewNameAndInitialData() {
		String viewName = unit.searchForm(false, model);

		assertThat(viewName, is("portal/suggestionForm"));
		
	}

	@Test
	public void urgencyListFromMap() {
		Map<String, String> hashMap = new HashMap<String, String>();
		unit.urgencyList = hashMap;
		assertSame(hashMap, unit.getUrgencyList());
	}

	@Test
	public void suggestionProblemListFromMap() {
		Map<String, String> hashMap = new HashMap<String, String>();
		unit.suggestionProblemList = hashMap;
		assertSame(hashMap, unit.getSuggestionProblemList());
	}
	
	@Test
	public void suggestionFormSubmissionSuccess() throws Exception {
		
		String viewName = unit.submitEmail(servletRequest, suggestionFormCommand, errors, model);
		
		String emailBody = unit.createEmailBody(suggestionFormCommand.getSuggestionFormRequest());
		
        SimpleMailMessage email = new SimpleMailMessage();
        email.setTo("test@localhost");
        email.setSubject("OJBC Portal Email Suggestion");
        email.setText(emailBody);
		
		verify(mockMailSender).send(email);
		
		assertThat(viewName, is("portal/suggestionConfirmation"));
	}	

	
	@Test
	public void testCreateEmailBody() throws Exception
	{
		SuggestionFormRequest suggestionFormRequest = new SuggestionFormRequest();
		
		suggestionFormRequest.setSuggestionProblem("suggestion or problem");
		suggestionFormRequest.setUrgency("normal");
		suggestionFormRequest.setUserAgency("agency");
		suggestionFormRequest.setUserEmail("user@email.com");
		suggestionFormRequest.setUserFeedback("User Feedback");
		suggestionFormRequest.setUserName("username");
		suggestionFormRequest.setUserPhone("phone");
		
		String generatedEmailResult = unit.createEmailBody(suggestionFormRequest);
		
		log.info("Generated Email Address: " + generatedEmailResult);
		
		StringBuffer expectedEmailResult = new StringBuffer();
		
		expectedEmailResult.append("Suggestion/Problem: suggestion or problem\n\n");
		expectedEmailResult.append("Urgency: normal\n\n");
		expectedEmailResult.append("User agency: agency\n\n");
		expectedEmailResult.append("Email: user@email.com\n\n");
		expectedEmailResult.append("User Name: username\n\n");
		expectedEmailResult.append("User Phone: phone\n\n");
		expectedEmailResult.append("User Feedback: User Feedback\n\n");
		
		assertEquals(expectedEmailResult.toString(), generatedEmailResult);
		
	}
}
