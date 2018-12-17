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
package org.ojbc.adapters.rapbackdatastore.processor.demographics;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import org.apache.camel.Exchange;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.ojbc.adapters.rapbackdatastore.dao.RapbackDAO;
import org.ojbc.adapters.rapbackdatastore.dao.model.CriminalHistoryDemographicsUpdateRequest;
import org.ojbc.adapters.rapbackdatastore.dao.model.IdentificationTransaction;
import org.ojbc.intermediaries.sn.dao.SubscriptionSearchQueryDAO;
import org.ojbc.util.model.rapback.Subscription;
import org.ojbc.util.xml.XmlUtils;
import org.springframework.stereotype.Service;
import org.w3c.dom.Document;

@Service
public class CriminalHistoryDemographicUpdateProcessor {

	private final Log log = LogFactory.getLog(this.getClass());
	
	private static final String PRE_DEMOGRAPHICS_XPATH = "/chdu-report-doc:CriminalHistoryDemographicsUpdateReport/chdu-report-ext:PreDemographicsUpdateDemographics";
	private static final String POST_DEMOGRAPHICS_XPATH = "/chdu-report-doc:CriminalHistoryDemographicsUpdateReport/chdu-report-ext:PostDemographicsUpdateDemographics";
	
    @Resource
    protected RapbackDAO rapbackDAO;
	
    @Resource
    private SubscriptionSearchQueryDAO subscriptionSearchQueryDAO;
    
	public void processCriminalHistoryDemographicsUpdate(Exchange ex, Document doc) throws Exception
	{
		CriminalHistoryDemographicsUpdateRequest criminalHistoryDemographicsUpdateRequest = returnCriminalHistoryDemographicsUpdateRequest(doc);
		
		List<IdentificationTransaction> identificationTransactions = null;
		
		List<Subscription> subscriptionsToUpdate = new ArrayList<Subscription>();
		
		if (StringUtils.isNotBlank(criminalHistoryDemographicsUpdateRequest.getPreUpdateOTN()) && StringUtils.isNotBlank(criminalHistoryDemographicsUpdateRequest.getPreUpdateCivilSID()))
		{
			identificationTransactions = rapbackDAO.returnMatchingCivilIdentifications(criminalHistoryDemographicsUpdateRequest.getPreUpdateOTN(), criminalHistoryDemographicsUpdateRequest.getPreUpdateCivilSID());	
		}	
		
		if (identificationTransactions != null && identificationTransactions.size() > 0)
		{
			for (IdentificationTransaction identificationTransaction : identificationTransactions)
			{
				rapbackDAO.updateCriminalHistoryDemographics(criminalHistoryDemographicsUpdateRequest, identificationTransaction.getSubject().getSubjectId());
				
				Integer subscriptionId = identificationTransaction.getSubscriptionId();
				
				if (subscriptionId != null)
				{
					List<Subscription> subscriptions = subscriptionSearchQueryDAO.queryForSubscription(String.valueOf(subscriptionId));
					
					Subscription subscriptionToUpdate = subscriptions.get(0);
					
					if ( 
						 StringUtils.isNotBlank(criminalHistoryDemographicsUpdateRequest.getPostUpdateGivenName()) ||
						 StringUtils.isNotBlank(criminalHistoryDemographicsUpdateRequest.getPostUpdateMiddleName()) ||
						 StringUtils.isNotBlank(criminalHistoryDemographicsUpdateRequest.getPostUpdateSurName())		 
						)
					{	
						StringBuffer personFullName = new StringBuffer();
						
						if (StringUtils.isNotBlank(criminalHistoryDemographicsUpdateRequest.getPostUpdateGivenName()))
						{
							subscriptionToUpdate.setPersonFirstName(criminalHistoryDemographicsUpdateRequest.getPostUpdateGivenName());
							personFullName.append(criminalHistoryDemographicsUpdateRequest.getPostUpdateGivenName());
							personFullName.append(" ");
						}	

						if (StringUtils.isNotBlank(criminalHistoryDemographicsUpdateRequest.getPostUpdateMiddleName()))
						{
							personFullName.append(criminalHistoryDemographicsUpdateRequest.getPostUpdateMiddleName());
							personFullName.append(" ");
						}	

						if (StringUtils.isNotBlank(criminalHistoryDemographicsUpdateRequest.getPostUpdateSurName()))
						{
							subscriptionToUpdate.setPersonLastName(criminalHistoryDemographicsUpdateRequest.getPostUpdateSurName());
							personFullName.append(criminalHistoryDemographicsUpdateRequest.getPostUpdateSurName());
						}	

						subscriptionToUpdate.setPersonFullName(personFullName.toString());
					}
					
					if (criminalHistoryDemographicsUpdateRequest.getPostUpdateDOB() != null)
					{
						subscriptionToUpdate.setDateOfBirth(criminalHistoryDemographicsUpdateRequest.getPostUpdateDOB().toString());
					}	
					
					subscriptionsToUpdate.add(subscriptionToUpdate);
				}	
			}	
		}	
		
		ex.getIn().setHeader("subscriptionsToModify", subscriptionsToUpdate);
	}

	CriminalHistoryDemographicsUpdateRequest returnCriminalHistoryDemographicsUpdateRequest(Document doc)
			throws Exception {
		CriminalHistoryDemographicsUpdateRequest criminalHistoryDemographicsUpdateRequest = new CriminalHistoryDemographicsUpdateRequest();
		
		String postUpdateDobString = XmlUtils.xPathStringSearch(doc, POST_DEMOGRAPHICS_XPATH + "/nc30:Person/nc30:PersonBirthDate/nc30:Date");
		
		if (StringUtils.isNotBlank(postUpdateDobString))
		{
			try {
				LocalDate postUpdateDob = LocalDate.parse(postUpdateDobString);
				criminalHistoryDemographicsUpdateRequest.setPostUpdateDOB(postUpdateDob);
			} catch (Exception e) {
				log.error("Unable to process post update DOB: " + postUpdateDobString);
			}
		}		
		
		String postUpdateGivenName = XmlUtils.xPathStringSearch(doc, POST_DEMOGRAPHICS_XPATH + "/nc30:Person/nc30:PersonName/nc30:PersonGivenName");
		criminalHistoryDemographicsUpdateRequest.setPostUpdateGivenName(postUpdateGivenName);
		
		String postUpdateMiddleName = XmlUtils.xPathStringSearch(doc, POST_DEMOGRAPHICS_XPATH + "/nc30:Person/nc30:PersonName/nc30:PersonMiddleName");
		criminalHistoryDemographicsUpdateRequest.setPostUpdateMiddleName(postUpdateMiddleName);
		
		String postUpdateSurName = XmlUtils.xPathStringSearch(doc, POST_DEMOGRAPHICS_XPATH + "/nc30:Person/nc30:PersonName/nc30:PersonSurName");
		criminalHistoryDemographicsUpdateRequest.setPostUpdateSurName(postUpdateSurName);
		
		String preUpdateCivilSID = XmlUtils.xPathStringSearch(doc, PRE_DEMOGRAPHICS_XPATH + "/nc30:Person/jxdm50:PersonAugmentation/jxdm50:PersonStateFingerprintIdentification/nc30:IdentificationID");
		criminalHistoryDemographicsUpdateRequest.setPreUpdateCivilSID(preUpdateCivilSID);
		
		String postUpdateCivilSID = XmlUtils.xPathStringSearch(doc, POST_DEMOGRAPHICS_XPATH + "/nc30:Person/jxdm50:PersonAugmentation/jxdm50:PersonStateFingerprintIdentification/nc30:IdentificationID");
		criminalHistoryDemographicsUpdateRequest.setPostUpdateCivilSID(postUpdateCivilSID);
		
		String preUpdateOTN = XmlUtils.xPathStringSearch(doc, PRE_DEMOGRAPHICS_XPATH + "/nc30:Person/chdu-report-ext:PersonTrackingIdentification/nc30:IdentificationID");
		criminalHistoryDemographicsUpdateRequest.setPreUpdateOTN(preUpdateOTN);
		
		String postUpdateOTN = XmlUtils.xPathStringSearch(doc, POST_DEMOGRAPHICS_XPATH + "/nc30:Person/chdu-report-ext:PersonTrackingIdentification/nc30:IdentificationID");
		criminalHistoryDemographicsUpdateRequest.setPostUpdateOTN(postUpdateOTN);
		
		return criminalHistoryDemographicsUpdateRequest;
	}
	
}
