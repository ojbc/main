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
package org.ojbc.audit.enhanced.dao;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import junit.framework.Assert;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.ojbc.audit.enhanced.dao.model.FederalRapbackNotification;
import org.ojbc.audit.enhanced.dao.model.FederalRapbackRenewalNotification;
import org.ojbc.audit.enhanced.dao.model.FederalRapbackSubscription;
import org.ojbc.audit.enhanced.dao.model.IdentificationQueryResponse;
import org.ojbc.audit.enhanced.dao.model.IdentificationSearchRequest;
import org.ojbc.audit.enhanced.dao.model.PersonQueryCriminalHistoryResponse;
import org.ojbc.audit.enhanced.dao.model.PersonSearchRequest;
import org.ojbc.audit.enhanced.dao.model.PersonSearchResult;
import org.ojbc.audit.enhanced.dao.model.PrintResults;
import org.ojbc.audit.enhanced.dao.model.QueryRequest;
import org.ojbc.audit.enhanced.dao.model.UserAcknowledgement;
import org.ojbc.audit.enhanced.dao.model.UserInfo;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {
        "classpath:META-INF/spring/spring-context.xml"})
@DirtiesContext
public class EnhancedAuditDaoTest {
	
	private static final Log log = LogFactory.getLog(EnhancedAuditDaoTest.class);
	
	@Resource
	private EnhancedAuditDAO enhancedAuditDao;
		
	@Before
	public void init(){
	
		Assert.assertNotNull(enhancedAuditDao);
	}
	
	@Test
	public void testPersonSearchMethods() throws Exception
	{
		Integer userInfoPk = saveUserInfo();
		assertNotNull(userInfoPk);
		log.info("User info pk: " + userInfoPk);
		
		PersonSearchRequest psr = new PersonSearchRequest();
		
		LocalDate dobTo = LocalDate.now();
		LocalDate dobFrom = dobTo.minusYears(20);
		
		psr.setDobFrom(dobFrom);
		psr.setDobTo(dobTo);
		psr.setDriverLicenseId("DL123");
		psr.setDriverLiscenseIssuer("WI");
		psr.setEyeCode("BLK");
		psr.setHairCode("BRN");
		psr.setFbiNumber("FBI123");
		psr.setFirstName("first");
		psr.setFirstNameQualifier(1);
		psr.setLastName("last");
		psr.setLastNameQualifier(1);
		psr.setMessageId("123456");
		psr.setMiddleName("middle");
		psr.setOnBehalfOf("onbehalf");
		psr.setPurpose("purpose");
		psr.setRaceCode("race");
		psr.setSsn("123-45-7890");
		psr.setStateId("state");
		psr.setUserInfofk(userInfoPk);
		psr.setHeight(60);
		psr.setHeightMin(50);
		psr.setHeightMax(75);
		psr.setSsn("999-99-9999");
		
		
		Integer psrIdFromSave = enhancedAuditDao.savePersonSearchRequest(psr);
		
		List<String> systemsToSearch=new ArrayList<String>();
		
		systemsToSearch.add("system1");
		systemsToSearch.add("system2");
		
		psr.setSystemsToSearch(null);
		
		Integer psrIdFromRetreive = enhancedAuditDao.retrievePersonSearchIDfromMessageID("123456");
		
		log.info("ID from save: " + psrIdFromSave);
		log.info("ID from retreive: " + psrIdFromRetreive);
		
		assertEquals(psrIdFromSave, psrIdFromRetreive);
		
		PersonSearchResult psResult = new PersonSearchResult();
		
		psResult.setPersonSearchRequestId(psrIdFromRetreive);
		psResult.setSearchResultsCount(5);
		psResult.setSystemSearchResultURI("{system1}URI");
		psResult.setSearchResultsAccessDeniedText("Access Denied text");
		psResult.setSearchResultsAccessDeniedIndicator(true);
		psResult.setSearchResultsErrorText("search results error text");
		
		Integer systemToSearchID = enhancedAuditDao.retrieveSystemToSearchIDFromURI(psResult.getSystemSearchResultURI());
		
		psResult.setSystemSearchResultID(systemToSearchID);
		
		Integer psresultIdFromSave = enhancedAuditDao.savePersonSearchResult(psResult);
		
		assertNotNull(psresultIdFromSave);
	}
	

	@Test
	public void testUserInfoMethods() throws Exception
	{
		Integer userInfoPk = saveUserInfo();
		
		assertNotNull(userInfoPk);
		
		UserInfo userInfoFromDatabase = enhancedAuditDao.retrieveUserInfoFromId(userInfoPk);
		
		log.debug("User Info From Database: " + userInfoFromDatabase.toString());
		
		assertEquals("Employer Name", userInfoFromDatabase.getEmployerName());
		assertEquals("Sub Unit", userInfoFromDatabase.getEmployerSubunitName());
		assertEquals("Fed ID", userInfoFromDatabase.getFederationId());
		assertEquals("IDP", userInfoFromDatabase.getIdentityProviderId());
		assertEquals("email", userInfoFromDatabase.getUserEmailAddress());
		assertEquals("first", userInfoFromDatabase.getUserFirstName());
		assertEquals("last", userInfoFromDatabase.getUserLastName());
		
	}

	
	private Integer saveUserInfo() {
		UserInfo userInfo = new UserInfo();
		
		userInfo.setEmployerName("Employer Name");
		userInfo.setEmployerSubunitName("Sub Unit");
		userInfo.setFederationId("Fed ID");
		userInfo.setIdentityProviderId("IDP");
		userInfo.setUserEmailAddress("email");
		userInfo.setUserFirstName("first");
		userInfo.setUserLastName("last");
		
		Integer userInfoPk = enhancedAuditDao.saveUserInfo(userInfo);
		return userInfoPk;
	}	

	@Test
	public void testSaveFederalRapbackRenewalNotification() throws Exception
	{
		FederalRapbackRenewalNotification federalRapbackRenewalNotification = new FederalRapbackRenewalNotification();
		
		federalRapbackRenewalNotification.setNotificationRecievedTimestamp(LocalDateTime.now());
		federalRapbackRenewalNotification.setPathToNotificationFile("/tmp/path/toNotificationFile");
		federalRapbackRenewalNotification.setPersonDob(LocalDate.now().minusYears(18));
		federalRapbackRenewalNotification.setPersonFirstName("John");
		federalRapbackRenewalNotification.setPersonMiddleName("q");
		federalRapbackRenewalNotification.setPersonLastName("Public");
		federalRapbackRenewalNotification.setRapbackExpirationDate(LocalDate.now());
		federalRapbackRenewalNotification.setRecordControllingAgency("ORI123456");
		federalRapbackRenewalNotification.setSid("A123456789");
		federalRapbackRenewalNotification.setStateSubscriptionId("S123456");
		federalRapbackRenewalNotification.setTransactionStatusText("Transaction Status Text");
		federalRapbackRenewalNotification.setUcn("UCN123456");
		
		Integer frrPk = enhancedAuditDao.saveFederalRapbackRenewalNotification(federalRapbackRenewalNotification);
		assertNotNull(frrPk);
		
		LocalDate endDate = LocalDate.now().plusDays(1);
		LocalDate startDate = endDate.minusDays(7);
		
		List<FederalRapbackRenewalNotification> federalRapbackRenewalNotifications = enhancedAuditDao.retrieveFederalRapbackRenewalNotifications(startDate, endDate);
	
		assertEquals(1, federalRapbackRenewalNotifications.size());
		
		FederalRapbackRenewalNotification federalRapbackRenewalNotificationFromDatabase = federalRapbackRenewalNotifications.get(0);
		
		assertNotNull(federalRapbackRenewalNotificationFromDatabase.getNotificationRecievedTimestamp());
		assertEquals("/tmp/path/toNotificationFile", federalRapbackRenewalNotificationFromDatabase.getPathToNotificationFile());
		assertEquals(LocalDate.now().minusYears(18), federalRapbackRenewalNotificationFromDatabase.getPersonDob());
		assertEquals("John", federalRapbackRenewalNotificationFromDatabase.getPersonFirstName());
		assertEquals("q", federalRapbackRenewalNotificationFromDatabase.getPersonMiddleName());
		assertEquals("Public", federalRapbackRenewalNotificationFromDatabase.getPersonLastName());
		assertEquals(LocalDate.now(), federalRapbackRenewalNotification.getRapbackExpirationDate());
		assertEquals("ORI123456", federalRapbackRenewalNotificationFromDatabase.getRecordControllingAgency());
		assertEquals("A123456789", federalRapbackRenewalNotificationFromDatabase.getSid());
		assertEquals("S123456", federalRapbackRenewalNotificationFromDatabase.getStateSubscriptionId());
		assertEquals("Transaction Status Text", federalRapbackRenewalNotificationFromDatabase.getTransactionStatusText());
		assertEquals("UCN123456", federalRapbackRenewalNotificationFromDatabase.getUcn());
		
		
	}
		
	@Test
	public void testFederalSubscriptionMethods() throws Exception
	{

		FederalRapbackSubscription federalRapbackSubscription = new FederalRapbackSubscription();
		
		federalRapbackSubscription.setTransactionControlReferenceIdentification("9999999");
		federalRapbackSubscription.setPathToRequestFile("/some/path/to/requestFile");
		federalRapbackSubscription.setRequestSentTimestamp(LocalDateTime.now());
		federalRapbackSubscription.setSid("123");
		federalRapbackSubscription.setStateSubscriptionId("456");
		federalRapbackSubscription.setSubscriptonCategoryCode("CS");
		federalRapbackSubscription.setTransactionCategoryCodeRequest("RBSCRM");
		
		enhancedAuditDao.saveFederalRapbackSubscription(federalRapbackSubscription);

		FederalRapbackSubscription federalRapbackSubscriptionFromDatabase = enhancedAuditDao.retrieveFederalRapbackSubscriptionFromTCN("9999999");
		
		federalRapbackSubscription.setTransactionCategoryCodeResponse("ERRA");
		federalRapbackSubscription.setPathToResponseFile("/some/path/to/responseFile");
		federalRapbackSubscription.setResponseRecievedTimestamp(LocalDateTime.now());
		federalRapbackSubscription.setFederalRapbackSubscriptionId(federalRapbackSubscriptionFromDatabase.getFederalRapbackSubscriptionId());
		federalRapbackSubscription.setFbiSubscriptionId("FBISUBID");
		
		enhancedAuditDao.updateFederalRapbackSubscriptionWithResponse(federalRapbackSubscription);
		
		federalRapbackSubscriptionFromDatabase = enhancedAuditDao.retrieveFederalRapbackSubscriptionFromTCN("9999999");
		
		assertEquals("9999999", federalRapbackSubscriptionFromDatabase.getTransactionControlReferenceIdentification());
		assertEquals("/some/path/to/requestFile", federalRapbackSubscriptionFromDatabase.getPathToRequestFile());
		assertEquals("/some/path/to/responseFile", federalRapbackSubscriptionFromDatabase.getPathToResponseFile());
		assertEquals("ERRA", federalRapbackSubscriptionFromDatabase.getTransactionCategoryCodeResponse());
		assertEquals("RBSCRM", federalRapbackSubscriptionFromDatabase.getTransactionCategoryCodeRequest());
		assertEquals("FBISUBID", federalRapbackSubscriptionFromDatabase.getFbiSubscriptionId());
		
		assertEquals("123", federalRapbackSubscriptionFromDatabase.getSid());
		assertEquals("456", federalRapbackSubscriptionFromDatabase.getStateSubscriptionId());
		assertEquals("CS", federalRapbackSubscriptionFromDatabase.getSubscriptonCategoryCode());
		
		List<FederalRapbackSubscription> federalSubscriptions = enhancedAuditDao.retrieveFederalRapbackSubscriptionFromStateSubscriptionId("456");
		
		assertEquals(1, federalSubscriptions.size());
		
		federalRapbackSubscriptionFromDatabase = federalSubscriptions.get(0);
		
		assertEquals("9999999", federalRapbackSubscriptionFromDatabase.getTransactionControlReferenceIdentification());
		assertEquals("/some/path/to/requestFile", federalRapbackSubscriptionFromDatabase.getPathToRequestFile());
		assertEquals("/some/path/to/responseFile", federalRapbackSubscriptionFromDatabase.getPathToResponseFile());
		assertEquals("ERRA", federalRapbackSubscriptionFromDatabase.getTransactionCategoryCodeResponse());
		
		assertEquals("123", federalRapbackSubscriptionFromDatabase.getSid());
		assertEquals("456", federalRapbackSubscriptionFromDatabase.getStateSubscriptionId());
		assertEquals("CS", federalRapbackSubscriptionFromDatabase.getSubscriptonCategoryCode());
		
	}
	
	@Test
	public void testFederalRapbackNotificationMethods() throws Exception
	{
		FederalRapbackNotification federalRapbackNotification = new FederalRapbackNotification();

		federalRapbackNotification.setNotificationRecievedTimestamp(LocalDateTime.now());
		federalRapbackNotification.setOriginalIdentifier("123");
		federalRapbackNotification.setUpdatedIdentifier("456");
		federalRapbackNotification.setPathToNotificationFile("/tmp/path/toNotificationFile");
		federalRapbackNotification.setRapBackEventText("Rapback event text");
		federalRapbackNotification.setStateSubscriptionId("State12345");
		federalRapbackNotification.setTransactionType("UCN_Consolidation");
		federalRapbackNotification.setRecordRapBackActivityNotificationID("7654");
		
		enhancedAuditDao.saveFederalRapbackNotification(federalRapbackNotification);
		
		LocalDate endDate = LocalDate.now().plusDays(1);
		LocalDate startDate = endDate.minusDays(7);
		
		List<FederalRapbackNotification> federalNotifications = enhancedAuditDao.retrieveFederalNotifications(startDate, endDate);
		
		assertEquals(1, federalNotifications.size());
		
		assertEquals("123",federalNotifications.get(0).getOriginalIdentifier());
		assertEquals("456",federalNotifications.get(0).getUpdatedIdentifier());
		assertEquals("/tmp/path/toNotificationFile",federalNotifications.get(0).getPathToNotificationFile());
		assertEquals("Rapback event text",federalNotifications.get(0).getRapBackEventText());
		assertEquals("State12345",federalNotifications.get(0).getStateSubscriptionId());
		assertEquals("UCN_Consolidation",federalNotifications.get(0).getTransactionType());
		assertEquals("7654", federalNotifications.get(0).getRecordRapBackActivityNotificationID());
		
		federalNotifications = enhancedAuditDao.retrieveFederalNotificationsBySubscriptionId("State12345");

		assertEquals(1, federalNotifications.size());
		
		assertEquals("123",federalNotifications.get(0).getOriginalIdentifier());
		assertEquals("456",federalNotifications.get(0).getUpdatedIdentifier());
		assertEquals("/tmp/path/toNotificationFile",federalNotifications.get(0).getPathToNotificationFile());
		assertEquals("Rapback event text",federalNotifications.get(0).getRapBackEventText());
		assertEquals("State12345",federalNotifications.get(0).getStateSubscriptionId());
		assertEquals("UCN_Consolidation",federalNotifications.get(0).getTransactionType());

	}
		
	@Test
	public void testQueryMethods() throws Exception
	{
		Integer userInfoPk = saveUserInfo();
		
		QueryRequest queryRequest = new QueryRequest();
		
		queryRequest.setIdentificationId("123");
		queryRequest.setIdentificationSourceText("Source");
		queryRequest.setMessageId("123456");
		queryRequest.setUserInfofk(userInfoPk);
		
		Integer queryPk = enhancedAuditDao.saveQueryRequest(queryRequest);
		
		assertNotNull(queryPk);
		
		PersonQueryCriminalHistoryResponse personQueryCriminalHistoryResponse = new PersonQueryCriminalHistoryResponse();
		
		personQueryCriminalHistoryResponse.setQueryRequestId(queryPk);
		personQueryCriminalHistoryResponse.setSystemName("Criminal History");
		personQueryCriminalHistoryResponse.setMessageId("123456");
		personQueryCriminalHistoryResponse.setFbiId("12345");
		personQueryCriminalHistoryResponse.setFirstName("first");
		personQueryCriminalHistoryResponse.setMiddleName("middle");
		personQueryCriminalHistoryResponse.setLastName("last");
		personQueryCriminalHistoryResponse.setSid("SID123456");
		
		Integer queryResponsePk = enhancedAuditDao.savePersonQueryCriminalHistoryResponse(personQueryCriminalHistoryResponse);
		
		assertNotNull(queryResponsePk);
		
		IdentificationQueryResponse identificationQueryResponse = new IdentificationQueryResponse();
		
		identificationQueryResponse.setQueryRequestId(queryPk);
		identificationQueryResponse.setFbiId("123");
		identificationQueryResponse.setIdDate(LocalDate.now());
		identificationQueryResponse.setMessageId("123456");
		identificationQueryResponse.setOca("oca");
		identificationQueryResponse.setOtn("otn");
		identificationQueryResponse.setPersonFirstName("first");
		identificationQueryResponse.setPersonMiddleName("middle");
		identificationQueryResponse.setPersonLastName("last");
		identificationQueryResponse.setSid("A123");
		
		Integer identificationQueryResponsePk = enhancedAuditDao.saveidentificationQueryResponse(identificationQueryResponse);
		
		assertNotNull(identificationQueryResponsePk);
		
	}
	
	@Test
	public void testIdentificationMethods() throws Exception
	{
		Integer userInfoPk = saveUserInfo();
		
		LocalDate dobTo = LocalDate.now();
		LocalDate dobFrom = dobTo.minusYears(1);
		String messageId="88337755";
		
		IdentificationSearchRequest identificationSearchRequest = new IdentificationSearchRequest();
		
		identificationSearchRequest.setFirstName("first");
		identificationSearchRequest.setIdentificationResultsStatus("status");
		identificationSearchRequest.setLastName("last");
		identificationSearchRequest.setMessageId(messageId);
		identificationSearchRequest.setOtn("OTN");
		
		List<String> reasonCodes = new ArrayList<String>();
		
		reasonCodes.add("reason1");
		reasonCodes.add("reason2");
		
		identificationSearchRequest.setReasonCode(reasonCodes);
		
		identificationSearchRequest.setReportedFromDate(dobFrom);
		identificationSearchRequest.setReportedToDate(dobTo);
		identificationSearchRequest.setUserInfoId(userInfoPk);
		
		Integer identificationSearchRequestPK = enhancedAuditDao.saveIdentificationSearchRequest(identificationSearchRequest);
		
		assertNotNull(identificationSearchRequestPK);
		
		Integer reasonCode1 = enhancedAuditDao.retrieveIdentificationReasonCodeFromDescription("reason1");
		Integer reasonCode2 = enhancedAuditDao.retrieveIdentificationReasonCodeFromDescription("reason2");
		
		assertNotNull(reasonCode1);
		assertNotNull(reasonCode2);
		
		Integer joinerPk1 = enhancedAuditDao.saveIdentificationReasonCode(reasonCode1, identificationSearchRequestPK);
		Integer joinerPk2 = enhancedAuditDao.saveIdentificationReasonCode(reasonCode2, identificationSearchRequestPK);
		
		assertNotNull(joinerPk1);
		assertNotNull(joinerPk2);
		
		
	}	
	
	@Test
	public void testSavePrintResults() throws Exception
	{
		PrintResults printResults = new PrintResults();
		
		printResults.setDescription("description");
		printResults.setMessageId("123456");
		printResults.setSystemName("system name");
		
		Integer prPk = enhancedAuditDao.savePrintResults(printResults);
		
		assertNotNull(prPk);
		
		PrintResults printResultsResponse = enhancedAuditDao.retrievePrintResultsfromMessageID("123456");
		
		assertEquals("description", printResultsResponse.getDescription());
		assertEquals("123456", printResultsResponse.getMessageId());
		assertEquals("system name", printResultsResponse.getSystemName());
	}	

	@Test
	public void testSaveUserLogin() throws Exception
	{
		Integer userInfoPk = saveUserInfo();

		Integer userLoginPk = enhancedAuditDao.saveUserAuthentication(userInfoPk, "login");
		
		assertNotNull(userLoginPk);
		
	}	
	
	@Test
	public void testAuditResolveSubscriptionErrors() throws Exception
	{
		String stateSubscriptionId = "State1112233";
		
		FederalRapbackSubscription federalRapbackSubscription = new FederalRapbackSubscription();
		
		federalRapbackSubscription.setTransactionControlReferenceIdentification("9999999");
		federalRapbackSubscription.setPathToRequestFile("/some/path/to/requestFile");
		federalRapbackSubscription.setRequestSentTimestamp(LocalDateTime.now());
		federalRapbackSubscription.setSid("123");
		federalRapbackSubscription.setStateSubscriptionId(stateSubscriptionId);
		federalRapbackSubscription.setFbiSubscriptionId("789");
		federalRapbackSubscription.setSubscriptonCategoryCode("CS");
		federalRapbackSubscription.setTransactionCategoryCodeRequest("RBSCRM");
		federalRapbackSubscription.setTransactionStatusText("This is an FBI error");
		
		//Save subscription with error
		Integer federalSubcriptionId = enhancedAuditDao.saveFederalRapbackSubscription(federalRapbackSubscription);
		
		//Now save error
		enhancedAuditDao.saveFederalRapbackSubscriptionError(federalSubcriptionId, stateSubscriptionId);
		
		//Ensure we get a single error back from subscription
		Integer federalSubscriptionId = enhancedAuditDao.retrieveFederalRapbackSubscriptionError(stateSubscriptionId);
		assertNotNull(federalSubscriptionId);
		
		//Now test the DAO method that returns all errors, we should get one there too
		List<FederalRapbackSubscription> frs = enhancedAuditDao.retrieveFederalRapbackSubscriptionErrors();
		assertEquals(1, frs.size());
		
		//Resolve error
		enhancedAuditDao.resolveFederalRapbackSubscriptionError(stateSubscriptionId);
		
		federalSubscriptionId = enhancedAuditDao.retrieveFederalRapbackSubscriptionError(stateSubscriptionId);
		assertNull(federalSubscriptionId);

	}

	@Test
	public void testSaveuserAcknowledgement() throws Exception
	{
		Integer userInfoPk = saveUserInfo();
		assertNotNull(userInfoPk);
		log.info("User info pk: " + userInfoPk);
		
		UserAcknowledgement userAcknowledgement = new UserAcknowledgement();
		
		UserInfo userInfo = new UserInfo();
		userInfo.setUserInfoId(userInfoPk);
		
		userAcknowledgement.setUserInfo(userInfo);
		
		LocalDateTime now = LocalDateTime.now();
		
		userAcknowledgement.setDecision(true);
		userAcknowledgement.setDecisionDateTime(now);
		userAcknowledgement.setSid("A123456789");
		
		Integer userAckPk = enhancedAuditDao.saveuserAcknowledgement(userAcknowledgement);
		assertNotNull(userAckPk);
		
		List<UserAcknowledgement> userAcknowledgements = enhancedAuditDao.retrieveUserAcknowledgement("Fed ID");
		
		assertEquals(1, userAcknowledgements.size());
		
		UserAcknowledgement userAcknowledgementFromDatabase = userAcknowledgements.get(0);
		
		assertEquals("A123456789", userAcknowledgementFromDatabase.getSid());
		assertEquals(true, userAcknowledgementFromDatabase.isDecision());
		
		
		assertEquals("Employer Name", userAcknowledgementFromDatabase.getUserInfo().getEmployerName());
		assertEquals("Sub Unit", userAcknowledgementFromDatabase.getUserInfo().getEmployerSubunitName());
		assertEquals("Fed ID", userAcknowledgementFromDatabase.getUserInfo().getFederationId());
		assertEquals("IDP", userAcknowledgementFromDatabase.getUserInfo().getIdentityProviderId());
		assertEquals("email", userAcknowledgementFromDatabase.getUserInfo().getUserEmailAddress());
		assertEquals("first", userAcknowledgementFromDatabase.getUserInfo().getUserFirstName());
		assertEquals("last", userAcknowledgementFromDatabase.getUserInfo().getUserLastName());
		
	}
}

