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
package org.ojbc.pep;

import java.io.File;
import java.io.IOException;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import junit.framework.TestCase;

import org.junit.Before;
import org.junit.Test;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.xml.sax.SAXException;

public class TestPolicyEnforcementPoint extends TestCase {

    private PolicyDecisionPoint pep;
    private DocumentBuilder documentBuilder;

    @Before
    public void setUp() throws Exception {
        pep = new PolicyDecisionPoint();
        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        dbf.setNamespaceAware(true);
        documentBuilder = dbf.newDocumentBuilder();
    }

    @Test
    public void testIncidentRequest() throws Exception {
        pep.addPolicyDocument("src/test/resources/policy-sets/ps1/FederatedQuery.IncidentPolicy.xml");
        Document requestDocument = parseFileLocation("src/test/resources/policy-sets/ps1/test-requests/incident-request.xml");
        XacmlRequest request = new XacmlRequest(requestDocument);
        XacmlResponse response = pep.evaluate(request);
        List<XacmlResult> results = response.getResults();
        assertEquals(1, results.size());
        XacmlResult result = results.get(0);
        Document resultDocument = response.getResponseDocument();
        printNode(resultDocument);
        assertEquals(XacmlResult.DECISION_PERMIT, result.getDecision());
    }

    @Test
    public void testValidWarrantRequest() throws Exception {
        pep.addPolicyDocument("src/test/resources/policy-sets/ps1/FederatedQuery.WarrantPolicy.xml");
        Document requestDocument = parseFileLocation("src/test/resources/policy-sets/ps1/test-requests/warrant-request-valid.xml");
        XacmlRequest request = new XacmlRequest(requestDocument);
        List<XacmlResult> results = pep.evaluate(request).getResults();
        assertEquals(1, results.size());
        XacmlResult result = results.get(0);
        //Document resultDocument = result.getResultDocument();
        //printNode(resultDocument);
        assertEquals(XacmlResult.DECISION_PERMIT, result.getDecision());
    }
    
    @Test
    public void testInvalidWarrantRequest() throws Exception {
        pep.addPolicyDocument("src/test/resources/policy-sets/ps1/FederatedQuery.WarrantPolicy.xml");
        Document requestDocument = parseFileLocation("src/test/resources/policy-sets/ps1/test-requests/warrant-request-invalid.xml");
        XacmlRequest request = new XacmlRequest(requestDocument);
        List<XacmlResult> results = pep.evaluate(request).getResults();
        assertEquals(1, results.size());
        XacmlResult result = results.get(0);
        //Document resultDocument = result.getResultDocument();
        //printNode(resultDocument);
        assertEquals(XacmlResult.DECISION_DENY, result.getDecision());
    }
    
    @Test
    public void testValidAdultCriminalHistoryRequest() throws Exception {
        pep.addPolicyDocument("src/test/resources/policy-sets/ps1/FederatedQuery.CriminalHistoryPolicy.xml");
        Document requestDocument = parseFileLocation("src/test/resources/policy-sets/ps1/test-requests/criminal-history-request-adult-valid.xml");
        XacmlRequest request = new XacmlRequest(requestDocument);
        List<XacmlResult> results = pep.evaluate(request).getResults();
        assertEquals(1, results.size());
        XacmlResult result = results.get(0);
        //Document resultDocument = result.getResultDocument();
        //printNode(resultDocument);
        assertEquals(XacmlResult.DECISION_PERMIT, result.getDecision());
    }
    
    @Test
    public void testValidAdultCriminalHistoryRequestWithAllAttributes() throws Exception {
        pep.addPolicyDocument("src/test/resources/policy-sets/ps1/FederatedQuery.CriminalHistoryPolicy.xml");
        Document requestDocument = parseFileLocation("src/test/resources/policy-sets/ps1/test-requests/criminal-history-request-adult-valid-with-all-attributes.xml");
        XacmlRequest request = new XacmlRequest(requestDocument);
        List<XacmlResult> results = pep.evaluate(request).getResults();
        assertEquals(1, results.size());
        XacmlResult result = results.get(0);
        //Document resultDocument = result.getResultDocument();
        //printNode(resultDocument);
        assertEquals(XacmlResult.DECISION_PERMIT, result.getDecision());
    }
    
    @Test
    public void testInvalidAdultCriminalHistoryRequest() throws Exception {
        pep.addPolicyDocument("src/test/resources/policy-sets/ps1/FederatedQuery.CriminalHistoryPolicy.xml");
        Document requestDocument = parseFileLocation("src/test/resources/policy-sets/ps1/test-requests/criminal-history-request-adult-invalid.xml");
        XacmlRequest request = new XacmlRequest(requestDocument);
        List<XacmlResult> results = pep.evaluate(request).getResults();
        assertEquals(1, results.size());
        XacmlResult result = results.get(0);
        //Document resultDocument = result.getResultDocument();
        //printNode(resultDocument);
        assertEquals(XacmlResult.DECISION_DENY, result.getDecision());
    }
    
    @Test
    public void testValidJuvenileCriminalHistoryRequest() throws Exception {
        pep.addPolicyDocument("src/test/resources/policy-sets/ps1/FederatedQuery.CriminalHistoryPolicy.xml");
        Document requestDocument = parseFileLocation("src/test/resources/policy-sets/ps1/test-requests/criminal-history-request-juvenile-valid.xml");
        XacmlRequest request = new XacmlRequest(requestDocument);
        List<XacmlResult> results = pep.evaluate(request).getResults();
        assertEquals(1, results.size());
        XacmlResult result = results.get(0);
        //Document resultDocument = result.getResultDocument();
        //printNode(resultDocument);
        assertEquals(XacmlResult.DECISION_PERMIT, result.getDecision());
    }
    
    @Test
    public void testInalidJuvenileCriminalHistoryRequests() throws Exception {
        Document criminalHistoryPolicy = parseFileLocation("src/test/resources/policy-sets/ps1/FederatedQuery.CriminalHistoryPolicy.xml");
        pep.addPolicyDocument(criminalHistoryPolicy);
        
        Document requestDocument = parseFileLocation("src/test/resources/policy-sets/ps1/test-requests/criminal-history-request-juvenile-invalid-subject-county.xml");
        XacmlRequest request = new XacmlRequest(requestDocument);
        List<XacmlResult> results = pep.evaluate(request).getResults();
        assertEquals(1, results.size());
        XacmlResult result = results.get(0);
        //Document resultDocument = result.getResultDocument();
        //printNode(resultDocument);
        assertEquals(XacmlResult.DECISION_DENY, result.getDecision());
        requestDocument = parseFileLocation("src/test/resources/policy-sets/ps1/test-requests/criminal-history-request-juvenile-invalid-agency-type.xml");
        request = new XacmlRequest(requestDocument);
        results = pep.evaluate(request).getResults();
        assertEquals(1, results.size());
        result = results.get(0);
        //Document resultDocument = result.getResultDocument();
        //printNode(resultDocument);
        assertEquals(XacmlResult.DECISION_DENY, result.getDecision());
    }

    private Document parseFileLocation(String documentLocation) throws SAXException, IOException {
        File requestFile = new File(documentLocation);
        return documentBuilder.parse(requestFile);
    }

    private static void printNode(Node n) throws Exception {
        TransformerFactory tf = TransformerFactory.newInstance();
        Transformer t = tf.newTransformer();
        t.setOutputProperty(OutputKeys.INDENT, "yes");
        t.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "yes");
        t.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "4");
        t.transform(new DOMSource(n), new StreamResult(System.out));
    }

}
