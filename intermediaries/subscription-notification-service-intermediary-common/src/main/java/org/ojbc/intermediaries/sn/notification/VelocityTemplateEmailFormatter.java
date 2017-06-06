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
package org.ojbc.intermediaries.sn.notification;

import java.io.StringWriter;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.Velocity;

/**
 * An EmailFormatter implementation that sets up a Velocity context and then applies that context to one of several possible Velocity templates.  These templates are
 * supplied via a "map of maps" property.  The key in the map at the first level is topic; the key at the second level is system identifier.  The value in the inner map
 * is an EmailTemplate object (reference the static inner class here), which provides access to a pair of Velocity templates:  one to create the subject line of the email,
 * the other to create the body.  If the topic of the notification is found in the outer map, but no object is found for the system, then the formatter will look for a
 * entry in the inner map with a system key of {http://ojbc.org/OJB/Subscriptions/1.0}DefaultSystem, and use it if it's found.  If there is no topic key for the notification topic,
 * or if there is no system found and no default system template configured, then the defaultEmailTemplate property will be used to format the message.<br/>
 * 
 * The context supplied to the Velocity templates includes the EmailNotification object corresponding to the email to be sent (which in turn provides access to the
 * NotificationRequest object).  It also includes the "user friendly" descriptor of the subscribing system (which is typically more useful than the formal identifier available from
 * the EmailNotification object).
 *
 */
public class VelocityTemplateEmailFormatter implements EmailFormatter {
    
    private static final Log log = LogFactory.getLog(VelocityTemplateEmailFormatter.class);

    public static final class EmailTemplate {
        private String emailSubjectTemplate;
        private String emailBodyTemplate;

        public String getEmailSubjectTemplate() {
            return emailSubjectTemplate;
        }

        public void setEmailSubjectTemplate(String emailSubjectTemplate) {
            this.emailSubjectTemplate = emailSubjectTemplate;
        }

        public String getEmailBodyTemplate() {
            return emailBodyTemplate;
        }

        public void setEmailBodyTemplate(String emailBodyTemplate) {
            this.emailBodyTemplate = emailBodyTemplate;
        }

		@Override
		public String toString() {
			return "EmailTemplate [emailSubjectTemplate="
					+ emailSubjectTemplate + ", emailBodyTemplate="
					+ emailBodyTemplate + "]";
		}
    }

    private EmailTemplate defaultEmailTemplate;
    private Map<String, Map<NotificationFormatKey, EmailTemplate>> topicSystemTemplateMap = new HashMap<String, Map<NotificationFormatKey, EmailTemplate>>();
    private Map<String, String> systemIdentifierToDescriptorMap = new HashMap<String, String>();

    public Map<String, String> getSystemIdentifierToDescriptorMap() {
        return systemIdentifierToDescriptorMap;
    }

    public void setSystemIdentifierToDescriptorMap(Map<String, String> systemIdentifierToDescriptorMap) {
        this.systemIdentifierToDescriptorMap = systemIdentifierToDescriptorMap;
    }

    public Map<String, Map<NotificationFormatKey, EmailTemplate>> getTopicSystemTemplateMap() {
        return topicSystemTemplateMap;
    }

    public void setTopicSystemTemplateMap(Map<String, Map<NotificationFormatKey, EmailTemplate>> topicSystemTemplateMap) {
        this.topicSystemTemplateMap = topicSystemTemplateMap;
    }

    public VelocityTemplateEmailFormatter() {
        Velocity.init();
    }

    public EmailTemplate getDefaultEmailTemplate() {
        return defaultEmailTemplate;
    }

    public void setDefaultEmailTemplate(EmailTemplate defaultEmailTemplate) {
        this.defaultEmailTemplate = defaultEmailTemplate;
    }

    @Override
    public String getEmailBody(EmailNotification emailNotification) throws Exception {
        return applyTemplate(emailNotification, getEmailTemplateForNotification(emailNotification).emailBodyTemplate);
    }

    @Override
    public String getEmailSubject(EmailNotification emailNotification) {
        return applyTemplate(emailNotification, getEmailTemplateForNotification(emailNotification).emailSubjectTemplate);
    }

    private EmailTemplate getEmailTemplateForNotification(EmailNotification emailNotification) {
        String topic = emailNotification.getNotificationRequest().getTopic();
        String subscribingSystem = emailNotification.getSubscribingSystemIdentifier();
        String subscriptionCategoryCode = emailNotification.getSubscriptionCategoryCode();
        String notifyingSystemName = emailNotification.getNotificationRequest().getNotifyingSystemName();
        
        EmailTemplate ret = defaultEmailTemplate;
        Map<NotificationFormatKey, EmailTemplate> systemTemplateMap = topicSystemTemplateMap.get(topic);
        if (systemTemplateMap != null) {
        	
        	NotificationFormatKey emailNotificationIdentifierKeyWrapper = new NotificationFormatKey();
        	
        	emailNotificationIdentifierKeyWrapper.setSubscribingSystemName(subscribingSystem);
        	emailNotificationIdentifierKeyWrapper.setSubscriptionCategoryCode(subscriptionCategoryCode);
        	emailNotificationIdentifierKeyWrapper.setNotifyingSystemName(notifyingSystemName);
        	
        	log.info("Subscribing System: " + subscribingSystem + ", Subscription Category Code: " + subscriptionCategoryCode + ", Notifying system name: " + notifyingSystemName);
        	
            EmailTemplate mappedTemplate = systemTemplateMap.get(emailNotificationIdentifierKeyWrapper);
            if (mappedTemplate == null) {
            	
            	NotificationFormatKey emailNotificationIdentifierKeyWrapperDefault = new NotificationFormatKey();
            	
            	emailNotificationIdentifierKeyWrapperDefault.setSubscribingSystemName("{http://ojbc.org/OJB/Subscriptions/1.0}DefaultSystem");
            	emailNotificationIdentifierKeyWrapperDefault.setNotifyingSystemName("{http://ojbc.org/OJB/Subscriptions/1.0}DefaultSystem");
            	emailNotificationIdentifierKeyWrapperDefault.setSubscriptionCategoryCode("default");
            	
                mappedTemplate = systemTemplateMap.get(emailNotificationIdentifierKeyWrapperDefault);
            }
            if (mappedTemplate != null) {
                ret = mappedTemplate;
            }
        }
        return ret;
    }

    private String applyTemplate(EmailNotification emailNotification, String template) {
        VelocityContext context = new VelocityContext();
        context.put("emailNotification", emailNotification);
        String subscribingSystemId = systemIdentifierToDescriptorMap.get(emailNotification.getSubscribingSystemIdentifier());
        context.put("subscribingSystemDescriptor", subscribingSystemId == null ? "Unknown system" : subscribingSystemId);
        StringWriter w = new StringWriter();
        Velocity.evaluate(context, w, "applyTemplates", template);
        return w.toString();
    }

}
