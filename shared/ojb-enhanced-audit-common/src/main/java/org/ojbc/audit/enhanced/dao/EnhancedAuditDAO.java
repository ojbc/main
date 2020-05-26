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

import java.time.LocalDate;
import java.util.List;

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
import org.ojbc.audit.enhanced.dao.model.IdentificationSearchResult;
import org.ojbc.audit.enhanced.dao.model.IncidentSearchRequest;
import org.ojbc.audit.enhanced.dao.model.NotificationSent;
import org.ojbc.audit.enhanced.dao.model.PersonQueryCriminalHistoryResponse;
import org.ojbc.audit.enhanced.dao.model.PersonQueryWarrantResponse;
import org.ojbc.audit.enhanced.dao.model.PersonSearchRequest;
import org.ojbc.audit.enhanced.dao.model.PersonSearchResult;
import org.ojbc.audit.enhanced.dao.model.PrintResults;
import org.ojbc.audit.enhanced.dao.model.QueryRequest;
import org.ojbc.audit.enhanced.dao.model.SubscriptionAction;
import org.ojbc.audit.enhanced.dao.model.SubscriptionQueryResponse;
import org.ojbc.audit.enhanced.dao.model.SubscriptionSearchResult;
import org.ojbc.audit.enhanced.dao.model.TriggeringEvents;
import org.ojbc.audit.enhanced.dao.model.UserAcknowledgement;
import org.ojbc.audit.enhanced.dao.model.UserInfo;
import org.ojbc.audit.enhanced.dao.model.VehicleCrashQueryResponse;
import org.ojbc.audit.enhanced.dao.model.VehicleSearchRequest;
import org.ojbc.audit.enhanced.dao.model.auditsearch.AuditSearchRequest;
import org.ojbc.audit.enhanced.dao.model.auditsearch.UserAuthenticationSearchRequest;
import org.ojbc.audit.enhanced.dao.model.auditsearch.UserAuthenticationSearchResponse;
import org.ojbc.util.sn.SubscriptionSearchRequest;


public interface EnhancedAuditDAO {

	public Integer saveFederalRapbackIdentityHistory(FederalRapbackIdentityHistory federalRapbackIdentityHistory);
	
	public void updateFederalRapbackIdentityHistoryWithResponse(FederalRapbackIdentityHistory federalRapbackIdentityHistory) throws Exception;
	
	public Integer saveFederalRapbackRenewalNotification(FederalRapbackRenewalNotification federalRapbackRenewalNotification);
	
	public Integer saveFederalRapbackSubscription(FederalRapbackSubscription federalRapbackSubscription);
	
	public void updateFederalRapbackSubscriptionWithResponse(FederalRapbackSubscription federalRapbackSubscription) throws Exception;
	
	public FederalRapbackSubscription retrieveFederalRapbackSubscriptionFromTCN(String transactionControlNumber);
	
	public List<FederalRapbackSubscription> retrieveFederalRapbackSubscriptionErrors();
	
	public List<FederalRapbackSubscription> retrieveFederalRapbackSubscriptionFromStateSubscriptionId(String stateSubscriptionId);
	
	public List<FederalRapbackNotification> retrieveFederalNotifications(LocalDate startDate, LocalDate endDate);
	
	public List<NotificationSent> retrieveNotifications(LocalDate startDate, LocalDate endDate);
	
	public List<FederalRapbackRenewalNotification> retrieveFederalRapbackRenewalNotifications(LocalDate startDate, LocalDate endDate);
	
	public List<FederalRapbackNotification> retrieveFederalNotificationsBySubscriptionId(String subscriptionId);
	
	public Integer saveFederalRapbackNotification(FederalRapbackNotification federalRapbackNotification);
	
	public Integer savePersonSearchRequest(PersonSearchRequest personSearchRequest);
	
	public Integer saveSubscriptionSearchRequest(SubscriptionSearchRequest subscriptionSearchRequest);
	
	public Integer saveSubscriptionSearchResult(SubscriptionSearchResult subscriptionSearchResult);
	
	public Integer saveSubscriptionQueryResponse(SubscriptionQueryResponse subscriptionQueryResponse);
	
	public Integer saveFirearmsSearchRequest(FirearmsSearchRequest firearmsSearchRequest);
	
	public Integer saveVehicleSearchRequest(VehicleSearchRequest vehicleSearchRequest);
	
	public Integer saveIncidentSearchRequest(IncidentSearchRequest incidentSearchRequest);
	
	public Integer saveIdentificationSearchRequest(IdentificationSearchRequest identificationSearchRequest);
	
	public Integer saveidentificationSearchResponse(IdentificationSearchResult identificationSearchResult);
	
	public Integer saveidentificationQueryResponse(IdentificationQueryResponse identificationQueryResponse);
	
	public Integer saveQueryRequest(QueryRequest queryRequest);
	
	public Integer savePersonQueryCriminalHistoryResponse(PersonQueryCriminalHistoryResponse personQueryCriminalHistoryResponse);
	
	public Integer saveVehicleQueryCrashResponse(VehicleCrashQueryResponse vehicleCrashQueryResponse);
	
	public Integer saveCrashVehicle(CrashVehicle crashVehicle);
	
	public Integer saveFirearmsQueryResponse(FirearmsQueryResponse firearmsQueryResponse);
	
	public Integer savePersonQueryWarrantResponse(PersonQueryWarrantResponse personQueryWarrantResponse);
	
	public Integer savePersonSearchResult(PersonSearchResult personSearchResult);
	
	public Integer saveFirearmSearchResult(FirearmSearchResult firearmSearchResult);
	
	public Integer saveUserInfo(UserInfo userInfo);
	
	public Integer saveuserAcknowledgement(UserAcknowledgement userAcknowledgement);
	
	public List<UserAcknowledgement> retrieveUserAcknowledgement(String federationId);
	
	public Integer saveUserAuthentication(Integer userInfoPk, String action);

	public UserInfo retrieveUserInfoFromId(Integer userInfoPk);
	
	public List<UserInfo> retrieveUserInfoFromFederationId(String federationId);
	
	public List<UserInfo> retrieveAllUsers();
	
	public List<UserAuthenticationSearchResponse> retrieveUserAuthentication(UserAuthenticationSearchRequest userAuthenticationSearchRequest);

	public Integer savePrintResults(PrintResults printResults);
	
	public Integer retrieveSystemToSearchIDFromURI(String uri);
	
	public Integer retrieveSystemToSearchIDFromSystemName(String systemName);
	
	public Integer retrieveSearchQualifierCodeIDfromCodeName(String codeName);
	
	public Integer savePersonSystemToSearch(Integer pearchSearchPk, Integer systemsToSearchPk);
	
	public Integer saveSubscriptionCategoryCodes(Integer subscriptionSearchRequestPk, Integer subscriptionReasonCodePk);
	
	public Integer saveFirearmsSystemToSearch(Integer pearchSearchPk, Integer systemsToSearchPk);
	
	public Integer saveIncidentSystemToSearch(Integer incidentSearchPk, Integer systemsToSearchPk);
	
	public Integer saveVehicleSystemToSearch(Integer vehicleSearchPk, Integer systemsToSearchPk);

	public Integer retrieveIdentificationReasonCodeFromDescription(String description);
	
	public Integer saveIdentificationReasonCode(Integer identificationSearchReasonCodeId, Integer identificationSearchRequestId);

	public Integer retrievePersonSearchIDfromMessageID(String messageId);
	
	public Integer retrieveFirearmSearchIDfromMessageID(String messageId);
	
	public Integer retrieveSubscriptionSearchIDfromMessageID(String messageId);
	
	public Integer retrievePersonQueryIDfromMessageID(String messageId);
	
	public Integer retrieveOrganizationIdentificationIDfromMessageID(String messageId);
	
	public Integer retrieveFederalRapbackSubscriptionError(String stateSubscriptionId);
	
	public Integer deleteFederalRapbackSubscriptionError(String stateSubscriptionId);
	
	public Integer saveFederalRapbackSubscriptionError(Integer federalSubcriptionId, String stateSubscriptionId);
	
	public Integer resolveFederalRapbackSubscriptionError(String stateSubscriptionId);
	
	public PrintResults retrievePrintResultsfromMessageID(String messageId);
	
	public List<TriggeringEvents> retrieveAllTriggeringEvents();
	
	public Integer saveTriggeringEvent(Integer federalRapbackNotificationId, Integer triggeringEventId);
	
	public List<String> retrieveTriggeringEventsForNotification(Integer federalRapbackNotificationId);
	
	public Integer saveSubscriptionAction(SubscriptionAction subscriptionAction);
	
	public void updateSubscriptionActionWithResponse(SubscriptionAction subscriptionAction);
	
	public Integer retrieveSubReasonCodeIdFromCode(String subscriptionReasonCode);
	
	public List<PersonSearchRequest> retrievePersonSearchRequest (AuditSearchRequest personAuditSearchRequest);
	
	public List<FirearmsSearchRequest> retrieveFirearmSearchRequest (AuditSearchRequest firearmAuditSearchRequest);
	
	public List<VehicleSearchRequest> retrieveVehicleSearchRequest (AuditSearchRequest vehicleSearchRequest);
	
	public List<IncidentSearchRequest> retrieveIncidentSearchRequest (AuditSearchRequest incidentAuditSearchRequest);
	
	public List<QueryRequest> retrieveQueryRequest (AuditSearchRequest incidentAuditSearchRequest);
	
	public PersonQueryCriminalHistoryResponse retrieveCriminalHistoryQueryDetail(Integer queryRequestId);
	
	public FirearmsQueryResponse retrieveFirearmQueryDetail(Integer queryRequestId);
	
	public PersonQueryWarrantResponse retrieveWarrantQueryDetail(Integer queryRequestId);
	
	public VehicleCrashQueryResponse retrieveVehicleCrashQueryResultsDetail(Integer queryRequestId);
}
