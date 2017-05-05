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
package org.ojbc.web.model.identificationresult.search;

import java.util.List;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

public class IdentificationResultsQueryResponse {

	private String stateSearchResultFile; 
	private List<String> stateCriminalHistoryRecordDocuments;
	private String fbiSearchResultFile; 
	private List<String> fbiIdentityHistorySummaryDocuments;
	public String getStateSearchResultFile() {
		return stateSearchResultFile;
	}
	public void setStateSearchResultFile(String stateSearchResultFile) {
		this.stateSearchResultFile = stateSearchResultFile;
	}
	public List<String> getStateCriminalHistoryRecordDocuments() {
		return stateCriminalHistoryRecordDocuments;
	}
	public void setStateCriminalHistoryRecordDocuments(
			List<String> stateCriminalHistoryRecordDocuments) {
		this.stateCriminalHistoryRecordDocuments = stateCriminalHistoryRecordDocuments;
	}
	public String getFbiSearchResultFile() {
		return fbiSearchResultFile;
	}
	public void setFbiSearchResultFile(String fbiSearchResultFile) {
		this.fbiSearchResultFile = fbiSearchResultFile;
	}
	public List<String> getFbiIdentityHistorySummaryDocuments() {
		return fbiIdentityHistorySummaryDocuments;
	}
	public void setFbiIdentityHistorySummaryDocuments(
			List<String> fbiIdentityHistorySummaryDocuments) {
		this.fbiIdentityHistorySummaryDocuments = fbiIdentityHistorySummaryDocuments;
	} 
	
	public String toString(){
		return ToStringBuilder.reflectionToString(this, ToStringStyle.MULTI_LINE_STYLE);
	}
	
}
