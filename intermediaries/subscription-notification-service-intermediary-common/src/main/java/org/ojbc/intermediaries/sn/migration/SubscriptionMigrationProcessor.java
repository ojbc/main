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
package org.ojbc.intermediaries.sn.migration;

import java.io.File;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.camel.Exchange;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.ojbc.intermediaries.sn.dao.SubscriptionSearchQueryDAO;
import org.ojbc.util.xml.subscription.Subscription;
import org.ojbc.util.xml.subscription.SubscriptionNotificationDocumentBuilderUtils;
import org.w3c.dom.Document;

public class SubscriptionMigrationProcessor {

	private static final Log log = LogFactory.getLog(SubscriptionMigrationProcessor.class);
	
	private Map<Integer, Subscription> subscriptionMap;
	
	private Map<String, String> emailAddressToORILookupMap;
	
	/**
	 * All records will be assigned to this ORI if set
	 */
	private String defaultSubscriptionMigrationOri;
	
	/**
	 * When loading agency profiles to the agency profile table, it will load for the state that is set here
	 */
	private String defaultAgencyProfileState;
	
	/**
	 * Path to CSV file containing the email address / ORI. Used to populate emailAddressToORILookupMap
	 */
	private String pathToOriLookupFile;
	
	private SubscriptionSearchQueryDAO subscriptionSearchQueryDAO;
	
	/**
	 * this is the prefix in the federation ID prior to the email address
	 */
	private String subscriptionOwnerPrefix;
	
	//This method will populate Agency Profile entries based on the ORI Universe File.csv
	public void processAgencyProfileORIEntry (String csvExtractLine) throws Exception
	{
		//If we see this column header, this is a column header row not data row											
		if (StringUtils.contains(csvExtractLine, "DATA_YEAR"))
		{
			log.info("Skipping header record");
			
			return;
		}	

		//populate agency map here if needed
		if (StringUtils.isNotBlank(pathToOriLookupFile))
		{
			if (emailAddressToORILookupMap == null)
			{	
				emailAddressToORILookupMap  = new HashMap<String, String>();
				
				@SuppressWarnings("unchecked")
				List<String> lines = FileUtils.readLines(new File(pathToOriLookupFile));
				
				for (String line : lines)
				{
					String[] entries = line.split(",");
					emailAddressToORILookupMap.put(entries[0], entries[1]);					
				}	
			}	
		}	
		
		
        String[] values = csvExtractLine.split(",");

        if (StringUtils.isNotBlank(defaultAgencyProfileState))
        {
        	String stateName = values[15];
        	
        	if (!defaultAgencyProfileState.equals(stateName))
        	{
        		log.info("Default agency name doesn't match entry: " + stateName);
        		
        		return;
        	}	
        }	
        
		String agencyOri = values[1];
		String agencyName = values[8];
		
		if (StringUtils.isBlank(agencyName))
		{
			agencyName= values[9];
		}	
		
		subscriptionSearchQueryDAO.saveAgencyProfileEntry(agencyOri, agencyName, false, true, false);
	}
	
	public Document enrichMessageWithHeaders(Exchange ex, Subscription subscription) throws Exception
	{
		ex.getIn().setHeader("subscriptionOwner", subscription.getOwnerFederationId());
		ex.getIn().setHeader("subscriptionOwnerEmailAddress", subscription.getOwnerEmailAddress());
		ex.getIn().setHeader("subscriptionOwnerFirstName", subscription.getOwnerFirstName());
		ex.getIn().setHeader("subscriptionOwnerLastName", subscription.getOwnerLastName());
		
		//We won't have ORI in all cases
		//Use it if we have it
		if (StringUtils.isNotBlank(subscription.getOri()))
		{
			ex.getIn().setHeader("subscriptionOwnerOri", subscription.getOri());	
		}	
		//if a default ORI is set, use that
		else if (StringUtils.isNotBlank(defaultSubscriptionMigrationOri))
		{
			ex.getIn().setHeader("subscriptionOwnerOri", defaultSubscriptionMigrationOri);
		} 
		//set the ORI based off a lookup
		else if (StringUtils.isNotBlank(subscription.getOwnerEmailAddress()))
		{
			//look up ORI here	
			if (emailAddressToORILookupMap.containsKey(subscription.getOwnerEmailAddress()))
			{
				ex.getIn().setHeader("subscriptionOwnerOri", emailAddressToORILookupMap.get(subscription.getOwnerEmailAddress()));
			}	
		}	
		
		
		Document ret = SubscriptionNotificationDocumentBuilderUtils.createSubscriptionRequest(subscription, null);
		
		return ret;

	}
	
	/**
	 * A query like this is used to produce the CSV file:
	 * 
	 * SELECT sub.*, nm.*, ssi.* from subscription sub, notification_mechanism nm, subscription_subject_identifier ssi 
	 *	where sub.id = nm.subscriptionId
	 *	and  sub.id =ssi.subscriptionId
	 *	and active = 1 and subscribingSystemIdentifier = '{http://ojbc.org/OJB_Portal/Subscriptions/1.0}OJB'
	 * 
	 * 
	 * @param csvExtractLine
	 * @throws Exception
	 */
	
	public void processSubscriptionCSVRecord (String csvExtractLine) throws Exception
	{
		//If we see this column header, this is a column header row not data row											
		if (StringUtils.contains(csvExtractLine, "subscribingSystemIdentifier"))
		{
			return;
		}	
		
		if (subscriptionMap == null)
		{
			subscriptionMap = new HashMap<Integer, Subscription>();
		}	
		
		log.debug("CSV extract entry line as string: " + csvExtractLine);
		
		if (StringUtils.isBlank(csvExtractLine))
		{
			throw new IllegalStateException("Extract entry is blank.");
		}	

        // use comma as separator
        String[] values = csvExtractLine.split(",");
        Integer subscriptionId = Integer.valueOf(values[0]);
        
        Subscription subscription = null;
        
        if (!subscriptionMap.containsKey(subscriptionId))
        {
        	subscription = new Subscription();
    		
            subscription.setActive(true);
            
            String subscriptionOwner = values[6];
            
//    		<!-- These headers are used when saving the subscription to the database -->
//    		<camel:setHeader headerName="subscriptionOwner"><simple>${in.headers.saml_FederationID}</simple></camel:setHeader>
            subscription.setOwnerFederationId(subscriptionOwner);
            
//    		<camel:setHeader headerName="subscriptionOwnerEmailAddress"><simple>${in.headers.saml_EmailAddress}</simple></camel:setHeader>

            String subscriptionOwnerEmailAddress = StringUtils.substringAfter(subscriptionOwner, subscriptionOwnerPrefix);
            subscription.setOwnerEmailAddress(subscriptionOwnerEmailAddress);
            
        	String[] subscriptionOwnerNameValues = StringUtils.substringBefore(subscriptionOwnerEmailAddress, "@").split("\\.");
            
        	if (subscriptionOwnerNameValues != null)
        	{	
        		subscription.setOwnerFirstName(subscriptionOwnerNameValues[0]);
        		
        		if (subscriptionOwnerNameValues.length == 2)
        		{
        			subscription.setOwnerLastName(subscriptionOwnerNameValues[1]);
        		}	
        		else if (subscriptionOwnerNameValues.length == 3)
        		{
        			subscription.setOwnerLastName(subscriptionOwnerNameValues[2]);
        		}	
        	}
        	
//    		<camel:setHeader headerName="subscriptionOwnerOri"><simple>${in.headers.saml_EmployerOri}</simple></camel:setHeader>
//    		We don't have ORI so we don't set it
        	
        	String startDateString = values[2];
        	
        	if (StringUtils.isNotBlank(startDateString))
        	{
	        	Date startDate=new SimpleDateFormat("yyyy-MM-dd").parse(startDateString);  
	        	subscription.setSubscriptionStartDate(startDate);
        	}

        	String endDateString = values[3];
        	
        	if (StringUtils.isNotBlank(endDateString) && !endDateString.equals("NULL"))
        	{
        		Date endDate=new SimpleDateFormat("yyyy-MM-dd").parse(endDateString);  
	        	subscription.setSubscriptionEndDate(endDate);
        	}
        	
        	subscription.setTopic(values[1]);
        	
        	String fullName = values[7];
        	
        	if (StringUtils.isNotBlank(fullName))
        	{
        		fullName = fullName.replaceAll("^\"|\"$", "");
        		subscription.setFullName(fullName);	
        		
        	}	

        	List<String> emailAddressesToNotify = new ArrayList<String>();
        	emailAddressesToNotify.add(values[15]);
        	subscription.setEmailList(emailAddressesToNotify);
        	
        	setSubjectIdentifierValues(values, subscription);
        	
            subscriptionMap.put(subscriptionId, subscription);

            
        }	
        else
        {
        	subscription = subscriptionMap.get(subscriptionId);
        	
        	setSubjectIdentifierValues(values, subscription);
        }	
        
	}

	private void setSubjectIdentifierValues(String[] values, Subscription subscription) throws ParseException {
		if (!subscription.getEmailList().contains(values[15]))
		{
			subscription.getEmailList().add(values[15]);
		}

		if (values[18].equals("firstName"))
		{	
			subscription.setFirstName(values[19]);
		}

		if (values[18].equals("lastName"))
		{	
			subscription.setLastName(values[19]);
		}

		if (values[18].equals("SID"))
		{	
			subscription.setStateId(values[19]);
		}

		if (values[18].equals("dateOfBirth"))
		{	
			Date dateOfBirth=new SimpleDateFormat("yyyy-MM-dd").parse(values[19]);
			subscription.setDateOfBirth(dateOfBirth);
		}
	}
	
	public List<Subscription> getCompleteSubscriptions()
	{
		List<Subscription> subscriptions = new ArrayList<Subscription>(subscriptionMap.values());
		return subscriptions;
	}
	
	public void clearSubscriptionMap()
	{
		if (subscriptionMap != null)
		{	
			subscriptionMap.clear();
		}
	}

	public Map<Integer, Subscription> getSubscriptionMap() {
		return subscriptionMap;
	}

	public void setSubscriptionMap(Map<Integer, Subscription> subscriptionMap) {
		this.subscriptionMap = subscriptionMap;
	}

	public String getDefaultSubscriptionMigrationOri() {
		return defaultSubscriptionMigrationOri;
	}

	public void setDefaultSubscriptionMigrationOri(String defaultSubscriptionMigrationOri) {
		this.defaultSubscriptionMigrationOri = defaultSubscriptionMigrationOri;
	}

	public String getDefaultAgencyProfileState() {
		return defaultAgencyProfileState;
	}

	public void setDefaultAgencyProfileState(String defaultAgencyProfileState) {
		this.defaultAgencyProfileState = defaultAgencyProfileState;
	}

	public SubscriptionSearchQueryDAO getSubscriptionSearchQueryDAO() {
		return subscriptionSearchQueryDAO;
	}

	public void setSubscriptionSearchQueryDAO(SubscriptionSearchQueryDAO subscriptionSearchQueryDAO) {
		this.subscriptionSearchQueryDAO = subscriptionSearchQueryDAO;
	}

	public Map<String, String> getEmailAddressToORILookupMap() {
		return emailAddressToORILookupMap;
	}

	public void setEmailAddressToORILookupMap(Map<String, String> emailAddressToORILookupMap) {
		this.emailAddressToORILookupMap = emailAddressToORILookupMap;
	}

	public String getPathToOriLookupFile() {
		return pathToOriLookupFile;
	}

	public void setPathToOriLookupFile(String pathToOriLookupFile) {
		this.pathToOriLookupFile = pathToOriLookupFile;
	}

	public String getSubscriptionOwnerPrefix() {
		return subscriptionOwnerPrefix;
	}

	public void setSubscriptionOwnerPrefix(String subscriptionOwnerPrefix) {
		this.subscriptionOwnerPrefix = subscriptionOwnerPrefix;
	}

}
