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

import static org.ojbc.util.xml.OjbcNamespaceContext.NS_NC_30;
import static org.ojbc.util.xml.OjbcNamespaceContext.NS_ORGANIZATION_IDENTIFICATION_INITIAL_RESULTS_QUERY_RESULTS_EXT;
import static org.ojbc.util.xml.OjbcNamespaceContext.NS_ORGANIZATION_IDENTIFICATION_SUBSEQUENT_RESULTS_QUERY_RESULTS;
import static org.ojbc.util.xml.OjbcNamespaceContext.NS_PREFIX_NC_30;
import static org.ojbc.util.xml.OjbcNamespaceContext.NS_PREFIX_ORGANIZATION_IDENTIFICATION_INITIAL_RESULTS_QUERY_RESULTS_EXT;
import static org.ojbc.util.xml.OjbcNamespaceContext.NS_PREFIX_ORGANIZATION_IDENTIFICATION_SUBSEQUENT_RESULTS_QUERY_RESULTS;
import static org.ojbc.util.xml.OjbcNamespaceContext.NS_PREFIX_STRUCTURES_30;
import static org.ojbc.util.xml.OjbcNamespaceContext.NS_PREFIX_XMIME;
import static org.ojbc.util.xml.OjbcNamespaceContext.NS_PREFIX_XOP;
import static org.ojbc.util.xml.OjbcNamespaceContext.NS_STRUCTURES_30;
import static org.ojbc.util.xml.OjbcNamespaceContext.NS_XMIME;
import static org.ojbc.util.xml.OjbcNamespaceContext.NS_XOP;

import java.util.List;

import javax.xml.parsers.ParserConfigurationException;

import org.apache.camel.Body;
import org.apache.camel.Exchange;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.ojbc.intermediaries.sn.dao.rapback.SubsequentResults;
import org.ojbc.util.xml.XmlUtils;
import org.springframework.stereotype.Service;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

@Service
public class SubsequentResultsQueryProcessor extends AbstractIdentificationResultsQueryProcessor{
	
	private final Log log = LogFactory.getLog(this.getClass());

    public SubsequentResultsQueryProcessor() throws ParserConfigurationException {
    	super();
    }

    /**
     * 
     * @param federationId
     * @return a XML string response abide by the Access Control Response
     *         schema.
     * @throws Exception 
     */
    public Document returnSubsequentResultsQueryResponse(
    		Exchange exchange, @Body Document report) throws Exception {
    	
    	String transactionNumber = XmlUtils.xPathStringSearch(report, "/oisrq-req-doc:OrganizationIdentificationSubsequentResultsQueryRequest/intel30:SystemIdentification/nc30:IdentificationID");
    	
    	if (StringUtils.isBlank(transactionNumber)){
        	throw new IllegalArgumentException(
        			"The transaction number can not be null to query the subsequent results. ");
    	}
    	
        Document subseqentResultsQueryResponseDocument;
		try {
			subseqentResultsQueryResponseDocument = buildSubsequentResultsQueryResponse(exchange, transactionNumber);
		} catch (Exception e) {
			log.error("Got exception building initial results query response", e);
			throw e;
		}

        return subseqentResultsQueryResponseDocument;
    }
    
    private Document buildSubsequentResultsQueryResponse(Exchange exchange, String transactionNumber) throws Exception {
    	log.info("Get subsequent results query request, building the response");
    	
    	List<SubsequentResults> subsequentResults = 
    			rapbackDAO.getSubsequentResults(transactionNumber); 
        Document document = documentBuilder.newDocument();
        Element rootElement = createSubsequentResultsQueryResponseRootElement(exchange, document);
        
        for (SubsequentResults subsequentResult: subsequentResults){
        	createHistorySummaryDocumentElement(exchange, subsequentResult, rootElement);
        }
        return document;
    }


	private void createHistorySummaryDocumentElement(Exchange exchange,
			SubsequentResults subsequentResult, Element parentElement) {

		QueryResponseElementName queryResponseElementName;
		DocumentId documentId;
		
		switch(subsequentResult.getResultsSender()){
		case FBI:
			queryResponseElementName = QueryResponseElementName.FBIIdentityHistorySummaryDocument; 
			documentId = DocumentId.fbiIdentityHistorySummaryDocument;
			break; 
		case State:
		default:
			queryResponseElementName = QueryResponseElementName.StateCriminalHistoryRecordDocument; 
			documentId = DocumentId.stateCriminalHistoryRecordDocument;
		}
		
		byte[] rapSheet = subsequentResult.getRapSheet(); 
		String documentIdString = documentId.name() + "_" + StringUtils.leftPad(subsequentResult.getId().toString(), 8,'0');
		appendDocumentElement(parentElement, 
				queryResponseElementName, 
				documentIdString,
				rapSheet);
	}

	private Element createSubsequentResultsQueryResponseRootElement(
			Exchange exchange, Document document) {
        Element rootElement = document.createElementNS(
        		NS_ORGANIZATION_IDENTIFICATION_SUBSEQUENT_RESULTS_QUERY_RESULTS,
        		NS_PREFIX_ORGANIZATION_IDENTIFICATION_SUBSEQUENT_RESULTS_QUERY_RESULTS +":" 
        		+ QueryResponseElementName.OrganizationIdentificationSubsequentResultsQueryResults.name());
        rootElement.setAttribute("xmlns:"+NS_PREFIX_ORGANIZATION_IDENTIFICATION_SUBSEQUENT_RESULTS_QUERY_RESULTS, NS_ORGANIZATION_IDENTIFICATION_SUBSEQUENT_RESULTS_QUERY_RESULTS);
        rootElement.setAttribute("xmlns:"+NS_PREFIX_ORGANIZATION_IDENTIFICATION_INITIAL_RESULTS_QUERY_RESULTS_EXT, 
        		NS_ORGANIZATION_IDENTIFICATION_INITIAL_RESULTS_QUERY_RESULTS_EXT);
        rootElement.setAttribute("xmlns:"+NS_PREFIX_STRUCTURES_30, NS_STRUCTURES_30);
        rootElement.setAttribute("xmlns:"+NS_PREFIX_NC_30, NS_NC_30);
        rootElement.setAttribute("xmlns:"+NS_PREFIX_XOP, NS_XOP);
        rootElement.setAttribute("xmlns:"+NS_PREFIX_XMIME, NS_XMIME);
        document.appendChild(rootElement);
		return rootElement;
	}
}
