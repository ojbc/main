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
package org.ojbc.audit.enhanced.dao;

import org.ojbc.audit.enhanced.dao.model.FederalRapbackSubscription;
import org.ojbc.audit.enhanced.dao.model.FirearmsQueryResponse;
import org.ojbc.audit.enhanced.dao.model.IdentificationSearchRequest;
import org.ojbc.audit.enhanced.dao.model.IdentificationSearchResult;
import org.ojbc.audit.enhanced.dao.model.PersonQueryCriminalHistoryResponse;
import org.ojbc.audit.enhanced.dao.model.PersonQueryWarrantResponse;
import org.ojbc.audit.enhanced.dao.model.PersonSearchRequest;
import org.ojbc.audit.enhanced.dao.model.PersonSearchResult;
import org.ojbc.audit.enhanced.dao.model.QueryRequest;
import org.ojbc.audit.enhanced.dao.model.UserInfo;


public interface EnhancedAuditDAO {

	public Integer saveFederalRapbackSubscription(FederalRapbackSubscription federalRapbackSubscription);
	
	public void updateFederalRapbackSubscriptionWithResponse(FederalRapbackSubscription federalRapbackSubscription) throws Exception;
	
	public FederalRapbackSubscription retrieveFederalRapbackSubscriptionFromTCN(String transactionControlNumber);
	
	public Integer savePersonSearchRequest(PersonSearchRequest personSearchRequest);
	
	public Integer saveIdentificationSearchRequest(IdentificationSearchRequest identificationSearchRequest);
	
	public Integer saveidentificationSearchResponse(IdentificationSearchResult identificationSearchResult);
	
	public Integer saveQueryRequest(QueryRequest queryRequest);
	
	public Integer savePersonQueryCriminalHistoryResponse(PersonQueryCriminalHistoryResponse personQueryCriminalHistoryResponse);
	
	public Integer saveFirearmsQueryResponse(FirearmsQueryResponse firearmsQueryResponse);
	
	public Integer savePersonQueryWarrantResponse(PersonQueryWarrantResponse personQueryWarrantResponse);
	
	public Integer savePersonSearchResult(PersonSearchResult personSearchResult);
	
	public Integer saveUserInfo(UserInfo userInfo);
	
	public UserInfo retrieveUserInfoFromId(Integer userInfoPk);
	
	public Integer retrieveSystemToSearchIDFromURI(String uri);
	
	public Integer retrieveSystemToSearchIDFromSystemName(String systemName);
	
	public Integer retrieveSearchQualifierCodeIDfromCodeName(String codeName);
	
	public Integer savePersonSystemToSearch(Integer pearchSearchPk, Integer systemsToSearchPk);

	public Integer retrieveIdentificationReasonCodeFromDescription(String description);
	
	public Integer saveIdentificationReasonCode(Integer identificationSearchReasonCodeId, Integer identificationSearchRequestId);

	public Integer retrievePersonSearchIDfromMessageID(String messageId);
	
	public Integer retrievePersonQueryIDfromMessageID(String messageId);
	
	public Integer retrieveOrganizationIdentificationIDfromMessageID(String messageId);
	
}
