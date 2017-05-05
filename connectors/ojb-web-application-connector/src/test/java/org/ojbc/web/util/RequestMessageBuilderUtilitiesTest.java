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
package org.ojbc.web.util;

import java.io.File;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.custommonkey.xmlunit.Diff;
import org.custommonkey.xmlunit.Difference;
import org.custommonkey.xmlunit.DifferenceConstants;
import org.custommonkey.xmlunit.DifferenceListener;
import org.junit.Assert;
import org.junit.Test;
import org.ojbc.util.camel.helper.OJBUtils;
import org.ojbc.util.camel.security.saml.SAMLTokenUtils;
import org.ojbc.util.model.saml.SamlAttribute;
import org.ojbc.util.xml.XmlUtils;
import org.ojbc.web.model.person.query.DetailsRequest;
import org.opensaml.xml.signature.SignatureConstants;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

public class RequestMessageBuilderUtilitiesTest {
    private final Log log = LogFactory.getLog( this.getClass() );
    private final String VALID_REQUESTED_RESOURCE="{http://ojbc.org/OJB_Portal/Subscriptions/1.0}OJB"; 

    @Test
    public void testCreateJuvenileQueryRequest() throws Exception {
    	Document document = RequestMessageBuilderUtilities.createJuvenileQueryRequest("12345","{http://ojbc.org/Services/WSDL/JuvenileHistoryRequest/1.0}CasePlanRequest");
    	XmlUtils.printNode(document);
    	
    	String identificationId = XmlUtils.xPathStringSearch(document, "/jh-req-doc:JuvenileHistoryQuery/jh-ext:JuvenileHistoryQueryCriteria/jh-ext:JuvenileInformationRecordID/nc30:IdentificationID");
    	String identificationSourceText = XmlUtils.xPathStringSearch(document, "/jh-req-doc:JuvenileHistoryQuery/jh-ext:JuvenileHistoryQueryCriteria/jh-ext:JuvenileInformationRecordID/nc30:IdentificationSourceText");
    	
    	Assert.assertEquals("12345", identificationId);
    	Assert.assertEquals("{http://ojbc.org/Services/WSDL/JuvenileHistoryRequest/1.0}CasePlanRequest", identificationSourceText);
    	
    }
    
    @Test
    public void testCreatePolicyAcknowledgementRecordingRequest() throws Exception {
        Document document = RequestMessageBuilderUtilities.createPolicyAcknowledgementRecordingRequest();
        
        String documentInString = OJBUtils.getStringFromDocument(document);
        log.debug("\nPolicy Acknowledgement Recording Request:\n"+ StringUtils.trimToEmpty(documentInString));
        
        Assert.assertNotNull(document);
        
        File expectedReponseFile = new File("src/test/resources/xml/policyAcknowledgementRecordingRequest"
                + "/acknowledgementRecordingRequestForAllPolicies.xml");
        String expectedResponseAsString = FileUtils.readFileToString(expectedReponseFile);
        
        //Use XML Unit to compare these files
        Diff myDiff = new Diff(documentInString, expectedResponseAsString);
        myDiff.overrideDifferenceListener(new IgnoreNamedElementsDifferenceListener("nc30:Date"));
        Assert.assertTrue("XML identical " + myDiff.toString(),
                       myDiff.identical());     

    }
    
    @Test
    public void testCreateIdentificationInitialResultsQueryRequest() throws Exception {
    	Document document = RequestMessageBuilderUtilities.createIdentificationInitialResultsQueryRequest("000001820140729014008339993");
    	
    	String documentInString = OJBUtils.getStringFromDocument(document);
    	log.debug("\nIdentification Results Query Request:\n"+ StringUtils.trimToEmpty(documentInString));
    	
    	Assert.assertNotNull(document);
    	
    	File expectedReponseFile = new File("src/test/resources/xml/identificationResultsQuery/identificationResultsQueryRequest.xml");
    	String expectedResponseAsString = FileUtils.readFileToString(expectedReponseFile);
    	
    	//Use XML Unit to compare these files
    	Diff myDiff = new Diff(documentInString, expectedResponseAsString);
    	Assert.assertTrue("XML identical " + myDiff.toString(),
    			myDiff.identical());     
    	
    }

    @Test
    public void testCreateIdentificationSubsequentResultsQueryRequest() throws Exception {
    	Document document = RequestMessageBuilderUtilities.createIdentificationSubsequentResultsQueryRequest("000001820140729014008339993");
    	
    	String documentInString = OJBUtils.getStringFromDocument(document);
    	log.debug("\nIdentification Results Query Request:\n"+ StringUtils.trimToEmpty(documentInString));
    	
    	Assert.assertNotNull(document);
    	
    	File expectedReponseFile = new File("src/test/resources/xml/identificationResultsQuery/identificationSubsequentResultsQueryRequest.xml");
    	String expectedResponseAsString = FileUtils.readFileToString(expectedReponseFile);
    	
    	//Use XML Unit to compare these files
    	Diff myDiff = new Diff(documentInString, expectedResponseAsString);
    	Assert.assertTrue("XML identical " + myDiff.toString(),
    			myDiff.identical());     
    	
    }
    
    @Test
    public void testCreateIdentificationResultsModificationRequest() throws Exception {
    	Document document = RequestMessageBuilderUtilities.createIdentificationResultsModificationRequest("000001820140729014008339993");
    	
    	String documentInString = OJBUtils.getStringFromDocument(document);
    	log.debug("\nIdentification Results Modification Request:\n"+ StringUtils.trimToEmpty(documentInString));
    	
    	Assert.assertNotNull(document);
    	
    	File expectedReponseFile = new File("src/test/resources/xml/identificationResultsModification/identificationResultsModificationRequest.xml");
    	String expectedResponseAsString = FileUtils.readFileToString(expectedReponseFile);
    	
    	//Use XML Unit to compare these files
    	Diff myDiff = new Diff(documentInString, expectedResponseAsString);
    	Assert.assertTrue("XML identical " + myDiff.toString(),
    			myDiff.identical());     
    	
    }
    
    @Test
    public void testCreatePolicyBasedAccessControlRequest() throws Exception {
        //Create Default SAML Token Element. 
        Element samlToken = SAMLTokenUtils.createStaticAssertionAsElement("https://idp.ojbc-local.org:9443/idp/shibboleth", 
                SignatureConstants.ALGO_ID_C14N_EXCL_OMIT_COMMENTS, 
                SignatureConstants.ALGO_ID_SIGNATURE_RSA_SHA1, true, true, null);
        
        //Create the request doc with default SAML token. 
        Document document = RequestMessageBuilderUtilities.createPolicyBasedAccessControlRequest(samlToken, VALID_REQUESTED_RESOURCE);
        String documentInString = OJBUtils.getStringFromDocument(document);
        log.debug("\nIdentitiy Based Access Control Request:\n"+ StringUtils.trimToEmpty(documentInString));
 
        Assert.assertNotNull(document);
        
        File expectedReponseFile = new File("src/test/resources/xml/identityBasedAccessControlRequest"
                + "/createdIdentityBasedAccessControlRequest.xml");
        String expectedResponseAsString = FileUtils.readFileToString(expectedReponseFile);
        
        //Use XML Unit to compare these files
        Diff myDiff = new Diff(documentInString, expectedResponseAsString);
        Assert.assertTrue("XML identical " + myDiff.toString(),
                       myDiff.identical());   
        
        Map<SamlAttribute, String> customAttributes = new HashMap<SamlAttribute, String>();
        customAttributes.put(SamlAttribute.FederationId, "");
        samlToken = SAMLTokenUtils.createStaticAssertionAsElement("https://idp.ojbc-local.org:9443/idp/shibboleth", 
                SignatureConstants.ALGO_ID_C14N_EXCL_OMIT_COMMENTS, 
                SignatureConstants.ALGO_ID_SIGNATURE_RSA_SHA1, true, true, customAttributes);
    }
    
    @Test
    public void testCreatefirearmsPurchaseProhibition() throws Exception {
    	
    	DetailsRequest detailsRequest = new DetailsRequest();
    	
    	detailsRequest.setIdentificationID("System Record ID");
    	detailsRequest.setIdentificationSourceText("System Name");
    	
    	Document document = RequestMessageBuilderUtilities.createfirearmsPurchaseProhibition(detailsRequest);
    	
    	String documentInString = OJBUtils.getStringFromDocument(document);
    	log.debug("\nFirearms Prohibition Query Request:\n"+ StringUtils.trimToEmpty(documentInString));
    	
    	Assert.assertNotNull(document);
    	
    	File expectedReponseFile = new File("src/test/resources/xml/firearmsProhibitionQueryRequest/firearmsProhibitionQueryRequest.xml");
    	String expectedResponseAsString = FileUtils.readFileToString(expectedReponseFile);
    	
    	//Use XML Unit to compare these files
    	Diff myDiff = new Diff(documentInString, expectedResponseAsString);
    	Assert.assertTrue("XML identical " + myDiff.toString(),
    			myDiff.identical());     
    	
    }
    
    @Test(expected = Exception.class)
    public void testGetOutstandingPoliciesForUserWithEmptyFedId() throws Exception {
        RequestMessageBuilderUtilities.createPolicyBasedAccessControlRequest(null, VALID_REQUESTED_RESOURCE);
    }

    public class IgnoreNamedElementsDifferenceListener implements DifferenceListener {
        private Set<String> blackList = new HashSet<String>();

        public IgnoreNamedElementsDifferenceListener(String ... elementNames) {
            for (String name : elementNames) {
                blackList.add(name);
            }
        }

        public int differenceFound(Difference difference) {
            if (difference.getId() == DifferenceConstants.TEXT_VALUE_ID) {
                if (blackList.contains(difference.getControlNodeDetail().getNode().getParentNode().getNodeName())) {
                    return DifferenceListener.RETURN_IGNORE_DIFFERENCE_NODES_IDENTICAL;
                }
            }

            return DifferenceListener.RETURN_ACCEPT_DIFFERENCE;
        }

        public void skippedComparison(Node node, Node node1) {

        }
    }
}
