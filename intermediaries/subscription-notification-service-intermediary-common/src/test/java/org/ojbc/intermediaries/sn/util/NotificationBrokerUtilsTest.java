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

import static junit.framework.Assert.assertEquals;

import java.util.ArrayList;
import java.util.List;

import org.ojbc.intermediaries.sn.notification.Offense;
import org.ojbc.util.xml.XmlUtils;

import org.joda.time.DateTime;
import org.joda.time.LocalDate;
import org.junit.Test;

public class NotificationBrokerUtilsTest {

	@Test
	public void testReturnFormattedNotificationEventDate() throws Exception
	{
		assertEquals("", NotificationBrokerUtils.returnFormattedNotificationEventDate(null, true));
		
		DateTime dateTime = XmlUtils.parseXmlDateTime("2014-01-10T12:11:10");
		assertEquals("2014-01-10 12:11:10", NotificationBrokerUtils.returnFormattedNotificationEventDate(dateTime, true));
		
		dateTime = XmlUtils.parseXmlDate("2014-01-10");
		assertEquals("2014-01-10", NotificationBrokerUtils.returnFormattedNotificationEventDate(dateTime, false));
	}
	
    @Test
    public void testFullyQualifiedTopic() {
        assertEquals("{http://ojbc.org/wsn/topics}:person/arrest", NotificationBrokerUtils.getFullyQualifiedTopic("topics:person/arrest"));
    }
    
    @Test(expected=IllegalArgumentException.class)
    public void testFullyQualifiedTopic_unknownPrefix() {
        NotificationBrokerUtils.getFullyQualifiedTopic("unknownPrefix:person/arrest");
    }
    

    @Test
    public void testGetOffenseStrings() throws Exception
    {
    	Offense offense1 = new Offense();
    	Offense offense2 = new Offense();
    	Offense offense3 = new Offense();
    
    	offense1.setOffenseDescriptionText("Offense 1 Text");
    	offense2.setOffenseDescriptionText("Offense 2 Text");
    	offense3.setOffenseDescriptionText("Offense 3 Text");
    	
    	offense1.setFbiNdexCode("Offense 1 - NDEx Code");
    	offense2.setOffenseCategoryText("Offense 2 - Category Text");
    	
    	List<Offense> offenses = new ArrayList<Offense>();
    	
    	offenses.add(offense1);
    	offenses.add(offense2);
    	offenses.add(offense3);
    	
		List<String> offenseStrings = NotificationBrokerUtils.getOffenseStrings(offenses );
		
		System.out.println(offenseStrings);
		
		assertEquals("Offense Code: Offense 1 - NDEx Code<br/>Offense Description: Offense 1 Text<br/>", offenseStrings.get(0));
		assertEquals("Offense Code: Offense 2 - Category Text<br/>Offense Description: Offense 2 Text<br/>", offenseStrings.get(1));
		assertEquals("Offense Description: Offense 3 Text<br/>", offenseStrings.get(2));
    	
    }
    
    @Test
    public void testCalculateAgeFromDate() throws Exception
    {
    	assertEquals("",NotificationBrokerUtils.calculatePersonAgeFromDate(null));
    	assertEquals("",NotificationBrokerUtils.calculatePersonAgeFromDate(""));
    	
    	LocalDate now = new LocalDate();
    	
    	assertEquals("0",NotificationBrokerUtils.calculatePersonAgeFromDate(now.toString("yyyy-MM-dd")));
    	
    	//Day of 30th Birthday
    	assertEquals("30",NotificationBrokerUtils.calculatePersonAgeFromDate(now.minusYears(30).toString("yyyy-MM-dd")));
    	
    	//Day before 30th birthday
    	assertEquals("29",NotificationBrokerUtils.calculatePersonAgeFromDate(now.minusYears(30).plusDays(1).toString("yyyy-MM-dd")));
    	
    	//Day after 30th birthday
    	assertEquals("30",NotificationBrokerUtils.calculatePersonAgeFromDate(now.minusYears(30).minusDays(1).toString("yyyy-MM-dd")));
    	
    }
    
}
