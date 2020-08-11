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
package org.ojbc.adapters.rapbackdatastore.processor;

import static org.junit.Assert.assertEquals;

import java.io.File;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.ojbc.util.xml.XmlUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.w3c.dom.Document;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {
        "classpath:META-INF/spring/camel-context.xml",
        "classpath:META-INF/spring/spring-context.xml",
        "classpath:META-INF/spring/cxf-endpoints.xml",      
        "classpath:META-INF/spring/properties-context.xml",
        "classpath:META-INF/spring/dao.xml",
        "classpath:META-INF/spring/h2-mock-database-application-context.xml",
        "classpath:META-INF/spring/h2-mock-database-context-rapback-datastore.xml",
        "classpath:META-INF/spring/subscription-management-routes.xml"
      })
@DirtiesContext
public class TestNsorFiveYearCheckResultsQueryProcessor {
	
	private static final Log log = LogFactory.getLog( TestNsorFiveYearCheckResultsQueryProcessor.class );
    
    @Autowired
    private NsorFiveYearchCheckResultsQueryProcessor nsorFiveYearchCheckResultsQueryProcessor;

    @Test
    public void testNsorFiveYearCheckQuery() throws Exception
    {
    	Document report = XmlUtils.parseFileToDocument(new File("src/test/resources/xmlInstances/identificationResultsQuery/OrganizationIdentificationNsorQueryRequest.xml"));
    	Document reponse = nsorFiveYearchCheckResultsQueryProcessor.returnNsorFiveYearCheckQueryResponse(report);
    	
    	assertEquals("true", XmlUtils.xPathStringSearch(reponse, "/oinfq-res-doc:OrganizationIdentificationNsorQueryResults/oirq-res-ext:NsorFiveYearCheckResultsAvailableIndicator"));
    	
    	assertEquals("http://ojbc.org/Services/WSDL/Organization_Identification_Results_Search_Request_Service/Subscriptions/1.0}RapbackDatastore", XmlUtils.xPathStringSearch(reponse, "/oinfq-res-doc:OrganizationIdentificationNsorQueryResults/oirq-res-ext:SourceSystemNameText"));
    	
    	assertEquals("PersonRecordID", XmlUtils.xPathStringSearch(reponse, "/oinfq-res-doc:OrganizationIdentificationNsorQueryResults/intel30:SystemIdentification/nc30:IdentificationID"));
    	assertEquals("RapbackDataStore", XmlUtils.xPathStringSearch(reponse, "/oinfq-res-doc:OrganizationIdentificationNsorQueryResults/intel30:SystemIdentification/nc30:SystemName"));
    	
    	XmlUtils.printNode(reponse);
    	
    	
    }
	
}
