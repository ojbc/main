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
package org.ojbc.util.camel.header.filter;

import org.apache.camel.component.cxf.common.header.CxfHeaderFilterStrategy;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * This class extends the Camel CxfHeaderFilterStrategy and a header pattern is added
 * to remove any header that starts with OJBC prior to sending the message over the wire.
 * 
 * This allows the route to use internal message headers but any receiving endpoint will
 * not receive the headers as HTTP headers over the wire.
 * 
 * 
 */
public class OJBCCamelHeaderFilterStrategy extends CxfHeaderFilterStrategy {

	private static final Log log = LogFactory.getLog(OJBCCamelHeaderFilterStrategy.class);
	
	public OJBCCamelHeaderFilterStrategy()
	{
		super();
		
		String defaultFilterPattern = this.getOutFilterPattern();
		
		log.debug("Default out filter pattern provided by camel: " + defaultFilterPattern);

		defaultFilterPattern += "|OJBC[.|a-z|A-z|0-9]*";
		
		log.debug("Updated default filter pattern to include OJBC headers: " + defaultFilterPattern);
		
		this.setOutFilterPattern(defaultFilterPattern);
	}
	
}
