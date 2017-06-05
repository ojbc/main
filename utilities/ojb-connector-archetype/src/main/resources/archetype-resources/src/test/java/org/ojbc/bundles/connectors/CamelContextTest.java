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
#set( $symbol_pound = '#' )
#set( $symbol_dollar = '$' )
#set( $symbol_escape = '\' )
package org.ojbc.bundles.connectors;

import org.apache.camel.test.junit4.CamelSpringTestSupport;
import org.junit.Test;
import org.springframework.context.support.AbstractXmlApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * Intended to test load the Camel Context into memory and ensuring 
 * that its refrences are satisfied
 * 
 * @author SEARCH
 */
public class CamelContextTest extends CamelSpringTestSupport {

	/**
	 * Creates the camel context application context
	 */
	@Override
	protected AbstractXmlApplicationContext createApplicationContext() {
		
		return new ClassPathXmlApplicationContext(
				new String[] { "META-INF/spring/camel-context.xml" });
	}

	/**
	 * Tests starting the camel context and loading its references into memory 
	 */
	@Test
	public void testApplicationStartup() {
		assertTrue(true);
	}
}
