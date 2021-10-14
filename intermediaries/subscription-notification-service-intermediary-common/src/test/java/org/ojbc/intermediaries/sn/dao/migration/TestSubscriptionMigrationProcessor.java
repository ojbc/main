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
package org.ojbc.intermediaries.sn.dao.migration;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.util.List;
import java.util.Map;

import org.apache.camel.CamelContext;
import org.apache.camel.Exchange;
import org.apache.camel.impl.DefaultCamelContext;
import org.apache.camel.support.DefaultExchange;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.ojbc.intermediaries.sn.dao.SubscriptionSearchQueryDAO;
import org.ojbc.intermediaries.sn.migration.SubscriptionMigrationProcessor;
import org.ojbc.util.model.rapback.AgencyProfile;
import org.ojbc.util.xml.XmlUtils;
import org.ojbc.util.xml.subscription.Subscription;
import org.ojbc.util.xml.subscription.SubscriptionNotificationDocumentBuilderUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations={
		"classpath:META-INF/spring/test-application-context.xml",
		"classpath:META-INF/spring/h2-mock-database-application-context.xml",
		"classpath:META-INF/spring/h2-mock-database-context-rapback-datastore.xml",
		"classpath:META-INF/spring/h2-mock-database-context-enhanced-auditlog.xml"
		})
@DirtiesContext
public class TestSubscriptionMigrationProcessor {
    
	private static final Log log = LogFactory.getLog(TestSubscriptionMigrationProcessor.class);
	
	@Autowired
    private SubscriptionMigrationProcessor subscriptionMigrationProcessor;
	
	@Autowired
	private SubscriptionSearchQueryDAO subscriptionSearchQueryDAO;

    @Test
    public void testProcessAgencyProfileORIEntry() throws Exception {
    
    	List<AgencyProfile> agencyProfiles = subscriptionSearchQueryDAO.returnAllAgencies();
    	
    	assertEquals(3, agencyProfiles.size());
    	
    	subscriptionMigrationProcessor.setDefaultAgencyProfileState("VT");
    	
    	String csvLine1 = "DATA_YEAR,ORI,LEGACY_ORI,COVERED_BY_LEGACY_ORI,DIRECT_CONTRIBUTOR_FLAG,DORMANT_FLAG,DORMANT_YEAR,REPORTING_TYPE,UCR_AGENCY_NAME,NCIC_AGENCY_NAME,PUB_AGENCY_NAME,PUB_AGENCY_UNIT,AGENCY_STATUS,STATE_ID,STATE_NAME,STATE_ABBR,STATE_POSTAL_ABBR,DIVISION_CODE,DIVISION_NAME,REGION_CODE,REGION_NAME,REGION_DESC,AGENCY_TYPE_NAME,POPULATION,SUBMITTING_AGENCY_ID,SAI,SUBMITTING_AGENCY_NAME,SUBURBAN_AREA_FLAG,POPULATION_GROUP_ID,POPULATION_GROUP_CODE,POPULATION_GROUP_DESC,PARENT_POP_GROUP_CODE,PARENT_POP_GROUP_DESC,MIP_FLAG,POP_SORT_ORDER,SUMMARY_RAPE_DEF,PE_REPORTED_FLAG,PE_MALE_OFFICER_COUNT,PE_FEMALE_OFFICER_COUNT,PE_MALE_CIVILIAN_COUNT,PE_FEMALE_CIVILIAN_COUNT,NIBRS_CERT_DATE,NIBRS_START_DATE,NIBRS_LEOKA_START_DATE,NIBRS_CT_START_DATE,NIBRS_MULTI_BIAS_START_DATE,NIBRS_OFF_ETH_START_DATE,COVERED_FLAG,COUNTY_NAME,MSA_NAME,MSA_SORT_ORDER,PUBLISHABLE_FLAG,CITY_NAME,POPULATION_RANK,MIP_RANK,CAMPUS_ID,AGENCY_TYPE_ID,COUNTY_CODE,MSA_CODE,PE_PUBLISHABLE_FLAG,JUDICIAL_DIST_CODE,FID_CODE,DEPARTMENT_ID,ADDED_DATE,POPULATION_FAMILY_ID,FIELD_OFFICE_ID\r\n"; 
    	subscriptionMigrationProcessor.processAgencyProfileORIEntry(csvLine1);
    	
    	String csvLine2 = "2018,AK0010100,AK001010,,N,N,,S,ANCHORAGE,ANCHORAGE PD,Anchorage,,A,1,Alaska,AK,AK,9,Pacific,4,West,Region IV,City,291992,23353,AKUCR0001,Department of Public Safety Criminal Records and Identification Bureau Uniform Crime Reporting Section,N,5,1C,\"Cities from 250,000 thru 499,999\",1,\"All cities 250,000 or over\",Y,1,R,Y,369,58,36,111,,,,,,,N,ANCHORAGE,\"Anchorage, AK\",\"anchorage, ak\",Y,Anchorage,114,112,,1,,,Y,020A,,,30-APR-91,2,5\r\n"; 
    	subscriptionMigrationProcessor.processAgencyProfileORIEntry(csvLine2);

    	String csvLine3 = "2018,VT0020500,VT0020500,VT0020400,N,N,,S,MANCHESTER VILLAGE,MANCHESTER VILLAGE,Manchester Village,,A,52,Vermont,VT,VT,1,New England,1,Northeast,Region I,City,740,23397,VTUCR0001,Department of Public Safety Vermont Criminal Information Center,N,11,7,\"Cities under 2,500\",7,\"Cities under 2,500\",N,2,,N,0,0,0,0,01-JAN-93,,,,,,Y,BENNINGTON,Non-MSA,zzzzzz,N,Manchester Village,10540,499,,1,,,,405A,,,30-APR-91,2,3\r\n"; 
    	subscriptionMigrationProcessor.processAgencyProfileORIEntry(csvLine3);

    	agencyProfiles = subscriptionSearchQueryDAO.returnAllAgencies();
    	
    	assertEquals(4, agencyProfiles.size());
    	
    	Integer agencyPk = subscriptionSearchQueryDAO.returnAgencyPkFromORI("VT0020500");
    	assertNotNull(agencyPk);
    }
	
    @Test
    @Ignore
    public void testSubscriptionMigration() throws Exception {
    	
    	subscriptionMigrationProcessor.clearSubscriptionMap();
    	
    	String csvLine1 = "84172,{http://ojbc.org/wsn/topics}:person/criminalHistoryCycleTrackingIdentifierAssignment,2017-11-20,NULL,2017-11-20,{http://ojbc.org/OJB_Portal/Subscriptions/1.0}OJB,MAINE:IDP:SOM:USER:First.Person@local.gov,First Subject,1,NULL,NULL,2017-11-20 15:36,109090,84172,email,First.Person@local.gov,338034,84172,dateOfBirth,1960-01-02"; 
    	subscriptionMigrationProcessor.processSubscriptionCSVRecord(csvLine1);
    	
    	String csvLine2 = "84172,{http://ojbc.org/wsn/topics}:person/criminalHistoryCycleTrackingIdentifierAssignment,2017-11-20,NULL,2017-11-20,{http://ojbc.org/OJB_Portal/Subscriptions/1.0}OJB,MAINE:IDP:SOM:USER:First.Person@local.gov,First Subject,1,NULL,NULL,2017-11-20 15:36,109090,84172,email,First.Person@local.gov,338031,84172,firstName,First"; 
    	subscriptionMigrationProcessor.processSubscriptionCSVRecord(csvLine2);

    	String csvLine3 = "84172,{http://ojbc.org/wsn/topics}:person/criminalHistoryCycleTrackingIdentifierAssignment,2017-11-20,NULL,2017-11-20,{http://ojbc.org/OJB_Portal/Subscriptions/1.0}OJB,MAINE:IDP:SOM:USER:First.Person@local.gov,First Subject,1,NULL,NULL,2017-11-20 15:36,109090,84172,email,First.Person@local.gov,338032,84172,lastName,Subject"; 
    	subscriptionMigrationProcessor.processSubscriptionCSVRecord(csvLine3);

    	String csvLine4 = "84172,{http://ojbc.org/wsn/topics}:person/criminalHistoryCycleTrackingIdentifierAssignment,2017-11-20,NULL,2017-11-20,{http://ojbc.org/OJB_Portal/Subscriptions/1.0}OJB,MAINE:IDP:SOM:USER:First.Person@local.gov,First Subject,1,NULL,NULL,2017-11-20 15:36,109090,84172,email,First.Person@local.gov,338033,84172,subscriptionQualifier,977b0ff6-246e-413d-abc8-ee7cc1ee23ee"; 
    	subscriptionMigrationProcessor.processSubscriptionCSVRecord(csvLine4);

    	String csvLine5 = "100843,{http://ojbc.org/wsn/topics}:person/criminalHistoryCycleTrackingIdentifierAssignment,2018-03-02,2019-03-02,2018-03-02,{http://ojbc.org/OJB_Portal/Subscriptions/1.0}OJB,MAINE:IDP:SOM:USER:Second.2.person@local.gov,Second Subject,1,NULL,NULL,2018-03-02 13:59,130712,100843,email,third.person@local.gov,406264,100843,dateOfBirth,1990-04-01"; 
    	subscriptionMigrationProcessor.processSubscriptionCSVRecord(csvLine5);

    	String csvLine6 = "100843,{http://ojbc.org/wsn/topics}:person/criminalHistoryCycleTrackingIdentifierAssignment,2018-03-02,2019-03-02,2018-03-02,{http://ojbc.org/OJB_Portal/Subscriptions/1.0}OJB,MAINE:IDP:SOM:USER:Second.2.person@local.gov,Second Subject,1,NULL,NULL,2018-03-02 13:59,130712,100843,email,third.person@local.gov,406261,100843,firstName,Second"; 
    	subscriptionMigrationProcessor.processSubscriptionCSVRecord(csvLine6);

    	String csvLine7 = "100843,{http://ojbc.org/wsn/topics}:person/criminalHistoryCycleTrackingIdentifierAssignment,2018-03-02,2019-03-02,2018-03-02,{http://ojbc.org/OJB_Portal/Subscriptions/1.0}OJB,MAINE:IDP:SOM:USER:Second.2.person@local.gov,Second Subject,1,NULL,NULL,2018-03-02 13:59,130712,100843,email,third.person@local.gov,406262,100843,lastName,Subject"; 
    	subscriptionMigrationProcessor.processSubscriptionCSVRecord(csvLine7);

    	String csvLine8 = "100843,{http://ojbc.org/wsn/topics}:person/criminalHistoryCycleTrackingIdentifierAssignment,2018-03-02,2019-03-02,2018-03-02,{http://ojbc.org/OJB_Portal/Subscriptions/1.0}OJB,MAINE:IDP:SOM:USER:Second.2.person@local.gov,Second Subject,1,NULL,NULL,2018-03-02 13:59,130712,100843,email,third.person@local.gov,406263,100843,subscriptionQualifier,e31bb757-5ac8-4f13-adce-2879bd76b97d"; 
    	subscriptionMigrationProcessor.processSubscriptionCSVRecord(csvLine8);

    	String csvLine9 = "100843,{http://ojbc.org/wsn/topics}:person/criminalHistoryCycleTrackingIdentifierAssignment,2018-03-02,2019-03-02,2018-03-02,{http://ojbc.org/OJB_Portal/Subscriptions/1.0}OJB,MAINE:IDP:SOM:USER:Second.2.person@local.gov,Second Subject,1,NULL,NULL,2018-03-02 13:59,130711,100843,email,Second.2.person@local.gov,406264,100843,dateOfBirth,1990-04-01"; 
    	subscriptionMigrationProcessor.processSubscriptionCSVRecord(csvLine9);

    	String csvLine10 = "100843,{http://ojbc.org/wsn/topics}:person/criminalHistoryCycleTrackingIdentifierAssignment,2018-03-02,2019-03-02,2018-03-02,{http://ojbc.org/OJB_Portal/Subscriptions/1.0}OJB,MAINE:IDP:SOM:USER:Second.2.person@local.gov,Second Subject,1,NULL,NULL,2018-03-02 13:59,130711,100843,email,Second.2.person@local.gov,406261,100843,firstName,Second"; 
    	subscriptionMigrationProcessor.processSubscriptionCSVRecord(csvLine10);

    	String csvLine11 = "100843,{http://ojbc.org/wsn/topics}:person/criminalHistoryCycleTrackingIdentifierAssignment,2018-03-02,2019-03-02,2018-03-02,{http://ojbc.org/OJB_Portal/Subscriptions/1.0}OJB,MAINE:IDP:SOM:USER:Second.2.person@local.gov,Second Subject,1,NULL,NULL,2018-03-02 13:59,130711,100843,email,Second.2.person@local.gov,406262,100843,lastName,Subject"; 
    	subscriptionMigrationProcessor.processSubscriptionCSVRecord(csvLine11);

    	String csvLine12 = "100843,{http://ojbc.org/wsn/topics}:person/criminalHistoryCycleTrackingIdentifierAssignment,2018-03-02,2019-03-02,2018-03-02,{http://ojbc.org/OJB_Portal/Subscriptions/1.0}OJB,MAINE:IDP:SOM:USER:Second.2.person@local.gov,Second Subject,1,NULL,NULL,2018-03-02 13:59,130711,100843,email,Second.2.person@local.gov,406263,100843,subscriptionQualifier,e31bb757-5ac8-4f13-adce-2879bd76b97d"; 
    	subscriptionMigrationProcessor.processSubscriptionCSVRecord(csvLine12);

    	log.info("Subscriptions: " + subscriptionMigrationProcessor.getSubscriptionMap());
    	
    	assertEquals(2, subscriptionMigrationProcessor.getSubscriptionMap().size());
    	
    	Map<Integer, Subscription> subscriptionsMap = subscriptionMigrationProcessor.getSubscriptionMap();
    	
    	Subscription subscription1 = subscriptionsMap.get(84172);
    	assertNotNull(subscription1);
    	assertEquals(1, subscription1.getEmailList().size());
    	
    	
    	Subscription subscription2 = subscriptionsMap.get(100843);
    	assertNotNull(subscription2);
    	assertEquals(2, subscription2.getEmailList().size());

        // using for-each loop for iteration over Map.entrySet() 
        for (Map.Entry<Integer,Subscription> entry : subscriptionsMap.entrySet())  
        {	
        	Subscription subToPrint = entry.getValue();
            XmlUtils.printNode(SubscriptionNotificationDocumentBuilderUtils.createSubscriptionRequest(subToPrint, null)); 
            
            CamelContext ctx = new DefaultCamelContext(); 
            Exchange ex = new DefaultExchange(ctx);
            
            subscriptionMigrationProcessor.enrichMessageWithHeaders(ex, subToPrint);
            
            if (entry.getKey().equals(84172))
            {
            	assertEquals("MAINE:IDP:SOM:USER:First.Person@local.gov", ex.getIn().getHeader("subscriptionOwner"));
            	assertEquals("First.Person@local.gov", ex.getIn().getHeader("subscriptionOwnerEmailAddress"));
            	assertEquals("First", ex.getIn().getHeader("subscriptionOwnerFirstName"));
            	assertEquals("Person", ex.getIn().getHeader("subscriptionOwnerLastName"));
            	assertEquals("ORI1", ex.getIn().getHeader("subscriptionOwnerOri"));
            }
            
            if (entry.getKey().equals(100843))
            {
            	assertEquals("MAINE:IDP:SOM:USER:Second.2.person@local.gov", ex.getIn().getHeader("subscriptionOwner"));
            	assertEquals("Second.2.person@local.gov", ex.getIn().getHeader("subscriptionOwnerEmailAddress"));
            	assertEquals("Second", ex.getIn().getHeader("subscriptionOwnerFirstName"));
            	assertEquals("person", ex.getIn().getHeader("subscriptionOwnerLastName"));
            	assertEquals("ORI2", ex.getIn().getHeader("subscriptionOwnerOri"));
            	
            }	
        }
        
        List<Subscription> subscriptions = subscriptionMigrationProcessor.getCompleteSubscriptions();
        assertEquals(2, subscriptions.size());
        
	}

}
