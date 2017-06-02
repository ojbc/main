/*
 * Unless explicitly acquired and licensed from Licensor under another license, the contents of
 * this file are subject to the Reciprocal Public License ("RPL") Version 1.5, or subsequent
 * versions as allowed by the RPL, and You may not copy or use this file in either source code
 * or executable form, except in compliance with the terms and conditions of the RPL
 *
 * All software distributed under the RPL is provided strictly on an "AS IS" basis, WITHOUT
 * WARRANTY OF ANY KIND, EITHER EXPRESS OR IMPLIED, AND LICENSOR HEREBY DISCLAIMS ALL SUCH
 * WARRANTIES, INCLUDING WITHOUT LIMITATION, ANY WARRANTIES OF MERCHANTABILITY, FITNESS FOR A
 * PARTICULAR PURPOSE, QUIET ENJOYMENT, OR NON-INFRINGEMENT. See the RPL for specific language
 * governing rights and limitations under the RPL.
 *
 * http://opensource.org/licenses/RPL-1.5
 *
 * Copyright 2012-2017 Open Justice Broker Consortium
 */
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
