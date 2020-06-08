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
package org.ojbc.bundles.utilities.auditing;

import java.util.List;

import javax.jws.WebService;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.ojbc.audit.enhanced.dao.model.FederalRapbackNotification;
import org.ojbc.audit.enhanced.dao.model.FederalRapbackSubscription;
import org.ojbc.audit.enhanced.dao.model.FederalRapbackSubscriptionDetail;
import org.ojbc.audit.enhanced.dao.model.FirearmsQueryResponse;
import org.ojbc.audit.enhanced.dao.model.FirearmsSearchRequest;
import org.ojbc.audit.enhanced.dao.model.IdentificationQueryResponse;
import org.ojbc.audit.enhanced.dao.model.IncidentReportQueryResponse;
import org.ojbc.audit.enhanced.dao.model.IncidentSearchRequest;
import org.ojbc.audit.enhanced.dao.model.NotificationSent;
import org.ojbc.audit.enhanced.dao.model.PersonQueryCriminalHistoryResponse;
import org.ojbc.audit.enhanced.dao.model.PersonQueryWarrantResponse;
import org.ojbc.audit.enhanced.dao.model.PersonSearchRequest;
import org.ojbc.audit.enhanced.dao.model.PrintResults;
import org.ojbc.audit.enhanced.dao.model.ProfessionalLicensingQueryResponse;
import org.ojbc.audit.enhanced.dao.model.QueryRequest;
import org.ojbc.audit.enhanced.dao.model.QueryRequestByDateRange;
import org.ojbc.audit.enhanced.dao.model.SubscriptionQueryResponse;
import org.ojbc.audit.enhanced.dao.model.UserAcknowledgement;
import org.ojbc.audit.enhanced.dao.model.UserInfo;
import org.ojbc.audit.enhanced.dao.model.VehicleCrashQueryResponse;
import org.ojbc.audit.enhanced.dao.model.VehicleSearchRequest;
import org.ojbc.audit.enhanced.dao.model.WildlifeQueryResponse;
import org.ojbc.audit.enhanced.dao.model.auditsearch.AuditSearchRequest;
import org.ojbc.audit.enhanced.dao.model.auditsearch.UserAuthenticationSearchRequest;
import org.ojbc.audit.enhanced.dao.model.auditsearch.UserAuthenticationSearchResponse;
import org.ojbc.util.model.rapback.AgencyProfile;
import org.ojbc.util.model.rapback.ExpiringSubscriptionRequest;
import org.ojbc.util.model.rapback.Subscription;

@Path("/audit")
@WebService
public interface AuditInterface {
 
   @POST
   @Path("/userAcknowledgement")
   @Produces(MediaType.APPLICATION_JSON)
   @Consumes(MediaType.APPLICATION_JSON)
   public Response auditUserAcknowledgement(UserAcknowledgement userAcknowledgement);

	
   @POST
   @Path("/printResults")
   @Produces(MediaType.APPLICATION_JSON)
   @Consumes(MediaType.APPLICATION_JSON)
   public Response auditPrintResults(PrintResults printResults);
   
   @POST
   @Path("/userLogin")
   @Produces(MediaType.APPLICATION_JSON)
   @Consumes(MediaType.APPLICATION_JSON)
   public Response auditUserLogin(UserInfo userInfo);

   @POST
   @Path("/userLogout")
   @Produces(MediaType.APPLICATION_JSON)
   @Consumes(MediaType.APPLICATION_JSON)
   public Response auditUserLogout(UserInfo userInfo);

   @GET
   @Path("/searchForFederalRapbackSubscriptionsByStateSubscriptionId/{subscriptionId}")
   @Produces(MediaType.APPLICATION_JSON)
   @Consumes(MediaType.APPLICATION_JSON)
   public List<FederalRapbackSubscription> searchForFederalRapbackSubscriptions(@PathParam("subscriptionId") String subscriptionId);

   @GET
   @Path("/subscriptions/{subscriptionId}/federalRapbackSubscriptionsDetail")
   @Produces(MediaType.APPLICATION_JSON)
   @Consumes(MediaType.APPLICATION_JSON)
   public FederalRapbackSubscriptionDetail returnFederalRapbackSubscriptionDetail(@PathParam("subscriptionId") String subscriptionId);
   
   @POST
   @Path("/retrieveExpiringSubscriptions")
   @Produces(MediaType.APPLICATION_JSON)
   @Consumes(MediaType.APPLICATION_JSON)
   public List<Subscription> retrieveExpiringSubscriptions(ExpiringSubscriptionRequest request );
   
   @POST
   @Path("/retrieveExpiredSubscriptions")
   @Produces(MediaType.APPLICATION_JSON)
   @Consumes(MediaType.APPLICATION_JSON)
   public List<Subscription> retrieveExpiredSubscriptions(ExpiringSubscriptionRequest request );

   @GET
   @Path("/agencies")
   @Produces(MediaType.APPLICATION_JSON)
   @Consumes(MediaType.APPLICATION_JSON)
   public List<AgencyProfile> retrieveAllAgencies();

   @POST
   @Path("/retrieveRapbackNotifications")
   @Produces(MediaType.APPLICATION_JSON)
   @Consumes(MediaType.APPLICATION_JSON)
   public List<FederalRapbackNotification> retrieveRapbackNotifications(QueryRequestByDateRange queryRequestByDateRange);
   
   @POST
   @Path("/retrieveNotificationsSent")
   @Produces(MediaType.APPLICATION_JSON)
   @Consumes(MediaType.APPLICATION_JSON)
   public List<NotificationSent> retrieveNotificationsSent(QueryRequestByDateRange queryRequestByDateRange);

   
   @GET
   @Path("/federalRapbackNotifications/{subscriptionId}")
   @Produces(MediaType.APPLICATION_JSON)
   @Consumes(MediaType.APPLICATION_JSON)
   public List<FederalRapbackNotification> searchForFederalRapbackNotifications(@PathParam("subscriptionId") String subscriptionId);

   @GET
   @Path("/retrieveFederalRapbackSubscriptionErrors")
   @Produces(MediaType.APPLICATION_JSON)
   @Consumes(MediaType.APPLICATION_JSON)
   public List<FederalRapbackSubscription> retrieveFederalRapbackSubscriptionErrors();
   
   @POST
   @Path("/retrieveUserAuthentications")
   @Produces(MediaType.APPLICATION_JSON)
   @Consumes(MediaType.APPLICATION_JSON)
   public List<UserAuthenticationSearchResponse> retrieveUserAuthentications(UserAuthenticationSearchRequest authenticationSearchRequest);

   @POST
   @Path("/retrieveUserPrintRequests")
   @Produces(MediaType.APPLICATION_JSON)
   @Consumes(MediaType.APPLICATION_JSON)
   public List<PrintResults> retrieveUserPrintRequests(Integer userInfoId);

   @POST
   @Path("/retrievePersonSearchRequest")
   @Produces(MediaType.APPLICATION_JSON)
   @Consumes(MediaType.APPLICATION_JSON)
   public List<PersonSearchRequest> retrievePersonSearchRequest(AuditSearchRequest auditSearchRequest);

   @POST
   @Path("/retrieveFirearmSearchRequest")
   @Produces(MediaType.APPLICATION_JSON)
   @Consumes(MediaType.APPLICATION_JSON)
   public List<FirearmsSearchRequest> retrieveFirearmSearchRequest(AuditSearchRequest auditSearchRequest);
   
   @POST
   @Path("/retrieveVehicleSearchRequest")
   @Produces(MediaType.APPLICATION_JSON)
   @Consumes(MediaType.APPLICATION_JSON)
   public List<VehicleSearchRequest> retrieveVehicleSearchRequest(AuditSearchRequest auditSearchRequest);

   @POST
   @Path("/retrieveIncidentSearchRequest")
   @Produces(MediaType.APPLICATION_JSON)
   @Consumes(MediaType.APPLICATION_JSON)
   public List<IncidentSearchRequest> retrieveIncidentSearchRequest(AuditSearchRequest auditSearchRequest);

   @POST
   @Path("/retrieveQueryRequest")
   @Produces(MediaType.APPLICATION_JSON)
   @Consumes(MediaType.APPLICATION_JSON)
   public List<QueryRequest> retrieveQueryRequest(AuditSearchRequest auditSearchRequest);

   @POST
   @Path("/retrieveCriminalHistoryQueryDetail")
   @Produces(MediaType.APPLICATION_JSON)
   @Consumes(MediaType.APPLICATION_JSON)
   public PersonQueryCriminalHistoryResponse retrieveCriminalHistoryQueryDetail(Integer queryRequestId);

   @POST
   @Path("/retrieveIncidentReportQueryDetail")
   @Produces(MediaType.APPLICATION_JSON)
   @Consumes(MediaType.APPLICATION_JSON)
   public IncidentReportQueryResponse retrieveIncidentReportQueryDetail(Integer queryRequestId);

   @POST
   @Path("/retrieveFirearmQueryDetail")
   @Produces(MediaType.APPLICATION_JSON)
   @Consumes(MediaType.APPLICATION_JSON)
   public FirearmsQueryResponse retrieveFirearmQueryDetail(Integer queryRequestId);

   @POST
   @Path("/retrieveWarrantQueryDetail")
   @Produces(MediaType.APPLICATION_JSON)
   @Consumes(MediaType.APPLICATION_JSON)
   public PersonQueryWarrantResponse retrieveWarrantQueryDetail(Integer queryRequestId);
   
   @POST
   @Path("/retrieveVehicleCrashQueryResultsDetail")
   @Produces(MediaType.APPLICATION_JSON)
   @Consumes(MediaType.APPLICATION_JSON)
   public VehicleCrashQueryResponse retrieveVehicleCrashQueryResultsDetail(Integer queryRequestId);

   @POST
   @Path("/retrieveIdentificationResultsQueryDetail")
   @Produces(MediaType.APPLICATION_JSON)
   @Consumes(MediaType.APPLICATION_JSON)
   public IdentificationQueryResponse retrieveIdentificationResultsQueryDetail(Integer queryRequestId);

   @POST
   @Path("/retrieveSubscriptionQueryResults")
   @Produces(MediaType.APPLICATION_JSON)
   @Consumes(MediaType.APPLICATION_JSON)
   public SubscriptionQueryResponse retrieveSubscriptionQueryResults(Integer queryRequestId);

   @POST
   @Path("/retrieveProfessionalLicensingQueryDetail")
   @Produces(MediaType.APPLICATION_JSON)
   @Consumes(MediaType.APPLICATION_JSON)
   public ProfessionalLicensingQueryResponse retrieveProfessionalLicensingQueryDetail(Integer queryRequestId);

   @POST
   @Path("/retrieveWildlifeQueryDetail")
   @Produces(MediaType.APPLICATION_JSON)
   @Consumes(MediaType.APPLICATION_JSON)
   public WildlifeQueryResponse retrieveWildlifeQueryDetail(Integer queryRequestId);
   
}
