package org.ojbc.intermediaries.sn.util;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.ojbc.intermediaries.sn.notification.Offense;
import org.ojbc.util.xml.OjbcNamespaceContext;
import org.ojbc.util.xml.XmlUtils;

import org.apache.commons.lang.StringUtils;
import org.joda.time.DateTime;
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
    
    public static List<String> getOffenseStrings(List<Offense> offenses) {

        // Add offense info
        List<String> ret = new ArrayList<String>(offenses.size());

        if (offenses != null) {
            for (Offense offense : offenses) {
                StringBuffer emailBody = new StringBuffer();
                if (StringUtils.isNotEmpty(offense.getFbiNdexCode()) || StringUtils.isNotBlank(offense.getOffenseCategoryText()) || StringUtils.isNotBlank(offense.getOffenseDescriptionText())) {
                    // If both are set, then the NDex code is displayed. Or If just Ndex code is set, only display ndex code
                    if ((StringUtils.isNotEmpty(offense.getFbiNdexCode()) && StringUtils.isNotEmpty(offense.getOffenseCategoryText())) || StringUtils.isNotEmpty(offense.getFbiNdexCode())) {
                        emailBody.append("Offense Code: " + offense.getFbiNdexCode() + ", ");
                    }

                    // If offense category is set but ndex is not set, display offense category
                    if (StringUtils.isNotEmpty(offense.getOffenseCategoryText()) && StringUtils.isEmpty(offense.getFbiNdexCode())) {
                        emailBody.append("Offense Code: " + offense.getOffenseCategoryText() + ", ");
                    }

                    if (StringUtils.isNotEmpty(offense.getOffenseDescriptionText())) {
                        emailBody.append("Offense Description: " + offense.getOffenseDescriptionText() + ", ");
                    }

                    emailBody.delete(emailBody.length() - 2, emailBody.length());
                    ret.add(emailBody.toString());

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
}
