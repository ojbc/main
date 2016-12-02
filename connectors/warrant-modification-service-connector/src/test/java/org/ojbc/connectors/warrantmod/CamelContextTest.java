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

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.sql.Connection;
import java.sql.ResultSet;

import javax.annotation.Resource;
import javax.sql.DataSource;

import org.apache.camel.EndpointInject;
import org.apache.camel.Exchange;
import org.apache.camel.Produce;
import org.apache.camel.ProducerTemplate;
import org.apache.camel.builder.AdviceWithRouteBuilder;
import org.apache.camel.component.mock.MockEndpoint;
import org.apache.camel.model.ModelCamelContext;
import org.apache.camel.test.spring.CamelSpringJUnit4ClassRunner;
import org.apache.camel.test.spring.UseAdviceWith;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.junit.After;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.ojbc.test.util.SoapMessageUtils;
import org.ojbc.test.util.XmlTestUtils;
import org.ojbc.util.camel.helper.OJBUtils;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ContextConfiguration;
import org.w3c.dom.Document;

@UseAdviceWith	// NOTE: this causes Camel contexts to not start up automatically
@RunWith(CamelSpringJUnit4ClassRunner.class)
@ContextConfiguration(locations={
		"classpath:META-INF/spring/camel-context.xml", 
		"classpath:META-INF/spring/spring-context.xml", 
		"classpath:META-INF/spring/cxf-endpoints.xml",
		"classpath:META-INF/spring/dao.xml",
        "classpath:META-INF/spring/h2-mock-database-application-context.xml",
        "classpath:META-INF/spring/h2-mock-database-context-warrant-repository.xml",
		"classpath:META-INF/spring/properties-context.xml"})
@DirtiesContext
//TODO Fix the tests after talking with Andrew. 
@Ignore
public class CamelContextTest {
	private final Log log = LogFactory.getLog( CamelContextTest.class );

    @Resource
    private ModelCamelContext context;
	
    @Resource  
    private DataSource dataSource;  

    @Produce
    protected ProducerTemplate template;
    
    private Connection conn;
    
    @EndpointInject(uri = "mock:cxf:bean:warrantModRequestService")
    protected MockEndpoint warrantModRequestServiceMock;

    @Before
    public void setUp() throws Exception {
		conn = dataSource.getConnection();
        // Advise the Request Service endpoint and replace it
        // with a mock endpoint. We then will test this mock endpoint to see 
        // if it gets the proper payload.
        context.getRouteDefinition("checkDatabaseForAcceptedWarrantsRoute")
        .adviceWith(context, new AdviceWithRouteBuilder() {
            @Override
            public void configure() throws Exception {
                // The line below allows us to bypass CXF and send a
                // message directly into the route
            	replaceFromWith("direct:start");
            }
        });
        
        context.getRouteDefinition("sendWarrantModificationRoute")
                .adviceWith(context, new AdviceWithRouteBuilder() {
                    @Override
                    public void configure() throws Exception {
                        // The line below allows us to bypass CXF and send a
                        // message directly into the route
                    	
                    	mockEndpointsAndSkip("cxf:bean:warrantModRequestService*");
                    }
                });
        
        context.getRouteDefinition("warrantModConnectorResultsHandler_route")
        .adviceWith(context, new AdviceWithRouteBuilder() {
            @Override
            public void configure() throws Exception {
                // The line below allows us to bypass CXF and send a
                // message directly into the route
            	replaceFromWith("direct:sendWarrantModificationResponse");
            }
        });
        
        context.start();
    }
    
	@After
	public void tearDown() throws Exception {
		conn.close();
	}

    @Test
    public void testDatasource() throws Exception {
		Connection conn = dataSource.getConnection();
		ResultSet rs = conn.createStatement().executeQuery("select count(*) as rowcount from Warrant");
		assertTrue(rs.next());
		assertEquals(1,rs.getInt("rowcount"));
		conn.close(); 
    }
    
    @Test
    public void testWarrantModificationRequestSendingRoute() throws Exception {
    	assertTrue(true);
    	Thread.sleep(2000);
    	
		ResultSet rs = conn.createStatement().executeQuery("select count(*) as rowcount from Warrant where StateWarrantRepositoryID is not null AND warrantModRequestSent = false");
		assertTrue(rs.next());
		assertEquals(1,rs.getInt("rowcount"));
		
    	template.sendBody("direct:start", null);
    	
		Exchange receivedExchange = warrantModRequestServiceMock.getExchanges().get(0);
		Document bodyDocument = receivedExchange.getIn().getBody(Document.class);
		String bodyString = OJBUtils.getStringFromDocument(bodyDocument);
		log.info("body: \n" + bodyString);
		
		XmlTestUtils.compareDocs("src/test/resources/xmlInstances/warrantModRequest.xml", bodyString);

		ResultSet rsAfter = conn.createStatement().executeQuery("select count(*) as rowcount from Warrant where StateWarrantRepositoryID is not null AND warrantModRequestSent = false");
		assertTrue(rsAfter.next());
		assertEquals(0, rsAfter.getInt("rowcount"));
		
    }	
    
    @Test
    public void testWarrantModConnectorResultsHandlerRoute() throws Exception {
		ResultSet rsBefore = conn.createStatement().executeQuery("select WarrantModResponseReceived from Warrant where warrantId = 1");
		assertTrue(rsBefore.next());
		assertFalse(rsBefore.getBoolean("WarrantModResponseReceived"));
		
		Exchange senderExchange = SoapMessageUtils.createSenderExchange("src/test/resources/xmlInstances/WarrantModificationResponse.xml", context);

    	template.send("direct:sendWarrantModificationResponse", senderExchange);
    	
		ResultSet rsAfter = conn.createStatement().executeQuery("select WarrantModResponseReceived from Warrant where warrantId = 1");
		assertTrue(rsAfter.next());
		assertTrue(rsAfter.getBoolean("WarrantModResponseReceived"));
		
    	
    }	
}

