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
package org.ojbc.util.camel.processor;

import static junit.framework.Assert.assertEquals;

import java.util.HashMap;
import java.util.Map;

import org.apache.camel.CamelContext;
import org.apache.camel.Exchange;
import org.apache.camel.impl.DefaultCamelContext;
import org.apache.camel.impl.DefaultExchange;
import org.junit.Test;

public class TestSystemNameToTopicExpressionMapper {

	@Test
	public void populateTopicExpressionHeader() throws Exception
	{
		SystemNameToTopicExpressionMapper systemNameToTopicExpressionMapper = new SystemNameToTopicExpressionMapper();
		
		Map<String, String> systemNameToTopicMap = new HashMap<String, String>();
		
		systemNameToTopicMap.put("{http://ojbc.org/ParoleCase/1.0}ParolingAuthority","topics:person/arrest");
		
		systemNameToTopicExpressionMapper.setSystemNameToTopicMap(systemNameToTopicMap );
		
		CamelContext ctx = new DefaultCamelContext(); 
		Exchange ex = new DefaultExchange(ctx);
		
		systemNameToTopicExpressionMapper.populateTopicExpressionHeader(ex, "{http://ojbc.org/ParoleCase/1.0}ParolingAuthority");
		
		assertEquals("topics:person/arrest", (String)ex.getIn().getHeader("topicExpression"));
	}

}
