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
package org.ojbc.adapters.rapbackdatastore.processor;

import static org.ojbc.util.xml.OjbcNamespaceContext.NS_NC_30;
import static org.ojbc.util.xml.OjbcNamespaceContext.NS_ORGANIZATION_IDENTIFICATION_INITIAL_RESULTS_QUERY_RESULTS;
import static org.ojbc.util.xml.OjbcNamespaceContext.NS_ORGANIZATION_IDENTIFICATION_INITIAL_RESULTS_QUERY_RESULTS_EXT;
import static org.ojbc.util.xml.OjbcNamespaceContext.NS_ORGANIZATION_IDENTIFICATION_RESULTS_SEARCH_RESULTS_EXT;
import static org.ojbc.util.xml.OjbcNamespaceContext.NS_PREFIX_ORGANIZATION_IDENTIFICATION_INITIAL_RESULTS_QUERY_RESULTS;
import static org.ojbc.util.xml.OjbcNamespaceContext.NS_PREFIX_ORGANIZATION_IDENTIFICATION_INITIAL_RESULTS_QUERY_RESULTS_EXT;
import static org.ojbc.util.xml.OjbcNamespaceContext.NS_PREFIX_ORGANIZATION_IDENTIFICATION_RESULTS_SEARCH_RESULTS_EXT;
import static org.ojbc.util.xml.OjbcNamespaceContext.NS_PREFIX_XOP;
import static org.ojbc.util.xml.OjbcNamespaceContext.NS_STRUCTURES_30;
import static org.ojbc.util.xml.OjbcNamespaceContext.NS_XMIME;
import static org.ojbc.util.xml.OjbcNamespaceContext.NS_XOP;

import java.util.List;

import javax.activation.DataHandler;
import javax.annotation.Resource;
import javax.mail.util.ByteArrayDataSource;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.apache.camel.Body;
import org.apache.camel.Exchange;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.ojbc.adapters.rapbackdatastore.dao.RapbackDAO;
import org.ojbc.adapters.rapbackdatastore.dao.model.CivilInitialResults;
import org.ojbc.util.xml.XmlUtils;
import org.springframework.stereotype.Service;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

@Service
public class InitialResultsQueryProcessor {
	
	private enum QueryResponseElementName{
		OrganizationIdentificationInitialResultsQueryResults, 
		StateIdentificationSearchResultDocument,
		FBIIdentificationSearchResultDocument,
		StateCriminalHistoryRecordDocument,
		FBIIdentityHistorySummaryDocument,
		DocumentBinary,
		Include,
	}
	
	private enum DocumentId{
		fbiSearchResultDocument, 
		fbiIdentityHistorySummaryDocument,
		stateCriminalHistoryRecordDocument,
		stateSearchResultDocument
	}

	private static final String CONTENT_TYPE_TEXT_PLAIN = "text/plain";
	
	private static final String CID = "cid:";

	private static final String ATTACHMENT_URL_FORE_STRING = "http://ojbc.org/identification/results/";

	private final Log log = LogFactory.getLog(this.getClass());

    @Resource
    private RapbackDAO rapbackDAO;

    private DocumentBuilder documentBuilder;

    public InitialResultsQueryProcessor() throws ParserConfigurationException {
        DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
        documentBuilderFactory.setNamespaceAware(true);
        documentBuilder = documentBuilderFactory.newDocumentBuilder();
    }

    /**
     * 
     * @param federationId
     * @return a XML string response abide by the Access Control Response
     *         schema.
     * @throws Exception 
     */
    public Document returnRapbackSearchResponse(
    		Exchange exchange, @Body Document report) throws Exception {
    	
    	String transactionNumber = XmlUtils.xPathStringSearch(report, "/oiirq-req-doc:OrganizationIdentificationInitialResultsQueryRequest/intel30:SystemIdentification/nc30:IdentificationID");
    	
    	if (StringUtils.isBlank(transactionNumber)){
        	throw new IllegalArgumentException(
        			"The transaction number can not be null to query the initial results. ");
    	}
    	
        Document initialResultsQueryResponseDocument;
		try {
			initialResultsQueryResponseDocument = buildInitialResultsQueryResponse(exchange, transactionNumber);
		} catch (Exception e) {
			log.error("Got exception building initial results query response", e);
			throw e;
		}

        return initialResultsQueryResponseDocument;
    }
    
    private Document buildInitialResultsQueryResponse(Exchange exchange, String transactionNumber) throws Exception {
    	log.info("Get initial results query request, building the response");
    	
    	List<CivilInitialResults> civilInitialResults = 
    			rapbackDAO.getIdentificationCivilInitialResults(transactionNumber); 
        Document document = documentBuilder.newDocument();
        Element rootElement = createInitialResultsQueryResponseRootElement(exchange, document);
        
        for (CivilInitialResults civilInitialResult: civilInitialResults){
        	createSearchResultDocumentElement(exchange, civilInitialResult, rootElement);
        	createHistorySummaryDocumentElement(exchange, civilInitialResult, rootElement);
        }
        return document;
    }


	private void createHistorySummaryDocumentElement(Exchange exchange,
			CivilInitialResults civilInitialResult, Element parentElement) {
		if (civilInitialResult.getRapsheets().size() ==0)
			return; 

		QueryResponseElementName queryResponseElementName;
		DocumentId documentId;
		switch(civilInitialResult.getResultsSender()){
		case FBI:
			queryResponseElementName = QueryResponseElementName.FBIIdentityHistorySummaryDocument; 
			documentId = DocumentId.fbiIdentityHistorySummaryDocument;
			break; 
		case State:
		default:
			queryResponseElementName = QueryResponseElementName.StateCriminalHistoryRecordDocument; 
			documentId = DocumentId.stateCriminalHistoryRecordDocument;
		}
		
		List<byte[]> rapSheets = civilInitialResult.getRapsheets(); 
		for (int i=0; i < rapSheets.size(); i++){
			String documentIdString = documentId.name() + StringUtils.leftPad(String.valueOf(i+1), 3, '0');
			addAttachment(exchange, rapSheets.get(i), documentIdString);
			appendDocumentElement(parentElement, 
					queryResponseElementName, 
					documentIdString);
		}
		
	}

	private void createSearchResultDocumentElement(Exchange exchange,
			CivilInitialResults civilIntialResult, Element parentElement) {
		switch (civilIntialResult.getResultsSender()){
		case FBI:
			
			addAttachment(exchange, civilIntialResult.getSearchResultFile(), 
					DocumentId.fbiSearchResultDocument.name());
			appendDocumentElement(parentElement, 
					QueryResponseElementName.FBIIdentificationSearchResultDocument, 
					DocumentId.fbiSearchResultDocument.name());
			break; 
		case State: 
			addAttachment(exchange, civilIntialResult.getSearchResultFile(), 
					DocumentId.stateSearchResultDocument.name());
			appendDocumentElement(parentElement, 
					QueryResponseElementName.StateIdentificationSearchResultDocument, 
					DocumentId.stateSearchResultDocument.name());
			break;
		}
	}

	private void appendDocumentElement(Element parentElement, QueryResponseElementName elementName, String documentId) {
		Element fbiIdentificationSearchResultDocument = 
			XmlUtils.appendElement(parentElement, 
					NS_ORGANIZATION_IDENTIFICATION_INITIAL_RESULTS_QUERY_RESULTS_EXT, 
					elementName.name());
		XmlUtils.addAttribute(fbiIdentificationSearchResultDocument, NS_STRUCTURES_30, "id", documentId );
		Element documentBinary = 
			XmlUtils.appendElement(fbiIdentificationSearchResultDocument, 
					NS_ORGANIZATION_IDENTIFICATION_INITIAL_RESULTS_QUERY_RESULTS_EXT, 
					QueryResponseElementName.DocumentBinary.name());
		XmlUtils.addAttribute(documentBinary, NS_XMIME, "contentType", CONTENT_TYPE_TEXT_PLAIN);
		Element include = 
			XmlUtils.appendElement(documentBinary, NS_NC_30, QueryResponseElementName.Include.name());
		String hrefValue = getHrefValue(documentId);
		XmlUtils.addAttribute(include, NS_XOP, "href", hrefValue);
	}

	private void addAttachment(Exchange exchange,
			byte[] attachment, String documentId) {
		String attachmentId = ATTACHMENT_URL_FORE_STRING + documentId;  
		exchange.getIn().addAttachment(attachmentId, new DataHandler(new ByteArrayDataSource(attachment, CONTENT_TYPE_TEXT_PLAIN)));
	}

	private String getHrefValue(String documentId) {
		String attachmentId = CID + ATTACHMENT_URL_FORE_STRING + documentId;
		return attachmentId;
	}

	private Element createInitialResultsQueryResponseRootElement(
			Exchange exchange, Document document) {
        Element rootElement = document.createElementNS(
        		NS_ORGANIZATION_IDENTIFICATION_INITIAL_RESULTS_QUERY_RESULTS,
        		NS_PREFIX_ORGANIZATION_IDENTIFICATION_INITIAL_RESULTS_QUERY_RESULTS +":" 
        		+ QueryResponseElementName.OrganizationIdentificationInitialResultsQueryResults.name());
        rootElement.setAttribute("xmlns:"+NS_PREFIX_ORGANIZATION_IDENTIFICATION_INITIAL_RESULTS_QUERY_RESULTS, NS_ORGANIZATION_IDENTIFICATION_INITIAL_RESULTS_QUERY_RESULTS);
        rootElement.setAttribute("xmlns:"+NS_PREFIX_ORGANIZATION_IDENTIFICATION_INITIAL_RESULTS_QUERY_RESULTS_EXT, 
        		NS_ORGANIZATION_IDENTIFICATION_INITIAL_RESULTS_QUERY_RESULTS_EXT);
        rootElement.setAttribute("xmlns:"+NS_PREFIX_ORGANIZATION_IDENTIFICATION_RESULTS_SEARCH_RESULTS_EXT, 
        		NS_ORGANIZATION_IDENTIFICATION_RESULTS_SEARCH_RESULTS_EXT);
        rootElement.setAttribute("xmlns:"+NS_PREFIX_XOP, NS_XOP);
        document.appendChild(rootElement);
		return rootElement;
	}
}
