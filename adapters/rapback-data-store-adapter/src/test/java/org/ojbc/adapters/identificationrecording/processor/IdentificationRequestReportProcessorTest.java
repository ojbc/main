package org.ojbc.adapters.identificationrecording.processor;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {
        "classpath:META-INF/spring/spring-context.xml",
        "classpath:META-INF/spring/dao.xml",
        "classpath:META-INF/spring/properties-context.xml",
		})
public class IdentificationRequestReportProcessorTest {

	@Autowired
	IdentificationRequestReportProcessor identificationRequestReportProcessor;
	
	@Before
	public void setUp() throws Exception {
		assertNotNull(identificationRequestReportProcessor);
	}

	@Test
	public void test() {
		//fail("Not yet implemented");
	}

}
