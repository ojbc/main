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
package org.ojbc.bundles.adapters;

import org.apache.camel.Exchange;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.ojbc.bundles.adapters.staticmock.StaticMockQuery;
import org.w3c.dom.Document;

public class StaticMockAdapterProcessor {

	private static final Log log = LogFactory.getLog( StaticMockAdapterProcessor.class );
	private StaticMockQuery staticMockQuery;
	
    public void search(Exchange exchange) throws Exception
    {
        log.debug("Enter method - Call Response Handler for service");
        Document requestDocument = exchange.getIn().getBody(Document.class);
        Document response = staticMockQuery.searchDocuments(requestDocument, exchange.getProperty(getClass().getName() + ".context"));
        exchange.getIn().setBody(response);
    }
    
    public void query(Exchange exchange) throws Exception
    {
        log.debug("Enter method - Call Response Handler for service");
        Document requestDocument = exchange.getIn().getBody(Document.class);
        Document response = staticMockQuery.queryDocuments(requestDocument, 
        		exchange.getProperty(getClass().getName() + ".context")).get(0).getDocument();
        exchange.getIn().setBody(response);
    }

	public StaticMockQuery getStaticMockQuery() {
		return staticMockQuery;
	}

	public void setStaticMockQuery(StaticMockQuery staticMockQuery) {
		this.staticMockQuery = staticMockQuery;
	}
}
