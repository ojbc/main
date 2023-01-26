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
package org.ojbc.intermediaries.sn.topic.warrantfile;

import java.util.HashMap;
import java.util.Map;

import org.apache.camel.Message;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.joda.time.DateTime;
import org.ojbc.intermediaries.sn.SubscriptionNotificationConstants;
import org.ojbc.intermediaries.sn.notification.NotificationRequest;
import org.ojbc.intermediaries.sn.notification.NotificationRequest.Alias;
import org.ojbc.intermediaries.sn.util.NotificationBrokerUtils;
import org.ojbc.util.xml.XmlUtils;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class WarrantFileNotificationRequest extends NotificationRequest{
	
	private static final Log logger = LogFactory.getLog(WarrantFileNotificationRequest.class);
	
    public WarrantFileNotificationRequest(Message message) throws Exception{
        this(message.getBody(Document.class));
    }

    public WarrantFileNotificationRequest(Document document) throws Exception {
        super(document);
        setUpRequest(document);
        buildSubjectIdMap(); 
    }
    
    public void setUpRequest(Document document) throws Exception{
    	this.requestDocument = document;

        String notificationEventDateTimeString = "Test";
        String notificationEventDateOnlyString = XmlUtils.xPathStringSearch(document, getNotificationEventDateRootXpath() + "/nc:Date");

        if (StringUtils.isNotEmpty(notificationEventDateTimeString)) {
            notificationEventDate = DateTime.now();
            isNotificationEventDateInclusiveOfTime = true;
        } else if (StringUtils.isNotEmpty(notificationEventDateOnlyString)) {
            notificationEventDate = XmlUtils.parseXmlDate(notificationEventDateOnlyString);
            isNotificationEventDateInclusiveOfTime = false;
        } else {
            notificationEventDate = null;
        }

        personActivityInvolvementText = XmlUtils.xPathStringSearch(document,
                "/b-2:Notify/b-2:NotificationMessage/b-2:Message/notfm-exch:NotificationMessage/nc:ActivityInvolvedPersonAssociation/nc:PersonActivityInvolvementText");

        String personReference = XmlUtils.xPathStringSearch(document,
                "/b-2:Notify/b-2:NotificationMessage/b-2:Message/notfm-exch:NotificationMessage/nc:ActivityInvolvedPersonAssociation/nc:PersonReference/@s:ref");

        if (StringUtils.isNotBlank(personReference)) {
            personFirstName = StringUtils.strip(XmlUtils.xPathStringSearch(document, "/b-2:Notify/b-2:NotificationMessage/b-2:Message/notfm-exch:NotificationMessage/notfm-exch:Person[@s:id=/b-2:Notify/b-2:NotificationMessage/b-2:Message/notfm-exch:NotificationMessage/notfm-ext:NotifyingWarrant/jxdm41:Warrant/jxdm41:CourtOrderDesignatedSubject/nc:RoleOfPersonReference/@s:ref]/nc:PersonName/nc:PersonGivenName"));
            personMiddleName = StringUtils.strip(XmlUtils.xPathStringSearch(document, "/b-2:Notify/b-2:NotificationMessage/b-2:Message/notfm-exch:NotificationMessage/notfm-exch:Person[@s:id=/b-2:Notify/b-2:NotificationMessage/b-2:Message/notfm-exch:NotificationMessage/notfm-ext:NotifyingWarrant/jxdm41:Warrant/jxdm41:CourtOrderDesignatedSubject/nc:RoleOfPersonReference/@s:ref]/nc:PersonName/nc:PersonMiddleName"));
            personLastName = StringUtils.strip(XmlUtils.xPathStringSearch(document, "/b-2:Notify/b-2:NotificationMessage/b-2:Message/notfm-exch:NotificationMessage/notfm-exch:Person[@s:id=/b-2:Notify/b-2:NotificationMessage/b-2:Message/notfm-exch:NotificationMessage/notfm-ext:NotifyingWarrant/jxdm41:Warrant/jxdm41:CourtOrderDesignatedSubject/nc:RoleOfPersonReference/@s:ref]/nc:PersonName/nc:PersonSurName"));
            personNameSuffix = StringUtils.strip(XmlUtils.xPathStringSearch(document, "/b-2:Notify/b-2:NotificationMessage/b-2:Message/notfm-exch:NotificationMessage/notfm-exch:Person[@s:id=/b-2:Notify/b-2:NotificationMessage/b-2:Message/notfm-exch:NotificationMessage/notfm-ext:NotifyingWarrant/jxdm41:Warrant/jxdm41:CourtOrderDesignatedSubject/nc:RoleOfPersonReference/@s:ref]/nc:PersonName/nc:PersonNameSuffixText"));
            personBirthDate = StringUtils.strip(XmlUtils.xPathStringSearch(document, "/b-2:Notify/b-2:NotificationMessage/b-2:Message/notfm-exch:NotificationMessage/notfm-exch:Person[@s:id=/b-2:Notify/b-2:NotificationMessage/b-2:Message/notfm-exch:NotificationMessage/notfm-ext:NotifyingWarrant/jxdm41:Warrant/jxdm41:CourtOrderDesignatedSubject/nc:RoleOfPersonReference/@s:ref]/nc:PersonBirthDate/nc:Date"));
            
            try
            {
            	personAge = NotificationBrokerUtils.calculatePersonAgeFromDate(personBirthDate);	
            }
            catch (Exception ex)
            {
            	logger.error("Unable to calculate person age.");
            }
            	
            NodeList aliasNodes = XmlUtils.xPathNodeListSearch(document,
            		"/b-2:Notify/b-2:NotificationMessage/b-2:Message/notfm-exch:NotificationMessage/notfm-exch:Person[@s:id='" + personReference + "']/nc:PersonAlternateName");

		    if (aliasNodes != null && aliasNodes.getLength() > 0) {
		        for (int i = 0; i < aliasNodes.getLength(); i++) {
		            if (aliasNodes.item(i).getNodeType() == Node.ELEMENT_NODE) {
		
		                Element aliasElement = (Element) aliasNodes.item(i);
		
		                Alias alias = new Alias();
		
		                alias.setPersonFirstName(StringUtils.strip(XmlUtils.xPathStringSearch(aliasElement, "nc:PersonGivenName")));
		                alias.setPersonLastName(StringUtils.strip(XmlUtils.xPathStringSearch(aliasElement, "nc:PersonSurName")));
		                
		                aliases.add(alias);
		
		            }
		        }
		    }

            String personContactInfoReference = XmlUtils.xPathStringSearch(document, "/b-2:Notify/b-2:NotificationMessage/b-2:Message/notfm-exch:NotificationMessage/nc:PersonContactInformationAssociation/nc:ContactInformationReference/@s:ref");

            NodeList telephoneNumberNodes = XmlUtils.xPathNodeListSearch(document,
            		"/b-2:Notify/b-2:NotificationMessage/b-2:Message/notfm-exch:NotificationMessage//nc:ContactInformation[@s:id='" + personContactInfoReference + "']/nc:ContactTelephoneNumber/nc:FullTelephoneNumber/nc:TelephoneNumberFullID");
                                                                                                                      
		    if (telephoneNumberNodes != null && telephoneNumberNodes.getLength() > 0) {
		        for (int i = 0; i < telephoneNumberNodes.getLength(); i++) {
		            if (telephoneNumberNodes.item(i).getNodeType() == Node.ELEMENT_NODE) {
		            	
		            	if (StringUtils.isNotBlank(telephoneNumberNodes.item(i).getTextContent()))
		            	{	
		            		personTelephoneNumbers.add(StringUtils.strip(telephoneNumberNodes.item(i).getTextContent()));
		            	}	
		            }
		        }
		    }
            
            
        } else {
            logger.error("Unable to find person reference. Unable to XQuery for person name.");
        }

        if (StringUtils.isNotEmpty(getOfficerNameReferenceXPath()))
        {	
	        NodeList officerReferences = XmlUtils.xPathNodeListSearch(document, getOfficerNameReferenceXPath());
	
			if (officerReferences != null && officerReferences.getLength() > 0) {
	            for (int i = 0; i < officerReferences.getLength(); i++) {
	                if (officerReferences.item(i).getNodeType() == Node.ATTRIBUTE_NODE) {
	
	                    String officerReference = officerReferences.item(i).getTextContent();
	                    String officerName = XmlUtils.xPathStringSearch(document, "/b-2:Notify/b-2:NotificationMessage/b-2:Message/notfm-exch:NotificationMessage/notfm-exch:Person[@s:id='" + officerReference + "']/nc:PersonName/nc:PersonFullName");
	                    
	                    if (StringUtils.isNotEmpty(officerName))
	                    {	
	                    	officerNames.add(StringUtils.strip(officerName));
	                    }	
	                }
	            }
			}    
        }	
			
        notificationEventIdentifier = XmlUtils.xPathStringSearch(document, getNotificationEventIdentifierXpath());
        notificationEventIdentifier = StringUtils.strip(notificationEventIdentifier);

        if (StringUtils.isNotBlank(getNotifyingAgencyXpath()))
        {	
	        notifyingAgencyName = XmlUtils.xPathStringSearch(document, getNotifyingAgencyXpath());
	        notifyingAgencyName = StringUtils.strip(notifyingAgencyName);
        }    
	        
        notifyingAgencyOri = StringUtils.trimToNull(XmlUtils.xPathStringSearch(document, getNotifyingAgencyOriXpath()));
        
        if (StringUtils.isNotBlank(getNotificationAgencyPhoneNumberXpath()))
        {	
	        notifyingAgencyPhoneNumber = XmlUtils.xPathStringSearch(document, getNotificationAgencyPhoneNumberXpath());
	        notifyingAgencyPhoneNumber = StringUtils.strip(notifyingAgencyPhoneNumber);
        }    
	        
        notifyingSystemName = XmlUtils.xPathStringSearch(document, getNotifyingSystemNameXPath());
        notifyingSystemName = StringUtils.strip(notifyingSystemName);

        // subjectIdentification intentionally omitted - should be populated in subclass

        Node topicNode = XmlUtils.xPathNodeSearch(document, "/b-2:Notify/b-2:NotificationMessage/b-2:Topic");
        String unqualifiedTopic = topicNode.getTextContent();
        topic = NotificationBrokerUtils.getFullyQualifiedTopic(unqualifiedTopic);
    }
    
    public String getPersonFullName() {
        String personFirstName = StringUtils.strip(getPersonFirstName());
        String personLastName = StringUtils.strip(getPersonLastName());
        return personFirstName + " " + personLastName;
    }
    
    public String getEventDateTimeDisplay() {
        boolean isNotificationEventDateInclusiveOfTime = isNotificationEventDateInclusiveOfTime();
        DateTime eventDateTime = getNotificationEventDate();
        String eventDate = NotificationBrokerUtils.returnFormattedNotificationEventDate(eventDateTime, isNotificationEventDateInclusiveOfTime);
        return StringUtils.strip(eventDate);
    }
    
	@Override
	protected String getNotificationEventDateRootXpath() {
		
		// note doesn't return nc:Date or nc:DateTime but rather returns their parent node,
		// because inherited logic conditionally uses one the the two child values
		return "/b-2:Notify/b-2:NotificationMessage/b-2:Message/notfm-exch:NotificationMessage/notfm-ext:NotifyingCriminalHistoryUpdate/chu:CycleTrackingIdentifierAssignment/nc:ActivityDate";
	}

	@Override
	protected String getNotifyingAgencyXpath() {
		
		String notifyingAgencyXpath = null;		
		
		try {
			String origOrganizerRef = getOrigOrganizationRef();
			
			notifyingAgencyXpath = "/b-2:Notify/b-2:NotificationMessage/b-2:Message/notfm-exch:NotificationMessage/jxdm41:Organization[@s:id='"+origOrganizerRef+"']/nc:OrganizationName";	
		} catch (Exception e) {			
			e.printStackTrace();
		}

		return notifyingAgencyXpath;
	}

	private String getOrigOrganizationRef() throws Exception {
		String origOrganizerRef = XmlUtils.xPathStringSearch(requestDocument, 
				"/b-2:Notify/b-2:NotificationMessage/b-2:Message/notfm-exch:NotificationMessage/notfm-ext:NotifyingWarrant/notfm-ext:NotifyingOrganizationReference/@s:ref");
		
		logger.info("Notifying Agency ref: " + origOrganizerRef);
		return origOrganizerRef;
	}

	@Override
	protected String getNotificationEventIdentifierXpath() {

		//FIX THIS
		return "/b-2:Notify/b-2:NotificationMessage/b-2:Message/notfm-exch:NotificationMessage/notfm-ext:NotifyingCriminalHistoryUpdate/chu:CycleTrackingIdentifierAssignment/nc:Case/nc:ActivityIdentification/nc:IdentificationID";
	}
		
	
	@Override
	protected String getNotifyingSystemNameXPath(){
		return "/b-2:Notify/b-2:NotificationMessage/b-2:Message/notfm-exch:NotificationMessage/notfm-ext:NotifyingActivityReportingSystemNameText";
	}
	
	@Override
	public String getDescriptiveSubjectIdentifier() {
		
		Map<String, String> sidMap = getSubjectIdentifiers();
		
		String lastName = sidMap.get(SubscriptionNotificationConstants.LAST_NAME);
		
		String firstName = sidMap.get(SubscriptionNotificationConstants.FIRST_NAME);
		
		String dateOfBirth = sidMap.get(SubscriptionNotificationConstants.DATE_OF_BIRTH);
				
		return lastName + "_" + firstName + "_" + dateOfBirth;
	}
	
	@Override
	protected String getNotificationAgencyPhoneNumberXpath() {
		return "''";	
	}

	@Override
	protected String getOfficerNameReferenceXPath() {		
		return null;
	}

	private void buildSubjectIdMap() throws Exception {
		
		subjectIdentifiers = new HashMap<String, String>();
		
		String agencyCaseNumber = XmlUtils.xPathStringSearch(requestDocument, "/b-2:Notify/b-2:NotificationMessage/b-2:Message/notfm-exch:NotificationMessage/notfm-ext:NotifyingWarrant/jxdm41:Warrant/nc:ActivityIdentification/nc:IdentificationID");
		
		subjectIdentifiers.put(SubscriptionNotificationConstants.SUBSCRIPTION_QUALIFIER, agencyCaseNumber);
	}

	@Override
	protected String getNotifyingAgencyOriXpath() {
		try {
			String origOrganizerRef = getOrigOrganizationRef();
			return "/b-2:Notify/b-2:NotificationMessage/b-2:Message/notfm-exch:NotificationMessage/jxdm41:Organization[@s:id='"
					+origOrganizerRef+"']/jxdm41:OrganizationAugmentation/jxdm41:OrganizationORIIdentification/nc:IdentificationID";			
		} catch (Exception e) {			
			e.printStackTrace();
			return null;
		}

	}	
}
