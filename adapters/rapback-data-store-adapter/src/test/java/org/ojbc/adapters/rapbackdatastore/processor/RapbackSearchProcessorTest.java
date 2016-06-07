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
package org.ojbc.adapters.rapbackdatastore.processor;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;

import junit.framework.Assert;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.ojbc.test.util.SAMLTokenTestUtils;
import org.ojbc.util.camel.helper.OJBUtils;
import org.ojbc.util.model.saml.SamlAttribute;
import org.ojbc.util.xml.XmlUtils;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.annotation.DirtiesContext.ClassMode;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.w3c.dom.Document;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {
        "classpath:META-INF/spring/spring-context.xml",
        "classpath:META-INF/spring/dao.xml",
        "classpath:META-INF/spring/properties-context.xml",
        "classpath:META-INF/spring/h2-mock-database-application-context.xml",
        "classpath:META-INF/spring/h2-mock-database-context-rapback-datastore.xml"
		})
@DirtiesContext
public class RapbackSearchProcessorTest {
	private static final Log log = LogFactory.getLog( RapbackSearchProcessorTest.class );

	@Resource 
	RapbackSearchProcessor rapbackSearchProcessor; 
	
	@Before
	public void setUp() throws Exception {
		Assert.assertNotNull(rapbackSearchProcessor);
	}

	@Test
	public void testAgencySuperUserSearchResult() throws Exception {
        Map<SamlAttribute, String> customAttributes = new HashMap<SamlAttribute, String>();
        customAttributes.put(SamlAttribute.EmployerORI, "1234567890");
		customAttributes.put(SamlAttribute.FederationId, "HIJIS:IDP:HCJDC:USER:demouser");
		customAttributes.put(SamlAttribute.EmployerSubUnitName, "Honolulu PD Records and ID Division");
		customAttributes.put(SamlAttribute.EmployeePositionName, "Sworn Supervisors");

        org.apache.cxf.message.Message message = 
        		SAMLTokenTestUtils.createSamlAssertionMessageWithAttributes(customAttributes);
        
        Document civilIdentificationSearchRequest = 
        		XmlUtils.parseFileToDocument(new File("src/test/resources/xmlInstances/"
        				+ "rapbackSearch/OrganizationIdentificationResultsSearchRequest-Civil.xml"));
        
        Document searchResponeDoc = rapbackSearchProcessor.returnRapbackSearchResponse(message, civilIdentificationSearchRequest);
        
        log.info("Civil identification search Response: \n" + OJBUtils.getStringFromDocument(searchResponeDoc));
        IdentificationReportingResponseProcessorTest.assertAsExpected(
        		OJBUtils.getStringFromDocument(searchResponeDoc), 
        		"src/test/resources/xmlInstances/rapbackSearch/CivilIdentficationSearchResponseAgencySuperUser.xml");
	}

	@Test
	public void testSuperUserSearchResult() throws Exception {
		Map<SamlAttribute, String> customAttributes = new HashMap<SamlAttribute, String>();
		customAttributes.put(SamlAttribute.EmployerORI, "HCJDC");
		customAttributes.put(SamlAttribute.FederationId, "HIJIS:IDP:HCJDC:USER:superuser");
		customAttributes.put(SamlAttribute.EmployerSubUnitName, "Honolulu PD Records and ID Division");
		customAttributes.put(SamlAttribute.EmployeePositionName, "Sworn Supervisors");
		
		org.apache.cxf.message.Message message = 
				SAMLTokenTestUtils.createSamlAssertionMessageWithAttributes(customAttributes);
		
		Document civilIdentificationSearchRequest = 
				XmlUtils.parseFileToDocument(new File("src/test/resources/xmlInstances/"
						+ "rapbackSearch/OrganizationIdentificationResultsSearchRequest-Civil.xml"));
		
		Document searchResponeDoc = rapbackSearchProcessor.returnRapbackSearchResponse(message, civilIdentificationSearchRequest);
		
		log.info("Civil identification search Response: \n" + OJBUtils.getStringFromDocument(searchResponeDoc));
		IdentificationReportingResponseProcessorTest.assertAsExpected(
				OJBUtils.getStringFromDocument(searchResponeDoc), 
				"src/test/resources/xmlInstances/rapbackSearch/CivilIdentficationSearchResponseSuperUser.xml");
	}
	
}
