package org.ojbc.intermediaries.sn.notification;


/**
 * The interface for objects that create the contents of a notification email from a message.
 *
 */
public interface EmailFormatter {
    
    public static final class DefaultEmailFormatter implements EmailFormatter {

        @Override
        public String getEmailBody(EmailNotification emailNotification)  {
            return "An event occurred.";
        }

        @Override
        public String getEmailSubject(EmailNotification emailNotification) {
            return "Notification subject.";
        }
        
    }
    
    /**
     * Build the email body from the message.
     * @param camelMessage
     * @param emailNotification the email notification object representing the notification email information
     * @return the email body
     * @throws Exception 
     */
    public String getEmailBody(EmailNotification emailNotification) throws Exception;
    
    /**
     * Build the email subject from the message
     * @param camelMessage
     * @param emailNotification the email notification object representing the notification email information
     * @return the email subject
     */
    public String getEmailSubject(EmailNotification emailNotification);

}
