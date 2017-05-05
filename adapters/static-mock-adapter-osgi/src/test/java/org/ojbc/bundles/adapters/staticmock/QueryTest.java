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
package org.ojbc.bundles.adapters.staticmock;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.ParserConfigurationException;

import org.junit.Test;
import org.ojbc.util.xml.XmlUtils;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

public class QueryTest extends AbstractStaticMockTest {

    @Test
    public void testFirearmQuery() throws Exception {
        Document responseDocument = doFirearmTestQuery("sample-9006054698886898174.xml:F1", StaticMockQuery.FIREARM_MOCK_ADAPTER_QUERY_BY_FIREARM_SYSTEM_ID, "firearm-doc:FirearmRegistrationQueryResults");
        List<String> schemaPaths = new ArrayList<String>();
        String iepdRootPath = "ssp/Firearm_Registration_Query_Results/artifacts/service_model/information_model/IEPD/xsd/";
        schemaPaths.add(iepdRootPath + "impl/demostate/demostate-firearm-codes.xsd");
        XmlUtils.validateInstance(iepdRootPath, "Subset/niem", "exchange_schema.xsd", schemaPaths, responseDocument);
        //XmlUtils.validateInstance("ssp/Firearm_Registration_Query_Results/artifacts/service_model/information_model/IEPD/xsd", "Subset/niem", "exchange_schema.xsd", responseDocument);
    }

    @Test
    public void testCriminalHistoryPersonQuery() throws Exception {
        doPersonTestQuery("sample-5329911427000337392.xml", StaticMockQuery.CRIMINAL_HISTORY_MOCK_ADAPTER_QUERY_SYSTEM_ID, "ch-doc:CriminalHistory");
    }

    @Test
    public void testIncidentPersonQuery() throws Exception {
        doPersonTestQuery("sample-3611105527476841852.xml", StaticMockQuery.INCIDENT_MOCK_ADAPTER_QUERY_SYSTEM_ID, "ir:IncidentReport");
    }

    @Test
    public void testWarrantPersonQuery() throws Exception {
        doPersonTestQuery("sample-2099271419112367733.xml", StaticMockQuery.WARRANT_MOCK_ADAPTER_QUERY_SYSTEM_ID, "warrant:Warrants");
    }

    @Test
    public void testFirearmRegistrationPersonQuery() throws Exception {
        doPersonTestQuery("sample-9006054698886898174.xml", StaticMockQuery.FIREARM_MOCK_ADAPTER_QUERY_BY_PERSON_SYSTEM_ID, "firearm-doc:PersonFirearmRegistrationQueryResults");
    }
    
    @Test
    public void testCustodyPersonQuery() throws ParserConfigurationException, Exception{
    	doPersonTestQuery("sample-CustodyCaseQueryResults2.xml", StaticMockQuery.CUSTODY_QUERY_SYSTEM_ID, "cq-res-exch:CustodyQueryResults");
    }
    
    @Test
    public void testCourtCasePersonQuery() throws ParserConfigurationException, Exception{
    	doPersonTestQuery("sample-CourtCaseQueryResults3.xml", StaticMockQuery.COURT_CASE_QUERY_SYSTEM_ID, "ccq-res-doc:CourtCaseQueryResults");
    }    
    
    @Test
    public void testVehicleCrashPersonQuery() throws ParserConfigurationException, Exception{    	
    	doPersonTestQuery("sample-VehicleCrash.xml", StaticMockQuery.VEHICLE_CRASH_QUERY_SYSTEM_ID, "vcq-res-doc:VehicleCrashQueryResults");    	
    }
    
    
    @Test
    public void testJuvenileHistoryQueries() throws Exception {
    	Document queryRequestMessage = buildPersonQueryRequestMessage(StaticMockQuery.JUVENILE_HISTORY_MOCK_ADAPTER_QUERY_SYSTEM_ID, "sample-108040379083497483.xml");
    	//XmlUtils.printNode(queryRequestMessage);
    	assertJuvenileHistoryComponentMessage(queryRequestMessage, "JuvenileReferralHistory", "jh-referral-doc:JuvenileReferralHistoryResponse", "nc30:Referral");
    	assertJuvenileHistoryComponentMessage(queryRequestMessage, "JuvenileIntakeHistory", "jh-intake-doc:JuvenileIntakeHistoryResponse", "cyfs:JuvenileIntakeAssessment");
    	assertJuvenileHistoryComponentMessage(queryRequestMessage, "JuvenilePlacementHistory", "jh-placement-doc:JuvenilePlacementHistoryResponse", "cyfs:JuvenilePlacement");
    	assertJuvenileHistoryComponentMessage(queryRequestMessage, "JuvenileOffenseHistory", "jh-offense-doc:JuvenileOffenseHistoryResponse", "jxdm50:OffenseChargeAssociation");
    	assertJuvenileHistoryComponentMessage(queryRequestMessage, "JuvenileHearingHistory", "jh-hearing-doc:JuvenileHearingHistoryResponse", "cyfs:CourtCase/jxdm50:CaseAugmentation/jxdm50:CaseHearing");
    	assertJuvenileHistoryComponentMessage(queryRequestMessage, "JuvenileCasePlanHistory", "jh-case-plan-doc:JuvenileCasePlanHistoryResponse", "jh-case-plan:CasePlan");
    }
    
    @Test
    public void testJuvenileHistoryQueriesObjectNotFound() throws Exception {
    	Document queryRequestMessage = buildPersonQueryRequestMessage(StaticMockQuery.JUVENILE_HISTORY_MOCK_ADAPTER_QUERY_SYSTEM_ID, "sample-108040379083497483-NoPlacement.xml");
    	//XmlUtils.printNode(queryRequestMessage);
    	List<IdentifiableDocumentWrapper> resultingDocuments = staticMockQuery.queryDocuments(queryRequestMessage, "JuvenilePlacementHistory");
        assertEquals(1, resultingDocuments.size());
        IdentifiableDocumentWrapper doc = resultingDocuments.get(0);
        assertNotNull(doc);
        Document document = doc.getDocument();
        //XmlUtils.printNode(document);
        assertEquals("NOT FOUND", XmlUtils.xPathNodeSearch(document, "/jh-placement-doc:JuvenilePlacementHistoryResponse/jh-placement:JuvenilePlacementHistory/jh-ext:JuvenileInformationAvailabilityCode").getTextContent());
        assertEquals(0, XmlUtils.xPathNodeListSearch(document, "/jh-placement-doc:JuvenilePlacementHistoryResponse/jh-placement:JuvenilePlacementHistory/cyfs:JuvenilePlacement").getLength());
    }

	public void assertJuvenileHistoryComponentMessage(Document queryRequestMessage, String componentTypeCode, String rootElementName, String componentElementName) throws Exception {
		List<IdentifiableDocumentWrapper> resultingDocuments = staticMockQuery.queryDocuments(queryRequestMessage, componentTypeCode);
        assertEquals(1, resultingDocuments.size());
        IdentifiableDocumentWrapper doc = resultingDocuments.get(0);
        assertNotNull(doc);
        Document document = doc.getDocument();
        //XmlUtils.printNode(document);
        Element root = document.getDocumentElement();
        assertEquals(rootElementName, root.getNodeName());
        assertEquals("FOUND", XmlUtils.xPathNodeSearch(document, "//jh-ext:JuvenileInformationAvailabilityCode").getTextContent());
        assertTrue(XmlUtils.xPathNodeListSearch(document, "//" + componentElementName).getLength() > 0);
	}
    
    private Document doFirearmTestQuery(String identifier, String systemId, String rootElementName) throws ParserConfigurationException, Exception {
        Document queryRequestMessage = buildFirearmQueryRequestMessage(systemId, identifier);
        //XmlUtils.printNode(queryRequestMessage);
        List<IdentifiableDocumentWrapper> resultingDocuments = staticMockQuery.queryDocuments(queryRequestMessage);
        assertEquals(1, resultingDocuments.size());
        IdentifiableDocumentWrapper doc = resultingDocuments.get(0);
        assertNotNull(doc);
        Document document = doc.getDocument();
        //XmlUtils.printNode(document);
        Element root = document.getDocumentElement();
        assertEquals(rootElementName, root.getNodeName());
        queryRequestMessage = buildFirearmQueryRequestMessage(systemId, "doesn't exist");
        resultingDocuments = staticMockQuery.queryDocuments(queryRequestMessage);
        assertEquals(0, resultingDocuments.size());
        return document;
    }

    private void doPersonTestQuery(String identifier, String systemId, String rootElementName) throws ParserConfigurationException, Exception {
    	
        Document queryRequestMessage = buildPersonQueryRequestMessage(systemId, identifier);
        //XmlUtils.printNode(queryRequestMessage);
        List<IdentifiableDocumentWrapper> resultingDocuments = staticMockQuery.queryDocuments(queryRequestMessage);
        assertEquals(1, resultingDocuments.size());
        IdentifiableDocumentWrapper doc = resultingDocuments.get(0);
        assertNotNull(doc);
        Document document = doc.getDocument();
        //XmlUtils.printNode(document);
        Element root = document.getDocumentElement();
        assertEquals(rootElementName, root.getNodeName());
        queryRequestMessage = buildPersonQueryRequestMessage(systemId, "doesn't exist");
        resultingDocuments = staticMockQuery.queryDocuments(queryRequestMessage);
        assertEquals(0, resultingDocuments.size());
    }

}
