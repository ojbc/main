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
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.ojbc.util.camel.helper.OJBUtils;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ContextConfiguration;
import org.w3c.dom.Document;

@UseAdviceWith	// NOTE: this causes Camel contexts to not start up automatically
@RunWith(CamelSpringJUnit4ClassRunner.class)
@ContextConfiguration(locations={
		"classpath:META-INF/spring/camel-context.xml", 
		"classpath:META-INF/spring/cxf-endpoints.xml",
		"classpath:META-INF/spring/dao.xml",
        "classpath:META-INF/spring/h2-mock-database-application-context.xml",
        "classpath:META-INF/spring/h2-mock-database-context-warrant-repository.xml",
		"classpath:META-INF/spring/properties-context.xml"})
@DirtiesContext
public class CamelContextTest {
	private final Log log = LogFactory.getLog( CamelContextTest.class );

    @Resource
    private ModelCamelContext context;
	
    @Resource  
    private DataSource dataSource;  

    @Produce
    protected ProducerTemplate template;
    
    @EndpointInject(uri = "mock:result")
    protected MockEndpoint resultEndpoint;

    @Before
    public void setUp() throws Exception {
        // Advise the Request Service endpoint and replace it
        // with a mock endpoint. We then will test this mock endpoint to see 
        // if it gets the proper payload.
        context.getRouteDefinition("sendWarrantModificationRoute")
                .adviceWith(context, new AdviceWithRouteBuilder() {
                    @Override
                    public void configure() throws Exception {
                        // The line below allows us to bypass CXF and send a
                        // message directly into the route
                        interceptSendToEndpoint(
                                "warrantModificationRequestIntermediaryEndpoint")
                                .to("mock:result")
                                .log("Called Warrant Modificatio Request Intermediary Service Endpoint")
                                .stop();
                    }
                });

        context.start();
    }

    @Test
    public void testDatasource() throws Exception {
		Connection conn = dataSource.getConnection();
		ResultSet rs = conn.createStatement().executeQuery("select count(*) as rowcount from Warrant");
		assertTrue(rs.next());
		assertEquals(1,rs.getInt("rowcount"));
    }
    
    @Test
    public void testApplicationStartup() throws Exception {
    	assertTrue(true);
    	context.start();
    	
    	Thread.sleep(2000);
    	
		Exchange receivedExchange = resultEndpoint.getExchanges().get(0);
		Document bodyDocument = receivedExchange.getIn().getBody(Document.class);
		String bodyString = OJBUtils.getStringFromDocument(bodyDocument);
		log.info("body: \n" + bodyString);
		

    }	
    
}

