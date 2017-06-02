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
package org.ojbc.intermediaries.dispositionreporting;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.ops4j.pax.exam.CoreOptions.maven;
import static org.ops4j.pax.exam.CoreOptions.mavenBundle;
import static org.ops4j.pax.exam.CoreOptions.provision;
import static org.ops4j.pax.exam.karaf.options.KarafDistributionOption.karafDistributionConfiguration;
import static org.ops4j.pax.exam.karaf.options.KarafDistributionOption.logLevel;
import static org.ops4j.pax.tinybundles.core.TinyBundles.bundle;
import static org.ops4j.pax.tinybundles.core.TinyBundles.withBnd;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Collection;

import javax.inject.Inject;

import org.apache.camel.CamelContext;
import org.apache.camel.component.cxf.CxfEndpoint;
import org.apache.commons.io.FileUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
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
import org.osgi.framework.Constants;
import org.springframework.context.ApplicationContext;
import org.springframework.osgi.context.event.OsgiBundleApplicationContextEvent;
import org.springframework.osgi.context.event.OsgiBundleApplicationContextListener;

/**
 * Integration test for Disposition Reporting bundles using Pax Exam.
 * 
 */
@RunWith(PaxExam.class)
@ExamReactorStrategy(PerClass.class)
public class DispositionReportingServiceTest extends AbstractPaxExamIntegrationTest {

	private static final Log log = LogFactory.getLog(DispositionReportingServiceTest.class);

	private static final String KARAF_VERSION = "2.2.11";

	//Intermediaries
	@Inject
	@Filter(value = "(org.springframework.context.service.name=org.ojbc.bundles.intermediaries.disposition-reporting-service-intermediary)", timeout = 40000)
	private ApplicationContext dispositionReportingBundleContext;

	//Connector
	@Inject
    @Filter(value = "(org.springframework.context.service.name=connector-adapter-context-bundle)", timeout = 20000)
    private ApplicationContext connectorAndAdapterContext;

	@Configuration
	public Option[] config() {

		MavenArtifactUrlReference karafUrl = maven().groupId("org.apache.karaf").artifactId("apache-karaf").version(KARAF_VERSION).type("zip");

		MavenUrlReference karafCamelFeature = maven().groupId("org.apache.camel.karaf").artifactId("apache-camel").version("2.10.7").classifier("features").type("xml");

		return new Option[] {

				//Bump up RAM and PermGen to support SSPs
				CoreOptions.vmOptions("-Xmx1G", "-XX:MaxPermSize=128M"),
				//KarafDistributionOption.debugConfiguration("8899", true),

				karafDistributionConfiguration().frameworkUrl(karafUrl).karafVersion(KARAF_VERSION).name("Apache Karaf").unpackDirectory(new File("target/exam")).useDeployFolder(false),

				//keepRuntimeFolder(),

				logLevel(LogLevel.INFO),

				KarafDistributionOption.replaceConfigurationFile("etc/org.ops4j.pax.url.mvn.cfg", new File("src/main/config/org.ops4j.pax.url.mvn.cfg")),
                KarafDistributionOption.replaceConfigurationFile("etc/org.apache.cxf.osgi.cfg", new File("src/main/config/org.apache.cxf.osgi.cfg")),
                KarafDistributionOption.replaceConfigurationFile("etc/org.ops4j.pax.web.cfg", new File("src/main/config/org.ops4j.pax.web.cfg")),

				// Camel dependencies
				KarafDistributionOption.features(karafCamelFeature, "camel"),
				KarafDistributionOption.features(karafCamelFeature, "camel-cxf"),
				KarafDistributionOption.features(karafCamelFeature, "camel-saxon"),
				KarafDistributionOption.features(karafCamelFeature, "camel-mail"),
				KarafDistributionOption.features(karafCamelFeature, "camel-http"),

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

				
				mavenBundle().groupId("commons-pool").artifactId("commons-pool").version("1.6").start(),
				mavenBundle().groupId("org.springframework").artifactId("spring-jdbc").version("3.0.7.RELEASE").start(),
				
				// OJB shared bundles here
				mavenBundle().groupId("org.ojbc.bundles.shared").artifactId("ojb-osgi-common").start(),
				mavenBundle().groupId("org.ojbc.bundles.shared").artifactId("ojb-osgi-test-common").start(),
				mavenBundle().groupId("org.ojbc.bundles.shared").artifactId("ojb-common").start(),
				mavenBundle().groupId("org.ojbc.bundles.shared").artifactId("ojb-camel-common").start(),
				mavenBundle().groupId("org.ojbc.bundles.shared").artifactId("ojb-resources-common").start(),
				
				// intermediaries
				mavenBundle().groupId("org.ojbc.bundles.intermediaries").artifactId("disposition-reporting-service-intermediary").start(),
				
                provision(bundle().
                        add("META-INF/spring/camel-context.xml", getContextFile("src/main/resources/META-INF/spring/camel-context.xml")).
                        add("META-INF/spring/cxf-endpoints.xml", getContextFile("src/main/resources/META-INF/spring/cxf-endpoints.xml")).
                        add("META-INF/spring/properties-context.xml", getContextFile("src/main/resources/META-INF/spring/properties-context.xml")).
                        set(Constants.DYNAMICIMPORT_PACKAGE, "*").
                        set(Constants.BUNDLE_SYMBOLICNAME, "connector-adapter-context-bundle").
                        build(withBnd()))
		};
	}

	private InputStream getContextFile(String fileName) {
		InputStream in =null;
		try {
			in = new FileInputStream(new File(fileName));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}	
		return in;
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
		assertNotNull(dispositionReportingBundleContext);
		
		System.err.println(executeCommand("osgi:list -t 1", 20000L, false));
	}
	
	@Test
	public void testQueryBundleStartup() throws Exception {

		//Test startup of Disposition Reporting Service by getting the web service endpoint
		CxfEndpoint dispositionReportingServiceEndpoint = 
		        dispositionReportingBundleContext.getBean("dispositionReportingServiceIntermediary", CxfEndpoint.class);
		String dispositionServiceAddress = dispositionReportingServiceEndpoint.getAddress();

		assertNotNull(dispositionReportingServiceEndpoint);
		
		log.info("Disposition Reporting Service Endpoint: " + dispositionServiceAddress);

		assertEquals("/intermediary/DispositionReportingService", dispositionServiceAddress);
		
        //Test startup of Disposition Reporting Adapter Service by getting the web service endpoint
        CxfEndpoint dispositionReportingServiceAdpater = 
                connectorAndAdapterContext.getBean("dispositionReportingServiceAdpater", CxfEndpoint.class);
        String dispositionReportingServiceAdpaterServiceAddress = dispositionReportingServiceAdpater.getAddress();

        assertNotNull(dispositionReportingServiceAdpater);
        
        log.info("Disposition Reporting Service Adpter Endpoint: " + dispositionReportingServiceAdpaterServiceAddress);

        assertEquals("https://localhost:18400/OJB/DispositionReportingServiceAdapter", dispositionReportingServiceAdpaterServiceAddress);
	}
	
	@Test
	public void testDispositionReportingFlow() throws Exception {

		//Retrieve camel contexts to get properties from bundle
	    CamelContext mockConnectorAndAdpapterCamelContext = 
	            getOsgiService(CamelContext.class, "(camel.context.name=mock-disposition-reportin-connector-adapter-context)", 40000);
		
		assertNotNull(mockConnectorAndAdpapterCamelContext);

		//Get input directory by resolving property
		String inputDirectoryString = mockConnectorAndAdpapterCamelContext.resolvePropertyPlaceholders("{{dispositionReporting.ConnectorFileDirectory}}") + "/input";
		log.info("Connector input directory:"+ inputDirectoryString);
		assertEquals("/tmp/ojb/connector/dispositionReporting/input", inputDirectoryString);
		
		//Get adapter output directory, due to Camel bug, it is retrieved by endpoint
		String adapterOutputDirectoryString = mockConnectorAndAdpapterCamelContext.resolvePropertyPlaceholders("{{adapterOutboundFileRoot}}");
		log.info("Adapter output directory:"+ adapterOutputDirectoryString);
		assertEquals("/tmp/ojb/adapter/dispositionReporting/output", adapterOutputDirectoryString);
		
		testFlow(adapterOutputDirectoryString, inputDirectoryString, "src/test/resources/xmlInstances/Disposition_Report.xml" );

	}

    private void testFlow(String adapterOutputDirectoryString, String inputDirectoryString, String inputFileName)
            throws IOException, InterruptedException {
        
        File inputDiretory = initializeDirectory(inputDirectoryString);
        File outputDirectory = initializeDirectory(adapterOutputDirectoryString);
        
        File inputFile = new File(getMavenProjectRootDirectory(), inputFileName);
		assertNotNull(inputFile);
		
		//Kick off the process by copying the process record file to the input directory 
		FileUtils.copyFileToDirectory(inputFile, inputDiretory);
		
		//Sleep while processing happens
		Thread.sleep(15000);
		
		
		//Assert that we get exactly two file in the adpater's output directory
		String[] extensions = {"xml"};
		Collection<File> files = FileUtils.listFiles(outputDirectory, extensions, true);

		assertEquals(1, files.size());
		
		File responseFile = files.iterator().next();
		
		//Confirm file name is correct in output directory
		assertTrue(responseFile.getName().startsWith("InboundMessage"));
		assertTrue(responseFile.getName().endsWith(".xml"));
    }

    private File initializeDirectory(String directoryName) throws IOException {
        File inputDirectory = new File(directoryName);
		
		if (inputDirectory.exists())
		{	
			FileUtils.cleanDirectory(inputDirectory);
		}
		
		return inputDirectory;
    }

	private static final class ContextListener implements OsgiBundleApplicationContextListener {

		@Override
		public void onOsgiApplicationEvent(OsgiBundleApplicationContextEvent e) {
			// you can put stuff in here if you want to be notified of osgi-spring context events
		}
	}

}
