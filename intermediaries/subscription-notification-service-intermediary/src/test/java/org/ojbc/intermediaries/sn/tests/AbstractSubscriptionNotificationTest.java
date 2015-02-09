package org.ojbc.intermediaries.sn.tests;

import org.apache.camel.test.junit4.CamelSpringJUnit4ClassRunner;
import org.junit.runner.RunWith;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ContextConfiguration;

@RunWith(CamelSpringJUnit4ClassRunner.class)
@DirtiesContext
@ContextConfiguration(locations={
		"classpath:META-INF/spring/camel-context.xml",
		"classpath:META-INF/spring/email-strategies.xml",
		"classpath:META-INF/spring/email-formatters.xml",
		"classpath:META-INF/spring/cxf-endpoints.xml",
		"classpath:META-INF/spring/dao.xml",
		"classpath:META-INF/spring/extensible-beans.xml",
		"classpath:META-INF/spring/properties-context.xml",
		"classpath:META-INF/spring/search-query-routes.xml",
		"classpath:META-INF/spring/subscription-secure-routes.xml",
		"classpath:META-INF/spring/local-osgi-context.xml",
		"classpath:META-INF/spring/h2-mock-database-application-context.xml",
		"classpath:META-INF/spring/h2-mock-database-context-subscription.xml",
}) 
public abstract class AbstractSubscriptionNotificationTest {
	
}
