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

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.ojbc.intermediaries.sn.SubscriptionNotificationConstants;
import org.ojbc.intermediaries.sn.dao.Subscription;
import org.ojbc.intermediaries.sn.dao.SubscriptionSearchQueryDAO;
import org.ojbc.intermediaries.sn.topic.rapback.FederalTriggeringEventCode;
import org.ojbc.util.xml.subscription.SubscriptionNotificationDocumentBuilderUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.w3c.dom.Document;

@Service
public class SubscriptionModificationXMLProcessor {

	private static final Log log = LogFactory.getLog(SubscriptionModificationXMLProcessor.class);
	
	private static final SimpleDateFormat SDF = new SimpleDateFormat("yyyy-MM-dd");
	
    @Resource
    private SubscriptionSearchQueryDAO subscriptionSearchQueryDAO;
    
    @Value("${rapbackDatastoreAdapter.agencyOri:defaultORI}")
    private String agencyOri;
    

	public Document createOJBCsubscriptionDocument(Subscription subscription) {
		Document doc = null;

		try {
			org.ojbc.util.xml.subscription.Subscription subscriptionXMLPojo = new org.ojbc.util.xml.subscription.Subscription();

			createSubscriptionPOJO(subscription, subscriptionXMLPojo);

			doc = SubscriptionNotificationDocumentBuilderUtils
					.createSubscriptionRequest(subscriptionXMLPojo,null);

		} catch (Exception ex) {
			log.error("An error occurred.");

			ex.printStackTrace();
		}

		return doc;
	}
	
	public Document createOJBCsubscriptionModificationDocument(Subscription subscription)
	{
		Document doc = null;
		
        try {
            org.ojbc.util.xml.subscription.Subscription subscriptionXMLPojo = new org.ojbc.util.xml.subscription.Subscription();
            
            createSubscriptionPOJO(subscription, subscriptionXMLPojo);    
            
            doc = SubscriptionNotificationDocumentBuilderUtils.createSubscriptionModifyRequest(subscriptionXMLPojo);
            
        }
        catch (Exception ex)
        {
        	log.error("An error occurred.");
        	
        	ex.printStackTrace();
        } 
            
		
		return doc;
	}

	private void createSubscriptionPOJO(Subscription subscription,
			org.ojbc.util.xml.subscription.Subscription subscriptionXMLPojo)
			throws ParseException {
		subscriptionXMLPojo.setFullName(subscription.getPersonFullName());
		subscriptionXMLPojo.setFirstName(subscription.getPersonFirstName());
		subscriptionXMLPojo.setLastName(subscription.getPersonLastName());
		
		subscriptionXMLPojo.setFbiId(subscription.getSubscriptionSubjectIdentifiers().get(SubscriptionNotificationConstants.FBI_ID));
		subscriptionXMLPojo.setStateId(subscription.getSubscriptionSubjectIdentifiers().get(SubscriptionNotificationConstants.SID));
		
		if (StringUtils.isNotBlank(subscription.getDateOfBirth()))
		{		
			Date dob = SDF.parse(subscription.getDateOfBirth());
			subscriptionXMLPojo.setDateOfBirth(dob);
		}	
		
		subscriptionXMLPojo.setCaseId(subscription.getAgencyCaseNumber());
		
		subscriptionXMLPojo.setOri(agencyOri);
		
		subscriptionXMLPojo.setEmailList(new ArrayList<String>(subscription.getEmailAddressesToNotify()));
		
		subscriptionXMLPojo.setSubscriptionQualificationID(subscription.getSubscriptionIdentifier());
		
		subscriptionXMLPojo.setSubscriptionStartDate(subscription.getStartDate().toDate());
		subscriptionXMLPojo.setSubscriptionEndDate(subscription.getEndDate().toDate());
		
		subscriptionXMLPojo.setSubscriptionPurpose(subscription.getSubscriptionCategoryCode());

		List<String> federalTriggeringEventCode = new ArrayList<String>();
		
		//Triggering Event Codes
		//subscriptionXMLPojo.setFederalTriggeringEventCode(federalTriggeringEventCode);
		Map<String, String> subscriptionProperties = subscription.getSubscriptionProperties();
		
		if (subscriptionProperties != null)
		{
		
			for (Map.Entry<String, String> entry : subscriptionProperties.entrySet()) {
			    if (inFederalTriggeringEventCodeEnum(entry.getKey().replace("-", "_")))
			    {
			    	federalTriggeringEventCode.add(entry.getValue());
			    }	
			    
			}
		}
		
		subscriptionXMLPojo.setFederalTriggeringEventCode(federalTriggeringEventCode);
		
		if (subscription.getSubscriptionProperties() != null)
		{	
			subscriptionXMLPojo.setFederalRapSheetDisclosureAttentionDesignationText(subscription.getSubscriptionProperties().get(SubscriptionNotificationConstants.FEDERAL_RAP_SHEET_DISCLOSURE_ATTENTION_DESIGNATION_TEXT));
			String disclosureIndicator = subscription.getSubscriptionProperties().get(SubscriptionNotificationConstants.FEDERAL_RAP_SHEET_DISCLOSURE_INDICATOR);
			
		    if (StringUtils.isNotBlank(disclosureIndicator))
		    {	
		    	subscriptionXMLPojo.setFederalRapSheetDisclosureIndicator(Boolean.valueOf(disclosureIndicator));
		    }	
		            		
		}	
		
		

		Subscription fbiSubscription = subscriptionSearchQueryDAO.findSubscriptionWithFbiInfoBySubscriptionId(String.valueOf(subscription.getId()));
		
		if (fbiSubscription != null)
		{	
		    String fbiSubscriptionID = fbiSubscription.getFbiRapbackSubscription().getFbiSubscriptionId();
		    subscriptionXMLPojo.setFbiSubscriptionID(fbiSubscriptionID);
		}
	}

    private static boolean inFederalTriggeringEventCodeEnum(String test) {

        for (FederalTriggeringEventCode c : FederalTriggeringEventCode.values()) {
            if (c.name().equals(test)) {
                return true;
            }
        }

        return false;
    }
	
}
