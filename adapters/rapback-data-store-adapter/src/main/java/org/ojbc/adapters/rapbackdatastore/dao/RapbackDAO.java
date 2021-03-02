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
package org.ojbc.adapters.rapbackdatastore.dao;

import java.util.List;

import org.apache.wss4j.common.principal.SAMLTokenPrincipal;
import org.ojbc.adapters.rapbackdatastore.dao.model.AgencyProfile;
import org.ojbc.adapters.rapbackdatastore.dao.model.CivilFingerPrints;
import org.ojbc.adapters.rapbackdatastore.dao.model.CivilInitialRapSheet;
import org.ojbc.adapters.rapbackdatastore.dao.model.CivilInitialResults;
import org.ojbc.adapters.rapbackdatastore.dao.model.CriminalHistoryDemographicsUpdateRequest;
import org.ojbc.adapters.rapbackdatastore.dao.model.CriminalInitialResults;
import org.ojbc.adapters.rapbackdatastore.dao.model.FingerPrintsType;
import org.ojbc.adapters.rapbackdatastore.dao.model.IdentificationTransaction;
import org.ojbc.adapters.rapbackdatastore.dao.model.NsorDemographics;
import org.ojbc.adapters.rapbackdatastore.dao.model.NsorFiveYearCheck;
import org.ojbc.adapters.rapbackdatastore.dao.model.NsorSearchResult;
import org.ojbc.adapters.rapbackdatastore.dao.model.Subject;
import org.ojbc.intermediaries.sn.dao.rapback.ResultSender;
import org.ojbc.intermediaries.sn.dao.rapback.SubsequentResults;
import org.ojbc.util.model.rapback.FbiRapbackSubscription;
import org.ojbc.util.model.rapback.IdentificationResultSearchRequest;


public interface RapbackDAO {
	
	public Integer updateCriminalHistoryDemographics(CriminalHistoryDemographicsUpdateRequest criminalHistoryDemographicsUpdateRequest, Integer subjectId);
	public List<IdentificationTransaction> returnMatchingCivilIdentifications(String otn, String civilSid);
	public Integer saveSubject(final Subject subject);
	public void saveIdentificationTransaction(IdentificationTransaction identificationTransaction);
	public void updateIdentificationTransaction(IdentificationTransaction identificationTransaction);
	public Integer saveCivilFingerPrints(final CivilFingerPrints civilFingerPrints);
	public Integer deleteCivilFingerPrints(String transactionNumber, FingerPrintsType fingerPrintsType);
	public Integer saveCivilInitialRapSheet(final CivilInitialRapSheet civilInitialRapSheet);
	public Integer saveCivilInitialResults(final CivilInitialResults civilInitialResults);
	public Integer deleteCivilInitialResults(String transactionNumber, ResultSender resultSender);
	public Integer getCivilIntialResultsId(String transactionNumber, ResultSender resultSender);
	public List<CivilInitialResults> getCivilInitialResults(String transactionNumber, ResultSender resultSender);
	public Integer saveCriminalInitialResults(final CriminalInitialResults criminalInitialResults);
	public Integer deleteCriminalInitialResults(String transactionNumber, ResultSender resultSender);
	public Integer saveNsorDemographics(final NsorDemographics nsorDemographics);
	public Integer saveNsorSearchResult(final NsorSearchResult nsorSearchResult);
	public Integer deleteNsorDemographics(String transactionNumber, ResultSender resultSender);
	public Integer deleteNsorSearchResult(String transactionNumber, ResultSender resultSender);
	public void saveFbiRapbackSubscription(final FbiRapbackSubscription fbiRapbackSubscription);
	
	public Subject getSubject(Integer id);
	public IdentificationTransaction getIdentificationTransaction(String transactionNumber);
	public List<CivilInitialResults> getCivilInitialResults(String ori);
	public List<CivilInitialResults> getIdentificationCivilInitialResults(String transactionNumber);
	public List<CriminalInitialResults> getIdentificationCriminalInitialResults(String transactionNumber);
	public List<IdentificationTransaction> getCivilIdentificationTransactions(SAMLTokenPrincipal token, 
			IdentificationResultSearchRequest searchRequest);
	public List<IdentificationTransaction> getCriminalIdentificationTransactions(SAMLTokenPrincipal token,
			IdentificationResultSearchRequest searchRequest);
	public String getIdentificationCategoryType(String transactionNumber);
	
	public List<NsorDemographics> getNsorDemographics(String transactionNumber);
	public List<NsorSearchResult> getNsorSearchResults(String transactionNumber);
	
	public Boolean isExistingTransactionNumber(String transactionNumber);
	public Boolean isExistingNsorTransaction(String transactionNumber);
	public void updateIdentificationCategory(String transactionNumber, String identificationCategory);
	
	public void updateSubject(Subject subject);
	public void updateFbiRapbackSubscription(
			FbiRapbackSubscription fbiRapbackSubscription);
	
	public void consolidateSidFederal(String currentSid, String newSid);
	public void consolidateUcnFederal(String currentUcn, String newUcn);

	public AgencyProfile getAgencyProfile(String ori);
	public List<AgencyProfile> getAgencyProfiles(List<String> oris);
	
	public int archiveCivilIdentifications();
	public int archiveCriminalIdentifications();
	public int archiveIdentificationResult(String transactionNumber);
	public int unarchiveIdentificationResult(String transactionNumber);
	
	public List<NsorFiveYearCheck> getNsorFiveYearChecks(String transactionNumber);
	public List<SubsequentResults> getSubsequentResults(String transactionNumber);
	public List<SubsequentResults> getSubsequentResultsByUcn(String ucn);
	
	public List<String> getViewableIdentificationCategories(
			SAMLTokenPrincipal token, String identificationCategoryType);

    public void updateFbiSubscriptionStatus(Integer subscriptionId, String status);
}
