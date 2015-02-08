package org.ojbc.intermediaries.sn.topic.chcycle;

import org.ojbc.intermediaries.sn.notification.NotificationProcessor;
import org.ojbc.intermediaries.sn.notification.NotificationRequest;

import org.apache.camel.Message;

public class ChCycleNotificationProcessor extends NotificationProcessor {
	
	@Override
	protected NotificationRequest makeNotificationRequestFromIncomingMessage(
			Message msg) throws Exception {

		return new ChCycleNotificationRequest(msg);
	}
		
}

