package org.ojbc.util.osgi.tests;

import static org.junit.Assert.*;

import java.io.File;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.ops4j.pax.exam.junit.PaxExam;
import org.ops4j.pax.exam.spi.reactors.ExamReactorStrategy;
import org.osgi.framework.ServiceReference;
import org.ops4j.pax.exam.spi.reactors.PerMethod;

@RunWith(PaxExam.class)
@ExamReactorStrategy(PerMethod.class)
public class ActivationWithNoPlaceholdersTest extends AbstractActivationTest {
	
	private static final Log log = LogFactory.getLog(ActivationWithNoPlaceholdersTest.class);

	@Test
	public void test() throws Exception {
		
		ServiceReference[] serviceReferences = bundleContext.getAllServiceReferences(null, null);
		
		for (ServiceReference ref : serviceReferences) {
			String beanName = (String) ref.getProperty("org.springframework.osgi.bean.name");
			assertNull(beanName);
		}
		
	}

	@Override
	protected File getConfigFile() {
		return new File("src/test/config/ojb-osgi-common-noprops.cfg");
	}

}
