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
package org.ojbc.intermediaries.sn.dao;

import java.util.Map;
import java.util.Set;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;
import org.joda.time.DateTime;
import org.joda.time.Interval;
import org.ojbc.intermediaries.sn.dao.rapback.FbiRapbackSubscription;

/**
 * Data Access Object for subscriptions.
 *
 */
public class Subscription {

	private long id;
	
	private DateTime startDate;
	private DateTime endDate;
	private DateTime lastValidationDate;
	
    private String topic;
	
	private String personFirstName;
	private String personLastName;
	private String personFullName;
	private String dateOfBirth;
	
	private Set<String> emailAddressesToNotify;
	private Map<String, String> subscriptionSubjectIdentifiers;
	
	private String subscriptionOwner;
	private String subscriptionOwnerEmailAddress;

	private String subscriptionIdentifier;
	private String subscribingSystemIdentifier;
	
	private DateTime validationDueDate;
	private Interval gracePeriod;
	private Boolean active; 
	
	private String agencyCaseNumber; 
	private String subscriptionCategoryCode; 

	private Map<String, String> subscriptionProperties;
	
	private FbiRapbackSubscription fbiRapbackSubscription;
	
    public DateTime getValidationDueDate() {
	    return validationDueDate;
	}
	
	public Interval getGracePeriod() {
	    return gracePeriod;
	}
	
    public void setValidationDueDate(DateTime validationDueDate) {
        this.validationDueDate = validationDueDate;
    }

    public void setGracePeriod(Interval gracePeriod) {
        this.gracePeriod = gracePeriod;
    }

    public DateTime getLastValidationDate() {
        return lastValidationDate;
    }
    public void setLastValidationDate(DateTime lastValidationDate) {
        this.lastValidationDate = lastValidationDate;
    }
	public DateTime getStartDate() {
		return startDate;
	}
	public void setStartDate(DateTime startDate) {
		this.startDate = startDate;
	}
	public DateTime getEndDate() {
		return endDate;
	}
	public void setEndDate(DateTime endDate) {
		this.endDate = endDate;
	}
	public String getTopic() {
		return topic;
	}
	public void setTopic(String topic) {
		this.topic = topic;
	}
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
	public String getSubscriptionOwner() {
		return subscriptionOwner;
	}
	public void setSubscriptionOwner(String subscriptionOwner) {
		this.subscriptionOwner = subscriptionOwner;
	}
	public String getSubscribingSystemIdentifier() {
		return subscribingSystemIdentifier;
	}
	public void setSubscribingSystemIdentifier(String subscribingSystemIdentifier) {
		this.subscribingSystemIdentifier = subscribingSystemIdentifier;
	}
	public String getSubscriptionIdentifier() {
		return subscriptionIdentifier;
	}
	public void setSubscriptionIdentifier(String subscriptionIdentifier) {
		this.subscriptionIdentifier = subscriptionIdentifier;
	}
	public Set<String> getEmailAddressesToNotify() {
		return emailAddressesToNotify;
	}
	public void setEmailAddressesToNotify(Set<String> emailAddressToNotify) {
		this.emailAddressesToNotify = emailAddressToNotify;
	}
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public String getPersonFullName() {
		return personFullName;
	}
	public void setPersonFullName(String personFullName) {
		this.personFullName = personFullName;
	}

	public Map<String, String> getSubscriptionSubjectIdentifiers() {
		return subscriptionSubjectIdentifiers;
	}
	public void setSubscriptionSubjectIdentifiers(
			Map<String, String> subscriptionSubjectIdentifiers) {
		this.subscriptionSubjectIdentifiers = subscriptionSubjectIdentifiers;
	}
	public String getDateOfBirth() {
		return dateOfBirth;
	}
	public void setDateOfBirth(String dateOfBirth) {
		this.dateOfBirth = dateOfBirth;
	}
	
	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this, ToStringStyle.MULTI_LINE_STYLE);
	}

	public Boolean getActive() {
		return active;
	}

	public void setActive(Boolean active) {
		this.active = active;
	}
	
	public void setActive(Integer active) {
		this.active = (active.intValue() == 1);
	}

	public String getAgencyCaseNumber() {
		return agencyCaseNumber;
	}

	public void setAgencyCaseNumber(String agencyCaseNumber) {
		this.agencyCaseNumber = agencyCaseNumber;
	}

	public String getSubscriptionCategoryCode() {
		return subscriptionCategoryCode;
	}

	public void setSubscriptionCategoryCode(String subscriptionCategoryCode) {
		this.subscriptionCategoryCode = subscriptionCategoryCode;
	}

	public String getSubscriptionOwnerEmailAddress() {
		return subscriptionOwnerEmailAddress;
	}

	public void setSubscriptionOwnerEmailAddress(
			String subscriptionOwnerEmailAddress) {
		this.subscriptionOwnerEmailAddress = subscriptionOwnerEmailAddress;
	}

	public Map<String, String> getSubscriptionProperties() {
		return subscriptionProperties;
	}

	public void setSubscriptionProperties(Map<String, String> subscriptionProperties) {
		this.subscriptionProperties = subscriptionProperties;
	}

	public FbiRapbackSubscription getFbiRapbackSubscription() {
		return fbiRapbackSubscription;
	}

	public void setFbiRapbackSubscription(FbiRapbackSubscription fbiRapbackSubscription) {
		this.fbiRapbackSubscription = fbiRapbackSubscription;
	}

}
