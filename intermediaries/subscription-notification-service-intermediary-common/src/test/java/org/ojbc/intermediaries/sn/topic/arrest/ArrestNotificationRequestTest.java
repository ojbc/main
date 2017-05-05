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
 * Copyright 2012-2015 Open Justice Broker Consortium
 */
package org.ojbc.intermediaries.sn.topic.arrest;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThat;

import java.util.List;

import org.ojbc.intermediaries.sn.SubscriptionNotificationConstants;
import org.ojbc.intermediaries.sn.notification.NotificationRequest.Alias;
import org.ojbc.intermediaries.sn.testutil.TestNotificationBuilderUtil;
import org.ojbc.intermediaries.sn.util.NotificationBrokerUtils;
import org.apache.camel.Message;
import org.apache.commons.lang.StringUtils;
import org.junit.Test;
import org.mockito.Mockito;
import org.w3c.dom.Document;

public class ArrestNotificationRequestTest {
	
	@Test
	public void test() throws Exception {
		Message message = Mockito.mock(Message.class);
		
		Mockito.when(message.getBody(Document.class)).thenReturn(TestNotificationBuilderUtil.getMessageBody("src/test/resources/xmlInstances/notificationSoapRequest.xml"));
		
		ArrestNotificationRequest request = new ArrestNotificationRequest(message);
		
		assertThat(request.isNotificationEventDateInclusiveOfTime(), is(false));
		assertThat(request.getNotificationEventDate().toString("yyyy-MM-dd"), is("2013-09-06"));
		assertThat(request.getPersonFirstName(), is("Homer"));
		assertNull(request.getPersonMiddleName());
		assertThat(request.getPersonLastName(), is("Simpson"));
		assertNull(request.getPersonNameSuffix());
		assertThat(request.getNotifyingAgencyName(), is("Honolulu PD"));
		assertNull(request.getNotificationEventIdentifier());
		assertThat(request.getAttorneyGeneralIndicator(), is("false"));
		assertThat(request.getSubjectIdentifiers().size(), is(4));
		assertThat(request.getSubjectIdentifiers().get(SubscriptionNotificationConstants.SID), is("A9999999"));
		assertThat(request.getSubjectIdentifiers().get(SubscriptionNotificationConstants.FIRST_NAME), is("Homer"));
		assertThat(request.getSubjectIdentifiers().get(SubscriptionNotificationConstants.LAST_NAME), is("Simpson"));
		assertThat(request.getSubjectIdentifiers().get(SubscriptionNotificationConstants.DATE_OF_BIRTH), is("1955-01-15"));
		
		assertThat(request.getTopic(), is("{http://ojbc.org/wsn/topics}:person/arrest"));
		assertThat(request.getPersonBookingName(), is("Simpson, Homer"));
		assertThat(request.getBookingDateTimeDisplay(), is("2013-09-06"));
		
        assertEquals(2013, request.getNotificationEventDate().getYear());
        assertEquals(9, request.getNotificationEventDate().getMonthOfYear());
        assertEquals(6, request.getNotificationEventDate().getDayOfMonth());
        
        List<String> arrestChargeStrings = request.getArrestCharges();
        assertEquals(1, arrestChargeStrings.size());
        assertEquals("Description: Assault Severity: very severe, ARN: I-04679", arrestChargeStrings.get(0));
		
	}

	@Test
	public void testArrestWithIncidentInfo() throws Exception {
		Message message = Mockito.mock(Message.class);
		
		Mockito.when(message.getBody(Document.class)).thenReturn(TestNotificationBuilderUtil.getMessageBody("src/test/resources/xmlInstances/notificationMessage-arrestWithIncidentInfo.xml"));
		
		ArrestNotificationRequest request = new ArrestNotificationRequest(message);
		
		assertThat(request.isNotificationEventDateInclusiveOfTime(), is(true));
		assertThat(request.getNotificationEventDate().toString("yyyy-MM-dd HH:mm:ss"), is("2014-01-01 08:06:02"));
		assertThat(request.getPersonFirstName(), is("Mark"));
		assertNull(request.getPersonMiddleName());
		assertThat(request.getPersonLastName(), is("Smith"));
		assertNull(request.getPersonNameSuffix());
		assertThat(request.getPersonBirthDate(), is("1976-09-03"));
		assertThat(request.getPersonAge(), is(NotificationBrokerUtils.calculatePersonAgeFromDate(request.getPersonBirthDate())));
		assertThat(request.getNotifyingAgencyName(), is("Burlington Police Department"));
		assertNull(request.getNotificationEventIdentifier());
		assertNull(request.getAttorneyGeneralIndicator());
		assertThat(request.getSubjectIdentifiers().size(), is(4));
		assertNull(request.getSubjectIdentifiers().get("SID"));
		assertThat(request.getSubjectIdentifiers().get(SubscriptionNotificationConstants.FIRST_NAME), is("Mark"));
		assertThat(request.getSubjectIdentifiers().get(SubscriptionNotificationConstants.LAST_NAME), is("Smith"));
		assertThat(request.getSubjectIdentifiers().get(SubscriptionNotificationConstants.DATE_OF_BIRTH), is("1976-09-03"));

		
		assertThat(request.getTopic(), is("{http://ojbc.org/wsn/topics}:person/arrest"));
		assertThat(request.getPersonBookingName(), is("Smith, Mark"));
		assertThat(request.getBookingDateTimeDisplay(), is("2014-01-01 08:06:02"));
		
        assertEquals(2014, request.getNotificationEventDate().getYear());
        assertEquals(1, request.getNotificationEventDate().getMonthOfYear());
        assertEquals(1, request.getNotificationEventDate().getDayOfMonth());
        
        assertThat(request.getIncidentId(), is("14BU000056"));
        assertThat(request.getIncidentDate(), is("2014-01-01T04:43:56Z"));
        
        assertThat(request.getIncidentLocationCity(), is("Burlington"));
        assertThat(request.getIncidentLocationState(), is("VT"));
        assertThat(request.getIncidentLocationStreetFullText(), is("1 Woodlawn Rd North Ave"));
        assertThat(request.getInicidentLocationCompleteAddress(), is("1 Woodlawn Rd North Ave Burlington VT"));
        
        assertThat(request.getArresteeLastKnownAddressCity(), is("Arrestee City"));
        assertThat(request.getArresteeLastKnownAddressState(), is("VT"));
        assertThat(request.getArresteeLastKnownAddressStreetNumber(), is("600"));
        assertThat(request.getArresteeLastKnownAddressStreet(), is("Arrestee Rd"));
        
        assertThat(request.getArresteeLastKnownAddressStreetFullText(), is("600 Arrestee Rd"));
        assertThat(request.getArresteeLastKnownAddressCompleteAddress(), is("600 Arrestee Rd Arrestee City VT"));
        
        List<String> arrestOffenses = request.getOffenseStrings();
        assertEquals(2, arrestOffenses.size());
        
        assertEquals("Offense Code: Driving Under Influence<br/>Offense Description: Driving Under The Influence, First Offense 23 VSA 1201 90D<br/>", arrestOffenses.get(0));
        assertEquals("Offense Code: Robbery<br/>Offense Description: Robbery<br/>", arrestOffenses.get(1));
        
        List<String> officerNames = request.getOfficerNames();
        assertEquals(2, officerNames.size());
        assertEquals("Officer 1 full name", officerNames.get(0));
        assertEquals("Officer 2 full name", officerNames.get(1));

        List<Alias> aliases = request.getAliases();
        assertEquals(2, aliases.size());

        assertEquals("Mark", aliases.get(0).getPersonFirstName());
        assertEquals("Smitty", aliases.get(0).getPersonLastName());

        assertEquals("Marc", aliases.get(1).getPersonFirstName());
        assertEquals("Smith", aliases.get(1).getPersonLastName());

        List<String> arrestChargeStrings = request.getArrestCharges();
        assertEquals(0, arrestChargeStrings.size());
        
        List<String> personTelephoneNumbers = request.getPersonTelephoneNumbers();
        assertEquals(2, personTelephoneNumbers.size());
        assertEquals("8081234567", personTelephoneNumbers.get(0));
        assertEquals("9999999999", personTelephoneNumbers.get(1));
		
	}

	@Test
	public void testArrestWithIncidentInfoNoAddressInfo() throws Exception {
		Message message = Mockito.mock(Message.class);
		
		Mockito.when(message.getBody(Document.class)).thenReturn(TestNotificationBuilderUtil.getMessageBody("src/test/resources/xmlInstances/notificationMessage-arrestWithIncidentInfoWithNoAddressInfo.xml"));
		
		ArrestNotificationRequest request = new ArrestNotificationRequest(message);
		
		assertThat(request.isNotificationEventDateInclusiveOfTime(), is(true));
		assertThat(request.getNotificationEventDate().toString("yyyy-MM-dd HH:mm:ss"), is("2014-01-01 08:06:02"));
		assertThat(request.getPersonFirstName(), is("Mark"));
		assertNull(request.getPersonMiddleName());
		assertThat(request.getPersonLastName(), is("Smith"));
		assertNull(request.getPersonNameSuffix());
		assertThat(request.getPersonBirthDate(), is("1976-09-03"));
		assertThat(request.getPersonAge(), is(NotificationBrokerUtils.calculatePersonAgeFromDate(request.getPersonBirthDate())));
		assertThat(request.getNotifyingAgencyName(), is("Burlington Police Department"));
		assertNull(request.getNotificationEventIdentifier());
		assertNull(request.getAttorneyGeneralIndicator());
		assertThat(request.getSubjectIdentifiers().size(), is(4));
		assertNull(request.getSubjectIdentifiers().get("SID"));
		assertThat(request.getSubjectIdentifiers().get(SubscriptionNotificationConstants.FIRST_NAME), is("Mark"));
		assertThat(request.getSubjectIdentifiers().get(SubscriptionNotificationConstants.LAST_NAME), is("Smith"));
		assertThat(request.getSubjectIdentifiers().get(SubscriptionNotificationConstants.DATE_OF_BIRTH), is("1976-09-03"));
		
		assertThat(request.getTopic(), is("{http://ojbc.org/wsn/topics}:person/arrest"));
		assertThat(request.getPersonBookingName(), is("Smith, Mark"));
		assertThat(request.getBookingDateTimeDisplay(), is("2014-01-01 08:06:02"));
		
        assertEquals(2014, request.getNotificationEventDate().getYear());
        assertEquals(1, request.getNotificationEventDate().getMonthOfYear());
        assertEquals(1, request.getNotificationEventDate().getDayOfMonth());
        
        assertThat(request.getIncidentId(), is("14BU000056"));
        assertThat(request.getIncidentDate(), is("2014-01-01T04:43:56Z"));
        
        assertNull(request.getIncidentLocationCity());
        assertNull(request.getIncidentLocationState());
        assertThat(request.getIncidentLocationStreetFullText(), is(""));
        
        assertNull(request.getArresteeLastKnownAddressCity());
        assertNull(request.getArresteeLastKnownAddressState());
        assertNull(request.getArresteeLastKnownAddressStreetNumber());
        assertNull(request.getArresteeLastKnownAddressStreet());
        
        assertThat(request.getArresteeLastKnownAddressStreetFullText(), is(""));

        List<String> arrestOffenses = request.getOffenseStrings();
        assertEquals(2, arrestOffenses.size());
        
        assertEquals("Offense Code: Driving Under Influence<br/>Offense Description: Driving Under The Influence, First Offense 23 VSA 1201 90D<br/>", arrestOffenses.get(0));
        assertEquals("Offense Code: Robbery<br/>Offense Description: Robbery<br/>", arrestOffenses.get(1));
        
        List<String> officerNames = request.getOfficerNames();
        assertEquals(2, officerNames.size());
        assertEquals("Officer 1 full name", officerNames.get(0));
        assertEquals("Officer 2 full name", officerNames.get(1));

        List<Alias> aliases = request.getAliases();
        assertEquals(2, aliases.size());

        assertEquals("Mark", aliases.get(0).getPersonFirstName());
        assertEquals("Smitty", aliases.get(0).getPersonLastName());

        assertEquals("Marc", aliases.get(1).getPersonFirstName());
        assertEquals("Smith", aliases.get(1).getPersonLastName());

        List<String> arrestChargeStrings = request.getArrestCharges();
        assertEquals(0, arrestChargeStrings.size());
        
        List<String> personTelephoneNumbers = request.getPersonTelephoneNumbers();
        assertEquals(0, personTelephoneNumbers.size());
		
	}
}
