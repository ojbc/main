package org.ojbc.bundles.intermediaries.incidentreporting;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;
import javax.xml.namespace.QName;
import javax.xml.parsers.DocumentBuilderFactory;

import org.apache.camel.EndpointInject;
import org.apache.camel.Exchange;
import org.apache.camel.Produce;
import org.apache.camel.ProducerTemplate;
import org.apache.camel.builder.AdviceWithRouteBuilder;
import org.apache.camel.component.mock.MockEndpoint;
import org.apache.camel.impl.DefaultExchange;
import org.apache.camel.model.ModelCamelContext;
import org.apache.camel.test.junit4.CamelSpringJUnit4ClassRunner;
import org.apache.commons.io.FileUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.cxf.binding.soap.SoapHeader;
import org.apache.cxf.headers.Header;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.ojbc.intermediaries.incidentreporting.IncidentReportProcessor;
import org.springframework.test.context.ContextConfiguration;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

@RunWith(CamelSpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {
		"classpath:META-INF/spring/camel-context.xml",
		"classpath:META-INF/spring/cxf-endpoints.xml",		
		"classpath:META-INF/spring/properties-context.xml"
		})
public class CamelContextTest {

	private static final Log log = LogFactory.getLog( CamelContextTest.class );
	
	public static final String CXF_OPERATION_NAME = "ReportIncident";
	public static final String CXF_OPERATION_NAMESPACE = "http://ojbc.org/Services/WSDL/IncidentReportingService/1.0";
	
    @Resource
    private ModelCamelContext context;
	
    @Produce
    protected ProducerTemplate template;
    
    @EndpointInject(uri = "mock:cxf:bean:N-DexSubmissionServiceFacade")
    protected MockEndpoint ndexServiceMock;
    
	@Before
	public void setUp() throws Exception {
		
    	//We replace the 'from' web service endpoint with a direct endpoint we call in our test
    	context.getRouteDefinition("IncidentReportingServiceHandlerRoute").adviceWith(context, new AdviceWithRouteBuilder() {
    	    @Override
    	    public void configure() throws Exception {
    	    	// The line below allows us to bypass CXF and send a message directly into the route
    	    	replaceFromWith("direct:IncidentReportingServiceEndpoint");
    	    }              
    	});

    	//We mock the 'N-DEx' web service endpoint and intercept any submissions
    	context.getRouteDefinition("CallNDExSubmissionServiceRoute").adviceWith(context, new AdviceWithRouteBuilder() {
    	    @Override
    	    public void configure() throws Exception {
    	    	// The line below allows us to bypass CXF and send a message directly into the route
    	    	mockEndpointsAndSkip("cxf:bean:N-DexSubmissionServiceFacade*");
    	    }              
    	});
    	
		context.start();		
		
	}

	@After
	public void tearDown() throws Exception {
	}
	
	@Test
	public void contextStartup() {
		assertTrue(true);
	}

	@Test
	public void testContextRoutes() throws Exception
	{
		
    	//We should get one message
		ndexServiceMock.expectedMessageCount(1);
		
    	//Create a new exchange
    	Exchange senderExchange = new DefaultExchange(context);
					
    	//Test Incident Report Processor bean that sets 'call ndex' header
    	IncidentReportProcessor incidentReportProcessor = new IncidentReportProcessor("123", "456,789");
    	
    	incidentReportProcessor.confirmNdexAuthorizedOri(senderExchange, "123");
    	assertEquals(senderExchange.getIn().getHeader("callNDExSubmissionService"), true);

    	senderExchange.getIn().removeHeader("callNDExSubmissionService");
    	
    	incidentReportProcessor.confirmNdexAuthorizedOri(senderExchange, "456");
    	assertEquals(senderExchange.getIn().getHeader("callNDExSubmissionService"), false);
    	
    	senderExchange.getIn().removeHeader("callNDExSubmissionService");
    	
    	//Test the entire web service route by sending through an Incident Report
		Document doc = createDocument();
		List<SoapHeader> soapHeaders = new ArrayList<SoapHeader>();
		soapHeaders.add(makeSoapHeader(doc, "http://www.w3.org/2005/08/addressing", "MessageID", "12345"));
		senderExchange.getIn().setHeader(Header.HEADER_LIST , soapHeaders);

	    //Read the incident report file from the file system, demostate has this submitter as an approved submitter
	    File inputFile = new File("src/test/resources/xmlInstances/incidentReport/IncidentReport.xml");
	    String inputStr = FileUtils.readFileToString(inputFile);
	    
	    assertNotNull(inputStr);
	    
	    //Set it as the message message body
	    senderExchange.getIn().setBody(inputStr);

	    //Send the one-way exchange.  Using template.send will send an one way message
		Exchange returnExchange = template.send("direct:IncidentReportingServiceEndpoint", senderExchange);
		
		//Use getException to see if we received an exception
		if (returnExchange.getException() != null)
		{	
			throw new Exception(returnExchange.getException());
		}	
		
		//Sleep while a response is generated
		Thread.sleep(1000);

		//Assert that the mock endpoint expectations are satisfied
		ndexServiceMock.assertIsSatisfied();
		
    	//We should get one message
		ndexServiceMock.reset();
		ndexServiceMock.expectedMessageCount(0);

    	//Create a new exchange
    	Exchange senderExchangeNotSendToNDEx = new DefaultExchange(context);
					    	
    	//Test the entire web service route by sending through an Incident Report
		doc = createDocument();
		soapHeaders = new ArrayList<SoapHeader>();
		soapHeaders.add(makeSoapHeader(doc, "http://www.w3.org/2005/08/addressing", "MessageID", "12345"));
		senderExchangeNotSendToNDEx.getIn().setHeader(Header.HEADER_LIST , soapHeaders);

	    //Read the incident report file from the file system, this submitter is not an approved submitter
	    inputFile = new File("src/test/resources/xmlInstances/incidentReport/IncidentReportNotSendToNDex.xml");
	    inputStr = FileUtils.readFileToString(inputFile);
	    
	    assertNotNull(inputStr);
	    
	    //Set it as the message message body
	    senderExchangeNotSendToNDEx.getIn().setBody(inputStr);

	    //Send the one-way exchange.  Using template.send will send an one way message
		returnExchange = template.send("direct:IncidentReportingServiceEndpoint", senderExchangeNotSendToNDEx);
		
		//Use getException to see if we received an exception
		if (returnExchange.getException() != null)
		{	
			throw new Exception(returnExchange.getException());
		}	
		
		//Sleep while a response is generated
		Thread.sleep(1000);
		
		ndexServiceMock.assertIsSatisfied();

	}
	
	private SoapHeader makeSoapHeader(Document doc, String namespace, String localName, String value) {
		Element messageId = doc.createElementNS(namespace, localName);
		messageId.setTextContent(value);
		SoapHeader soapHeader = new SoapHeader(new QName(namespace, localName), messageId);
		return soapHeader;
	}		
	
	public static Document createDocument() throws Exception{

		DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
		dbf.setNamespaceAware(true);
		Document doc = dbf.newDocumentBuilder().newDocument();

		return doc;
	}
	
}
