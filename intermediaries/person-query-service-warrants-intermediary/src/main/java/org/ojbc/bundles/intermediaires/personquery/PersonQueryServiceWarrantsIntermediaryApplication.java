package org.ojbc.bundles.intermediaires.personquery;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.autoconfigure.orm.jpa.HibernateJpaAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.ImportResource;

@SpringBootApplication(exclude= {DataSourceAutoConfiguration.class, 
		HibernateJpaAutoConfiguration.class})
@ComponentScan({"org.ojbc.bundles.intermediaires.personquery"})
@ImportResource(value = {"classpath:META-INF/spring/camel-context.xml", 
		"classpath:META-INF/spring/cxf-endpoints.xml", 
		"classpath:META-INF/spring/beans.xml", 
		"classpath:META-INF/spring/dev-beans.xml"})
public class PersonQueryServiceWarrantsIntermediaryApplication {
	@Autowired
	PersonQueryServiceWarrantsIntermediaryProperties adatperProperties; 
    /**
     * A main method to start this application.
     */
    public static void main(String[] args) {
        SpringApplication.run(PersonQueryServiceWarrantsIntermediaryApplication.class, args);
    }
}
