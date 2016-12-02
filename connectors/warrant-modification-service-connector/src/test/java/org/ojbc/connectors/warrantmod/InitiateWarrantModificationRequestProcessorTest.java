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
package org.ojbc.connectors.warrantmod;

import java.util.HashMap;
import java.util.Map;

import org.apache.camel.test.spring.CamelSpringJUnit4ClassRunner;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.ojbc.test.util.XmlTestUtils;
import org.ojbc.util.camel.helper.OJBUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ContextConfiguration;
import org.w3c.dom.Document;

@RunWith(CamelSpringJUnit4ClassRunner.class)
@ContextConfiguration(locations={
		"classpath:META-INF/spring/spring-context.xml", 
		"classpath:META-INF/spring/dao.xml",
        "classpath:META-INF/spring/h2-mock-database-application-context.xml",
        "classpath:META-INF/spring/h2-mock-database-context-warrant-repository.xml",
		"classpath:META-INF/spring/properties-context.xml"})
@DirtiesContext
@Ignore
//TODO  Fix this after talking with Andrew. 
public class InitiateWarrantModificationRequestProcessorTest {
	private final Log log = LogFactory.getLog( InitiateWarrantModificationRequestProcessorTest.class );

	@Autowired 
	InitiateWarrantModificationRequestProcessor initiateWarrantModificationRequestProcessor; 

	@Test
	public void testCreateWarrantModificationRequest() throws Exception {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("WARRANTID", Integer.valueOf(1));
		
		Document document = initiateWarrantModificationRequestProcessor.createWarrantModificationRequest(map, new HashMap<String, Object>());
		String bodyString = OJBUtils.getStringFromDocument(document);
		log.info("body: \n" + bodyString);
		XmlTestUtils.compareDocs("src/test/resources/xmlInstances/warrantModRequest.xml", bodyString);
	}

}
