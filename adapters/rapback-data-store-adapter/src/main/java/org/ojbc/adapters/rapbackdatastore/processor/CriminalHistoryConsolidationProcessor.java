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
package org.ojbc.adapters.rapbackdatastore.processor;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.camel.Body;
import org.apache.camel.Exchange;
import org.apache.camel.Header;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.ojbc.adapters.rapbackdatastore.dao.RapbackDAO;
import org.ojbc.intermediaries.sn.SubscriptionNotificationConstants;
import org.ojbc.intermediaries.sn.dao.Subscription;
import org.ojbc.intermediaries.sn.dao.SubscriptionSearchQueryDAO;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class CriminalHistoryConsolidationProcessor {

    private static final Log log = LogFactory.getLog(CriminalHistoryConsolidationProcessor.class);

    @Resource
    private RapbackDAO rapbackDAO;

    @Resource
    private SubscriptionSearchQueryDAO subscriptionSearchQueryDAO;

    @Value("${rapbackDatastoreAdapter.federalSubscriptions:true}")
    private boolean federalSubscriptionsEnabled;

    private final static String EXPUNGEMENT_EMAIL_TEMPLATE ="<Old SID> has been deleted from CJIS-Hawaii and the State AFIS; you will no longer receive Rap Back notifications on this offender.  Please logon to the HIJIS Portal to update your subscription as necessary.";
    private final static String CONSOLIDATION_EMAIL_TEMPLATE ="<Old SID> has been consolidated into <New SID>.  Our records show you have an active Rap Back subscription to one of these SIDs.  Please logon to the HIJIS portal to verify your subscription.  For the updated criminal history record information, logon to CJIS-Hawaii to run a query on <New SID>.  A new arrest may or may not have occurred.";
    private final static String IDENTIFIER_UPDATE_EMAIL_TEMPLATE ="<Old SID> has been updated to <New SID>.  Our records show you have an active Rap Back subscription to one of these SIDs.  Please logon to the HIJIS portal to verify your subscription.  For the updated criminal history record information, logon to CJIS-Hawaii to run a query on <New SID>.  A new arrest may or may not have occurred.";
    private final static String USER_FEDERAL_SUB_WITH_UCN_EMAIL_TEMPLATE="SID: <SID> \n UCN: <UCN>\n\nThe UCN associated to this SID has been updated.  Our records show you have an active State Rap Back subscription associated with this offender.  A federal Rap Back subscription was automatically created on your behalf.  Please log onto the HIJIS portal to verify or update your subscription as necessary.  For the updated criminal history record information, logon to OpenFox/NCIC to run a query on this UCN.";
    private final static String HCJDC_FEDERAL_SUB_WITH_UCN_EMAIL_TEMPLATE="SID: <SID> \n UCN: <UCN>\n\nThe UCN associated to this SID has been updated.  Federal subscription added for user.";
    private final static String HCJDC_UCN_MISMATCH_EMAIL_TEMPLATE="SID: <SID> \n UCN: <UCN>\n\nUCN mismatch.";
    
    
    /**
     * Main behavior method, invoked from the Camel route, to replace the current SID with the new SID.
     * and replace the current UCN to the new UCN. 
     * @param currentSid
     * @param newSid
     * @param currentUcn
     * @param newUcn
     * @throws Exception
     */
    @Transactional
    public List<CriminalHistoryConsolidationNotification> consolidateCriminalHistory(Exchange exch, @Header("currentSid") String currentSid, 
    		@Header("newSid") String newSid, 
    		@Header("currentUcn") String currentUcn, 
    		@Header("newUcn") String newUcn,
    		@Header("consolidationType") String consolidationType
    		) throws Exception {
    	
    	log.info("Current SID: "  + currentSid + ", new SID: " + newSid);
    	log.info("Current UCN: "  + currentUcn + ", new UCN: " + newUcn);
    	
    	if (federalSubscriptionsEnabled)
    	{	
    		log.info("Federal subscriptions enabled.  Consolidate SID and UCN.");
    		
	    	//Consolidate SID and UCN for members who use rapback
	    	if (StringUtils.isNotBlank(currentSid) && StringUtils.isNotBlank(newSid)){
	    		log.info("Consolidating SID " + currentSid + " with " + newSid);
	    		rapbackDAO.consolidateSidFederal(currentSid, newSid);
	    	}
	    	
	    	if (StringUtils.isNotBlank(currentUcn) && StringUtils.isNotBlank(newUcn)){
	    		log.info("Consolidating UCN " + currentUcn + " with " + newUcn);
	    		rapbackDAO.consolidateUcnFederal(currentUcn, newUcn);
	    	}
    	}	
    	
    	Map<String,String> subjectIdentifiers = Collections.singletonMap(SubscriptionNotificationConstants.SID, currentSid);
    	
    	//Search for active subscriptions with matching SIDs
    	List<Subscription> subscriptionsMatchingSID = subscriptionSearchQueryDAO.queryForSubscription(null, null, null, subjectIdentifiers);

    	List<Subscription> subscriptionsWithUCNMismatches = new ArrayList<Subscription>();
    	List<Subscription> subscriptionsWithUCNAdded = new ArrayList<Subscription>();
    			
    	if (StringUtils.isNotEmpty(newUcn))
    	{
    		for (Subscription subscriptionMatchingSID : subscriptionsMatchingSID)
    		{
    			//Since we have a SID, we know Subject Identifiers are not null, no need to check
    			Map<String,String> subscriptionSubjectIdentifiers = subscriptionMatchingSID.getSubscriptionSubjectIdentifiers();
    			
				String ucnInSubscription = subscriptionSubjectIdentifiers.get(SubscriptionNotificationConstants.FBI_ID);

				if (StringUtils.isNotEmpty(ucnInSubscription))
				{
    				if (!newUcn.equals(ucnInSubscription))
    				{
    					//We don't have a match notify agency
    					subscriptionsWithUCNMismatches.add(subscriptionMatchingSID);
    				}
    				else
    				{
    					log.info("UCN in consolidation message: "  + newUcn + " matches UCN in subscription" + ucnInSubscription);
    				}	
				}	
				else
				{
					subscriptionMatchingSID.getSubscriptionSubjectIdentifiers().put(SubscriptionNotificationConstants.FBI_ID, "newUcn");
					//Update subscription in database here
					subscriptionSearchQueryDAO.insertSubjectIdentifier(subscriptionMatchingSID.getId(), SubscriptionNotificationConstants.FBI_ID, newUcn);
					subscriptionsWithUCNAdded.add(subscriptionMatchingSID);
				}	
    		}	
    	}
    	
    	List<CriminalHistoryConsolidationNotification> chcNotificationUcnMismatches = returnCriminalHistoryConsolidations(
				currentSid, newSid, currentUcn, newUcn, "reportUCNMismatches", subscriptionsWithUCNAdded);
    	
    	exch.getIn().setHeader("subscriptionsToModify", subscriptionsMatchingSID);
    	
    	//Results in an email to the agency
    	exch.getIn().setHeader("chcNotificationUcnMismatches", chcNotificationUcnMismatches);
    	
    	//Results in a new federal subscription for user and email
    	exch.getIn().setHeader("subscriptionsWithUCNAdded", subscriptionsWithUCNMismatches);
    	
    	List<CriminalHistoryConsolidationNotification> criminalHistoryConsolidationNotifications = returnCriminalHistoryConsolidations(
				currentSid, newSid, currentUcn, newUcn, consolidationType, subscriptionsMatchingSID);

    	return returnUniqueEmailNotifications(criminalHistoryConsolidationNotifications);
    	
    }

    /**
     * Main behavior method, invoked from the Camel route, to replace the current SID with the new SID.
     * and replace the current UCN to the new UCN. 
     * @param currentSid
     * @param newSid
     * @param currentUcn
     * @param newUcn
     * @throws Exception
     */
    public List<CriminalHistoryConsolidationNotification> expungeCriminalHistory(@Header("currentSid") String currentSid, 
    		@Header("newSid") String newSid, 
    		@Header("currentUcn") String currentUcn, 
    		@Header("newUcn") String newUcn,
    		@Header("consolidationType") String consolidationType) throws Exception {
    	
    	log.info("Current SID: "  + currentSid + ", new SID: " + newSid);
    	log.info("Current UCN: "  + currentUcn + ", new UCN: " + newUcn);
    	
    	List<CriminalHistoryConsolidationNotification> criminalHistoryConsolidationNotifications = returnCriminalHistoryConsolidations(
				currentSid, newSid, currentUcn, newUcn, consolidationType, null);

    	return returnUniqueEmailNotifications(criminalHistoryConsolidationNotifications);
    	
    }
    
	List<CriminalHistoryConsolidationNotification> returnCriminalHistoryConsolidations(
			String currentSid, String newSid, String currentUcn, String newUcn, String consolidationType, List<Subscription> subscriptionsMatchingSID) {
		//Consolidate SID and optionally UCN for members who use simple subscription database setup
    	//Query based on SID only
    	
    	Map<String,String> subjectIdentifiers = Collections.singletonMap(SubscriptionNotificationConstants.SID, currentSid);
    	
    	//Search for active subscriptions with matching SIDs only if null
    	if (subscriptionsMatchingSID == null)
    	{	
    		subscriptionsMatchingSID = subscriptionSearchQueryDAO.queryForSubscription(null, null, null, subjectIdentifiers);
    	}	
    	
    	List<CriminalHistoryConsolidationNotification> criminalHistoryConsolidationNotifications = new ArrayList<CriminalHistoryConsolidationNotification>();
    	
    	for (Subscription subscription : subscriptionsMatchingSID)
    	{
    		log.info("Subscriptions to consolidate by SID: " + subscription.getId());
    		
    		if (consolidationType.equals("criminalHistoryConsolidationReport") || consolidationType.equals("criminalHistoryIdentifierUpdateReport"))
    		{
        		//Update SID in subject identifier table
        		subscriptionSearchQueryDAO.updateSubscriptionSubjectIdentifier(currentSid, newSid, subscription.getSubscriptionIdentifier(), SubscriptionNotificationConstants.SID);
        		subscriptionSearchQueryDAO.updateSubscriptionSubjectIdentifier(currentUcn, newUcn, subscription.getSubscriptionIdentifier(), SubscriptionNotificationConstants.FBI_ID);
    			
    		}	
    		
    		CriminalHistoryConsolidationNotification chcNotification  = new CriminalHistoryConsolidationNotification();
    		
    		chcNotification.setSubscription(subscription);
    		chcNotification.setConsolidationType(consolidationType);
    		chcNotification.setEmailTo(subscription.getSubscriptionOwnerEmailAddress());
    		chcNotification.setEmailBody(returnEmailBody(chcNotification, currentSid, newSid));
    		chcNotification.setEmailSubject(returnEmailSubject(chcNotification, currentSid));
    		
    		criminalHistoryConsolidationNotifications.add(chcNotification);
    		
    	}
    	
		return criminalHistoryConsolidationNotifications;
	}    
    
	/**
	 * This method will make sure we only have unique notifications by creating a map using the 'to' address 
	 * and then converting that back to a list
	 * 
	 * @param criminalHistoryConsolidationNotifications
	 * @return
	 */
    List<CriminalHistoryConsolidationNotification> returnUniqueEmailNotifications(List<CriminalHistoryConsolidationNotification> criminalHistoryConsolidationNotifications)
    {
    	Map<String, CriminalHistoryConsolidationNotification> emailNotifications = new HashMap<String, CriminalHistoryConsolidationNotification>();
    	
    	for (CriminalHistoryConsolidationNotification chcNotification : criminalHistoryConsolidationNotifications)
    	{
    		emailNotifications.put(chcNotification.getEmailTo(), chcNotification);
    	}	
    	
    	criminalHistoryConsolidationNotifications.clear();
    	
    	// Iterating over keys only
    	for (CriminalHistoryConsolidationNotification value : emailNotifications.values()) {
    		criminalHistoryConsolidationNotifications.add(value);
    	}
    	
    	return criminalHistoryConsolidationNotifications;
    	
    }

	String returnEmailBody(
			CriminalHistoryConsolidationNotification chcNotification, String currentSid, String newSid) {
		
		String emailBody = "";
		
		if (chcNotification.getConsolidationType().equals("criminalHistoryExpungementReport"))
		{
			emailBody = StringUtils.replace(EXPUNGEMENT_EMAIL_TEMPLATE, "<Old SID>", currentSid);
		}	

		if (chcNotification.getConsolidationType().equals("criminalHistoryConsolidationReport"))
		{
			emailBody = StringUtils.replace(CONSOLIDATION_EMAIL_TEMPLATE, "<Old SID>", currentSid);
			emailBody = StringUtils.replace(emailBody, "<New SID>", newSid);
		}	

		if (chcNotification.getConsolidationType().equals("criminalHistoryIdentifierUpdateReport"))
		{
			emailBody = StringUtils.replace(IDENTIFIER_UPDATE_EMAIL_TEMPLATE, "<Old SID>", currentSid);
			emailBody = StringUtils.replace(emailBody, "<New SID>", newSid);
		}	

		if (chcNotification.getConsolidationType().equals("reportUCNMismatches"))
		{
			emailBody = StringUtils.replace(HCJDC_UCN_MISMATCH_EMAIL_TEMPLATE, "<Old SID>", currentSid);
		}	
			
		return emailBody;
	}

	String returnEmailSubject(
			CriminalHistoryConsolidationNotification chcNotification, String currentSid) {
		
		String emailSubject = "";
		
		if (chcNotification.getConsolidationType().equals("criminalHistoryExpungementReport"))
		{
			emailSubject = "SID Expungement for: " + currentSid;
		}	

		if (chcNotification.getConsolidationType().equals("criminalHistoryConsolidationReport"))
		{
			emailSubject = "Criminal History Consolidation for SID for: " + currentSid;
		}	

		if (chcNotification.getConsolidationType().equals("criminalHistoryIdentifierUpdateReport"))
		{
			emailSubject = "Criminal History Update for SID for: " + currentSid;
		}	

		if (chcNotification.getConsolidationType().equals("reportUCNMismatches"))
		{
			emailSubject = "UCN mismatch report for SID for: " + currentSid;
		}	

		return emailSubject;
	}

	public void returnCamelEmail(Exchange ex)
	{	
		CriminalHistoryConsolidationNotification chcNotification = (CriminalHistoryConsolidationNotification) ex.getIn().getBody();

		ex.getIn().removeHeaders("*");
		
		ex.getIn().setHeader("to", chcNotification.getEmailTo());
		ex.getIn().setHeader("subject", chcNotification.getEmailSubject());
		
		ex.getIn().setBody(chcNotification.getEmailBody());
		
	}
	
	public boolean isFederalSubscriptionsEnabled() {
		return federalSubscriptionsEnabled;
	}

	public void setFederalSubscriptionsEnabled(boolean federalSubscriptionsEnabled) {
		this.federalSubscriptionsEnabled = federalSubscriptionsEnabled;
	}

}
