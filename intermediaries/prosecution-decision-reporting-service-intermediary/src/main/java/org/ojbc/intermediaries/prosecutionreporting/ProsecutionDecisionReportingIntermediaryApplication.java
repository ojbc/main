package org.ojbc.intermediaries.prosecutionreporting;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.ImportResource;

/**
 * A sample Spring Boot application that starts the Camel routes.
 */
@SpringBootApplication(exclude = {DataSourceAutoConfiguration.class })
@ComponentScan({"org.ojbc.adapters.prosecutionreporting"})
@ImportResource(value = {"classpath:META-INF/spring/camel-context.xml", 
		"classpath:META-INF/spring/cxf-endpoints.xml",
		"classpath:META-INF/spring/beans.xml"})

public class ProsecutionDecisionReportingIntermediaryApplication extends SpringBootServletInitializer{
	 /**
     * A main method to start this application.
     */
    public static void main(String[] args) {
        SpringApplication.run(ProsecutionDecisionReportingIntermediaryApplication.class, args);
    }
}
