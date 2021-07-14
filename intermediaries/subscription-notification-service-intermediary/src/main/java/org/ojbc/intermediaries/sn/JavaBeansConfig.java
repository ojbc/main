package org.ojbc.intermediaries.sn;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;

@Configuration
public class JavaBeansConfig {
    @Autowired 
    SubscriptionNotificationServiceProperties appProperties; 
    
}
