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

import static org.ojbc.util.xml.OjbcNamespaceContext.*;

import java.util.List;

import javax.xml.parsers.ParserConfigurationException;

import org.apache.camel.Body;
import org.apache.camel.Exchange;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.ojbc.adapters.rapbackdatastore.dao.model.NsorFiveYearCheck;
import org.ojbc.util.xml.OjbcNamespaceContext;
import org.ojbc.util.xml.XmlUtils;
import org.springframework.stereotype.Service;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

@Service
public class NsorFiveYearchCheckResultsQueryProcessor extends AbstractIdentificationResultsQueryProcessor{
	
	private final Log log = LogFactory.getLog(this.getClass());

    public NsorFiveYearchCheckResultsQueryProcessor() throws ParserConfigurationException {
    	super();
    }

    /**
     * 
     * @param federationId
     * @return a XML string response abide by the Access Control Response
     *         schema.
     * @throws Exception 
     */
    public Document returnNsorFiveYearCheckQueryResponse(@Body Document report) throws Exception {
    	
    	String transactionNumber = XmlUtils.xPathStringSearch(report, "//intel30:SystemIdentification/nc30:IdentificationID");
    	
    	if (StringUtils.isBlank(transactionNumber)){
        	throw new IllegalArgumentException(
        			"The transaction number can not be null to query the NSOR 5 year check results. ");
    	}
    	
        Document nsorFiveYearCheckQueryResponseDocument;
		try {
			nsorFiveYearCheckQueryResponseDocument = buildNsorFiveYearCheckQueryResponse(transactionNumber);
		} catch (Exception e) {
			log.error("Got exception building nsor 5 year check query response", e);
			throw e;
		}

        return nsorFiveYearCheckQueryResponseDocument;
    }
    
    private Document buildNsorFiveYearCheckQueryResponse(String transactionNumber) throws Exception {
    	log.info("Got NSOR Five Year Check query request, building the response for transaction number: " + transactionNumber);
    	
    	List<NsorFiveYearCheck> nsorFiveYearChecks = 
    			rapbackDAO.getNsorFiveYearChecks(transactionNumber); 
        Document document = documentBuilder.newDocument();
        Element rootElement = createNsorFiveYearCheckQueryResponseRootElement(document);
        
        int i = 0;
        
        //Add summary data here
//    	<oirq-res-ext:NsorFiveYearCheckResultsAvailableIndicator>true</oirq-res-ext:NsorFiveYearCheckResultsAvailableIndicator>
//    	<oirq-res-ext:SourceSystemNameText>http://ojbc.org/Services/WSDL/Organization_Identification_Results_Search_Request_Service/Subscriptions/1.0}RapbackDatastore</oirq-res-ext:SourceSystemNameText>
//    	<intel:SystemIdentification>
//    		<nc:IdentificationID>123456</nc:IdentificationID>
//    		<nc:SystemName>RapbackDataStore</nc:SystemName>
//    	</intel:SystemIdentification>        
        
        XmlUtils.appendTextElement(rootElement, NS_ORGANIZATION_IDENTIFICATION_INITIAL_RESULTS_QUERY_RESULTS_EXT, "oirq-res-ext:NsorFiveYearCheckResultsAvailableIndicator", "true");
        XmlUtils.appendTextElement(rootElement, NS_ORGANIZATION_IDENTIFICATION_INITIAL_RESULTS_QUERY_RESULTS_EXT, "oirq-res-ext:SourceSystemNameText", "http://ojbc.org/Services/WSDL/Organization_Identification_Results_Search_Request_Service/Subscriptions/1.0}RapbackDatastore");
        
		Element SystemIdentification = XmlUtils.appendElement(rootElement, OjbcNamespaceContext.NS_INTEL_30, "intel:SystemIdentification");
		
		XmlUtils.appendTextElement(SystemIdentification, OjbcNamespaceContext.NS_NC_30, "nc30:IdentificationID", transactionNumber);
		XmlUtils.appendTextElement(SystemIdentification, OjbcNamespaceContext.NS_NC_30, "nc30:SystemName", "RapbackDataStore");        
        
        for (NsorFiveYearCheck nsorFiveYearCheck: nsorFiveYearChecks){
        	createNsorFiveYearCheckDocumentElement(nsorFiveYearCheck, rootElement, i);
        	i++;
        }
        return document;
    }


	private void createNsorFiveYearCheckDocumentElement(NsorFiveYearCheck nsorFiveYearCheck, Element parentElement, int i) {

		QueryResponseElementName queryResponseElementName = QueryResponseElementName.NsorFiveYearCheckDocument;
		
		byte[] results = nsorFiveYearCheck.getResultsFile(); 
		String documentIdString = "Document_" + String.valueOf(i);
		appendDocumentElement(parentElement, 
				queryResponseElementName, 
				documentIdString,
				results);
	}

	private Element createNsorFiveYearCheckQueryResponseRootElement(Document document) {
        Element rootElement = document.createElementNS(
        		NS_ORGANIZATION_IDENTIFICATION_NSOR_QUERY_RESULTS_DOC,
        		NS_PREFIX_ORGANIZATION_IDENTIFICATION_NSOR_QUERY_RESULTS_DOC +":" 
        		+ QueryResponseElementName.OrganizationIdentificationNsorQueryResults.name());
        rootElement.setAttribute("xmlns:"+NS_PREFIX_ORGANIZATION_IDENTIFICATION_INITIAL_RESULTS_QUERY_RESULTS_EXT, 
        		NS_ORGANIZATION_IDENTIFICATION_INITIAL_RESULTS_QUERY_RESULTS_EXT);
        rootElement.setAttribute("xmlns:"+NS_PREFIX_STRUCTURES_30, NS_STRUCTURES_30);
        rootElement.setAttribute("xmlns:"+NS_PREFIX_NC_30, NS_NC_30);
        rootElement.setAttribute("xmlns:"+NS_PREFIX_JXDM_50, NS_JXDM_50);
        rootElement.setAttribute("xmlns:"+NS_PREFIX_INTEL_30, NS_INTEL_30);
        document.appendChild(rootElement);
		return rootElement;
	}
}


