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
package org.ojbc.intermediaries.sn.tests;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.ops4j.pax.exam.CoreOptions.maven;
import static org.ops4j.pax.exam.CoreOptions.mavenBundle;
import static org.ops4j.pax.exam.CoreOptions.wrappedBundle;
import static org.ops4j.pax.exam.karaf.options.KarafDistributionOption.karafDistributionConfiguration;
import static org.ops4j.pax.exam.karaf.options.KarafDistributionOption.logLevel;

import java.io.File;

import javax.inject.Inject;

import org.apache.camel.component.cxf.CxfEndpoint;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.ojbc.util.camel.processor.MessageProcessor;
import org.ojbc.util.camel.processor.SystemNameToTopicExpressionMapper;
import org.ojbc.util.osgi.OjbcContext;
import org.ojbc.util.osgi.test.AbstractPaxExamIntegrationTest;
import org.ops4j.pax.exam.Configuration;
import org.ops4j.pax.exam.Option;
import org.ops4j.pax.exam.junit.PaxExam;
import org.ops4j.pax.exam.karaf.options.KarafDistributionOption;
import org.ops4j.pax.exam.karaf.options.LogLevelOption.LogLevel;
import org.ops4j.pax.exam.options.MavenArtifactUrlReference;
import org.ops4j.pax.exam.options.MavenUrlReference;
import org.ops4j.pax.exam.spi.reactors.ExamReactorStrategy;
import org.ops4j.pax.exam.spi.reactors.PerClass;
import org.ops4j.pax.exam.util.Filter;
import org.springframework.context.ApplicationContext;
import org.springframework.osgi.context.event.OsgiBundleApplicationContextEvent;
import org.springframework.osgi.context.event.OsgiBundleApplicationContextListener;


/**
 * Integration test for subscription-notification bundles using Pax Exam.
 * 
 */
@RunWith(PaxExam.class)
@ExamReactorStrategy(PerClass.class)
public class SubscriptionNotificationIntegrationTest extends AbstractPaxExamIntegrationTest {

    private static final Log log = LogFactory.getLog(SubscriptionNotificationIntegrationTest.class);

	private static final String KARAF_VERSION = "2.2.11";

	@Inject
	@Filter(value = "(org.springframework.osgi.bean.name=org.ojbc.bundles.intermediaries.subscription-notification-service-intermediary-context)", timeout = 40000)
	private OjbcContext ojbcContext;

	@Inject
	@Filter(value = "(org.springframework.osgi.bean.name=org.ojbc.bundles.intermediaries.parole-event-handler-service-intermediary-context)", timeout = 40000)
	private OjbcContext paroleEventHandlerContext;

	@Inject
	@Filter(value = "(org.springframework.osgi.bean.name=org.ojbc.bundles.intermediaries.probation-event-handler-service-intermediary-context)", timeout = 40000)
	private OjbcContext probationEventHandlerContext;
	
	@Inject
	@Filter(value = "(org.springframework.context.service.name=org.ojbc.bundles.utilities.h2-mock-database)", timeout = 20000)
	private ApplicationContext h2MockBundleApplicationContext;

	@Inject
    @Filter(value = "(org.springframework.context.service.name=subscription-notification-service-intermediary)", timeout = 20000)
    private ApplicationContext mainBundleContext;

	@Inject
	@Filter(value = "(org.springframework.context.service.name=org.ojbc.bundles.intermediaries.parole-event-handler-service-intermediary)", timeout = 20000)
	private ApplicationContext paroleEventHandlerBundleContext;
	
	@Inject
	@Filter(value = "(org.springframework.context.service.name=org.ojbc.bundles.intermediaries.probation-event-handler-service-intermediary)", timeout = 20000)
	private ApplicationContext probationEventHandlerBundleContext;
	
	@Configuration
	public Option[] config() {

		MavenArtifactUrlReference karafUrl = maven().groupId("org.apache.karaf").artifactId("apache-karaf").version(KARAF_VERSION).type("zip");

		MavenUrlReference karafCamelFeature = maven().groupId("org.apache.camel.karaf").artifactId("apache-camel").version("2.10.7").classifier("features").type("xml");

		return new Option[] {

				karafDistributionConfiguration().frameworkUrl(karafUrl).karafVersion(KARAF_VERSION).name("Apache Karaf").unpackDirectory(new File("target/exam")).useDeployFolder(false),

				//keepRuntimeFolder(),

				logLevel(LogLevel.INFO),

				KarafDistributionOption.replaceConfigurationFile("etc/org.ops4j.pax.url.mvn.cfg", new File("src/main/config/org.ops4j.pax.url.mvn.cfg")),
				KarafDistributionOption.replaceConfigurationFile("etc/ojbc.context.services.cfg", new File("src/main/config/ojbc.context.services.cfg")),

				// OJB_Utilities stuff here

				// the cxf-jaxb feature is necessary to turn on java activation.  without it, sending of email will fail.  go figure...
				KarafDistributionOption.features(karafCamelFeature, "cxf-jaxb"),

				KarafDistributionOption.features(karafCamelFeature, "camel"),
				KarafDistributionOption.features(karafCamelFeature, "camel-cxf"),
				KarafDistributionOption.features(karafCamelFeature, "wss4j"),
				KarafDistributionOption.features(karafCamelFeature, "camel-saxon"),

				mavenBundle().groupId("commons-codec").artifactId("commons-codec").version("1.6").start(),
				mavenBundle().groupId("org.apache.servicemix.bundles").artifactId("org.apache.servicemix.bundles.commons-io").version("1.3.2_5").start(),
				mavenBundle().groupId("org.apache.servicemix.bundles").artifactId("org.apache.servicemix.bundles.xpp3").version("1.1.4c_5").start(),
				mavenBundle().groupId("org.apache.servicemix.bundles").artifactId("org.apache.servicemix.bundles.dom4j").version("1.6_1").start(),
				mavenBundle().groupId("org.apache.httpcomponents").artifactId("httpcore-osgi").version("4.2.5").start(),
				mavenBundle().groupId("org.apache.httpcomponents").artifactId("httpclient-osgi").version("4.2.5").start(),
				mavenBundle().groupId("org.springframework").artifactId("spring-jdbc").version("3.0.7.RELEASE").start(),
                mavenBundle().groupId("org.ojbc.bundles.shared").artifactId("ojb-common").start(),
                mavenBundle().groupId("org.ojbc.bundles.shared").artifactId("ojb-camel-common").start(),
				mavenBundle().groupId("org.ojbc.bundles.shared").artifactId("ojb-osgi-common").start(),
				mavenBundle().groupId("org.ojbc.bundles.shared").artifactId("ojb-osgi-test-common").start(),
				mavenBundle().groupId("org.ojbc.bundles.shared").artifactId("ojb-resources-common").start(),

				// _Common intermediary stuff

				mavenBundle().groupId("org.apache.servicemix.bundles").artifactId("org.apache.servicemix.bundles.commons-collections").version("3.2.1_3").start(),
				mavenBundle().groupId("org.apache.servicemix.bundles").artifactId("org.apache.servicemix.bundles.commons-lang").version("2.4_6").start(),
				mavenBundle().groupId("org.apache.servicemix.bundles").artifactId("org.apache.servicemix.bundles.velocity").version("1.7_6").start(),
				mavenBundle().groupId("org.ojbc.bundles.intermediaries").artifactId("subscription-notification-service-intermediary-common").start(),

				// Camel Intermediary dependencies

				mavenBundle().groupId("org.apache.camel").artifactId("camel-mail").version("2.10.7").start(),
				mavenBundle().groupId("org.osgi").artifactId("org.osgi.enterprise").version("4.2.0").start(),
				mavenBundle().groupId("com.h2database").artifactId("h2").version("1.3.174").start(),
				mavenBundle().groupId("org.ojbc.bundles.utilities").artifactId("h2-mock-database").version("1.0.1").start(),

				mavenBundle().groupId("commons-pool").artifactId("commons-pool").version("1.6").start(),
				mavenBundle().groupId("org.apache.servicemix.bundles").artifactId("org.apache.servicemix.bundles.commons-dbcp").version("1.2.2_7").start(),

				// Base intermediary
				mavenBundle().groupId("org.ojbc.bundles.intermediaries").artifactId("subscription-notification-service-intermediary").start(),
				mavenBundle().groupId("org.ojbc.bundles.intermediaries").artifactId("probation-event-handler-service-intermediary").start(),
				mavenBundle().groupId("org.ojbc.bundles.intermediaries").artifactId("parole-event-handler-service-intermediary").start(),

				wrappedBundle(mavenBundle().groupId("org.subethamail").artifactId("subethasmtp").version("3.1.7")).start(),

		};
	}

	@Before
	public void setup() throws Exception {
		bundleContext.registerService(OsgiBundleApplicationContextListener.class.getName(), new ContextListener(), null);
	}

	@Test
	public void testSetup() throws Exception {
		assertNotNull(ojbcContext);
		assertNotNull(paroleEventHandlerContext);
		assertNotNull(probationEventHandlerContext);
		assertNotNull(h2MockBundleApplicationContext);
		assertNotNull(mainBundleContext);
		assertNotNull(paroleEventHandlerBundleContext);
		assertNotNull(probationEventHandlerBundleContext);
		System.err.println(executeCommand("osgi:list -t 1", 20000L, false));
	}
	
	@Test
	public void testBundlesStartup() throws Exception {

		CxfEndpoint notificationBrokerServiceEndpoint = mainBundleContext.getBean("notificationBrokerService", CxfEndpoint.class);
		String notificationBrokerEndpointAddress = notificationBrokerServiceEndpoint.getAddress();
		assertEquals("http://localhost:18040/OJB/SubscribeNotify", notificationBrokerEndpointAddress);
		CxfEndpoint subscriptionManagerServiceEndpoint = mainBundleContext.getBean("subscriptionManagerService", CxfEndpoint.class);
		String subscriptionManagerEndpointAddress = subscriptionManagerServiceEndpoint.getAddress();
        assertEquals("http://localhost:18041/OJB/SubscriptionManager", subscriptionManagerEndpointAddress);

        //Parole event handler up
        CxfEndpoint notificationBrokerCalledByParoleEventHandler = paroleEventHandlerBundleContext.getBean("notificationBrokerService", CxfEndpoint.class);
        String paroleNotificationBrokerAddress = notificationBrokerCalledByParoleEventHandler.getAddress();
        log.info("\nparoleNotificationBrokerAddress: " + paroleNotificationBrokerAddress);
        assertEquals(notificationBrokerEndpointAddress, paroleNotificationBrokerAddress);

        assertNotNull(notificationBrokerCalledByParoleEventHandler);
        
        MessageProcessor subscriptionManagerServiceMessageProcessor = paroleEventHandlerBundleContext.getBean("subscriptionManagerServiceMessageProcessor", 
                org.ojbc.util.camel.processor.MessageProcessor.class);
        
        assertEquals( subscriptionManagerServiceMessageProcessor.getDestinationEndpoint(), "subscriptionManagerService");
        
        //Probation event handler up. 
        CxfEndpoint subscriptionServiceCalledByProbationEventHandler = probationEventHandlerBundleContext.getBean("subscriptionManagerService", CxfEndpoint.class);
        assertNotNull(subscriptionServiceCalledByProbationEventHandler);

        String subscriptionManagerAddressCalledByProbation = subscriptionServiceCalledByProbationEventHandler.getAddress();
        log.info("\nsubscriptionManagerAddressCalledByProbation: " + subscriptionManagerAddressCalledByProbation);
        assertEquals(subscriptionManagerEndpointAddress, subscriptionManagerAddressCalledByProbation);

        
        SystemNameToTopicExpressionMapper systemNameToTopicExpressionMapper = probationEventHandlerBundleContext.getBean("systemNameToTopicExpressionMapper", 
                org.ojbc.util.camel.processor.SystemNameToTopicExpressionMapper.class);
        
        assertEquals(2, systemNameToTopicExpressionMapper.getSystemNameToTopicMap().size());

	}

	private static final class ContextListener implements OsgiBundleApplicationContextListener {

		@Override
		public void onOsgiApplicationEvent(OsgiBundleApplicationContextEvent e) {
			// you can put stuff in here if you want to be notified of osgi-spring context events
		}
	}

}
