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
package org.ojbc.intermediaries.sn.notification;

import static org.junit.Assert.assertEquals;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.ojbc.intermediaries.sn.notification.VelocityTemplateEmailFormatter.EmailTemplate;
import org.ojbc.intermediaries.sn.topic.arrest.ArrestNotificationRequest;
import org.ojbc.intermediaries.sn.topic.incident.IncidentNotificationRequest;
import org.ojbc.util.xml.XmlUtils;
import org.junit.Before;
import org.junit.Test;
import org.w3c.dom.Document;
import org.w3c.dom.Node;

public class VelocityTemplateEmailFormatterTest {

    private VelocityTemplateEmailFormatter formatter;

    @Before
    public void setUp() {

        formatter = new VelocityTemplateEmailFormatter();

        VelocityTemplateEmailFormatter.EmailTemplate defaultTemplate = new VelocityTemplateEmailFormatter.EmailTemplate();
        defaultTemplate.setEmailSubjectTemplate("Default notification subject is $emailNotification.subjectName");
        defaultTemplate.setEmailBodyTemplate("Default email body:\nSomething from email notification: $emailNotification.subjectName\n"
                + "Something from notification request: $emailNotification.notificationRequest.personLastName");
        formatter.setDefaultEmailTemplate(defaultTemplate);

        Map<String, Map<NotificationFormatKey, VelocityTemplateEmailFormatter.EmailTemplate>> topicSystemTemplateMap = new HashMap<String, Map<NotificationFormatKey, VelocityTemplateEmailFormatter.EmailTemplate>>();

        Map<NotificationFormatKey, EmailTemplate> arrestMap = new HashMap<NotificationFormatKey, VelocityTemplateEmailFormatter.EmailTemplate>();
        
        VelocityTemplateEmailFormatter.EmailTemplate template = new VelocityTemplateEmailFormatter.EmailTemplate();
        template.setEmailSubjectTemplate("Notification subject is $emailNotification.subjectName for arrest, parole, subject");
        template.setEmailBodyTemplate("Notification body is $emailNotification.subjectName for arrest, parole, body.  Subject identifer is: $emailNotification.subscriptionSubjectIdentifiers.get('subscriptionQualifier')");
        
        NotificationFormatKey emailNotificationIdentifierKeyWrapperA = new NotificationFormatKey();
        emailNotificationIdentifierKeyWrapperA.setSubscribingSystemName("{http://demostate.gov/SystemNames/1.0}SystemA");
        emailNotificationIdentifierKeyWrapperA.setSubscriptionCategoryCode("default");
        
        arrestMap.put(emailNotificationIdentifierKeyWrapperA, template);

        //Non Criminal Justice
        template = new VelocityTemplateEmailFormatter.EmailTemplate();
        template.setEmailSubjectTemplate("Notification subject is $emailNotification.subjectName for non criminal justice");
        template.setEmailBodyTemplate("Notification body is $emailNotification.subjectName for non criminal justice");
        
        NotificationFormatKey emailNotificationIdentifierKeyWrapperNonCJ = new NotificationFormatKey();
        emailNotificationIdentifierKeyWrapperNonCJ.setSubscribingSystemName("{http://demostate.gov/SystemNames/1.0}SystemA");
        emailNotificationIdentifierKeyWrapperNonCJ.setSubscriptionCategoryCode("I");
        
        arrestMap.put(emailNotificationIdentifierKeyWrapperNonCJ, template);
        
        template = new VelocityTemplateEmailFormatter.EmailTemplate();
        template.setEmailSubjectTemplate("Notification subject is $emailNotification.subjectName for arrest, probation, subject");
        template.setEmailBodyTemplate("Notification body is $emailNotification.subjectName for arrest, probation, body");
        
        NotificationFormatKey emailNotificationIdentifierKeyWrapperB = new NotificationFormatKey();
        emailNotificationIdentifierKeyWrapperB.setSubscribingSystemName("{http://demostate.gov/SystemNames/1.0}SystemB");
        emailNotificationIdentifierKeyWrapperB.setSubscriptionCategoryCode("default");
        
        arrestMap.put(emailNotificationIdentifierKeyWrapperB, template);

        template = new VelocityTemplateEmailFormatter.EmailTemplate();
        template.setEmailSubjectTemplate("Notification subject is $emailNotification.subjectName for arrest, default arrest system, subject");
        template.setEmailBodyTemplate("Notification body is $emailNotification.subjectName for arrest, default arrest system, body");
        
        NotificationFormatKey emailNotificationIdentifierKeyWrapperDefault = new NotificationFormatKey();
        emailNotificationIdentifierKeyWrapperDefault.setSubscribingSystemName("{http://ojbc.org/OJB/Subscriptions/1.0}DefaultSystem");
        emailNotificationIdentifierKeyWrapperDefault.setSubscriptionCategoryCode("default");
        
        arrestMap.put(emailNotificationIdentifierKeyWrapperDefault, template);

        topicSystemTemplateMap.put("{http://ojbc.org/wsn/topics}:person/arrest", arrestMap);

        Map<NotificationFormatKey, EmailTemplate> incidentMap = new HashMap<NotificationFormatKey, VelocityTemplateEmailFormatter.EmailTemplate>();

        template = new VelocityTemplateEmailFormatter.EmailTemplate();
        template.setEmailSubjectTemplate("Notification subject is $emailNotification.subjectName for incident, parole, subject");
        template.setEmailBodyTemplate("Notification body is $emailNotification.subjectName for incident, parole, body");
        
        NotificationFormatKey emailNotificationIdentifierKeyWrapperAIncident = new NotificationFormatKey();
        emailNotificationIdentifierKeyWrapperAIncident.setSubscribingSystemName("{http://demostate.gov/SystemNames/1.0}SystemA");
        emailNotificationIdentifierKeyWrapperAIncident.setSubscriptionCategoryCode("default");
        
        incidentMap.put(emailNotificationIdentifierKeyWrapperAIncident, template);

        template = new VelocityTemplateEmailFormatter.EmailTemplate();
        template.setEmailSubjectTemplate("Notification subject is $emailNotification.subjectName for incident, probation, subject");
        template.setEmailBodyTemplate("Notification body is $emailNotification.subjectName for incident, probation, body");
        
        NotificationFormatKey emailNotificationIdentifierKeyWrapperBIncident = new NotificationFormatKey();
        emailNotificationIdentifierKeyWrapperBIncident.setSubscribingSystemName("{http://demostate.gov/SystemNames/1.0}SystemB");
        emailNotificationIdentifierKeyWrapperBIncident.setSubscriptionCategoryCode("default");
        
        incidentMap.put(emailNotificationIdentifierKeyWrapperBIncident, template);

        topicSystemTemplateMap.put("{http://ojbc.org/wsn/topics}:person/incident", incidentMap);

        formatter.setTopicSystemTemplateMap(topicSystemTemplateMap);

    }

    @Test
    public void testDefaultTemplates() throws Exception {
        // this scenario tests an unknown topic...you get the default template then
        EmailNotification email = buildArrestEmailNotification(getUnknownTopicParoleSystemNotificationMessage(), "{http://demostate.gov/SystemNames/1.0}SystemA", "default");
        String emailSubject = formatter.getEmailSubject(email);
        assertEquals("Default notification subject is offenderName", emailSubject);
        String emailBody = formatter.getEmailBody(email);
        assertEquals("Default email body:\nSomething from email notification: offenderName\nSomething from notification request: Simpson", emailBody);
    }

    @Test
    public void testArrestParoleTemplates() throws Exception {
        EmailNotification email = buildArrestEmailNotification(getArrestTopicParoleSystemNotificationMessage(), "{http://demostate.gov/SystemNames/1.0}SystemA", "default");
        String emailSubject = formatter.getEmailSubject(email);
        assertEquals("Notification subject is offenderName for arrest, parole, subject", emailSubject);
        String emailBody = formatter.getEmailBody(email);
        assertEquals("Notification body is offenderName for arrest, parole, body.  Subject identifer is: 123", emailBody);
    }

    @Test
    public void testArrestNonCriminalJusticeTemplates() throws Exception {
        EmailNotification email = buildArrestEmailNotification(getArrestTopicParoleSystemNotificationMessage(), "{http://demostate.gov/SystemNames/1.0}SystemA", "I");
        String emailSubject = formatter.getEmailSubject(email);
        assertEquals("Notification subject is offenderName for non criminal justice", emailSubject);
        String emailBody = formatter.getEmailBody(email);
        assertEquals("Notification body is offenderName for non criminal justice", emailBody);
    }

    @Test
    public void testArrestDefaultSystemTemplates() throws Exception {
        // the arrest topic is configured with a default system template, so that will be used
        EmailNotification email = buildArrestEmailNotification(getArrestTopicParoleSystemNotificationMessage(), "{http://ojbc.org/Dummy/1.0}UnkownSystem", "default");
        String emailSubject = formatter.getEmailSubject(email);
        assertEquals("Notification subject is offenderName for arrest, default arrest system, subject", emailSubject);
        String emailBody = formatter.getEmailBody(email);
        assertEquals("Notification body is offenderName for arrest, default arrest system, body", emailBody);
    }

    @Test
    public void testIncidentParoleTemplates() throws Exception {
        EmailNotification email = buildIncidentEmailNotification(getIncidentTopicParoleSystemNotificationMessage(), "{http://demostate.gov/SystemNames/1.0}SystemA", "default");
        String emailSubject = formatter.getEmailSubject(email);
        assertEquals("Notification subject is offenderName for incident, parole, subject", emailSubject);
        String emailBody = formatter.getEmailBody(email);
        assertEquals("Notification body is offenderName for incident, parole, body", emailBody);
    }

    @Test
    public void testIncidentNoDefaultSystemTemplates() throws Exception {
        // the incident topic is not configured with a default system template, so the overall default is used (same as if an unknown topic is provided)
        EmailNotification email = buildIncidentEmailNotification(getIncidentTopicParoleSystemNotificationMessage(), "{http://ojbc.org/Dummy/1.0}UnkownSystem", "default");
        String emailSubject = formatter.getEmailSubject(email);
        assertEquals("Default notification subject is offenderName", emailSubject);
        String emailBody = formatter.getEmailBody(email);
        assertEquals("Default email body:\nSomething from email notification: offenderName\nSomething from notification request: Doe", emailBody);
    }

    private EmailNotification buildArrestEmailNotification(Document notificationMessage, String systemName, String subscriptionCategoryCode) throws Exception {
        EmailNotification email = new EmailNotification();
        
        Map<String, String> subscriptionSubjectIdentifiers = new HashMap<String, String>();
        subscriptionSubjectIdentifiers.put("subscriptionQualifier", "123");
        
        email.setSubscriptionSubjectIdentifiers(subscriptionSubjectIdentifiers);
        email.addToAddressee("email@address");
        email.setSubjectName("offenderName");
        email.setSubscribingSystemIdentifier(systemName);
        email.setSubscriptionCategoryCode(subscriptionCategoryCode);
        email.setNotificationRequest(new ArrestNotificationRequest(notificationMessage));
        return email;
    }

    private EmailNotification buildIncidentEmailNotification(Document notificationMessage, String systemName, String subscriptionCategoryCode) throws Exception {
        EmailNotification email = new EmailNotification();
        email.addToAddressee("email@address");
        email.setSubjectName("offenderName");
        email.setSubscribingSystemIdentifier(systemName);
        email.setSubscriptionCategoryCode(subscriptionCategoryCode);
        email.setNotificationRequest(new IncidentNotificationRequest(notificationMessage));
        return email;
    }

    private Document getArrestTopicParoleSystemNotificationMessage() throws Exception {
        File inputFile = new File("src/test/resources/xmlInstances/notificationMessage.xml");
        DocumentBuilderFactory docBuilderFact = DocumentBuilderFactory.newInstance();
        docBuilderFact.setNamespaceAware(true);
        DocumentBuilder docBuilder = docBuilderFact.newDocumentBuilder();
        Document document = docBuilder.parse(inputFile);
        return document;
    }

    private Document getUnknownTopicParoleSystemNotificationMessage() throws Exception {
        Document document = getArrestTopicParoleSystemNotificationMessage();
        Node topicNode = XmlUtils.xPathNodeSearch(document, "//b-2:Notify/b-2:NotificationMessage/b-2:Topic");
        topicNode.setTextContent("{http://ojbc.org/wsn/topics}:person/unknownTopic");
        return document;
    }

    private Document getIncidentTopicParoleSystemNotificationMessage() throws Exception {
        File inputFile = new File("src/test/resources/xmlInstances/notificationMessage-incidentFutureNotificationDate.xml");
        DocumentBuilderFactory docBuilderFact = DocumentBuilderFactory.newInstance();
        docBuilderFact.setNamespaceAware(true);
        DocumentBuilder docBuilder = docBuilderFact.newDocumentBuilder();
        Document document = docBuilder.parse(inputFile);
        return document;
    }

}
