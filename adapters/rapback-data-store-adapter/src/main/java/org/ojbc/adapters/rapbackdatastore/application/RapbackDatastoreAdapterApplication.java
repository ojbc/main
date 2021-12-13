package org.ojbc.adapters.rapbackdatastore.application;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.autoconfigure.jms.activemq.ActiveMQAutoConfiguration;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.ImportResource;

/**
 * A sample Spring Boot application that starts the Camel routes.
 */
@SpringBootApplication(exclude = {DataSourceAutoConfiguration.class, ActiveMQAutoConfiguration.class})
@ComponentScan({"org.ojbc.adapters.rapbackdatastore.application"})
@ImportResource(value = {"classpath:META-INF/spring/camel-context.xml", 
		"classpath:META-INF/spring/cxf-endpoints.xml",
		"classpath:META-INF/spring/beans.xml",
		"classpath:META-INF/spring/dao.xml",
		"classpath:META-INF/spring/subscription-management-routes.xml"})
public class RapbackDatastoreAdapterApplication extends SpringBootServletInitializer{
	public static void main(String[] args) {
        SpringApplication.run(RapbackDatastoreAdapterApplication.class, args);
    }
}  
