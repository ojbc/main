package org.ojbc.intermediaries.sn;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.ImportResource;

/**
 * A sample Spring Boot application that starts the Camel routes.
 */
@SpringBootApplication
@ComponentScan({"org.ojbc.intermediaries.sn"})
@ImportResource(value = {"classpath:META-INF/spring/camel-context.xml", 
		"classpath:META-INF/spring/cxf-endpoints.xml", 
		"classpath:META-INF/spring/beans.xml", 
		"classpath:META-INF/spring/email-formatters.xml", 
		"classpath:META-INF/spring/extensible-beans.xml", 
		"classpath:META-INF/spring/fbi-routes.xml", 
		"classpath:META-INF/spring/notification-routes.xml", 
		"classpath:META-INF/spring/search-query-routes.xml", 
		"classpath:META-INF/spring/subscription-migration.xml", 
		"classpath:META-INF/spring/subscription-secure-routes.xml", 
		"classpath:META-INF/spring/dao.xml"})
public class SubscriptionNotificationService {
	@Autowired
	SubscriptionNotificationServiceProperties adatperProperties; 
    /**
     * A main method to start this application.
     */
    public static void main(String[] args) {
        SpringApplication.run(SubscriptionNotificationService.class, args);
    }
}
