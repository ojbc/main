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
package org.ojbc.intermediaries.sn;

import javax.annotation.Resource;

import org.apache.camel.Body;
import org.apache.camel.Exchange;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.ojbc.intermediaries.sn.dao.rapback.FbiRapbackDao;
import org.ojbc.intermediaries.sn.dao.rapback.SubsequentResults;
import org.ojbc.util.camel.helper.MtomUtils;
import org.ojbc.util.xml.XmlUtils;
import org.w3c.dom.Document;
import org.w3c.dom.Node;

public class ArrestNotificationAttachmentProcessor {
	
	private final Log log = LogFactory.getLog(this.getClass());
	
	@Resource(name="rapbackDao")
	private FbiRapbackDao rapbackDao;
	
	public void processMtomAttachment(@Body Document report, Exchange exchange) throws Exception {
		
		log.info("Processing Arrest Notification MTOM attachement.");
		
		Node notificationMessageNode = XmlUtils.xPathNodeSearch(report, "//b-2:Notify/b-2:NotificationMessage/b-2:Message/notfm-exch:NotificationMessage");
		
		String attachmentHref = XmlUtils.xPathStringSearch(notificationMessageNode, "notfm-ext:NotifyingArrest/notfm-ext:CriminalHistoryRecordDocument/xop:Include/@href");
		
		SubsequentResults subsequentResult = new SubsequentResults(); 
		subsequentResult.setRapSheet(MtomUtils.getAttachment(exchange, null, attachmentHref));
		
		String fbiSubscriptionId = XmlUtils.xPathStringSearch(notificationMessageNode, "notfm-ext:NotifyingArrest/notfm-ext:RelatedFBISubscription/notfm-ext:RecordRapBackSubscriptionIdentification/nc:IdentificationID");
		subsequentResult.setFbiSubscriptionId(fbiSubscriptionId);
		
		//TODO persist the attachment only when there is at least one active state subscription. -hw. 
		rapbackDao.saveSubsequentResults(subsequentResult);
	}

}

