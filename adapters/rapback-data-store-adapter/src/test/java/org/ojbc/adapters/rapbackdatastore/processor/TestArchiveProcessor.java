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
import static org.junit.Assert.assertNotNull;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.ojbc.adapters.rapbackdatastore.dao.RapbackDAOImpl;
import org.ojbc.util.xml.XmlUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.DirtiesContext;
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
public class TestArchiveProcessor {

	@SuppressWarnings("unused")
	private final Log log = LogFactory.getLog(this.getClass());
    
	@Autowired
	RapbackDAOImpl rapbackDAO;

	@Before
	public void setUp() throws Exception {
		assertNotNull(rapbackDAO);
	}

	@Test
	@DirtiesContext
	public void testProcessArchiveResult() throws Exception
	{
		ArchiveProcessor archiveProcessor = new ArchiveProcessor();
		
		archiveProcessor.setRapbackDAO(rapbackDAO);
		
		Document result = archiveProcessor.processArchiveResult("000001820140729014008339997", "System Name");
		
		XmlUtils.printNode(result);
		
		assertEquals("true",XmlUtils.xPathStringSearch(result, "/irm-resp-doc:IdentificationResultsModificationResponse/irm-resp-ext:IdentificationResultsModificationIndicator"));
		assertEquals("System Name",XmlUtils.xPathStringSearch(result, "/irm-resp-doc:IdentificationResultsModificationResponse/nc30:SystemName"));
	}
	
	@Test
	@DirtiesContext
	public void testProcessArchiveError() throws Exception
	{
		ArchiveProcessor archiveProcessor = new ArchiveProcessor();
		
		archiveProcessor.setRapbackDAO(rapbackDAO);
		
		Document result = archiveProcessor.processArchiveError(new Exception("Error Text"), "System Name");
		
		XmlUtils.printNode(result);
		
		assertEquals("Error Text",XmlUtils.xPathStringSearch(result, "/irm-resp-doc:IdentificationResultsModificationResponse/irm-rm:IdentificationResultsModificationResponseMetadata/irm-err-rep:IdentificationResultsModificationRequestError/irm-err-rep:ErrorText"));
		assertEquals("System Name",XmlUtils.xPathStringSearch(result, "/irm-resp-doc:IdentificationResultsModificationResponse/irm-rm:IdentificationResultsModificationResponseMetadata/irm-err-rep:IdentificationResultsModificationRequestError/nc30:SystemName"));
	}

}
