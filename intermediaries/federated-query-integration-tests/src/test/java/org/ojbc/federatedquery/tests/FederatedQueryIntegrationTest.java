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
package org.ojbc.federatedquery.tests;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.ops4j.pax.exam.CoreOptions.maven;
import static org.ops4j.pax.exam.CoreOptions.mavenBundle;
import static org.ops4j.pax.exam.karaf.options.KarafDistributionOption.karafDistributionConfiguration;
import static org.ops4j.pax.exam.karaf.options.KarafDistributionOption.logLevel;

import java.io.File;

import javax.inject.Inject;

import org.apache.camel.component.cxf.CxfEndpoint;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.ojbc.util.osgi.OjbcContext;
import org.ojbc.util.osgi.test.AbstractPaxExamIntegrationTest;
import org.ops4j.pax.exam.Configuration;
import org.ops4j.pax.exam.Option;
import org.ops4j.pax.exam.junit.PaxExam;
import org.ops4j.pax.exam.karaf.options.KarafDistributionOption;
import org.ops4j.pax.exam.karaf.options.LogLevelOption.LogLevel;
import org.ops4j.pax.exam.options.MavenArtifactUrlReference;
import org.ops4j.pax.exam.options.MavenUrlReference;
import org.ops4j.pax.exam.options.extra.VMOption;
import org.ops4j.pax.exam.spi.reactors.ExamReactorStrategy;
import org.ops4j.pax.exam.spi.reactors.PerClass;
import org.ops4j.pax.exam.util.Filter;
import org.springframework.context.ApplicationContext;
import org.springframework.osgi.context.event.OsgiBundleApplicationContextEvent;
import org.springframework.osgi.context.event.OsgiBundleApplicationContextListener;

/**
 * Integration test for federated query bundles using Pax Exam.
 * 
 */
@RunWith(PaxExam.class)
@ExamReactorStrategy(PerClass.class)
public class FederatedQueryIntegrationTest extends AbstractPaxExamIntegrationTest {

	private static final Log log = LogFactory.getLog(FederatedQueryIntegrationTest.class);

	private static final String KARAF_VERSION = "2.2.11";

	//Person Search
	@Inject
	@Filter(value = "(org.springframework.osgi.bean.name=org.ojbc.bundles.intermediaries.person-search-request-service-intermediary-context)", timeout = 60000)
	private OjbcContext personSearchIntermediaryOjbcContext;

	@Inject
	@Filter(value = "(org.springframework.context.service.name=org.ojbc.bundles.intermediaries.person-search-request-service-intermediary)", timeout = 60000)
	private ApplicationContext personSearchIntermediaryBundleContext;

	//Vehicle Search
	@Inject
	@Filter(value = "(org.springframework.osgi.bean.name=org.ojbc.bundles.intermediaries.vehicle-search-request-service-intermediary-context)", timeout = 60000)
	private OjbcContext vehicleSearchIntermediaryOjbcContext;

	@Inject
	@Filter(value = "(org.springframework.context.service.name=org.ojbc.bundles.intermediaries.vehicle-search-request-service-intermediary)", timeout = 60000)
	private ApplicationContext vehicleSearchIntermediaryBundleContext;
	
	//Firearms Search
	@Inject
	@Filter(value = "(org.springframework.osgi.bean.name=org.ojbc.bundles.intermediaries.firearms-search-request-service-intermediary-context)", timeout = 60000)
	private OjbcContext firearmsSearchIntermediaryOjbcContext;

	@Inject
	@Filter(value = "(org.springframework.context.service.name=org.ojbc.bundles.intermediaries.firearm-search-request-service-intermediary)", timeout = 60000)
	private ApplicationContext firearmsSearchIntermediaryBundleContext;	

	//Incident Search
	@Inject
	@Filter(value = "(org.springframework.osgi.bean.name=org.ojbc.bundles.intermediaries.incident-search-request-service-intermediary-context)", timeout = 60000)
	private OjbcContext incidentSearchIntermediaryOjbcContext;

	@Inject
	@Filter(value = "(org.springframework.context.service.name=org.ojbc.bundles.intermediaries.incident-search-request-service-intermediary)", timeout = 60000)
	private ApplicationContext incidentSearchIntermediaryBundleContext;	
	
	//Person Query - Warrants
	@Inject
	@Filter(value = "(org.springframework.osgi.bean.name=org.ojbc.bundles.intermediaries.person-query-service-warrants-intermediary-context)", timeout = 60000)
	private OjbcContext warrantsQueryIntermediaryOjbcContext;

	@Inject
	@Filter(value = "(org.springframework.context.service.name=org.ojbc.bundles.intermediaries.person-query-service-warrants-intermediary)", timeout = 60000)
	private ApplicationContext warrantsQueryIntermediaryBundleContext;
	
	//Person Query - Criminal History
	@Inject
	@Filter(value = "(org.springframework.osgi.bean.name=org.ojbc.bundles.intermediaries.person-query-service-criminal-history-intermediary-context)", timeout = 60000)
	private OjbcContext criminalHistoryQueryIntermediaryOjbcContext;

	@Inject
	@Filter(value = "(org.springframework.context.service.name=org.ojbc.bundles.intermediaries.person-query-service-criminal-history-intermediary)", timeout = 60000)
	private ApplicationContext criminalHistoryQueryIntermediaryBundleContext;	
	
	//Firearms Query
	@Inject
	@Filter(value = "(org.springframework.osgi.bean.name=org.ojbc.bundles.intermediaries.firearm-registration-query-request-service-intermediary-context)", timeout = 60000)
	private OjbcContext firearmsQueryIntermediaryOjbcContext;

	@Inject
	@Filter(value = "(org.springframework.context.service.name=org.ojbc.bundles.intermediaries.firearm-registration-query-request-service-intermediary)", timeout = 60000)
	private ApplicationContext firearmsQueryIntermediaryBundleContext;	

	//Incident Report Query
	@Inject
	@Filter(value = "(org.springframework.osgi.bean.name=org.ojbc.bundles.intermediaries.incident-report-request-service-intermediary-context)", timeout = 60000)
	private OjbcContext incidentReportQueryIntermediaryOjbcContext;

	@Inject
	@Filter(value = "(org.springframework.context.service.name=org.ojbc.bundles.intermediaries.incident-report-request-service-intermediary)", timeout = 60000)
	private ApplicationContext incidentReportQueryIntermediaryBundleContext;	

	//Juvenile History Query
	@Inject
	@Filter(value = "(org.springframework.osgi.bean.name=org.ojbc.bundles.intermediaries.juvenile-history-service-intermediary-context)", timeout = 20000)
	private OjbcContext juvenileQueryIntermediaryOjbcContext;

	@Inject
	@Filter(value = "(org.springframework.context.service.name=org.ojbc.bundles.intermediaries.juvenile-history-service-intermediary)", timeout = 20000)
	private ApplicationContext juvenileQueryIntermediaryBundleContext;	
	
	@Inject
	@Filter(value = "(org.springframework.osgi.bean.name=org.ojbc.bundles.intermediaries.subscription-notification-service-intermediary-context)", timeout = 20000)
	private OjbcContext subscriptionNotificationContext;

	@Inject
	@Filter(value = "(org.springframework.context.service.name=subscription-notification-service-intermediary)", timeout = 20000)
	private ApplicationContext subscriptionNotificationBundleContext;
	
	
	@Configuration
	public Option[] config() {

		MavenArtifactUrlReference karafUrl = maven().groupId("org.apache.karaf").artifactId("apache-karaf").version(KARAF_VERSION).type("zip");

		MavenUrlReference karafCamelFeature = maven().groupId("org.apache.camel.karaf").artifactId("apache-camel").version("2.10.7").classifier("features").type("xml");
		MavenUrlReference karafPaxWebFeature = maven().groupId("org.ops4j.pax.web").artifactId("pax-web-features").version("2.1.0").classifier("features").type("xml");
		MavenUrlReference karafStandardFeatures = maven().groupId("org.apache.karaf.assemblies.features").artifactId("standard").version("2.2.11").classifier("features").type("xml");

		return new Option[] {
				
				//Need extra memory due to the number of bundles being installed and their service specs
				new VMOption("-Xmx1G"),
				
				karafDistributionConfiguration().frameworkUrl(karafUrl).karafVersion(KARAF_VERSION).name("Apache Karaf").unpackDirectory(new File("target/exam")).useDeployFolder(false),

				//keepRuntimeFolder(), 	
				logLevel(LogLevel.INFO),

				KarafDistributionOption.replaceConfigurationFile("etc/org.ops4j.pax.url.mvn.cfg", new File("src/main/config/org.ops4j.pax.url.mvn.cfg")),
				KarafDistributionOption.replaceConfigurationFile("etc/org.apache.cxf.osgi.cfg", new File("src/main/config/org.apache.cxf.osgi.cfg")),
				KarafDistributionOption.replaceConfigurationFile("etc/org.ops4j.pax.web.cfg", new File("src/main/config/org.ops4j.pax.web.cfg")),
				KarafDistributionOption.replaceConfigurationFile("etc/ojbc.context.services.cfg", replaceConfigurationFile()),
				
				KarafDistributionOption.features(karafCamelFeature, "camel"),
				KarafDistributionOption.features(karafCamelFeature, "camel-cxf"),
				KarafDistributionOption.features(karafCamelFeature, "camel-saxon"),
				KarafDistributionOption.features(karafCamelFeature, "camel-mail"),
				
				//Jetty SSL / HTTP features
				KarafDistributionOption.features(karafPaxWebFeature, "pax-jetty"),
				KarafDistributionOption.features(karafStandardFeatures, "http"),
				KarafDistributionOption.features(karafStandardFeatures, "jetty"),

				mavenBundle().groupId("commons-codec").artifactId("commons-codec").version("1.6").start(),
				mavenBundle().groupId("org.apache.servicemix.bundles").artifactId("org.apache.servicemix.bundles.commons-io").version("1.3.2_5").start(),
				mavenBundle().groupId("org.apache.servicemix.bundles").artifactId("org.apache.servicemix.bundles.xpp3").version("1.1.4c_5").start(),
				mavenBundle().groupId("org.apache.servicemix.bundles").artifactId("org.apache.servicemix.bundles.dom4j").version("1.6_1").start(),
				mavenBundle().groupId("org.apache.httpcomponents").artifactId("httpcore-osgi").version("4.2.5").start(),
				mavenBundle().groupId("org.apache.httpcomponents").artifactId("httpclient-osgi").version("4.2.5").start(),
				mavenBundle().groupId("org.springframework").artifactId("spring-jdbc").version("3.0.7.RELEASE").start(),
				mavenBundle().groupId("org.apache.commons").artifactId("commons-math3").version("3.2").start(),
				
				// _Common intermediary stuff

				mavenBundle().groupId("org.apache.servicemix.bundles").artifactId("org.apache.servicemix.bundles.commons-collections").version("3.2.1_3").start(),
				mavenBundle().groupId("org.apache.servicemix.bundles").artifactId("org.apache.servicemix.bundles.commons-lang").version("2.4_6").start(),
				

				// Camel Intermediary dependencies

				mavenBundle().groupId("org.osgi").artifactId("org.osgi.enterprise").version("4.2.0").start(),
				mavenBundle().groupId("com.h2database").artifactId("h2").version("1.3.174").start(),
				mavenBundle().groupId("commons-pool").artifactId("commons-pool").version("1.6").start(),
				mavenBundle().groupId("org.apache.servicemix.bundles").artifactId("org.apache.servicemix.bundles.commons-dbcp").version("1.2.2_7").start(),

				// OJB bundles here
				mavenBundle().groupId("org.ojbc.bundles.utilities").artifactId("h2-mock-database").start(),
				
				mavenBundle().groupId("org.ojbc.bundles.shared").artifactId("ojb-osgi-test-common").start(),
				mavenBundle().groupId("org.ojbc.bundles.shared").artifactId("ojb-common").start(),
				mavenBundle().groupId("org.ojbc.bundles.shared").artifactId("ojb-camel-common").start(),
				mavenBundle().groupId("org.ojbc.bundles.shared").artifactId("ojb-fedquery-common").start(),
				mavenBundle().groupId("org.ojbc.bundles.shared").artifactId("ojb-resources-common").start(),
				mavenBundle().groupId("org.ojbc.bundles.shared").artifactId("ojb-osgi-common").start(),
								
				//Entity Resolution Bundles
				mavenBundle().groupId("gov.nij.bundles.shared").artifactId("Entity_Resolution_Resources").start(),
								
				// Search intermediaries
				mavenBundle().groupId("org.ojbc.bundles.intermediaries").artifactId("person-search-request-service-intermediary").start(),
				mavenBundle().groupId("org.ojbc.bundles.intermediaries").artifactId("vehicle-search-request-service-intermediary").start(),
				mavenBundle().groupId("org.ojbc.bundles.intermediaries").artifactId("firearm-search-request-service-intermediary").start(),
				mavenBundle().groupId("org.ojbc.bundles.intermediaries").artifactId("incident-search-request-service-intermediary").start(),
				
				// Query intermediaries
				mavenBundle().groupId("org.ojbc.bundles.intermediaries").artifactId("person-query-service-warrants-intermediary").start(),
				mavenBundle().groupId("org.ojbc.bundles.intermediaries").artifactId("person-query-service-criminal-history-intermediary").start(),
				mavenBundle().groupId("org.ojbc.bundles.intermediaries").artifactId("firearm-registration-query-request-service-intermediary").start(),
				mavenBundle().groupId("org.ojbc.bundles.intermediaries").artifactId("incident-report-request-service-intermediary").start(),
				mavenBundle().groupId("org.ojbc.bundles.intermediaries").artifactId("juvenile-history-service-intermediary").start(),
				
				//Subscription Notification Intermediary
				mavenBundle().groupId("org.apache.servicemix.bundles").artifactId("org.apache.servicemix.bundles.velocity").version("1.7_6").start(),
				mavenBundle().groupId("org.ojbc.bundles.intermediaries").artifactId("subscription-notification-service-intermediary-common").start(),
				mavenBundle().groupId("org.ojbc.bundles.intermediaries").artifactId("subscription-notification-service-intermediary").start(),
				

		};
	}

	private File replaceConfigurationFile() {
		File contextConfigurationFile =new File("src/main/config/ojbc.context.services.cfg"); 
		return contextConfigurationFile;
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
		assertNotNull(personSearchIntermediaryOjbcContext);
		assertNotNull(personSearchIntermediaryBundleContext);
		assertNotNull(vehicleSearchIntermediaryOjbcContext);
		assertNotNull(vehicleSearchIntermediaryBundleContext);
		assertNotNull(firearmsSearchIntermediaryOjbcContext);
		assertNotNull(firearmsSearchIntermediaryBundleContext);
		assertNotNull(incidentSearchIntermediaryOjbcContext);
		assertNotNull(incidentSearchIntermediaryBundleContext);

		assertNotNull(warrantsQueryIntermediaryOjbcContext);
		assertNotNull(warrantsQueryIntermediaryBundleContext);
		assertNotNull(criminalHistoryQueryIntermediaryOjbcContext);
		assertNotNull(criminalHistoryQueryIntermediaryBundleContext);
		assertNotNull(firearmsQueryIntermediaryOjbcContext);
		assertNotNull(firearmsQueryIntermediaryBundleContext);
		assertNotNull(incidentReportQueryIntermediaryOjbcContext);
		assertNotNull(incidentReportQueryIntermediaryBundleContext);		
		assertNotNull(juvenileQueryIntermediaryOjbcContext);
		assertNotNull(juvenileQueryIntermediaryBundleContext);
		
		assertNotNull(subscriptionNotificationContext);
		assertNotNull(subscriptionNotificationBundleContext);

		
		System.err.println(executeCommand("osgi:list -t 1", 20000L, false));
	}
	
	@Test
	public void testQueryBundleStartup() throws Exception {
		
		//Person Search
		CxfEndpoint personSearchRequestEndpoint = personSearchIntermediaryBundleContext.getBean("searchRequestFederatedServiceEndpoint", CxfEndpoint.class);
		String personSearchRequestEndpointAddress = personSearchRequestEndpoint.getAddress();
		assertEquals(personSearchRequestEndpointAddress, "/intermediary/PersonSearchRequestService");

		assertNotNull(personSearchRequestEndpoint);
		log.info("Person Search Federated Endpoint: " + personSearchRequestEndpointAddress);

		//Vehicle Search
		CxfEndpoint vehicleSearchRequestEndpoint = vehicleSearchIntermediaryBundleContext.getBean("searchRequestFederatedServiceEndpoint", CxfEndpoint.class);
		String vehicleSearchRequestEndpointAddress = vehicleSearchRequestEndpoint.getAddress();
		assertEquals(vehicleSearchRequestEndpointAddress, "/intermediary/VehicleSearchRequestService");
		
		assertNotNull(vehicleSearchRequestEndpoint);
		log.info("Vehicle Search Federated Endpoint: " + vehicleSearchRequestEndpointAddress);

		//Firearms Search
		CxfEndpoint firearmsSearchRequestEndpoint = firearmsSearchIntermediaryBundleContext.getBean("searchRequestFederatedServiceEndpoint", CxfEndpoint.class);
		String firearmsSearchRequestEndpointAddress = firearmsSearchRequestEndpoint.getAddress();
		assertEquals(firearmsSearchRequestEndpointAddress, "/intermediary/FirearmSearchRequestService");
		
		assertNotNull(firearmsSearchRequestEndpoint);
		log.info("Firearms Search Federated Endpoint: " + firearmsSearchRequestEndpointAddress);

		//Incident Search
		CxfEndpoint incidentSearchRequestEndpoint = incidentSearchIntermediaryBundleContext.getBean("searchRequestFederatedServiceEndpoint", CxfEndpoint.class);
		String incidentSearchRequestEndpointAddress = incidentSearchRequestEndpoint.getAddress();
		assertEquals(incidentSearchRequestEndpointAddress, "/intermediary/IncidentSearchRequestService");

		assertNotNull(incidentSearchRequestEndpoint);
		log.info("Incident Search Federated Endpoint: " + incidentSearchRequestEndpointAddress);

		
		//Warrants Query
		CxfEndpoint personQueryWarrantsEndpoint = warrantsQueryIntermediaryBundleContext.getBean("searchRequestFederatedServiceEndpoint", CxfEndpoint.class);
		String personQueryWarrantsEndpointAddress = personQueryWarrantsEndpoint.getAddress();
		assertEquals(personQueryWarrantsEndpointAddress, "/intermediary/PersonQueryServiceWarrants");
		assertNotNull(personQueryWarrantsEndpoint);
		
		log.info("Person Query Warrants Federated Endpoint: " + personQueryWarrantsEndpointAddress);

		//Criminal History Query
        CxfEndpoint personQueryCriminalHistoryEndpoint = criminalHistoryQueryIntermediaryBundleContext.getBean("searchRequestFederatedServiceEndpoint", CxfEndpoint.class);
        String personQueryCriminalHistoryEndpointAddress = personQueryCriminalHistoryEndpoint.getAddress();
        assertEquals(personQueryCriminalHistoryEndpointAddress, "/intermediary/PersonQueryServiceCriminalHistory");

        assertNotNull(personQueryCriminalHistoryEndpoint);
        
		log.info("Person Query Criminal History Federated Endpoint: " + personQueryCriminalHistoryEndpointAddress);

		//Firearms Query
		CxfEndpoint firearmRegistrationQueryRequestFederatedServiceEndpoint = firearmsQueryIntermediaryBundleContext.getBean("firearmRegistrationQueryRequestFederatedServiceEndpoint", CxfEndpoint.class);
		assertNotNull(firearmRegistrationQueryRequestFederatedServiceEndpoint);
		
		String firearmRegistrationQueryRequestFederatedServiceEndpointAddress = firearmRegistrationQueryRequestFederatedServiceEndpoint.getAddress();
		assertEquals("/intermediary/FirearmRegistrationQueryRequestService", firearmRegistrationQueryRequestFederatedServiceEndpointAddress);
		
		log.info("Firearms Query Federated Endpoint: " + firearmRegistrationQueryRequestFederatedServiceEndpointAddress);
	
		//Incident Report Query
		CxfEndpoint incidentReportRequestFederatedServiceEndpoint = incidentReportQueryIntermediaryBundleContext.getBean("incidentReportRequestFederatedServiceEndpoint", CxfEndpoint.class);
		String incidentReportRequestFederatedServiceEndpointAddress = incidentReportRequestFederatedServiceEndpoint.getAddress();
		assertEquals("/intermediary/IncidentReportRequestService", incidentReportRequestFederatedServiceEndpointAddress);

		assertNotNull(incidentReportRequestFederatedServiceEndpoint);
		
		log.info("Incident Report Query Federated Endpoint: " + incidentReportRequestFederatedServiceEndpointAddress);
		
		//Juvenile Query
		CxfEndpoint juvenileCasePlanHistoryRequestFederatedService = juvenileQueryIntermediaryBundleContext.getBean("juvenileCasePlanHistoryRequestFederatedService", CxfEndpoint.class);
		assertNotNull(juvenileCasePlanHistoryRequestFederatedService);
		
		String juvenileCasePlanHistoryRequestFederatedServiceAddress = juvenileCasePlanHistoryRequestFederatedService.getAddress();
		assertEquals(juvenileCasePlanHistoryRequestFederatedServiceAddress,"/intermediary/CasePlanHistoryRequestService");
		
		log.info("Juvenile Query Federated Endpoint: " + juvenileCasePlanHistoryRequestFederatedService);
		
		//Subscription Notification
		CxfEndpoint notificationBrokerServiceEndpoint = subscriptionNotificationBundleContext.getBean("notificationBrokerService", CxfEndpoint.class);
		String notificationBrokerEndpointAddress = notificationBrokerServiceEndpoint.getAddress();
		assertEquals(notificationBrokerEndpointAddress,"http://localhost:18040/OJB/SubscribeNotify");
			
		log.info("Notification Broker Endpoint: " + notificationBrokerEndpointAddress);
		
		CxfEndpoint subscriptionManagerServiceEndpoint = subscriptionNotificationBundleContext.getBean("subscriptionManagerService", CxfEndpoint.class);
		String subscriptionManagerEndpointAddress = subscriptionManagerServiceEndpoint.getAddress();	
		assertEquals(subscriptionManagerEndpointAddress,"http://localhost:18041/OJB/SubscriptionManager");
		
		
		log.info("Subscription Management Endpoint: " + subscriptionManagerEndpointAddress);
	}



	private static final class ContextListener implements OsgiBundleApplicationContextListener {

		@Override
		public void onOsgiApplicationEvent(OsgiBundleApplicationContextEvent e) {
			// you can put stuff in here if you want to be notified of osgi-spring context events
		}
	}

}
