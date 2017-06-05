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


/**
 * The interface for objects that create the contents of a notification email from a message.
 *
 */
public interface EmailFormatter {
    
    public static final class DefaultEmailFormatter implements EmailFormatter {

        @Override
        public String getEmailBody(EmailNotification emailNotification)  {
            return "An event occurred.";
        }

        @Override
        public String getEmailSubject(EmailNotification emailNotification) {
            return "Notification subject.";
        }
        
    }
    
    /**
     * Build the email body from the message.
     * @param camelMessage
     * @param emailNotification the email notification object representing the notification email information
     * @return the email body
     * @throws Exception 
     */
    public String getEmailBody(EmailNotification emailNotification) throws Exception;
    
    /**
     * Build the email subject from the message
     * @param camelMessage
     * @param emailNotification the email notification object representing the notification email information
     * @return the email subject
     */
    public String getEmailSubject(EmailNotification emailNotification);

}
