package org.ojbc.bundles.connectors;

import static org.junit.Assert.assertTrue;

import org.apache.camel.test.spring.CamelSpringJUnit4ClassRunner;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;

@RunWith(CamelSpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {
		"classpath:META-INF/spring/spring-beans-ojb-web-application-connector-context.xml" })
@ActiveProfiles(profiles={"person-search", "incident-search", "vehicle-search", "firearms-search","person-vehicle-to-incident-search", 
		"warrants-query", "criminal-history-query", "firearms-query","incident-report-query", 
		"subscriptions", "policy-acknowledgement", "access-control", "juvenile-query"})
public class CamelContextTest {

	@Test
	public void contextStartup() {
		assertTrue(true);
	}

}
