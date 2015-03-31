package org.ojbc.web.portal.validators;

import static org.junit.Assert.assertEquals;

import java.util.Calendar;
import java.util.Date;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.junit.Test;
import org.ojbc.web.model.subscription.edit.SubscriptionEditRequest;

public class ArrestSubscriptionEditValidatorTest {
	
	private ArrestSubscriptionEditValidator validator = new ArrestSubscriptionEditValidator();
	
	@Test
	public void testValidatorErrors(){
						
		SubscriptionEditRequest subEditRequest = new SubscriptionEditRequest();
									
		Map<String, String> fieldToErrorMap = validator.getValidationErrorsList(subEditRequest);	
				
		String subTypeError = fieldToErrorMap.get("subscriptionEditRequest.subscriptionType");		
		assertEquals("Subscription type must be specified", subTypeError);
				
		String stateIdError = fieldToErrorMap.get("subscriptionEditRequest.stateId");
		assertEquals("SID must be specified", stateIdError);
		
		String fullNameError =  fieldToErrorMap.get("subscriptionEditRequest.fullName");
		assertEquals("Name must be specified", fullNameError);
		
		String startDateError = fieldToErrorMap.get("subscriptionEditRequest.subscriptionStartDate");
		assertEquals("Start date must be specified", startDateError);		
								
		String emailListError = fieldToErrorMap.get("subscriptionEditRequest.emailList");
		assertEquals("Email Address must be specified", emailListError);				
	}
	
	@Test
	public void testValidatorSuccesses(){
				
		SubscriptionEditRequest subEditRequest = new SubscriptionEditRequest();
		
		subEditRequest.setSubscriptionType("{http://ojbc.org/wsn/topics}:person/arrest");		
		subEditRequest.setStateId("123");		
		subEditRequest.setFullName("Homer Simpson");	
		subEditRequest.setSubscriptionStartDate(new Date());
		subEditRequest.getEmailList().add("hsimpson@gmail.com");
		
		Map<String, String> fieldToErrorMap = validator.getValidationErrorsList(subEditRequest);
		
		String subTypeError = fieldToErrorMap.get("subscriptionEditRequest.subscriptionType");		
		boolean hasSubTypeError = StringUtils.isNotBlank(subTypeError);				
		assertEquals(false, hasSubTypeError);
		
		String stateIdError = fieldToErrorMap.get("subscriptionEditRequest.stateId");
		boolean hasStateIdError = StringUtils.isNotBlank(stateIdError);
		assertEquals(false, hasStateIdError);
		
		String fullNameError =  fieldToErrorMap.get("subscriptionEditRequest.fullName");
		boolean hasFullNameError = StringUtils.isNotBlank(fullNameError);
		assertEquals(false, hasFullNameError);
		
		String startDateError = fieldToErrorMap.get("subscriptionEditRequest.subscriptionStartDate");
		boolean hasStartDateError = StringUtils.isNotBlank(startDateError);
		assertEquals(false, hasStartDateError);
		
		String emailListError = fieldToErrorMap.get("subscriptionEditRequest.emailList");
		boolean hasEmailError = StringUtils.isNotBlank(emailListError);
		assertEquals(false, hasEmailError);
	}
		
	
	@Test
	public void testInvalidDates(){
		
		SubscriptionEditRequest subEditRequest = new SubscriptionEditRequest();
		
		Calendar startDateCal = Calendar.getInstance();
		startDateCal.set(2014, 10, 21);
		Date startDate = startDateCal.getTime();
				
		Calendar endDateCal = Calendar.getInstance();
		endDateCal.set(2014, 10, 20);
		Date endDate = endDateCal.getTime();
				
		subEditRequest.setSubscriptionStartDate(startDate);		
		subEditRequest.setSubscriptionEndDate(endDate);			

		Map<String, String> fieldToErrorMap = validator.getValidationErrorsList(subEditRequest);
		
		String endDateError = fieldToErrorMap.get("subscriptionEditRequest.subscriptionEndDate");
		assertEquals("End date may not occur before start date", endDateError);		
	}
	
	
	@Test
	public void testValidDates(){
		
		SubscriptionEditRequest subEditReq = new SubscriptionEditRequest();
		
		Calendar startDateCal = Calendar.getInstance();
		startDateCal.set(2014, 10, 21);
		Date startDate = startDateCal.getTime();
				
		Calendar endDateCal = Calendar.getInstance();
		endDateCal.set(2014, 10, 22);
		Date endDate = endDateCal.getTime();
				
		subEditReq.setSubscriptionStartDate(startDate);		
		subEditReq.setSubscriptionEndDate(endDate);			

		Map<String, String> fieldToErrorMap = validator.getValidationErrorsList(subEditReq);
		
		String endDateError = fieldToErrorMap.get("subscriptionAddRequest.subscriptionEndDate");
		boolean hasEndDateError = StringUtils.isNotBlank(endDateError);
		assertEquals(false, hasEndDateError);						
	}	

}


