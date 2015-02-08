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
