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
package org.ojbc.intermediaries.sn.tests;

import java.io.FileInputStream;

import org.apache.camel.test.spring.junit5.CamelSpringBootTest;
import org.dbunit.dataset.IDataSet;
import org.dbunit.dataset.xml.FlatXmlDataSetBuilder;
import org.ojbc.intermediaries.sn.SubscriptionNotificationService;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.annotation.DirtiesContext.ClassMode;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;

@CamelSpringBootTest
@SpringBootTest(classes=SubscriptionNotificationService.class, webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@ActiveProfiles("dev")
@DirtiesContext(classMode = ClassMode.AFTER_EACH_TEST_METHOD)
@SpringJUnitConfig(locations={
		"classpath:META-INF/spring/h2-mock-database-application-context.xml",		
		"classpath:META-INF/spring/h2-mock-database-context-rapback-datastore.xml",
		"classpath:META-INF/spring/h2-mock-database-context-enhanced-auditlog.xml"
}) 
public abstract class AbstractSubscriptionNotificationTest {

	protected static final String SUBSCRIPTION_REFERENCE_ELEMENT_STRING = "<b-2:SubscriptionReference>";
	protected static final String UNSUBSCRIBE_RESPONSE_ELEMENT_STRING = "<b-2:UnsubscribeResponse xmlns:b-2=\"http://docs.oasis-open.org/wsn/b-2\"/>";
	
    IDataSet getCleanDataSet() throws Exception{  
    	// get insert data  
    	return new FlatXmlDataSetBuilder().build(new FileInputStream(
    			"src/test/resources/xmlInstances/dbUnit/cleanDataSet.xml"));
    }  

}
