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

import java.util.List;

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
import org.ojbc.util.model.rapback.AgencyProfile;
import org.ojbc.util.model.rapback.ExpiringSubscriptionRequest;
import org.ojbc.util.model.rapback.Subscription;

import jakarta.jws.WebService;
import jakarta.websocket.server.PathParam;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.Response;

@Path("/audit")
@WebService
public interface AuditInterface {
 
   @POST
   @Path("/userAcknowledgement")
   @Produces("application/json")
   @Consumes("application/json")
   public Response auditUserAcknowledgement(UserAcknowledgement userAcknowledgement);

	
   @POST
   @Path("/printResults")
   @Produces("application/json")
   @Consumes("application/json")
   public Response auditPrintResults(PrintResults printResults);
   
   @POST
   @Path("/userLogin")
   @Produces("application/json")
   @Consumes("application/json")
   public Response auditUserLogin(UserInfo userInfo);

   @POST
   @Path("/userLogout")
   @Produces("application/json")
   @Consumes("application/json")
   public Response auditUserLogout(UserInfo userInfo);

   @GET
   @Path("/searchForFederalRapbackSubscriptionsByStateSubscriptionId/{subscriptionId}")
   @Produces("application/json")
   @Consumes("application/json")
   public List<FederalRapbackSubscription> searchForFederalRapbackSubscriptions(@PathParam("subscriptionId") String subscriptionId);

   @GET
   @Path("/subscriptions/{subscriptionId}/federalRapbackSubscriptionsDetail")
   @Produces("application/json")
   @Consumes("application/json")
   public FederalRapbackSubscriptionDetail returnFederalRapbackSubscriptionDetail(@PathParam("subscriptionId") String subscriptionId);
   
   @POST
   @Path("/retrieveExpiringSubscriptions")
   @Produces("application/json")
   @Consumes("application/json")
   public List<Subscription> retrieveExpiringSubscriptions(ExpiringSubscriptionRequest request );
   
   @POST
   @Path("/retrieveExpiredSubscriptions")
   @Produces("application/json")
   @Consumes("application/json")
   public List<Subscription> retrieveExpiredSubscriptions(ExpiringSubscriptionRequest request );

   @GET
   @Path("/agencies")
   @Produces("application/json")
   @Consumes("application/json")
   public List<AgencyProfile> retrieveAllAgencies();

   @POST
   @Path("/retrieveRapbackNotifications")
   @Produces("application/json")
   @Consumes("application/json")
   public List<FederalRapbackNotification> retrieveRapbackNotifications(QueryRequestByDateRange queryRequestByDateRange);
   
   @POST
   @Path("/retrieveNotificationsSent")
   @Produces("application/json")
   @Consumes("application/json")
   public List<NotificationSent> retrieveNotificationsSent(QueryRequestByDateRange queryRequestByDateRange);

   
   @GET
   @Path("/federalRapbackNotifications/{subscriptionId}")
   @Produces("application/json")
   @Consumes("application/json")
   public List<FederalRapbackNotification> searchForFederalRapbackNotifications(@PathParam("subscriptionId") String subscriptionId);

   @GET
   @Path("/retrieveFederalRapbackSubscriptionErrors")
   @Produces("application/json")
   @Consumes("application/json")
   public List<FederalRapbackSubscription> retrieveFederalRapbackSubscriptionErrors();
   
   @POST
   @Path("/retrieveUserAuthentications")
   @Produces("application/json")
   @Consumes("application/json")
   public List<UserAuthenticationSearchResponse> retrieveUserAuthentications(UserAuthenticationSearchRequest authenticationSearchRequest);

   @POST
   @Path("/retrieveUserPrintRequests")
   @Produces("application/json")
   @Consumes("application/json")
   public List<PrintResults> retrieveUserPrintRequests(Integer userInfoId);

   @POST
   @Path("/retrievePersonSearchRequest")
   @Produces("application/json")
   @Consumes("application/json")
   public List<PersonSearchRequest> retrievePersonSearchRequest(AuditSearchRequest auditSearchRequest);

   @POST
   @Path("/retrievePersonSearchRequestByPerson")
   @Produces("application/json")
   @Consumes("application/json")
   public List<PersonSearchRequest> retrievePersonSearchRequestByPerson(AuditPersonSearchRequest auditSearchRequest);

   @POST
   @Path("/retrievePersonSearchResults")
   @Produces("application/json")
   @Consumes("application/json")
   public List<PersonSearchResult> retrievePersonSearchResults(Integer personSearchRequestId);

   @POST
   @Path("/retrieveFirearmSearchRequest")
   @Produces("application/json")
   @Consumes("application/json")
   public List<FirearmsSearchRequest> retrieveFirearmSearchRequest(AuditSearchRequest auditSearchRequest);

   @POST
   @Path("/retrieveFirearmSearchResults")
   @Produces("application/json")
   @Consumes("application/json")
   public List<FirearmSearchResult> retrieveFirearmSearchResults(Integer firearmSearchRequestId);

   @POST
   @Path("/retrieveVehicleSearchRequest")
   @Produces("application/json")
   @Consumes("application/json")
   public List<VehicleSearchRequest> retrieveVehicleSearchRequest(AuditSearchRequest auditSearchRequest);

   @POST
   @Path("/retrieveVehicleSearchResults")
   @Produces("application/json")
   @Consumes("application/json")
   public List<VehicleSearchResult> retrieveVehicleSearchResults(Integer vehicleSearchRequestId);
   
   @POST
   @Path("/retrieveIncidentSearchRequest")
   @Produces("application/json")
   @Consumes("application/json")
   public List<IncidentSearchRequest> retrieveIncidentSearchRequest(AuditSearchRequest auditSearchRequest);

   @POST
   @Path("/retrieveIncidentSearchResults")
   @Produces("application/json")
   @Consumes("application/json")
   public List<IncidentSearchResult> retrieveIncidentSearchResults(Integer incidentSearchRequestId);
   
   @POST
   @Path("/retrieveQueryRequest")
   @Produces("application/json")
   @Consumes("application/json")
   public List<QueryRequest> retrieveQueryRequest(AuditSearchRequest auditSearchRequest);

   @POST
   @Path("/retrieveCriminalHistoryQueryDetail")
   @Produces("application/json")
   @Consumes("application/json")
   public PersonQueryCriminalHistoryResponse retrieveCriminalHistoryQueryDetail(Integer queryRequestId);

   @POST
   @Path("/retrieveIncidentReportQueryDetail")
   @Produces("application/json")
   @Consumes("application/json")
   public IncidentReportQueryResponse retrieveIncidentReportQueryDetail(Integer queryRequestId);

   @POST
   @Path("/retrieveFirearmQueryDetail")
   @Produces("application/json")
   @Consumes("application/json")
   public FirearmsQueryResponse retrieveFirearmQueryDetail(Integer queryRequestId);

   @POST
   @Path("/retrieveWarrantQueryDetail")
   @Produces("application/json")
   @Consumes("application/json")
   public PersonQueryWarrantResponse retrieveWarrantQueryDetail(Integer queryRequestId);
   
   @POST
   @Path("/retrieveVehicleCrashQueryResultsDetail")
   @Produces("application/json")
   @Consumes("application/json")
   public VehicleCrashQueryResponse retrieveVehicleCrashQueryResultsDetail(Integer queryRequestId);

   @POST
   @Path("/retrieveIdentificationResultsQueryDetail")
   @Produces("application/json")
   @Consumes("application/json")
   public IdentificationQueryResponse retrieveIdentificationResultsQueryDetail(Integer queryRequestId);

   @POST
   @Path("/retrieveSubscriptionQueryResults")
   @Produces("application/json")
   @Consumes("application/json")
   public SubscriptionQueryResponse retrieveSubscriptionQueryResults(Integer queryRequestId);

   @POST
   @Path("/retrieveProfessionalLicensingQueryDetail")
   @Produces("application/json")
   @Consumes("application/json")
   public ProfessionalLicensingQueryResponse retrieveProfessionalLicensingQueryDetail(Integer queryRequestId);

   @POST
   @Path("/retrieveCannabisLicensingQueryDetail")
   @Produces("application/json")
   @Consumes("application/json")
   public CannabisLicensingQueryResponse retrieveCannabisLicensingQueryDetail(Integer queryRequestId);

   @POST
   @Path("/retrieveWildlifeQueryDetail")
   @Produces("application/json")
   @Consumes("application/json")
   public WildlifeQueryResponse retrieveWildlifeQueryDetail(Integer queryRequestId);
   
}
