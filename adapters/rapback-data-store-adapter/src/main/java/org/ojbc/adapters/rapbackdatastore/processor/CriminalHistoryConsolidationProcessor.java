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

    @Value("${rapbackDatastoreAdapter.agencyNotificationEmailAddress:agencyemail@local.gov}")
    private String agencyNotificationEmailAddress;
    
    private final static String STATE_USER_EXPUNGEMENT_EMAIL_TEMPLATE ="<Old SID> has been deleted from CJIS-Hawaii and the State AFIS; you will no longer receive Rap Back notifications on this offender.  Please logon to the HIJIS Portal to update your subscription as necessary.";
    private final static String STATE_AGENCY_EXPUNGEMENT_EMAIL_TEMPLATE="<Old SID> EMAIL TEMPLATE PENDING";
    		
    private final static String STATE_CONSOLIDATION_EMAIL_TEMPLATE ="<Old SID> has been consolidated into <New SID>.  Our records show you have an active Rap Back subscription to one of these SIDs.  Please logon to the HIJIS portal to verify your subscription.  For the updated criminal history record information, logon to CJIS-Hawaii to run a query on <New SID>.  A new arrest may or may not have occurred.";
    private final static String STATE_AGENCY_CONSOLIDATION_UCN_MISMATCH_EMAIL_TEMPLATE ="<Old SID> has been consolidated into <New SID>.  EMAIL TEMPLATE PENDING";
    
    private final static String STATE_IDENTIFIER_UPDATE_EMAIL_TEMPLATE ="<Old SID> has been updated to <New SID>.  Our records show you have an active Rap Back subscription to one of these SIDs.  Please logon to the HIJIS portal to verify your subscription.  For the updated criminal history record information, logon to CJIS-Hawaii to run a query on <New SID>.  A new arrest may or may not have occurred.";
    
    private final static String FEDERAL_USER_SUB_WITH_UCN_EMAIL_TEMPLATE="SID: <SID> \n UCN: <UCN>\n\nThe UCN associated to this SID has been updated.  Our records show you have an active State Rap Back subscription associated with this offender.  A federal Rap Back subscription was automatically created on your behalf.  Please log onto the HIJIS portal to verify or update your subscription as necessary.  For the updated criminal history record information, logon to OpenFox/NCIC to run a query on this UCN.";
    private final static String FEDERAL_AGENCY_SUB_WITH_UCN_EMAIL_TEMPLATE="SID: <SID> \n UCN: <UCN>\n\nThe UCN associated to this SID has been updated.  Federal subscription added for user.";
    
    private final static String FEDERAL_USER_UCN_CONSOLIDATION_EMAIL_TEMPLATE="New UCN: <New UCN> \n Old UCN: <Old UCN>\n\nThe FBI's NGI System has consolidated the UCNs stated above. Our records show you have an active federal Rap Back subscription to one of these UCNs. Please logon to the HIJIS portal to verify your subscription.  For the updated criminal history record information, logon to OpenFox/NCIC to run a query on the new UCN. A new arrest may or may not have occurred. You may receive another notification once the UCN is updated in CJIS-Hawaii.";
    private final static String FEDERAL_AGENCY_UCN_CONSOLIDATION_EMAIL_TEMPLATE="New UCN: <New UCN> \n Old UCN: <Old UCN>\n\n  EMAIL TEMPLATE PENDING";
    
    private final static String FEDERAL_USER_UCN_EXPUNGEMENT_EMAIL_TEMPLATE="Deleted UCN:  <Old UCN> \n This UCN has been deleted from the FBI's NGI System; you will no longer receive federal Rap Back notifications on this offender.  Should a new UCN be reassigned to this offender, a new federal Rap Back subscrition will automatically occur.  Please logon to the HIJIS Portal to update your subscription, if necessary.";
    private final static String FEDERAL_AGENCY_UCN_EXPUNGEMENT_EMAIL_TEMPLATE="Deleted UCN:  <Old UCN> \n\n  EMAIL TEMPLATE PENDING";

    private final static String FEDERAL_AGENCY_UCN_RESTORATION_EMAIL_TEMPLATE="RESTORATION EMAIL TEMPLATE PENDING";
    
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
    public List<CriminalHistoryConsolidationNotification> consolidateCriminalHistoryState(Exchange exch, @Header("currentSid") String currentSid, 
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

    	//When handling a SID consolidation or update message, from in state, check that the new UCN received from matches what is on the active subscription.
    	//If the new UCN does not match, notify the RB administrator via email.
    	//If the new UCN matches, notify the subscription owner. ??????
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
    	
    	List<CriminalHistoryConsolidationNotification> criminalHistoryConsolidationNotifications = returnStateCriminalHistoryConsolidations(
				currentSid, newSid, currentUcn, newUcn, consolidationType, subscriptionsMatchingSID);
    	
    	//Do something with UCN mismatches
    	
    	//Create subscriptions for user for subscriptions where UCN is added
    	exch.getIn().setHeader("subscriptionsToModify", subscriptionsWithUCNAdded);

    	criminalHistoryConsolidationNotifications = returnUniqueEmailNotifications(criminalHistoryConsolidationNotifications);
    	
    	//In addition to notifying the user, also notify the agency with a different email template
    	if (!subscriptionsWithUCNMismatches.isEmpty())
    	{
			CriminalHistoryConsolidationNotification consolidationNotificationForAgency = new CriminalHistoryConsolidationNotification();
			
   			consolidationNotificationForAgency.setConsolidationType("reportUCNMismatchToAgency");
			consolidationNotificationForAgency.setEmailTo(agencyNotificationEmailAddress);
			
			String emailBody = StringUtils.replace(STATE_AGENCY_CONSOLIDATION_UCN_MISMATCH_EMAIL_TEMPLATE, "<Old SID>", currentSid);
			
			consolidationNotificationForAgency.setEmailBody(emailBody);
			consolidationNotificationForAgency.setEmailSubject(returnStateEmailSubject(consolidationNotificationForAgency, currentSid));    		
    		
			//Add UCN mismatch here
			criminalHistoryConsolidationNotifications.add(consolidationNotificationForAgency);
    	}	
    	
    	return criminalHistoryConsolidationNotifications;
    	
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
    @Transactional
    public List<CriminalHistoryConsolidationNotification> consolidateCriminalHistoryFederal(Exchange exch,
    		@Header("currentUcn") String currentUcn, 
    		@Header("newUcn") String newUcn,
    		@Header("consolidationType") String consolidationType
    		) throws Exception {
    	
    	log.info("Current UCN: "  + currentUcn + ", new UCN: " + newUcn + ", Consolidation Type: " + consolidationType);
    	
    	if (federalSubscriptionsEnabled)
    	{	
    		log.info("Federal subscriptions enabled.  Consolidate UCN if current and new UCN exists.");
	    	
	    	if (StringUtils.isNotBlank(currentUcn) && StringUtils.isNotBlank(newUcn)){
	    		log.info("Consolidating UCN " + currentUcn + " with " + newUcn);
	    		rapbackDAO.consolidateUcnFederal(currentUcn, newUcn);
	    	}
    	}	

    	return performFederalConsolidationMaintenance(exch, currentUcn, newUcn, consolidationType);
    	
    }
    

	private List<CriminalHistoryConsolidationNotification> performFederalConsolidationMaintenance(Exchange exch,
			String currentUcn, String newUcn, String consolidationType) {
		List<Subscription> subscriptionsWithFederalUCNConsolidations = new ArrayList<Subscription>();
		
		List<CriminalHistoryConsolidationNotification> criminalHistoryConsolidationNotifications = new ArrayList<CriminalHistoryConsolidationNotification>();
		
		if (StringUtils.isNotEmpty(newUcn))
		{
			
	    	Map<String,String> subjectIdentifiers = Collections.singletonMap(SubscriptionNotificationConstants.FBI_ID, currentUcn);
	    	
	    	//Search for active subscriptions with matching SIDs
	    	List<Subscription> subscriptionsMatchingFBIID = subscriptionSearchQueryDAO.queryForSubscription(null, null, null, subjectIdentifiers);

	    	if (subscriptionsMatchingFBIID == null || subscriptionsMatchingFBIID.isEmpty())
	    	{
	    		log.info("No subscriptions found with FBI ID: " + currentUcn);
	    	}	
	    	
			for (Subscription subscriptionMatchingFBIID : subscriptionsMatchingFBIID)
			{
				
				//We don't have a match notify agency of UCN consolidation
				subscriptionsWithFederalUCNConsolidations.add(subscriptionMatchingFBIID);
				
				subscriptionSearchQueryDAO.updateSubscriptionSubjectIdentifier(currentUcn, newUcn, subscriptionMatchingFBIID.getSubscriptionIdentifier(), SubscriptionNotificationConstants.FBI_ID);
				
	    		CriminalHistoryConsolidationNotification chcNotification  = new CriminalHistoryConsolidationNotification();
	    		
	    		chcNotification.setSubscription(subscriptionMatchingFBIID);
	    		
	    		if (consolidationType.equals("criminalHistoryConsolidationReport"))
	    		{
	    			chcNotification.setConsolidationType("reportUCNConsolidationToUser");
	    		}	

	    		if (consolidationType.equals("criminalHistoryExpungementReport"))
	    		{
	    			chcNotification.setConsolidationType("reportUCNExpungementToUser");
	    		}	

	    		chcNotification.setEmailTo(subscriptionMatchingFBIID.getSubscriptionOwnerEmailAddress());
	    		chcNotification.setEmailBody(returnFederalNotificationEmailBody(chcNotification, currentUcn, newUcn));
	    		chcNotification.setEmailSubject(returnFederalEmailSubject(chcNotification, currentUcn));
	    		
	    		criminalHistoryConsolidationNotifications.add(chcNotification);
				
			}	
		}
		
		if (!criminalHistoryConsolidationNotifications.isEmpty())
		{	
			criminalHistoryConsolidationNotifications = returnUniqueEmailNotifications(criminalHistoryConsolidationNotifications);
			
			CriminalHistoryConsolidationNotification consolidationNotificationForAgency = new CriminalHistoryConsolidationNotification();
			
    		if (consolidationType.equals("criminalHistoryConsolidationReport"))
    		{
    			consolidationNotificationForAgency.setConsolidationType("reportUCNConsolidationToAgency");
    		}	

    		if (consolidationType.equals("criminalHistoryExpungementReport"))
    		{
    			consolidationNotificationForAgency.setConsolidationType("reportUCNExpungementToAgency");
    		}
    		
			consolidationNotificationForAgency.setEmailTo(agencyNotificationEmailAddress);
			consolidationNotificationForAgency.setEmailBody(returnFederalNotificationEmailBody(consolidationNotificationForAgency, currentUcn, newUcn));
			consolidationNotificationForAgency.setEmailSubject(returnFederalEmailSubject(consolidationNotificationForAgency, currentUcn));
			
			criminalHistoryConsolidationNotifications.add(consolidationNotificationForAgency);
			
		}	

		return criminalHistoryConsolidationNotifications;
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
    public List<CriminalHistoryConsolidationNotification> expungeCriminalHistoryState(@Header("currentSid") String currentSid, 
    		@Header("currentUcn") String currentUcn, 
    		@Header("consolidationType") String consolidationType) throws Exception {
    	
    	log.info("Current SID: "  + currentSid + ", current UCN: " + currentUcn);
    	
    	List<CriminalHistoryConsolidationNotification> criminalHistoryConsolidationNotifications = returnStateCriminalHistoryConsolidations(
				currentSid, "", currentUcn, "", consolidationType, null);

    	criminalHistoryConsolidationNotifications =  returnUniqueEmailNotifications(criminalHistoryConsolidationNotifications);
    	
    	//In addition to notifying the user, also notify the agency with a different email template
    	if (!criminalHistoryConsolidationNotifications.isEmpty())
    	{
			CriminalHistoryConsolidationNotification consolidationNotificationForAgency = new CriminalHistoryConsolidationNotification();
			
   			consolidationNotificationForAgency.setConsolidationType("reportSIDExpungementToAgency");
			consolidationNotificationForAgency.setEmailTo(agencyNotificationEmailAddress);
			consolidationNotificationForAgency.setEmailBody(returnStateNotificationEmailBody(consolidationNotificationForAgency, currentSid, ""));
			consolidationNotificationForAgency.setEmailSubject(returnStateEmailSubject(consolidationNotificationForAgency, currentSid));    		
    		
			criminalHistoryConsolidationNotifications.add(consolidationNotificationForAgency);
    	}	
    	
    	return criminalHistoryConsolidationNotifications;
    	
    }
    
    public List<CriminalHistoryConsolidationNotification> federalUCNRestoration(Exchange exch,
    		@Header("currentUcn") String currentUcn
    		) throws Exception {
    	
    	log.info("Current UCN: "  + currentUcn );
    	
    	List<CriminalHistoryConsolidationNotification> criminalHistoryConsolidationNotifications = new ArrayList<CriminalHistoryConsolidationNotification>();
    	
    	CriminalHistoryConsolidationNotification consolidationNotificationForAgency = new CriminalHistoryConsolidationNotification();
    	
		consolidationNotificationForAgency.setConsolidationType("federalUCNRestoration");
		consolidationNotificationForAgency.setEmailTo(agencyNotificationEmailAddress);
		
		String emailBody = StringUtils.replace(FEDERAL_AGENCY_UCN_RESTORATION_EMAIL_TEMPLATE, "<Old SID>", currentUcn);
		consolidationNotificationForAgency.setEmailSubject("UCN Restoration message for: " + currentUcn);
		consolidationNotificationForAgency.setEmailBody(emailBody);    
    	
		criminalHistoryConsolidationNotifications.add(consolidationNotificationForAgency);
    	
    	return criminalHistoryConsolidationNotifications;
    }    
    
	List<CriminalHistoryConsolidationNotification> returnStateCriminalHistoryConsolidations(
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
    		chcNotification.setEmailBody(returnStateNotificationEmailBody(chcNotification, currentSid, newSid));
    		chcNotification.setEmailSubject(returnStateEmailSubject(chcNotification, currentSid));
    		
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

	String returnStateNotificationEmailBody(
			CriminalHistoryConsolidationNotification chcNotification, String currentSid, String newSid) {
		
		String emailBody = "";
		
		if (chcNotification.getConsolidationType().equals("criminalHistoryExpungementReport"))
		{
			emailBody = StringUtils.replace(STATE_USER_EXPUNGEMENT_EMAIL_TEMPLATE, "<Old SID>", currentSid);
		}	

		if (chcNotification.getConsolidationType().equals("criminalHistoryConsolidationReport"))
		{
			emailBody = StringUtils.replace(STATE_CONSOLIDATION_EMAIL_TEMPLATE, "<Old SID>", currentSid);
			emailBody = StringUtils.replace(emailBody, "<New SID>", newSid);
		}	

		if (chcNotification.getConsolidationType().equals("criminalHistoryIdentifierUpdateReport"))
		{
			emailBody = StringUtils.replace(STATE_IDENTIFIER_UPDATE_EMAIL_TEMPLATE, "<Old SID>", currentSid);
			emailBody = StringUtils.replace(emailBody, "<New SID>", newSid);
		}	
		
		if (chcNotification.getConsolidationType().equals("reportSIDExpungementToAgency"))
		{
			emailBody = StringUtils.replace(STATE_AGENCY_EXPUNGEMENT_EMAIL_TEMPLATE, "<Old SID>", currentSid);
		}	

		return emailBody;
	}

	String returnStateEmailSubject(
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

		if (chcNotification.getConsolidationType().equals("reportSIDExpungementToAgency"))
		{
			emailSubject = "Agency Notification: SID Expungement for: " + currentSid;
		}	

		if (chcNotification.getConsolidationType().equals("reportUCNMismatchToAgency"))
		{
			emailSubject = "Agency Notification: UCN mismatch during SID consolidation for: " + currentSid;
		}	
		
		
		
		
		return emailSubject;
	}
	
	String returnFederalNotificationEmailBody(
			CriminalHistoryConsolidationNotification chcNotification, String currentUcn, String newUcn) {
		
		String emailBody = "";

		if (chcNotification.getConsolidationType().equals("reportUCNConsolidationToAgency"))
		{
			emailBody = StringUtils.replace(FEDERAL_AGENCY_UCN_CONSOLIDATION_EMAIL_TEMPLATE, "<Old UCN>", currentUcn);
			emailBody = StringUtils.replace(emailBody, "<New UCN>", newUcn);
		}	

		if (chcNotification.getConsolidationType().equals("reportUCNConsolidationToUser"))
		{
			emailBody = StringUtils.replace(FEDERAL_USER_UCN_CONSOLIDATION_EMAIL_TEMPLATE, "<Old UCN>", currentUcn);
			emailBody = StringUtils.replace(emailBody, "<New UCN>", newUcn);
		}	

		if (chcNotification.getConsolidationType().equals("reportUCNExpungementToAgency"))
		{
			emailBody = StringUtils.replace(FEDERAL_AGENCY_UCN_EXPUNGEMENT_EMAIL_TEMPLATE, "<Old UCN>", currentUcn);
		}	

		if (chcNotification.getConsolidationType().equals("reportUCNExpungementToUser"))
		{
			emailBody = StringUtils.replace(FEDERAL_USER_UCN_EXPUNGEMENT_EMAIL_TEMPLATE, "<Old UCN>", currentUcn);
		}	
		
		return emailBody;
	}	

	String returnFederalEmailSubject(
			CriminalHistoryConsolidationNotification chcNotification, String currentUcn) {
		
		String emailSubject = "";
		
		if (chcNotification.getConsolidationType().equals("reportUCNConsolidationToAgency"))
		{
			emailSubject = "Agency UCN consolidation report for UNC for: " + currentUcn;
		}	

		if (chcNotification.getConsolidationType().equals("reportUCNConsolidationToUser"))
		{
			emailSubject = "UCN consolidation report for UNC for: " + currentUcn;
		}	

		if (chcNotification.getConsolidationType().equals("reportUCNExpungementToAgency"))
		{
			emailSubject = "Agency UCN expungement report for UNC for: " + currentUcn;
		}	

		if (chcNotification.getConsolidationType().equals("reportUCNExpungementToUser"))
		{
			emailSubject = "UCN expungement report for UNC for: " + currentUcn;
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
