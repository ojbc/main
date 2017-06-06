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
package org.ojbc.bundles.adapters.staticmock.custody;

import java.util.Date;

public class CustodyDetail {

	private String docCreationDate;
	
	private String docId;

	private String personDob;
	
	private String personGivenName;
	
	private String personMiddleName;
	
	private String personSurName;
	
	private String personSex;
	
	private String personRace;
	
	private String personSsn;
	
	private String personStateId;		
	
	private Date bookingActivityDate;
	
	private Integer bookingNumber;
	
	private String bookingSubjectId;
	
	private String imageLocation;
	
	private String chargeCount;
	
	private String chargeDescription;
	
	private String chargeStatuteCodeId;
	
	private String statuteIdCategoryDescriptionTxt;
	
	private String sourceSystemNameText;
	
	private String systemId;
	
	private String systemName;
	
	private String searchResultCategoryDescriptionText;
	
	private String organizationName;
	
	private String organizationBranchName;
	
	private String lastUpdatedDate;

	public String getDocCreationDate() {
		return docCreationDate;
	}

	public void setDocCreationDate(String docCreationDate) {
		this.docCreationDate = docCreationDate;
	}

	public String getDocId() {
		return docId;
	}

	public void setDocId(String docId) {
		this.docId = docId;
	}

	public String getPersonDob() {
		return personDob;
	}

	public void setPersonDob(String personDob) {
		this.personDob = personDob;
	}

	public String getPersonGivenName() {
		return personGivenName;
	}

	public void setPersonGivenName(String personGivenName) {
		this.personGivenName = personGivenName;
	}

	public String getPersonMiddleName() {
		return personMiddleName;
	}

	public void setPersonMiddleName(String personMiddleName) {
		this.personMiddleName = personMiddleName;
	}

	public String getPersonSurName() {
		return personSurName;
	}

	public void setPersonSurName(String personSurName) {
		this.personSurName = personSurName;
	}

	public String getPersonSex() {
		return personSex;
	}

	public void setPersonSex(String personSex) {
		this.personSex = personSex;
	}

	public String getPersonRace() {
		return personRace;
	}

	public void setPersonRace(String personRace) {
		this.personRace = personRace;
	}

	public String getPersonSsn() {
		return personSsn;
	}

	public void setPersonSsn(String personSsn) {
		this.personSsn = personSsn;
	}

	public String getPersonStateId() {
		return personStateId;
	}

	public void setPersonStateId(String personStateId) {
		this.personStateId = personStateId;
	}

	public Date getBookingActivityDate() {
		return bookingActivityDate;
	}

	public void setBookingActivityDate(Date bookingActivityDate) {
		this.bookingActivityDate = bookingActivityDate;
	}

	public Integer getBookingNumber() {
		return bookingNumber;
	}

	public void setBookingNumber(Integer bookingNumber) {
		this.bookingNumber = bookingNumber;
	}

	public String getBookingSubjectId() {
		return bookingSubjectId;
	}

	public void setBookingSubjectId(String bookingSubjectId) {
		this.bookingSubjectId = bookingSubjectId;
	}

	public String getImageLocation() {
		return imageLocation;
	}

	public void setImageLocation(String imageLocation) {
		this.imageLocation = imageLocation;
	}

	public String getChargeCount() {
		return chargeCount;
	}

	public void setChargeCount(String chargeCount) {
		this.chargeCount = chargeCount;
	}

	public String getChargeDescription() {
		return chargeDescription;
	}

	public void setChargeDescription(String chargeDescription) {
		this.chargeDescription = chargeDescription;
	}

	public String getChargeStatuteCodeId() {
		return chargeStatuteCodeId;
	}

	public void setChargeStatuteCodeId(String chargeStatuteCodeId) {
		this.chargeStatuteCodeId = chargeStatuteCodeId;
	}

	public String getStatuteIdCategoryDescriptionTxt() {
		return statuteIdCategoryDescriptionTxt;
	}

	public void setStatuteIdCategoryDescriptionTxt(
			String statuteIdCategoryDescriptionTxt) {
		this.statuteIdCategoryDescriptionTxt = statuteIdCategoryDescriptionTxt;
	}

	public String getSourceSystemNameText() {
		return sourceSystemNameText;
	}

	public void setSourceSystemNameText(String sourceSystemNameText) {
		this.sourceSystemNameText = sourceSystemNameText;
	}

	public String getSystemId() {
		return systemId;
	}

	public void setSystemId(String systemId) {
		this.systemId = systemId;
	}

	public String getSystemName() {
		return systemName;
	}

	public void setSystemName(String systemName) {
		this.systemName = systemName;
	}

	public String getSearchResultCategoryDescriptionText() {
		return searchResultCategoryDescriptionText;
	}

	public void setSearchResultCategoryDescriptionText(
			String searchResultCategoryDescriptionText) {
		this.searchResultCategoryDescriptionText = searchResultCategoryDescriptionText;
	}

	public String getOrganizationName() {
		return organizationName;
	}

	public void setOrganizationName(String organizationName) {
		this.organizationName = organizationName;
	}

	public String getOrganizationBranchName() {
		return organizationBranchName;
	}

	public void setOrganizationBranchName(String organizationBranchName) {
		this.organizationBranchName = organizationBranchName;
	}

	public String getLastUpdatedDate() {
		return lastUpdatedDate;
	}

	public void setLastUpdatedDate(String lastUpdatedDate) {
		this.lastUpdatedDate = lastUpdatedDate;
	}

	@Override
	public String toString() {
		return "CustodyDetail [docCreationDate=" + docCreationDate + ", docId="
				+ docId + ", personDob=" + personDob + ", personGivenName="
				+ personGivenName + ", personMiddleName=" + personMiddleName
				+ ", personSurName=" + personSurName + ", personSex="
				+ personSex + ", personRace=" + personRace + ", personSsn="
				+ personSsn + ", personStateId=" + personStateId
				+ ", bookingActivityDate=" + bookingActivityDate
				+ ", bookingNumber=" + bookingNumber + ", bookingSubjectId="
				+ bookingSubjectId + ", imageLocation=" + imageLocation
				+ ", chargeCount=" + chargeCount + ", chargeDescription="
				+ chargeDescription + ", chargeStatuteCodeId="
				+ chargeStatuteCodeId + ", statuteIdCategoryDescriptionTxt="
				+ statuteIdCategoryDescriptionTxt + ", sourceSystemNameText="
				+ sourceSystemNameText + ", systemId=" + systemId
				+ ", systemName=" + systemName
				+ ", searchResultCategoryDescriptionText="
				+ searchResultCategoryDescriptionText + ", organizationName="
				+ organizationName + ", organizationBranchName="
				+ organizationBranchName + ", lastUpdatedDate="
				+ lastUpdatedDate + "]";
	}
	
}
