/*
 * Unless explicitly acquired and licensed from Licensor under another license, the contents of
 * this file are subject to the Reciprocal Public License ("RPL") Version 1.5, or subsequent
 * versions as allowed by the RPL, and You may not copy or use this file in either source code
 * or executable form, except in compliance with the terms and conditions of the RPL
 *
 * All software distributed under the RPL is provided strictly on an "AS IS" basis, WITHOUT
 * WARRANTY OF ANY KIND, EITHER EXPRESS OR IMPLIED, AND LICENSOR HEREBY DISCLAIMS ALL SUCH
 * WARRANTIES, INCLUDING WITHOUT LIMITATION, ANY WARRANTIES OF MERCHANTABILITY, FITNESS FOR A
 * PARTICULAR PURPOSE, QUIET ENJOYMENT, OR NON-INFRINGEMENT. See the RPL for specific language
 * governing rights and limitations under the RPL.
 *
 * http://opensource.org/licenses/RPL-1.5
 *
 * Copyright 2012-2017 Open Justice Broker Consortium
 */
package org.ojbc.util.osgi.tests;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.io.File;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.ojbc.util.osgi.OjbcContext;
import org.ops4j.pax.exam.junit.PaxExam;
import org.ops4j.pax.exam.spi.reactors.ExamReactorStrategy;
import org.ops4j.pax.exam.spi.reactors.PerMethod;
import org.osgi.framework.ServiceReference;

@RunWith(PaxExam.class)
@ExamReactorStrategy(PerMethod.class)
public class ActivationWithTwoServicesTest extends AbstractActivationTest {

	private static final Log log = LogFactory.getLog(ActivationWithTwoServicesTest.class);

	@Test
	public void test() throws Exception {

		ServiceReference[] serviceReferences = bundleContext.getAllServiceReferences(null, null);

		ServiceReference foundService1 = null;
		ServiceReference foundService2 = null;

		for (ServiceReference ref : serviceReferences) {
			String beanName = (String) ref.getProperty("org.springframework.osgi.bean.name");
			if (beanName != null) {
				if ("service1".equals(beanName)) {
					foundService1 = ref;
				} else if ("service2".equals(beanName)) {
					foundService2 = ref;
				}
			}
		}

		assertNotNull(foundService1);
		assertTrue(OjbcContext.class.isAssignableFrom(bundleContext.getService(foundService1).getClass()));
		assertNotNull(foundService2);
		assertTrue(OjbcContext.class.isAssignableFrom(bundleContext.getService(foundService2).getClass()));

	}

	@Override
	protected File getConfigFile() {
		return new File("src/test/config/ojb-osgi-common-two-services.cfg");
	}

}
