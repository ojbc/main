package org.ojbc.intermediaries.sn.notification;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * An email edit strategy that adds a static BCC to every email notification. The bccAddress property is a comma-separated string of email addresses.
 * 
 */
public class StaticBCCEmailEnhancementStrategy implements EmailEnhancementStrategy {

    private static final Log log = LogFactory.getLog(StaticBCCEmailEnhancementStrategy.class);

    private String bccAddress;

    public String getBccAddress() {
        return bccAddress;
    }

    public void setBccAddress(String bccAddress) {
        this.bccAddress = bccAddress;
    }

    @Override
    public EmailNotification enhanceEmail(EmailNotification emailNotification) {
        EmailNotification ret = (EmailNotification) emailNotification.clone();
        if (bccAddress != null) {
            log.debug("Adding bcc=" + bccAddress + " to email with to=" + ret.getToAddressees());
            if (bccAddress.contains(",")) {
                String[] addresses = bccAddress.split(",");
                for (String address : addresses) {
                    ret.addBccAddressee(address);
                }
            } else {
                ret.addBccAddressee(bccAddress);
            }
        }
        else {
            log.warn("BCC email decorator called but no address has been set. No bcc added to email notification.");
        }
        return ret;
    }

}
