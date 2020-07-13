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

import static org.ojbc.adapters.rapbackdatastore.RapbackDataStoreAdapterConstants.YYYY_MM_DD;
import static org.ojbc.util.xml.OjbcNamespaceContext.NS_JXDM_50;
import static org.ojbc.util.xml.OjbcNamespaceContext.NS_NC_30;

import javax.annotation.Resource;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.apache.commons.lang.BooleanUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.joda.time.DateTime;
import org.ojbc.adapters.rapbackdatastore.dao.RapbackDAO;
import org.ojbc.adapters.rapbackdatastore.dao.model.IdentificationTransaction;
import org.ojbc.adapters.rapbackdatastore.dao.model.Subject;
import org.ojbc.util.model.rapback.IdentificationTransactionState;
import org.ojbc.util.model.rapback.Subscription;
import org.ojbc.util.xml.XmlUtils;
import org.w3c.dom.Element;

public class AbstractSearchQueryProcessor {
	
	protected final Log log = LogFactory.getLog(this.getClass());

    @Resource
    protected RapbackDAO rapbackDAO;

    protected DocumentBuilder documentBuilder;

    public AbstractSearchQueryProcessor() throws ParserConfigurationException {
        DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
        documentBuilderFactory.setNamespaceAware(true);
        documentBuilder = documentBuilderFactory.newDocumentBuilder();
    }
    
	void appendIdentifiedPersonElement(Element organizationIdentificationResultsSearchResultElement,
			IdentificationTransaction identificationTransaction, String extNamespace) {
		
		Subject subject = identificationTransaction.getSubject();
		Element identifiedPerson = XmlUtils.appendElement(organizationIdentificationResultsSearchResultElement, 
				extNamespace, "IdentifiedPerson");
		
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
		
		appendPersonAugmentationElement(subject, identifiedPerson, extNamespace);
		
		Element identifiedPersonTrackingIdentification = XmlUtils.appendElement(identifiedPerson, 
				extNamespace, "IdentifiedPersonTrackingIdentification");
		Element identificationIdElement = XmlUtils.appendElement(
				identifiedPersonTrackingIdentification, NS_NC_30, "IdentificationID");
		identificationIdElement.setTextContent(identificationTransaction.getOtn());
	}

	void appendPersonAugmentationElement(Subject subject, Element identifiedPerson, String extNamespace) {
		if (StringUtils.isNotBlank(subject.getUcn()) 
				|| StringUtils.isNotBlank(subject.getCivilSid()) 
				|| StringUtils.isNotBlank(subject.getCriminalSid())){
			Element personAugmentation = XmlUtils.appendElement(identifiedPerson, NS_JXDM_50, "PersonAugmentation");
			appendFbiIdElement(subject.getUcn(), personAugmentation);
			appendSidElement(subject.getCivilSid(), personAugmentation, true, extNamespace);
			appendSidElement(subject.getCriminalSid(), personAugmentation, false, extNamespace);
		}
	}
	
	void appendFbiIdElement(String ucn, Element personAugmentation) {
		if (StringUtils.isNotBlank(ucn)){
			Element personFBIIdentification = XmlUtils.appendElement(personAugmentation, NS_JXDM_50, "PersonFBIIdentification");
			Element identificationID = 
					XmlUtils.appendElement(personFBIIdentification, NS_NC_30, "IdentificationID");
			identificationID.setTextContent(ucn);
		}
		
	}

	void appendSidElement(String sid, Element personAugmentation, boolean isCivilSid, String extNamespace) {
		if (StringUtils.isNotBlank(sid)){
			Element personStateFingerprintIdentification = 
					XmlUtils.appendElement(personAugmentation, NS_JXDM_50, "PersonStateFingerprintIdentification");
			Element identificationID = 
					XmlUtils.appendElement(personStateFingerprintIdentification, NS_NC_30, "IdentificationID");
			identificationID.setTextContent(sid);
			if (isCivilSid){
				Element fingerprintIdentificationIssuedForCivilPurposeIndicator =
						XmlUtils.appendElement(personStateFingerprintIdentification, extNamespace, "FingerprintIdentificationIssuedForCivilPurposeIndicator");
				fingerprintIdentificationIssuedForCivilPurposeIndicator.setTextContent("true");
			}
			else{
				Element fingerprintIdentificationIssuedForCriminalPurposeIndicator =
						XmlUtils.appendElement(personStateFingerprintIdentification, extNamespace, "FingerprintIdentificationIssuedForCriminalPurposeIndicator");
				fingerprintIdentificationIssuedForCriminalPurposeIndicator.setTextContent("true");
			}
				
		}
	}

	IdentificationTransactionState getCurrentState(
			IdentificationTransaction identificationTransaction) {
		if (BooleanUtils.isTrue(identificationTransaction.getArchived())){
			return IdentificationTransactionState.Archived;
		}
		else {
			
			Subscription subscription = identificationTransaction.getSubscription(); 
			if (subscription != null && subscription.getActive() == Boolean.TRUE 
					&& (subscription.getEndDate() == null || subscription.getEndDate().plusDays(1).isAfterNow())){
				if (StringUtils.isNotBlank(identificationTransaction.getFbiSubscriptionId()) 
						&& "SUBSCRIBED".equals(identificationTransaction.getFbiSubscriptionStatus())){
					return IdentificationTransactionState.Subscribed_State_FBI;
				}
				else {
					switch(StringUtils.trimToEmpty(identificationTransaction.getFbiSubscriptionStatus())) {
					case "PENDING": 
						return IdentificationTransactionState.Subscribed_State_FBI_Pending;
					case "ERROR": 
						return IdentificationTransactionState.Subscribed_State_FBI_Error;
					default:
						return IdentificationTransactionState.Subscribed_State;
					}
				}
			}
			else{
				return IdentificationTransactionState.Available_for_Subscription;
			}
		}
	}

	void appendSubsequentResultsAvailableIndicator(
			Element parentElement,
			Boolean havingSubsequentResults, 
			String extNamespace) {
		
			Element subsequentResultsAvailableIndicator = 
					XmlUtils.appendElement(parentElement, extNamespace, "SubsequentResultsAvailableIndicator");
			subsequentResultsAvailableIndicator.setTextContent(BooleanUtils.toString(havingSubsequentResults, "true", "false", "false"));
	}

	void appendDateElement(DateTime dateObject, Element parentElement, 
			String elementName, String wrapperElementNS) {
		if (dateObject != null){
			Element dateWrapperElement = XmlUtils.appendElement(parentElement, wrapperElementNS, elementName);
			Element date = XmlUtils.appendElement(dateWrapperElement, NS_NC_30, "Date");
			date.setTextContent(dateObject.toString(YYYY_MM_DD));
		}
	}

	void appendReasonCodeElement(boolean isCivilResponse,
			IdentificationTransaction identificationTransaction,
			Element parentElement, String extNamespace) {
		Element identificationReasonCode;
		if (isCivilResponse){
			identificationReasonCode = XmlUtils.appendElement(parentElement, extNamespace, "CivilIdentificationReasonCode");
		}
		else{
			identificationReasonCode = XmlUtils.appendElement(parentElement, extNamespace, "CriminalIdentificationReasonCode");
		}
		identificationReasonCode.setTextContent(identificationTransaction.getIdentificationCategory());
	}



}
