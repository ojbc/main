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
import javax.jws.WebService;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.ojbc.util.model.TotpUser;

@WebService
public interface TotpUserRestInterface {
   
   @POST
   @Path("")
   @Produces(MediaType.APPLICATION_JSON)
   @Consumes(MediaType.APPLICATION_JSON)
   public Integer saveTotpUser(TotpUser totpUser);
   
   @GET
   @Path("/{userName}")
   @Produces(MediaType.APPLICATION_JSON)
   public TotpUser getTotpUserByUserName(@PathParam("userName") String userName);
   
   @DELETE
   @Path("/{userName}")
   @Produces(MediaType.APPLICATION_JSON)
   public Integer deleteTotpUserByUserName(@PathParam("userName") String userName);
   
   @GET
   @Path("/totpUsers")
   @Produces(MediaType.APPLICATION_JSON)
   public Response returnTotpUsers(); 
}
