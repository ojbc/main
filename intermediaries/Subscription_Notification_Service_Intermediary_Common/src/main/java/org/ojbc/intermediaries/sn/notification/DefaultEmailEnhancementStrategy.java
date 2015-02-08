package org.ojbc.intermediaries.sn.notification;


/**
 * A null object implementation that does no editing.
 *
 */
public class DefaultEmailEnhancementStrategy implements EmailEnhancementStrategy {

    @Override
    public EmailNotification enhanceEmail(EmailNotification emailNotification) {
        return emailNotification;
    }

}
