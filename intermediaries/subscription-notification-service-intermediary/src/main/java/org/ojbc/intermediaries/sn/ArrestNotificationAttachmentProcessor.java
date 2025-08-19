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
package org.ojbc.intermediaries.sn;

import java.util.List;

import org.apache.camel.Body;
import org.apache.camel.Header;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.ojbc.intermediaries.sn.dao.rapback.FbiRapbackDao;
import org.ojbc.intermediaries.sn.dao.rapback.ResultSender;
import org.ojbc.intermediaries.sn.dao.rapback.SubsequentResults;
import org.ojbc.intermediaries.sn.notification.EmailNotification;
import org.ojbc.util.xml.XmlUtils;
import org.w3c.dom.Document;
import org.w3c.dom.Node;

import jakarta.annotation.Resource;



public class ArrestNotificationAttachmentProcessor {
	
	private final Log log = LogFactory.getLog(this.getClass());
	
	@Resource(name="rapbackDao")
	private FbiRapbackDao rapbackDao;
	
	public void processBase64BinaryObject(@Body Document report, @Header("base64BinaryData") String baseb4BinaryData) throws Exception {
		
		log.info("Processing Arrest Notification MTOM attachement.");
		
		Node notificationMessageNode = XmlUtils.xPathNodeSearch(report, "//b-2:Notify/b-2:NotificationMessage/b-2:Message/notfm-exch:NotificationMessage");
		
		SubsequentResults subsequentResult = new SubsequentResults(); 
		subsequentResult.setRapSheet(Base64.decodeBase64(baseb4BinaryData));
		
		String resultSenderString = XmlUtils.xPathStringSearch(notificationMessageNode, "notfm-ext:NotifyingArrest/notfm-ext:NotifyingActivityReportingOrganization/nc:OrganizationName");
		if (ResultSender.FBI.name().equals(resultSenderString)){
			subsequentResult.setResultsSender(ResultSender.FBI);
		}
		else{
			subsequentResult.setResultsSender(ResultSender.State);
		}
		
		String civilSid = XmlUtils.xPathStringSearch(notificationMessageNode, "jxdm41:Person[@s:id = ../nc:ActivityInvolvedPersonAssociation/nc:PersonReference/@s:ref]"
				+ "/jxdm41:PersonAugmentation/jxdm41:PersonStateFingerprintIdentification/nc:IdentificationID");
		
		subsequentResult.setCivilSid(civilSid);

		String fbiSubscriptionId = XmlUtils.xPathStringSearch(notificationMessageNode, "notfm-ext:NotifyingArrest/notfm-ext:RelatedFBISubscription/notfm-ext:RecordRapBackSubscriptionIdentification/nc:IdentificationID");
		String ucn = rapbackDao.getUcnByFbiSubscritionId(fbiSubscriptionId);
		subsequentResult.setUcn(ucn);
		String transactionNumber = rapbackDao.getTransactionNumberByFbiSubscriptionId(fbiSubscriptionId);
		if (StringUtils.isNotBlank(transactionNumber)){
			subsequentResult.setTransactionNumber(transactionNumber);
			subsequentResult.setNotificationIndicator(true);
			rapbackDao.deleteSubsequentResults(subsequentResult);
			rapbackDao.saveSubsequentResults(subsequentResult);
		}
		else{
			log.info("No valid FBI subscription found for the civil SID, the arrest notification attachment is not persisted");
		}
	}

	public void saveInstateRapsheet(@Body List<EmailNotification> emailNotifications, @Header("base64BinaryData") String baseb4BinaryData) throws Exception {
		
		log.info("Processing in state rapsheet.");
		
		if (emailNotifications != null && emailNotifications.size() > 0)
		{
			for(EmailNotification emailNotification: emailNotifications){
				
				if (StringUtils.isBlank(baseb4BinaryData))
				{
					continue;
				}	
				
				SubsequentResults subsequentResult = new SubsequentResults(); 
				subsequentResult.setRapSheet(Base64.decodeBase64(baseb4BinaryData));
				subsequentResult.setResultsSender(ResultSender.State);
				subsequentResult.setNotificationIndicator(true);
				subsequentResult.setTransactionNumber(getTransactionNumber(emailNotification));
				subsequentResult.setCivilSid(getCivilSid(emailNotification));
				
				rapbackDao.deleteSubsequentResults(subsequentResult);
				rapbackDao.saveSubsequentResults(subsequentResult);
			}
		}
	}

	private String getTransactionNumber(EmailNotification emailNotification) {
		long stateSubscriptionId = emailNotification.getSubscription().getId();
		String transactionNumber = rapbackDao.getTransactionNumberBySubscriptionId(stateSubscriptionId);
		
		log.info("Transaction Number: "  + transactionNumber + ", for subscription ID: " + stateSubscriptionId);
		
		return transactionNumber;
	}

	private String getCivilSid(EmailNotification emailNotification)
			throws Exception {
		Document notificationReport = emailNotification.getNotificationRequest().getRequestDocument();
		Node notificationMessageNode = XmlUtils.xPathNodeSearch(notificationReport, "//b-2:Notify/b-2:NotificationMessage/b-2:Message/notfm-exch:NotificationMessage");
		String civilSid = XmlUtils.xPathStringSearch(notificationMessageNode, "jxdm41:Person/jxdm41:PersonAugmentation/jxdm41:PersonStateFingerprintIdentification/nc:IdentificationID");
		return civilSid;
	}
	
}

