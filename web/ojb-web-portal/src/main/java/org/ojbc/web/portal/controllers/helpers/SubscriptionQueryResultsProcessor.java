package org.ojbc.web.portal.controllers.helpers;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.commons.lang.StringUtils;
import org.ojbc.util.xml.XmlUtils;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class SubscriptionQueryResultsProcessor {
	
	private static final SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");	
	
	
	/**
	 * TODO see if null checks should be added
	 * TODO get fname, lname, dob
	 */
	public SubscriptionQueryResults parseSubscriptionQueryResults(Document subscriptionQueryResponseDoc) throws Exception{
		
		SubscriptionQueryResults subQueryResults = new SubscriptionQueryResults();
		
		Node rootSubQueryResultsNode = XmlUtils.xPathNodeSearch(subscriptionQueryResponseDoc, "sqr:SubscriptionQueryResults");
							
		Node subQueryResultNode = XmlUtils.xPathNodeSearch(rootSubQueryResultsNode, "sqr-ext:SubscriptionQueryResult");
		parseSubscriptionQueryResultNode(subQueryResultNode, subQueryResults);	
						
		Node personNode = XmlUtils.xPathNodeSearch(rootSubQueryResultsNode, "sqr-ext:Person");
		parsePersonNode(personNode, subQueryResults);

		parseContactInfoNode(rootSubQueryResultsNode, subQueryResults);
		
		return subQueryResults;
	}	
	
	
	private void parseSubscriptionQueryResultNode(Node subQueryResultNode, 
			SubscriptionQueryResults subQueryResults) throws Exception{
					
		Node subscriptionNode = XmlUtils.xPathNodeSearch(subQueryResultNode, "sqr-ext:Subscription");
		
		Node dateRangeNode = XmlUtils.xPathNodeSearch(subscriptionNode, "nc:ActivityDateRange");		
		parseDateNode(dateRangeNode, subQueryResults);				
				
		String topic = XmlUtils.xPathStringSearch(subscriptionNode, "wsn-br:Topic");
		subQueryResults.setSubscriptionType(topic.trim());
		
		String systemId = XmlUtils.xPathStringSearch(subQueryResultNode, "intel:SystemIdentifier/nc:IdentificationID");
		subQueryResults.setSystemId(systemId);		
	}	
	
	private void parseDateNode(Node dateRangeNode, SubscriptionQueryResults subQueryResults) throws Exception{				

		String sStartDate = XmlUtils.xPathStringSearch(dateRangeNode, "nc:StartDate/nc:Date");			
		if(StringUtils.isNotEmpty(sStartDate)){
			Date dStartDate = sdf.parse(sStartDate.trim());		
			subQueryResults.setSubscriptionStartDate(dStartDate);			
		}		
		
		String sEndDate = XmlUtils.xPathStringSearch(dateRangeNode, "nc:EndDate");		
		if(StringUtils.isNotEmpty(sEndDate)){
			Date dEndDate = sdf.parse(sEndDate.trim());
			subQueryResults.setSubscriptionEndDate(dEndDate);			
		}
	}
	
	
	private void parsePersonNode(Node personNode, SubscriptionQueryResults subQueryResults) throws Exception{
		
//		   <sqr-ext:Person s:id="P0">
//		      <nc:PersonBirthDate>
//		         <nc:Date>2014-05-15</nc:Date>
//		      </nc:PersonBirthDate>
//		      <nc:PersonName/
		      
		String sDob = XmlUtils.xPathStringSearch(personNode, "nc:PersonBirthDate/nc:Date");
		if(StringUtils.isNotBlank(sDob)){
			Date dDob = sdf.parse(sDob);
			subQueryResults.setDateOfBirth(dDob);			
		}
		      
		String sFullName = XmlUtils.xPathStringSearch(personNode, "nc:PersonName/nc:PersonFullName");
		subQueryResults.setFullName(sFullName.trim());
		
		String sFirstName = XmlUtils.xPathStringSearch(personNode, "nc:PersonName/nc:PersonGivenName");
		subQueryResults.setFirstName(sFirstName);
		
		String sLastName = XmlUtils.xPathStringSearch(personNode, "nc:PersonName/nc:PersonSurName");
		subQueryResults.setLastName(sLastName);
				
		String sid = XmlUtils.xPathStringSearch(personNode, 
				"jxdm41:PersonAugmentation/jxdm41:PersonStateFingerprintIdentification/nc:IdentificationID");		
		subQueryResults.setStateId(sid.trim());		
	}
	
	
	private void parseContactInfoNode(Node rootSubQueryResultsNode, SubscriptionQueryResults subQueryResults) throws Exception{
		
		NodeList contactInfoNodeList = XmlUtils.xPathNodeListSearch(rootSubQueryResultsNode, "nc:ContactInformation");
		
		for(int i=0; i<contactInfoNodeList.getLength(); i++){
			
			Node iContactInfoNode = contactInfoNodeList.item(i);			
			String iEmail = XmlUtils.xPathStringSearch(iContactInfoNode, "nc:ContactEmailID");		
			
			if(StringUtils.isNotBlank(iEmail)){
				subQueryResults.getEmailList().add(iEmail.trim());			
			}							
		}		
	}

}


