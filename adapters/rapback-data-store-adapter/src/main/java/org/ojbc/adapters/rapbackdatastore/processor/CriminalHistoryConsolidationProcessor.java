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
import java.util.Set;

import org.apache.camel.Exchange;
import org.apache.camel.Header;
import org.apache.commons.collections4.ListUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.ojbc.adapters.rapbackdatastore.dao.RapbackDAO;
import org.ojbc.intermediaries.sn.SubscriptionNotificationConstants;
import org.ojbc.intermediaries.sn.dao.SubscriptionSearchQueryDAO;
import org.ojbc.intermediaries.sn.notification.CivilEmailNotificationFilterProcessor;
import org.ojbc.util.model.rapback.Subscription;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import jakarta.annotation.Resource;

@Service
public class CriminalHistoryConsolidationProcessor {

    private static final Log log = LogFactory.getLog(CriminalHistoryConsolidationProcessor.class);

    @Resource
    private RapbackDAO rapbackDAO;
    
    @Resource
    private CivilEmailNotificationFilterProcessor civilEmailNotificationFilterProcessor;

    @Resource
    private SubscriptionSearchQueryDAO subscriptionSearchQueryDAO;

    @Value("${rapbackDatastoreAdapter.emailSubjectPrefix:}")
    private String emailSubjectPrefix;
    
    @Value("${rapbackDatastoreAdapter.federalSubscriptions:true}")
    private boolean federalSubscriptionsEnabled;

    @Value("${rapbackDatastoreAdapter.agencyNotificationEmailAddress:agencyemail@local.gov}")
    private String agencyNotificationEmailAddress;
    
    @Value("${rapbackDatastoreAdapter.consolidateEmailAddresses:false}")
    private boolean consolidateEmailAddresses;
    
    private final static String STATE_USER_EXPUNGEMENT_CIVIL_EMAIL_TEMPLATE ="You are receiving this message because a identity deletion event has occurred related to one or more of your Rap Back subscriptions.  Your subscriptions have been cancelled.";  
    private final static String STATE_IDENTIFIER_UPDATE_CIVIL_EMAIL_TEMPLATE ="<Old SID> has been updated to <New SID>.  Our records show you have an active Rap Back subscription to one of these SIDs.  Please logon to the HIJIS portal to verify your subscription and for updated criminal history record information.";
    private final static String STATE_CONSOLIDATION_CIVIL_EMAIL_TEMPLATE ="<Old SID> has been updated to <New SID>.  Our records show you have an active Rap Back subscription to one of these SIDs.  Please logon to the HIJIS portal to verify your subscription and for updated criminal history record information.";
    
    private final static String STATE_USER_EXPUNGEMENT_EMAIL_TEMPLATE ="<Old SID> has been deleted from CJIS-Hawaii and the State AFIS; you will no longer receive Rap Back notifications on this offender.  Please logon to the HIJIS Portal to update your subscription as necessary.";
    private final static String STATE_AGENCY_EXPUNGEMENT_EMAIL_TEMPLATE="<Old SID> has been deleted from CJIS-Hawaii and the State AFIS; Rap Back notifications will no longer be sent for this offender.";
    		
    private final static String STATE_CONSOLIDATION_EMAIL_TEMPLATE ="<Old SID> has been consolidated into <New SID>.  Our records show you have an active Rap Back subscription to one of these SIDs.  Please logon to the HIJIS portal to verify your subscription.  For the updated criminal history record information, logon to CJIS-Hawaii to run a query on <New SID>.  A new arrest may or may not have occurred.";
    private final static String STATE_AGENCY_CONSOLIDATION_UCN_MISMATCH_EMAIL_TEMPLATE ="New SID/ UCN: <New SID>/ <New UCN>\n\nOld SID/ UCN: <Old SID>/ <Old UCN>\n\nThe UCN stated above has been consolidated or updated in CJIS-Hawaii. The new UCN or SID do not match what is on the subscription.\n\nFollow up with CRID Section to verify or correct the UCN or SID and then verify that any subscriptions were updated, as appropriate.";
    private final static String STATE_USER_CONSOLIDATION_UCN_MISMATCH_EMAIL_TEMPLATE ="New UCN: <New UCN>\nOld UCN: <Old UCN>\n\nThe UCNs stated above have been consolidated or updated in CJIS-Hawaii.  Our records show you have an active federal Rap Back subscription to one of these UCNs. Please logon to the HIJIS portal to verify your subscription.  For the updated criminal history record information, logon to OpenFox/NCIC to run a query on the new UCN. A new arrest may or may not have occurred.\nYou may receive another notification once the UCN is updated in FBI NGI System."; 
    
    private final static String STATE_IDENTIFIER_UPDATE_EMAIL_TEMPLATE ="<Old SID> has been updated to <New SID>.  Our records show you have an active Rap Back subscription to one of these SIDs.  Please logon to the HIJIS portal to verify your subscription.  For the updated criminal history record information, logon to CJIS-Hawaii to run a query on <New SID>.  A new arrest may or may not have occurred.";
    
    private final static String FEDERAL_USER_SUB_WITH_UCN_EMAIL_TEMPLATE="SID: <New SID> \n UCN: <New UCN>\n\nThe UCN associated to this SID has been updated.  Our records show you have an active State Rap Back subscription associated with this offender.  A federal Rap Back subscription was automatically created on your behalf.  Please log onto the HIJIS portal to verify or update your subscription as necessary.  For the updated criminal history record information, logon to OpenFox/NCIC to run a query on this UCN.";
    private final static String FEDERAL_AGENCY_SUB_WITH_UCN_EMAIL_TEMPLATE="Agency: SID: <New SID> \n UCN: <New UCN>\n\nThe active subscription for this SID did not have a corresponding federal subscription.  This subscription was updated to include the UCN stated above.  A Federal Rap Back subscription request was automatically sent.";
    
    private final static String FEDERAL_USER_UCN_CONSOLIDATION_EMAIL_TEMPLATE="New UCN: <New UCN> \n Old UCN: <Old UCN>\n\nThe FBI's NGI System has consolidated the UCNs stated above. Our records show you have an active federal Rap Back subscription to one of these UCNs. Please logon to the HIJIS portal to verify your subscription.  For the updated criminal history record information, logon to OpenFox/NCIC to run a query on the new UCN. A new arrest may or may not have occurred. You may receive another notification once the UCN is updated in CJIS-Hawaii.";
    private final static String FEDERAL_AGENCY_UCN_CONSOLIDATION_EMAIL_TEMPLATE="New UCN: <New UCN> \n Old UCN: <Old UCN>\n\nThe FBI's NGI System has consolidated the UCNs stated above. The corresponding State subscription(s) was updated with the consolidated UCN received in the RBN.";
    
    private final static String FEDERAL_USER_UCN_EXPUNGEMENT_EMAIL_TEMPLATE="Deleted UCN:  <Old UCN> \n This UCN has been deleted from the FBI's NGI System; you will no longer receive federal Rap Back notifications on this offender.  Should a new UCN be reassigned to this offender, a new federal Rap Back subscrition will automatically occur.  Please logon to the HIJIS Portal to update your subscription, if necessary.";
    private final static String FEDERAL_AGENCY_UCN_EXPUNGEMENT_EMAIL_TEMPLATE="Deleted UCN:  <Old UCN> \n\nThis UCN has been deleted from the FBI's NGI System.\n\nNotifications for this offender will no longer be sent for Federal Rap Back.";

    private final static String FEDERAL_AGENCY_UCN_RESTORATION_EMAIL_TEMPLATE="Restored UCN: <Old SID>\n\nThis UCN was restored to the FBI's NGI System.";
    
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
    		@Header("consolidationType") String consolidationType,
    		@Header("fingerprintPurpose") String fingerprintPurpose
    		) throws Exception {
    	
    	log.info("Current SID: "  + currentSid + ", new SID: " + newSid);
    	log.info("Current UCN: "  + currentUcn + ", new UCN: " + newUcn);
    	
    	if (federalSubscriptionsEnabled)
    	{	
    		log.info("Federal subscriptions enabled.  Consolidate SID however don't update UCN, notify agency and user.");
    		
	    	//Consolidate SID for members who use rapback
	    	if (StringUtils.isNotBlank(currentSid) && StringUtils.isNotBlank(newSid)){
	    		log.info("Consolidating SID " + currentSid + " with " + newSid);
	    		rapbackDAO.consolidateSidFederal(currentSid, newSid);
	    	}
	    	
    	}	
    	
    	Map<String,String> subjectIdentifiers = Collections.singletonMap(SubscriptionNotificationConstants.SID, currentSid);
    	//Search for active subscriptions with matching current SID
    	List<Subscription> subscriptionsMatchingCurrentSID = subscriptionSearchQueryDAO.queryForSubscription(null, null, null, subjectIdentifiers);

    	List<Subscription> subscriptionsMatchingNewSID = new ArrayList<Subscription>();
    	
    	if (StringUtils.isNotBlank(newSid) && StringUtils.isNotBlank(currentUcn))
    	{	
    		//If new and current SID are the same, no need to re-run this query
    		if (!newSid.equals(currentSid))
    		{	
		    	subjectIdentifiers = Collections.singletonMap(SubscriptionNotificationConstants.SID, newSid);
		    	//Search for active subscriptions with matching new SID
		   		subscriptionsMatchingNewSID = subscriptionSearchQueryDAO.queryForSubscription(null, null, null, subjectIdentifiers);
    		}
    	}
   		
   		@SuppressWarnings("unchecked")
		List<Subscription> subscriptionsMatchingSID = ListUtils.union(subscriptionsMatchingCurrentSID, subscriptionsMatchingNewSID);
   		
    	//When handling a SID consolidation or update message, from in state, check that the new UCN received from matches what is on the active subscription.
    	//If the new UCN does not match, notify the RB administrator
    	//If the new UCN does match, notify the user
    	List<Subscription> subscriptionsWithUCNMatches = new ArrayList<Subscription>();
    	List<Subscription> subscriptionsWithUCNAdded = new ArrayList<Subscription>();

    	List<CriminalHistoryConsolidationNotification> subscriptionWithUcnMismatches = new ArrayList<CriminalHistoryConsolidationNotification>();
    			
    	if (StringUtils.isNotEmpty(newUcn))
    	{
    		for (Subscription subscriptionMatchingSID : subscriptionsMatchingSID)
    		{
    			//Since we have a SID, we know Subject Identifiers are not null, no need to check
    			Map<String,String> subscriptionSubjectIdentifiers = subscriptionMatchingSID.getSubscriptionSubjectIdentifiers();
    			
				String ucnInSubscription = subscriptionSubjectIdentifiers.get(SubscriptionNotificationConstants.FBI_ID);

				if (StringUtils.isNotEmpty(ucnInSubscription))
				{
    				if (newUcn.equals(ucnInSubscription))
    				{
    					log.info("UCN in consolidation message: "  + newUcn + " matches UCN in subscription" + ucnInSubscription);
    					
    					//We have a match, notify user based on current requirements
    					subscriptionsWithUCNMatches.add(subscriptionMatchingSID);
    				}
    				else
    				{
    					log.info("UCN in consolidation message: "  + newUcn + " does NOT match UCN in subscription" + ucnInSubscription + ".  Notify agency");
    					
    					CriminalHistoryConsolidationNotification consolidationNotificationForAgency = new CriminalHistoryConsolidationNotification();
    					
    		   			consolidationNotificationForAgency.setConsolidationType("reportUCNMismatchToAgency");
    					consolidationNotificationForAgency.setEmailTo(agencyNotificationEmailAddress);
    					
    					String emailBody = StringUtils.replace(STATE_AGENCY_CONSOLIDATION_UCN_MISMATCH_EMAIL_TEMPLATE, "<Old UCN>", currentUcn);
    					
    					consolidationNotificationForAgency.setEmailBody(emailBody);
    					consolidationNotificationForAgency.setEmailSubject(returnStateEmailSubject(consolidationNotificationForAgency, currentUcn));    		
    		    		
    					//Add UCN mismatch here
    					subscriptionWithUcnMismatches.add(consolidationNotificationForAgency);
    				}	
				}	
				else
				{
					subscriptionMatchingSID.getSubscriptionSubjectIdentifiers().put(SubscriptionNotificationConstants.FBI_ID, newUcn);
					//Update subscription in database here
					subscriptionSearchQueryDAO.insertSubjectIdentifier(subscriptionMatchingSID.getId(), SubscriptionNotificationConstants.FBI_ID, newUcn);
					subscriptionsWithUCNAdded.add(subscriptionMatchingSID);
				}	
    		}	
    	}
    	
    	//These notifications are based on SID consolidation
    	List<CriminalHistoryConsolidationNotification> criminalHistoryConsolidationNotifications = returnStateCriminalHistoryConsolidations(
				currentSid, newSid, currentUcn, newUcn, consolidationType, subscriptionsMatchingSID, fingerprintPurpose);
    	
    	//Create subscriptions for user for subscriptions where UCN is added
    	exch.getIn().setHeader("subscriptionsToModify", subscriptionsWithUCNAdded);

    	criminalHistoryConsolidationNotifications = returnUniqueEmailNotifications(criminalHistoryConsolidationNotifications);
    	
    	//Notify the user if the new UCN matches
    	List<CriminalHistoryConsolidationNotification> ucnMatchNotifications = addUcnMatchNotifications(currentUcn, newUcn, subscriptionsWithUCNMatches);	
    	
    	//Create federal subscriptions if No UCN is present
    	List<CriminalHistoryConsolidationNotification> federalSubscriptionNotifications = addFederalSubscriptionNotifications(currentSid, newSid, newUcn,
				subscriptionsWithUCNAdded);	    	
    	
    	criminalHistoryConsolidationNotifications.addAll(federalSubscriptionNotifications);
    	criminalHistoryConsolidationNotifications.addAll(ucnMatchNotifications);
    	criminalHistoryConsolidationNotifications.addAll(subscriptionWithUcnMismatches);
    	
    	
    	return criminalHistoryConsolidationNotifications;
    	
    }

	private List<CriminalHistoryConsolidationNotification> addUcnMatchNotifications(
			String currentUcn,
			String newUcn,
			List<Subscription> subscriptionsWithUCNMatches) {
		
		List<CriminalHistoryConsolidationNotification> criminalHistoryConsolidationNotifications  = new ArrayList<CriminalHistoryConsolidationNotification>();
		
		if (!subscriptionsWithUCNMatches.isEmpty())
    	{
			
			for (Subscription subscription : subscriptionsWithUCNMatches)
			{
				CriminalHistoryConsolidationNotification consolidationNotificationForUser = new CriminalHistoryConsolidationNotification();
				
				consolidationNotificationForUser.setSubscription(subscription);
				consolidationNotificationForUser.setConsolidationType("reportUCNMmatchToUser");
				
				String emailBody="";
				
				emailBody = StringUtils.replace(STATE_USER_CONSOLIDATION_UCN_MISMATCH_EMAIL_TEMPLATE, "<New UCN>", newUcn);
				emailBody = StringUtils.replace(emailBody, "<Old UCN>", currentUcn);
				
				consolidationNotificationForUser.setEmailBody(emailBody);
				
				consolidationNotificationForUser.setEmailSubject(returnStateEmailSubject(consolidationNotificationForUser, currentUcn));
				
				addCriminalHistoryNotificationEmails(criminalHistoryConsolidationNotifications, consolidationNotificationForUser, subscription);
				
				criminalHistoryConsolidationNotifications.add(consolidationNotificationForUser);
				
			}				
			
    	}
		
		return returnUniqueEmailNotifications(criminalHistoryConsolidationNotifications);
	}

	private List<CriminalHistoryConsolidationNotification> addFederalSubscriptionNotifications(String currentSid,
			String newSid, String newUcn,
			List<Subscription> subscriptionsWithUCNAdded) {
		
		List<CriminalHistoryConsolidationNotification> criminalHistoryConsolidationNotifications = new ArrayList<CriminalHistoryConsolidationNotification>();
		
		//Notify user and agency if federal subscription is created
    	if (!subscriptionsWithUCNAdded.isEmpty())
    	{
			CriminalHistoryConsolidationNotification consolidationNotificationForAgency = new CriminalHistoryConsolidationNotification();
			
   			consolidationNotificationForAgency.setConsolidationType("reportUCNAddedToAgency");
			consolidationNotificationForAgency.setEmailTo(agencyNotificationEmailAddress);
			
			String emailBody = StringUtils.replace(FEDERAL_AGENCY_SUB_WITH_UCN_EMAIL_TEMPLATE, "<New UCN>", newUcn);
			emailBody = StringUtils.replace(emailBody, "<New SID>", newSid);

			consolidationNotificationForAgency.setEmailBody(emailBody);
			consolidationNotificationForAgency.setEmailSubject(returnStateEmailSubject(consolidationNotificationForAgency, currentSid));    		
    	
			criminalHistoryConsolidationNotifications.add(consolidationNotificationForAgency);
			
			for (Subscription subscription : subscriptionsWithUCNAdded)
			{
				CriminalHistoryConsolidationNotification consolidationNotificationForUser = new CriminalHistoryConsolidationNotification();
				
				consolidationNotificationForUser.setSubscription(subscription);
				consolidationNotificationForUser.setConsolidationType("reportUCNAddedToUser");
				
				emailBody="";
				
				emailBody = StringUtils.replace(FEDERAL_USER_SUB_WITH_UCN_EMAIL_TEMPLATE, "<New SID>", newSid);
				emailBody = StringUtils.replace(emailBody, "<New UCN>", newUcn);
				
				consolidationNotificationForUser.setEmailBody(emailBody);
				
				consolidationNotificationForUser.setEmailSubject(returnStateEmailSubject(consolidationNotificationForUser, currentSid));
				addCriminalHistoryNotificationEmails(criminalHistoryConsolidationNotifications, consolidationNotificationForUser, subscription);
				
				criminalHistoryConsolidationNotifications.add(consolidationNotificationForUser);
				
			}	
    	}
    	
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
	    		
	    		chcNotification.setEmailBody(returnFederalNotificationEmailBody(chcNotification, currentUcn, newUcn));
	    		chcNotification.setEmailSubject(returnFederalEmailSubject(chcNotification, currentUcn));
	    		
	    		addCriminalHistoryNotificationEmails(criminalHistoryConsolidationNotifications, chcNotification, subscriptionMatchingFBIID);
				
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
    		@Header("consolidationType") String consolidationType,
    		@Header("fingerprintPurpose") String fingerprintPurpose
    		) throws Exception {
    	
    	log.info("Current SID: "  + currentSid + ", current UCN: " + currentUcn);
    	
    	List<CriminalHistoryConsolidationNotification> criminalHistoryConsolidationNotifications = returnStateCriminalHistoryConsolidations(
				currentSid, "", currentUcn, "", consolidationType, null, fingerprintPurpose);

    	criminalHistoryConsolidationNotifications =  returnUniqueEmailNotifications(criminalHistoryConsolidationNotifications);
    	
    	boolean sendAgencyNotification = false;
    	
    	//Set to true unless purpose is explicitly civil
    	if (StringUtils.isNotBlank(fingerprintPurpose))
    	{
    		if (fingerprintPurpose.equals("criminal"))
    		{
    			sendAgencyNotification = true;
    		}	
    	}	
    	else
    	{
    		sendAgencyNotification=true;
    	}	
    	
    	if (sendAgencyNotification)
    	{	
	    	//In addition to notifying the user, also notify the agency with a different email template
	    	if (!criminalHistoryConsolidationNotifications.isEmpty())
	    	{
				CriminalHistoryConsolidationNotification consolidationNotificationForAgency = new CriminalHistoryConsolidationNotification();
				
	   			consolidationNotificationForAgency.setConsolidationType("reportSIDExpungementToAgency");
				consolidationNotificationForAgency.setEmailTo(agencyNotificationEmailAddress);
				consolidationNotificationForAgency.setEmailBody(returnStateNotificationEmailBody(consolidationNotificationForAgency, currentSid, "", fingerprintPurpose));
				consolidationNotificationForAgency.setEmailSubject(returnStateEmailSubject(consolidationNotificationForAgency, currentSid));    		
	    		
				criminalHistoryConsolidationNotifications.add(consolidationNotificationForAgency);
	    	}
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
		consolidationNotificationForAgency.setEmailSubject("Rap Back: UCN Restored by FBI: " + currentUcn);
		consolidationNotificationForAgency.setEmailBody(emailBody);    
    	
		criminalHistoryConsolidationNotifications.add(consolidationNotificationForAgency);
    	
    	return criminalHistoryConsolidationNotifications;
    }    
    
	List<CriminalHistoryConsolidationNotification> returnStateCriminalHistoryConsolidations(
			String currentSid, String newSid, String currentUcn, String newUcn, String consolidationType, List<Subscription> subscriptionsMatchingSID, String fingerprintPurpose) {
		//Consolidate SID but not UCN for members who use simple subscription database setup
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
    			
    		}	
    		
    		CriminalHistoryConsolidationNotification chcNotification  = new CriminalHistoryConsolidationNotification();
    		
    		chcNotification.setSubscription(subscription);
    		chcNotification.setConsolidationType(consolidationType);
    		chcNotification.setEmailBody(returnStateNotificationEmailBody(chcNotification, currentSid, newSid, fingerprintPurpose));
    		chcNotification.setEmailSubject(returnStateEmailSubject(chcNotification, currentSid));

    		addCriminalHistoryNotificationEmails(criminalHistoryConsolidationNotifications, chcNotification, subscription);
    		
    		
    	}
    	
		return criminalHistoryConsolidationNotifications;
	}    
    
	
	private void addCriminalHistoryNotificationEmails(
			List<CriminalHistoryConsolidationNotification> criminalHistoryConsolidationNotifications, CriminalHistoryConsolidationNotification chcNotification,
			Subscription subscription) {
			
			Set<String> emailAddressesToNotify = subscription.getEmailAddressesToNotify();
			
			if (consolidateEmailAddresses)
			{
				String commaSeperateEmails = String.join(",", emailAddressesToNotify);
				
				if (!emailAddressesToNotify.contains(subscription.getSubscriptionOwnerEmailAddress()))
				{
					commaSeperateEmails = commaSeperateEmails + "," + subscription.getSubscriptionOwnerEmailAddress();
				}	
				
				chcNotification.setEmailTo(commaSeperateEmails);
				
				criminalHistoryConsolidationNotifications.add(chcNotification);
			}	
			else
			{	
				chcNotification.setEmailTo(subscription.getSubscriptionOwnerEmailAddress());
				criminalHistoryConsolidationNotifications.add(chcNotification);

				for(String emailAddress : emailAddressesToNotify)
				{
					CriminalHistoryConsolidationNotification uniqueCCN = new CriminalHistoryConsolidationNotification(chcNotification);
					uniqueCCN.setEmailTo(emailAddress);
					criminalHistoryConsolidationNotifications.add(uniqueCCN);
				}
				
			}	
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
			CriminalHistoryConsolidationNotification chcNotification, String currentSid, String newSid, String fingerprintPurpose) {
		
		String emailBody = "";
		
		if (chcNotification.getConsolidationType().equals("criminalHistoryExpungementReport"))
		{
			if (fingerprintPurpose.equals("criminal")|| StringUtils.isBlank(fingerprintPurpose))
			{	
				emailBody = StringUtils.replace(STATE_USER_EXPUNGEMENT_EMAIL_TEMPLATE, "<Old SID>", currentSid);
			}
			
			if (fingerprintPurpose.equals("civil"))
			{	
				emailBody = StringUtils.replace(STATE_USER_EXPUNGEMENT_CIVIL_EMAIL_TEMPLATE, "<Old SID>", currentSid);
			}	
			
		}	

		if (chcNotification.getConsolidationType().equals("criminalHistoryConsolidationReport"))
		{
			if (fingerprintPurpose.equals("criminal") || StringUtils.isBlank(fingerprintPurpose))
			{	
				emailBody = StringUtils.replace(STATE_CONSOLIDATION_EMAIL_TEMPLATE, "<Old SID>", currentSid);
				emailBody = StringUtils.replace(emailBody, "<New SID>", newSid);
			}
			
			if (fingerprintPurpose.equals("civil"))
			{	
				emailBody = StringUtils.replace(STATE_CONSOLIDATION_CIVIL_EMAIL_TEMPLATE, "<Old SID>", currentSid);
				emailBody = StringUtils.replace(emailBody, "<New SID>", newSid);
			}				
		}	

		if (chcNotification.getConsolidationType().equals("criminalHistoryIdentifierUpdateReport"))
		{
			if (fingerprintPurpose.equals("criminal") || StringUtils.isBlank(fingerprintPurpose))
			{	
				emailBody = StringUtils.replace(STATE_IDENTIFIER_UPDATE_EMAIL_TEMPLATE, "<Old SID>", currentSid);
				emailBody = StringUtils.replace(emailBody, "<New SID>", newSid);
			}
			
			if (fingerprintPurpose.equals("civil"))
			{	
				emailBody = StringUtils.replace(STATE_IDENTIFIER_UPDATE_CIVIL_EMAIL_TEMPLATE, "<Old SID>", currentSid);
				emailBody = StringUtils.replace(emailBody, "<New SID>", newSid);
			}				
		}	
		
		if (chcNotification.getConsolidationType().equals("reportSIDExpungementToAgency"))
		{
			
			if (fingerprintPurpose.equals("criminal") || StringUtils.isBlank(fingerprintPurpose))
			{	
				emailBody = StringUtils.replace(STATE_AGENCY_EXPUNGEMENT_EMAIL_TEMPLATE, "<Old SID>", currentSid);
			}
			
		}	

		return emailBody;
	}

	String returnStateEmailSubject(
			CriminalHistoryConsolidationNotification chcNotification, String currentIdentifier) {
		
		String emailSubject = "";
		
		if (chcNotification.getConsolidationType().equals("criminalHistoryExpungementReport"))
		{
			emailSubject = emailSubjectPrefix + "Rap Back: SID Deletion by HCJDC";
		}	

		if (chcNotification.getConsolidationType().equals("criminalHistoryConsolidationReport"))
		{
			emailSubject = emailSubjectPrefix + "Rap Back: SID Consolidation by HCJDC";
		}	

		if (chcNotification.getConsolidationType().equals("criminalHistoryIdentifierUpdateReport"))
		{
			emailSubject = emailSubjectPrefix + "Rap Back: SID Update by HCJDC";
		}	

		if (chcNotification.getConsolidationType().equals("reportSIDExpungementToAgency"))
		{
			emailSubject = emailSubjectPrefix + "Agency Notification: SID Expungement for: " + currentIdentifier;
		}	

		if (chcNotification.getConsolidationType().equals("reportUCNMismatchToAgency"))
		{
			emailSubject = emailSubjectPrefix + "Agency Notification: UCN mismatch during SID consolidation for: " + currentIdentifier;
		}	
		
		if (chcNotification.getConsolidationType().equals("reportUCNAddedToAgency"))
		{
			emailSubject = emailSubjectPrefix + "Rap Back: Federal Subscription Created: " + currentIdentifier;
		}	

		if (chcNotification.getConsolidationType().equals("reportUCNAddedToUser"))
		{
			emailSubject = emailSubjectPrefix + "Rap Back: Federal Subscription Created: " + currentIdentifier;
		}	

		if (chcNotification.getConsolidationType().equals("reportUCNMmatchToUser"))
		{
			if (StringUtils.isNotBlank(currentIdentifier))
			{	
				emailSubject = emailSubjectPrefix + "UCN update occurred for: " + currentIdentifier;
			}
			else
			{
				emailSubject = emailSubjectPrefix + "UCN update occurred";
			}	
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
			emailSubject = emailSubjectPrefix + "Rap Back: UCN Consolidation by FBI: " + currentUcn;
		}	

		if (chcNotification.getConsolidationType().equals("reportUCNConsolidationToUser"))
		{
			emailSubject = emailSubjectPrefix +"Rap Back: UCN Consolidation by FBI: " + currentUcn;
		}	

		if (chcNotification.getConsolidationType().equals("reportUCNExpungementToAgency"))
		{
			emailSubject = emailSubjectPrefix +"Rap Back: UCN Deleted by FBI: " + currentUcn;
		}	

		if (chcNotification.getConsolidationType().equals("reportUCNExpungementToUser"))
		{
			emailSubject = emailSubjectPrefix +"Rap Back: UCN Deleted by FBI: " + currentUcn;
		}	

		return emailSubject;
	}
	
	public void returnCamelEmail(Exchange ex)
	{	
		CriminalHistoryConsolidationNotification chcNotification = (CriminalHistoryConsolidationNotification) ex.getIn().getBody();

		ex.getIn().removeHeaders("*");
		
		ex.getIn().setHeader("to", chcNotification.getEmailTo());
		
		Subscription subscription = chcNotification.getSubscription();
		
		if (subscription != null)
		{
			//Get email address here
			List<String> emailAddressesToAdd = civilEmailNotificationFilterProcessor.returnEmailAddresses(subscription);
	        
			if (emailAddressesToAdd.size() > 0)
			{
				String commaSeperatedToAddresses = String.join(",", emailAddressesToAdd);
				
				ex.getIn().setHeader("to", commaSeperatedToAddresses);
			}    
	
		}
		
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
