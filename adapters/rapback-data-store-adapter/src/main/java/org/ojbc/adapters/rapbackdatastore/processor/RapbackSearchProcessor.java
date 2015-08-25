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

import static org.ojbc.util.xml.OjbcNamespaceContext.NS_INTEL;
import static org.ojbc.util.xml.OjbcNamespaceContext.NS_JXDM_50;
import static org.ojbc.util.xml.OjbcNamespaceContext.NS_NC_30;
import static org.ojbc.util.xml.OjbcNamespaceContext.NS_ORGANIZATION_IDENTIFICATION_RESULTS_SEARCH_RESULTS;
import static org.ojbc.util.xml.OjbcNamespaceContext.NS_ORGANIZATION_IDENTIFICATION_RESULTS_SEARCH_RESULTS_EXT;
import static org.ojbc.util.xml.OjbcNamespaceContext.NS_PREFIX_INTEL;
import static org.ojbc.util.xml.OjbcNamespaceContext.NS_PREFIX_JXDM_50;
import static org.ojbc.util.xml.OjbcNamespaceContext.NS_PREFIX_NC_30;
import static org.ojbc.util.xml.OjbcNamespaceContext.NS_PREFIX_ORGANIZATION_IDENTIFICATION_RESULTS_SEARCH_RESULTS;
import static org.ojbc.util.xml.OjbcNamespaceContext.NS_PREFIX_ORGANIZATION_IDENTIFICATION_RESULTS_SEARCH_RESULTS_EXT;
import static org.ojbc.util.xml.OjbcNamespaceContext.NS_PREFIX_SEARCH_REQUEST_ERROR_REPORTING;
import static org.ojbc.util.xml.OjbcNamespaceContext.NS_PREFIX_SEARCH_RESULTS_METADATA_EXT;
import static org.ojbc.util.xml.OjbcNamespaceContext.NS_PREFIX_STRUCTURES;
import static org.ojbc.util.xml.OjbcNamespaceContext.NS_PREFIX_WSN_BROKERED;
import static org.ojbc.util.xml.OjbcNamespaceContext.NS_SEARCH_REQUEST_ERROR_REPORTING;
import static org.ojbc.util.xml.OjbcNamespaceContext.NS_SEARCH_RESULTS_METADATA_EXT;
import static org.ojbc.util.xml.OjbcNamespaceContext.NS_STRUCTURES;
import static org.ojbc.util.xml.OjbcNamespaceContext.NS_WSN_BROKERED;

import java.util.List;

import javax.annotation.Resource;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.apache.camel.Body;
import org.apache.camel.ExchangeException;
import org.apache.camel.Header;
import org.apache.camel.component.cxf.common.message.CxfConstants;
import org.apache.commons.lang.BooleanUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.cxf.message.Message;
import org.ojbc.adapters.rapbackdatastore.dao.RapbackDAO;
import org.ojbc.adapters.rapbackdatastore.dao.model.IdentificationTransaction;
import org.ojbc.adapters.rapbackdatastore.dao.model.IdentificationTransactionState;
import org.ojbc.adapters.rapbackdatastore.dao.model.Subject;
import org.ojbc.util.camel.security.saml.SAMLTokenUtils;
import org.ojbc.util.model.saml.SamlAttribute;
import org.ojbc.util.xml.XmlUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

@Service
public class RapbackSearchProcessor {
    private static final String SYSTEM_NAME = "RapbackDataStore";

	private static final String SOURCE_SYSTEM_NAME_TEXT = 
    		"http://ojbc.org/Services/WSDL/Organization_Identification_Results_Search_Request_Service/Subscriptions/1.0}RapbackDatastore";

	private static final String YYYY_MM_DD = "yyyy-MM-dd";

	private final Log log = LogFactory.getLog(this.getClass());

    @Resource
    private RapbackDAO rapbackDAO;

    private DocumentBuilder documentBuilder;

    @Value("${system.searchResultsExceedThreshold}")
    private Integer maxResultCount;

    @Value("${system.name}")
    private String systemName;

    public RapbackSearchProcessor() throws ParserConfigurationException {
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
    		@Header(CxfConstants.CAMEL_CXF_MESSAGE) Message cxfMessage, @Body Document report) throws Exception {
    	
    	
    	if (cxfMessage == null) {
    		throw new IllegalArgumentException(
    				"Invalid request. CXF message is not found.");
    	}
    	
        String federationId = SAMLTokenUtils.getSamlAttributeFromCxfMessage(cxfMessage, SamlAttribute.FederationId);
        String employerOri = SAMLTokenUtils.getSamlAttributeFromCxfMessage(cxfMessage, SamlAttribute.EmployerORI); 

        log.debug("Processing rapback search request for federationId:" + StringUtils.trimToEmpty(federationId));
        log.debug("Employer ORI : " + StringUtils.trimToEmpty(employerOri)); 
        
        if (StringUtils.isBlank(federationId) || StringUtils.isBlank(employerOri)) {
        	throw new IllegalArgumentException(
        			"Either request is missing SAML assertion or the federation "
        					+ "ID or Employer ORI is missing in the SAML assertion. ");
        } 
        
        Document rapbackSearchResponseDocument = buildRapbackSearchResponse(employerOri, report);

        return rapbackSearchResponseDocument;
    }
    
    private Document buildRapbackSearchResponse(String employerOri, Document report) throws Exception {
    	log.info("Get rapback search results");

        Document document = documentBuilder.newDocument();
        Element rootElement = createRapbackSearchResponseRootElement(document);
        
        String resultsCategoryCode = XmlUtils.xPathStringSearch(report, 
        		"/oirs-req-doc:OrganizationIdentificationResultsSearchRequest/oirs-req-ext:IdentificationResultsCategoryCode"); 
        List<IdentificationTransaction> identificationTransactions = null;
        if ("Civil".equals(resultsCategoryCode)){
        	identificationTransactions = rapbackDAO.getCivilIdentificationTransactions(employerOri);
        }
        else if ("Criminal".equals(resultsCategoryCode)){
        	identificationTransactions = rapbackDAO.getCriminalIdentificationTransactions(employerOri);
        }
        buildSearchResults(identificationTransactions, rootElement);
        return document;
    }

	private void buildSearchResults(
			List<IdentificationTransaction> identificationTransactions,
			Element rootElement) {
		
		if (identificationTransactions.size() > maxResultCount){
			buildTooManyResultElement(identificationTransactions.size(), rootElement); 
		}
		else{
			for (IdentificationTransaction identificationTransaction : identificationTransactions){
				Element organizationIdentificationResultsSearchResultElement = 
						XmlUtils.appendElement(rootElement, NS_ORGANIZATION_IDENTIFICATION_RESULTS_SEARCH_RESULTS_EXT, 
								"OrganizationIdentificationResultsSearchResult");
				appendIdentifiedPersonElement(organizationIdentificationResultsSearchResultElement, identificationTransaction);
				appdendStatusElement(organizationIdentificationResultsSearchResultElement,
						identificationTransaction);
				//TODO add subscripion info if available. 
				appendSourceSystemNameTextElement(organizationIdentificationResultsSearchResultElement);
				
				Element systemIdentifierElement = XmlUtils.appendElement(
						organizationIdentificationResultsSearchResultElement, NS_INTEL, "SystemIdentifierElement");
				Element identificationIdElement = XmlUtils.appendElement(systemIdentifierElement, NS_NC_30, "IdentificationID"); 
				identificationIdElement.setTextContent(identificationTransaction.getTransactionNumber());  
				Element systemNameElement = XmlUtils.appendElement(systemIdentifierElement, NS_INTEL, "SystemName");
				systemNameElement.setTextContent(SYSTEM_NAME);
			}
		}
	}

	private void buildTooManyResultElement(int size, Element rootElement) {
		Element searchResultsMetadata = XmlUtils.appendElement(rootElement, NS_SEARCH_RESULTS_METADATA_EXT, "SearchResultsMetadata"); 
		Element searchErrors = XmlUtils.appendElement(searchResultsMetadata, NS_SEARCH_REQUEST_ERROR_REPORTING, "SearchErrors"); 
		Element systemName = XmlUtils.appendElement(searchErrors, NS_NC_30, "SystemName");
		systemName.setTextContent(SYSTEM_NAME);
		Element searchResultsExceedThresholdError = 
				XmlUtils.appendElement(searchErrors, NS_SEARCH_REQUEST_ERROR_REPORTING, "SearchResultsExceedThresholdError");
		Element searchReultsRecordCount = 
				XmlUtils.appendElement(searchResultsExceedThresholdError, NS_SEARCH_REQUEST_ERROR_REPORTING, "SearchResultsRecordCount");
		searchReultsRecordCount.setTextContent(String.valueOf(size));
	}

	private void appendSourceSystemNameTextElement(
			Element organizationIdentificationResultsSearchResultElement) {
		Element sourceSystemNameText = XmlUtils.appendElement(organizationIdentificationResultsSearchResultElement, 
				NS_ORGANIZATION_IDENTIFICATION_RESULTS_SEARCH_RESULTS_EXT, "SourceSystemNameText");
		sourceSystemNameText.setTextContent(SOURCE_SYSTEM_NAME_TEXT);
	}

	private void appdendStatusElement(
			Element organizationIdentificationResultsSearchResultElement,
			IdentificationTransaction identificationTransaction) {
		Element identificationResultStatusCode = XmlUtils.appendElement(
				organizationIdentificationResultsSearchResultElement, 
				NS_ORGANIZATION_IDENTIFICATION_RESULTS_SEARCH_RESULTS_EXT, "IdentificationResultStatusCode");
		identificationResultStatusCode.setTextContent(getCurrentState(identificationTransaction));
	}

	private String getCurrentState(
			IdentificationTransaction identificationTransaction) {
		if (BooleanUtils.isTrue(identificationTransaction.getArchived())){
			return IdentificationTransactionState.Archived.toString();
		}
		else {
			//TODO need to check the subscription state to decide the state.
			return IdentificationTransactionState.Available_for_subscription.toString();
		}
	}

	private void appendIdentifiedPersonElement(Element organizationIdentificationResultsSearchResultElement,
			IdentificationTransaction identificationTransaction) {
		
		Subject subject = identificationTransaction.getSubject();
		Element identifiedPerson = XmlUtils.appendElement(organizationIdentificationResultsSearchResultElement, 
				NS_ORGANIZATION_IDENTIFICATION_RESULTS_SEARCH_RESULTS_EXT, "IdentifiedPerson");
		
		if (subject.getDob() != null){
			Element personBirthDateElement = 
					XmlUtils.appendElement(identifiedPerson, NS_NC_30, "PersonBirthDate");
			Element dateElement = XmlUtils.appendElement(personBirthDateElement, NS_NC_30, "Date");
			dateElement.setTextContent(subject.getDob().toString(YYYY_MM_DD));
		}
		
		Element personNameElement = XmlUtils.appendElement(identifiedPerson, NS_NC_30, "PersonName"); 
		Element personFullNameElement = XmlUtils.appendElement(personNameElement, NS_NC_30, "PersonFullName");
		personFullNameElement.setTextContent(subject.getFullName());
		
		Element identifiedPersonTrackingIdentification = XmlUtils.appendElement(identifiedPerson, 
				NS_PREFIX_ORGANIZATION_IDENTIFICATION_RESULTS_SEARCH_RESULTS_EXT, "IdentifiedPersonTrackingIdentification");
		Element identificationIdElement = XmlUtils.appendElement(
				identifiedPersonTrackingIdentification, NS_NC_30, "IdentificationID");
		identificationIdElement.setTextContent(identificationTransaction.getOtn());
	}

    private Element createRapbackSearchResponseRootElement(Document document) {
        Element rootElement = document.createElementNS(
        		NS_ORGANIZATION_IDENTIFICATION_RESULTS_SEARCH_RESULTS,
        		NS_PREFIX_ORGANIZATION_IDENTIFICATION_RESULTS_SEARCH_RESULTS +"OrganizationIdentificationResultsSearchResults");
        rootElement.setAttribute("xmlns:"+NS_PREFIX_STRUCTURES, NS_STRUCTURES);
        rootElement.setAttribute("xmlns:"+NS_PREFIX_ORGANIZATION_IDENTIFICATION_RESULTS_SEARCH_RESULTS, 
        		NS_ORGANIZATION_IDENTIFICATION_RESULTS_SEARCH_RESULTS);
        rootElement.setAttribute("xmlns:"+NS_PREFIX_ORGANIZATION_IDENTIFICATION_RESULTS_SEARCH_RESULTS_EXT, 
        		NS_ORGANIZATION_IDENTIFICATION_RESULTS_SEARCH_RESULTS_EXT);
        rootElement.setAttribute("xmlns:"+NS_PREFIX_INTEL, NS_INTEL);
        rootElement.setAttribute("xmlns:"+NS_PREFIX_JXDM_50, NS_JXDM_50);
        rootElement.setAttribute("xmlns:"+NS_PREFIX_NC_30, NS_NC_30);
        rootElement.setAttribute("xmlns:"+NS_PREFIX_WSN_BROKERED, NS_WSN_BROKERED);
        document.appendChild(rootElement);
		return rootElement;
	}

    public Document buildErrorResponse(@ExchangeException Exception exception) {
        Document document = documentBuilder.newDocument();
        Element rootElement = createErrorResponseRootElement(document);

        Element searchResultsMetadataNode = XmlUtils.appendElement(rootElement,
        		NS_SEARCH_RESULTS_METADATA_EXT, "SearchResultsMetadata");

        Element searchRequestErrorNode = XmlUtils.appendElement(searchResultsMetadataNode, 
        		NS_SEARCH_REQUEST_ERROR_REPORTING, "SearchRequestError");

        Element errorTextNode = XmlUtils.appendElement(searchRequestErrorNode,
        		NS_SEARCH_REQUEST_ERROR_REPORTING, "ErrorText");
        errorTextNode.setTextContent(exception.getMessage());

        Element systemNameNode = XmlUtils.appendElement(searchRequestErrorNode, NS_INTEL,
                "SystemName");
        systemNameNode.setTextContent(systemName);
        return document;
    }

    private Element createErrorResponseRootElement(Document document) {
        Element rootElement = document.createElementNS(
        		NS_ORGANIZATION_IDENTIFICATION_RESULTS_SEARCH_RESULTS,
        		NS_PREFIX_ORGANIZATION_IDENTIFICATION_RESULTS_SEARCH_RESULTS +"OrganizationIdentificationResultsSearchResults");
        rootElement.setAttribute("xmlns:"+NS_PREFIX_STRUCTURES, NS_STRUCTURES);
        rootElement.setAttribute("xmlns:"+NS_PREFIX_ORGANIZATION_IDENTIFICATION_RESULTS_SEARCH_RESULTS, 
        		NS_ORGANIZATION_IDENTIFICATION_RESULTS_SEARCH_RESULTS);
        rootElement.setAttribute("xmlns:"+NS_PREFIX_INTEL, NS_INTEL);
        rootElement.setAttribute("xmlns:"+NS_PREFIX_SEARCH_RESULTS_METADATA_EXT, NS_SEARCH_RESULTS_METADATA_EXT);
        rootElement.setAttribute("xmlns:"+NS_PREFIX_SEARCH_REQUEST_ERROR_REPORTING, NS_SEARCH_REQUEST_ERROR_REPORTING);

        document.appendChild(rootElement);
        return rootElement;
    }


}
