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

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.ojbc.intermediaries.sn.dao.Subscription;
import org.ojbc.intermediaries.sn.dao.audit.AuditDAO;

public class NotificationLoggingProcessor {

	private static final Log log = LogFactory.getLog(NotificationProcessor.class);
	
	private AuditDAO auditDaoImpl;
	
	public void logNotification(EmailNotification emailNotification)
	{
		
		try {
			log.info("Email Notification to log (Notification Processor): " + emailNotification);
			
			if (emailNotification.getBlockedAddresseeSet() !=null && emailNotification.getBlockedAddresseeSet().size() > 0)
			{
				log.info("Notification contains blocked addresses, don't log: " + emailNotification.getBlockedAddresseeSet());
				return;
			}	
			
			Subscription subscription = emailNotification.getSubscription();
			
			if (subscription != null)
			{
				auditDaoImpl.saveNotificationLogEntry(emailNotification);
			}
		} catch (Exception e) {
			log.error("Unable to log notification!");
			e.printStackTrace();
		}	
		
		
	}

	public AuditDAO getAuditDaoImpl() {
		return auditDaoImpl;
	}

	public void setAuditDaoImpl(AuditDAO auditDaoImpl) {
		this.auditDaoImpl = auditDaoImpl;
	}
	
	
}
