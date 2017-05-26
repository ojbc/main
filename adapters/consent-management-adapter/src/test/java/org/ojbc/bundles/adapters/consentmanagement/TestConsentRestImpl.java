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
package org.ojbc.bundles.adapters.consentmanagement;

import static org.junit.Assert.*;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.logging.Logger;

import javax.annotation.Resource;

import org.apache.camel.model.ModelCamelContext;
import org.apache.camel.test.spring.CamelSpringJUnit4ClassRunner;
import org.apache.camel.test.spring.UseAdviceWith;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.codehaus.jackson.JsonFactory;
import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.JsonParser;
import org.codehaus.jackson.JsonToken;
import org.codehaus.jackson.map.ObjectMapper;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.ojbc.bundles.adapters.consentmanagement.dao.ConsentManagementDAOImpl;
import org.ojbc.bundles.adapters.consentmanagement.model.Consent;
import org.ojbc.bundles.adapters.consentmanagement.util.ConsentManagementAdapterTestUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.web.client.RestTemplate;

@UseAdviceWith	// NOTE: this causes Camel contexts to not start up automatically
@RunWith(CamelSpringJUnit4ClassRunner.class)
@ContextConfiguration(locations={
		"classpath:META-INF/spring/properties-context.xml",
		"classpath:META-INF/spring/camel-context.xml",
		"classpath:META-INF/spring/test-beans.xml",
		"classpath:META-INF/spring/dao.xml",
        "classpath:META-INF/spring/h2-mock-database-application-context.xml",
        "classpath:META-INF/spring/h2-mock-database-context-consent-management-datastore.xml",
		"classpath:META-INF/spring/cxf-endpoints.xml"
		})		
@DirtiesContext
public class TestConsentRestImpl {
	
	private static final String URI_BASE = "https://localhost:9898/consentService/";
	private static final String CONSENT_UPDATE_URI = URI_BASE + "consent";
	private static final String CONSENT_SEARCH_URI = URI_BASE + "search";

	private Logger log = Logger.getLogger(TestConsentRestImpl.class.getName());
	
    @Resource
    private ModelCamelContext context;

    @Resource
    private RestTemplate restTemplate;
    
	@Autowired
	private ConsentManagementDAOImpl consentManagementDAOImpl;

	private Consent consent1;
	private Consent consent2;
    
	@Before
	public void setUp() throws Exception {
    	context.start();
		consent1 = ConsentManagementAdapterTestUtils.returnConsent(null, "B1234", "N1234", LocalDate.parse("1975-02-01"), "TestFirst", "F", "TestLast", "TestMiddle", LocalDateTime.now());
		consent1.setConsentId(consentManagementDAOImpl.saveConsentDecision(consent1));
		consent2 = ConsentManagementAdapterTestUtils.returnConsent(null, "B2345", "N234", LocalDate.parse("1974-03-01"), "TestFirst1", "M", "TestLast1", "TestMiddle1", LocalDateTime.now());
		consent2.setConsentId(consentManagementDAOImpl.saveConsentDecision(consent2));
	}
	
	@After
	public void tearDown() throws Exception {
		consentManagementDAOImpl.deleteAllConsentRecords();
	}
	
	@Test
	public void testConsentViaAPI() throws Exception {
		
		CloseableHttpClient httpClient = HttpClients.createDefault();
		HttpPost postRequest = new HttpPost(CONSENT_UPDATE_URI);
		
		consent1.setConsenterUserID("user ID consent");
		consent1.setConsentUserFirstName("userFirstName");
		consent1.setConsentUserLastName("userLastName");
		consent1.setConsentDocumentControlNumber("control");
		consent1.setConsentDecisionTypeID(1);
		
		ObjectMapper om = new ObjectMapper();
		String json = om.writeValueAsString(consent1);
		//log.info(json);
		StringEntity jsonS = new StringEntity(json);
		jsonS.setContentType("application/json");
		postRequest.setEntity(jsonS);
		
		CloseableHttpResponse response = httpClient.execute(postRequest);
		assertEquals(200, response.getStatusLine().getStatusCode());
		
		BufferedReader br = new BufferedReader(new InputStreamReader((response.getEntity().getContent())));
		assertNull(br.readLine());
		
		response.close();
		
		Consent updatedConsent = consentManagementDAOImpl.returnConsentRecordfromId(consent1.getConsentId());
		
		assertConsentBasePropertiesSame(consent1, updatedConsent);
		assertConsentUpdatedPropertiesSame(consent1, updatedConsent);
		assertNotNull(updatedConsent.getConsentDecisionTimestamp());
		
	}
	
	@Test
	public void testSearchViaAPI() throws Exception {
		
		CloseableHttpClient httpClient = HttpClients.createDefault();
		HttpGet getRequest = new HttpGet(CONSENT_SEARCH_URI);
		getRequest.addHeader("accept", "application/json");
		CloseableHttpResponse response = httpClient.execute(getRequest);
		assertEquals(200, response.getStatusLine().getStatusCode());
		
		BufferedReader br = new BufferedReader(new InputStreamReader((response.getEntity().getContent())));
		
		StringBuffer content = new StringBuffer();
		String output = null;
		
		while ((output = br.readLine()) != null) {
			content.append(output);
		}
		
		//log.info(content.toString());
		
		JsonFactory factory = new JsonFactory();
		JsonParser parser = factory.createJsonParser(content.toString());
		
		assertEquals(JsonToken.START_ARRAY, parser.nextToken());
		assertEquals(JsonToken.START_OBJECT, parser.nextToken());
		
		assertEquals(JsonToken.FIELD_NAME, parser.nextToken());
		assertEquals("consentId", parser.getCurrentName());
		parser.nextToken(); // we don't assert the value of the consent id because that will depend on context, how many times test has been run, etc.
		
		assertNextJsonValue(parser, "consentDecisionTypeID", JsonToken.VALUE_NUMBER_INT, "0");
		assertNextJsonValue(parser, "bookingNumber", JsonToken.VALUE_STRING, "B1234");
		assertNextJsonValue(parser, "nameNumber", JsonToken.VALUE_STRING, "N1234");
		assertNextJsonValue(parser, "personFirstName", JsonToken.VALUE_STRING, "TestFirst");
		assertNextJsonValue(parser, "personMiddleName", JsonToken.VALUE_STRING, "TestMiddle");
		assertNextJsonValue(parser, "personLastName", JsonToken.VALUE_STRING, "TestLast");
		assertNextJsonValue(parser, "personGender", JsonToken.VALUE_STRING, "F");
		assertNextJsonValue(parser, "personDOBString", JsonToken.VALUE_STRING, "1975-02-01");
		assertNextJsonValue(parser, "consenterUserID", JsonToken.VALUE_NULL, null);
		assertNextJsonValue(parser, "consentUserFirstName", JsonToken.VALUE_NULL, null);
		assertNextJsonValue(parser, "consentUserLastName", JsonToken.VALUE_NULL, null);
		assertNextJsonValue(parser, "consentDocumentControlNumber", JsonToken.VALUE_NULL, null);
		assertEquals(JsonToken.END_OBJECT, parser.nextToken());
		assertEquals(JsonToken.START_OBJECT, parser.nextToken());
		// no need to assert the rest

		response.close();
		
	}

	@Test
	public void testConsent() throws Exception
	{
		
		consent1.setConsenterUserID("user ID consent");
		consent1.setConsentUserFirstName("userFirstName");
		consent1.setConsentUserLastName("userLastName");
		consent1.setConsentDocumentControlNumber("control");
		consent1.setConsentDecisionTypeID(1);
		
		restTemplate.postForLocation(CONSENT_UPDATE_URI, consent1);
		
		Consent updatedConsent = consentManagementDAOImpl.returnConsentRecordfromId(consent1.getConsentId());
		
		assertConsentBasePropertiesSame(consent1, updatedConsent);
		assertConsentUpdatedPropertiesSame(consent1, updatedConsent);
		assertNotNull(updatedConsent.getConsentDecisionTimestamp());
		
	}
	
	@Test
	public void testSearch() throws Exception
	{
		
		Consent[] response = restTemplate.getForObject(CONSENT_SEARCH_URI, Consent[].class);
		
		if (response != null)
		{	
			log.info(response.toString());
		}
		
		Consent c1 = null;
		Consent c2 = null;
		
		for (Consent c : response) {
			if (c.getConsentId() == consent1.getConsentId()) {
				c1 = c;
			} else if (c.getConsentId() == consent2.getConsentId()) {
				c2 = c;
			} else {
				throw new IllegalStateException("Unexpected consent ID " + c.getConsentId());
			}
		}
		
		assertNotNull(c1);
		assertNotNull(c2);
		
		assertConsentBasePropertiesSame(consent1, c1);
		assertConsentBasePropertiesSame(consent2, c2);
		
	}
	
	private static final void assertConsentBasePropertiesSame(Consent c1, Consent c2) {
		assertEquals(c1.getBookingNumber(), c2.getBookingNumber());
		assertEquals(c1.getNameNumber(), c2.getNameNumber());
		assertEquals(c1.getPersonDOBString(), c2.getPersonDOBString());
		assertEquals(c1.getPersonFirstName(), c2.getPersonFirstName());
		assertEquals(c1.getPersonMiddleName(), c2.getPersonMiddleName());
		assertEquals(c1.getPersonLastName(), c2.getPersonLastName());
		assertEquals(c1.getPersonGender(), c2.getPersonGender());
	}

	private static void assertConsentUpdatedPropertiesSame(Consent c1, Consent c2) {
		assertEquals(c1.getConsentDecisionTypeID(), c2.getConsentDecisionTypeID());
		assertEquals(c1.getConsenterUserID(), c2.getConsenterUserID());
		assertEquals(c1.getConsentUserFirstName(), c2.getConsentUserFirstName());
		assertEquals(c1.getConsentUserLastName(), c2.getConsentUserLastName());
		assertEquals(c1.getConsentDocumentControlNumber(), c2.getConsentDocumentControlNumber());
	}
	
	private static final void assertNextJsonValue(JsonParser parser, String fieldName, JsonToken type, String value) throws IOException, JsonParseException {
		assertEquals(JsonToken.FIELD_NAME, parser.nextToken());
		assertEquals(fieldName, parser.getCurrentName());
		assertEquals(type, parser.nextToken());
		if (value != null) {
			assertEquals(value, parser.getText());
		}
	}
	
}
