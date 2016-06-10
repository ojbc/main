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

import static org.ojbc.adapters.rapbackdatastore.RapbackDataStoreAdapterConstants.SOURCE_SYSTEM_NAME_TEXT;
import static org.ojbc.adapters.rapbackdatastore.RapbackDataStoreAdapterConstants.SYSTEM_NAME;
import static org.ojbc.adapters.rapbackdatastore.RapbackDataStoreAdapterConstants.TOPIC_DIALECT;
import static org.ojbc.adapters.rapbackdatastore.RapbackDataStoreAdapterConstants.YYYY_MM_DD;
import static org.ojbc.util.xml.OjbcNamespaceContext.NS_INTEL_30;
import static org.ojbc.util.xml.OjbcNamespaceContext.NS_JXDM_50;
import static org.ojbc.util.xml.OjbcNamespaceContext.NS_NC_30;
import static org.ojbc.util.xml.OjbcNamespaceContext.NS_ORGANIZATION_IDENTIFICATION_RESULTS_SEARCH_RESULTS;
import static org.ojbc.util.xml.OjbcNamespaceContext.NS_ORGANIZATION_IDENTIFICATION_RESULTS_SEARCH_RESULTS_EXT;
import static org.ojbc.util.xml.OjbcNamespaceContext.NS_PREFIX_INTEL_30;
import static org.ojbc.util.xml.OjbcNamespaceContext.NS_PREFIX_JXDM_50;
import static org.ojbc.util.xml.OjbcNamespaceContext.NS_PREFIX_NC_30;
import static org.ojbc.util.xml.OjbcNamespaceContext.NS_PREFIX_ORGANIZATION_IDENTIFICATION_RESULTS_SEARCH_RESULTS;
import static org.ojbc.util.xml.OjbcNamespaceContext.NS_PREFIX_ORGANIZATION_IDENTIFICATION_RESULTS_SEARCH_RESULTS_EXT;
import static org.ojbc.util.xml.OjbcNamespaceContext.NS_PREFIX_SEARCH_REQUEST_ERROR_REPORTING;
import static org.ojbc.util.xml.OjbcNamespaceContext.NS_PREFIX_SEARCH_RESULTS_METADATA_EXT;
import static org.ojbc.util.xml.OjbcNamespaceContext.NS_PREFIX_STRUCTURES_30;
import static org.ojbc.util.xml.OjbcNamespaceContext.NS_PREFIX_WSN_BROKERED;
import static org.ojbc.util.xml.OjbcNamespaceContext.NS_SEARCH_REQUEST_ERROR_REPORTING;
import static org.ojbc.util.xml.OjbcNamespaceContext.NS_SEARCH_RESULTS_METADATA_EXT;
import static org.ojbc.util.xml.OjbcNamespaceContext.NS_STRUCTURES_30;
import static org.ojbc.util.xml.OjbcNamespaceContext.NS_WSN_BROKERED;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

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
import org.apache.wss4j.common.principal.SAMLTokenPrincipal;
import org.joda.time.DateTime;
import org.ojbc.adapters.rapbackdatastore.dao.RapbackDAO;
import org.ojbc.adapters.rapbackdatastore.dao.model.AgencyProfile;
import org.ojbc.adapters.rapbackdatastore.dao.model.IdentificationTransaction;
import org.ojbc.adapters.rapbackdatastore.dao.model.Subject;
import org.ojbc.intermediaries.sn.dao.Subscription;
import org.ojbc.util.camel.security.saml.SAMLTokenUtils;
import org.ojbc.util.model.rapback.IdentificationResultSearchRequest;
import org.ojbc.util.model.rapback.IdentificationTransactionState;
import org.ojbc.util.model.saml.SamlAttribute;
import org.ojbc.util.xml.XmlUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

@Service
public class RapbackSearchProcessor {

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
        SAMLTokenPrincipal token = (SAMLTokenPrincipal) cxfMessage
                .get("wss4j.principal.result");

        log.debug("Processing rapback search request for federationId:" + StringUtils.trimToEmpty(federationId));
        log.debug("Employer ORI : " + StringUtils.trimToEmpty(employerOri)); 
        
        if (StringUtils.isBlank(federationId) || StringUtils.isBlank(employerOri)) {
        	throw new IllegalArgumentException(
        			"Either request is missing SAML assertion or the federation "
        					+ "ID or Employer ORI is missing in the SAML assertion. ");
        } 
        
        Document rapbackSearchResponseDocument;
		try {
			rapbackSearchResponseDocument = buildRapbackSearchResponse(token, employerOri, report);
		} catch (Exception e) {
			log.error("Got exception building rapback search response", e);
			throw e;
		}

        return rapbackSearchResponseDocument;
    }
    
    private Document buildRapbackSearchResponse(SAMLTokenPrincipal token, String employerOri, Document report) throws Exception {
    	log.info("Get rapback search request, building the response.");

        Document document = documentBuilder.newDocument();
        Element rootElement = createRapbackSearchResponseRootElement(document);
        
        IdentificationResultSearchRequest searchRequest = getSearchRequestFromXml(report);
        
        List<IdentificationTransaction> identificationTransactions = null;
        if ("Civil".equals(searchRequest.getIdentificationResultCategory())){
        	identificationTransactions = rapbackDAO.getCivilIdentificationTransactions(token, searchRequest);
        	
        	Set<String> oris = getDistinctOris(identificationTransactions); 
        	List<AgencyProfile> agencyProfiles = rapbackDAO.getAgencyProfiles(oris);
        	
        	buildSearchResults(identificationTransactions, rootElement, agencyProfiles, true);
        	
        	appendOrganizationInfo(rootElement, agencyProfiles);
        }
        else if ("Criminal".equals(searchRequest.getIdentificationResultCategory())){
        	identificationTransactions = rapbackDAO.getCriminalIdentificationTransactions(token, searchRequest);
        	buildSearchResults(identificationTransactions, rootElement, null, false);
        }
        return document;
    }

    public IdentificationResultSearchRequest getSearchRequestFromXml(Document report) throws Exception{
        IdentificationResultSearchRequest searchRequest = new IdentificationResultSearchRequest();
        Node requestRoot = XmlUtils.xPathNodeSearch(report, "/oirs-req-doc:OrganizationIdentificationResultsSearchRequest");
        String resultCategoryCode = XmlUtils.xPathStringSearch(requestRoot, "oirs-req-ext:IdentificationResultsCategoryCode");
        searchRequest.setIdentificationResultCategory(StringUtils.trimToNull(resultCategoryCode));

        extractPersonCriteria(searchRequest, requestRoot);
        extractDateRangeCriteria(searchRequest, requestRoot);
        extractStatusCodeCriteria(searchRequest, requestRoot);
		
        extractCivilReasonCodes(searchRequest, requestRoot);
        extractCriminalReasonCodes(searchRequest, requestRoot);

        return searchRequest; 

    }

	private void extractPersonCriteria(
			IdentificationResultSearchRequest searchRequest, Node requestRoot) throws Exception {
		Node personNode = XmlUtils.xPathNodeSearch(requestRoot, "nc30:Person");
		if (personNode != null){
			String personGivenName = XmlUtils.xPathStringSearch(personNode, "nc30:PersonName/nc30:PersonGivenName"); 
			searchRequest.setFirstName(StringUtils.trimToNull(personGivenName));
			String personSurName = XmlUtils.xPathStringSearch(personNode, "nc30:PersonName/nc30:PersonSurName"); 
			searchRequest.setLastName(StringUtils.trimToNull(personSurName));
			
			String otn = XmlUtils.xPathStringSearch(personNode, 
					"oirs-req-ext:IdentifiedPersonTrackingIdentification/nc30:IdentificationID");
			searchRequest.setOtn(StringUtils.trimToNull(otn));
		}
		
	}

	private void extractCriminalReasonCodes(IdentificationResultSearchRequest searchRequest,
			Node requestRoot) throws Exception {
		NodeList reasonCodeNodes = XmlUtils.xPathNodeListSearch(requestRoot, "oirs-req-ext:CriminalIdentificationReasonCode");
        
		if (reasonCodeNodes != null && reasonCodeNodes.getLength() > 0){
			List<String> reasonCodes = new ArrayList<String>();
			for (int i = 0; i < reasonCodeNodes.getLength(); i++) {
	            if (reasonCodeNodes.item(i).getNodeType() == Node.ELEMENT_NODE) {
	                Element reasonCodeElement = (Element) reasonCodeNodes.item(i);
					if (StringUtils.isNotBlank(reasonCodeElement.getTextContent())){
						reasonCodes.add(reasonCodeElement.getTextContent());
					}
	            }
	        }
			
			if (reasonCodes.size() > 0){
				searchRequest.setCriminalIdentificationReasonCodes(reasonCodes);
			}
		}
	}

	private void extractCivilReasonCodes(IdentificationResultSearchRequest searchRequest,
			Node requestRoot) throws Exception {
		NodeList civilReasonCodeNodes = XmlUtils.xPathNodeListSearch(requestRoot, "oirs-req-ext:CivilIdentificationReasonCode");
		
		if (civilReasonCodeNodes != null && civilReasonCodeNodes.getLength() > 0){
			List<String> reasonCodes = new ArrayList<String>();
			for (int i = 0; i < civilReasonCodeNodes.getLength(); i++) {
				if (civilReasonCodeNodes.item(i).getNodeType() == Node.ELEMENT_NODE) {
					Element reasonCodeElement = (Element) civilReasonCodeNodes.item(i);
					if (StringUtils.isNotBlank(reasonCodeElement.getTextContent())){
						reasonCodes.add(reasonCodeElement.getTextContent());
					}
				}
			}
			
			if (reasonCodes.size() > 0){
				searchRequest.setCivilIdentificationReasonCodes(reasonCodes);
			}
		}
	}
	
	private void extractStatusCodeCriteria(IdentificationResultSearchRequest searchRequest,
			Node requestRoot) throws Exception {
		NodeList statusCodeNodes = XmlUtils.xPathNodeListSearch(requestRoot, "oirs-req-ext:IdentificationResultStatusCode");
        
		if (statusCodeNodes != null && statusCodeNodes.getLength() > 0){
			List<String> status = new ArrayList<String>();
			for (int i = 0; i < statusCodeNodes.getLength(); i++) {
	            if (statusCodeNodes.item(i).getNodeType() == Node.ELEMENT_NODE) {
	                Element statusElement = (Element) statusCodeNodes.item(i);
	                
	                if (StringUtils.isNotBlank(statusElement.getTextContent())){
	                	status.add(StringUtils.normalizeSpace(statusElement.getTextContent()));
	                }
	            }
	        }
			
			if (status.size() > 0 ){
				searchRequest.setIdentificationTransactionStatus(status);
			}
		}
	}

	private void extractDateRangeCriteria(IdentificationResultSearchRequest searchRequest,
			Node requestRoot) throws Exception {
		String startDateString = XmlUtils.xPathStringSearch(requestRoot, 
        		"oirs-req-ext:IdentificationReportedDateRange/nc30:StartDate/nc30:Date");
        if (StringUtils.isNotBlank(startDateString)){
        	searchRequest.setReportedDateStartLocalDate(LocalDate.parse(startDateString));
        }
        String endDateString = XmlUtils.xPathStringSearch(requestRoot, 
        		"oirs-req-ext:IdentificationReportedDateRange/nc30:EndDate/nc30:Date");
        if (StringUtils.isNotBlank(endDateString)){
        	searchRequest.setReportedDateEndLocalDate(LocalDate.parse(endDateString));
        }
	}
	private Set<String> getDistinctOris(
			List<IdentificationTransaction> identificationTransactions) {
		Set<String> distinctOris = new HashSet<String>(); 
		
		for (IdentificationTransaction identificationTransaction: identificationTransactions){
			distinctOris.add(identificationTransaction.getOwnerOri());
		}
		return distinctOris;
	}

	private void appendOrganizationInfo(Element rootElement,
			List<AgencyProfile> agencyProfiles) {
		
		for (int i = 0; i < agencyProfiles.size(); i++){
			
			AgencyProfile agencyProfile = agencyProfiles.get(i);
			
			Element entityOrganization = XmlUtils.appendElement(rootElement, NS_NC_30, "EntityOrganization");
			XmlUtils.addAttribute(entityOrganization, NS_STRUCTURES_30, "id", getOrgnizationId(i));
			
			Element organizationName = XmlUtils.appendElement(entityOrganization, NS_NC_30, "OrganizationName");
			organizationName.setTextContent(agencyProfile.getAgencyName());
			Element organizationAugmentation = XmlUtils.appendElement(entityOrganization, NS_JXDM_50, "OrganizationAugmentation");
			Element organizationORIIdentification = XmlUtils.appendElement(organizationAugmentation, NS_JXDM_50, "OrganizationORIIdentification");
			Element identificationID = XmlUtils.appendElement(organizationORIIdentification, NS_NC_30, "IdentificationID");
			identificationID.setTextContent(agencyProfile.getAgencyOri());
		}
		
		for (int i = 0; i < agencyProfiles.size(); i++){
			AgencyProfile agencyProfile = agencyProfiles.get(i);
			
			if (agencyProfile.getEmails().size()>0){
				Element contactInformationAssociation = XmlUtils.appendElement(rootElement, NS_NC_30, "ContactInformationAssociation");
				Element contactEntity = XmlUtils.appendElement(contactInformationAssociation, NS_NC_30, "ContactEntity");
				XmlUtils.addAttribute(contactEntity, NS_STRUCTURES_30, "ref", getOrgnizationId(i));
		
				Element contactInformation = XmlUtils.appendElement(contactInformationAssociation, NS_NC_30, "ContactInformation");
				
				for (String email : agencyProfile.getEmails()){
					Element contactEmailID = XmlUtils.appendElement(contactInformation, NS_NC_30, "ContactEmailID");
					contactEmailID.setTextContent(email);
				}
			}
		
		}
		
	}

	private String getOrgnizationId(int i) {
		String orgId = "ORG_" + StringUtils.leftPad(String.valueOf(i+1), 2, '0');
		return orgId;
	}

	private void buildSearchResults(
			List<IdentificationTransaction> identificationTransactions,
			Element rootElement, List<AgencyProfile> agencyProfiles, boolean isCivilResponse) {
		
		Map<String, String> oriOrganizationIdMap = new HashMap<String, String>();
		if (agencyProfiles != null){
			for (int i = 0; i < agencyProfiles.size(); i++){
				
				AgencyProfile agencyProfile = agencyProfiles.get(i);
				oriOrganizationIdMap.put(agencyProfile.getAgencyOri(), getOrgnizationId(i));
			}
		}
		
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
						identificationTransaction, oriOrganizationIdMap);
				
				appendReasonCodeElement(isCivilResponse, identificationTransaction,
						organizationIdentificationResultsSearchResultElement);
				
				appendDateElement(identificationTransaction.getTimestamp(), 
						organizationIdentificationResultsSearchResultElement, 
						"IdentificationReportedDate", NS_ORGANIZATION_IDENTIFICATION_RESULTS_SEARCH_RESULTS_EXT);
				appendSourceSystemNameTextElement(organizationIdentificationResultsSearchResultElement);
 
				Element systemIdentifierElement = XmlUtils.appendElement(
						organizationIdentificationResultsSearchResultElement, NS_INTEL_30, "SystemIdentification");
				Element identificationIdElement = XmlUtils.appendElement(systemIdentifierElement, NS_NC_30, "IdentificationID"); 
				identificationIdElement.setTextContent(identificationTransaction.getTransactionNumber());  
				Element systemNameElement = XmlUtils.appendElement(systemIdentifierElement, NS_NC_30, "SystemName");
				systemNameElement.setTextContent(SYSTEM_NAME);
			}
		}
	}

	private void appendReasonCodeElement(boolean isCivilResponse,
			IdentificationTransaction identificationTransaction,
			Element organizationIdentificationResultsSearchResultElement) {
		Element identificationReasonCode;
		if (isCivilResponse){
			identificationReasonCode = XmlUtils.appendElement(
					organizationIdentificationResultsSearchResultElement, 
					NS_ORGANIZATION_IDENTIFICATION_RESULTS_SEARCH_RESULTS_EXT, 
					"CivilIdentificationReasonCode");
		}
		else{
			identificationReasonCode = XmlUtils.appendElement(
					organizationIdentificationResultsSearchResultElement, 
					NS_ORGANIZATION_IDENTIFICATION_RESULTS_SEARCH_RESULTS_EXT, 
					"CriminalIdentificationReasonCode");
		}
		identificationReasonCode.setTextContent(identificationTransaction.getIdentificationCategory());
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
			IdentificationTransaction identificationTransaction, Map<String, String> oriOrgnizationIdMap) {
		Element identificationResultStatusCode = XmlUtils.appendElement(
				organizationIdentificationResultsSearchResultElement, 
				NS_ORGANIZATION_IDENTIFICATION_RESULTS_SEARCH_RESULTS_EXT, "IdentificationResultStatusCode");
		
		IdentificationTransactionState currentState = getCurrentState(identificationTransaction); 
		identificationResultStatusCode.setTextContent(currentState.toString());
		
		appendIdentificationRequestingOrganization(organizationIdentificationResultsSearchResultElement,
				identificationTransaction, oriOrgnizationIdMap);
		
		if (currentState == IdentificationTransactionState.Subscribed){
			appendSubscriptionElement(
					organizationIdentificationResultsSearchResultElement, identificationTransaction.getSubscription());
		}
		appendSubsequentResultsAvailableIndicator(
				organizationIdentificationResultsSearchResultElement,
				identificationTransaction.getHavingSubsequentResults());
	}

	private void appendIdentificationRequestingOrganization(
			Element organizationIdentificationResultsSearchResultElement,
			IdentificationTransaction identificationTransaction,
			Map<String, String> oriOrgnizationIdMap) {
		String orgnizationId = oriOrgnizationIdMap.get(identificationTransaction.getOwnerOri());
		if (StringUtils.isNotBlank(orgnizationId)){
			Element identificationRequestingOrganization = 
					XmlUtils.appendElement(organizationIdentificationResultsSearchResultElement,
							NS_ORGANIZATION_IDENTIFICATION_RESULTS_SEARCH_RESULTS_EXT, "IdentificationRequestingOrganization");
			XmlUtils.addAttribute(identificationRequestingOrganization, NS_STRUCTURES_30, "ref", orgnizationId);
		}
	}

	private void appendSubsequentResultsAvailableIndicator(
			Element organizationIdentificationResultsSearchResultElement,
			Boolean havingSubsequentResults) {
		
			Element subsequentResultsAvailableIndicator = 
					XmlUtils.appendElement(organizationIdentificationResultsSearchResultElement, 
							NS_ORGANIZATION_IDENTIFICATION_RESULTS_SEARCH_RESULTS_EXT, 
							"SubsequentResultsAvailableIndicator");
			subsequentResultsAvailableIndicator.setTextContent(BooleanUtils.toString(havingSubsequentResults, "true", "false", "false"));
	}

	private void appendSubscriptionElement(
			Element organizationIdentificationResultsSearchResultElement,
			Subscription subscription) {
		Element subscriptionElement = XmlUtils.appendElement(organizationIdentificationResultsSearchResultElement, 
				NS_ORGANIZATION_IDENTIFICATION_RESULTS_SEARCH_RESULTS_EXT, "Subscription");
		Element activityDateRange = 
				XmlUtils.appendElement(subscriptionElement, NS_NC_30, "ActivityDateRange"); 
		appendDateElement(subscription.getStartDate(), activityDateRange, "StartDate", NS_NC_30);
		appendDateElement(subscription.getEndDate(), activityDateRange, "EndDate", NS_NC_30);
		
		appendTopicElement(subscription.getTopic(), subscriptionElement);
		
		appendValidationDueDate(subscription.getValidationDueDate(), subscriptionElement);
		appendSubscriptionId(subscription.getId(), subscriptionElement);
		
	}

	private void appendSubscriptionId(long id, Element subscriptionElement) {
		Element subscriptionIdentification = 
				XmlUtils.appendElement(subscriptionElement, NS_ORGANIZATION_IDENTIFICATION_RESULTS_SEARCH_RESULTS_EXT, "SubscriptionIdentification");
		Element identificationId = XmlUtils.appendElement(subscriptionIdentification, NS_NC_30, "IdentificationID");
		identificationId.setTextContent(String.valueOf(id));
	}

	private void appendTopicElement(String topicValue, Element subscriptionElement) {
		Element topic = XmlUtils.appendElement(subscriptionElement, NS_WSN_BROKERED, "Topic");
		topic.setAttribute("Dialect", TOPIC_DIALECT);
		topic.setTextContent(topicValue);
	}

	private void appendValidationDueDate(DateTime validationDueDate,
			Element subscriptionElement) {
		Element subscriptionValidation = XmlUtils.appendElement(
				subscriptionElement, NS_ORGANIZATION_IDENTIFICATION_RESULTS_SEARCH_RESULTS_EXT, "SubscriptionValidation");
		appendDateElement(validationDueDate, subscriptionValidation, "SubscriptionValidationDueDate", NS_ORGANIZATION_IDENTIFICATION_RESULTS_SEARCH_RESULTS_EXT);
		
	}

	private void appendDateElement(DateTime dateObject, Element parentElement, 
			String elementName, String wrapperElementNS) {
		if (dateObject != null){
			Element dateWrapperElement = XmlUtils.appendElement(parentElement, wrapperElementNS, elementName);
			Element date = XmlUtils.appendElement(dateWrapperElement, NS_NC_30, "Date");
			date.setTextContent(dateObject.toString(YYYY_MM_DD));
		}
	}

	private IdentificationTransactionState getCurrentState(
			IdentificationTransaction identificationTransaction) {
		if (BooleanUtils.isTrue(identificationTransaction.getArchived())){
			return IdentificationTransactionState.Archived;
		}
		else {
			
			Subscription subscription = identificationTransaction.getSubscription(); 
			if (subscription != null && subscription.getActive() == Boolean.TRUE){
				return IdentificationTransactionState.Subscribed;
			}
			else{
				return IdentificationTransactionState.Available_for_Subscription;
			}
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
		Element personFirstNameElement = XmlUtils.appendElement(personNameElement, NS_NC_30, "PersonGivenName");
		personFirstNameElement.setTextContent(subject.getFirstName());
		Element personMiddleNameElement = XmlUtils.appendElement(personNameElement, NS_NC_30, "PersonMiddleName");
		personMiddleNameElement.setTextContent(subject.getMiddleInitial());
		Element personSurNameElement = XmlUtils.appendElement(personNameElement, NS_NC_30, "PersonSurName");
		personSurNameElement.setTextContent(subject.getLastName());
		Element personFullNameElement = XmlUtils.appendElement(personNameElement, NS_NC_30, "PersonFullName");
		personFullNameElement.setTextContent(subject.getFullName());
		
		appendPersonAugmentationElement(subject, identifiedPerson);
		
		Element identifiedPersonTrackingIdentification = XmlUtils.appendElement(identifiedPerson, 
				NS_ORGANIZATION_IDENTIFICATION_RESULTS_SEARCH_RESULTS_EXT, "IdentifiedPersonTrackingIdentification");
		Element identificationIdElement = XmlUtils.appendElement(
				identifiedPersonTrackingIdentification, NS_NC_30, "IdentificationID");
		identificationIdElement.setTextContent(identificationTransaction.getOtn());
	}

	private void appendPersonAugmentationElement(Subject subject, Element identifiedPerson) {
		log.info("subject: " + subject.toString());
		if (StringUtils.isNotBlank(subject.getUcn()) 
				|| StringUtils.isNotBlank(subject.getCivilSid()) 
				|| StringUtils.isNotBlank(subject.getCriminalSid())){
			Element personAugmentation = XmlUtils.appendElement(identifiedPerson, NS_JXDM_50, "PersonAugmentation");
			appendFbiIdElement(subject.getUcn(), personAugmentation);
			appendSidElement(subject.getCivilSid(), personAugmentation, true);
			appendSidElement(subject.getCriminalSid(), personAugmentation, false);
		}
	}

	private void appendFbiIdElement(String ucn, Element personAugmentation) {
		if (StringUtils.isNotBlank(ucn)){
			Element personFBIIdentification = XmlUtils.appendElement(personAugmentation, NS_JXDM_50, "PersonFBIIdentification");
			Element identificationID = 
					XmlUtils.appendElement(personFBIIdentification, NS_NC_30, "IdentificationID");
			identificationID.setTextContent(ucn);
		}
		
	}

	private void appendSidElement(String sid, Element personAugmentation, boolean isCivilSid) {
		if (StringUtils.isNotBlank(sid)){
			Element personStateFingerprintIdentification = 
					XmlUtils.appendElement(personAugmentation, NS_JXDM_50, "PersonStateFingerprintIdentification");
			Element identificationID = 
					XmlUtils.appendElement(personStateFingerprintIdentification, NS_NC_30, "IdentificationID");
			identificationID.setTextContent(sid);
			if (isCivilSid){
				Element fingerprintIdentificationIssuedForCivilPurposeIndicator =
						XmlUtils.appendElement(personStateFingerprintIdentification, NS_ORGANIZATION_IDENTIFICATION_RESULTS_SEARCH_RESULTS_EXT, "FingerprintIdentificationIssuedForCivilPurposeIndicator");
				fingerprintIdentificationIssuedForCivilPurposeIndicator.setTextContent("true");
			}
			else{
				Element fingerprintIdentificationIssuedForCriminalPurposeIndicator =
						XmlUtils.appendElement(personStateFingerprintIdentification, NS_ORGANIZATION_IDENTIFICATION_RESULTS_SEARCH_RESULTS_EXT, "FingerprintIdentificationIssuedForCriminalPurposeIndicator");
				fingerprintIdentificationIssuedForCriminalPurposeIndicator.setTextContent("true");
			}
				
		}
	}

    private Element createRapbackSearchResponseRootElement(Document document) {
        Element rootElement = document.createElementNS(
        		NS_ORGANIZATION_IDENTIFICATION_RESULTS_SEARCH_RESULTS,
        		NS_PREFIX_ORGANIZATION_IDENTIFICATION_RESULTS_SEARCH_RESULTS +":OrganizationIdentificationResultsSearchResults");
        rootElement.setAttribute("xmlns:"+NS_PREFIX_STRUCTURES_30, NS_STRUCTURES_30);
        rootElement.setAttribute("xmlns:"+NS_PREFIX_ORGANIZATION_IDENTIFICATION_RESULTS_SEARCH_RESULTS, 
        		NS_ORGANIZATION_IDENTIFICATION_RESULTS_SEARCH_RESULTS);
        rootElement.setAttribute("xmlns:"+NS_PREFIX_ORGANIZATION_IDENTIFICATION_RESULTS_SEARCH_RESULTS_EXT, 
        		NS_ORGANIZATION_IDENTIFICATION_RESULTS_SEARCH_RESULTS_EXT);
        rootElement.setAttribute("xmlns:"+NS_PREFIX_INTEL_30, NS_INTEL_30);
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

        Element systemNameNode = XmlUtils.appendElement(searchRequestErrorNode, NS_NC_30, "SystemName");
        systemNameNode.setTextContent(systemName);
        return document;
    }

    private Element createErrorResponseRootElement(Document document) {
        Element rootElement = document.createElementNS(
        		NS_ORGANIZATION_IDENTIFICATION_RESULTS_SEARCH_RESULTS,
        		NS_PREFIX_ORGANIZATION_IDENTIFICATION_RESULTS_SEARCH_RESULTS +":OrganizationIdentificationResultsSearchResults");
        rootElement.setAttribute("xmlns:"+NS_PREFIX_STRUCTURES_30, NS_STRUCTURES_30);
        rootElement.setAttribute("xmlns:"+NS_PREFIX_ORGANIZATION_IDENTIFICATION_RESULTS_SEARCH_RESULTS, 
        		NS_ORGANIZATION_IDENTIFICATION_RESULTS_SEARCH_RESULTS);
        rootElement.setAttribute("xmlns:"+NS_PREFIX_INTEL_30, NS_INTEL_30);
        rootElement.setAttribute("xmlns:"+NS_PREFIX_SEARCH_RESULTS_METADATA_EXT, NS_SEARCH_RESULTS_METADATA_EXT);
        rootElement.setAttribute("xmlns:"+NS_PREFIX_SEARCH_REQUEST_ERROR_REPORTING, NS_SEARCH_REQUEST_ERROR_REPORTING);

        document.appendChild(rootElement);
        return rootElement;
    }


}
