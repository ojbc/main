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

import static org.junit.Assert.*;

import java.io.File;
import java.util.List;

import javax.annotation.Resource;

import org.apache.camel.model.ModelCamelContext;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.ojbc.adapters.rapbackdatastore.RapbackDataStoreAdapterConstants;
import org.ojbc.adapters.rapbackdatastore.dao.RapbackDAOImpl;
import org.ojbc.intermediaries.sn.dao.SubscriptionSearchQueryDAO;
import org.ojbc.intermediaries.sn.dao.rapback.SubsequentResults;
import org.ojbc.util.xml.XmlUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.w3c.dom.Document;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {
        "classpath:META-INF/spring/camel-context.xml",
        "classpath:META-INF/spring/spring-context.xml",
        "classpath:META-INF/spring/cxf-endpoints.xml",      
        "classpath:META-INF/spring/properties-context.xml",
        "classpath:META-INF/spring/dao.xml",
        "classpath:META-INF/spring/h2-mock-database-application-context.xml",
        "classpath:META-INF/spring/h2-mock-database-context-rapback-datastore.xml",
        "classpath:META-INF/spring/subscription-management-routes.xml"
      })
@DirtiesContext
public class TestSubscriptionNotificationReportingProcessor {
	
	private static final Log log = LogFactory.getLog( TestSubscriptionNotificationReportingProcessor.class );

    @Resource
    private SubscriptionModificationXMLProcessor subModificationXMLProcessor;
    
    @Resource
    private SubscriptionSearchQueryDAO subscriptionSearchQueryDAO;
    
    @Resource
    private SubscriptionNotificationReportingProcessor subscriptionNotificationReportingProcessor;
    
    @Resource
    private ModelCamelContext context;
    
    @Resource
    private RapbackDAOImpl rapbackDAOImpl;
    
    @Autowired
	private JdbcTemplate jdbcTemplate;

    @Test
    public void testCreateOJBCsubscriptionModificationDocument() throws Exception
    {
    	assertNotNull(subscriptionSearchQueryDAO);
    	
        String sql = "SELECT count(*) FROM SUBSEQUENT_RESULTS";

    	int originalCount = jdbcTemplate.queryForObject(
                            sql, Integer.class);
    			
    	log.info("Current subsequent results: " + originalCount);
    	
    	String operationName=RapbackDataStoreAdapterConstants.REPORT_CRIMINAL_HISTORY_CONSOLIDATION;
    	String currentUcn="";
    	String newUcn="9222202";
    	String transactionNumber = "000001820140729014008339995";
    	
    	Document report = XmlUtils.parseFileToDocument(new File("src/test/resources/xmlInstances/criminalHistoryUpdateReporting/CriminalHistory-Consolidation-Report.xml"));
    	
    	subscriptionNotificationReportingProcessor.processFbiNotificationReport(report, operationName, currentUcn, newUcn, transactionNumber);
    	
    	List<SubsequentResults> subsequentResults = rapbackDAOImpl.getSubsequentResultsByUcn("9222202");
    	assertEquals(1, subsequentResults.size());
    	
    	operationName=RapbackDataStoreAdapterConstants.REPORT_CRIMINAL_HISTORY_IDENTIFIER_UPDATE;
    	currentUcn="";
    	newUcn="FBI2095607";
    	
    	report = XmlUtils.parseFileToDocument(new File("src/test/resources/xmlInstances/criminalHistoryUpdateReporting/CriminalHistory-IdentifierUpdate-Report.xml"));
    	
    	subscriptionNotificationReportingProcessor.processFbiNotificationReport(report, operationName, currentUcn, newUcn, transactionNumber);

    	subsequentResults = rapbackDAOImpl.getSubsequentResultsByUcn("FBI2095607");
    	assertEquals(1, subsequentResults.size());
    	
    	operationName=RapbackDataStoreAdapterConstants.REPORT_CRIMINAL_HISTORY_RESTORATION;
    	currentUcn="FBI234567";
    	newUcn="";
    	
    	report = XmlUtils.parseFileToDocument(new File("src/test/resources/xmlInstances/criminalHistoryUpdateReporting/CriminalHistory-Restoration-Report.xml"));
    	
    	subscriptionNotificationReportingProcessor.processFbiNotificationReport(report, operationName, currentUcn, newUcn, transactionNumber);
    	
    	subsequentResults = rapbackDAOImpl.getSubsequentResultsByUcn("FBI234567");
    	assertEquals(1, subsequentResults.size());
    	
    	int updatedCount = jdbcTemplate.queryForObject(
                sql, Integer.class);
	
    	log.info("Current subsequent results: " + updatedCount);
    	
    	assertEquals(updatedCount, originalCount + 3);
    	
    	
    }
	
}
