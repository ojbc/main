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

        Map<String, Map<String, VelocityTemplateEmailFormatter.EmailTemplate>> topicSystemTemplateMap = new HashMap<String, Map<String, VelocityTemplateEmailFormatter.EmailTemplate>>();

        Map<String, EmailTemplate> arrestMap = new HashMap<String, VelocityTemplateEmailFormatter.EmailTemplate>();

        VelocityTemplateEmailFormatter.EmailTemplate template = new VelocityTemplateEmailFormatter.EmailTemplate();
        template.setEmailSubjectTemplate("Notification subject is $emailNotification.subjectName for arrest, parole, subject");
        template.setEmailBodyTemplate("Notification body is $emailNotification.subjectName for arrest, parole, body");
        arrestMap.put("{http://demostate.gov/SystemNames/1.0}SystemA", template);

        template = new VelocityTemplateEmailFormatter.EmailTemplate();
        template.setEmailSubjectTemplate("Notification subject is $emailNotification.subjectName for arrest, probation, subject");
        template.setEmailBodyTemplate("Notification body is $emailNotification.subjectName for arrest, probation, body");
        arrestMap.put("{http://demostate.gov/SystemNames/1.0}SystemB", template);

        template = new VelocityTemplateEmailFormatter.EmailTemplate();
        template.setEmailSubjectTemplate("Notification subject is $emailNotification.subjectName for arrest, default arrest system, subject");
        template.setEmailBodyTemplate("Notification body is $emailNotification.subjectName for arrest, default arrest system, body");
        arrestMap.put("{http://ojbc.org/OJB/Subscriptions/1.0}DefaultSystem", template);

        topicSystemTemplateMap.put("{http://ojbc.org/wsn/topics}:person/arrest", arrestMap);

        Map<String, EmailTemplate> incidentMap = new HashMap<String, VelocityTemplateEmailFormatter.EmailTemplate>();

        template = new VelocityTemplateEmailFormatter.EmailTemplate();
        template.setEmailSubjectTemplate("Notification subject is $emailNotification.subjectName for incident, parole, subject");
        template.setEmailBodyTemplate("Notification body is $emailNotification.subjectName for incident, parole, body");
        incidentMap.put("{http://demostate.gov/SystemNames/1.0}SystemA", template);

        template = new VelocityTemplateEmailFormatter.EmailTemplate();
        template.setEmailSubjectTemplate("Notification subject is $emailNotification.subjectName for incident, probation, subject");
        template.setEmailBodyTemplate("Notification body is $emailNotification.subjectName for incident, probation, body");
        incidentMap.put("{http://demostate.gov/SystemNames/1.0}SystemB", template);

        topicSystemTemplateMap.put("{http://ojbc.org/wsn/topics}:person/incident", incidentMap);

        formatter.setTopicSystemTemplateMap(topicSystemTemplateMap);

    }

    @Test
    public void testDefaultTemplates() throws Exception {
        // this scenario tests an unknown topic...you get the default template then
        EmailNotification email = buildArrestEmailNotification(getUnknownTopicParoleSystemNotificationMessage(), "{http://demostate.gov/SystemNames/1.0}SystemA");
        String emailSubject = formatter.getEmailSubject(email);
        assertEquals("Default notification subject is offenderName", emailSubject);
        String emailBody = formatter.getEmailBody(email);
        assertEquals("Default email body:\nSomething from email notification: offenderName\nSomething from notification request: Simpson", emailBody);
    }

    @Test
    public void testArrestParoleTemplates() throws Exception {
        EmailNotification email = buildArrestEmailNotification(getArrestTopicParoleSystemNotificationMessage(), "{http://demostate.gov/SystemNames/1.0}SystemA");
        String emailSubject = formatter.getEmailSubject(email);
        assertEquals("Notification subject is offenderName for arrest, parole, subject", emailSubject);
        String emailBody = formatter.getEmailBody(email);
        assertEquals("Notification body is offenderName for arrest, parole, body", emailBody);
    }

    @Test
    public void testArrestDefaultSystemTemplates() throws Exception {
        // the arrest topic is configured with a default system template, so that will be used
        EmailNotification email = buildArrestEmailNotification(getArrestTopicParoleSystemNotificationMessage(), "{http://ojbc.org/Dummy/1.0}UnkownSystem");
        String emailSubject = formatter.getEmailSubject(email);
        assertEquals("Notification subject is offenderName for arrest, default arrest system, subject", emailSubject);
        String emailBody = formatter.getEmailBody(email);
        assertEquals("Notification body is offenderName for arrest, default arrest system, body", emailBody);
    }

    @Test
    public void testIncidentParoleTemplates() throws Exception {
        EmailNotification email = buildIncidentEmailNotification(getIncidentTopicParoleSystemNotificationMessage(), "{http://demostate.gov/SystemNames/1.0}SystemA");
        String emailSubject = formatter.getEmailSubject(email);
        assertEquals("Notification subject is offenderName for incident, parole, subject", emailSubject);
        String emailBody = formatter.getEmailBody(email);
        assertEquals("Notification body is offenderName for incident, parole, body", emailBody);
    }

    @Test
    public void testIncidentNoDefaultSystemTemplates() throws Exception {
        // the incident topic is not configured with a default system template, so the overall default is used (same as if an unknown topic is provided)
        EmailNotification email = buildIncidentEmailNotification(getIncidentTopicParoleSystemNotificationMessage(), "{http://ojbc.org/Dummy/1.0}UnkownSystem");
        String emailSubject = formatter.getEmailSubject(email);
        assertEquals("Default notification subject is offenderName", emailSubject);
        String emailBody = formatter.getEmailBody(email);
        assertEquals("Default email body:\nSomething from email notification: offenderName\nSomething from notification request: Doe", emailBody);
    }

    private EmailNotification buildArrestEmailNotification(Document notificationMessage, String systemName) throws Exception {
        EmailNotification email = new EmailNotification();
        email.addToAddressee("email@address");
        email.setSubjectName("offenderName");
        email.setSubscribingSystemIdentifier(systemName);
        email.setNotificationRequest(new ArrestNotificationRequest(notificationMessage));
        return email;
    }

    private EmailNotification buildIncidentEmailNotification(Document notificationMessage, String systemName) throws Exception {
        EmailNotification email = new EmailNotification();
        email.addToAddressee("email@address");
        email.setSubjectName("offenderName");
        email.setSubscribingSystemIdentifier(systemName);
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
