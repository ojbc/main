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
package org.ojbc.intermediaries.sn;

import javax.annotation.Resource;

import org.apache.camel.builder.RouteBuilder;
import org.springframework.stereotype.Component;

@Component
public class EmailEncryptionService extends RouteBuilder{
	
	@Resource
	SubscriptionNotificationServiceProperties appProperties;
	
    @Override
    public void configure() throws Exception {
        from("direct:encryptBody").routeId("encryptionRoute")
        .log("In encryption route.")
        .filter()
           .simple(appProperties.getEncryptEmailBody() + "== 'true'")
            	.marshal()
            	.pgp(appProperties.getPgpPublicKeyFileName(), appProperties.getPgpUserName())
            	.log("Email body encrypted and marshalled.");
    }
}
