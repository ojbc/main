package org.ojbc.bundles.utiltities.auditing.application;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.ImportResource;
import org.springframework.web.client.RestTemplate;

/**
 * A sample Spring Boot application that starts the Camel routes.
 */
@SpringBootApplication(exclude = {DataSourceAutoConfiguration.class })
@ComponentScan({"org.ojbc.bundles.utiltities.auditing.application"})
@ImportResource(value = {"classpath:META-INF/spring/camel-context.xml", 
		"classpath:META-INF/spring/cxf-endpoints.xml",
		"classpath:META-INF/spring/beans.xml"})
public class AuditRestUtility extends SpringBootServletInitializer{
	public static void main(String[] args) {
        SpringApplication.run(AuditRestUtility.class, args);
    }
	
	@Bean
	public RestTemplate restTemplate(RestTemplateBuilder builder) {
	   // Do any additional configuration here
	   return builder.build();
	}
}

