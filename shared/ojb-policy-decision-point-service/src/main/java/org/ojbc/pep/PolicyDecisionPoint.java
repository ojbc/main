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
import java.io.StringReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.w3c.dom.Document;
import org.wso2.balana.Balana;
import org.wso2.balana.PDP;
import org.wso2.balana.PDPConfig;
import org.wso2.balana.ParsingException;
import org.wso2.balana.ctx.AbstractRequestCtx;
import org.wso2.balana.ctx.AbstractResult;
import org.wso2.balana.ctx.RequestCtxFactory;
import org.wso2.balana.ctx.ResponseCtx;
import org.wso2.balana.ctx.Status;
import org.wso2.balana.finder.AttributeFinder;
import org.wso2.balana.finder.PolicyFinder;
import org.wso2.balana.finder.PolicyFinderModule;
import org.wso2.balana.finder.ResourceFinder;
import org.wso2.balana.finder.impl.InMemoryPolicyFinderModule;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

/**
 * A generic interface to a XACML 3.0 Policy Decision Point.
 *
 */
public class PolicyDecisionPoint {

    private PDP pdp;
    private PDPConfig pdpConfig;
    private DocumentBuilder documentBuilder;
    
    private static final Log LOG = LogFactory.getLog(PolicyDecisionPoint.class);

    public PolicyDecisionPoint() {
        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        dbf.setNamespaceAware(true);
        try {
            documentBuilder = dbf.newDocumentBuilder();
        } catch (ParserConfigurationException e) {
            throw new RuntimeException(e);
        }
    }
   

    public void addPolicyDocument(String policyDocumentFileLocation) throws Exception
    {
    	addPolicyDocumentsFromFileLocations(Arrays.asList(policyDocumentFileLocation));     
    }

    public void addPolicyDocumentsFromFileLocations(List<String> policyDocumentFileLocations) throws Exception {
    	
    	List<Document> policyDocuments = new ArrayList<Document>();
    	
    	for (String policyDocumentFileLocation : policyDocumentFileLocations)
    	{
            File f = new File(policyDocumentFileLocation);
            policyDocuments.add(documentBuilder.parse(f));
    	}	
    	
    	addPolicyDocuments(policyDocuments);
    }
    
    
    /**
     * Add a policy document to the set of policies available to the PDP.
     * @param policy A policy Dom document
     */
    public void addPolicyDocument(Document policy) 
    {
    	createPdpConfig(Arrays.asList(policy));                
    }

    /**
     * Add policy documents to the set of policies available to the PDP.
     * @param policyDocuments A list of policy Dom documents
     */
    public void addPolicyDocuments(List<Document> policyDocuments) 
    { 
    	createPdpConfig(policyDocuments); 
    }
    
    /**
     * Evaluate a XACML request against the policies
     * @param request the request
     * @return the result
     */
    public XacmlResponse evaluate(XacmlRequest request)
    {
        XacmlResponse ret = new XacmlResponse();
        AbstractRequestCtx requestCtx; 
        try {
            requestCtx = RequestCtxFactory.getFactory().getRequestCtx(request.getRequestDocument().getDocumentElement());
            ResponseCtx responseCtx = pdp.evaluate(requestCtx);
            Set<AbstractResult> resultSet = responseCtx.getResults();
            ret.setDocument(parseResponseDocument(responseCtx.encode()));
            if (resultSet.size() > 1)
            {
                LOG.warn("WARNING: Result set contains multiple results. " + resultSet);
            }
            for(AbstractResult ar : resultSet) {
                ret.addResult(new XacmlResult(ar.getDecision()));
            }
        } catch (ParsingException e) {
            LOG.error("Invalid request  : " + e.getMessage());
            String error = "Invalid request  : " + e.getMessage();
            ArrayList<String> code = new ArrayList<String>();
            code.add(Status.STATUS_SYNTAX_ERROR);
            Status status = new Status(code, error);
            org.wso2.balana.ctx.xacml3.Result r = new org.wso2.balana.ctx.xacml3.Result(AbstractResult.DECISION_INDETERMINATE, status);
            try {
                ret.addResult(new XacmlResult(r.getDecision()));
            } catch (Exception e1) {
                throw new RuntimeException(e1);
            }
        } catch (SAXException se) {
            LOG.error("Invalid xml in response  : " + se.getMessage());
            String error = "Invalid xml in response  : " + se.getMessage();
            ArrayList<String> code = new ArrayList<String>();
            code.add(Status.STATUS_PROCESSING_ERROR);
            Status status = new Status(code, error);
            org.wso2.balana.ctx.xacml3.Result r = new org.wso2.balana.ctx.xacml3.Result(AbstractResult.DECISION_INDETERMINATE, status);
            try {
                ret.addResult(new XacmlResult(r.getDecision()));
            } catch (Exception e1) {
                throw new RuntimeException(e1);
            }
        }
        return ret;
    }

    private Document parseResponseDocument(String response) throws SAXException {
        Document ret;
        try {
            ret = documentBuilder.parse(new InputSource(new StringReader(response)));
        } catch (IOException e) {
            throw new RuntimeException (e);
        }
        return ret;
    }

    private void createPdpConfig(List<Document> documentList) {
    	
        PolicyFinder finder = new PolicyFinder();
        
        InMemoryPolicyFinderModule inMemoryPolicyFinderModule = new InMemoryPolicyFinderModule(documentList);
                
        Set<PolicyFinderModule> policyModules = new HashSet<PolicyFinderModule>();
        
        policyModules.add(inMemoryPolicyFinderModule);
        
        finder.setModules(policyModules);
        
        Balana balana = Balana.getInstance();        
        pdpConfig = balana.getPdpConfig();        
        
        AttributeFinder attributeFinder = pdpConfig.getAttributeFinder();
        ResourceFinder resourceFinder = pdpConfig.getResourceFinder();
        
        pdpConfig = new PDPConfig(attributeFinder, finder, resourceFinder, true);
        
        pdp = new PDP(pdpConfig);
    }
}
