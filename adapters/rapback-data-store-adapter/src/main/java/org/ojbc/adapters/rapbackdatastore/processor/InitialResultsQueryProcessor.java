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

import static org.ojbc.util.xml.OjbcNamespaceContext.NS_INTEL_30;
import static org.ojbc.util.xml.OjbcNamespaceContext.NS_JXDM_50;
import static org.ojbc.util.xml.OjbcNamespaceContext.NS_NC_30;
import static org.ojbc.util.xml.OjbcNamespaceContext.NS_ORGANIZATION_IDENTIFICATION_INITIAL_RESULTS_QUERY_RESULTS;
import static org.ojbc.util.xml.OjbcNamespaceContext.NS_ORGANIZATION_IDENTIFICATION_INITIAL_RESULTS_QUERY_RESULTS_EXT;
import static org.ojbc.util.xml.OjbcNamespaceContext.NS_PREFIX_INTEL_30;
import static org.ojbc.util.xml.OjbcNamespaceContext.NS_PREFIX_JXDM_50;
import static org.ojbc.util.xml.OjbcNamespaceContext.NS_PREFIX_NC_30;
import static org.ojbc.util.xml.OjbcNamespaceContext.NS_PREFIX_ORGANIZATION_IDENTIFICATION_INITIAL_RESULTS_QUERY_RESULTS;
import static org.ojbc.util.xml.OjbcNamespaceContext.NS_PREFIX_ORGANIZATION_IDENTIFICATION_INITIAL_RESULTS_QUERY_RESULTS_EXT;
import static org.ojbc.util.xml.OjbcNamespaceContext.NS_PREFIX_PROXY_XSD_30;
import static org.ojbc.util.xml.OjbcNamespaceContext.NS_PREFIX_STRUCTURES_30;
import static org.ojbc.util.xml.OjbcNamespaceContext.NS_PREFIX_WSN_BROKERED;
import static org.ojbc.util.xml.OjbcNamespaceContext.NS_PREFIX_XMIME;
import static org.ojbc.util.xml.OjbcNamespaceContext.NS_PREFIX_XOP;
import static org.ojbc.util.xml.OjbcNamespaceContext.NS_PROXY_XSD_30;
import static org.ojbc.util.xml.OjbcNamespaceContext.NS_STRUCTURES_30;
import static org.ojbc.util.xml.OjbcNamespaceContext.NS_WSN_BROKERED;
import static org.ojbc.util.xml.OjbcNamespaceContext.NS_XMIME;
import static org.ojbc.util.xml.OjbcNamespaceContext.NS_XOP;

import java.util.List;

import javax.xml.parsers.ParserConfigurationException;

import org.apache.camel.Body;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.ojbc.adapters.rapbackdatastore.dao.model.CivilInitialResults;
import org.ojbc.adapters.rapbackdatastore.dao.model.CriminalInitialResults;
import org.ojbc.adapters.rapbackdatastore.dao.model.IdentificationTransaction;
import org.ojbc.util.model.rapback.IdentificationTransactionState;
import org.ojbc.util.xml.XmlUtils;
import org.springframework.stereotype.Service;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

@Service
public class InitialResultsQueryProcessor extends AbstractIdentificationResultsQueryProcessor{
	
	private final Log log = LogFactory.getLog(this.getClass());

    public InitialResultsQueryProcessor() throws ParserConfigurationException {
    	super();
    }

    /**
     * 
     * @param federationId
     * @return a XML string response abide by the Access Control Response
     *         schema.
     * @throws Exception 
     */
    public Document returnInitialResultsQueryResponse(@Body Document report) throws Exception {
    	
    	String transactionNumber = XmlUtils.xPathStringSearch(report, 
    			"/oiirq-req-doc:OrganizationIdentificationInitialResultsQueryRequest"
    			+ "/intel30:SystemIdentification/nc30:IdentificationID");
    	
    	if (StringUtils.isBlank(transactionNumber)){
        	throw new IllegalArgumentException(
        			"The transaction number can not be null to query the initial results. ");
    	}
    	
        Document initialResultsQueryResponseDocument;
		try {
			initialResultsQueryResponseDocument = buildInitialResultsQueryResponse(transactionNumber);
		} catch (Exception e) {
			log.error("Got exception building initial results query response", e);
			throw e;
		}

        return initialResultsQueryResponseDocument;
    }
    
    private Document buildInitialResultsQueryResponse(String transactionNumber) throws Exception {
    	log.info("Get initial results query request, building the response");
    	
    	Document document = documentBuilder.newDocument();
    	Element rootElement = createInitialResultsQueryResponseRootElement(document);
    	
    	String identificationCategoryType = rapbackDAO.getIdentificationCategoryType(transactionNumber);
    	
    	if ("CIVIL".equals(identificationCategoryType)){
    		buildResponseWithCivilInitialResults(transactionNumber, rootElement);
    	}
    	else{
    		buildResponseWithCriminalInitialResults(transactionNumber, rootElement);
    	}
    	
        return document;
    }

	private void buildResponseWithCriminalInitialResults(
			String transactionNumber, Element rootElement) {
		List<CriminalInitialResults> criminalInitialResults = 
    			rapbackDAO.getIdentificationCriminalInitialResults(transactionNumber);
		
        for (CriminalInitialResults criminalInitialResult: criminalInitialResults){
    		IdentificationTransaction identificationTransaction = criminalInitialResult.getIdentificationTransaction();
			appendIdentificationTransactionInfo(rootElement, identificationTransaction, false);
    		createSearchResultDocumentElement(criminalInitialResult, rootElement);
        }
	}

	private void appendIdentificationTransactionInfo(Element rootElement,
			IdentificationTransaction identificationTransaction, boolean isCivilResponse) {
		appendIdentifiedPersonElement(rootElement, identificationTransaction, 
				NS_ORGANIZATION_IDENTIFICATION_INITIAL_RESULTS_QUERY_RESULTS_EXT);
		appendDateElement(identificationTransaction.getTimestamp(), rootElement, 
				"IdentificationReportedDate", NS_ORGANIZATION_IDENTIFICATION_INITIAL_RESULTS_QUERY_RESULTS_EXT);
		appdendStatusElement(rootElement, identificationTransaction, NS_ORGANIZATION_IDENTIFICATION_INITIAL_RESULTS_QUERY_RESULTS_EXT);
		appendReasonCodeElement(isCivilResponse, identificationTransaction,rootElement, NS_ORGANIZATION_IDENTIFICATION_INITIAL_RESULTS_QUERY_RESULTS_EXT);
		appendOrganizationInfo(rootElement, identificationTransaction);
	}

	private void createSearchResultDocumentElement(
			CriminalInitialResults criminalInitialResult, Element parentElement) {
		switch (criminalInitialResult.getResultsSender()){
		case FBI:
			
			appendDocumentElement(parentElement, 
					QueryResponseElementName.FBIIdentificationSearchResultDocument, 
					DocumentId.fbiSearchResultDocument.name(),
					criminalInitialResult.getSearchResultFile());
			break; 
		case State: 
			appendDocumentElement(parentElement, 
					QueryResponseElementName.StateIdentificationSearchResultDocument, 
					DocumentId.stateSearchResultDocument.name(),
					criminalInitialResult.getSearchResultFile());
			break;
		}
	}
	

	private void buildResponseWithCivilInitialResults(String transactionNumber, Element rootElement) {
		List<CivilInitialResults> civilInitialResults = 
    			rapbackDAO.getIdentificationCivilInitialResults(transactionNumber); 
		log.debug("CivilInitialResults: " + civilInitialResults.toString());
        
        for (CivilInitialResults civilInitialResult: civilInitialResults){
        	
    		IdentificationTransaction identificationTransaction = civilInitialResult.getIdentificationTransaction();
			appendIdentificationTransactionInfo(rootElement, identificationTransaction, true);
    		
        	createSearchResultDocumentElement(civilInitialResult, rootElement);
        	createHistorySummaryDocumentElement(civilInitialResult, rootElement);
        }
	}

	private void appdendStatusElement(Element parentElement, IdentificationTransaction identificationTransaction, String extNamespace) {
		Element identificationResultStatusCode = XmlUtils.appendElement(parentElement, 
				extNamespace, "IdentificationResultStatusCode");
		
		IdentificationTransactionState currentState = getCurrentState(identificationTransaction); 
		identificationResultStatusCode.setTextContent(currentState.toString());
		
		Element identificationRequestingOrganization = 
				XmlUtils.appendElement(parentElement,
						extNamespace, "IdentificationRequestingOrganization");
		XmlUtils.addAttribute(identificationRequestingOrganization, NS_STRUCTURES_30, "ref", "ORG1");
		
		appendSubsequentResultsAvailableIndicator(parentElement,
				identificationTransaction.getHavingSubsequentResults(), 
				extNamespace);
	}


	private void createHistorySummaryDocumentElement(CivilInitialResults civilInitialResult, Element parentElement) {
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
			String documentIdString = documentId.name() + "_" + StringUtils.leftPad(String.valueOf(i+1), 3, '0');
			appendDocumentElement(parentElement, 
					queryResponseElementName, 
					documentIdString,
					rapSheets.get(i));
		}
		
	}

	private void createSearchResultDocumentElement(
			CivilInitialResults civilIntialResult, Element parentElement) {
		switch (civilIntialResult.getResultsSender()){
		case FBI:
			
			appendDocumentElement(parentElement, 
					QueryResponseElementName.FBIIdentificationSearchResultDocument, 
					DocumentId.fbiSearchResultDocument.name(),
					civilIntialResult.getSearchResultFile());
			break; 
		case State: 
			appendDocumentElement(parentElement, 
					QueryResponseElementName.StateIdentificationSearchResultDocument, 
					DocumentId.stateSearchResultDocument.name(),
					civilIntialResult.getSearchResultFile());
			break;
		}
	}

	private Element createInitialResultsQueryResponseRootElement(Document document) {
        Element rootElement = document.createElementNS(
        		NS_ORGANIZATION_IDENTIFICATION_INITIAL_RESULTS_QUERY_RESULTS,
        		NS_PREFIX_ORGANIZATION_IDENTIFICATION_INITIAL_RESULTS_QUERY_RESULTS +":" 
        		+ QueryResponseElementName.OrganizationIdentificationInitialResultsQueryResults.name());
        rootElement.setAttribute("xmlns:"+NS_PREFIX_ORGANIZATION_IDENTIFICATION_INITIAL_RESULTS_QUERY_RESULTS, NS_ORGANIZATION_IDENTIFICATION_INITIAL_RESULTS_QUERY_RESULTS);
        rootElement.setAttribute("xmlns:"+NS_PREFIX_ORGANIZATION_IDENTIFICATION_INITIAL_RESULTS_QUERY_RESULTS_EXT, 
        		NS_ORGANIZATION_IDENTIFICATION_INITIAL_RESULTS_QUERY_RESULTS_EXT);
        rootElement.setAttribute("xmlns:"+NS_PREFIX_STRUCTURES_30, NS_STRUCTURES_30);
        rootElement.setAttribute("xmlns:"+NS_PREFIX_NC_30, NS_NC_30);
        rootElement.setAttribute("xmlns:"+NS_PREFIX_XOP, NS_XOP);
        rootElement.setAttribute("xmlns:"+NS_PREFIX_XMIME, NS_XMIME);
        rootElement.setAttribute("xmlns:"+NS_PREFIX_INTEL_30, NS_INTEL_30);
        rootElement.setAttribute("xmlns:"+NS_PREFIX_JXDM_50, NS_JXDM_50);
        rootElement.setAttribute("xmlns:"+NS_PREFIX_PROXY_XSD_30, NS_PROXY_XSD_30);
        rootElement.setAttribute("xmlns:"+NS_PREFIX_WSN_BROKERED, NS_WSN_BROKERED);
        document.appendChild(rootElement);
		return rootElement;
	}
	
	private void appendOrganizationInfo(Element rootElement,
			IdentificationTransaction identificationTransaction) {
		
			Element entityOrganization = XmlUtils.appendElement(rootElement, NS_NC_30, "EntityOrganization");
			XmlUtils.addAttribute(entityOrganization, NS_STRUCTURES_30, "id", "ORG1");
			
			Element organizationName = XmlUtils.appendElement(entityOrganization, NS_NC_30, "OrganizationName");
			organizationName.setTextContent(identificationTransaction.getOwnerAgencyName());
			Element organizationAugmentation = XmlUtils.appendElement(entityOrganization, NS_JXDM_50, "OrganizationAugmentation");
			Element organizationORIIdentification = XmlUtils.appendElement(organizationAugmentation, NS_JXDM_50, "OrganizationORIIdentification");
			Element identificationID = XmlUtils.appendElement(organizationORIIdentification, NS_NC_30, "IdentificationID");
			identificationID.setTextContent(identificationTransaction.getOwnerOri());
	}

}
