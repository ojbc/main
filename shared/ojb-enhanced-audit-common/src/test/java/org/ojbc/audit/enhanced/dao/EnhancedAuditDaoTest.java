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
import static org.junit.Assert.assertTrue;

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
import org.ojbc.audit.enhanced.dao.model.CrashVehicle;
import org.ojbc.audit.enhanced.dao.model.FederalRapbackIdentityHistory;
import org.ojbc.audit.enhanced.dao.model.FederalRapbackNotification;
import org.ojbc.audit.enhanced.dao.model.FederalRapbackRenewalNotification;
import org.ojbc.audit.enhanced.dao.model.FederalRapbackSubscription;
import org.ojbc.audit.enhanced.dao.model.FirearmSearchResult;
import org.ojbc.audit.enhanced.dao.model.FirearmsQueryResponse;
import org.ojbc.audit.enhanced.dao.model.FirearmsSearchRequest;
import org.ojbc.audit.enhanced.dao.model.IdentificationQueryResponse;
import org.ojbc.audit.enhanced.dao.model.IdentificationSearchRequest;
import org.ojbc.audit.enhanced.dao.model.IncidentReportQueryResponse;
import org.ojbc.audit.enhanced.dao.model.IncidentSearchRequest;
import org.ojbc.audit.enhanced.dao.model.NotificationSent;
import org.ojbc.audit.enhanced.dao.model.PersonQueryCriminalHistoryResponse;
import org.ojbc.audit.enhanced.dao.model.PersonQueryWarrantResponse;
import org.ojbc.audit.enhanced.dao.model.PersonSearchRequest;
import org.ojbc.audit.enhanced.dao.model.PersonSearchResult;
import org.ojbc.audit.enhanced.dao.model.PrintResults;
import org.ojbc.audit.enhanced.dao.model.ProfessionalLicensingQueryResponse;
import org.ojbc.audit.enhanced.dao.model.QueryRequest;
import org.ojbc.audit.enhanced.dao.model.SubscriptionAction;
import org.ojbc.audit.enhanced.dao.model.SubscriptionQueryResponse;
import org.ojbc.audit.enhanced.dao.model.UserAcknowledgement;
import org.ojbc.audit.enhanced.dao.model.UserInfo;
import org.ojbc.audit.enhanced.dao.model.VehicleCrashQueryResponse;
import org.ojbc.audit.enhanced.dao.model.VehicleSearchRequest;
import org.ojbc.audit.enhanced.dao.model.WildlifeQueryResponse;
import org.ojbc.audit.enhanced.dao.model.auditsearch.AuditSearchRequest;
import org.ojbc.audit.enhanced.dao.model.auditsearch.UserAuthenticationSearchRequest;
import org.ojbc.audit.enhanced.dao.model.auditsearch.UserAuthenticationSearchResponse;
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
	public void testFirearmSearchMethods() throws Exception
	{
		Integer userInfoPk = saveUserInfo();
		assertNotNull(userInfoPk);
		log.info("User info pk: " + userInfoPk);
		
		FirearmsSearchRequest fsr = new FirearmsSearchRequest();
		
		fsr.setMessageId("123456");
		fsr.setOnBehalfOf("onbehalf");
		fsr.setPurpose("purpose");
		fsr.setUserInfofk(userInfoPk);
		fsr.setCurrentRegistrationsOnly(true);
		fsr.setFirearmsType("handgun");
		fsr.setMake("glock");
		fsr.setModel("9mm");
		fsr.setRegistrationNumber("123");
		fsr.setSerialNumber("432");
		
		Integer fsrIdFromSave = enhancedAuditDao.saveFirearmsSearchRequest(fsr);
		
		enhancedAuditDao.saveFirearmsSystemToSearch(fsrIdFromSave, 1);
		enhancedAuditDao.saveFirearmsSystemToSearch(fsrIdFromSave, 2);
		
		Integer fsrIdFromRetreive = enhancedAuditDao.retrieveFirearmSearchIDfromMessageID("123456");
		
		log.info("ID from save: " + fsrIdFromSave);
		log.info("ID from retreive: " + fsrIdFromRetreive);
		
		assertEquals(fsrIdFromSave, fsrIdFromRetreive);
		
		AuditSearchRequest firearmsAuditSearchRequest = new AuditSearchRequest();
		
		firearmsAuditSearchRequest.setStartTime(LocalDateTime.now().minusHours(1));
		firearmsAuditSearchRequest.setEndTime(LocalDateTime.now().plusHours(1));
		
		List<FirearmsSearchRequest> firearmsSearchRequests = enhancedAuditDao.retrieveFirearmSearchRequest(firearmsAuditSearchRequest);
		
		//Additional assertions in processor test
		assertEquals(1, firearmsSearchRequests.size());
					
		FirearmSearchResult fsResult = new FirearmSearchResult();
		
		fsResult.setFirearmSearchRequestId(fsrIdFromRetreive);
		fsResult.setSearchResultsCount(5);
		fsResult.setSystemSearchResultURI("{system1}URI");
		fsResult.setSearchResultsAccessDeniedText("Access Denied text");
		fsResult.setSearchResultsAccessDeniedIndicator(true);
		fsResult.setSearchResultsErrorText("search results error text");
		
		Integer systemToSearchID = enhancedAuditDao.retrieveSystemToSearchIDFromURI(fsResult.getSystemSearchResultURI());
		
		fsResult.setSystemSearchResultID(systemToSearchID);
		
		Integer fsresultIdFromSave = enhancedAuditDao.saveFirearmSearchResult(fsResult);
		
		assertNotNull(fsresultIdFromSave);
		
		List<FirearmSearchResult> firearmSearchResult = enhancedAuditDao.retrieveFirearmSearchResults(fsrIdFromSave);
		assertEquals(1, firearmSearchResult.size());
		
		log.info(firearmSearchResult.get(0).toString());
		
		assertEquals(new Integer(5), firearmSearchResult.get(0).getSearchResultsCount());
		assertEquals("system1", firearmSearchResult.get(0).getSystemName());
		assertEquals("{system1}URI", firearmSearchResult.get(0).getSystemURI());
		assertEquals("search results error text", firearmSearchResult.get(0).getSearchResultsErrorText());
		assertTrue( firearmSearchResult.get(0).getSearchResultsAccessDeniedIndicator());
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
		
		enhancedAuditDao.savePersonSystemToSearch(psrIdFromSave, 1);
		enhancedAuditDao.savePersonSystemToSearch(psrIdFromSave, 2);
		
		Integer psrIdFromRetreive = enhancedAuditDao.retrievePersonSearchIDfromMessageID("123456");
		
		log.info("ID from save: " + psrIdFromSave);
		log.info("ID from retreive: " + psrIdFromRetreive);
		
		assertEquals(psrIdFromSave, psrIdFromRetreive);
		
		AuditSearchRequest personAuditSearchRequest = new AuditSearchRequest();
		
		personAuditSearchRequest.setStartTime(LocalDateTime.now().minusHours(1));
		personAuditSearchRequest.setEndTime(LocalDateTime.now().plusHours(1));
		
		List<PersonSearchRequest> personSearchRequests = enhancedAuditDao.retrievePersonSearchRequest(personAuditSearchRequest);
		
		//Additional assertions in processor test
		assertEquals(1, personSearchRequests.size());
					
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
		
		List<PersonSearchResult> personSearchResults = enhancedAuditDao.retrievePersonSearchResults(psrIdFromSave);
		assertEquals(1, personSearchResults.size());
		
		log.info(personSearchResults.get(0).toString());
		
		assertEquals(new Integer(5), personSearchResults.get(0).getSearchResultsCount());
		assertEquals("system1", personSearchResults.get(0).getSystemName());
		assertEquals("{system1}URI", personSearchResults.get(0).getSystemURI());
		assertEquals("search results error text", personSearchResults.get(0).getSearchResultsErrorText());
		assertTrue( personSearchResults.get(0).getSearchResultsAccessDeniedIndicator());
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
		
		
		List<UserInfo> userInfoEntries = enhancedAuditDao.retrieveUserInfoFromFederationId(userInfoFromDatabase.getFederationId());
		
		assertNotNull(userInfoEntries);
		assertTrue(userInfoEntries.size() > 0);
		
		userInfoFromDatabase = userInfoEntries.get(0);
		
		assertEquals("Employer Name", userInfoFromDatabase.getEmployerName());
		assertEquals("Sub Unit", userInfoFromDatabase.getEmployerSubunitName());
		assertEquals("Fed ID", userInfoFromDatabase.getFederationId());
		assertEquals("IDP", userInfoFromDatabase.getIdentityProviderId());
		assertEquals("email", userInfoFromDatabase.getUserEmailAddress());
		assertEquals("first", userInfoFromDatabase.getUserFirstName());
		assertEquals("last", userInfoFromDatabase.getUserLastName());
		
		userInfoEntries = enhancedAuditDao.retrieveAllUsers();
		
		assertTrue(userInfoEntries.size() > 0);
		
	}

	
	private Integer saveUserInfo() {
		UserInfo userInfo = getExampleUserInfo();
		
		Integer userInfoPk = enhancedAuditDao.saveUserInfo(userInfo);
		return userInfoPk;
	}

	private UserInfo getExampleUserInfo() {
		UserInfo userInfo = new UserInfo();
		
		userInfo.setEmployerName("Employer Name");
		userInfo.setEmployerSubunitName("Sub Unit");
		userInfo.setFederationId("Fed ID");
		userInfo.setIdentityProviderId("IDP");
		userInfo.setUserEmailAddress("email");
		userInfo.setUserFirstName("first");
		userInfo.setUserLastName("last");
		userInfo.setEmployerOri("employer ori");
		return userInfo;
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
	public void testFederalRapbackIdentityHistoryMethods() throws Exception
	{

		FederalRapbackIdentityHistory federalRapbackIdentityHistory = new FederalRapbackIdentityHistory();
		
		federalRapbackIdentityHistory.setFbiNotificationId("notificationid1");
		federalRapbackIdentityHistory.setFbiSubscriptionId("subscription1");
		federalRapbackIdentityHistory.setPathToRequestFile("c:/pathToRequestFile");
		federalRapbackIdentityHistory.setRequestSentTimestamp(LocalDateTime.now());
		federalRapbackIdentityHistory.setTransactionCategoryCodeRequest("transcodetext");
		federalRapbackIdentityHistory.setTransactionType("RBIH");
		federalRapbackIdentityHistory.setUcn("UCN1");
		federalRapbackIdentityHistory.setTransactionControlReferenceIdentification("tcri");
		
		Integer federalRapbackIdentityHistoryPk = enhancedAuditDao.saveFederalRapbackIdentityHistory(federalRapbackIdentityHistory);
		assertNotNull(federalRapbackIdentityHistoryPk);
		
		FederalRapbackIdentityHistory frihUpdate = new FederalRapbackIdentityHistory();
		
		frihUpdate.setTransactionControlReferenceIdentification("tcri");
		frihUpdate.setResponseReceivedTimestamp(LocalDateTime.now());
		frihUpdate.setTransactionCategoryCodeResponse("RBIHS");
		frihUpdate.setPathToResponseFile("c:/pathToResponseFile");
		
		enhancedAuditDao.updateFederalRapbackIdentityHistoryWithResponse(frihUpdate);
		
		log.info("update complete");
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
		
		IncidentReportQueryResponse incidentReportQueryResponse = new IncidentReportQueryResponse();
		
		incidentReportQueryResponse.setIncidentNumber("INC234");
		incidentReportQueryResponse.setMessageId("49878");
		incidentReportQueryResponse.setSystemName("System Name");
		incidentReportQueryResponse.setQueryRequestId(queryPk);
		
		enhancedAuditDao.saveIncidentReportQueryResponse(incidentReportQueryResponse);
		
		IncidentReportQueryResponse incidentReportQueryResponseFromDatabase = enhancedAuditDao.retrieveIncidentReportQueryResponse(queryPk);
		
		assertEquals("INC234", incidentReportQueryResponseFromDatabase.getIncidentNumber());
		assertEquals("49878", incidentReportQueryResponseFromDatabase.getMessageId());
		assertEquals("System Name", incidentReportQueryResponseFromDatabase.getSystemName());
				
		
		ProfessionalLicensingQueryResponse professionalLicensingQueryResponse = new ProfessionalLicensingQueryResponse();
		
		professionalLicensingQueryResponse.setLicenseNumber("123");
		professionalLicensingQueryResponse.setLicenseType("pro license");
		professionalLicensingQueryResponse.setQueryRequestId(queryPk);
		professionalLicensingQueryResponse.setIssueDate(LocalDate.now().minusDays(7));
		professionalLicensingQueryResponse.setExpirationDate(LocalDate.now());
		professionalLicensingQueryResponse.setMessageId("123456");
		
		enhancedAuditDao.saveProfessionalLicensingQueryResponse(professionalLicensingQueryResponse);
		
		ProfessionalLicensingQueryResponse professionalLicensingQueryResponseFromDatabase = enhancedAuditDao.retrieveProfessionalLicensingQueryResponse(queryPk);
		
		assertEquals("123", professionalLicensingQueryResponseFromDatabase.getLicenseNumber());
		assertEquals("pro license", professionalLicensingQueryResponseFromDatabase.getLicenseType());
		assertEquals(LocalDate.now().minusDays(7), professionalLicensingQueryResponseFromDatabase.getIssueDate());
		assertEquals(LocalDate.now(), professionalLicensingQueryResponseFromDatabase.getExpirationDate());
		assertEquals("123456", professionalLicensingQueryResponseFromDatabase.getMessageId());
		
		//Wildlife query response
		WildlifeQueryResponse wildlifeQueryResponse = new WildlifeQueryResponse();
		
		wildlifeQueryResponse.setQueryRequestId(queryPk);
		wildlifeQueryResponse.setMessageId("123456");
		wildlifeQueryResponse.setResidenceCity("chicago");
		
		enhancedAuditDao.saveWildlifeQueryResponse(wildlifeQueryResponse);
		
		WildlifeQueryResponse wildlifeQueryResponseResponseFromDatabase = enhancedAuditDao.retrieveWildlifeQueryResponse(queryPk);
		
		assertEquals("123456", wildlifeQueryResponseResponseFromDatabase.getMessageId());
		assertEquals("chicago", wildlifeQueryResponseResponseFromDatabase.getResidenceCity());
		
		//End Wildlife query response
		
		SubscriptionQueryResponse subscriptionQueryResponse = new SubscriptionQueryResponse();
		
		subscriptionQueryResponse.setFbiSubscriptionId("FBI");
		subscriptionQueryResponse.setMessageId("12345");
		subscriptionQueryResponse.setQueryRequestId(queryPk);
		subscriptionQueryResponse.setSubscriptionQualifierId("321");
		subscriptionQueryResponse.setSystemName("Subscriptions");
		
		enhancedAuditDao.saveSubscriptionQueryResponse(subscriptionQueryResponse);
		
		SubscriptionQueryResponse subscriptionQueryResponseFromDatabase = enhancedAuditDao.retrieveSubscriptionQueryResults(queryPk);
		
		assertEquals("FBI", subscriptionQueryResponseFromDatabase.getFbiSubscriptionId());
		assertEquals("12345", subscriptionQueryResponseFromDatabase.getMessageId());
		assertEquals("321", subscriptionQueryResponseFromDatabase.getSubscriptionQualifierId());
		assertEquals("Subscriptions", subscriptionQueryResponseFromDatabase.getSystemName());
		
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
		
		PersonQueryCriminalHistoryResponse personQueryCriminalHistoryResponseFromDatabase = enhancedAuditDao.retrieveCriminalHistoryQueryDetail(queryPk);
		assertEquals("12345", personQueryCriminalHistoryResponseFromDatabase.getFbiId());
		assertEquals("first", personQueryCriminalHistoryResponseFromDatabase.getFirstName());
		assertEquals("last", personQueryCriminalHistoryResponseFromDatabase.getLastName());
		assertEquals("123456", personQueryCriminalHistoryResponseFromDatabase.getMessageId());
		assertEquals("middle", personQueryCriminalHistoryResponseFromDatabase.getMiddleName());
		assertEquals("SID123456", personQueryCriminalHistoryResponseFromDatabase.getSid());
		assertEquals("Criminal History", personQueryCriminalHistoryResponseFromDatabase.getSystemName());
		assertNotNull(personQueryCriminalHistoryResponseFromDatabase.getTimestamp());
		
		IdentificationQueryResponse identificationQueryResponse = new IdentificationQueryResponse();
		
		identificationQueryResponse.setQueryRequestId(queryPk);
		identificationQueryResponse.setFbiId("123");
		identificationQueryResponse.setIdDate(LocalDate.now());
		identificationQueryResponse.setOca("oca");
		identificationQueryResponse.setOri("ori");
		identificationQueryResponse.setOtn("otn");
		identificationQueryResponse.setPersonFirstName("first");
		identificationQueryResponse.setPersonMiddleName("middle");
		identificationQueryResponse.setPersonLastName("last");
		identificationQueryResponse.setSid("A123");
		
		Integer identificationQueryResponsePk = enhancedAuditDao.saveidentificationQueryResponse(identificationQueryResponse);
		
		assertNotNull(identificationQueryResponsePk);
		
		IdentificationQueryResponse identificationQueryResponseFromDatabase = enhancedAuditDao.retrieveIdentificationResultsQueryDetail(queryPk);
		
		assertEquals("123",identificationQueryResponseFromDatabase.getFbiId());
		assertEquals(LocalDate.now(),identificationQueryResponseFromDatabase.getIdDate());
		assertEquals("oca",identificationQueryResponseFromDatabase.getOca());
		assertEquals("ori",identificationQueryResponseFromDatabase.getOri());
		assertEquals("otn",identificationQueryResponseFromDatabase.getOtn());
		assertEquals("first",identificationQueryResponseFromDatabase.getPersonFirstName());
		assertEquals("middle",identificationQueryResponseFromDatabase.getPersonMiddleName());
		assertEquals("last",identificationQueryResponseFromDatabase.getPersonLastName());
		assertEquals("A123",identificationQueryResponseFromDatabase.getSid());
		
		FirearmsQueryResponse firearmsQueryResponse = new FirearmsQueryResponse();
		
		firearmsQueryResponse.setQueryRequestId(queryPk);
		firearmsQueryResponse.setMessageId("123456");
		firearmsQueryResponse.setSystemName("Firearms System");
		firearmsQueryResponse.setCounty("county");
		firearmsQueryResponse.setFirstName("first");
		firearmsQueryResponse.setMiddleName("middle");
		firearmsQueryResponse.setLastName("last");
		firearmsQueryResponse.setRegistrationNumber("reg number");
		
		Integer firearmsQueryResponsePk = enhancedAuditDao.saveFirearmsQueryResponse(firearmsQueryResponse);
		assertNotNull(firearmsQueryResponsePk);
		
		FirearmsQueryResponse firearmsQueryResponseFromDatabase = enhancedAuditDao.retrieveFirearmQueryDetail(queryPk);
		assertEquals("123456", firearmsQueryResponseFromDatabase.getMessageId());
		assertEquals("first", firearmsQueryResponseFromDatabase.getFirstName());
		assertEquals("last", firearmsQueryResponseFromDatabase.getLastName());
		assertEquals("middle", firearmsQueryResponseFromDatabase.getMiddleName());
		assertEquals("Firearms System", firearmsQueryResponseFromDatabase.getSystemName());
		assertEquals("county", firearmsQueryResponseFromDatabase.getCounty());
		assertEquals("reg number", firearmsQueryResponseFromDatabase.getRegistrationNumber());
		assertNotNull(firearmsQueryResponseFromDatabase.getTimestamp());
		
		PersonQueryWarrantResponse personQueryWarrantResponse = new PersonQueryWarrantResponse();
		
		personQueryWarrantResponse.setQueryRequestId(queryPk);
		personQueryWarrantResponse.setFbiId("FBIID");
		personQueryWarrantResponse.setFirstName("first");
		personQueryWarrantResponse.setLastName("last");
		personQueryWarrantResponse.setMessageId("123456");
		personQueryWarrantResponse.setMiddleName("middle");
		personQueryWarrantResponse.setSid("SID");
		personQueryWarrantResponse.setSystemName("Warrants");
		
		enhancedAuditDao.savePersonQueryWarrantResponse(personQueryWarrantResponse);
		
		PersonQueryWarrantResponse personQueryWarrantResponseFromDatabase = enhancedAuditDao.retrieveWarrantQueryDetail(queryPk);
		assertEquals("FBIID", personQueryWarrantResponseFromDatabase.getFbiId());
		assertEquals("first", personQueryWarrantResponseFromDatabase.getFirstName());
		assertEquals("last", personQueryWarrantResponseFromDatabase.getLastName());
		assertEquals("123456", personQueryWarrantResponseFromDatabase.getMessageId());
		assertEquals("middle", personQueryWarrantResponseFromDatabase.getMiddleName());
		assertEquals("SID", personQueryWarrantResponseFromDatabase.getSid());
		assertEquals("Warrants", personQueryWarrantResponseFromDatabase.getSystemName());
		assertNotNull(personQueryWarrantResponseFromDatabase.getTimestamp());
		
		VehicleCrashQueryResponse vehicleCrashQueryResponse = new VehicleCrashQueryResponse();
		
		vehicleCrashQueryResponse.setQueryRequestId(queryPk);
		vehicleCrashQueryResponse.setMessageId("12345");
		vehicleCrashQueryResponse.setSystemName("System Name");
		
		Integer vehicleCrashQueryResponsePk = enhancedAuditDao.saveVehicleQueryCrashResponse(vehicleCrashQueryResponse);
		
		CrashVehicle vehicle1 = new CrashVehicle();
		vehicle1.setVehicleIdentificationNumber("vin1");
		vehicle1.setVehicleMake("make1");
		vehicle1.setVehicleModel("model1");
		vehicle1.setVehicleCrashQueryResultsId(vehicleCrashQueryResponsePk);
		
		CrashVehicle vehicle2 = new CrashVehicle();
		vehicle2.setVehicleIdentificationNumber("vin2");
		vehicle2.setVehicleMake("make2");
		vehicle2.setVehicleModel("model2");		
		vehicle2.setVehicleCrashQueryResultsId(vehicleCrashQueryResponsePk);
		
		enhancedAuditDao.saveCrashVehicle(vehicle1);
		enhancedAuditDao.saveCrashVehicle(vehicle2);
		
		VehicleCrashQueryResponse vehicleCrashQueryResponseFromDatabase = enhancedAuditDao.retrieveVehicleCrashQueryResultsDetail(queryPk);
		assertEquals(2, vehicleCrashQueryResponseFromDatabase.getCrashVehicles().size());
		
		assertEquals("vin1", vehicleCrashQueryResponseFromDatabase.getCrashVehicles().get(0).getVehicleIdentificationNumber());
		assertEquals("vin2", vehicleCrashQueryResponseFromDatabase.getCrashVehicles().get(1).getVehicleIdentificationNumber());

		assertEquals("make1", vehicleCrashQueryResponseFromDatabase.getCrashVehicles().get(0).getVehicleMake());
		assertEquals("make2", vehicleCrashQueryResponseFromDatabase.getCrashVehicles().get(1).getVehicleMake());

		assertEquals("model1", vehicleCrashQueryResponseFromDatabase.getCrashVehicles().get(0).getVehicleModel());
		assertEquals("model2", vehicleCrashQueryResponseFromDatabase.getCrashVehicles().get(1).getVehicleModel());

		
		assertEquals("12345", vehicleCrashQueryResponseFromDatabase.getMessageId());
		assertEquals("System Name", vehicleCrashQueryResponseFromDatabase.getSystemName());
		
		AuditSearchRequest auditSearchRequest = new AuditSearchRequest();
		
		List<QueryRequest> queryRequests = enhancedAuditDao.retrieveQueryRequest(auditSearchRequest);
		
		assertNotNull(queryRequests);
		assertEquals(1, queryRequests.size());
		assertEquals(new Integer(1), queryRequests.get(0).getQueryRequestId());
		assertEquals("123", queryRequests.get(0).getIdentificationId());
		assertEquals("Source", queryRequests.get(0).getIdentificationSourceText());
		assertEquals("123456", queryRequests.get(0).getMessageId());
		assertNotNull(queryRequests.get(0).getTimestamp());
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
		printResults.setSid("sid");

		UserInfo userInfo = getExampleUserInfo();
		
		printResults.setUserInfo(userInfo);
		
		Integer prPk = enhancedAuditDao.savePrintResults(printResults);
		
		assertNotNull(prPk);
		
		PrintResults printResultsResponse = enhancedAuditDao.retrievePrintResultsfromMessageID("123456");
		
		assertEquals("description", printResultsResponse.getDescription());
		assertEquals("123456", printResultsResponse.getMessageId());
		assertEquals("system name", printResultsResponse.getSystemName());
		assertEquals("sid", printResultsResponse.getSid());
	
		assertEquals("Employer Name", printResultsResponse.getUserInfo().getEmployerName());
		assertEquals("Sub Unit", printResultsResponse.getUserInfo().getEmployerSubunitName());
		assertEquals("Fed ID", printResultsResponse.getUserInfo().getFederationId());
		assertEquals("IDP", printResultsResponse.getUserInfo().getIdentityProviderId());
		assertEquals("email", printResultsResponse.getUserInfo().getUserEmailAddress());
		assertEquals("first", printResultsResponse.getUserInfo().getUserFirstName());
		assertEquals("last", printResultsResponse.getUserInfo().getUserLastName());
		assertEquals("employer ori", printResultsResponse.getUserInfo().getEmployerOri());		
		
		//Look up user info here
		List<UserInfo> userInfoEntries = enhancedAuditDao.retrieveUserInfoFromFederationId(userInfo.getFederationId());
		
		Integer userInfoPk = null;
		
		if (userInfoEntries != null && userInfoEntries.size() > 0)
		{
			userInfoPk = userInfoEntries.get(0).getUserInfoId();
		}
		
		List<PrintResults> printResultsByUserId = enhancedAuditDao.retrieveUserPrintRequests(userInfoPk);
		
		assertEquals(1, printResultsByUserId.size());
		
	}	

	@Test
	public void testSaveUserLogin() throws Exception
	{
		Integer userInfoPk = saveUserInfo();

		Integer userLoginPk = enhancedAuditDao.saveUserAuthentication(userInfoPk, "login");
		
		assertNotNull(userLoginPk);
		
		UserAuthenticationSearchRequest userAuthenticationSearchRequest = new UserAuthenticationSearchRequest();
		
		userAuthenticationSearchRequest.setStartTime(LocalDateTime.now().minusDays(1));
		userAuthenticationSearchRequest.setEndTime(LocalDateTime.now().plusHours(1));
		
		List<UserAuthenticationSearchResponse> userAuthenticationSearchResponses = enhancedAuditDao.retrieveUserAuthentication(userAuthenticationSearchRequest);
		verifyUserAuthenticationResponse(userAuthenticationSearchResponses);

		userAuthenticationSearchRequest = new UserAuthenticationSearchRequest();
		userAuthenticationSearchRequest.setFirstName("first");
		userAuthenticationSearchRequest.setLastName("last");
		userAuthenticationSearchRequest.setEmailAddress("email");
		userAuthenticationSearchRequest.setEmployerOri("employer ori");
		userAuthenticationSearchRequest.setFirstName("first");
		userAuthenticationSearchRequest.setUserAction("login");
		
		userAuthenticationSearchResponses = enhancedAuditDao.retrieveUserAuthentication(userAuthenticationSearchRequest);
		verifyUserAuthenticationResponse(userAuthenticationSearchResponses);
		
		userAuthenticationSearchRequest.setUserAction("logout");
		userAuthenticationSearchResponses = enhancedAuditDao.retrieveUserAuthentication(userAuthenticationSearchRequest);
		assertEquals(0, userAuthenticationSearchResponses.size());
	}

	private void verifyUserAuthenticationResponse(
			List<UserAuthenticationSearchResponse> userAuthenticationSearchResponses) {
		assertEquals(1, userAuthenticationSearchResponses.size());
		assertEquals("Employer Name", userAuthenticationSearchResponses.get(0).getEmployerName());
		assertEquals("employer ori", userAuthenticationSearchResponses.get(0).getEmployerOri());
		assertEquals("Sub Unit", userAuthenticationSearchResponses.get(0).getEmployerSubunitName());
		assertEquals("Fed ID", userAuthenticationSearchResponses.get(0).getFederationId());
		assertEquals("IDP", userAuthenticationSearchResponses.get(0).getIdentityProviderId());
		assertEquals("login", userAuthenticationSearchResponses.get(0).getUserAction());
		assertEquals("email", userAuthenticationSearchResponses.get(0).getUserEmailAddress());
		assertEquals("first", userAuthenticationSearchResponses.get(0).getUserFirstName());
		assertEquals("last", userAuthenticationSearchResponses.get(0).getUserLastName());
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
		assertEquals("employer ori", userAcknowledgementFromDatabase.getUserInfo().getEmployerOri());
		
	}
	
	@Test
	public void testVehicleSearchMethods() throws Exception
	{
		Integer userInfoPk = saveUserInfo();
		assertNotNull(userInfoPk);
		log.info("User info pk: " + userInfoPk);
		
		VehicleSearchRequest vsr = new VehicleSearchRequest();
		
		vsr.setMessageId("123");
		vsr.setOnBehalfOf("behalf");
		vsr.setPurpose("purpose");
		vsr.setUserInfofk(userInfoPk);
		vsr.setVehicleColor("color");
		vsr.setVehicleIdentificationNumber("vin");
		vsr.setVehicleLicensePlate("plate");
		vsr.setVehicleMake("make");
		vsr.setVehicleModel("model");
		vsr.setVehicleYearRangeEnd("2009");
		vsr.setVehicleYearRangeEnd("2018");
		
		List<String> systemsToSearch=new ArrayList<String>();
		
		systemsToSearch.add("system1");
		systemsToSearch.add("system2");
		
		vsr.setSourceSystemsList(systemsToSearch);
		
		Integer vehiclePk = enhancedAuditDao.saveVehicleSearchRequest(vsr);
		
		assertNotNull(vehiclePk);
		
	}

	@Test
	public void testIncidentSearchMethods() throws Exception
	{
		Integer userInfoPk = saveUserInfo();
		assertNotNull(userInfoPk);
		log.info("User info pk: " + userInfoPk);
		
		IncidentSearchRequest isr = new IncidentSearchRequest();
		
		isr.setMessageId("123");
		isr.setOnBehalfOf("behalf");
		isr.setPurpose("purpose");
		isr.setUserInfofk(userInfoPk);
		isr.setCategoryType("law");
		isr.setCityTown("city");
		isr.setEndDate(LocalDate.now());
		isr.setStartDate(LocalDate.now().minusDays(10));
		
		List<String> systemsToSearch=new ArrayList<String>();
		
		systemsToSearch.add("system1");
		systemsToSearch.add("system2");
		
		isr.setSystemsToSearch(systemsToSearch);
		
		Integer incidentPk = enhancedAuditDao.saveIncidentSearchRequest(isr);
		
		assertNotNull(incidentPk);
		
	}

	@Test
	public void testSubscriptionActionMethods() throws Exception
	{
		Integer userInfoPk = saveUserInfo();
		assertNotNull(userInfoPk);
		log.info("User info pk: " + userInfoPk);
		
		SubscriptionAction subscriptionAction = new SubscriptionAction();

		subscriptionAction.setFbiSubscriptionId("55");
		subscriptionAction.setAction(SubscriptionAction.VALIDATION_ACTION);
		subscriptionAction.setUserInfoFK(userInfoPk);
		subscriptionAction.setValidationDueDate(LocalDate.now());
		subscriptionAction.setStartDate(LocalDate.now().minusYears(2));
		subscriptionAction.setEndDate(LocalDate.now().plusYears(2));
		subscriptionAction.setValidationDueDate(LocalDate.now());
		
		Integer subscriptionActionPk = enhancedAuditDao.saveSubscriptionAction(subscriptionAction);
		assertNotNull(subscriptionActionPk);
		
		subscriptionAction = new SubscriptionAction();
		
		subscriptionAction.setSuccessIndicator(true);
		subscriptionAction.setStateSubscriptionId("44");
		subscriptionAction.setSubscriptionActionId(subscriptionActionPk);
		
		enhancedAuditDao.updateSubscriptionActionWithResponse(subscriptionAction);
		
		log.info("Subscription action test complete.");
	}
	
	@Test
	public void testRetrieveNotifications() throws Exception
	{
		LocalDate startDate = LocalDate.now().minusYears(100);
		LocalDate endDate = LocalDate.now();
		
		List<NotificationSent> notificationsSent = enhancedAuditDao.retrieveNotifications(startDate, endDate);
		
		log.info(notificationsSent);
		assertEquals(3, notificationsSent.size());
		
		NotificationSent notificationSent = notificationsSent.get(0);
		
		assertEquals(new Integer(3), notificationSent.getNotificationSentId());
		assertEquals("{http://ojbc.org/wsn/topics}:person/rapback", notificationSent.getTopic());
		assertEquals("62725", notificationSent.getSubscriptionIdentifier());
		assertEquals("test3@email.com", notificationSent.getSubscriptionOwnerEmailAddress());
		assertEquals("STATE:IDP:AGENCY:USER:test3email.com", notificationSent.getSubscriptionOwner());
		assertEquals("http://www.hawaii.gov/arrestNotificationProducer", notificationSent.getNotifyingSystemName());
		assertEquals("{http://ojbc.org/OJB_Portal/Subscriptions/1.0}OJB", notificationSent.getSubscribingSystemIdentifier());
		assertEquals("2018-10-22T11:14:46", notificationSent.getNotificationSentTimestamp().toString());
		assertEquals("Bill Padmanabhan", notificationSent.getSubscriptionSubject());
		assertEquals("HI123456", notificationSent.getSubscriptionOwnerAgency());
		
		NotificationSent notificationSentWithTriggeringEvent = notificationsSent.get(1);
		
		assertNotNull(notificationSentWithTriggeringEvent.getTriggeringEvents());
		
	}
	
}

