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
package org.ojbc.adapters.rapbackdatastore.dao;

import java.util.List;

import org.ojbc.adapters.rapbackdatastore.dao.model.AgencyProfile;
import org.ojbc.adapters.rapbackdatastore.dao.model.CivilFbiSubscriptionRecord;
import org.ojbc.adapters.rapbackdatastore.dao.model.CivilFingerPrints;
import org.ojbc.adapters.rapbackdatastore.dao.model.CivilInitialRapSheet;
import org.ojbc.adapters.rapbackdatastore.dao.model.CivilInitialResults;
import org.ojbc.adapters.rapbackdatastore.dao.model.CriminalFbiSubscriptionRecord;
import org.ojbc.adapters.rapbackdatastore.dao.model.CriminalInitialResults;
import org.ojbc.adapters.rapbackdatastore.dao.model.IdentificationTransaction;
import org.ojbc.adapters.rapbackdatastore.dao.model.Subject;
import org.ojbc.intermediaries.sn.dao.rapback.FbiRapbackSubscription;
import org.ojbc.intermediaries.sn.dao.rapback.ResultSender;
import org.ojbc.intermediaries.sn.dao.rapback.SubsequentResults;


public interface RapbackDAO {
	
	public Integer saveSubject(final Subject subject);
	public void saveIdentificationTransaction(IdentificationTransaction identificationTransaction);
	public Integer saveCivilFbiSubscriptionRecord(final CivilFbiSubscriptionRecord civilFbiSubscriptionRecord);
	public Integer saveCriminalFbiSubscriptionRecord(final CriminalFbiSubscriptionRecord criminalFbiSubscriptionRecord);
	public Integer saveCivilFingerPrints(final CivilFingerPrints civilFingerPrints);
	//TODO remove this method when we are 100% certain the table is not needed. 
//	public Integer saveCriminalFingerPrints(final CriminalFingerPrints criminalFingerPrints);
	public Integer saveCivilInitialRapSheet(final CivilInitialRapSheet civilInitialRapSheet);
	public Integer saveCivilInitialResults(final CivilInitialResults civilInitialResults);
	public Integer getCivilIntialResultsId(String transactionNumber, ResultSender resultSender);
	public Integer saveCriminalInitialResults(final CriminalInitialResults criminalInitialResults);
	public void saveFbiRapbackSubscription(final FbiRapbackSubscription fbiRapbackSubscription);
	
	public Subject getSubject(Integer id);
	public IdentificationTransaction getIdentificationTransaction(String transactionNumber);
	public List<CivilInitialResults> getCivilInitialResults(String ori);
	public List<CivilInitialResults> getIdentificationCivilInitialResults(String transactionNumber);
	public List<CriminalInitialResults> getIdentificationCriminalInitialResults(String transactionNumber);
	public List<IdentificationTransaction> getCivilIdentificationTransactions(String ori);
	public List<IdentificationTransaction> getCriminalIdentificationTransactions(String ori);
	public String getIdentificationCategory(String transactionNumber); 
	
	public void updateSubject(Subject subject);
	public void updateFbiRapbackSubscription(
			FbiRapbackSubscription fbiRapbackSubscription);
	
	public void consolidateSid(String currentSid, String newSid);
	public void consolidateUcn(String currentUcn, String newUcn);
	
	public AgencyProfile getAgencyProfile(String ori);
	
	public int archiveCivilIdentifications();
	public int archiveCriminalIdentifications();
	public int archiveIdentificationResult(String transactionNumber);
	
	public List<SubsequentResults> getSubsequentResults(String transactionNumber);
	public List<SubsequentResults> getSubsequentResultsByUcn(String ucn);
}
