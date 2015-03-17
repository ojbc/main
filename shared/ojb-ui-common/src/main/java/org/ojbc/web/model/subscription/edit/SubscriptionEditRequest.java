package org.ojbc.web.model.subscription.edit;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class SubscriptionEditRequest implements Serializable{

    private static final long serialVersionUID = -1155944074369877946L;

    private String subscriptionType;
	
	private String stateId;	
	
	private String systemId;
	
	private String fullName;
	
	//note f/l name only used for incident(not arrest)
	// TODO - probably remove fullname and get arrest to use f/l Name,
	// or create a new pojo for incident
	private String firstName;	
	private String lastName;	
	
	private Date dateOfBirth;
	
	private Date subscriptionStartDate;
	
	private Date subscriptionEndDate;
	
	private List<String> emailList = new ArrayList<String>();		
	
	private String personNamesJsonArray;

	
	
	public String getSubscriptionType() {
		return subscriptionType;
	}

	public String getStateId() {
		return stateId;
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

	public Date getDateOfBirth() {
		return dateOfBirth;
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

	public String getPersonNamesJsonArray() {
		return personNamesJsonArray;
	}

	public void setSubscriptionType(String subscriptionType) {
		this.subscriptionType = subscriptionType;
	}

	public void setStateId(String stateId) {
		this.stateId = stateId;
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

	public void setDateOfBirth(Date dateOfBirth) {
		this.dateOfBirth = dateOfBirth;
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

	public void setPersonNamesJsonArray(String personNamesJsonArray) {
		this.personNamesJsonArray = personNamesJsonArray;
	}

	@Override
	public String toString() {
		return "SubscriptionEditRequest [subscriptionType=" + subscriptionType
				+ ", stateId=" + stateId + ", systemId=" + systemId
				+ ", fullName=" + fullName + ", firstName=" + firstName
				+ ", lastName=" + lastName + ", dateOfBirth=" + dateOfBirth
				+ ", subscriptionStartDate=" + subscriptionStartDate
				+ ", subscriptionEndDate=" + subscriptionEndDate
				+ ", emailList=" + emailList + ", personNamesJsonArray="
				+ personNamesJsonArray + "]";
	}
		
}



