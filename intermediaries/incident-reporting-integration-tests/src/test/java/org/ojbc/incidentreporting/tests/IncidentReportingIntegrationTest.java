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
package org.ojbc.incidentreporting.tests;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.ops4j.pax.exam.CoreOptions.maven;
import static org.ops4j.pax.exam.CoreOptions.mavenBundle;
import static org.ops4j.pax.exam.karaf.options.KarafDistributionOption.karafDistributionConfiguration;
import static org.ops4j.pax.exam.karaf.options.KarafDistributionOption.logLevel;

import java.io.File;
import java.util.Collection;

import javax.inject.Inject;

import org.apache.camel.CamelContext;
import org.apache.camel.Endpoint;
import org.apache.camel.component.cxf.CxfEndpoint;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.joda.time.DateTime;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.ojbc.util.osgi.test.AbstractPaxExamIntegrationTest;
import org.ops4j.pax.exam.Configuration;
import org.ops4j.pax.exam.CoreOptions;
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
 * Integration test for Incident Reporting bundles using Pax Exam.
 * The N-DEx intermediary has been upgrade to Java 8 so references to it are commented out.
 * 
 */
@RunWith(PaxExam.class)
@ExamReactorStrategy(PerClass.class)
public class IncidentReportingIntegrationTest extends AbstractPaxExamIntegrationTest {

	private static final Log log = LogFactory.getLog(IncidentReportingIntegrationTest.class);

	private static final String KARAF_VERSION = "2.2.11";

	//Connectors
	@Inject
	@Filter(value = "(org.springframework.context.service.name=org.ojbc.bundles.connectors.incident-reporting-service-connector)", timeout = 40000)
	private ApplicationContext incidentReportingConnectorBundleContext;

	//Intermediaries
	@Inject
	@Filter(value = "(org.springframework.context.service.name=org.ojbc.bundles.intermediaries.incident-reporting-service-intermediary)", timeout = 40000)
	private ApplicationContext incidentReportingBundleContext;
	
	@Inject
	@Filter(value = "(org.springframework.context.service.name=org.ojbc.bundles.intermediaries.arrest-reporting-service-intermediary)", timeout = 40000)
	private ApplicationContext arrestReportingBundleContext;

	//Upgraded to Java 8, comment out for now
	//@Inject
	//@Filter(value = "(org.springframework.context.service.name=org.ojbc.bundles.intermediaries.ndex-submission-service-intermediary)", timeout = 40000)
	//private ApplicationContext ndexBundleContext;

	@Inject
    @Filter(value = "(org.springframework.context.service.name=subscription-notification-service-intermediary)", timeout = 20000)
    private ApplicationContext notificationBrokerBundleContext;
	
	//Adapters
	//Upgraded to Java 8, comment out for now
	//@Inject
	//@Filter(value = "(org.springframework.context.service.name=org.ojbc.bundles.adapters.n-dex-submission-service-mock-adapter)", timeout = 40000)
	//private ApplicationContext ndexMockAdapterBundleContext;

	//Camel Contexts
	private CamelContext incidentReportingConnectorCamelContext;
	
	//Upgraded to Java 8, comment out for now
	//private CamelContext ndexSubmissionServiceCamelContext;

	@Configuration
	public Option[] config() {

		MavenArtifactUrlReference karafUrl = maven().groupId("org.apache.karaf").artifactId("apache-karaf").version(KARAF_VERSION).type("zip");

		MavenUrlReference karafCamelFeature = maven().groupId("org.apache.camel.karaf").artifactId("apache-camel").version("2.10.7").classifier("features").type("xml");

		return new Option[] {

				//Bump up RAM and PermGen to support SSPs
				CoreOptions.vmOptions("-Xmx1G", "-XX:MaxPermSize=128M"),
				
				karafDistributionConfiguration().frameworkUrl(karafUrl).karafVersion(KARAF_VERSION).name("Apache Karaf").unpackDirectory(new File("target/exam")).useDeployFolder(false),

				//keepRuntimeFolder(),

				logLevel(LogLevel.INFO),

				KarafDistributionOption.replaceConfigurationFile("etc/org.ops4j.pax.url.mvn.cfg", new File("src/main/config/org.ops4j.pax.url.mvn.cfg")),
				KarafDistributionOption.replaceConfigurationFile("etc/ojbc.context.services.cfg", new File("src/main/config/ojbc.context.services.cfg")),
				KarafDistributionOption.replaceConfigurationFile("etc/org.apache.cxf.osgi.cfg", new File("src/main/config/org.apache.cxf.osgi.cfg")),
				KarafDistributionOption.replaceConfigurationFile("etc/org.ops4j.pax.web.cfg", new File("src/main/config/org.ops4j.pax.web.cfg")),
				
				// Camel dependencies
				KarafDistributionOption.features(karafCamelFeature, "camel"),
				KarafDistributionOption.features(karafCamelFeature, "camel-cxf"),
				KarafDistributionOption.features(karafCamelFeature, "camel-saxon"),
				KarafDistributionOption.features(karafCamelFeature, "camel-mail"),
				KarafDistributionOption.features(karafCamelFeature, "camel-http"),
				KarafDistributionOption.features(karafCamelFeature, "camel-http4"),

				// Common intermediary stuff
				mavenBundle().groupId("commons-codec").artifactId("commons-codec").version("1.6").start(),
				mavenBundle().groupId("org.apache.servicemix.bundles").artifactId("org.apache.servicemix.bundles.commons-io").version("1.3.2_5").start(),
				mavenBundle().groupId("org.apache.servicemix.bundles").artifactId("org.apache.servicemix.bundles.xpp3").version("1.1.4c_5").start(),
				mavenBundle().groupId("org.apache.servicemix.bundles").artifactId("org.apache.servicemix.bundles.dom4j").version("1.6_1").start(),
				mavenBundle().groupId("org.apache.httpcomponents").artifactId("httpcore-osgi").version("4.2.5").start(),
				mavenBundle().groupId("org.apache.httpcomponents").artifactId("httpclient-osgi").version("4.2.5").start(),
				mavenBundle().groupId("org.apache.servicemix.bundles").artifactId("org.apache.servicemix.bundles.commons-collections").version("3.2.1_3").start(),
				mavenBundle().groupId("org.apache.servicemix.bundles").artifactId("org.apache.servicemix.bundles.commons-lang").version("2.4_6").start(),
				mavenBundle().groupId("org.apache.servicemix.bundles").artifactId("org.apache.servicemix.bundles.not-yet-commons-ssl").start(),
				
				mavenBundle().groupId("org.osgi").artifactId("org.osgi.enterprise").version("4.2.0").start(),
				mavenBundle().groupId("com.h2database").artifactId("h2").version("1.3.174").start(),
				mavenBundle().groupId("commons-pool").artifactId("commons-pool").version("1.6").start(),
				mavenBundle().groupId("org.apache.servicemix.bundles").artifactId("org.apache.servicemix.bundles.commons-dbcp").version("1.2.2_7").start(),

				//Required by S/N bundles
				mavenBundle().groupId("org.apache.servicemix.bundles").artifactId("org.apache.servicemix.bundles.velocity").version("1.7_6").start(),
				
				mavenBundle().groupId("commons-pool").artifactId("commons-pool").version("1.6").start(),
				mavenBundle().groupId("org.springframework").artifactId("spring-jdbc").version("3.0.7.RELEASE").start(),
				
				// OJB shared bundles here
				mavenBundle().groupId("org.ojbc.bundles.shared").artifactId("ojb-osgi-common").start(),
				mavenBundle().groupId("org.ojbc.bundles.shared").artifactId("ojb-osgi-test-common").start(),
				mavenBundle().groupId("org.ojbc.bundles.shared").artifactId("ojb-common").start(),
				mavenBundle().groupId("org.ojbc.bundles.shared").artifactId("ojb-camel-common").start(),
				mavenBundle().groupId("org.ojbc.bundles.shared").artifactId("ojb-resources-common").start(),
				
				//H2 Mock bundle
				mavenBundle().groupId("org.ojbc.bundles.utilities").artifactId("h2-mock-database").start(),
				
				// intermediaries
				//mavenBundle().groupId("org.ojbc.bundles.intermediaries").artifactId("ndex-submission-service-intermediary").start(),
				mavenBundle().groupId("org.ojbc.bundles.intermediaries").artifactId("arrest-reporting-service-intermediary").start(),
				mavenBundle().groupId("org.ojbc.bundles.intermediaries").artifactId("incident-reporting-service-intermediary-common").start(),
				mavenBundle().groupId("org.ojbc.bundles.intermediaries").artifactId("incident-reporting-service-intermediary").version("1.0.2").start(),
				mavenBundle().groupId("org.ojbc.bundles.intermediaries").artifactId("subscription-notification-service-intermediary-common").version("1.0.1").start(),
				mavenBundle().groupId("org.ojbc.bundles.intermediaries").artifactId("subscription-notification-service-intermediary").start(),

				//Connector
				mavenBundle().groupId("org.ojbc.bundles.connectors").artifactId("incident-reporting-service-connector").start(),
				
				//Adapters
				//mavenBundle().groupId("org.ojbc.bundles.adapters").artifactId("n-dex-submission-service-mock-adapter").start(),
		};
	}

	@Before
	public void setup() throws Exception {
		bundleContext.registerService(OsgiBundleApplicationContextListener.class.getName(), new ContextListener(), null);
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void testSetup() throws Exception {
		//Confirm all bundle contexts properly start
		assertNotNull(arrestReportingBundleContext);
		assertNotNull(incidentReportingBundleContext);
		assertNotNull(incidentReportingConnectorBundleContext);
		//assertNotNull(ndexBundleContext);
		//assertNotNull(ndexMockAdapterBundleContext);
		assertNotNull(notificationBrokerBundleContext);
		
		System.err.println(executeCommand("osgi:list -t 1", 20000L, false));
	}
	
	@Test
	public void testQueryBundleStartup() throws Exception {

		//Test startup of Arrest Reporting Service by getting the web service endpoint
		CxfEndpoint arrestReportingServiceEndpoint = arrestReportingBundleContext.getBean("arrestReportingServiceEndpoint", CxfEndpoint.class);
		String arrestServiceAddress = arrestReportingServiceEndpoint.getAddress();

		assertNotNull(arrestReportingServiceEndpoint);
		
		log.info("Arrest Reporting Service Endpoint: " + arrestServiceAddress);

		assertEquals("/intermediary/ArrestReportingService", arrestServiceAddress);
		
		//Test startup of Incident Reporting Service by getting the web service endpoint
		CxfEndpoint incidentReportingServiceEndpoint = incidentReportingBundleContext.getBean("incidentReportingServiceEndpoint", CxfEndpoint.class);
		String incidentServiceAddress = incidentReportingServiceEndpoint.getAddress();

		assertNotNull(incidentReportingServiceEndpoint);
		
		log.info("Incident Reporting Service Endpoint: " + incidentServiceAddress);

		assertEquals("/intermediary/IncidentReportingService", incidentServiceAddress);

		//Test startup of Ndex Submission Service by getting the web service endpoint
		//CxfEndpoint ndexSubmissionServiceEndpoint = ndexBundleContext.getBean("ndexWebserviceFacadeEndpoint", CxfEndpoint.class);
		//String ndexSubmissionAddress = ndexSubmissionServiceEndpoint.getAddress();

		//assertNotNull(ndexSubmissionAddress);
		
		//log.info("N-DEx Submission Service Endpoint: " + ndexSubmissionAddress);

		//assertEquals("/intermediary/N-DexSubmissionService", ndexSubmissionAddress);
	
		//Subscription Notification
		CxfEndpoint notificationBrokerServiceEndpoint = notificationBrokerBundleContext.getBean("notificationBrokerService", CxfEndpoint.class);
		String notificationBrokerEndpointAddress = notificationBrokerServiceEndpoint.getAddress();
		assertEquals(notificationBrokerEndpointAddress,"http://localhost:18040/OJB/SubscribeNotify");
			
		log.info("Notification Broker Endpoint: " + notificationBrokerEndpointAddress);
		
		CxfEndpoint subscriptionManagerServiceEndpoint = notificationBrokerBundleContext.getBean("subscriptionManagerService", CxfEndpoint.class);
		String subscriptionManagerEndpointAddress = subscriptionManagerServiceEndpoint.getAddress();	
		assertEquals(subscriptionManagerEndpointAddress,"http://localhost:18041/OJB/SubscriptionManager");
		
		
		log.info("Subscription Management Endpoint: " + subscriptionManagerEndpointAddress);
		
	}
	
	@Test
	public void testIncidentReportFlow() throws Exception {

		//Retrieve camel contexts to get properties from bundle
		incidentReportingConnectorCamelContext = getOsgiService(CamelContext.class, "(camel.context.name=incident-reporting-service-connector)", 40000);
		
		//Upgraded to Java 8, comment out for now
		//ndexSubmissionServiceCamelContext = getOsgiService(CamelContext.class, "(camel.context.name=ndex-submission-service-intermediary)", 40000);
		
		assertNotNull(incidentReportingConnectorCamelContext);
		
		//Upgraded to Java 8, comment out for now
		//assertNotNull(ndexSubmissionServiceCamelContext);

		//Get input directory by resolving property
		String inputDirectoryString = incidentReportingConnectorCamelContext.resolvePropertyPlaceholders("{{incidentChargeReporting.ConnectorFileDirectory}}") + "/input";
		log.info("Connector input directory:"+ inputDirectoryString);

		//Upgraded to Java 8, comment out for now
		//Get intermediary output directory, due to Camel bug, it is retrieved by endpoint
		//Endpoint ndexSuccessEndpoint = ndexSubmissionServiceCamelContext.getEndpoint("ndexSubmissionSuccessDir");
		//assertNotNull(ndexSuccessEndpoint);
		
		//log.info("NDEx output directory:"+ ndexSuccessEndpoint.getEndpointUri());
		
		//String ndexSuccessEndpointDirectoryString = StringUtils.substringAfter(ndexSuccessEndpoint.getEndpointUri(), "file:///");
		
		//Clean the input directory up for a clean test
		File inputDirectory = new File(inputDirectoryString);
		
		if (inputDirectory.exists())
		{	
			FileUtils.cleanDirectory(inputDirectory);
		}

		//Clean the ndex directory up for a clean test
		//File ndexDirectory = new File(ndexSuccessEndpointDirectoryString);
		
		//if (ndexDirectory.exists())
		//{	
		//	FileUtils.cleanDirectory(ndexDirectory);
		//}
		
		DateTime now = new DateTime();
		
		File inputFile = new File(getMavenProjectRootDirectory(), "src/test/resources/xmlInstances/IncidentReport.xml");
		assertNotNull(inputFile);
		
		//Kick off the process by copying the incident report to the input directory (reportable to DA is set to false in incident report)
		FileUtils.copyFileToDirectory(inputFile, inputDirectory);
		
		//Sleep while processing happens
		Thread.sleep(15000);
		
		//File ndexDirectoryForToday = new File(ndexSuccessEndpointDirectoryString + "/" +  now.toString("yyyyMMdd") );
		
		//Assert that we get exactly one file in the ndex 'success' directory and then delete it
		//String[] extensions = {"xml"};
		//Collection<File> files = FileUtils.listFiles(ndexDirectoryForToday, extensions, true);
		
		//assertEquals(1, files.size());
		
		//File theNdexResponseFile = files.iterator().next();
		
		//Confirm ndex file name is correct in 'success' directory
		//assertTrue(theNdexResponseFile.getName().startsWith("INCIDENT_14BU000056"));
		//assertTrue(theNdexResponseFile.getName().endsWith(".xml"));
		
		//TODO: Support Arrest Reporting Service, Charge Referral Service and Notification Broker Service
		
	}

	private static final class ContextListener implements OsgiBundleApplicationContextListener {

		@Override
		public void onOsgiApplicationEvent(OsgiBundleApplicationContextEvent e) {
			// you can put stuff in here if you want to be notified of osgi-spring context events
		}
	}

}
