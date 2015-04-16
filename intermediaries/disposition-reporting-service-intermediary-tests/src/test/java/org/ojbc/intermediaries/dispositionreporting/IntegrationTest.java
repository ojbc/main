package org.ojbc.intermediaries.dispositionreporting;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.ops4j.pax.exam.CoreOptions.maven;
import static org.ops4j.pax.exam.CoreOptions.mavenBundle;
import static org.ops4j.pax.exam.karaf.options.KarafDistributionOption.karafDistributionConfiguration;
import static org.ops4j.pax.exam.karaf.options.KarafDistributionOption.logLevel;

import java.io.File;
import java.io.IOException;
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
import org.springframework.context.ApplicationContext;
import org.springframework.osgi.context.event.OsgiBundleApplicationContextEvent;
import org.springframework.osgi.context.event.OsgiBundleApplicationContextListener;

/**
 * Integration test for Disposition Reporting bundles using Pax Exam.
 * 
 */
@RunWith(PaxExam.class)
@ExamReactorStrategy(PerClass.class)
public class IntegrationTest extends AbstractPaxExamIntegrationTest {

    private static final Log log = LogFactory.getLog(IntegrationTest.class);

    private static final String KARAF_VERSION = "2.2.11";

    //Connectors
    @Inject
    @Filter(value = "(org.springframework.context.service.name=org.ojbc.bundles.connectors.disposition-connector-jims)", timeout = 40000)
    private ApplicationContext dispositionConnectorJimsBundleContext;

    //Intermediaries
    @Inject
    @Filter(value = "(org.springframework.context.service.name=org.ojbc.bundles.intermediaries.disposition-reporting-service-intermediary)", timeout = 40000)
    private ApplicationContext dispositionReportingBundleContext;
        
    //Adapters
    @Inject
    @Filter(value = "(org.springframework.context.service.name=org.ojbc.bundles.adapters.hawaii-county-prosecutor-case-adapter)", timeout = 40000)
    private ApplicationContext hawaiiCountyProsecutorCaseAdapterBundleContext;

    //Camel Contexts
    private CamelContext dispositionConnectorHajisCamelContext;
    private CamelContext dispositionConnectorJimsCamelContext;
    private CamelContext hawaiiCountyProsecutorCaseServiceCamelContext;

    @Configuration
    public Option[] config() {

        MavenArtifactUrlReference karafUrl = maven().groupId("org.apache.karaf").artifactId("apache-karaf").version(KARAF_VERSION).type("zip");

        MavenUrlReference karafCamelFeature = maven().groupId("org.apache.camel.karaf").artifactId("apache-camel").version("2.10.7").classifier("features").type("xml");

        return new Option[] {

                //Bump up RAM and PermGen to support SSPs
                CoreOptions.vmOptions("-Xmx1G", "-XX:MaxPermSize=128M"),
//              KarafDistributionOption.debugConfiguration("8899", true),

                karafDistributionConfiguration().frameworkUrl(karafUrl).karafVersion(KARAF_VERSION).name("Apache Karaf").unpackDirectory(new File("target/exam")).useDeployFolder(false),

                //keepRuntimeFolder(),

                logLevel(LogLevel.INFO),

                KarafDistributionOption.replaceConfigurationFile("etc/org.ops4j.pax.url.mvn.cfg", new File("src/main/config/org.ops4j.pax.url.mvn.cfg")),
                KarafDistributionOption.replaceConfigurationFile("etc/disposition_reporting_service_intermediary.cfg", new File("src/main/config/disposition_reporting_service_intermediary.cfg")),

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
                mavenBundle().groupId("org.ojbc.bundles.intermediaries").artifactId("disposition-reporting-service-intermediary-hawaii").start(),
                mavenBundle().groupId("org.ojbc.bundles.intermediaries").artifactId("disposition-reporting-service-intermediary").start(),
                
                //Connector
                mavenBundle().groupId("org.ojbc.bundles.connectors").artifactId("disposition-connector-hajis").start(),
                mavenBundle().groupId("org.ojbc.bundles.connectors").artifactId("disposition-connector-jims").start(),
                
                //Adapters
                mavenBundle().groupId("org.ojbc.bundles.adapters").artifactId("hawaii-county-prosecutor-case-adapter").start(),
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

        assertEquals("https://localhost:18200/OJB/DispositionReportingService", dispositionServiceAddress);
        
        //Test startup of Hawaii County Prosecutor Case Adapter Service by getting the web service endpoint
        CxfEndpoint hawaiiCountyProsecutorCaseServiceEndpoint = 
                hawaiiCountyProsecutorCaseAdapterBundleContext.getBean("prosecutionCaseUpdateService", CxfEndpoint.class);
        String hawaiiCountyProsecutorCaseServiceAddress = hawaiiCountyProsecutorCaseServiceEndpoint.getAddress();

        assertNotNull(hawaiiCountyProsecutorCaseServiceAddress);
        
        log.info("Hawaii County Prosecutor Case Adapter Service Service Endpoint: " + hawaiiCountyProsecutorCaseServiceAddress);

        assertEquals("https://0.0.0.0:18400/OJB/ProsecutionCaseUpdateService", hawaiiCountyProsecutorCaseServiceAddress);
        
    }
    
//    @Test
//    public void testIncidentReportFlow() throws Exception {
//
//        //Retrieve camel contexts to get properties from bundle
//        dispositionConnectorHajisCamelContext = getOsgiService(CamelContext.class, "(camel.context.name=disposition-connector-hajis)", 40000);
//        dispositionConnectorJimsCamelContext = getOsgiService(CamelContext.class, "(camel.context.name=disposition-connector-jims)", 40000);
//        hawaiiCountyProsecutorCaseServiceCamelContext = getOsgiService(CamelContext.class, "(camel.context.name=hawaii-county-prosecutor-case-adapter)", 40000);
//        
//        assertNotNull(dispositionConnectorHajisCamelContext);
//        assertNotNull(dispositionConnectorJimsCamelContext);
//        assertNotNull(hawaiiCountyProsecutorCaseServiceCamelContext);
//
//        //Get input directory by resolving property
//        String hajisInputDirectoryString = dispositionConnectorHajisCamelContext.resolvePropertyPlaceholders("{{dispositionReporting.ConnectorFileDirectory}}") + "/input";
//        log.info("Connector input directory:"+ hajisInputDirectoryString);
//        
//        String jimsInputDirectoryString = dispositionConnectorJimsCamelContext.resolvePropertyPlaceholders("{{dispositionReporting.ConnectorFileDirectory}}") + "/input";
//        log.info("Connector input directory:"+ jimsInputDirectoryString);
//
//        //Get adapter output directory, due to Camel bug, it is retrieved by endpoint
//        Endpoint prosecutionCaseUpdateServiceAdapterDir = hawaiiCountyProsecutorCaseServiceCamelContext.getEndpoint("prosecutionCaseUpdateServiceAdapterDir");
//        assertNotNull(prosecutionCaseUpdateServiceAdapterDir);
//        
//        log.info("County Prosecutor Case Service output directory:"+ prosecutionCaseUpdateServiceAdapterDir.getEndpointUri());
//        
//        String caseUpdateEndpointDirectoryString = StringUtils.substringAfter(prosecutionCaseUpdateServiceAdapterDir.getEndpointUri(), "file:///");
//        
//        //Clean the adapter output directory for a clean test
//        initializeDirectory(caseUpdateEndpointDirectoryString);
//        
//        testFlow(caseUpdateEndpointDirectoryString, hajisInputDirectoryString, "HAJIS", "src/test/resources/xmlInstances/HajisProcessedRecord.xml" );
//
//        testFlow(caseUpdateEndpointDirectoryString, jimsInputDirectoryString, "JIMS", "src/test/resources/xmlInstances/JIMS-FTP680_1_1.xml" );
//
//    }

    private void testFlow(String caseUpdateEndpointDirectoryString, String inputDirectoryString, String flowName, String inputFileName)
            throws IOException, InterruptedException {
        
        File inputDiretory = initializeDirectory(inputDirectoryString);
        
        File hajisInputFile = new File(getMavenProjectRootDirectory(), inputFileName);
        assertNotNull(hajisInputFile);
        
        //Kick off the process by copying the process record file to the input directory 
        FileUtils.copyFileToDirectory(hajisInputFile, inputDiretory);
        
        //Sleep while processing happens
        Thread.sleep(15000);
        
        File outputDirectoryForHajis = new File(caseUpdateEndpointDirectoryString + "/" + flowName);
        
        //Assert that we get exactly two file in the adpater's output directory
        String[] extensions = {"xml"};
        Collection<File> files = FileUtils.listFiles(outputDirectoryForHajis, extensions, true);

        assertEquals(1, files.size());
        
        File responseFile = files.iterator().next();
        
        //Confirm file name is correct in output directory
        assertTrue(responseFile.getName().startsWith(flowName + "_InboundMessage"));
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
