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
package org.ojbc.bundles.adapters.consentmanagement.processor;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.joda.time.LocalDate;
import org.ojbc.bundles.adapters.consentmanagement.dao.ConsentManagementDAOImpl;
import org.ojbc.bundles.adapters.consentmanagement.model.Consent;
import org.ojbc.util.xml.OjbcNamespaceContext;
import org.ojbc.util.xml.XmlUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

@Component
public class ConsentXMLProcessor {
	
	private static final Log log = LogFactory.getLog(ConsentXMLProcessor.class);

	@Autowired
	private ConsentManagementDAOImpl consentManagementDAOImpl;

	public Document createConsentReport(Consent consent) throws Exception
	{
		Document doc = null;
		
        DocumentBuilderFactory docBuilderFact = DocumentBuilderFactory.newInstance();
        docBuilderFact.setNamespaceAware(true);
        DocumentBuilder docBuilder = docBuilderFact.newDocumentBuilder();

        doc = docBuilder.newDocument();

        Element root = doc.createElementNS(OjbcNamespaceContext.NS_CONSENT_DECISION_REPORTING_DOC, "ConsentDecisionReport");
        doc.appendChild(root);
        root.setPrefix(OjbcNamespaceContext.NS_PREFIX_CONSENT_DECISION_REPORTING_DOC);
        
        Element documentCreationDate = XmlUtils.appendElement(root, OjbcNamespaceContext.NS_NC_30, "nc:DocumentCreationDate");
        Element date = XmlUtils.appendElement(documentCreationDate, OjbcNamespaceContext.NS_NC_30, "nc:Date");
        
        LocalDate localDate = LocalDate.now();
        date.setTextContent(localDate.toString());
        
//        	<j:Booking>
//	    		<j:BookingAgencyRecordIdentification>
//	    			<nc:IdentificationID>234567890</nc:IdentificationID>
//	    		</j:BookingAgencyRecordIdentification>		
//	    		<j:BookingSubject>
//	    			<nc:RoleOfPerson structures:ref="person01" />
//	    			<j:SubjectIdentification>
//	    				<nc:IdentificationID>S29858</nc:IdentificationID>
//	    			</j:SubjectIdentification>
//	    		</j:BookingSubject>
//	    	</j:Booking>            
        
        Element booking = XmlUtils.appendElement(root, OjbcNamespaceContext.NS_JXDM_51, "j:Booking");
        
        Element bookingAgencyRecordIdentification = XmlUtils.appendElement(booking, OjbcNamespaceContext.NS_JXDM_51, "j:BookingAgencyRecordIdentification");
        
        Element identificationId = XmlUtils.appendElement(bookingAgencyRecordIdentification, OjbcNamespaceContext.NS_NC_30, "nc:IdentificationID");
        identificationId.setTextContent(consent.getBookingNumber());
        
        Element bookingSubject = XmlUtils.appendElement(booking, OjbcNamespaceContext.NS_JXDM_51, "j:BookingSubject");
        
        Element roleOfPerson = XmlUtils.appendElement(bookingSubject, OjbcNamespaceContext.NS_NC_30, "nc:RoleOfPerson");
        XmlUtils.addAttribute(roleOfPerson, OjbcNamespaceContext.NS_STRUCTURES_30, "ref", "person01");
        
        Element subjectIdentification = XmlUtils.appendElement(bookingSubject, OjbcNamespaceContext.NS_JXDM_51, "j:SubjectIdentification");
        Element identificationIdSubject = XmlUtils.appendElement(subjectIdentification, OjbcNamespaceContext.NS_NC_30, "nc:IdentificationID");
        identificationIdSubject.setTextContent(consent.getNameNumber());
        
//	        <nc:Person structures:id="person01">
//	    		<nc:PersonBirthDate>
//	    			<nc:Date>1980-06-01</nc:Date>
//	    		</nc:PersonBirthDate>
//	    		<nc:PersonName>
//	    			<nc:PersonGivenName>John</nc:PersonGivenName>
//	    			<nc:PersonMiddleName>K</nc:PersonMiddleName>
//	    			<nc:PersonSurName>Jones</nc:PersonSurName>
//	    		</nc:PersonName>
//	    		<j:PersonSexCode>M</j:PersonSexCode>
//	    	</nc:Person>            
        
        Element person = XmlUtils.appendElement(root, OjbcNamespaceContext.NS_NC_30, "nc:Person");
        XmlUtils.addAttribute(roleOfPerson, OjbcNamespaceContext.NS_STRUCTURES_30, "id", "person01");
        
        Element personDob = XmlUtils.appendElement(person, OjbcNamespaceContext.NS_NC_30, "nc:PersonBirthDate");
        
        Element personDate = XmlUtils.appendElement(personDob, OjbcNamespaceContext.NS_NC_30, "nc:Date");
        personDate.setTextContent(consent.getPersonDOBString());
        
        Element personName = XmlUtils.appendElement(person, OjbcNamespaceContext.NS_NC_30, "nc:PersonName");
        
        Element personGivenName = XmlUtils.appendElement(personName, OjbcNamespaceContext.NS_NC_30, "nc:PersonGivenName");
        personGivenName.setTextContent(consent.getPersonFirstName());
        
        Element personMiddleName = XmlUtils.appendElement(personName, OjbcNamespaceContext.NS_NC_30, "nc:PersonMiddleName");
        personMiddleName.setTextContent(consent.getPersonMiddleName());
        
        Element personSurName = XmlUtils.appendElement(personName, OjbcNamespaceContext.NS_NC_30, "nc:PersonSurName");
        personSurName.setTextContent(consent.getPersonLastName());
        
        Element personSexCode = XmlUtils.appendElement(person, OjbcNamespaceContext.NS_JXDM_51, "j:PersonSexCode");
        personSexCode.setTextContent(consent.getPersonGender());
        
		//<cdr-ext:ConsentDecision>
		//	<nc:ActivityIdentification>
		//		<nc:IdentificationID>CD858</nc:IdentificationID>
		//	</nc:ActivityIdentification>
		//	<nc:ActivityDate>
		//		<nc:Date>2017-03-26</nc:Date>
		//	</nc:ActivityDate>
		//	<cdr-ext:ConsentDecisionCode>Consent Granted</cdr-ext:ConsentDecisionCode>
		//	<cdr-ext:ConsentDecisionRecordingEntity>
		//		<nc:EntityPerson>
		//			<nc:PersonName>
		//				<nc:PersonGivenName>Walter</nc:PersonGivenName>
		//				<nc:PersonSurName>White</nc:PersonSurName>
		//			</nc:PersonName>
		//		</nc:EntityPerson>
		//		<cdr-ext:RecordingEntityUsernameText>wwhite</cdr-ext:RecordingEntityUsernameText>
		//	</cdr-ext:ConsentDecisionRecordingEntity>
		//</cdr-ext:ConsentDecision>            
        
        Element consentDecision = XmlUtils.appendElement(root, OjbcNamespaceContext.NS_CONSENT_DECISION_REPORTING_EXT, "cdr-ext:ConsentDecision");

        Element consentActivityIdentification = XmlUtils.appendElement(consentDecision, OjbcNamespaceContext.NS_NC_30, "nc:ActivityIdentification");
        
        Element consentIdentificationId = XmlUtils.appendElement(consentActivityIdentification, OjbcNamespaceContext.NS_NC_30, "nc:IdentificationID");
        consentIdentificationId.setTextContent(consent.getConsentId().toString());
        
        Element activityDate = XmlUtils.appendElement(consentDecision, OjbcNamespaceContext.NS_NC_30, "nc:ActivityDate");
        
        if (consent.getConsentDecisionTimestamp() != null)
        {	
        	Element consentDate = XmlUtils.appendElement(activityDate, OjbcNamespaceContext.NS_NC_30, "nc:DateTime");
        	consentDate.setTextContent(consent.getConsentDecisionTimestamp().toString());
        }	
        
        String consentDescription = consentManagementDAOImpl.retrieveConsentDecisionText(consent.getConsentDecisionTypeID());

        Element consentDecisionCode = XmlUtils.appendElement(consentDecision, OjbcNamespaceContext.NS_CONSENT_DECISION_REPORTING_EXT, "cdr-ext:ConsentDecisionCode");
        consentDecisionCode.setTextContent(consentDescription);
        
        Element consentDecisionRecordingEntity = XmlUtils.appendElement(consentDecision, OjbcNamespaceContext.NS_CONSENT_DECISION_REPORTING_EXT, "cdr-ext:ConsentDecisionRecordingEntity");
        
        Element entityPerson = XmlUtils.appendElement(consentDecisionRecordingEntity, OjbcNamespaceContext.NS_NC_30, "nc:EntityPerson");
        
        Element entityPersonName = XmlUtils.appendElement(entityPerson, OjbcNamespaceContext.NS_NC_30, "nc:PersonName");
        
        Element entityPersonGivenName = XmlUtils.appendElement(entityPersonName, OjbcNamespaceContext.NS_NC_30, "nc:PersonGivenName");
        entityPersonGivenName.setTextContent(consent.getConsentUserFirstName());
        
        Element entityPersonSurName = XmlUtils.appendElement(entityPersonName, OjbcNamespaceContext.NS_NC_30, "nc:PersonSurName");
        entityPersonSurName.setTextContent(consent.getConsentUserLastName());
        
        Element recordingEntityUsernameText = XmlUtils.appendElement(consentDecisionRecordingEntity, OjbcNamespaceContext.NS_CONSENT_DECISION_REPORTING_EXT, "cdr-ext:RecordingEntityUsernameText");
        recordingEntityUsernameText.setTextContent(consent.getConsenterUserID());


		return doc;
	}
	
}
