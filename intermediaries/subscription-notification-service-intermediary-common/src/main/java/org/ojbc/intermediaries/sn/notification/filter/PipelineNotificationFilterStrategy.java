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
package org.ojbc.intermediaries.sn.notification.filter;

import java.util.ArrayList;
import java.util.List;

import org.ojbc.intermediaries.sn.notification.NotificationRequest;

/**
 * This class allows for notification filter strategies to pipelined. If one strategy dictates that the message will be filtered, then it will be filtered.
 * 
 */

public class PipelineNotificationFilterStrategy implements NotificationFilterStrategy {

    private List<NotificationFilterStrategy> notificationFilterStrategies;
    
    public PipelineNotificationFilterStrategy() {
        notificationFilterStrategies = new ArrayList<NotificationFilterStrategy>();
    }

    @Override
    public boolean shouldMessageBeFiltered(NotificationRequest notificationRequest) {


        for (NotificationFilterStrategy notificationFilterStrategy : notificationFilterStrategies) {
            if (notificationFilterStrategy.shouldMessageBeFiltered(notificationRequest)) {
                return true;
            }
        }

        return false;
        
    }

    /**
     * Get the list of component strategies
     * @return
     */
    public List<NotificationFilterStrategy> getNotificationFilterStrategies() {
        return notificationFilterStrategies;
    }

    /**
     * Set the list of component strategies to be applied to determine whether the request should be filtered out.
     * @param notificationFilterStrategies
     */
    public void setNotificationFilterStrategies(List<NotificationFilterStrategy> notificationFilterStrategies) {
        this.notificationFilterStrategies = notificationFilterStrategies;
    }

}
