package org.ojbc.intermediaries.sn.notification;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * An email enhancement implementation that edits emails by passing them to a list of child editors, in sequence.
 *
 */
public class CompositeEmailEnhancementStrategy implements EmailEnhancementStrategy {
    
    private List<EmailEnhancementStrategy> compositeStrategy = new ArrayList<EmailEnhancementStrategy>();

    @Override
    public EmailNotification enhanceEmail(EmailNotification emailNotification) {
        EmailNotification ret = emailNotification;
        for (EmailEnhancementStrategy s : compositeStrategy) {
            ret = s.enhanceEmail(ret);
        }
        return ret;
    }

    public List<EmailEnhancementStrategy> getCompositeStrategy() {
        return Collections.unmodifiableList(compositeStrategy);
    }

    public void setCompositeStrategy(List<EmailEnhancementStrategy> compositeStrategy) {
        this.compositeStrategy = compositeStrategy;
    }
    
}
