package org.ojbc.intermediaries.sn.topic.warrantfile;

import org.apache.camel.Message;
import org.ojbc.intermediaries.sn.notification.NotificationProcessor;
import org.ojbc.intermediaries.sn.notification.NotificationRequest;
import org.ojbc.intermediaries.sn.topic.chcycle.ChCycleNotificationRequest;

public class WarrantFileNotificationProcessor extends NotificationProcessor{
	@Override
	protected NotificationRequest makeNotificationRequestFromIncomingMessage(
			Message msg) throws Exception {

		return new WarrantFileNotificationRequest(msg);
	}
}
