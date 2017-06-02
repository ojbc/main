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
 * The interface for objects that edit emails before they are sent.  Note that this differs from the NotificationFilterStrategy, which filters out
 * entire notifications (whether email or not, and whether those notifications result in multiple emails or not.)
 *
 */
public interface EmailEnhancementStrategy {
    
    /**
     * Edit the specified email notification object before it is sent
     * @param emailNotification the candidate email
     * @return an "edited" notification object
     */
    public EmailNotification enhanceEmail(EmailNotification emailNotification);

}
