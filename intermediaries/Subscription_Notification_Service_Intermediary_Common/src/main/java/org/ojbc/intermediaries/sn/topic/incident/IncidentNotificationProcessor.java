package org.ojbc.intermediaries.sn.topic.incident;

import org.ojbc.intermediaries.sn.notification.NotificationProcessor;
import org.ojbc.intermediaries.sn.notification.NotificationRequest;

import org.apache.camel.Message;

public class IncidentNotificationProcessor extends NotificationProcessor {

    @Override
    protected NotificationRequest makeNotificationRequestFromIncomingMessage(Message msg) throws Exception {

        return new IncidentNotificationRequest(msg);
    }

}
