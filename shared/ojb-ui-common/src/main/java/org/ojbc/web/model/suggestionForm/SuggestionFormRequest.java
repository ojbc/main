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
package org.ojbc.web.model.suggestionForm;

import java.io.Serializable;

public class SuggestionFormRequest implements Serializable{

	private static final long serialVersionUID = -5768438625820046407L;
	
	private String userName;
	private String userEmail;
	private String userAgency;
	private String userPhone;
	private String suggestionProblem;
	private String urgency;
	private String userFeedback;
	
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	public String getUserEmail() {
		return userEmail;
	}
	public void setUserEmail(String userEmail) {
		this.userEmail = userEmail;
	}
	public String getUserAgency() {
		return userAgency;
	}
	public void setUserAgency(String userAgency) {
		this.userAgency = userAgency;
	}
	public String getUserPhone() {
		return userPhone;
	}
	public void setUserPhone(String userPhone) {
		this.userPhone = userPhone;
	}
	public String getSuggestionProblem() {
		return suggestionProblem;
	}
	public void setSuggestionProblem(String suggestionProblem) {
		this.suggestionProblem = suggestionProblem;
	}
	public String getUrgency() {
		return urgency;
	}
	public void setUrgency(String urgency) {
		this.urgency = urgency;
	}
	public String getUserFeedback() {
		return userFeedback;
	}
	public void setUserFeedback(String userFeedback) {
		this.userFeedback = userFeedback;
	}
	@Override
	public String toString() {
		return "SuggestionFormRequest [userName=" + userName + ", userEmail="
				+ userEmail + ", userAgency=" + userAgency + ", userPhone="
				+ userPhone + ", suggestionProblem=" + suggestionProblem
				+ ", urgency=" + urgency + ", userFeedback=" + userFeedback
				+ "]";
	}
	
	
}
