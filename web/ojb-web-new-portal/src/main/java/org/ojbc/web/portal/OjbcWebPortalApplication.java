package org.ojbc.web.portal;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.ImportResource;

@SpringBootApplication
@ImportResource({"classpath:META-INF/spring/spring-beans-ojb-web-application-connector-context.xml"})
public class OjbcWebPortalApplication {

	public static void main(String[] args) {
		ApplicationContext applicationContext = SpringApplication.run(OjbcWebPortalApplication.class, args);
		
		for (String name : applicationContext.getBeanDefinitionNames()) {
			System.out.println(name);
		}
	}
}
