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
