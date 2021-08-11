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
@Profile("subscriptions")
public class SubscriptionsResultsRoutes extends RouteBuilder {

    @Override
    public void configure() throws Exception {
        from("subscriptionSearchResultsHandlerServiceEndpoint")
        .routeId("subscriptionSearchResultsHandlerRoute")
        .log(LoggingLevel.DEBUG, "Received response message - Subscription Search: ${body}")
        .to("bean:subscriptionSearchMessageProcessor?method=processRequestPayload")
        .convertBodyTo(String.class)
        .to("bean:subscriptionSearchRequestProcessor?method=updateMapWithResponse");
        
        from("subscriptionQueryResultsHandlerServiceEndpoint")
        .routeId("subscriptionQueryResultsHandlerRoute")
        .log(LoggingLevel.DEBUG, "Received response message - Subscription Query: ${body}")
        .to("bean:subscriptionQueryMessageProcessor?method=processRequestPayload")
        .convertBodyTo(String.class)
        .to("bean:subscriptionQueryRequestProcessor?method=updateMapWithResponse");
    }
}