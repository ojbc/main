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
package org.ojbc.web.portal.controllers.helpers;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.commons.lang.StringUtils;
import org.ojbc.util.xml.XmlUtils;
import org.ojbc.util.xml.subscription.Subscription;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class SubscriptionQueryResultsProcessor {
	
	private static final SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");	
	
	public Subscription parseSubscriptionQueryResults(Document subscriptionQueryResponseDoc) throws Exception{
		
		Subscription subscription = new Subscription();
		
		Node rootSubQueryResultsNode = XmlUtils.xPathNodeSearch(subscriptionQueryResponseDoc, "sqr:SubscriptionQueryResults");
							
		Node subQueryResultNode = XmlUtils.xPathNodeSearch(rootSubQueryResultsNode, "sqr-ext:SubscriptionQueryResult");
		parseSubscriptionQueryResultNode(subQueryResultNode, subscription);	
						
		Node personNode = XmlUtils.xPathNodeSearch(rootSubQueryResultsNode, "sqr-ext:Person");
		parsePersonNode(personNode, subscription);

		parseContactInfoNode(rootSubQueryResultsNode, subscription);
		
		return subscription;
	}		
	
	private void parseSubscriptionQueryResultNode(Node subQueryResultNode, 
			Subscription subscription) throws Exception{
					
		Node subscriptionNode = XmlUtils.xPathNodeSearch(subQueryResultNode, "sqr-ext:Subscription");
		
		Node dateRangeNode = XmlUtils.xPathNodeSearch(subscriptionNode, "nc:ActivityDateRange");		
		parseDateNode(dateRangeNode, subscription);				
				
		String topic = XmlUtils.xPathStringSearch(subscriptionNode, "wsn-br:Topic");
		subscription.setTopic(topic.trim());
		
		String systemId = XmlUtils.xPathStringSearch(subQueryResultNode, "intel:SystemIdentifier/nc:IdentificationID");
		subscription.setSystemId(systemId);		
	}	
	
	private void parseDateNode(Node dateRangeNode, Subscription subscription) throws Exception{				

		String sStartDate = XmlUtils.xPathStringSearch(dateRangeNode, "nc:StartDate/nc:Date");			
		if(StringUtils.isNotEmpty(sStartDate)){
			Date dStartDate = sdf.parse(sStartDate.trim());		
			subscription.setSubscriptionStartDate(dStartDate);			
		}		
		
		String sEndDate = XmlUtils.xPathStringSearch(dateRangeNode, "nc:EndDate");		
		if(StringUtils.isNotEmpty(sEndDate)){
			Date dEndDate = sdf.parse(sEndDate.trim());
			subscription.setSubscriptionEndDate(dEndDate);			
		}
	}
	
	
	private void parsePersonNode(Node personNode, Subscription subscription) throws Exception{		
		      
		String sDob = XmlUtils.xPathStringSearch(personNode, "nc:PersonBirthDate/nc:Date");
		if(StringUtils.isNotBlank(sDob)){
			Date dDob = sdf.parse(sDob);
			subscription.setDateOfBirth(dDob);			
		}
		      
		String sFullName = XmlUtils.xPathStringSearch(personNode, "nc:PersonName/nc:PersonFullName");
		subscription.setFullName(sFullName.trim());
		
		String sFirstName = XmlUtils.xPathStringSearch(personNode, "nc:PersonName/nc:PersonGivenName");
		subscription.setFirstName(sFirstName);
		
		String sLastName = XmlUtils.xPathStringSearch(personNode, "nc:PersonName/nc:PersonSurName");
		subscription.setLastName(sLastName);
				
		String sid = XmlUtils.xPathStringSearch(personNode, 
				"jxdm41:PersonAugmentation/jxdm41:PersonStateFingerprintIdentification/nc:IdentificationID");
		
		if (StringUtils.isNotBlank(sid))
		{	
			subscription.setStateId(sid.trim());
		}	
	}
	
	
	private void parseContactInfoNode(Node rootSubQueryResultsNode, Subscription subscription) throws Exception{
		
		NodeList contactInfoNodeList = XmlUtils.xPathNodeListSearch(rootSubQueryResultsNode, "nc:ContactInformation");
		
		for(int i=0; i<contactInfoNodeList.getLength(); i++){
			
			Node iContactInfoNode = contactInfoNodeList.item(i);			
			String iEmail = XmlUtils.xPathStringSearch(iContactInfoNode, "nc:ContactEmailID");		
			
			if(StringUtils.isNotBlank(iEmail)){
				subscription.getEmailList().add(iEmail.trim());			
			}							
		}		
	}

}


