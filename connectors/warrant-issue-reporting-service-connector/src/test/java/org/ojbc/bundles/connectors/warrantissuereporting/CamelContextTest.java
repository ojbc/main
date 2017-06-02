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
package org.ojbc.bundles.connectors.warrantissuereporting;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.io.File;

import javax.annotation.Resource;

import org.apache.camel.EndpointInject;
import org.apache.camel.Exchange;
import org.apache.camel.Produce;
import org.apache.camel.ProducerTemplate;
import org.apache.camel.builder.AdviceWithRouteBuilder;
import org.apache.camel.component.mock.MockEndpoint;
import org.apache.camel.model.ModelCamelContext;
import org.apache.camel.test.spring.CamelSpringJUnit4ClassRunner;
import org.apache.commons.io.FileUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
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
	
    @Resource
    private ModelCamelContext context;
	
    @Produce
    protected ProducerTemplate template;
    
    @EndpointInject(uri = "mock:cxf:bean:warrantIssueReportingService")
    protected MockEndpoint warrantIssueReportingServiceMockEndpoint;
	
    private static final String CXF_OPERATION_NAMESPACE = "http://ojbc.org/Services/WSDL/WarrantIssuedReportingService/1.0";
    
    @Before
	public void setUp() throws Exception {

    	//We mock the web service endpoints here
    	context.getRouteDefinition("WarrantIssueReportingServiceHandlerRoute").adviceWith(context, new AdviceWithRouteBuilder() {
    	    @Override
    	    public void configure() throws Exception {
    	    	
    	    	//We mock the notification broker endpoint
    	    	mockEndpointsAndSkip("cxf:bean:warrantIssueReportingService*");
    	    	
    	    }              
    	});
    	
		context.start();		
	}

	@Test
	public void testCaseFilingDecisionReport() throws Exception {
		assertTrue(true);
		
		warrantIssueReportingServiceMockEndpoint.expectedMessageCount(1);
		
		//Get input directory by resolving property
		String inputDirectoryString = context.resolvePropertyPlaceholders("{{warrantIssueReporting.ConnectorFileDirectory}}") + "/input";
		log.info("Connector input directory:"+ inputDirectoryString);
		assertEquals("/tmp/ojb/warrantIssueReporting/input", inputDirectoryString);
		
        File inputDirectory = new File(inputDirectoryString);
		
		if (inputDirectory.exists())
		{	
			FileUtils.cleanDirectory(inputDirectory);
		}

        File inputFile = new File("src/test/resources/xmlInstances/WarrantIssuedReport.xml");
		assertNotNull(inputFile);
		
		//Kick off the process by copying the fileto the input directory 
		FileUtils.copyFileToDirectory(inputFile, inputDirectory);

		Thread.sleep(3000);
		
		warrantIssueReportingServiceMockEndpoint.assertIsSatisfied();
		
		Exchange ex = warrantIssueReportingServiceMockEndpoint.getExchanges().get(0);
		assertEquals("ReportWarrantIssued", ex.getIn().getHeader("operationName"));
		assertEquals(CXF_OPERATION_NAMESPACE, ex.getIn().getHeader("operationNamespace"));
	
		warrantIssueReportingServiceMockEndpoint.reset();
		warrantIssueReportingServiceMockEndpoint.expectedMessageCount(1);

        inputFile = new File("src/test/resources/xmlInstances/WarrantAcceptedReport.xml");
		assertNotNull(inputFile);
		
		//Kick off the process by copying the fileto the input directory 
		warrantIssueReportingServiceMockEndpoint.reset();
		warrantIssueReportingServiceMockEndpoint.expectedMessageCount(1);
		
		FileUtils.copyFileToDirectory(inputFile, inputDirectory);

		Thread.sleep(3000);
		
		warrantIssueReportingServiceMockEndpoint.assertIsSatisfied();
		
		ex = warrantIssueReportingServiceMockEndpoint.getExchanges().get(0);
		assertEquals("ReportWarrantAccepted", ex.getIn().getHeader("operationName"));
		assertEquals(CXF_OPERATION_NAMESPACE, ex.getIn().getHeader("operationNamespace"));

		warrantIssueReportingServiceMockEndpoint.reset();
		warrantIssueReportingServiceMockEndpoint.expectedMessageCount(1);
		
        inputFile = new File("src/test/resources/xmlInstances/WarrantModificationReport.xml");
		assertNotNull(inputFile);
		
		//Kick off the process by copying the fileto the input directory 
		FileUtils.copyFileToDirectory(inputFile, inputDirectory);

		Thread.sleep(3000);
		
		warrantIssueReportingServiceMockEndpoint.assertIsSatisfied();
		
		ex = warrantIssueReportingServiceMockEndpoint.getExchanges().get(0);
		assertEquals("ReportWarrantModification", ex.getIn().getHeader("operationName"));
		assertEquals(CXF_OPERATION_NAMESPACE, ex.getIn().getHeader("operationNamespace"));

		warrantIssueReportingServiceMockEndpoint.reset();
		warrantIssueReportingServiceMockEndpoint.expectedMessageCount(1);

        inputFile = new File("src/test/resources/xmlInstances/WarrantRejectedReport.xml");
		assertNotNull(inputFile);
		
		//Kick off the process by copying the fileto the input directory 
		FileUtils.copyFileToDirectory(inputFile, inputDirectory);

		Thread.sleep(3000);
		
		warrantIssueReportingServiceMockEndpoint.assertIsSatisfied();
		
		ex = warrantIssueReportingServiceMockEndpoint.getExchanges().get(0);
		assertEquals("ReportWarrantRejected", ex.getIn().getHeader("operationName"));
		assertEquals(CXF_OPERATION_NAMESPACE, ex.getIn().getHeader("operationNamespace"));

		warrantIssueReportingServiceMockEndpoint.reset();
		warrantIssueReportingServiceMockEndpoint.expectedMessageCount(1);

        inputFile = new File("src/test/resources/xmlInstances/WarrantCancelledReport.xml");
		assertNotNull(inputFile);
		
		//Kick off the process by copying the fileto the input directory 
		FileUtils.copyFileToDirectory(inputFile, inputDirectory);

		Thread.sleep(3000);
		
		warrantIssueReportingServiceMockEndpoint.assertIsSatisfied();
		
		ex = warrantIssueReportingServiceMockEndpoint.getExchanges().get(0);
		assertEquals("ReportWarrantCancelled", ex.getIn().getHeader("operationName"));
		assertEquals(CXF_OPERATION_NAMESPACE, ex.getIn().getHeader("operationNamespace"));

	}
	
}
