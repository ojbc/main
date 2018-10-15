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
package org.ojbc.web.model.person.query;

import java.io.Serializable;
import java.lang.reflect.Field;
import java.util.Objects;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.ojbc.web.OJBCWebServiceURIs;

public class DetailsRequest implements Serializable{

	private static final long serialVersionUID = -8308338659183972879L;
    private String identificationID;
	private String identificationSourceText;
	private String queryType;
	private String activeAccordionId; 
	private Boolean civilPurposeRequest; 
	private Boolean textRapsheetRequest; 
	private Boolean admin; 
	
	private String fbiId; 
	private String rapbackSubscriptionId; 
	private String rapbackActivityNotificationId; 

	//Logging
	private String purpose;
	private String onBehalfOf;
	
    public DetailsRequest() {
		super();
	}

	public DetailsRequest(String identificationID, Boolean admin) {
		this();
		this.identificationID = identificationID;
		this.admin = admin;
	}
    
	public String getIdentificationID() {
		return identificationID;
	}
	public void setIdentificationID(String identificationID) {
		this.identificationID = identificationID;
	}
	public String getIdentificationSourceText() {
		return identificationSourceText;
	}
	public void setIdentificationSourceText(String identificationSourceText) {
		this.identificationSourceText = identificationSourceText;
	}

	public String toString(){
		return ToStringBuilder.reflectionToString(this);
	}
	
	public String getPurpose() {
		return purpose;
	}
	public void setPurpose(String purpose) {
		this.purpose = purpose;
	}
	public String getOnBehalfOf() {
		return onBehalfOf;
	}
	public void setOnBehalfOf(String onBehalfOf) {
		this.onBehalfOf = onBehalfOf;
	}
	public String getQueryType() {
		return queryType;
	}
	public void setQueryType(String queryType) {
		this.queryType = queryType;
	}

    public boolean isJuvenileDetailRequest() {
        return OJBCWebServiceURIs.JUVENILE_HISTORY.equals(identificationSourceText) || 
                OJBCWebServiceURIs.JUVENILE_HISTORY_SEARCH.equals(identificationSourceText);
    }
    public String getActiveAccordionId() {
        return activeAccordionId;
    }
    public void setActiveAccordionId(String activeAccordionId) {
        this.activeAccordionId = activeAccordionId;
    }
	public Boolean getAdmin() {
		return admin;
	}
	public void setAdmin(Boolean admin) {
		this.admin = admin;
	}

	public Boolean getCivilPurposeRequest() {
		return civilPurposeRequest;
	}

	public void setCivilPurposeRequest(Boolean civilPurposeRequest) {
		this.civilPurposeRequest = civilPurposeRequest;
	}

	public Boolean getTextRapsheetRequest() {
		return textRapsheetRequest;
	}

	public void setTextRapsheetRequest(Boolean textRapsheetRequest) {
		this.textRapsheetRequest = textRapsheetRequest;
	}

	public String getFbiId() {
		return fbiId;
	}

	public void setFbiId(String fbiId) {
		this.fbiId = fbiId;
	}

	public String getRapbackSubscriptionId() {
		return rapbackSubscriptionId;
	}

	public void setRapbackSubscriptionId(String rapbackSubscriptionId) {
		this.rapbackSubscriptionId = rapbackSubscriptionId;
	}

	public String getRapbackActivityNotificationId() {
		return rapbackActivityNotificationId;
	}

	public void setRapbackActivityNotificationId(
			String rapbackActivityNotificationId) {
		this.rapbackActivityNotificationId = rapbackActivityNotificationId;
	}

}
