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
package org.ojbc.web.portal.validators;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.ojbc.util.xml.subscription.Subscription;
import org.ojbc.web.portal.controllers.SubscriptionsController;
import org.ojbc.web.portal.validators.subscriptions.SubscriptionValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.Errors;
@RunWith(SpringJUnit4ClassRunner.class)
@WebAppConfiguration
@ContextConfiguration({
        "classpath:dispatcher-servlet.xml",
        "classpath:application-context.xml",
        "classpath:static-configuration-demostate.xml", "classpath:security-context.xml"
        })
@ActiveProfiles("standalone")
@DirtiesContext
public class SubscriptionValidatorTest {
	
	@Autowired
	protected SubscriptionValidator subscriptionValidator;
	
	private Errors errors;

	private void assertEndDateEarlyThanStartDateError(Subscription subscription) {
		errors = new BeanPropertyBindingResult(subscription, "subscription");
		subscriptionValidator.validate(subscription, errors);
		assertThat(errors.getErrorCount(), is(1));
		assertThat(errors.getFieldError("subscriptionEndDate").getCode(), is("End Date may not occur before Start Date"));
	}
		
	private void assertEndDateBeyondRange(Subscription subscription, int years) {
		errors = new BeanPropertyBindingResult(subscription, "subscription");
		subscriptionValidator.validate(subscription, errors);
		assertThat(errors.getErrorCount(), is(1));
		assertThat(errors.getFieldError("subscriptionEndDate").getCode(), is("End Date may not be more than " + years + " year after the Start Date"));
	}
	
	private void assertNoErrors(Subscription subscription) {
		errors = new BeanPropertyBindingResult(subscription, "subscription");
		subscriptionValidator.validate(subscription, errors);
		assertThat(errors.hasErrors(), is(false));
	}
		
	private void assertDisclosureAttentionDesignationTextMissing(Subscription subscription) {
		errors = new BeanPropertyBindingResult(subscription, "subscription");
		subscriptionValidator.validate(subscription, errors);
		assertThat(errors.getErrorCount(), is(0));
	}
	
	private void assertDisclosureIndicatorNotSelected(Subscription subscription) {
		errors = new BeanPropertyBindingResult(subscription, "subscription");
		subscriptionValidator.validate(subscription, errors);
		assertThat(errors.getErrorCount(), is(1));
		assertThat(errors.getFieldError("federalRapSheetDisclosureIndicator").getCode(),
				is("Disclosure Indicator must be selected"));
	}
	
	private Subscription getBaseArrestSubscription(String topic) {
		Subscription subscription = new Subscription();
		subscription.setTopic(topic);
		subscription.setStateId("123");		
		subscription.setFullName("Homer Simpson");	
		subscription.setSubscriptionStartDate(new Date());
		subscription.getEmailList().add("hsimpson@gmail.com");
		return subscription;
	}
	
	private Subscription getBaseRapbackSubscription(String purpose) {
		Subscription subscription = getBaseArrestSubscription(SubscriptionsController.RAPBACK_TOPIC_SUB_TYPE);
		setValidStartEndDates(subscription);
		setDateOfBirth(subscription);
		subscription.setSubscriptionPurpose(purpose);
		subscription.setCaseId("caseId");
		subscription.setFederalTriggeringEventCode(Arrays.asList("ARREST"));
		subscription.setFederalRapSheetDisclosureIndicator(Boolean.TRUE);
		subscription.setFederalRapSheetDisclosureAttentionDesignationText("Some contact info");;
		return subscription;
	}

	private Subscription getGoodNonArrestSubscription(String topic) {
		Subscription subscription = new Subscription();
		
		subscription.setTopic(topic);		
		subscription.setStateId("123");		
		subscription.setFirstName("Homer");
		subscription.setLastName("Simpson");			
		setValidStartEndDates(subscription);
		
		setDateOfBirth(subscription);
		subscription.getEmailList().add("hsimpson@gmail.com");
		return subscription;
	}

	private void setDateOfBirth(Subscription subscription) {
		Calendar calendar = Calendar.getInstance(); 
		calendar.add(Calendar.YEAR, -25);
		subscription.setDateOfBirth(calendar.getTime());
	}

	private void setEndDateEarlierThanStartDate(Subscription subscription) {
		Calendar startDateCal = Calendar.getInstance();
		startDateCal.set(2014, 10, 21);
		Date startDate = startDateCal.getTime();
		
		Calendar endDateCal = Calendar.getInstance();
		endDateCal.set(2014, 10, 20);
		Date endDate = endDateCal.getTime();
		
		subscription.setSubscriptionStartDate(startDate);		
		subscription.setSubscriptionEndDate(endDate);
	}
	
	private void setEndDateBeyondRange(Subscription subscription, int years) {
		Calendar startDateCal = Calendar.getInstance();
		startDateCal.set(2014, 10, 21);
		Date startDate = startDateCal.getTime();
		
		startDateCal.add(Calendar.YEAR, years);
		startDateCal.add(Calendar.DAY_OF_YEAR, 1);
		Date endDate = startDateCal.getTime();
		
		subscription.setSubscriptionStartDate(startDate);		
		subscription.setSubscriptionEndDate(endDate);
	}
	
	private void setEndDateRightWithinRange(Subscription subscription, int years) {
		Calendar startDateCal = Calendar.getInstance();
		startDateCal.set(2014, 10, 21);
		Date startDate = startDateCal.getTime();
		
		startDateCal.add(Calendar.YEAR, years);
		Date endDate = startDateCal.getTime();
		
		subscription.setSubscriptionStartDate(startDate);		
		subscription.setSubscriptionEndDate(endDate);
	}
	
	private void setValidStartEndDates(Subscription subscription) {
		Calendar startDateCal = Calendar.getInstance();
		startDateCal.set(2014, 10, 21);
		Date startDate = startDateCal.getTime();
				
		Calendar endDateCal = Calendar.getInstance();
		endDateCal.set(2015, 10, 20);
		Date endDate = endDateCal.getTime();
				
		subscription.setSubscriptionStartDate(startDate);		
		subscription.setSubscriptionEndDate(endDate);
	}
	
	@Test
	public void testValidatingArrestSubscriptionWithEndDateTooBig() throws Exception{
		Subscription subscription = getBaseArrestSubscription(SubscriptionsController.ARREST_TOPIC_SUB_TYPE);
		setEndDateBeyondRange(subscription, 1);		
		assertEndDateBeyondRange(subscription, 1);
	}
	
	@Test
	public void testValidatingEarlyEndDateArrestSubscription() throws Exception{
		Subscription subscription = getBaseArrestSubscription(SubscriptionsController.ARREST_TOPIC_SUB_TYPE);
		setEndDateEarlierThanStartDate(subscription);		
		
		assertEndDateEarlyThanStartDateError(subscription);
	}
	
	public void testValidatingEarlyEndDateNonArrestSubscription(String topic) throws Exception{
		Subscription subscription = getGoodNonArrestSubscription(topic);
		setEndDateEarlierThanStartDate(subscription);		
		assertEndDateEarlyThanStartDateError(subscription);
	}
	
	@Test
	public void testValidatingEarlyEndDateNonArrestSubscriptions() throws Exception{
		testValidatingEarlyEndDateNonArrestSubscription(SubscriptionsController.CHCYCLE_TOPIC_SUB_TYPE);
		testValidatingEarlyEndDateNonArrestSubscription(SubscriptionsController.INCIDENT_TOPIC_SUB_TYPE);
	}
	
	@Test
	public void testValidatingEmptyArrestSubscription() throws Exception{
		
		Subscription subscription = new Subscription();
		subscription.setTopic(SubscriptionsController.ARREST_TOPIC_SUB_TYPE);
		errors = new BeanPropertyBindingResult(subscription, "subscription");
		subscriptionValidator.validate(subscription, errors);
		assertThat(errors.getErrorCount(), is(4));
		assertThat(errors.hasFieldErrors("stateId"), is(true));
		assertThat(errors.hasFieldErrors("fullName"), is(true));
		assertThat(errors.hasFieldErrors("subscriptionStartDate"), is(true));
		assertThat(errors.hasFieldErrors("emailList"), is(true));
		assertThat(errors.getFieldError("stateId").getCode(), is("SID must be specified"));
		assertThat(errors.getFieldError("fullName").getCode(), is("Name must be specified"));
		assertThat(errors.getFieldError("subscriptionStartDate").getCode(), is("Start Date must be specified"));
		assertThat(errors.getFieldError("emailList").getCode(), is("Email Address must be specified"));
	}
	
	public void testValidatingEmptyNonArrestSubscription(String topic) throws Exception{
		Subscription subscription = new Subscription();
		subscription.setTopic(SubscriptionsController.INCIDENT_TOPIC_SUB_TYPE);
		errors = new BeanPropertyBindingResult(subscription, "subscription");
		subscriptionValidator.validate(subscription, errors);
		assertThat(errors.hasErrors(), is(true));
		assertThat(errors.getErrorCount(), is(5));
		assertThat(errors.getFieldError("firstName").getCode(), is("First name must be specified"));
		assertThat(errors.getFieldError("lastName").getCode(), is("Last name must be specified"));
		assertThat(errors.getFieldError("dateOfBirth").getCode(), is("DOB must be specified"));
		assertThat(errors.getFieldError("subscriptionStartDate").getCode(), is("Start Date must be specified"));
		assertThat(errors.getFieldError("emailList").getCode(), is("Email Address must be specified"));
	}
	
	@Test
	public void testValidatingEmptyNonArrestSubscriptions() throws Exception{
		testValidatingEmptyNonArrestSubscription(SubscriptionsController.CHCYCLE_TOPIC_SUB_TYPE);
		testValidatingEmptyNonArrestSubscription(SubscriptionsController.INCIDENT_TOPIC_SUB_TYPE);
	}

	@Test
	public void testValidatingEmptyRapbackSubscription() throws Exception{
		Subscription subscription = new Subscription();
		subscription.setTopic(SubscriptionsController.RAPBACK_TOPIC_SUB_TYPE);
		errors = new BeanPropertyBindingResult(subscription, "subscription");
		subscriptionValidator.validate(subscription, errors);
		assertThat(errors.getErrorCount(), is(9));
		assertThat(errors.getFieldError("federalTriggeringEventCode").getCode(), is("Arrest triggering Event must be selected"));
		assertThat(errors.getFieldError("subscriptionPurpose").getCode(), is("Purpose must be specified"));
		assertThat(errors.getFieldError("stateId").getCode(), is("SID must be specified"));
		assertThat(errors.getFieldError("fullName").getCode(), is("Name must be specified"));
		assertThat(errors.getFieldError("dateOfBirth").getCode(), is("Date of Birth must be specified"));
		assertThat(errors.getFieldError("subscriptionStartDate").getCode(), is("Start Date must be specified"));
		assertThat(errors.getFieldError("subscriptionEndDate").getCode(), is("End Date must be specified"));
		assertThat(errors.getFieldError("emailList").getCode(), is("Email Address must be specified"));
		assertThat(errors.getFieldError("caseId").getCode(), is("Case ID must be specified"));
	}
	
	@Test
	public void testValidatingEmptySubscription() throws Exception{
		
		Subscription subscription = new Subscription();
		errors = new BeanPropertyBindingResult(subscription, "subscription");
		
		subscriptionValidator.validate(subscription, errors);
		assertThat(errors.hasErrors(), is(true));
		assertThat(errors.hasFieldErrors("topic"), is(true));
	}
	
	@Test
	public void testValidatingGoodArrestSubscription() throws Exception{
		Subscription subscription = getBaseArrestSubscription(SubscriptionsController.ARREST_TOPIC_SUB_TYPE);
		setValidStartEndDates(subscription);
		assertNoErrors(subscription);
	}
	
	public void testValidatingGoodIncidentSubscription(String topic) throws Exception{
		Subscription subscription = getGoodNonArrestSubscription(SubscriptionsController.INCIDENT_TOPIC_SUB_TYPE);
		assertNoErrors(subscription);
	}

	public void testValidatingGoodVehicleCrashSubscription() throws Exception{
		Subscription subscription = getGoodNonArrestSubscription(SubscriptionsController.PERSON_VEHICLE_CRASH_TOPIC_SUB_TYPE);
		assertNoErrors(subscription);
	}
	
	@Test
	public void testValidatingGoodNonArrestSubscriptions() throws Exception{
		testValidatingGoodIncidentSubscription(SubscriptionsController.CHCYCLE_TOPIC_SUB_TYPE);
		testValidatingGoodIncidentSubscription(SubscriptionsController.INCIDENT_TOPIC_SUB_TYPE);
		testValidatingGoodVehicleCrashSubscription();
	}
	
	@Test
	public void testValidatingGoodRapbackSubscription() throws Exception{
		Subscription subscription = getBaseRapbackSubscription("CI");
		assertNoErrors(subscription);
		
		subscription = getBaseRapbackSubscription("CS");
		assertNoErrors(subscription);
		
		setEndDateRightWithinRange(subscription, 5);
		assertNoErrors(subscription);
		
		subscription.setSubscriptionPurpose("CI");
		subscription.setFederalRapSheetDisclosureIndicator(null);
		setEndDateRightWithinRange(subscription, 1);
		assertNoErrors(subscription);
		
	}

	@Test
	public void testValidatingOutofRangeEndDateRapbackSubscription() throws Exception{
		Subscription subscription = getBaseRapbackSubscription("CI");
		setEndDateEarlierThanStartDate(subscription);
		assertEndDateEarlyThanStartDateError(subscription);
		
		setEndDateBeyondRange(subscription, 1);
		assertEndDateBeyondRange(subscription, 1);
		
		subscription = getBaseRapbackSubscription("CS");
		setEndDateEarlierThanStartDate(subscription);
		assertEndDateEarlyThanStartDateError(subscription);
		
		setEndDateBeyondRange(subscription, 5);
		assertEndDateBeyondRange(subscription, 5);
	}
	
	@Test
	public void testValidatingRapbackSubscriptionWithDisclosureOptions() throws Exception{
		Subscription subscription = getBaseRapbackSubscription("CI");
		subscription.setFederalRapSheetDisclosureAttentionDesignationText(null);;
		assertDisclosureAttentionDesignationTextMissing(subscription);
		
		subscription.setSubscriptionPurpose("CS");
		assertDisclosureAttentionDesignationTextMissing(subscription);
		
		subscription.setFederalRapSheetDisclosureIndicator(null);
		assertDisclosureIndicatorNotSelected(subscription);
		
	}
}

