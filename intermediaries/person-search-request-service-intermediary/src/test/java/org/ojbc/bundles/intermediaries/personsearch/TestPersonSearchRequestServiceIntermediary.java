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
package org.ojbc.bundles.intermediaries.personsearch;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.io.File;

import javax.annotation.Resource;

import org.apache.camel.EndpointInject;
import org.apache.camel.Exchange;
import org.apache.camel.Produce;
import org.apache.camel.ProducerTemplate;
import org.apache.camel.builder.AdviceWith;
import org.apache.camel.component.mock.MockEndpoint;
import org.apache.camel.model.ModelCamelContext;
import org.apache.camel.support.DefaultExchange;
import org.apache.camel.test.spring.junit5.CamelSpringBootTest;
import org.apache.camel.test.spring.junit5.UseAdviceWith;
import org.apache.commons.io.FileUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.http.Consts;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.annotation.DirtiesContext.ClassMode;
import org.springframework.test.context.ActiveProfiles;


@CamelSpringBootTest
@SpringBootTest(classes=PersonSearchIntermediaryApplication.class)
@ActiveProfiles("dev")
@DirtiesContext(classMode = ClassMode.AFTER_EACH_TEST_METHOD)
@UseAdviceWith
public class TestPersonSearchRequestServiceIntermediary {
	
	private static final Log log = LogFactory.getLog( TestPersonSearchRequestServiceIntermediary.class );
	
    @Resource
    private ModelCamelContext context;
    
    @Produce
    protected ProducerTemplate template;
    	
    @EndpointInject(value = "mock:maxRecordsProcessorMock")
    protected MockEndpoint maxRecordsProcessorMock;

    
    @Test
    public void testApplicationStartup() {
    	assertTrue(true);
    }	
    
	@BeforeEach
	public void setUp() throws Exception {

		AdviceWith.adviceWith(context, "processFederatedResponseRoute", 
				route-> route.interceptSendToEndpoint("direct:sendMergeMessageResponse")
					.to(maxRecordsProcessorMock).stop());
    	
    	context.start();
	}
    
    @Test
    public void testPersonSearchMaxRecords() throws Exception {
    	
    	maxRecordsProcessorMock.expectedMessageCount(1);

    	//Create a new exchange
    	Exchange senderExchange = new DefaultExchange(context);

	    //Read the firearm search request file from the file system
	    File inputFile = new File("src/test/resources/xmlInstances/personSearchResults/personSearchResultsLarge.xml");
	    String inputStr = FileUtils.readFileToString(inputFile, Consts.UTF_8);
	    
	    //Set it as the message message body
	    senderExchange.getIn().setBody(inputStr);
	    
	    //Send the one-way exchange.  Using template.send will send an one way message
		template.send("direct:processFederatedResponse", senderExchange);

    	
    	maxRecordsProcessorMock.assertIsSatisfied();
    	String errorMessage = (String)maxRecordsProcessorMock.getReceivedExchanges().get(0).getIn().getBody();
    	log.debug("Here is the error response: " + errorMessage);
    	assertEquals("<exc:EntityMergeResultMessage xmlns:exc=\"http://nij.gov/IEPD/Exchange/EntityMergeResultMessage/1.0\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xmlns:srer=\"http://ojbc.org/IEPD/Extensions/SearchRequestErrorReporting/1.0\" xmlns:intel=\"http://niem.gov/niem/domains/intelligence/2.1\" xmlns:srm=\"http://ojbc.org/IEPD/Extensions/SearchResultsMetadata/1.0\"> <exc:SearchResultsMetadataCollection> 	<srm:SearchResultsMetadata> 		<srer:SearchRequestError> 			<srer:ErrorText>The search results contained too many records, please refine your search.  The maximum combined number of records that will display is 200.<br />The Criminal History search results contained 336 records.<br /></srer:ErrorText> 			<intel:SystemName>All Systems</intel:SystemName> 		</srer:SearchRequestError> 	</srm:SearchResultsMetadata> </exc:SearchResultsMetadataCollection></exc:EntityMergeResultMessage>", errorMessage);
    }
    
}
