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
import org.ojbc.audit.enhanced.dao.model.PrintResults;
import org.ojbc.audit.enhanced.dao.model.QueryRequestByDateRange;
import org.ojbc.audit.enhanced.dao.model.UserInfo;
import org.ojbc.util.model.rapback.AgencyProfile;
import org.ojbc.util.model.rapback.ExpiringSubscriptionRequest;
import org.ojbc.util.model.rapback.Subscription;

@Path("/audit")
@WebService
public interface AuditInterface {
 
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
   @Path("/retrieveAllAgencies")
   @Produces(MediaType.APPLICATION_JSON)
   @Consumes(MediaType.APPLICATION_JSON)
   public List<AgencyProfile> retrieveAllAgencies();

   @POST
   @Path("/retrieveRapbackNotifications")
   @Produces(MediaType.APPLICATION_JSON)
   @Consumes(MediaType.APPLICATION_JSON)
   public List<FederalRapbackNotification> retrieveRapbackNotifications(QueryRequestByDateRange queryRequestByDateRange);
   
   @GET
   @Path("/searchForFederalRapbackNotificationsByStateSubscriptionId/{subscriptionId}")
   @Produces(MediaType.APPLICATION_JSON)
   @Consumes(MediaType.APPLICATION_JSON)
   public List<FederalRapbackNotification> searchForFederalRapbackNotifications(@PathParam("subscriptionId") String subscriptionId);

   @GET
   @Path("/retrieveFederalRapbackSubscriptionErrors")
   @Produces(MediaType.APPLICATION_JSON)
   @Consumes(MediaType.APPLICATION_JSON)
   public List<FederalRapbackSubscription> retrieveFederalRapbackSubscriptionErrors();

}
