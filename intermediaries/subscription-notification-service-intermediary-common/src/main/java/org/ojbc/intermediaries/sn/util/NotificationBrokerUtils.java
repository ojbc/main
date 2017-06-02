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
package org.ojbc.intermediaries.sn.util;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.joda.time.DateTime;
import org.joda.time.LocalDate;
import org.joda.time.Years;
import org.joda.time.format.DateTimeFormat;
import org.ojbc.intermediaries.sn.notification.Offense;
import org.ojbc.util.xml.OjbcNamespaceContext;
import org.ojbc.util.xml.XmlUtils;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

/**
 * This class will hold static utility methods for this bundle.
 * 
 */
public class NotificationBrokerUtils {

	
	
	/**
	 * This method return a formatted date string in yyyy-MM-dd HH:mm:ss or yyyy-MM-dd format.
	 * 
	 * @param notificationEventDate - JodaTime date
	 * @param isNotificationEventDateInclusiveOfTime - indicates whether time was included in date or set as default by Joda
	 * @return
	 */
	public static String returnFormattedNotificationEventDate(DateTime notificationEventDate, boolean isNotificationEventDateInclusiveOfTime)
	{
		String ret = "";
		
		if (notificationEventDate != null)
		{
			if (isNotificationEventDateInclusiveOfTime)
			{
				ret = notificationEventDate.toString("yyyy-MM-dd HH:mm:ss");
			}	
			else
			{
				ret = notificationEventDate.toString("yyyy-MM-dd");
			}	
		}
		
		return ret;
	}

    public static final String getFullyQualifiedTopic(String topic) {
    	if (topic != null) {
    	    topic = topic.trim();
    	    if (!topic.startsWith("{")) {
    	        if (topic.contains(":")) {
    	            String[] topicQName = topic.split(":");
                    String topicPrefix = topicQName[0];
    	            String prefixUri = new OjbcNamespaceContext().getNamespaceURI(topicPrefix);
    	            if (prefixUri == null) {
    	                throw new IllegalArgumentException("Unknown topic prefix: " + topicPrefix);
    	            }
    	            topic = "{" + prefixUri + "}:" + topicQName[1];
    	        }
    	    }
    	}
        return topic;
    }
    
    /**
     * This method will return a list of Offense String that are used to create email notification text in a velocity template.
     * 
     * It is cleaner to do this in Java rather than velocity.
     * 
     * @param offenses
     * @return
     */
    public static List<String> getOffenseStrings(List<Offense> offenses) {

        // Add offense info
        List<String> ret = new ArrayList<String>(offenses.size());

        if (offenses != null) {
            for (Offense offense : offenses) {
                StringBuffer offenseString = new StringBuffer();
                if (StringUtils.isNotEmpty(offense.getFbiNdexCode()) || StringUtils.isNotBlank(offense.getOffenseCategoryText()) || StringUtils.isNotBlank(offense.getOffenseDescriptionText())) {
                    // If both are set, then the NDex code is displayed. Or If just Ndex code is set, only display ndex code
                    if ((StringUtils.isNotEmpty(offense.getFbiNdexCode()) && StringUtils.isNotEmpty(offense.getOffenseCategoryText())) || StringUtils.isNotEmpty(offense.getFbiNdexCode())) {
                        offenseString.append("Offense Code: " + offense.getFbiNdexCode() + "<br/>");
                    }

                    // If offense category is set but ndex is not set, display offense category
                    if (StringUtils.isNotEmpty(offense.getOffenseCategoryText()) && StringUtils.isEmpty(offense.getFbiNdexCode())) {
                        offenseString.append("Offense Code: " + offense.getOffenseCategoryText() + "<br/>");
                    }

                    if (StringUtils.isNotEmpty(offense.getOffenseDescriptionText())) {
                        offenseString.append("Offense Description: " + offense.getOffenseDescriptionText() + "<br/>");
                    }
                    
                    ret.add(offenseString.toString());

                }
            }
        }
        
        return Collections.unmodifiableList(ret);
        
    }
	
	public static List<Offense> returnOffenseNodes(NodeList offenseNodes) throws Exception {
		
		List<Offense> offenses = new ArrayList<Offense>();
		
		if (offenseNodes != null && offenseNodes.getLength() > 0) {
            for (int i = 0; i < offenseNodes.getLength(); i++) {
                if (offenseNodes.item(i).getNodeType() == Node.ELEMENT_NODE) {

                    Element offenseElement = (Element) offenseNodes.item(i);

                    Offense offense = new Offense();

                    offense.setFbiNdexCode(XmlUtils.xPathStringSearch(offenseElement, "notfm-ext:OffenseFBINDEXCode"));
                    offense.setOffenseDescriptionText(XmlUtils.xPathStringSearch(offenseElement, "nc:ActivityDescriptionText"));
                    offense.setOffenseCategoryText(XmlUtils.xPathStringSearch(offenseElement, "jxdm41:OffenseCategoryText"));

                    offenses.add(offense);

                }
            }
        }
		
		return offenses;
	}
	
	/**
	 * This method assumes date in format 'yyyy-MM-dd'
	 * 
	 * @param dateAsString
	 * @return
	 * @throws Exception
	 */
	public static String calculatePersonAgeFromDate(String dateAsString) throws Exception{
		
		String personAge="";
		
		if (StringUtils.isNotBlank(dateAsString)) {
	    	LocalDate personBirthDateLocalDate = DateTimeFormat.forPattern("yyyy-MM-dd").parseDateTime(dateAsString).toLocalDate();
	    	LocalDate now = new LocalDate();
	    	Years age = Years.yearsBetween(personBirthDateLocalDate, now);
	    	personAge = String.valueOf(age.getYears());
		}
		
		return personAge;
	}	
}