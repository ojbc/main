package org.ojbc.adapters.analyticaldatastore.application;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.ImportResource;
import org.springframework.boot.autoconfigure.orm.jpa.HibernateJpaAutoConfiguration;

/**
 * A sample Spring Boot application that starts the Camel routes.
 */
@SpringBootApplication(exclude = {HibernateJpaAutoConfiguration.class })
@ComponentScan({"org.ojbc.adapters.analyticaldatastore.application"})
@ImportResource(value = {"classpath:META-INF/spring/camel-context.xml", 
		"classpath:META-INF/spring/cxf-endpoints.xml",
		"classpath:META-INF/spring/beans.xml",
		"classpath:META-INF/spring/dao.xml"})
public class IncidentArrestAnalyticsStagingAdapterApplication extends SpringBootServletInitializer{
	public static void main(String[] args) {
        SpringApplication.run(IncidentArrestAnalyticsStagingAdapterApplication.class, args);
    }
} 
