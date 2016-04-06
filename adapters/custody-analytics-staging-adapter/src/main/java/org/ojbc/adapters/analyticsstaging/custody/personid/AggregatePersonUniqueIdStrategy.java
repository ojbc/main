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
 * Copyright 2012-2015 Open Justice Broker Consortium
 */
package org.ojbc.adapters.analyticsstaging.custody.personid;

import org.apache.camel.Exchange;
import org.apache.camel.processor.aggregate.AggregationStrategy;
import org.springframework.stereotype.Component;

/**
 * The interface to combine the person unique ID into the exchange header. 
 *
 */
@Component("aggregatePersonUniqueIdStrategy")
public class AggregatePersonUniqueIdStrategy implements AggregationStrategy{
	
	@Override
    public Exchange aggregate(Exchange original, Exchange resource) {
        Object originalBody = original.getIn().getBody();
        Object resourceResponse = resource.getIn().getBody();
        // TODO retrieve the personUnique ID from the resourceResponse. 
        String personUniqueId = "personUniqueId";
        
        if (original.getPattern().isOutCapable()) {
            original.getOut().setBody(originalBody);
            original.getOut().setHeader("personUniqueId", 1);
        } else {
            original.getIn().setBody(originalBody);
            original.getIn().setHeader("personUniqueId", 1);
        }
        return original;
    }
}
