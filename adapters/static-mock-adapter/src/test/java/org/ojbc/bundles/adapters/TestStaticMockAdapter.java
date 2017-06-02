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
package org.ojbc.bundles.adapters;

import static org.junit.Assert.assertEquals;

import java.io.File;
import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;
import javax.xml.namespace.QName;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.apache.camel.EndpointInject;
import org.apache.camel.Exchange;
import org.apache.camel.Produce;
import org.apache.camel.ProducerTemplate;
import org.apache.camel.builder.AdviceWithRouteBuilder;
import org.apache.camel.component.mock.MockEndpoint;
import org.apache.camel.impl.DefaultExchange;
import org.apache.camel.model.ModelCamelContext;
import org.apache.camel.test.spring.CamelSpringJUnit4ClassRunner;
import org.apache.camel.test.spring.UseAdviceWith;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.cxf.binding.soap.SoapHeader;
import org.apache.cxf.headers.Header;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.ojbc.bundles.adapters.staticmock.StaticMockQuery;
import org.ojbc.util.xml.OjbcNamespaceContext;
import org.ojbc.util.xml.XmlUtils;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ContextConfiguration;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

@UseAdviceWith
// NOTE: this causes Camel contexts to not start up automatically
@RunWith(CamelSpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {
    "classpath:META-INF/spring/camel-context.xml",
    "classpath:META-INF/spring/dao.xml",
    "classpath:META-INF/spring/cxf-endpoints.xml",
    "classpath:META-INF/spring/jetty-server.xml",
    "classpath:META-INF/spring/properties-context.xml"
})
public class TestStaticMockAdapter {

    @SuppressWarnings("unused")
    private static final Log log = LogFactory.getLog(TestStaticMockAdapter.class);

    @Resource
    private ModelCamelContext context;

    @Produce
    private ProducerTemplate template;

    @EndpointInject(uri = "mock:personSearchResultsHandlerServiceEndpoint")
    private MockEndpoint personSearchResultsMock;

    @EndpointInject(uri = "mock:firearmSearchResultsServiceEndpoint")
    private MockEndpoint firearmSearchResultsMock;

    @EndpointInject(uri = "mock:vehicleSearchResultsServiceEndpoint")
    private MockEndpoint vehicleSearchResultsMock;

    @EndpointInject(uri = "mock:incidentSearchResultsServiceEndpoint")
    private MockEndpoint incidentSearchResultsMock;

    @EndpointInject(uri = "mock:personQueryResultsHandlerCriminalHistoryServiceEndpoint")
    private MockEndpoint criminalHistoryQueryResultsMock;

    @EndpointInject(uri = "mock:personQueryResultsHandlerWarrantsServiceEndpoint")
    private MockEndpoint warrantQueryResultsMock;
    
    @EndpointInject(uri = "mock:juvenileIntakeHistoryResultsServiceEndpoint")
    private MockEndpoint juvenileIntakeQueryResultsMock;

    @EndpointInject(uri = "mock:personQueryIncidentReportResultsServiceEndpoint")
    private MockEndpoint incidentReportQueryResultsMock;

    @EndpointInject(uri = "mock:personQueryFirearmRegistratonQueryResultsServiceEndpoint")
    private MockEndpoint firearmReportQueryResultsMock;

    private DocumentBuilder documentBuilder;

    @Before
    public void setUp() throws Exception {
        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        dbf.setNamespaceAware(true);
        documentBuilder = dbf.newDocumentBuilder();
    }

    private class RequestParameter {
        MockEndpoint endpoint;
        String routeId;
        String resultsHandlerEndpointName;
        String requestEndpointName;
        String operationNameHeader;
        Document requestMessage;
        String resultObjectXPath;
        int expectedResultCount;
    }

    @DirtiesContext
    @Test
    public void testFirearmReportQuery() throws Exception {
        RequestParameter p = new RequestParameter();
        p.endpoint = firearmReportQueryResultsMock;
        p.routeId = "firearmReportQueryRequestService";
        p.resultsHandlerEndpointName = "personQueryFirearmRegistratonQueryResultsServiceEndpoint";
        p.requestEndpointName = "personQueryFirearmRegistratonQueryRequestServiceEndpoint";
        p.requestMessage = buildFirearmReportQueryRequest();
        p.resultObjectXPath = "/";
        p.expectedResultCount = 1;
        submitExchange(p);
    }

    @DirtiesContext
    @Test
    public void testIncidentReportQuery() throws Exception {
        RequestParameter p = new RequestParameter();
        p.endpoint = incidentReportQueryResultsMock;
        p.routeId = "incidentReportQueryRequestService";
        p.resultsHandlerEndpointName = "personQueryIncidentReportResultsServiceEndpoint";
        p.requestEndpointName = "personQueryIncidentReportRequestServiceEndpoint";
        p.requestMessage = buildIncidentReportQueryRequest();
        p.resultObjectXPath = "/";
        p.expectedResultCount = 1;
        submitExchange(p);
    }

    @DirtiesContext
    @Test
    public void testCriminalHistoryPersonQuery() throws Exception {
        RequestParameter p = new RequestParameter();
        p.endpoint = criminalHistoryQueryResultsMock;
        p.routeId = "criminalHistoryQueryRequestService";
        p.resultsHandlerEndpointName = "personQueryResultsHandlerCriminalHistoryServiceEndpoint";
        p.requestEndpointName = "personQueryRequestCriminalHistoryServiceEndpoint";
        p.requestMessage = buildCriminalHistoryQueryRequest();
        p.resultObjectXPath = "/";
        p.expectedResultCount = 1;
        submitExchange(p);
    }

    @DirtiesContext
    @Test
    public void testWarrantPersonQuery() throws Exception {
        RequestParameter p = new RequestParameter();
        p.endpoint = warrantQueryResultsMock;
        p.routeId = "warrantQueryRequestService";
        p.resultsHandlerEndpointName = "personQueryResultsHandlerWarrantsServiceEndpoint";
        p.requestEndpointName = "personQueryRequestWarrantsServiceEndpoint";
        p.requestMessage = buildWarrantQueryRequest();
        p.resultObjectXPath = "/";
        p.expectedResultCount = 1;
        submitExchange(p);
    }

    @DirtiesContext
    @Test
    public void testJuvenileIntakeQuery() throws Exception {
        RequestParameter p = new RequestParameter();
        p.endpoint = juvenileIntakeQueryResultsMock;
        p.routeId = "juvenileIntakeHistoryRequestServiceEndpointRoute";
        p.resultsHandlerEndpointName = "juvenileIntakeHistoryResultsServiceEndpoint";
        p.requestEndpointName = "juvenileIntakeHistoryRequestServiceEndpoint";
        p.requestMessage = buildJuvenileIntakeQueryRequest();
        p.resultObjectXPath = "/";
        p.expectedResultCount = 1;
        submitExchange(p);
    }

    @DirtiesContext
    @Test
    public void testIncidentSearch() throws Exception {
        RequestParameter p = new RequestParameter();
        p.endpoint = incidentSearchResultsMock;
        p.routeId = "incidentSearchRequestService";
        p.resultsHandlerEndpointName = "incidentSearchResultsServiceEndpoint";
        p.requestEndpointName = "incidentSearchRequestServiceEndpoint";
        p.operationNameHeader = "SubmitIncidentSearchRequest";
        p.requestMessage = buildIncidentSearchMessage();
        p.resultObjectXPath = "//isres:IncidentSearchResult";
        p.expectedResultCount = 1;
        submitExchange(p);
    }

    @DirtiesContext
    @Test
    public void testFirearmsSearch() throws Exception {
        RequestParameter p = new RequestParameter();
        p.endpoint = firearmSearchResultsMock;
        p.routeId = "firearmSearchRequestService";
        p.resultsHandlerEndpointName = "firearmSearchResultsServiceEndpoint";
        p.requestEndpointName = "firearmSearchRequestServiceEndpoint";
        p.requestMessage = buildFirearmSearchMessage();
        p.resultObjectXPath = "//firearm-search-resp-ext:Firearm";
        p.expectedResultCount = 1;
        submitExchange(p);
    }

    @DirtiesContext
    @Test
    public void testVehicleSearch() throws Exception {
    	
        RequestParameter p = new RequestParameter();
        p.endpoint = vehicleSearchResultsMock;
        p.routeId = "vehicleSearchRequestService";
        p.resultsHandlerEndpointName = "vehicleSearchResultsServiceEndpoint";
        p.requestEndpointName = "vehicleSearchRequestServiceEndpoint";
        p.requestMessage = buildVehicleSearchMessage();
        p.resultObjectXPath = "/vsres-exch:VehicleSearchResults/vsres:VehicleSearchResult[vsres:Vehicle/nc:VehicleIdentification/nc:IdentificationID='V643037248486712933']";
        p.expectedResultCount = 1;
        submitExchange(p);
        
    }

    @DirtiesContext
    @Test
    public void testFirearmsPersonSearch() throws Exception {
        RequestParameter p = new RequestParameter();
        p.endpoint = personSearchResultsMock;
        p.routeId = "personSearchRequestService";
        p.resultsHandlerEndpointName = "personSearchResultsHandlerServiceEndpoint";
        p.requestEndpointName = "personSearchRequestServiceEndpoint";
        p.requestMessage = buildFirearmsPersonSearchMessage();
        p.resultObjectXPath = "//psres:Person";
        p.expectedResultCount = 2;
        submitExchange(p);
    }

    @DirtiesContext
    @Test
    public void testCriminalHistoryPersonSearch() throws Exception {
        RequestParameter p = new RequestParameter();
        p.endpoint = personSearchResultsMock;
        p.routeId = "personSearchRequestService";
        p.resultsHandlerEndpointName = "personSearchResultsHandlerServiceEndpoint";
        p.requestEndpointName = "personSearchRequestServiceEndpoint";
        p.requestMessage = buildCriminalHistoryPersonSearchMessage();
        p.resultObjectXPath = "//psres:Person";
        p.expectedResultCount = 1;
        submitExchange(p);
    }

    @DirtiesContext
    @Test
    public void testJuvenileHistoryPersonSearch() throws Exception {
        RequestParameter p = new RequestParameter();
        p.endpoint = personSearchResultsMock;
        p.routeId = "personSearchRequestService";
        p.resultsHandlerEndpointName = "personSearchResultsHandlerServiceEndpoint";
        p.requestEndpointName = "personSearchRequestServiceEndpoint";
        p.requestMessage = buildJuvenileHistoryPersonSearchMessage();
        p.resultObjectXPath = "//psres:Person";
        p.expectedResultCount = 1;
        submitExchange(p);
    }

    @SuppressWarnings("deprecation")
    private Document submitExchange(final RequestParameter p) throws Exception, InterruptedException {

        context.getRouteDefinition(p.routeId).adviceWith(context, new AdviceWithRouteBuilder() {
            @Override
            public void configure() throws Exception {
                weaveByToString("To[" + p.resultsHandlerEndpointName + "]").replace().to("mock:" + p.resultsHandlerEndpointName);
                replaceFromWith("direct:" + p.requestEndpointName);
            }
        });

        context.start();

        p.endpoint.expectedMessageCount(1);

        Exchange senderExchange = new DefaultExchange(context);

        Document doc = createDocument();
        List<SoapHeader> soapHeaders = new ArrayList<SoapHeader>();
        soapHeaders.add(makeSoapHeader(doc, "http://www.w3.org/2005/08/addressing", "MessageID", "12345"));
        soapHeaders.add(makeSoapHeader(doc, "http://www.w3.org/2005/08/addressing", "ReplyTo", "https://reply.to"));
        senderExchange.getIn().setHeader(Header.HEADER_LIST, soapHeaders);
        
        if (p.operationNameHeader != null) {
            senderExchange.getIn().setHeader("operationName", p.operationNameHeader);
        }

        //XmlUtils.printNode(p.requestMessage);
        senderExchange.getIn().setBody(p.requestMessage);
        Exchange returnExchange = template.send("direct:" + p.requestEndpointName, senderExchange);

        if (returnExchange.getException() != null) {
            throw new Exception(returnExchange.getException());
        }

        Thread.sleep(1000);

        p.endpoint.assertIsSatisfied();

        Exchange ex = p.endpoint.getExchanges().get(0);
        Object body = ex.getIn().getBody();
        Document actualResponse = null;
        if (body instanceof String) {
            documentBuilder.parse((String) body);
        } else {
            actualResponse = (Document) body;
        }
        XmlUtils.printNode(actualResponse);
        assertEquals(p.expectedResultCount, XmlUtils.xPathNodeListSearch(actualResponse, p.resultObjectXPath).getLength());
        
        context.stop();
        
        return actualResponse;
    }

    private Document buildCriminalHistoryPersonSearchMessage() throws Exception {
        Document personSearchRequestMessage = buildPersonSearchRequestMessagePersonNameOnly(StaticMockQuery.CRIMINAL_HISTORY_MOCK_ADAPTER_SEARCH_SYSTEM_ID);
        Element personElement = (Element) XmlUtils.xPathNodeSearch(personSearchRequestMessage, "psr-doc:PersonSearchRequest/psr:Person");
        Element personNameElement = (Element) XmlUtils.xPathNodeSearch(personElement, "nc:PersonName");
        Element lastNameElement = (Element) XmlUtils.xPathNodeSearch(personNameElement, "nc:PersonSurName");
        lastNameElement.setTextContent("Ivey");
        Element firstNameElement = (Element) XmlUtils.xPathNodeSearch(personNameElement, "nc:PersonGivenName");
        personNameElement.removeChild(firstNameElement);
        Element middleNameElement = (Element) XmlUtils.xPathNodeSearch(personNameElement, "nc:PersonMiddleName");
        personNameElement.removeChild(middleNameElement);
        return personSearchRequestMessage;
    }

    private Document buildJuvenileHistoryPersonSearchMessage() throws Exception {
        Document personSearchRequestMessage = buildPersonSearchRequestMessagePersonNameOnly(StaticMockQuery.JUVENILE_HISTORY_MOCK_ADAPTER_SEARCH_SYSTEM_ID);
        Element personElement = (Element) XmlUtils.xPathNodeSearch(personSearchRequestMessage, "psr-doc:PersonSearchRequest/psr:Person");
        Element personNameElement = (Element) XmlUtils.xPathNodeSearch(personElement, "nc:PersonName");
        Element lastNameElement = (Element) XmlUtils.xPathNodeSearch(personNameElement, "nc:PersonSurName");
        lastNameElement.setTextContent("Mosley");
        Element firstNameElement = (Element) XmlUtils.xPathNodeSearch(personNameElement, "nc:PersonGivenName");
        personNameElement.removeChild(firstNameElement);
        Element middleNameElement = (Element) XmlUtils.xPathNodeSearch(personNameElement, "nc:PersonMiddleName");
        personNameElement.removeChild(middleNameElement);
        return personSearchRequestMessage;
    }

    private Document buildFirearmsPersonSearchMessage() throws Exception {
        Document personSearchRequestMessage = buildPersonSearchRequestMessagePersonNameOnly(StaticMockQuery.FIREARM_MOCK_ADAPTER_SEARCH_SYSTEM_ID);
        Element personElement = (Element) XmlUtils.xPathNodeSearch(personSearchRequestMessage, "psr-doc:PersonSearchRequest/psr:Person");
        Element personNameElement = (Element) XmlUtils.xPathNodeSearch(personElement, "nc:PersonName");
        Element lastNameElement = (Element) XmlUtils.xPathNodeSearch(personNameElement, "nc:PersonSurName");
        lastNameElement.setTextContent("McElroy");
        Element firstNameElement = (Element) XmlUtils.xPathNodeSearch(personNameElement, "nc:PersonGivenName");
        personNameElement.removeChild(firstNameElement);
        return personSearchRequestMessage;
    }

    private Document buildPersonSearchRequestMessagePersonNameOnly(String systemId) throws Exception {
        File inputFile = new File("src/test/resources/xml/BasePersonSearchRequest.xml");
        Document ret = documentBuilder.parse(new FileInputStream(inputFile));
        Element systemElement = (Element) XmlUtils.xPathNodeSearch(ret.getDocumentElement(), "psr:SourceSystemNameText");
        systemElement.setTextContent(systemId);
        Document personSearchRequestMessage = ret;
        Element personElement = (Element) XmlUtils.xPathNodeSearch(personSearchRequestMessage, "psr-doc:PersonSearchRequest/psr:Person");
        NodeList children = personElement.getChildNodes();
        int childCount = children.getLength();
        for (int i = childCount - 1; i >= 0; i--) {
            Node child = children.item(i);
            if (!("PersonName".equals(child.getLocalName()))) {
                personElement.removeChild(child);
            }
        }
        // XmlUtils.printNode(personSearchRequestMessage);
        return personSearchRequestMessage;
    }

    private Document buildFirearmSearchMessage() throws Exception {
        File inputFile = new File("src/test/resources/xml/BaseFirearmSearchRequest.xml");
        Document firearmSearchRequestMessage = documentBuilder.parse(new FileInputStream(inputFile));
        removeElement(firearmSearchRequestMessage, "/firearm-search-req-doc:FirearmSearchRequest/firearm-search-req-ext:Firearm/firearms-codes-demostate:FirearmMakeCode");
        removeElement(firearmSearchRequestMessage, "/firearm-search-req-doc:FirearmSearchRequest/firearm-search-req-ext:Firearm/firearm-search-req-ext:FirearmMakeText");
        removeElement(firearmSearchRequestMessage, "/firearm-search-req-doc:FirearmSearchRequest/firearm-search-req-ext:Firearm/nc:FirearmCategoryCode");
        removeElement(firearmSearchRequestMessage, "/firearm-search-req-doc:FirearmSearchRequest/firearm-search-req-ext:Firearm/nc:ItemModelName");
        removeElement(firearmSearchRequestMessage, "/firearm-search-req-doc:FirearmSearchRequest/firearm-search-req-ext:ItemRegistration");
        removeElement(firearmSearchRequestMessage, "/firearm-search-req-doc:FirearmSearchRequest/nc:PropertyRegistrationAssociation");
        Element identificationNode = (Element) XmlUtils.xPathNodeSearch(firearmSearchRequestMessage,
                "/firearm-search-req-doc:FirearmSearchRequest/firearm-search-req-ext:Firearm/nc:ItemSerialIdentification/nc:IdentificationID");
        identificationNode.setTextContent("7604980775");
        return firearmSearchRequestMessage;
    }

    private Document buildVehicleSearchMessage() throws Exception {
        File inputFile = new File("src/test/resources/xml/BaseVehicleSearchRequest.xml");
        Document srm = documentBuilder.parse(new FileInputStream(inputFile));
        return srm;
    }

    private Document buildIncidentSearchMessage() throws Exception {
        File inputFile = new File("src/test/resources/xml/BaseIncidentSearchRequest.xml");
        Document incidentSearchRequestMessage = documentBuilder.parse(new FileInputStream(inputFile));
        removeElement(incidentSearchRequestMessage, "/isr-doc:IncidentSearchRequest/isr:Incident/nc:ActivityDateRange");
        removeElement(incidentSearchRequestMessage, "/isr-doc:IncidentSearchRequest/isr:Incident/isr:IncidentCategoryCode");
        removeElement(incidentSearchRequestMessage, "/isr-doc:IncidentSearchRequest/nc:Location");
        removeElement(incidentSearchRequestMessage, "/isr-doc:IncidentSearchRequest/jxdm41:ActivityLocationAssociation");
        Element incidentNumberElement = (Element) XmlUtils.xPathNodeSearch(incidentSearchRequestMessage, "/isr-doc:IncidentSearchRequest/isr:Incident/nc:ActivityIdentification/nc:IdentificationID");
        incidentNumberElement.setTextContent("I8796338296");
        return incidentSearchRequestMessage;
    }
    
    private Document buildCriminalHistoryQueryRequest() throws Exception {
        Document ret = buildPersonQueryRequest(StaticMockQuery.CRIMINAL_HISTORY_MOCK_ADAPTER_QUERY_SYSTEM_ID, "sample-990144318698148225.xml");
        return ret;
    }

    private Document buildWarrantQueryRequest() throws Exception {
        Document ret = buildPersonQueryRequest(StaticMockQuery.WARRANT_MOCK_ADAPTER_QUERY_SYSTEM_ID, "sample-951708553359338252.xml");
        return ret;
    }
    
    private Document buildJuvenileIntakeQueryRequest() throws Exception {
        Document ret = createDocument();
        Element root = ret.createElementNS(OjbcNamespaceContext.NS_JUVENILE_HISTORY_QUERY_REQUEST_DOC, "JuvenileHistoryQuery");
		ret.appendChild(root);
		root.setPrefix(OjbcNamespaceContext.NS_PREFIX_JUVENILE_HISTORY_QUERY_REQUEST_DOC);
		Element e = XmlUtils.appendElement(root, OjbcNamespaceContext.NS_JUVENILE_HISTORY_EXT, "JuvenileHistoryQueryCriteria");
		Element id = XmlUtils.appendElement(e, OjbcNamespaceContext.NS_JUVENILE_HISTORY_EXT, "JuvenileInformationRecordID");
		e = XmlUtils.appendElement(id, OjbcNamespaceContext.NS_NC_30, "IdentificationID");
		e.setTextContent("sample-1147519288796085535.xml");
		e = XmlUtils.appendElement(id, OjbcNamespaceContext.NS_NC_30, "IdentificationSourceText");
		e.setTextContent(StaticMockQuery.JUVENILE_HISTORY_MOCK_ADAPTER_QUERY_SYSTEM_ID);
		XmlUtils.OJBC_NAMESPACE_CONTEXT.populateRootNamespaceDeclarations(root);
        return ret;
    }
    
    private Document buildPersonQueryRequest(final String system, final String identifier) throws Exception {
        Document ret = createDocument();
        Element root = ret.createElementNS(OjbcNamespaceContext.NS_PERSON_QUERY_REQUEST, "PersonRecordRequest");
        ret.appendChild(root);
        String prefix = XmlUtils.OJBC_NAMESPACE_CONTEXT.getPrefix(OjbcNamespaceContext.NS_PERSON_QUERY_REQUEST);
        root.setPrefix(prefix);
        Element id = XmlUtils.appendElement(root, OjbcNamespaceContext.NS_PERSON_QUERY_REQUEST, "PersonRecordRequestIdentification");
        Element e = XmlUtils.appendElement(id, OjbcNamespaceContext.NS_NC, "IdentificationID");
        e.setTextContent(identifier);
        e = XmlUtils.appendElement(id, OjbcNamespaceContext.NS_NC, "IdentificationSourceText");
        e.setTextContent(system);
        XmlUtils.OJBC_NAMESPACE_CONTEXT.populateRootNamespaceDeclarations(root);
        return ret;
    }
    
    private Document buildIncidentReportQueryRequest() throws Exception {
        return buildPersonQueryRequest(StaticMockQuery.INCIDENT_MOCK_ADAPTER_QUERY_SYSTEM_ID, "sample-4510266831318767293-matthews.xml");
    }
    
    private Document buildFirearmReportQueryRequest() throws Exception {
        Document ret = createDocument();
        Element root = ret.createElementNS(OjbcNamespaceContext.NS_FIREARM_REGISTRATION_QUERY_REQUEST_DOC, "FirearmRegistrationRequest");
        ret.appendChild(root);
        root.setPrefix(OjbcNamespaceContext.NS_PREFIX_FIREARM_REGISTRATION_QUERY_REQUEST_DOC);
        Element id = XmlUtils.appendElement(root, OjbcNamespaceContext.NS_FIREARM_REGISTRATION_QUERY_REQUEST_EXT, "FirearmRegistrationIdentification");
        Element e = XmlUtils.appendElement(id, OjbcNamespaceContext.NS_NC, "IdentificationID");
        e.setTextContent("sample-1009530696428120424.xml:F1");
        e = XmlUtils.appendElement(id, OjbcNamespaceContext.NS_NC, "IdentificationSourceText");
        e.setTextContent(StaticMockQuery.FIREARM_MOCK_ADAPTER_QUERY_BY_FIREARM_SYSTEM_ID);
        XmlUtils.OJBC_NAMESPACE_CONTEXT.populateRootNamespaceDeclarations(root);
        return ret;
    }

    protected Element removeElement(Document d, String xPath) throws Exception {
        Node n = XmlUtils.xPathNodeSearch(d, xPath);
        Node parent = n.getParentNode();
        parent.removeChild(n);
        return (Element) parent;
    }

    private SoapHeader makeSoapHeader(Document doc, String namespace, String localName, String value) {
        Element messageId = doc.createElementNS(namespace, localName);
        messageId.setTextContent(value);
        SoapHeader soapHeader = new SoapHeader(new QName(namespace, localName), messageId);
        return soapHeader;
    }

    private static Document createDocument() throws Exception {

        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        dbf.setNamespaceAware(true);
        Document doc = dbf.newDocumentBuilder().newDocument();

        return doc;
    }

}
