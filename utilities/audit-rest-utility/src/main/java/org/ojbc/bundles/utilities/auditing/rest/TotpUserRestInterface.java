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
import org.ojbc.util.model.TotpUser;

import jakarta.jws.WebService;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.DELETE;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.Response;

@Path("/")
@WebService
public interface TotpUserRestInterface {
   
   @POST
   @Path("")
   @Produces("application/json")
   @Consumes("application/json")
   public Integer saveTotpUser(TotpUser totpUser);
   
   @POST
   @Path("/userName")
   @Produces("application/json")
   public TotpUser getTotpUserByUserName(String userName);
   
   @DELETE
   @Path("")
   @Produces("application/json")
   public Integer deleteTotpUserByUserName(String userName);
   
   @GET
   @Path("/totpUsers")
   @Produces("application/json")
   public Response returnTotpUsers(); 
   
   @POST
   @Path("/user/isGoogleAuthUser")
   @Produces("application/json")
   @Consumes("application/json")
   public Boolean isGoogleAuthUser(String userName); 
}
