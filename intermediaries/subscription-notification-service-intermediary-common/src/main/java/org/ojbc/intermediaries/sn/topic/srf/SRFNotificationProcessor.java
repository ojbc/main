package org.ojbc.intermediaries.sn.topic.srf;

import org.apache.camel.Message;
import org.ojbc.intermediaries.sn.notification.NotificationProcessor;
import org.ojbc.intermediaries.sn.notification.NotificationRequest;

public class SRFNotificationProcessor extends NotificationProcessor{
	
	@Override
	protected NotificationRequest makeNotificationRequestFromIncomingMessage(
			Message msg) throws Exception {

		return new SRFNotificationRequest(msg);
	}
}
