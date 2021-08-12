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
@Profile("juvenile-query")
public class JuvenileQueryResultsRoutes extends RouteBuilder {

    @Override
    public void configure() throws Exception {
        from("juvenileOffenseHistoryResultsHandlerServiceEndpoint")
        .routeId("juvenileOffenseHistoryResultsHandlerRoute")
        .log(LoggingLevel.DEBUG, "Received response message - "
        		+ "Juvenile Offense Response: ${body}")
        .to("bean:juvenileQueryOffenseHistoryMessageProcessor?method=processRequestPayload")
        .convertBodyTo(String.class)
        .to("bean:juvenileOffenseHistoryRequestProcessor?method=updateMapWithResponse");
        
        from("juvenileCasePlanHistoryResultsHandlerServiceEndpoint")
        .routeId("juvenileCasePlanHistoryResultsHandlerRoute")
        .log(LoggingLevel.DEBUG, "Received response message - "
        		+ "Juvenile Case Plan Response: ${body}")
        .to("bean:juvenileQueryCasePlanHistoryMessageProcessor?method=processRequestPayload")
        .convertBodyTo(String.class)
        .to("bean:juvenileCasePlanHistoryRequestProcessor?method=updateMapWithResponse");
        
        from("juvenilePlacementHistoryResultsHandlerServiceEndpoint")
        .routeId("juvenilePlacementHistoryResultsHandlerRoute")
        .log(LoggingLevel.DEBUG, "Received response message - "
        		+ "Juvenile Placement Response:: ${body}")
        .to("bean:juvenileQueryPlacementHistoryMessageProcessor?method=processRequestPayload")
        .convertBodyTo(String.class)
        .to("bean:juvenilePlacementHistoryRequestProcessor?method=updateMapWithResponse");
        
        from("juvenileReferralHistoryResultsHandlerServiceEndpoint")
        .routeId("juvenileReferralHistoryResultsHandlerRoute")
        .log(LoggingLevel.DEBUG, "Received response message - "
        		+ "Juvenile Referral Response: ${body}")
        .to("bean:juvenileQueryReferralHistoryMessageProcessor?method=processRequestPayload")
        .convertBodyTo(String.class)
        .to("bean:juvenileReferralHistoryRequestProcessor?method=updateMapWithResponse");
        
        from("juvenileIntakeHistoryResultsHandlerServiceEndpoint")
        .routeId("juvenileIntakeHistoryResultsHandlerRoute")
        .log(LoggingLevel.DEBUG, "Received response message - "
        		+ "Juvenile Intake Response: ${body}")
        .to("bean:juvenileQueryIntakeHistoryMessageProcessor?method=processRequestPayload")
        .convertBodyTo(String.class)
        .to("bean:juvenileIntakeHistoryRequestProcessor?method=updateMapWithResponse");
        
        from("juvenileHearingHistoryResultsHandlerServiceEndpoint")
        .routeId("juvenileHearingHistoryResultsHandlerRoute")
        .log(LoggingLevel.DEBUG, "Received response message - "
        		+ "Juvenile Hearing Response: ${body}")
        .to("bean:juvenileQueryHearingHistoryMessageProcessor?method=processRequestPayload")
        .convertBodyTo(String.class)
        .to("bean:juvenileHearingHistoryRequestProcessor?method=updateMapWithResponse");
    }
}