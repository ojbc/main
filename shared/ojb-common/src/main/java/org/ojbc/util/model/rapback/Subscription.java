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
package org.ojbc.util.model.rapback;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.joda.time.DateTime;
import org.joda.time.Interval;
import org.ojbc.util.model.SubscriptionCategoryCode;
import org.ojbc.util.rest.jackson.JodaDateTimeDeserializer;
import org.ojbc.util.rest.jackson.JodaDateTimeSerializer;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;


/**
 * Data Access Object for subscriptions.
 *
 */
public class Subscription {

	private static final String MM_DD_YYYY = "MM/dd/yyyy";

	private long id;
	
	@JsonSerialize(using = JodaDateTimeSerializer.class)
	@JsonDeserialize(using = JodaDateTimeDeserializer.class)
	private DateTime startDate;

	@JsonSerialize(using = JodaDateTimeSerializer.class)
	@JsonDeserialize(using = JodaDateTimeDeserializer.class)
	private DateTime endDate;

	@JsonSerialize(using = JodaDateTimeSerializer.class)
	@JsonDeserialize(using = JodaDateTimeDeserializer.class)
	private DateTime lastValidationDate;

	@JsonSerialize(using = JodaDateTimeSerializer.class)
	@JsonDeserialize(using = JodaDateTimeDeserializer.class)
	private DateTime creationDate;

	@JsonSerialize(using = JodaDateTimeSerializer.class)
	@JsonDeserialize(using = JodaDateTimeDeserializer.class)
	private DateTime lastUpdatedDate;

    private String topic;
	
	private String sendingState;
    private String receivingState;
    
	private String personFirstName;
    private String personLastName;
    private String personFullName;
    private String dateOfBirth;
	
	private Set<String> emailAddressesToNotify;
	private Map<String, String> subscriptionSubjectIdentifiers;
	
	@JsonIgnore
	private Integer subscriptionOwnerFk;
	
	private String subscriptionOwner;
	private String subscriptionOwnerEmailAddress;
	private String subscriptionOwnerFirstName;
	private String subscriptionOwnerLastName;

	private String subscriptionIdentifier;
	private String subscribingSystemIdentifier;
	
	private String state;
	
	@JsonSerialize(using = JodaDateTimeSerializer.class)
	@JsonDeserialize(using = JodaDateTimeDeserializer.class)
	private DateTime validationDueDate;
	
	private Interval gracePeriod;
	
	@JsonIgnore
	private Boolean active; 
	
	private String agencyCaseNumber; 

	private String subscriptionCategoryCode;
	private String ori;
	private String agencyName;

	private Map<String, String> subscriptionProperties;
	
	@JsonIgnore
	private FbiRapbackSubscription fbiRapbackSubscription;
	
	@JsonIgnore
	DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM/dd/yyyy");

	private String stateId;	
	
	private String fbiId;
	
	private String systemId;
	
	private String fullName;
	
	private String otherIdentificationId;
	
	@JsonSerialize(using = JodaDateTimeSerializer.class)
	@JsonDeserialize(using = JodaDateTimeDeserializer.class)
	private LocalDate gracePeriodStartDate;
	
	@JsonSerialize(using = JodaDateTimeSerializer.class)
	@JsonDeserialize(using = JodaDateTimeDeserializer.class)
	private LocalDate gracePeriodEndDate; 
	
	//note f/l name only used for incident(not arrest)
	private String firstName;	
	
	private String lastName;
	
	private String sex;
	
	private String race;
	
	@JsonSerialize(using = JodaDateTimeSerializer.class)
	@JsonDeserialize(using = JodaDateTimeDeserializer.class)
	private Date subscriptionStartDate;
	
	@JsonSerialize(using = JodaDateTimeSerializer.class)
	@JsonDeserialize(using = JodaDateTimeDeserializer.class)
	private Date subscriptionEndDate;
	
	private List<String> emailList = new ArrayList<String>();
	
	//Category
	private String subscriptionPurpose;
	
	//Agency case number, save OCA into this field
	private String caseId;  

	private String systemName;

	//If set, the value provided will be used other wise it is autogenerated
	private String subscriptionQualificationId;
	
	private Boolean federalRapSheetDisclosureIndicator;
	
	private String federalRapSheetDisclosureAttentionDesignationText;
	
	private List<String> federalTriggeringEventCode = new ArrayList<String>();
	
	private String fbiSubscriptionID;
	
	private String ownerFederationId;
	private String ownerEmailAddress;
	private String ownerFirstName;
	private String ownerLastName;
	
	private String transactionNumber; 
	private String ownerProgramOca; 
	
	@JsonSerialize(using = JodaDateTimeSerializer.class)
	@JsonDeserialize(using = JodaDateTimeDeserializer.class)
	private DateTime lastMatchDate;
	
	public DateTime getLastMatchDate() {
		return lastMatchDate;
	}

	public void setLastMatchDate(DateTime lastMatchDate) {
		this.lastMatchDate = lastMatchDate;
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}
	
	public String getStateId() {
		return stateId;
	}

	public String getFbiId() {
		return fbiId;
	}

	public String getSystemId() {
		return systemId;
	}

	public String getFullName() {
		return fullName;
	}

	public String getFirstName() {
		return firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public Date getSubscriptionStartDate() {
		return subscriptionStartDate;
	}

	public Date getSubscriptionEndDate() {
		return subscriptionEndDate;
	}

	public List<String> getEmailList() {
		return emailList;
	}

	public String getSubscriptionPurpose() {
		return subscriptionPurpose;
	}

	public String getCaseId() {
		return caseId;
	}

	public void setStateId(String stateId) {
		this.stateId = StringUtils.trimToEmpty(stateId);
	}

	public void setFbiId(String fbiId) {
		this.fbiId = fbiId;
	}

	public void setSystemId(String systemId) {
		this.systemId = systemId;
	}

	public void setFullName(String fullName) {
		this.fullName = fullName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public void setSubscriptionStartDate(Date subscriptionStartDate) {
		this.subscriptionStartDate = subscriptionStartDate;
	}

	public void setSubscriptionEndDate(Date subscriptionEndDate) {
		this.subscriptionEndDate = subscriptionEndDate;
	}

	public void setEmailList(List<String> emailList) {
		this.emailList = emailList;
	}

	public void setSubscriptionPurpose(String subscriptionPurpose) {
		this.subscriptionPurpose = subscriptionPurpose;
	}

	public void setCaseId(String caseId) {
		this.caseId = caseId;
	}

	public String getSystemName() {
		return systemName;
	}

	public void setSystemName(String systemName) {
		this.systemName = systemName;
	}

	public String getSubscriptionQualificationId() {
		return subscriptionQualificationId;
	}

	public void setSubscriptionQualificationId(String subscriptionQualificationId) {
		this.subscriptionQualificationId = subscriptionQualificationId;
	}

	public Boolean getFederalRapSheetDisclosureIndicator() {
		return federalRapSheetDisclosureIndicator;
	}

	public void setFederalRapSheetDisclosureIndicator(
			Boolean federalRapSheetDisclosureIndicator) {
		this.federalRapSheetDisclosureIndicator = federalRapSheetDisclosureIndicator;
	}

	public String getFederalRapSheetDisclosureAttentionDesignationText() {
		return federalRapSheetDisclosureAttentionDesignationText;
	}

	public void setFederalRapSheetDisclosureAttentionDesignationText(
			String federalRapSheetDisclosureAttentionDesignationText) {
		this.federalRapSheetDisclosureAttentionDesignationText = federalRapSheetDisclosureAttentionDesignationText;
	}

	public List<String> getFederalTriggeringEventCode() {
		return federalTriggeringEventCode;
	}

	public void setFederalTriggeringEventCode(
			List<String> federalTriggeringEventCode) {
		this.federalTriggeringEventCode = federalTriggeringEventCode;
	}
	
	public String getFormattedFbiId() {
		return fbiId == null? "No FBI UCN found" : fbiId;
	}

	public String getFbiSubscriptionID() {
		return fbiSubscriptionID;
	}

	public void setFbiSubscriptionID(String fbiSubscriptionID) {
		this.fbiSubscriptionID = fbiSubscriptionID;
	}

	public String getOwnerFederationId() {
		return ownerFederationId;
	}

	public void setOwnerFederationId(String ownerFederationId) {
		this.ownerFederationId = ownerFederationId;
	}

	public String getOwnerEmailAddress() {
		return ownerEmailAddress;
	}

	public void setOwnerEmailAddress(String ownerEmailAddress) {
		this.ownerEmailAddress = ownerEmailAddress;
	}

	public String getOwnerFirstName() {
		return ownerFirstName;
	}

	public void setOwnerFirstName(String ownerFirstName) {
		this.ownerFirstName = ownerFirstName;
	}

	public String getOwnerLastName() {
		return ownerLastName;
	}

	public void setOwnerLastName(String ownerLastName) {
		this.ownerLastName = ownerLastName;
	}


	public LocalDate getGracePeriodStartDate() {
		return gracePeriodStartDate;
	}

	public void setGracePeriodStartDate(LocalDate gracePeriodStartDate) {
		this.gracePeriodStartDate = gracePeriodStartDate;
	}

	public LocalDate getGracePeriodEndDate() {
		return gracePeriodEndDate;
	}

	public void setGracePeriodEndDate(LocalDate gracePeriodEndDate) {
		this.gracePeriodEndDate = gracePeriodEndDate;
	}

	public String getTransactionNumber() {
		return transactionNumber;
	}

	public void setTransactionNumber(String transactionNumber) {
		this.transactionNumber = transactionNumber;
	}

	public String getOwnerProgramOca() {
		return ownerProgramOca;
	}

	public void setOwnerProgramOca(String ownerProgramOca) {
		this.ownerProgramOca = ownerProgramOca;
	}
	
	public String getSex() {
		return sex;
	}

	public void setSex(String sex) {
		this.sex = sex;
	}

	public String getRace() {
		return race;
	}

	public void setRace(String race) {
		this.race = race;
		
	}
	
	
    public DateTime getValidationDueDate() {
	    return validationDueDate;
	}
    
	@JsonIgnore
	public String getValidationDueDateString() {
		return validationDueDate != null? validationDueDate.toString(MM_DD_YYYY):"";
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
    
	@JsonIgnore
	public String getLastValidationDateString() {
		return lastValidationDate != null? lastValidationDate.toString(MM_DD_YYYY):"";
	}
    public void setLastValidationDate(DateTime lastValidationDate) {
        this.lastValidationDate = lastValidationDate;
    }
    
	public DateTime getStartDate() {
		return startDate;
	}
	
	@JsonIgnore
	public String getStartDateString() {
		return startDate != null? startDate.toString(MM_DD_YYYY):"";
	}
	public void setStartDate(DateTime startDate) {
		this.startDate = startDate;
	}
	public DateTime getEndDate() {
		return endDate;
	}
	
	@JsonIgnore
	public String getEndDateString() {
		return endDate != null? endDate.toString(MM_DD_YYYY):"";
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
	
	public void setActiveInt(Integer active) {
		this.active = (active.intValue() == 1);
	}

	public String getAgencyCaseNumber() {
		return StringUtils.trimToEmpty(agencyCaseNumber);
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

	public String getOri() {
		return ori;
	}

	public void setOri(String ori) {
		this.ori = ori;
	}

	public DateTime getCreationDate() {
		return creationDate;
	}

	public void setCreationDate(DateTime creationDate) {
		this.creationDate = creationDate;
	}

	public Integer getSubscriptionOwnerFk() {
		return subscriptionOwnerFk;
	}

	public void setSubscriptionOwnerFk(Integer subscriptionOwnerFk) {
		this.subscriptionOwnerFk = subscriptionOwnerFk;
	}

	public String getSubscriptionOwnerFirstName() {
		return subscriptionOwnerFirstName;
	}

	public void setSubscriptionOwnerFirstName(String subscriptionOwnerFirstName) {
		this.subscriptionOwnerFirstName = subscriptionOwnerFirstName;
	}

	public String getSubscriptionOwnerLastName() {
		return subscriptionOwnerLastName;
	}

	public void setSubscriptionOwnerLastName(String subscriptionOwnerLastName) {
		this.subscriptionOwnerLastName = subscriptionOwnerLastName;
	}

	@JsonIgnore
	public String getSubscriptionOwnerName(){
		return subscriptionOwnerFirstName + " " + subscriptionOwnerLastName;
	}
	public String getAgencyName() {
		return agencyName;
	}

	public void setAgencyName(String agencyName) {
		this.agencyName = agencyName;
	}

	public DateTime getLastUpdatedDate() {
		return lastUpdatedDate;
	}

	public void setLastUpdatedDate(DateTime lastUpdatedDate) {
		this.lastUpdatedDate = lastUpdatedDate;
	}
	
	public String getOtherIdentificationId() {
		return otherIdentificationId;
	}

	public void setOtherIdentificationId(String otherIdentificationId) {
		this.otherIdentificationId = otherIdentificationId;
	}
	
    public String getSendingState() {
		return sendingState;
	}

	public void setSendingState(String sendingState) {
		this.sendingState = sendingState;
	}

	public String getReceivingState() {
		return receivingState;
	}

	public void setReceivingState(String receivingState) {
		this.receivingState = receivingState;
	}

	@JsonIgnore
	public boolean isExpired(){
		return endDate != null && 
				((gracePeriod != null && gracePeriod.isBeforeNow()) 
				|| gracePeriod == null && endDate.isBeforeNow() );
	}
	@JsonIgnore
	public boolean isCriminalRapback(){
		return SubscriptionCategoryCode.isCriminalCategoryCode(subscriptionCategoryCode);
	}
	@JsonIgnore
	public boolean isCivilRapback(){
		return SubscriptionCategoryCode.isCivilCategoryCode(subscriptionCategoryCode);
	}
	@JsonIgnore
	public boolean isNotExpired(){
		return !isExpired();
	}
	
	
}
