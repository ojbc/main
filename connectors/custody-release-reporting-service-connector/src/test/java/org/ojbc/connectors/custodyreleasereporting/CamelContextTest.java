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
package org.ojbc.connectors.custodyreleasereporting;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.io.File;

import javax.annotation.Resource;

import org.apache.camel.EndpointInject;
import org.apache.camel.Exchange;
import org.apache.camel.builder.AdviceWithRouteBuilder;
import org.apache.camel.component.mock.MockEndpoint;
import org.apache.camel.model.ModelCamelContext;
import org.apache.camel.test.spring.CamelSpringJUnit4ClassRunner;
import org.apache.commons.io.FileUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;

@RunWith(CamelSpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {
		"classpath:META-INF/spring/camel-context.xml",
		"classpath:META-INF/spring/cxf-endpoints.xml",		
		"classpath:META-INF/spring/properties-context.xml",
		})
public class CamelContextTest {

    private static final Log log = LogFactory.getLog( CamelContextTest.class );
	
	public static final String CXF_OPERATION_NAME = "ReportCustodyRelease";
	public static final String CXF_OPERATION_NAMESPACE = "http://ojbc.org/Services/WSDL/CustodyReleaseReportingService/1.0";
    
    @Resource
    private ModelCamelContext context;
	    
    @EndpointInject(uri = "mock:cxf:bean:custoryReleaseReportingService")
    protected MockEndpoint custodyReleaseReportingServiceMockEndpoint;

    @Before
	public void setUp() throws Exception {

    	//We mock the web service endpoints here
    	context.getRouteDefinition("Custody_Release_Reporting_Connector_Intermediary_Route").adviceWith(context, new AdviceWithRouteBuilder() {
    	    @Override
    	    public void configure() throws Exception {
    	    	
    	    	//We mock the notification broker endpoint
    	    	mockEndpointsAndSkip("cxf:bean:custoryReleaseReportingService*");
    	    	
    	    }              
    	});
    	
		context.start();		
	}

	@After
	public void tearDown() throws Exception {
	}
	
	@Test
	public void contextStartup() throws Exception {
		assertTrue(true);
		
		custodyReleaseReportingServiceMockEndpoint.expectedMessageCount(1);
		
		//Get input directory by resolving property
		String inputDirectoryString = context.resolvePropertyPlaceholders("{{custody.connectorFileDirectory}}") + "/input";
		log.info("Connector input directory:"+ inputDirectoryString);
		assertEquals("/tmp/ojb/demo/custodyRelease/connector/input", inputDirectoryString);
		
        File inputDirectory = new File(inputDirectoryString);
		
		if (inputDirectory.exists())
		{	
			FileUtils.cleanDirectory(inputDirectory);
		}
		

        File inputFile = new File("src/test/resources/xmlInstances/CustodyReleaseReport.xml");
		assertNotNull(inputFile);
		
		//Kick off the process by copying the process record file to the input directory 
		FileUtils.copyFileToDirectory(inputFile, inputDirectory);

		Thread.sleep(5000);
		
		custodyReleaseReportingServiceMockEndpoint.assertIsSatisfied();
		
		Exchange ex = custodyReleaseReportingServiceMockEndpoint.getExchanges().get(0);
		assertEquals(ex.getIn().getHeader("operationName"), CXF_OPERATION_NAME);
		assertEquals(ex.getIn().getHeader("operationNamespace"), CXF_OPERATION_NAMESPACE);
		
		
	}

	
}
