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
 * Copyright 2012-2015 Open Justice Broker Consortium
 */
package org.ojbc.web.portal.validators;

import static org.junit.Assert.assertEquals;

import java.util.Calendar;
import java.util.Date;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.junit.Test;
import org.ojbc.util.xml.subscription.Subscription;

public class ChCycleSubscriptionValidatorTest {
	
	private ChCycleSubscriptionValidator validator = new ChCycleSubscriptionValidator();
	
	@Test
	public void testValidator(){
						
		Subscription subscription = new Subscription();
									
		Map<String, String> fieldToErrorMap = validator.getValidationErrorsList(subscription);	
				
		String fNameError = fieldToErrorMap.get("firstName");
		assertEquals("First name must be specified", fNameError);
		
		String lNameError = fieldToErrorMap.get("lastName");
		assertEquals("Last name must be specified", lNameError);
		
		String startDateError = fieldToErrorMap.get("subscriptionStartDate");
		assertEquals("Start date must be specified", startDateError);	
									
		String emailListError = fieldToErrorMap.get("emailList");
		assertEquals("Email Address must be specified", emailListError);				
	}
		
	
	@Test
	public void testValidDates(){
				
		Subscription subscription = new Subscription();
		
		Calendar startDateCal = Calendar.getInstance();
		startDateCal.set(2014, 10, 21);
		Date startDate = startDateCal.getTime();
				
		Calendar endDateCal = Calendar.getInstance();
		endDateCal.set(2014, 10, 22);
		Date endDate = endDateCal.getTime();
				
		subscription.setSubscriptionStartDate(startDate);		
		subscription.setSubscriptionEndDate(endDate);			

		Map<String, String> fieldToErrorMap = validator.getValidationErrorsList(subscription);
		
		String endDateError = fieldToErrorMap.get("subscriptionEndDate");
		boolean hasEndDateError = StringUtils.isNotBlank(endDateError);
		assertEquals(false, hasEndDateError);						
	}
	
	
	@Test
	public void testEndDateBeforeStartDate(){
				
		Subscription subscription = new Subscription();
		
		Calendar startDateCal = Calendar.getInstance();
		startDateCal.set(2014, 10, 21);
		Date startDate = startDateCal.getTime();
				
		Calendar endDateCal = Calendar.getInstance();
		endDateCal.set(2014, 10, 20);
		Date endDate = endDateCal.getTime();
				
		subscription.setSubscriptionStartDate(startDate);		
		subscription.setSubscriptionEndDate(endDate);			

		Map<String, String> fieldToErrorMap = validator.getValidationErrorsList(subscription);
		
		String endDateError = fieldToErrorMap.get("subscriptionEndDate");
		assertEquals("End date may not occur before start date", endDateError);		
	}	
	
	
	@Test
	public void testValidatorSuccesses(){
						
		Subscription subscription = new Subscription();
		
		subscription.setTopic("{http://ojbc.org/wsn/topics}:person/criminalHistoryCycleTrackingIdentifierAssignment");		
		subscription.setStateId("123");		
		subscription.setFirstName("Homer");
		subscription.setLastName("Simpson");			
		subscription.setSubscriptionStartDate(new Date());
		subscription.getEmailList().add("hsimpson@gmail.com");
		
		Map<String, String> fieldToErrorMap = validator.getValidationErrorsList(subscription);
		
		String subTypeError = fieldToErrorMap.get("subscriptionType");		
		boolean hasSubTypeError = StringUtils.isNotBlank(subTypeError);				
		assertEquals(false, hasSubTypeError);
		
		
		String fNameError = fieldToErrorMap.get("firstName");
		boolean hasFNameError = StringUtils.isNotBlank(fNameError);
		assertEquals(false, hasFNameError);
		

		String lNameError = fieldToErrorMap.get("lastName");
		boolean hasLNameError = StringUtils.isNotBlank(lNameError);
		assertEquals(false, hasLNameError);		
		
		
		String startDateError = fieldToErrorMap.get("subscriptionStartDate");
		boolean hasStartDateError = StringUtils.isNotBlank(startDateError);
		assertEquals(false, hasStartDateError);
		
		String emailListError = fieldToErrorMap.get("emailList");
		boolean hasEmailError = StringUtils.isNotBlank(emailListError);
		assertEquals(false, hasEmailError);
	}

}

