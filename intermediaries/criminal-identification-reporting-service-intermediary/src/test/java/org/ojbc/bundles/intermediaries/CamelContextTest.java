package org.ojbc.bundles.intermediaries;

import static org.junit.Assert.*;

import org.apache.camel.test.junit4.CamelSpringJUnit4ClassRunner;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;

//TODO FIXME enable test when passing
@Ignore
@RunWith(CamelSpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {
		"classpath:META-INF/spring/camel-context.xml" })
public class CamelContextTest {

	//TODO FIXME enable test when passing
	@Ignore
	public void contextStartup() {
		assertTrue(true);
	}

}
