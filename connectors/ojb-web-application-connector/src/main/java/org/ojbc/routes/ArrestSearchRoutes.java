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
package org.ojbc.routes;

import org.apache.camel.LoggingLevel;
import org.apache.camel.builder.RouteBuilder;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

@Component
@Profile("arrest-search")
public class ArrestSearchRoutes extends RouteBuilder {

    @Override
    public void configure() throws Exception {
        from("arrestSearchResultsHandlerServiceEndpoint").routeId("arrestSearchResultsHandlerRoute")
        .log(LoggingLevel.DEBUG, "Received response message - Arrest Search Response: ${body}")
        .to("bean:arrestSearchMessageProcessor?method=processRequestPayload")
        .convertBodyTo(String.class)
        .to("bean:arrestSearchRequestProcessor?method=updateMapWithResponse");
        
        from("arrestModifyResultsHandlerServiceEndpoint").routeId("arrestModifyResultsHandlerRoute")
        .log(LoggingLevel.DEBUG, "Received response message - Arrest modify Response: ${body}")
        .to("bean:arrestModifyMessageProcessor?method=processRequestPayload")
        .convertBodyTo(String.class)
        .to("bean:arrestModifyRequestProcessor?method=updateMapWithResponse");
        
        from("recordReplicationResultsHandlerServiceEndpoint").routeId("recordReplicationResultsHandlerRoute")
        .log(LoggingLevel.DEBUG, "Received response message - Record Replication Response: ${body}")
        .to("bean:recordReplicationMessageProcessor?method=processRequestPayload")
        .convertBodyTo(String.class)
        .to("bean:recordReplicationRequestProcessor?method=updateMapWithResponse");
        
        from("auditSearchResultsHandlerServiceEndpoint").routeId("auditSearchResultsHandlerRoute")
        .log(LoggingLevel.DEBUG, "Received response message - Audit Search Response: ${body}")
        .to("bean:auditSearchMessageProcessor?method=processRequestPayload")
        .convertBodyTo(String.class)
        .to("bean:auditSearchRequestProcessor?method=updateMapWithResponse");
    }
}