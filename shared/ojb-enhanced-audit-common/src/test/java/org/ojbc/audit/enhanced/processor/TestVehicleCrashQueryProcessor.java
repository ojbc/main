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
package org.ojbc.audit.enhanced.processor;

import static org.junit.Assert.assertEquals;

import java.io.File;

import javax.annotation.Resource;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.ojbc.audit.enhanced.dao.EnhancedAuditDAO;
import org.ojbc.audit.enhanced.dao.model.VehicleCrashQueryResponse;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.w3c.dom.Document;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {
        "classpath:META-INF/spring/spring-context.xml"})
@DirtiesContext
public class TestVehicleCrashQueryProcessor {

	private static final Log log = LogFactory.getLog(TestVehicleCrashQueryProcessor.class);
	
	@Resource
	private EnhancedAuditDAO enhancedAuditDao;
	
	@Test
	public void testProcessPersonQueryWarrantRequest() throws Exception
	{
		AuditTestUtils.saveQueryRequest(enhancedAuditDao, "123456645");
		
		VehicleCrashResponseSQLProcessor vehicleCrashResponseSQLProcessor = new VehicleCrashResponseSQLProcessor();
		UserInfoSQLProcessor userInfoSQLProcessor = new UserInfoSQLProcessor();
		
		userInfoSQLProcessor.setEnhancedAuditDAO(enhancedAuditDao);
		vehicleCrashResponseSQLProcessor.setEnhancedAuditDAO(enhancedAuditDao);
		
        File inputFile = new File("src/test/resources/xmlInstances/VehicleCrashQueryResponse.xml");

        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        dbf.setNamespaceAware(true);
        DocumentBuilder db = dbf.newDocumentBuilder();
        Document document = db.parse(inputFile);
		
		VehicleCrashQueryResponse vehicleCrashQueryResponse = vehicleCrashResponseSQLProcessor.processVehicleCrashResponse(document);
		
		log.info(vehicleCrashQueryResponse.toString());

		assertEquals("Vehicle Crash", vehicleCrashQueryResponse.getSystemName());
		assertEquals(1, vehicleCrashQueryResponse.getCrashVehicles().size());
		
		assertEquals("999998888", vehicleCrashQueryResponse.getCrashVehicles().get(0).getVehicleIdentificationNumber());
		assertEquals("INTERNATIONAL", vehicleCrashQueryResponse.getCrashVehicles().get(0).getVehicleMake());
		assertEquals("2014", vehicleCrashQueryResponse.getCrashVehicles().get(0).getVehicleModel());
		
		vehicleCrashResponseSQLProcessor.auditVehicleCrashResponse(document, "123456645");
		
		
	}
	
}
