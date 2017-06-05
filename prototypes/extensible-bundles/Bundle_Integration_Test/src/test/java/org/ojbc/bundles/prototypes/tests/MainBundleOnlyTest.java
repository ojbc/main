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
package org.ojbc.bundles.prototypes.tests;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.ops4j.pax.exam.CoreOptions.maven;
import static org.ops4j.pax.exam.CoreOptions.mavenBundle;
import static org.ops4j.pax.exam.karaf.options.KarafDistributionOption.karafDistributionConfiguration;
import static org.ops4j.pax.exam.karaf.options.KarafDistributionOption.logLevel;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.URL;

import javax.inject.Inject;

import org.apache.camel.CamelContext;
import org.apache.camel.Endpoint;
import org.apache.camel.ServiceStatus;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
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
import org.ops4j.pax.exam.spi.reactors.ExamReactorStrategy;
import org.ops4j.pax.exam.spi.reactors.PerMethod;
import org.ops4j.pax.exam.util.Filter;
import org.springframework.context.ApplicationContext;

/**
 * Pax Exam integration test that demonstrates how to test our prototype extensibility bundles.
 * 
 * For more on Pax Exam testing, see:
 * 
 * https://ops4j1.jira.com/wiki/display/PAXEXAM3/Documentation
 * https://ops4j1.jira.com/wiki/display/PAXEXAM3/Karaf+Container
 * https://github.com/ops4j/org.ops4j.pax.exam2
 * https://github.com/ANierbeck/Camel-Pax-Exam-Demo
 *
 */
@RunWith(PaxExam.class)
@ExamReactorStrategy(PerMethod.class)
public class MainBundleOnlyTest extends AbstractPaxExamIntegrationTest {
	
	private static final Log log = LogFactory.getLog(MainBundleOnlyTest.class);

	private static final String KARAF_VERSION = "2.2.11";

	@Inject
	protected OjbcContext ojbcContext;

	@Inject
	@Filter(value = "(org.springframework.context.service.name=Bundle_A_Test)", timeout = 20000)
	private ApplicationContext mainBundleContext;

	@Configuration
	public Option[] config() {

		// Maven reference to Karaf install image
		MavenArtifactUrlReference karafUrl = maven().groupId("org.apache.karaf").artifactId("apache-karaf").version(KARAF_VERSION).type("zip");

		// Maven reference to Karaf standard features (note this will vary if you change Karaf versions)
		MavenUrlReference karafSpringFeature = maven().groupId("org.apache.karaf.assemblies.features").artifactId("standard").version(KARAF_VERSION)
				.classifier("features").type("xml");
		
		// Maven reference to Camel features
		MavenUrlReference karafCamelFeature = maven().groupId("org.apache.camel.karaf").artifactId("apache-camel").versionAsInProject()
				.classifier("features").type("xml");

		// Set up Karaf options

		return new Option[] {

				// configure Pax Exam Karaf container
				karafDistributionConfiguration().frameworkUrl(karafUrl).karafVersion(KARAF_VERSION).name("Apache Karaf")
						.unpackDirectory(new File("target/exam")).useDeployFolder(false),

				// Keep the runtime folder (can be useful for debugging)
				// keepRuntimeFolder(),

				// Set the logging level for Karaf
				logLevel(LogLevel.INFO),

				// Install Karaf Spring and Camel features (this is the equivalent of Karaf shell command "features:install")
				KarafDistributionOption.features(karafSpringFeature, "spring", "spring-tx", "spring-test"),
				KarafDistributionOption.features(karafCamelFeature, "camel", "camel-core", "camel-spring"),

				// Install bundles under test (this is the equivalent of the Karaf shell command "install -s mvn:...")
				// Note that "versionAsInProject" installs the version of the bundle based on the maven dependency in the pom
				// Note that you want to start Bundle B first, since Bundle A references its service
				KarafDistributionOption.replaceConfigurationFile("etc/ojbc.context.services.cfg", new File("src/main/config/ojbc.context.services.cfg")),
				mavenBundle().groupId("org.ojbc.bundles.shared").artifactId("ojb-osgi-common").versionAsInProject().start(),
				mavenBundle().groupId("org.ojbc.bundles.shared").artifactId("ojb-osgi-test-common").versionAsInProject().start(),
				mavenBundle().groupId("org.ojbc.bundles.prototypes.shared").artifactId("Bundle_Integration_Test_Common").versionAsInProject().start(),
				mavenBundle().groupId("org.ojbc.bundles.prototypes").artifactId("Bundle_A_Test").versionAsInProject().start(),

				// Use custom pax url config in Karaf, by installing it into the etc directory.  This allows you to point pax url at local maven repo (useful for offline dev)
				// KarafDistributionOption.replaceConfigurationFile("etc/org.ops4j.pax.url.mvn.cfg", new File("src/main/config/org.ops4j.pax.url.mvn.cfg")),

				// Install compendium config files for bundles under test
				KarafDistributionOption.replaceConfigurationFile("etc/Bundle_A_Test.cfg", new File("src/main/config/Bundle_A_Test.cfg")),

		};
	}

	/*
	 * This test demonstrates the functionality of the prototypical bundles.
	 * 
	 * 1. Bundle A is the "main" bundle, and Bundle B is the "derived" bundle
	 * 2. Bundle A defines a seven-step process:
	 * 
	 * 		i.   A direct call to a route in Bundle A's context
	 * 		ii.  A direct call to a route in Bundle A's context
	 * 		iii. Bean processor invocation, where a bean property is defined by Bundle A
	 * 		iv.  Bean processor invocation, where a bean property is defined by Bundle A; bean name is defined in the classpath config file, but overridden by property in the context.
	 * 			   Step 4 also reads properties out of the properties files, and demonstrates how Bundle B can override properties (by name) from Bundle A.  Also regarding properties,
	 * 			   it demonstrates the order of precedence, which is: compendium (Karaf/etc) overrides props defined in context, which overrides props defined in properties file on
	 * 			   classpath.
	 * 		v.   Bean processor invocation, demonstrating use of SubstitutableObjectFactoryBean, but where Bundle B does not provide a substitute bean
	 * 		vi.  Bean processor invocation, demonstrating use of SubstitutableObjectFactoryBean, where Bundle B provides a substitute
	 * 		vii. direct-vm call from Bundle A to Bundle B, where the direct-vm call is specified in a property setting in Bundle B.  This demonstrates how a dervied bundle
	 * 			   substitutes a component in a main bundle's route by name.
	 * 
	 */
	@Test
	public void test() throws Exception {

		// demonstrate how to test if a feature is loaded in Karaf
		assertTrue(featuresService.isInstalled(featuresService.getFeature("camel-core")));
		
		// demonstrate how to get a reference to an OSGi service defined in our code.  the ojbcContext is the service started up by Bundle B and used by Bundle A
		// to get substitute beans, routes, and properties
		assertNotNull(ojbcContext);
		assertNotNull(mainBundleContext);

		// This is an example of issuing a command to the Karaf shell
//		String command = executeCommand("osgi:list -t 1", 20000L, false);
//		System.err.println(command);

		// grab our Camel Contexts.  This demonstrates that Karaf makes Camel Contexts available as OSGi services.
		
		CamelContext bundleAContext = getOsgiService(CamelContext.class, "(camel.context.name=bundleAcontext)", 10000);
		assertNotNull(bundleAContext);

		// demo of how to verify that a Camel route has started up
		
		ServiceStatus ss = bundleAContext.getRouteStatus("testRoute2");
		assertTrue(ss.isStarted());

		File inputFile = getEndpointMessageFile(bundleAContext, "inputDir2");
		BufferedWriter bw = new BufferedWriter(new FileWriter(inputFile));
		bw.write("message\n");
		bw.close();

		File outputFile = getEndpointMessageFile(bundleAContext, "outputDir");

		// have to do this to avoid Karaf shutting down the camel contexts before they have a chance to notice the file
		// for other tests, you'll need to wait on other things...basically, you want to wait until whatever you're going to assert against is there and finished "cooking"
		while (!outputFile.exists()) {
			Thread.sleep(100);
		}

		assertTrue(outputFile.exists());

		BufferedReader br = new BufferedReader(new FileReader(outputFile));
		assertEquals("message", br.readLine());
		br.readLine();
		assertEquals("Bundle A step 1", br.readLine());
		assertEquals("Bundle A step 2", br.readLine());
		assertEquals(
				"Bundle A Bean 1:1=A.properties.message1|2=A.context.message2|3=A.compendium.message3|4=A.context.message4|5=A.properties.message5|6=A.compendium.message6|",
				br.readLine());
		assertEquals("Bundle A step 4", br.readLine());
		assertEquals("Bundle A Bean 3:m=Message via helper", br.readLine());
		assertEquals("Bundle A step 7", br.readLine());

		br.close();
		outputFile.delete();

	}

	private File getEndpointMessageFile(CamelContext c, String endpointId) throws URISyntaxException, MalformedURLException {
		Endpoint e = c.getEndpoint(endpointId);
		String fileLocation = e.getEndpointUri();
		assertTrue(fileLocation.contains("?"));
		File d = new File(new URL(fileLocation.split("\\?")[0]).toURI());
		File f = new File(d, "message.txt");
		return f;
	}

}
