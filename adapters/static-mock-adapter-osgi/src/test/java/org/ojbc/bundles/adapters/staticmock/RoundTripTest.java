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

import java.io.StringReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.apache.commons.lang.StringUtils;
import org.joda.time.DateTime;
import org.junit.Test;
import org.ojbc.util.xml.XmlUtils;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

public class RoundTripTest extends AbstractStaticMockTest {

    private Map<String, String> personSearchSystemToQuerySystemMap;
    private DateTime baseDate;

    public RoundTripTest() {

        baseDate = StaticMockQuery.DATE_FORMATTER_YYYY_MM_DD.parseDateTime("2013-07-03");
        
        personSearchSystemToQuerySystemMap = new HashMap<String, String>();
        personSearchSystemToQuerySystemMap.put(StaticMockQuery.CRIMINAL_HISTORY_MOCK_ADAPTER_SEARCH_SYSTEM_ID, StaticMockQuery.CRIMINAL_HISTORY_MOCK_ADAPTER_QUERY_SYSTEM_ID);
        personSearchSystemToQuerySystemMap.put(StaticMockQuery.WARRANT_MOCK_ADAPTER_SEARCH_SYSTEM_ID, StaticMockQuery.WARRANT_MOCK_ADAPTER_QUERY_SYSTEM_ID);
        // note: incidents are special, because it's a two-stage search.  first we get matching incidents for a person, then we query for individual incidents
        personSearchSystemToQuerySystemMap.put(StaticMockQuery.INCIDENT_MOCK_ADAPTER_INCIDENT_PERSON_SEARCH_SYSTEM_ID, StaticMockQuery.INCIDENT_MOCK_ADAPTER_QUERY_SYSTEM_ID);
        personSearchSystemToQuerySystemMap.put(StaticMockQuery.FIREARM_MOCK_ADAPTER_SEARCH_SYSTEM_ID, StaticMockQuery.FIREARM_MOCK_ADAPTER_QUERY_BY_PERSON_SYSTEM_ID);        
        personSearchSystemToQuerySystemMap.put(StaticMockQuery.CUSTODY_SEARCH_SYSTEM_ID, StaticMockQuery.CUSTODY_QUERY_SYSTEM_ID);         
        personSearchSystemToQuerySystemMap.put(StaticMockQuery.COURT_CASE_SEARCH_SYSTEM_ID, StaticMockQuery.COURT_CASE_QUERY_SYSTEM_ID);        
        personSearchSystemToQuerySystemMap.put(StaticMockQuery.VEHICLE_CRASH_SEARCH_SYSTEM_ID, StaticMockQuery.VEHICLE_CRASH_QUERY_SYSTEM_ID);        
    }
    
    @Test
    public void testFirearmSearchRoundTrip() throws Exception {
        Document firearmSearchRequestMessage = buildBaseFirearmSearchRequestMessage();
        removeElement(firearmSearchRequestMessage, "/firearm-search-req-doc:FirearmSearchRequest/firearm-search-req-ext:Firearm/nc:ItemSerialIdentification");
        removeElement(firearmSearchRequestMessage, "/firearm-search-req-doc:FirearmSearchRequest/firearm-search-req-ext:Firearm/firearm-search-req-ext:FirearmMakeText");
        removeElement(firearmSearchRequestMessage, "/firearm-search-req-doc:FirearmSearchRequest/firearm-search-req-ext:Firearm/nc:FirearmCategoryCode");
        removeElement(firearmSearchRequestMessage, "/firearm-search-req-doc:FirearmSearchRequest/firearm-search-req-ext:ItemRegistration");
        removeElement(firearmSearchRequestMessage, "/firearm-search-req-doc:FirearmSearchRequest/nc:PropertyRegistrationAssociation");
        Document searchResults = staticMockQuery.searchDocuments(firearmSearchRequestMessage, baseDate);
        NodeList resultsNodes = XmlUtils.xPathNodeListSearch(searchResults, "firearm-search-resp-doc:FirearmSearchResults/firearm-search-resp-ext:FirearmSearchResult");
        assertEquals(3, resultsNodes.getLength());
        for (int i = 0; i < resultsNodes.getLength(); i++) {
            Node resultNode = resultsNodes.item(i);
            String searchSystemID = XmlUtils.xPathStringSearch(resultNode, "firearm-search-resp-ext:SourceSystemNameText");
            String recordID = XmlUtils.xPathStringSearch(resultNode, "intel:SystemIdentifier/nc:IdentificationID");
            assertEquals(StaticMockQuery.FIREARM_MOCK_ADAPTER_FIREARM_SEARCH_SYSTEM_ID, searchSystemID);
            Document queryRequestMessage = buildFirearmQueryRequestMessage(StaticMockQuery.FIREARM_MOCK_ADAPTER_QUERY_BY_FIREARM_SYSTEM_ID, recordID);
            List<IdentifiableDocumentWrapper> queryResultList = staticMockQuery.queryDocuments(queryRequestMessage);assertEquals(1, queryResultList.size());
            IdentifiableDocumentWrapper doc = queryResultList.get(0);
            assertNotNull(doc);
            assertTrue(XmlUtils.nodeExists(doc.getDocument(), "/firearm-doc:FirearmRegistrationQueryResults/firearm-ext:Firearm"));
        }
    }

    @Test
    public void testPersonSearchRoundTrip() throws Exception {
    	
        Document personSearchRequestMessage = buildFullResultsPersonSearchRequest();
        
        Document searchResults = staticMockQuery.searchDocuments(personSearchRequestMessage, baseDate);  
        
        XmlUtils.printNode(searchResults);
        
        int expectedResultCount = 7;
        assertEquals(expectedResultCount, XmlUtils.xPathNodeListSearch(searchResults, "/psres-doc:PersonSearchResults/psres:PersonSearchResult").getLength());
        
        List<Document> queryRequests = buildQueryRequestMessages(searchResults);
        
        assertEquals(expectedResultCount, queryRequests.size());
        
        boolean incidentFound = false;
        boolean firearmFound = false;
        boolean warrantFound = false;
        boolean chFound = false;               
        boolean custodyResultFound = false;        
        boolean courtCaseResultFound = false;
        boolean vehicleCrashFound = false;
        
        for (Document queryRequest : queryRequests) {
        	        	
            List<IdentifiableDocumentWrapper> queryResultList = staticMockQuery.queryDocuments(queryRequest);
            
            if(1 != queryResultList.size()){            	
            	log.error("\n\n\n ERROR: Query results != 1 for request: \n\n");
            	XmlUtils.printNode(queryRequest);
            }            
            
            assertEquals(1, queryResultList.size());
            
            IdentifiableDocumentWrapper docWrapper = queryResultList.get(0);
            Document doc = docWrapper.getDocument();
            assertNotNull(doc);
            
            incidentFound |= XmlUtils.nodeExists(doc, "/ir:IncidentReport");
            firearmFound |= XmlUtils.nodeExists(doc, "/firearm-doc:PersonFirearmRegistrationQueryResults");
            warrantFound |= XmlUtils.nodeExists(doc, "/warrant:Warrants");
            chFound |= XmlUtils.nodeExists(doc, "/ch-doc:CriminalHistory");                        
            custodyResultFound |= XmlUtils.nodeExists(doc, "/cq-res-exch:CustodyQueryResults");            
            courtCaseResultFound |= XmlUtils.nodeExists(doc, "/ccq-res-doc:CourtCaseQueryResults");            
            vehicleCrashFound |= XmlUtils.nodeExists(doc, "/vcq-res-doc:VehicleCrashQueryResults");
        }
        assertTrue(incidentFound);
        assertTrue(firearmFound);
        assertTrue(warrantFound);
        assertTrue(chFound);        
        assertTrue(custodyResultFound);
        assertTrue(courtCaseResultFound);
        assertTrue(vehicleCrashFound);        
    }

    private List<Document> buildQueryRequestMessages(Document searchResults) throws Exception {
    	
		Element rootElement = searchResults.getDocumentElement();
		String rootElementLocalName = rootElement.getLocalName();

		String resultNodeXpath = null;
		
		switch (rootElementLocalName){
		
		case "PersonSearchResults":
			resultNodeXpath = "psres:PersonSearchResult";
			break; 
		case "IncidentPersonSearchResults":
			resultNodeXpath = "isres:IncidentPersonSearchResult"; 
			break; 
		case "CustodySearchResults":
			resultNodeXpath = "cs-res-ext:CustodySearchResult"; 
			break; 
		case "CourtCaseSearchResults":
			resultNodeXpath = "ccs-res-ext:CourtCaseSearchResult";	
			break;			
		default:
		}
    	
        NodeList resultsNodes = XmlUtils.xPathNodeListSearch(rootElement, "//" + resultNodeXpath);
        
        List<Document> rQueryRequestMessageList = new ArrayList<Document>();
        
        for (int i = 0; i < resultsNodes.getLength(); i++) {
        	
            Node resultNode = resultsNodes.item(i);
            
            String searchSystemID = XmlUtils.xPathStringSearch(resultNode, resultNodeXpath.split(":")[0] + ":SourceSystemNameText");
            
            String recordID = XmlUtils.xPathStringSearch(resultNode, "intel:SystemIdentifier/nc:IdentificationID");
            
            if (resultNode.getLocalName().equals("CustodySearchResult")){
            	recordID = XmlUtils.xPathStringSearch(resultNode, "intel31:SystemIdentification/nc30:IdentificationID");
            	
            }else if(resultNode.getLocalName().equals("CourtCaseSearchResult")){
            	
            	XmlUtils.printNode(resultNode);
            	
            	recordID = XmlUtils.xPathStringSearch(resultNode, "intel31:SystemIdentification/nc30:IdentificationID");
            }
            
            if (StaticMockQuery.INCIDENT_MOCK_ADAPTER_SEARCH_SYSTEM_ID.equals(searchSystemID)) {
            	
                Document incidentPersonSearchRequestMessage = buildIncidentPersonSearchRequestMessage(new Integer(recordID));
                Document incidentPersonSearchResults = staticMockQuery.searchDocuments(incidentPersonSearchRequestMessage, baseDate);
                List<Document> incidentPersonQueryRequestMessages = buildQueryRequestMessages(incidentPersonSearchResults);
				rQueryRequestMessageList.addAll(incidentPersonQueryRequestMessages);
            } 
            else if (StaticMockQuery.CUSTODY_PERSON_SEARCH_SYSTEM_ID.equals(searchSystemID)){
                Document requestMessage = buildCustodySearchRequestMessage(recordID);
                Document custodySearchResults = staticMockQuery.searchDocuments(requestMessage, baseDate);
                List<Document> custodyQueryRequestMessages = buildQueryRequestMessages(custodySearchResults);
				rQueryRequestMessageList.addAll(custodyQueryRequestMessages);
            } 
            else if(StaticMockQuery.COURT_CASE_PERSON_SEARCH_SYSTEM_ID.equals(searchSystemID)){
            	            	
            	Document ccPersonSearchRequestDoc = buildCourtCasePersonSearchRequestMessage(recordID);                  	            	
            	Document ccPersonSearchResultsDoc = staticMockQuery.searchDocuments(ccPersonSearchRequestDoc, baseDate);            	
            	List<Document> ccPersonQueryRequestDocList = buildQueryRequestMessages(ccPersonSearchResultsDoc);            	
            	rQueryRequestMessageList.addAll(ccPersonQueryRequestDocList);
            
            }else {
            	
            	String querySystemID = personSearchSystemToQuerySystemMap.get(searchSystemID);
            	rQueryRequestMessageList.add(buildPersonQueryRequestMessage(querySystemID, recordID));
            }
        }
        return rQueryRequestMessageList;
    }

    public Document getDocumentFromXmlString(String xmlString) {
        InputSource inputSource  = new InputSource( new StringReader( xmlString )) ; 
        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        dbf.setNamespaceAware(true);
        
        Document document = null; 
        try {
            DocumentBuilder documentBuilder = dbf.newDocumentBuilder();
            document = documentBuilder.parse(inputSource);
        } catch (Exception e) {
            log.error("Faild to create a document out of the xml string: " 
                    + StringUtils.trimToEmpty(xmlString), e);
        }
 
        return document;
    }

}
