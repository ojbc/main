package org.ojbc.intermediaries.sn.topic.arrest;

import org.ojbc.intermediaries.sn.notification.NotificationProcessor;
import org.ojbc.intermediaries.sn.notification.NotificationRequest;

import org.apache.camel.Message;

public class ArrestNotificationProcessor extends NotificationProcessor {

	@Override
	protected NotificationRequest makeNotificationRequestFromIncomingMessage(Message msg) throws Exception{
		return new ArrestNotificationRequest(msg);
	}
	
}
