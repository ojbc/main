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
package org.ojbc.bundles.utilities.auditing.rest;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.ojbc.audit.enhanced.dao.EnhancedAuditDAO;
import org.ojbc.audit.enhanced.dao.model.CannabisLicensingQueryResponse;
import org.ojbc.audit.enhanced.dao.model.FederalRapbackNotification;
import org.ojbc.audit.enhanced.dao.model.FederalRapbackSubscription;
import org.ojbc.audit.enhanced.dao.model.FederalRapbackSubscriptionDetail;
import org.ojbc.audit.enhanced.dao.model.FirearmSearchResult;
import org.ojbc.audit.enhanced.dao.model.FirearmsQueryResponse;
import org.ojbc.audit.enhanced.dao.model.FirearmsSearchRequest;
import org.ojbc.audit.enhanced.dao.model.IdentificationQueryResponse;
import org.ojbc.audit.enhanced.dao.model.IncidentReportQueryResponse;
import org.ojbc.audit.enhanced.dao.model.IncidentSearchRequest;
import org.ojbc.audit.enhanced.dao.model.IncidentSearchResult;
import org.ojbc.audit.enhanced.dao.model.NotificationSent;
import org.ojbc.audit.enhanced.dao.model.PersonQueryCriminalHistoryResponse;
import org.ojbc.audit.enhanced.dao.model.PersonQueryWarrantResponse;
import org.ojbc.audit.enhanced.dao.model.PersonSearchRequest;
import org.ojbc.audit.enhanced.dao.model.PersonSearchResult;
import org.ojbc.audit.enhanced.dao.model.PrintResults;
import org.ojbc.audit.enhanced.dao.model.ProfessionalLicensingQueryResponse;
import org.ojbc.audit.enhanced.dao.model.QueryRequest;
import org.ojbc.audit.enhanced.dao.model.QueryRequestByDateRange;
import org.ojbc.audit.enhanced.dao.model.SubscriptionQueryResponse;
import org.ojbc.audit.enhanced.dao.model.UserAcknowledgement;
import org.ojbc.audit.enhanced.dao.model.UserInfo;
import org.ojbc.audit.enhanced.dao.model.VehicleCrashQueryResponse;
import org.ojbc.audit.enhanced.dao.model.VehicleSearchRequest;
import org.ojbc.audit.enhanced.dao.model.VehicleSearchResult;
import org.ojbc.audit.enhanced.dao.model.WildlifeQueryResponse;
import org.ojbc.audit.enhanced.dao.model.auditsearch.AuditPersonSearchRequest;
import org.ojbc.audit.enhanced.dao.model.auditsearch.AuditSearchRequest;
import org.ojbc.audit.enhanced.dao.model.auditsearch.UserAuthenticationSearchRequest;
import org.ojbc.audit.enhanced.dao.model.auditsearch.UserAuthenticationSearchResponse;
import org.ojbc.intermediaries.sn.dao.SubscriptionSearchQueryDAO;
import org.ojbc.util.model.rapback.AgencyProfile;
import org.ojbc.util.model.rapback.ExpiringSubscriptionRequest;
import org.ojbc.util.model.rapback.Subscription;
import org.springframework.stereotype.Component;

import jakarta.annotation.Resource;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.Response.Status;

@Path("/audit")
@Component
public class AuditRestImpl implements AuditInterface {

	private static final Log log = LogFactory.getLog(AuditRestImpl.class);
	
	@Resource
	private EnhancedAuditDAO enhancedAuditDao;
	
	@Resource
	private SubscriptionSearchQueryDAO subscriptionSearchQueryDAO;
	
	@Resource
	Map<String, String> notificationSystemToTriggeringEvent;
	
	private static final String LOGIN_ACTION="login";
	
    private static final String LOGOUT_ACTION = "logout";

    @POST
    @Path("/printResults")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)

    @Override
    public Response auditPrintResults(PrintResults printResults) {

        log.info("Print Results: " + printResults.toString());

        enhancedAuditDao.savePrintResults(printResults);

        return Response.status(Status.OK).entity(printResults).build();
    }

    @POST
    @Path("/userLogin")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    @Override
    public Response auditUserLogin(UserInfo userInfo) {
        log.info("Auditing user login info: " + userInfo.toString());

        Integer userInfoPk = null;

        // Look up user info here
        List<UserInfo> userInfoEntries = enhancedAuditDao.retrieveUserInfoFromFederationId(userInfo.getFederationId());

        if (userInfoEntries != null && userInfoEntries.size() > 0) {
            userInfoPk = userInfoEntries.get(0).getUserInfoId();
        } else {
            userInfoPk = enhancedAuditDao.saveUserInfo(userInfo);
        }

        enhancedAuditDao.saveUserAuthentication(userInfoPk, LOGIN_ACTION);

        return Response.status(Status.OK).entity(userInfo).build();
    }

    @POST
    @Path("/userLogout")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    @Override
    public Response auditUserLogout(UserInfo userInfo) {
        log.info("Auditing user logout info: " + userInfo.toString());

        Integer userInfoPk = null;

        // Look up user info here
        List<UserInfo> userInfoEntries = enhancedAuditDao.retrieveUserInfoFromFederationId(userInfo.getFederationId());

        if (userInfoEntries != null && userInfoEntries.size() > 0) {
            userInfoPk = userInfoEntries.get(0).getUserInfoId();
        } else {
            userInfoPk = enhancedAuditDao.saveUserInfo(userInfo);
        }

        enhancedAuditDao.saveUserAuthentication(userInfoPk, LOGOUT_ACTION);

        return Response.status(Status.OK).entity(userInfo).build();
    }

    @POST
    @Path("/userAcknowledgement")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    @Override
    public Response auditUserAcknowledgement(UserAcknowledgement userAcknowledgement) {
        log.info("Audit user acknowledgement: " + userAcknowledgement.toString());

        Integer userInfoPk = null;

        // Look up user info here
        List<UserInfo> userInfoEntries = enhancedAuditDao
                .retrieveUserInfoFromFederationId(userAcknowledgement.getUserInfo().getFederationId());

        if (userInfoEntries != null && userInfoEntries.size() > 0) {
            userInfoPk = userInfoEntries.get(0).getUserInfoId();
        } else {
            userInfoPk = enhancedAuditDao.saveUserInfo(userAcknowledgement.getUserInfo());
        }

        userAcknowledgement.getUserInfo().setUserInfoId(userInfoPk);

        enhancedAuditDao.saveuserAcknowledgement(userAcknowledgement);

        return Response.status(Status.OK).entity(userAcknowledgement).build();

    }

    @GET
    @Path("/searchForFederalRapbackSubscriptionsByStateSubscriptionId/{subscriptionId}")
    @Produces(MediaType.APPLICATION_JSON)
    @Override
    public List<FederalRapbackSubscription> searchForFederalRapbackSubscriptions(
            @PathParam("subscriptionId") String subscriptionId) {
        log.info("Subscription ID: " + subscriptionId);

        List<FederalRapbackSubscription> federalRapbackSubscriptions = enhancedAuditDao
                .retrieveFederalRapbackSubscriptionFromStateSubscriptionId(subscriptionId);

        return federalRapbackSubscriptions;
    }

    @POST
    @Path("/retrieveExpiringSubscriptions")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    @Override
    public List<Subscription> retrieveExpiringSubscriptions(ExpiringSubscriptionRequest request) {

        log.info("Days until expiration: " + request.getDaysUntilExpiry());
        log.info("ORIs: " + request.getOris());

        List<Subscription> subscriptions = subscriptionSearchQueryDAO.searchForExpiringAndInvalidSubscriptions(
                request.getOris(), request.getDaysUntilExpiry(), request.getSystemName());

        return subscriptions;
    }

    @POST
    @Path("/retrieveExpiredSubscriptions")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    @Override
    public List<Subscription> retrieveExpiredSubscriptions(ExpiringSubscriptionRequest request) {
        log.info("Days until expiration: " + request.getDaysUntilExpiry());
        log.info("ORIs: " + request.getOris());

        List<Subscription> subscriptions = subscriptionSearchQueryDAO.searchForExpiredAndInvalidSubscriptions(
                request.getOris(), request.getDaysUntilExpiry(), request.getSystemName());

        return subscriptions;
    }

    @GET
    @Path("/agencies")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    @Override
    public List<AgencyProfile> retrieveAllAgencies() {

        List<AgencyProfile> agencyProfiles = subscriptionSearchQueryDAO.returnAllAgencies();

        return agencyProfiles;
    }

    @POST
    @Path("/retrieveRapbackNotifications")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    @Override
    public List<FederalRapbackNotification> retrieveRapbackNotifications(
            QueryRequestByDateRange queryRequestByDateRange) {
        List<FederalRapbackNotification> federalRapbackNotifications = enhancedAuditDao.retrieveFederalNotifications(
                queryRequestByDateRange.getStartDate(), queryRequestByDateRange.getEndDate());
        return federalRapbackNotifications;
    }

    @POST
    @Path("/retrieveNotificationsSent")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    @Override
    public List<NotificationSent> retrieveNotificationsSent(QueryRequestByDateRange queryRequestByDateRange) {
        List<NotificationSent> notificationSents = enhancedAuditDao
                .retrieveNotifications(queryRequestByDateRange.getStartDate(), queryRequestByDateRange.getEndDate());

        for (NotificationSent notificationSent : notificationSents) {
            if (StringUtils.isNotBlank(notificationSent.getNotifyingSystemName())) {
                if (notificationSystemToTriggeringEvent.containsKey(notificationSent.getNotifyingSystemName())) {
                    ArrayList<String> triggeringEvents = new ArrayList<String>();
                    triggeringEvents
                            .add(notificationSystemToTriggeringEvent.get(notificationSent.getNotifyingSystemName()));

                    notificationSent.setTriggeringEvents(triggeringEvents);
                }
            }
        }

        return notificationSents;
    }

    @GET
    @Path("/federalRapbackNotifications/{subscriptionId}")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    @Override
    public List<FederalRapbackNotification> searchForFederalRapbackNotifications(String subscriptionId) {
        List<FederalRapbackNotification> federalRapbackNotifications = enhancedAuditDao
                .retrieveFederalNotificationsBySubscriptionId(subscriptionId);
        return federalRapbackNotifications;
    }

    @GET
    @Path("/retrieveFederalRapbackSubscriptionErrors")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    @Override
    public List<FederalRapbackSubscription> retrieveFederalRapbackSubscriptionErrors() {
        log.info("Retrieve Federal Subscription Errors.");

        List<FederalRapbackSubscription> federalRapbackSubscriptions = enhancedAuditDao
                .retrieveFederalRapbackSubscriptionErrors();

        return federalRapbackSubscriptions;
    }

    @GET
    @Path("/subscriptions/{subscriptionId}/federalRapbackSubscriptionsDetail")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    @Override
    public FederalRapbackSubscriptionDetail returnFederalRapbackSubscriptionDetail(String subscriptionId) {

        FederalRapbackSubscriptionDetail federalRapbackSubscriptionDetail = new FederalRapbackSubscriptionDetail();

        // Return all federal subscription info for the state subscription
        List<FederalRapbackSubscription> federalRapbackSubscriptions = enhancedAuditDao
                .retrieveFederalRapbackSubscriptionFromStateSubscriptionId(subscriptionId);

        federalRapbackSubscriptionDetail.setFederalRapbackSubscriptions(federalRapbackSubscriptions);

        boolean latestMaintenanceRequestFound = false;

        // Results come back sorted in descending order by timestamp
        for (FederalRapbackSubscription federalRapbackSubscription : federalRapbackSubscriptions) {
            if ("RBSCVL".equals(federalRapbackSubscription.getTransactionCategoryCodeRequest())) {
                federalRapbackSubscriptionDetail.setFbiSubscriptionSent(true);

                if (StringUtils.isNotBlank(federalRapbackSubscription.getTransactionCategoryCodeResponse())
                        && federalRapbackSubscription.getTransactionCategoryCodeResponse().equals("RBSR")
                        && StringUtils.isNotBlank(federalRapbackSubscription.getFbiSubscriptionId())) {
                    federalRapbackSubscriptionDetail.setFbiSubscriptionCreated(true);
                }

                if (StringUtils.isNotBlank(federalRapbackSubscription.getTransactionStatusText())) {
                    federalRapbackSubscriptionDetail
                            .setFbiSubscriptionErrorText(federalRapbackSubscription.getTransactionStatusText());
                }
            }

            if ("RBSCRM".equals(federalRapbackSubscription.getTransactionCategoryCodeRequest())) {
                federalRapbackSubscriptionDetail.setFbiSubscriptionSent(true);

                if (StringUtils.isNotBlank(federalRapbackSubscription.getTransactionCategoryCodeResponse())
                        && federalRapbackSubscription.getTransactionCategoryCodeResponse().equals("RBSR")
                        && StringUtils.isNotBlank(federalRapbackSubscription.getFbiSubscriptionId())) {
                    federalRapbackSubscriptionDetail.setFbiSubscriptionCreated(true);
                }

                if (StringUtils.isNotBlank(federalRapbackSubscription.getTransactionStatusText())) {
                    federalRapbackSubscriptionDetail
                            .setFbiSubscriptionErrorText(federalRapbackSubscription.getTransactionStatusText());
                }
            }

            if (!latestMaintenanceRequestFound) {
                if ("RBMNT".equals(federalRapbackSubscription.getTransactionCategoryCodeRequest())) {
                    federalRapbackSubscriptionDetail.setFbiRapbackMaintenanceSent(true);

                    if (StringUtils.isNotBlank(federalRapbackSubscription.getTransactionCategoryCodeResponse())
                            && federalRapbackSubscription.getTransactionCategoryCodeResponse().equals("RBMNTR")
                            && StringUtils.isNotBlank(federalRapbackSubscription.getFbiSubscriptionId())) {
                        federalRapbackSubscriptionDetail.setFbiRapbackMaintenanceConfirmed(true);
                    }

                    if (StringUtils.isNotBlank(federalRapbackSubscription.getTransactionStatusText())) {
                        federalRapbackSubscriptionDetail.setFbiRapbackMaintenanceErrorText(
                                federalRapbackSubscription.getTransactionStatusText());
                    }

                    latestMaintenanceRequestFound = true;
                }

            }
        }

        return federalRapbackSubscriptionDetail;
    }

    @POST
    @Path("/retrieveUserAuthentications")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    @Override
    public List<UserAuthenticationSearchResponse> retrieveUserAuthentications(
            UserAuthenticationSearchRequest authenticationSearchRequest) {

        log.info("User Authentication Search Request: " + authenticationSearchRequest.toString());

        List<UserAuthenticationSearchResponse> userAuthenticationSearchResponses = enhancedAuditDao
                .retrieveUserAuthentication(authenticationSearchRequest);

        return userAuthenticationSearchResponses;
    }

    @POST
    @Path("/retrievePersonSearchRequest")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    @Override
    public List<PersonSearchRequest> retrievePersonSearchRequest(AuditSearchRequest auditPersonSearchRequest) {

        log.info("Person Audit Search Request: " + auditPersonSearchRequest.toString());

        List<PersonSearchRequest> personSearchRequests = enhancedAuditDao
                .retrievePersonSearchRequest(auditPersonSearchRequest);

        return personSearchRequests;

    }

    @POST
    @Path("/retrievePersonSearchRequestByPerson")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    @Override
    public List<PersonSearchRequest> retrievePersonSearchRequestByPerson(
            AuditPersonSearchRequest auditPersonSearchRequest) {
        log.info("Person Audit Search Request: " + auditPersonSearchRequest.toString());

        List<PersonSearchRequest> personSearchRequests = enhancedAuditDao
                .retrievePersonSearchRequestByPerson(auditPersonSearchRequest);

        return personSearchRequests;
    }

    @POST
    @Path("/retrieveFirearmSearchRequest")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    @Override
    public List<FirearmsSearchRequest> retrieveFirearmSearchRequest(AuditSearchRequest auditSearchRequest) {

        log.info("Firearm Audit Search Request: " + auditSearchRequest.toString());

        List<FirearmsSearchRequest> firearmsSearchRequests = enhancedAuditDao
                .retrieveFirearmSearchRequest(auditSearchRequest);

        return firearmsSearchRequests;
    }

    @POST
    @Path("/retrieveVehicleSearchRequest")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    @Override
    public List<VehicleSearchRequest> retrieveVehicleSearchRequest(AuditSearchRequest auditSearchRequest) {

        log.info("Vehicle Audit Search Request: " + auditSearchRequest.toString());

        List<VehicleSearchRequest> vehicleSearchRequests = enhancedAuditDao
                .retrieveVehicleSearchRequest(auditSearchRequest);

        return vehicleSearchRequests;

    }

    @POST
    @Path("/retrieveIncidentSearchRequest")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    @Override
    public List<IncidentSearchRequest> retrieveIncidentSearchRequest(AuditSearchRequest auditSearchRequest) {

        log.info("Incident Audit Search Request: " + auditSearchRequest.toString());

        List<IncidentSearchRequest> incidentSearchRequests = enhancedAuditDao
                .retrieveIncidentSearchRequest(auditSearchRequest);

        return incidentSearchRequests;

    }

    @POST
    @Path("/retrieveQueryRequest")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    @Override
    public List<QueryRequest> retrieveQueryRequest(AuditSearchRequest auditSearchRequest) {
        log.info("Query Audit Search Request: " + auditSearchRequest.toString());

        List<QueryRequest> queryRequests = enhancedAuditDao.retrieveQueryRequest(auditSearchRequest);

        return queryRequests;
    }

    @POST
    @Path("/retrieveCriminalHistoryQueryDetail")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    @Override
    public PersonQueryCriminalHistoryResponse retrieveCriminalHistoryQueryDetail(Integer queryRequestId) {
        log.info("Retrieve Criminal History Detail for: " + queryRequestId.toString());

        PersonQueryCriminalHistoryResponse personQueryCriminalHistoryResponse = enhancedAuditDao
                .retrieveCriminalHistoryQueryDetail(queryRequestId);

        return personQueryCriminalHistoryResponse;
    }

    @POST
    @Path("/retrieveFirearmQueryDetail")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    @Override
    public FirearmsQueryResponse retrieveFirearmQueryDetail(Integer queryRequestId) {
        log.info("Retrieve Criminal History Detail for: " + queryRequestId.toString());

        FirearmsQueryResponse firearmsQueryResponse = enhancedAuditDao.retrieveFirearmQueryDetail(queryRequestId);

        return firearmsQueryResponse;
    }

    @POST
    @Path("/retrieveWarrantQueryDetail")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    @Override
    public PersonQueryWarrantResponse retrieveWarrantQueryDetail(Integer queryRequestId) {
        log.info("Retrieve Criminal History Detail for: " + queryRequestId.toString());

        PersonQueryWarrantResponse personQueryWarrantResponse = enhancedAuditDao
                .retrieveWarrantQueryDetail(queryRequestId);

        return personQueryWarrantResponse;
    }

    @POST
    @Path("/retrieveVehicleCrashQueryResultsDetail")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    @Override
    public VehicleCrashQueryResponse retrieveVehicleCrashQueryResultsDetail(Integer queryRequestId) {
        log.info("Retrieve Vehicle Crash Query Response for: " + queryRequestId.toString());

        VehicleCrashQueryResponse vehicleCrashQueryResponse = enhancedAuditDao
                .retrieveVehicleCrashQueryResultsDetail(queryRequestId);

        return vehicleCrashQueryResponse;
    }

    @POST
    @Path("/retrieveIdentificationResultsQueryDetail")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    @Override
    public IdentificationQueryResponse retrieveIdentificationResultsQueryDetail(Integer queryRequestId) {
        log.info("Retrieve Identification Query Response for: " + queryRequestId.toString());

        IdentificationQueryResponse identificationQueryResponse = enhancedAuditDao
                .retrieveIdentificationResultsQueryDetail(queryRequestId);

        return identificationQueryResponse;
    }

    @POST
    @Path("/retrieveSubscriptionQueryResults")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    @Override
    public SubscriptionQueryResponse retrieveSubscriptionQueryResults(Integer queryRequestId) {
        log.info("Retrieve Subscription Query Response for: " + queryRequestId.toString());

        SubscriptionQueryResponse subscriptionQueryResponse = enhancedAuditDao
                .retrieveSubscriptionQueryResults(queryRequestId);

        return subscriptionQueryResponse;
    }

    @POST
    @Path("/retrieveUserPrintRequests")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    @Override
    public List<PrintResults> retrieveUserPrintRequests(Integer userInfoId) {
        log.info("Retrieve User Print Requests for: " + userInfoId.toString());

        List<PrintResults> printResults = enhancedAuditDao.retrieveUserPrintRequests(userInfoId);

        return printResults;
    }

    @POST
    @Path("/retrieveProfessionalLicensingQueryDetail")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    @Override
    public ProfessionalLicensingQueryResponse retrieveProfessionalLicensingQueryDetail(Integer queryRequestId) {
        log.info("Retrieve Professional Licensing Query Response for: " + queryRequestId.toString());

        ProfessionalLicensingQueryResponse professionalLicensingQueryResponse = enhancedAuditDao
                .retrieveProfessionalLicensingQueryResponse(queryRequestId);

        return professionalLicensingQueryResponse;
    }

    @POST
    @Path("/retrieveIncidentReportQueryDetail")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    @Override
    public IncidentReportQueryResponse retrieveIncidentReportQueryDetail(Integer queryRequestId) {
        log.info("Retrieve Incident Report Query Response for: " + queryRequestId.toString());

        IncidentReportQueryResponse incidentReportQueryResponse = enhancedAuditDao
                .retrieveIncidentReportQueryResponse(queryRequestId);

        return incidentReportQueryResponse;
    }

    @POST
    @Path("/retrieveWildlifeQueryDetail")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    @Override
    public WildlifeQueryResponse retrieveWildlifeQueryDetail(Integer queryRequestId) {
        log.info("Retrieve Wildlife Query Response for: " + queryRequestId.toString());

        WildlifeQueryResponse wildlifeQueryResponse = enhancedAuditDao.retrieveWildlifeQueryResponse(queryRequestId);

        return wildlifeQueryResponse;
    }

    @POST
    @Path("/retrievePersonSearchResults")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    @Override
    public List<PersonSearchResult> retrievePersonSearchResults(Integer personSearchRequestId) {
        log.info("Retrieve Person Search Responses for: " + personSearchRequestId.toString());

        List<PersonSearchResult> personSearchResults = enhancedAuditDao
                .retrievePersonSearchResults(personSearchRequestId);

        return personSearchResults;
    }

    @POST
    @Path("/retrieveFirearmSearchResults")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    @Override
    public List<FirearmSearchResult> retrieveFirearmSearchResults(Integer firearmSearchRequestId) {
        log.info("Retrieve Firearms Search Responses for: " + firearmSearchRequestId.toString());

        List<FirearmSearchResult> firearmSearchResults = enhancedAuditDao
                .retrieveFirearmSearchResults(firearmSearchRequestId);

        return firearmSearchResults;
    }

    @POST
    @Path("/retrieveVehicleSearchResults")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    @Override
    public List<VehicleSearchResult> retrieveVehicleSearchResults(Integer vehicleSearchRequestId) {
        log.info("Retrieve Vehicle Search Responses for: " + vehicleSearchRequestId.toString());

        List<VehicleSearchResult> vehicleSearchResults = enhancedAuditDao
                .retrieveVehicleSearchResults(vehicleSearchRequestId);

        return vehicleSearchResults;
    }

    @POST
    @Path("/retrieveIncidentSearchResults")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    @Override
    public List<IncidentSearchResult> retrieveIncidentSearchResults(Integer incidentSearchRequestId) {
        log.info("Retrieve Incident Search Responses for: " + incidentSearchRequestId.toString());

        List<IncidentSearchResult> incidentSearchResults = enhancedAuditDao
                .retrieveIncidentSearchResults(incidentSearchRequestId);

        return incidentSearchResults;
    }

    @POST
    @Path("/retrieveCannabisLicensingQueryDetail")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    @Override
    public CannabisLicensingQueryResponse retrieveCannabisLicensingQueryDetail(Integer queryRequestId) {
        log.info("Retrieve Cannabis License Query Response for: " + queryRequestId.toString());

        CannabisLicensingQueryResponse cannabisLicensingQueryResponse = enhancedAuditDao
                .retrieveCannabisLicenseQueryResponse(queryRequestId);

        return cannabisLicensingQueryResponse;

    }

}