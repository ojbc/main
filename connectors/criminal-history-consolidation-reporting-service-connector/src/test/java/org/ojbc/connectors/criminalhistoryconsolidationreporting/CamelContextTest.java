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
package org.ojbc.connectors.criminalhistoryconsolidationreporting;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.io.IOException;
import java.util.concurrent.TimeUnit;

import javax.annotation.Resource;

import org.apache.camel.EndpointInject;
import org.apache.camel.Exchange;
import org.apache.camel.builder.AdviceWithRouteBuilder;
import org.apache.camel.builder.NotifyBuilder;
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
	
	public static final String CXF_OPERATION_NAMESPACE = "http://ojbc.org/Services/WSDL/CriminalHistoryUpdateReportingService/1.0";
    
    @Resource
    private ModelCamelContext context;
	    
    @EndpointInject(uri = "mock:cxf:bean:criminalhistoryConsolidationReportingService")
    protected MockEndpoint criminalHistoryConsolidationReportingServiceMockEndpoint;

    @Before
	public void setUp() throws Exception {

    	//We mock the web service endpoints here
    	context.getRouteDefinition("Criminal_History_Consolidation_Intermediary_Route").adviceWith(context, new AdviceWithRouteBuilder() {
    	    @Override
    	    public void configure() throws Exception {
    	    	
    	    	//We mock the notification broker endpoint
    	    	mockEndpointsAndSkip("cxf:bean:criminalhistoryConsolidationReportingService*");
    	    	
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
		
		criminalHistoryConsolidationReportingServiceMockEndpoint.expectedMessageCount(1);
		
		//Get input directory by resolving property
		String inputDirectoryString = context.resolvePropertyPlaceholders("{{criminalHistoryConsolidation.ConnectorFileDirectory}}") + "/input";
		log.info("Connector input directory:"+ inputDirectoryString);
		assertEquals("/tmp/ojb/demo/criminalHistoryConsolidation/connector/input", inputDirectoryString);
		
        File inputDirectory = new File(inputDirectoryString);
		
		if (inputDirectory.exists())
		{	
			FileUtils.cleanDirectory(inputDirectory);
		}

        testOperation(inputDirectory, "src/test/resources/xmlInstances/Court-Disposition-Recording-Report.xml","ReportCourtDispositionRecording");
        testOperation(inputDirectory, "src/test/resources/xmlInstances/CriminalHistory-Expungement-Report.xml","ReportCriminalHistoryExpungement");
        testOperation(inputDirectory, "src/test/resources/xmlInstances/CriminalHistory-IdentifierUpdate-Report.xml","ReportCriminalHistoryIdentifierUpdate");
        testOperation(inputDirectory, "src/test/resources/xmlInstances/Cycle-Tracking-Identifier-Assignment-Report.xml","ReportCycleTrackingIdentifierAssignment");
        testOperation(inputDirectory, "src/test/resources/xmlInstances/Prosecution-Decision-Recording-Report.xml","ReportProsecutionDecisionRecording");
        testOperation(inputDirectory, "src/test/resources/xmlInstances/CriminalHistory-Consolidation-Report.xml", "ReportCriminalHistoryConsolidation");
        
		if (inputDirectory.exists())
		{	
			FileUtils.cleanDirectory(inputDirectory);
		}

	}

	private void testOperation(File inputDirectory, String fileNameAndPath, String operationName) throws IOException,
			InterruptedException {
		File inputFile = new File(fileNameAndPath);
		assertNotNull(inputFile);
		
		//Kick off the process by copying the process record file to the input directory 
		FileUtils.copyFileToDirectory(inputFile, inputDirectory);

		Thread.sleep(3000);
		
		criminalHistoryConsolidationReportingServiceMockEndpoint.assertIsSatisfied();
		
		Exchange ex = criminalHistoryConsolidationReportingServiceMockEndpoint.getExchanges().get(0);
		assertEquals(operationName,ex.getIn().getHeader("operationName"));
		assertEquals(CXF_OPERATION_NAMESPACE, ex.getIn().getHeader("operationNamespace"));
		
		criminalHistoryConsolidationReportingServiceMockEndpoint.reset();
	}

	
}
