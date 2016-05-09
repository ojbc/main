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
package org.ojbc.intermediaries.sn.notification;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.camel.Message;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.joda.time.DateTime;
import org.ojbc.intermediaries.sn.util.NotificationBrokerUtils;
import org.ojbc.util.xml.XmlUtils;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public abstract class NotificationRequest {

    private static final Log log = LogFactory.getLog(NotificationRequest.class);

    protected Document requestDocument;

    protected DateTime notificationEventDate;
    protected boolean isNotificationEventDateInclusiveOfTime;

    protected String personFirstName;
    protected String personMiddleName;
    protected String personLastName;
    protected String personNameSuffix;
    protected String personBirthDate;
    protected String personAge;
    
    protected String personActivityInvolvementText;
    
    protected List<String> personTelephoneNumbers = new ArrayList<String>();
    
    protected List<Alias> aliases = new ArrayList<Alias>();

    protected String notificationEventIdentifier;
    protected String notifyingAgencyName;
    private String notifyingAgencyOri;
    protected String notifyingAgencyPhoneNumber;
    protected String notifyingSystemName;

    protected List<String> officerNames = new ArrayList<String>();
    
    protected Map<String, String> subjectIdentifiers;
    protected List<Map<String, String>> alternateSubjectIdentifiers = new ArrayList<Map<String,String>>();

    protected String topic;

    /**
     * Should not return the last node of nc:Date or nc:DateTime, but rather should return the parent node, one level up
     */
    protected abstract String getNotificationEventDateRootXpath();

    protected abstract String getNotifyingAgencyXpath();

    protected abstract String getNotifyingAgencyOriXpath();

    protected abstract String getNotificationAgencyPhoneNumberXpath();

    protected abstract String getNotificationEventIdentifierXpath();

    protected abstract String getNotifyingSystemNameXPath();

    protected abstract String getOfficerNameReferenceXPath();

    /**
     * Get a string from the request that helps to identify the subject of the notification. This can be used for logging and related purposes.
     * 
     * @return
     */
    abstract public String getDescriptiveSubjectIdentifier();

    public NotificationRequest(Document document) throws Exception {
        
        this.requestDocument = document;

        String notificationEventDateTimeString = XmlUtils.xPathStringSearch(document, getNotificationEventDateRootXpath() + "/nc:DateTime");
        String notificationEventDateOnlyString = XmlUtils.xPathStringSearch(document, getNotificationEventDateRootXpath() + "/nc:Date");

        if (StringUtils.isNotEmpty(notificationEventDateTimeString)) {
            notificationEventDate = XmlUtils.parseXmlDateTime(notificationEventDateTimeString);
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
            personFirstName = StringUtils.strip(XmlUtils.xPathStringSearch(document, "/b-2:Notify/b-2:NotificationMessage/b-2:Message/notfm-exch:NotificationMessage/jxdm41:Person[@s:id='" + personReference
                    + "']/nc:PersonName/nc:PersonGivenName"));
            personMiddleName = StringUtils.strip(XmlUtils.xPathStringSearch(document, "/b-2:Notify/b-2:NotificationMessage/b-2:Message/notfm-exch:NotificationMessage/jxdm41:Person[@s:id='" + personReference
                    + "']/nc:PersonName/nc:PersonMiddleName"));
            personLastName = StringUtils.strip(XmlUtils.xPathStringSearch(document, "/b-2:Notify/b-2:NotificationMessage/b-2:Message/notfm-exch:NotificationMessage/jxdm41:Person[@s:id='" + personReference
                    + "']/nc:PersonName/nc:PersonSurName"));
            personNameSuffix = StringUtils.strip(XmlUtils.xPathStringSearch(document, "/b-2:Notify/b-2:NotificationMessage/b-2:Message/notfm-exch:NotificationMessage/jxdm41:Person[@s:id='" + personReference
                    + "']/nc:PersonName/nc:PersonNameSuffixText"));
            personBirthDate = StringUtils.strip(XmlUtils.xPathStringSearch(document, "/b-2:Notify/b-2:NotificationMessage/b-2:Message/notfm-exch:NotificationMessage/jxdm41:Person[@s:id='" + personReference
                    + "']/nc:PersonBirthDate/nc:Date"));
            
            try
            {
            	personAge = NotificationBrokerUtils.calculatePersonAgeFromDate(personBirthDate);	
            }
            catch (Exception ex)
            {
            	log.error("Unable to calculate person age.");
            }
            	
            NodeList aliasNodes = XmlUtils.xPathNodeListSearch(document,
            		"/b-2:Notify/b-2:NotificationMessage/b-2:Message/notfm-exch:NotificationMessage/jxdm41:Person[@s:id='" + personReference + "']/nc:PersonAlternateName");

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
            log.error("Unable to find person reference. Unable to XQuery for person name.");
        }

        NodeList officerReferences = XmlUtils.xPathNodeListSearch(document, getOfficerNameReferenceXPath());

		if (officerReferences != null && officerReferences.getLength() > 0) {
            for (int i = 0; i < officerReferences.getLength(); i++) {
                if (officerReferences.item(i).getNodeType() == Node.ATTRIBUTE_NODE) {

                    String officerReference = officerReferences.item(i).getTextContent();
                    String officerName = XmlUtils.xPathStringSearch(document, "/b-2:Notify/b-2:NotificationMessage/b-2:Message/notfm-exch:NotificationMessage/jxdm41:Person[@s:id='" + officerReference + "']/nc:PersonName/nc:PersonFullName");
                    
                    if (StringUtils.isNotEmpty(officerName))
                    {	
                    	officerNames.add(StringUtils.strip(officerName));
                    }	
                }
            }
		}    

        notificationEventIdentifier = XmlUtils.xPathStringSearch(document, getNotificationEventIdentifierXpath());
        notificationEventIdentifier = StringUtils.strip(notificationEventIdentifier);

        notifyingAgencyName = XmlUtils.xPathStringSearch(document, getNotifyingAgencyXpath());
        notifyingAgencyName = StringUtils.strip(notifyingAgencyName);

        notifyingAgencyOri = StringUtils.trimToNull(XmlUtils.xPathStringSearch(document, getNotifyingAgencyOriXpath()));
        
        notifyingAgencyPhoneNumber = XmlUtils.xPathStringSearch(document, getNotificationAgencyPhoneNumberXpath());
        notifyingAgencyPhoneNumber = StringUtils.strip(notifyingAgencyPhoneNumber);

        notifyingSystemName = XmlUtils.xPathStringSearch(document, getNotifyingSystemNameXPath());
        notifyingSystemName = StringUtils.strip(notifyingSystemName);

        // subjectIdentification intentionally omitted - should be populated in subclass

        Node topicNode = XmlUtils.xPathNodeSearch(document, "/b-2:Notify/b-2:NotificationMessage/b-2:Topic");
        String unqualifiedTopic = topicNode.getTextContent();
        topic = NotificationBrokerUtils.getFullyQualifiedTopic(unqualifiedTopic);

    }

    public NotificationRequest(Message message) throws Exception {
        this(message.getBody(Document.class));
    }

    public String getPersonFirstName() {
        return personFirstName;
    }

    public String getPersonMiddleName() {
        return personMiddleName;
    }

    public String getPersonLastName() {
        return personLastName;
    }

    public String getPersonNameSuffix() {
        return personNameSuffix;
    }

    public Map<String, String> getSubjectIdentifiers() {
        return subjectIdentifiers;
    }

    public String getNotificationEventIdentifier() {
        return notificationEventIdentifier;
    }

    public String getNotifyingAgencyName() {
        return notifyingAgencyName;
    }

    public String getNotifyingAgencyPhoneNumber() {
        return notifyingAgencyPhoneNumber;
    }

    public String getNotifyingSystemName() {
        return notifyingSystemName;
    }

    public String getPersonActivityInvolvementText() {
        return personActivityInvolvementText;
    }

    public DateTime getNotificationEventDate() {
        return notificationEventDate;
    }

    public boolean isNotificationEventDateInclusiveOfTime() {
        return isNotificationEventDateInclusiveOfTime;
    }

    public String getTopic() {
        return topic;
    }

    public Document getRequestDocument() {
        return requestDocument;
    }

    public void setRequestDocument(Document requestDocument) {
        this.requestDocument = requestDocument;
    }

	public String getPersonBirthDate() {
		return personBirthDate;
	}
	
    public class Alias {

        private String personFirstName;
        private String personLastName;
        
		public String getPersonFirstName() {
			return personFirstName;
		}
		public void setPersonFirstName(String personFirstName) {
			this.personFirstName = personFirstName;
		}
		public String getPersonLastName() {
			return personLastName;
		}
		public void setPersonLastName(String personLastName) {
			this.personLastName = personLastName;
		}

    }

	public List<Alias> getAliases() {
		return aliases;
	}

	public List<String> getOfficerNames() {
		return officerNames;
	}

	public List<String> getPersonTelephoneNumbers() {
		return personTelephoneNumbers;
	}

	public String getPersonAge() {
		return personAge;
	}

	public String getNotifyingAgencyOri() {
		return notifyingAgencyOri;
	}

	public void setSubjectIdentifiers(Map<String, String> subjectIdentifiers) {
		this.subjectIdentifiers = subjectIdentifiers;
	}

	public List<Map<String, String>> getAlternateSubjectIdentifiers() {
		return alternateSubjectIdentifiers;
	}

}
