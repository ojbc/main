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
package org.ojbc.connectors.dispositionreporting;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.io.File;

import javax.annotation.Resource;

import org.apache.camel.EndpointInject;
import org.apache.camel.Exchange;
import org.apache.camel.builder.AdviceWith;
import org.apache.camel.builder.AdviceWithRouteBuilder;
import org.apache.camel.component.mock.MockEndpoint;
import org.apache.camel.model.ModelCamelContext;
import org.apache.camel.test.spring.junit5.CamelSpringBootTest;
import org.apache.commons.io.FileUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.junit.After;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.annotation.DirtiesContext.ClassMode;
import org.springframework.test.context.ActiveProfiles;

@CamelSpringBootTest
@SpringBootTest(classes=DispositionReportingConnectorApplication.class)
@ActiveProfiles("dev")
@DirtiesContext(classMode = ClassMode.AFTER_EACH_TEST_METHOD) 
public class CamelContextTest {

    private static final Log log = LogFactory.getLog( CamelContextTest.class );
	
	public static final String CXF_OPERATION_NAME = "ReportDisposition";
	public static final String CXF_OPERATION_NAMESPACE = "http://ojbc.org/Services/WSDL/DispositionReportingService/1.0";
    
    @Resource
    private ModelCamelContext context;
	    
    @EndpointInject(value = "mock:cxf:bean:dispositionReportingService")
    protected MockEndpoint dispositionReportingServiceMockEndpoint;

    @BeforeEach
	public void setUp() throws Exception {

    	//We mock the web service endpoints here
    	AdviceWith.adviceWith(context, "Disposition_Connector_Intermediary_Route", route -> {
    		route.weaveById("intermediaryEndpoint").replace().to(dispositionReportingServiceMockEndpoint);
    	});
    	
		context.start();		
	}

	@After
	public void tearDown() throws Exception {
	}
	
	@Test
	public void contextStartup() throws Exception {
		assertTrue(true);
		
		dispositionReportingServiceMockEndpoint.expectedMessageCount(1);
		
		//Get input directory by resolving property
		String inputDirectoryString = context.resolvePropertyPlaceholders("{{dispositionReporting.ConnectorFileDirectory}}") + "/input";
		log.info("Connector input directory:"+ inputDirectoryString);
		assertEquals("/tmp/ojb/demo/dispositions/connector/input", inputDirectoryString);
		
        File inputDirectory = new File(inputDirectoryString);
		
		if (inputDirectory.exists())
		{	
			FileUtils.cleanDirectory(inputDirectory);
		}
		

        File inputFile = new File("src/test/resources/xmlInstances/Disposition_Report.xml");
		assertNotNull(inputFile);
		
		//Kick off the process by copying the process record file to the input directory 
		FileUtils.copyFileToDirectory(inputFile, inputDirectory);

		Thread.sleep(5000);
		
		dispositionReportingServiceMockEndpoint.assertIsSatisfied();
		
		Exchange ex = dispositionReportingServiceMockEndpoint.getExchanges().get(0);
		assertEquals(ex.getIn().getHeader("operationName"), CXF_OPERATION_NAME);
		assertEquals(ex.getIn().getHeader("operationNamespace"), CXF_OPERATION_NAMESPACE);
		
		
	}

	
}
