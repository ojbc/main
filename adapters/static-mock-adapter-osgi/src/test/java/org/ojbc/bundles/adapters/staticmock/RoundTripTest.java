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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.ojbc.bundles.adapters.staticmock.IdentifiableDocumentWrapper;
import org.ojbc.bundles.adapters.staticmock.StaticMockQuery;
import org.ojbc.util.xml.XmlUtils;

import org.joda.time.DateTime;
import org.junit.Test;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class RoundTripTest extends AbstractStaticMockTest {

    Map<String, String> personSearchSystemToQuerySystemMap;
    private DateTime baseDate;

    public RoundTripTest() {

        baseDate = StaticMockQuery.DATE_FORMATTER_YYYY_MM_DD.parseDateTime("2013-07-03");
        
        personSearchSystemToQuerySystemMap = new HashMap<String, String>();
        personSearchSystemToQuerySystemMap.put(StaticMockQuery.CRIMINAL_HISTORY_MOCK_ADAPTER_SEARCH_SYSTEM_ID, StaticMockQuery.CRIMINAL_HISTORY_MOCK_ADAPTER_QUERY_SYSTEM_ID);
        personSearchSystemToQuerySystemMap.put(StaticMockQuery.WARRANT_MOCK_ADAPTER_SEARCH_SYSTEM_ID, StaticMockQuery.WARRANT_MOCK_ADAPTER_QUERY_SYSTEM_ID);
        // note: incidents are special, because it's a two-stage search.  first we get matching incidents for a person, then we query for individual incidents
        personSearchSystemToQuerySystemMap.put(StaticMockQuery.INCIDENT_MOCK_ADAPTER_INCIDENT_PERSON_SEARCH_SYSTEM_ID, StaticMockQuery.INCIDENT_MOCK_ADAPTER_QUERY_SYSTEM_ID);
        personSearchSystemToQuerySystemMap.put(StaticMockQuery.FIREARM_MOCK_ADAPTER_SEARCH_SYSTEM_ID, StaticMockQuery.FIREARM_MOCK_ADAPTER_QUERY_BY_PERSON_SYSTEM_ID);
        
    }
    
    @Test
    public void testFirearmSearchRoundTrip() throws Exception {
        Document firearmSearchRequestMessage = buildBaseFirearmSearchRequestMessage();
        removeElement(firearmSearchRequestMessage, "/firearm-search-req-doc:FirearmSearchRequest/firearm-search-req-ext:Firearm/nc:ItemSerialIdentification");
        removeElement(firearmSearchRequestMessage, "/firearm-search-req-doc:FirearmSearchRequest/firearm-search-req-ext:Firearm/firearms-codes-demostate:FirearmMakeCode");
        removeElement(firearmSearchRequestMessage, "/firearm-search-req-doc:FirearmSearchRequest/firearm-search-req-ext:Firearm/nc:FirearmCategoryCode");
        removeElement(firearmSearchRequestMessage, "/firearm-search-req-doc:FirearmSearchRequest/firearm-search-req-ext:ItemRegistration");
        removeElement(firearmSearchRequestMessage, "/firearm-search-req-doc:FirearmSearchRequest/nc:PropertyRegistrationAssociation");
        Document searchResults = staticMockQuery.searchDocuments(firearmSearchRequestMessage, baseDate);
        //XmlUtils.printNode(searchResults);
        NodeList resultsNodes = XmlUtils.xPathNodeListSearch(searchResults, "firearm-search-resp-doc:FirearmSearchResults/firearm-search-resp-ext:FirearmSearchResult");
        for (int i = 0; i < resultsNodes.getLength(); i++) {
            Node resultNode = resultsNodes.item(i);
            String searchSystemID = XmlUtils.xPathStringSearch(resultNode, "firearm-search-resp-ext:SourceSystemNameText");
            String recordID = XmlUtils.xPathStringSearch(resultNode, "intel:SystemIdentifier/nc:IdentificationID");
            assertEquals(StaticMockQuery.FIREARM_MOCK_ADAPTER_FIREARM_SEARCH_SYSTEM_ID, searchSystemID);
            Document queryRequestMessage = buildFirearmQueryRequestMessage(StaticMockQuery.FIREARM_MOCK_ADAPTER_QUERY_BY_FIREARM_SYSTEM_ID, recordID);
            List<IdentifiableDocumentWrapper> queryResultList = staticMockQuery.queryDocuments(queryRequestMessage);assertEquals(1, queryResultList.size());
            IdentifiableDocumentWrapper doc = queryResultList.get(0);
            assertNotNull(doc);
            // TODO: we could flesh this out more, but the important thing is to make sure we get some query result back
        }
    }

    @Test
    public void testPersonSearchRoundTrip() throws Exception {
        Document personSearchRequestMessage = buildFullResultsPersonSearchRequest();
        Document searchResults = staticMockQuery.searchDocuments(personSearchRequestMessage, baseDate);
        List<Document> queryRequests = buildPersonQueryRequestMessages(searchResults);
        for (Document queryRequest : queryRequests) {
            List<IdentifiableDocumentWrapper> queryResultList = staticMockQuery.queryDocuments(queryRequest);
            assertEquals(1, queryResultList.size());
            IdentifiableDocumentWrapper doc = queryResultList.get(0);
            assertNotNull(doc);
            // TODO: we could flesh this out more, but the important thing is to make sure we get some query result back
        }
    }

    private List<Document> buildPersonQueryRequestMessages(Document searchResults) throws Exception {
        NodeList resultsNodes = XmlUtils.xPathNodeListSearch(searchResults, "psres-doc:PersonSearchResults/psres:PersonSearchResult");
        List<Document> ret = new ArrayList<Document>();
        for (int i = 0; i < resultsNodes.getLength(); i++) {
            Node resultNode = resultsNodes.item(i);
            String searchSystemID = XmlUtils.xPathStringSearch(resultNode, "psres:SourceSystemNameText");
            String recordID = XmlUtils.xPathStringSearch(resultNode, "intel:SystemIdentifier/nc:IdentificationID");
            if (StaticMockQuery.INCIDENT_MOCK_ADAPTER_SEARCH_SYSTEM_ID.equals(searchSystemID)) {
                Document incidentPersonSearchRequestMessage = buildIncidentPersonSearchRequestMessage(new Integer(recordID));
                Document incidentPersonSearchResults = staticMockQuery.searchDocuments(incidentPersonSearchRequestMessage, baseDate);
                ret.addAll(buildPersonQueryRequestMessages(incidentPersonSearchResults));
            } else {
                String querySystemID = personSearchSystemToQuerySystemMap.get(searchSystemID);
                ret.add(buildPersonQueryRequestMessage(querySystemID, recordID));
            }
        }
        return ret;
    }

}
