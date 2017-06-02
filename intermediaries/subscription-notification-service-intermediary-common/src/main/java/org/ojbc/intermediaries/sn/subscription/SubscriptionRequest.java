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
package org.ojbc.intermediaries.sn.subscription;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.ojbc.intermediaries.sn.exception.InvalidEmailAddressesException;
import org.ojbc.intermediaries.sn.util.EmailAddressValidatorResponse;
import org.ojbc.util.xml.XmlUtils;
import org.apache.camel.Message;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

/**
 * The Subscription Request class is abstract and is extended by concrete classes.
 * It contains the core subscription elements that must be present in all subscriptions.
 * Specific topics can have additional subscription elements.
 * 
 * It will check if email addresses are unique and follow a defined email address pattern.
 * If there is a duplicate email address, a warning is logged but the subscription is still added.
 * 
 */
public abstract class SubscriptionRequest {

	private static final Log log = LogFactory.getLog(SubscriptionRequest.class);
	
	private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormat.forPattern("yyyy-MM-dd");
	
	protected Document document;
	private String topic;
	private String startDateString;
	private String endDateString;
	private String subjectName;
	private Set<String> emailAddresses;
	private String systemName;
	private String subscriptionQualifier;
	private String subscriptionSystemId;
	protected Map<String, String> subjectIdentifiers;
	protected Map<String, String> subscriptionProperties;
	private String agencyCaseNumber; 	
	private String reasonCategoryCode;
	private String subscriptionOwner;
	private String subscriptionOwnerEmailAddress;
	private String ori;
	
	public String getSubscriptionOwnerEmailAddress() {
		return subscriptionOwnerEmailAddress;
	}

	public void setSubscriptionOwnerEmailAddress(
			String subscriptionOwnerEmailAddress) {
		this.subscriptionOwnerEmailAddress = subscriptionOwnerEmailAddress;
	}
	
	public SubscriptionRequest(){
		super();
	}

	public SubscriptionRequest(Message message, String allowedEmailAddressPatterns) throws Exception{
	
		this();
		//Get the message body as DOM
		document = message.getBody(Document.class);
		
		reasonCategoryCode = XmlUtils.xPathStringSearch(document, "//submsg-exch:SubscriptionMessage/submsg-ext:CriminalSubscriptionReasonCode|//submsg-exch:SubscriptionMessage/submsg-ext:CivilSubscriptionReasonCode");		
		
		setTopic(XmlUtils.xPathStringSearch(document, "//b-2:Subscribe/b-2:Filter/b-2:TopicExpression"));
		setTopic(StringUtils.replace(getTopic(), "topics:", "{http://ojbc.org/wsn/topics}:"));
		
		Node subscriptionMsg = XmlUtils.xPathNodeSearch(document,"//submsg-exch:SubscriptionMessage");
		
		setStartDateString(XmlUtils.xPathStringSearch(subscriptionMsg,"nc:DateRange/nc:StartDate/nc:Date"));
		setEndDateString(XmlUtils.xPathStringSearch(subscriptionMsg,"nc:DateRange/nc:EndDate/nc:Date"));
		
		//Check start date versus end date here
		if (StringUtils.isNotBlank(getStartDateString()) && StringUtils.isNotBlank(getEndDateString()))
		{
			DateTime startDate = DATE_FORMATTER.parseDateTime(getStartDateString());
			DateTime endDate = DATE_FORMATTER.parseDateTime(getEndDateString());
			
			if (endDate.toDateMidnight().isBefore(startDate.toDateMidnight()))
			{
				throw new Exception("End Date Can Not Be Before Start Date in a Subscription.");
			}	
			
		}	
		
		NodeList emailNodes = XmlUtils.xPathNodeListSearch(subscriptionMsg,"nc:ContactEmailID");
		
		boolean wasEntryAddedToSet;
		
	    if (emailNodes != null && emailNodes.getLength() > 0) {
	        for (int i = 0; i < emailNodes.getLength(); i++) {
	            if (emailNodes.item(i).getNodeType() == Node.ELEMENT_NODE) {
	    			if (getEmailAddresses() == null)
	    			{
	    				setEmailAddresses(new HashSet<String>());
	    			}	
	    			
	    			wasEntryAddedToSet = getEmailAddresses().add(emailNodes.item(i).getTextContent());
	    			
	    			if (!wasEntryAddedToSet)
	    			{
	    				log.warn("Email Address is a duplicate and will not be added to subscription: " + emailNodes.item(i).getTextContent());
	    			}	
	            	
	            }
	        }
	    }
		
	    if (getEmailAddresses() == null || getEmailAddresses().size() == 0)
	    {
	    	throw new Exception("The request does not contain any email addresses.");
	    }	
	    
		EmailAddressPatternValidator emailAddressPatternValidator = new EmailAddressPatternValidator(allowedEmailAddressPatterns);
		
		//Check email addresses here
		EmailAddressValidatorResponse emailAddressValidatorResponse = emailAddressPatternValidator.areEmailAddressesValid(new ArrayList<String>(getEmailAddresses()));
		
		if (!emailAddressValidatorResponse.isAreAllEmailAddressValid())
		{
			log.error("The request contains invalid email addresses.");
			List<String> invalidEmailAddress = new ArrayList<String>();
			invalidEmailAddress.addAll(emailAddressValidatorResponse.getInvalidEmailAddresses());
			
			throw new InvalidEmailAddressesException("The request contains invalid email addresses.", invalidEmailAddress);
		}	
		
		setSystemName(XmlUtils.xPathStringSearch(subscriptionMsg,"submsg-ext:SystemName"));
		setSubjectName(XmlUtils.xPathStringSearch(subscriptionMsg,"submsg-ext:Subject/nc:PersonName/nc:PersonFullName"));
		setSubscriptionQualifier(XmlUtils.xPathStringSearch(subscriptionMsg,"submsg-ext:SubscriptionQualifierIdentification/nc:IdentificationID"));
		setSubscriptionSystemId(XmlUtils.xPathStringSearch(subscriptionMsg,"submsg-ext:SubscriptionIdentification/nc:IdentificationID"));
		agencyCaseNumber = XmlUtils.xPathStringSearch(subscriptionMsg, "submsg-ext:SubscriptionRelatedCaseIdentification/nc:IdentificationID");
		ori = XmlUtils.xPathStringSearch(subscriptionMsg, 
				"submsg-ext:SubscribingOrganization/jxdm41:OrganizationAugmentation/jxdm41:OrganizationORIIdentification/nc:IdentificationID");
		
		
		subscriptionOwner = (String) message.getHeader("subscriptionOwner");
		subscriptionOwnerEmailAddress = (String) message.getHeader("subscriptionOwnerEmailAddress");
		
		// subjectIdentifiers intentionally left out - should be populated by derived class 
		// subscriptionProperties intentionally left out - should be populated by derived class
	}
	
	public String getSubscriptionOwner() {
		return subscriptionOwner;
	}

	public void setSubscriptionOwner(String subscriptionOwner) {
		this.subscriptionOwner = subscriptionOwner;
	}

	public String getTopic() {
		return topic;
	}
	
	public String getStartDateString() {
		return startDateString;
	}
	
	public String getEndDateString() {
		return endDateString;
	}
	
	public String getSubjectName() {
		return subjectName;
	}
	
	public String getSystemName() {
		return systemName;
	}
	
	public String getSubscriptionQualifier() {
		return subscriptionQualifier;
	}
	
	public Map<String, String> getSubjectIdentifiers() {
		return subjectIdentifiers;
	}

	public Set<String> getEmailAddresses() {
		return emailAddresses;
	}
	
	public String getSubscriptionSystemId() {
		return subscriptionSystemId;
	}

	public String getAgencyCaseNumber() {
		return agencyCaseNumber;
	}

	public void setAgencyCaseNumber(String agencyCaseNumber) {
		this.agencyCaseNumber = agencyCaseNumber;
	}

	public String getReasonCategoryCode() {
		return reasonCategoryCode;
	}

	public void setReasonCategoryCode(String reasonCategoryCode) {
		this.reasonCategoryCode = reasonCategoryCode;
	}

	public void setSubjectIdentifiers(Map<String, String> subjectIdentifiers) {
		this.subjectIdentifiers = subjectIdentifiers;
	}	
	
	public Map<String, String> getSubscriptionProperties() {
		return subscriptionProperties;
	}

	public void setSubscriptionProperties(Map<String, String> subscriptionProperties) {
		this.subscriptionProperties = subscriptionProperties;
	}

	public String getOri() {
		return ori;
	}

	public void setOri(String ori) {
		this.ori = ori;
	}
	
	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this, ToStringStyle.MULTI_LINE_STYLE);	
	}

	public void setSubscriptionSystemId(String subscriptionSystemId) {
		this.subscriptionSystemId = subscriptionSystemId;
	}

	public void setSubscriptionQualifier(String subscriptionQualifier) {
		this.subscriptionQualifier = subscriptionQualifier;
	}

	public void setSystemName(String systemName) {
		this.systemName = systemName;
	}

	public void setEmailAddresses(Set<String> emailAddresses) {
		this.emailAddresses = emailAddresses;
	}

	public void setSubjectName(String subjectName) {
		this.subjectName = subjectName;
	}

	public void setEndDateString(String endDateString) {
		this.endDateString = endDateString;
	}

	public void setStartDateString(String startDateString) {
		this.startDateString = startDateString;
	}

	public void setTopic(String topic) {
		this.topic = topic;
	}


}
