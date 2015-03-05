package org.ojbc.bundles.intermediaries;

import static org.junit.Assert.assertTrue;

import org.apache.camel.test.junit4.CamelSpringJUnit4ClassRunner;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;

@RunWith(CamelSpringJUnit4ClassRunner.class)
@ContextConfiguration(locations={
		"classpath:META-INF/spring/camel-context.xml", 
		"classpath:META-INF/spring/cxf-endpoints.xml",
		"classpath:META-INF/spring/properties-context.xml"}) 
public class CamelContextTest {

	@Test
	public void contextStartup() {
		assertTrue(true);
	}

}
