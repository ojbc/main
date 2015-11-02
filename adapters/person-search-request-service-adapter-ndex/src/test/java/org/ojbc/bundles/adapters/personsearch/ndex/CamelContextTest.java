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
 * Copyright 2012-2015 Open Justice Broker Consortium
 */
package org.ojbc.bundles.adapters.personsearch.ndex;

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
import org.apache.camel.test.junit4.CamelSpringJUnit4ClassRunner;
import org.apache.camel.test.spring.UseAdviceWith;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.cxf.binding.soap.SoapHeader;
import org.apache.cxf.headers.Header;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
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
    "classpath:META-INF/spring/cxf-endpoints.xml",
    "classpath:META-INF/spring/jetty-server.xml",
    "classpath:META-INF/spring/properties-context.xml"
})
public class CamelContextTest {

    @SuppressWarnings("unused")
    private static final Log log = LogFactory.getLog(CamelContextTest.class);

    @Resource
    private ModelCamelContext context;

    @Produce
    private ProducerTemplate template;

    @EndpointInject(uri = "mock:personSearchResultsHandlerServiceEndpoint")
    private MockEndpoint personSearchResultsMock;

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
    @Ignore("Add unit when ndex mock processor available along with request and response XSLTs")
    public void testNdexPersonSearch() throws Exception {
        RequestParameter p = new RequestParameter();
        p.endpoint = personSearchResultsMock;
        p.routeId = "personSearchRequestService";
        p.resultsHandlerEndpointName = "personSearchResultsHandlerServiceEndpoint";
        p.requestEndpointName = "personSearchRequestServiceEndpoint";
        p.requestMessage = buildPersonSearchMessage();
        p.resultObjectXPath = "/psres-doc:PersonSearchResults";
        p.expectedResultCount = 1;
        submitExchange(p);
    }

    private Document buildPersonSearchMessage() throws Exception {
        Document personSearchRequestMessage = buildPersonSearchRequestMessagePersonNameOnly("System ID");
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
        
        XmlUtils.printNode(personSearchRequestMessage);
        return personSearchRequestMessage;
    }    

    
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

        Thread.sleep(2000);

        p.endpoint.assertIsSatisfied();

        Exchange ex = p.endpoint.getExchanges().get(0);
        Object body = ex.getIn().getBody();
        Document actualResponse = null;
        if (body instanceof String) {
        	log.debug("Reply message: " + body);
            documentBuilder.parse((String) body);
        } else {
            actualResponse = (Document) body;
        }
        
        log.info("Printing response");
        
        XmlUtils.printNode(actualResponse);
        assertEquals(p.expectedResultCount, XmlUtils.xPathNodeListSearch(actualResponse, p.resultObjectXPath).getLength());
        
        context.stop();
        
        return actualResponse;
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
