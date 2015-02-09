package org.ojbc.intermediaries.sn.util;

import static junit.framework.Assert.assertEquals;

import org.ojbc.util.xml.XmlUtils;

import org.joda.time.DateTime;
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
    
}
