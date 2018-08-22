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
package org.ojbc.util.camel.processor.audit;

import java.io.IOException;
import java.io.StringReader;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.xml.namespace.QName;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.apache.camel.Exchange;
import org.apache.camel.component.cxf.common.message.CxfConstants;
import org.apache.camel.impl.DefaultCamelContext;
import org.apache.camel.impl.DefaultExchange;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.cxf.binding.soap.SoapHeader;
import org.apache.cxf.headers.Header;
import org.apache.cxf.message.MessageImpl;
import org.apache.wss4j.common.principal.SAMLTokenPrincipal;
import org.apache.wss4j.common.principal.SAMLTokenPrincipalImpl;
import org.apache.wss4j.common.saml.SamlAssertionWrapper;
import org.joda.time.DateTime;
import org.joda.time.Minutes;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.ojbc.util.camel.security.saml.SAMLTokenUtils;
import org.opensaml.saml.saml2.core.Assertion;
import org.opensaml.xmlsec.signature.support.SignatureConstants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(locations = {
		"classpath:META-INF/spring/SQLLoggingProcessorTest-spring-context.xml"
		})
@DirtiesContext
public class SQLLoggingProcessorTest {
    
    @SuppressWarnings("unused")
	private static final Log log = LogFactory.getLog(SQLLoggingProcessorTest.class);
    
    @Autowired
    private SQLLoggingProcessor sqlLoggingProcessor;
    
    @Autowired
    private SQLLoggingProcessor sqlLoggingProcessorWithRedaction;
    
    @Autowired
    private SQLLoggingProcessor sqlLoggingProcessorWithRedactionAndNullValue;
    
    private DefaultCamelContext camelContext;
    
    @BeforeEach
    public void setup() throws Exception {
        Assertions.assertNotNull(sqlLoggingProcessor);
        JdbcTemplate t = sqlLoggingProcessor.getJdbcTemplate();
        Assertions.assertNotNull(t);
        t.execute("delete from AuditLog");
        camelContext = new DefaultCamelContext();
        camelContext.setName("SQLLoggingProcessorTest" + " CamelContext");
    }
    
    @Test
    public void testDocumentObject() throws Exception {
        Document doc = buildDocumentObject();
        Exchange e = setupExchange(doc);
        sqlLoggingProcessor.logExchange(e);
        assertDbContents();
    }
    
    @Test
    public void testStringObject() throws Exception {
        String doc = getXmlString();
        Exchange e = setupExchange(doc);
        sqlLoggingProcessor.logExchange(e);
        assertDbContents();
    }
    
    @Test
    public void testRedactionWithNullValue() throws Exception {
        Document doc = buildDocumentObject();
        Exchange e = setupExchange(doc);
        sqlLoggingProcessorWithRedactionAndNullValue.logExchange(e);
        List<Map<String, Object>> rows = sqlLoggingProcessor.getJdbcTemplate().queryForList("select * from AuditLog");
        Assertions.assertEquals(1, rows.size());
        Map<String, Object> row = rows.get(0);
        Assertions.assertNull(row.get("userLastName"));
        Assertions.assertEquals(row.get("userFirstName"), "andrew");
    }
    
    @Test
    public void testRedaction() throws Exception {
        Document doc = buildDocumentObject();
        Exchange e = setupExchange(doc);
        sqlLoggingProcessorWithRedaction.logExchange(e);
        List<Map<String, Object>> rows = sqlLoggingProcessor.getJdbcTemplate().queryForList("select * from AuditLog");
        Assertions.assertEquals(1, rows.size());
        Map<String, Object> row = rows.get(0);
        Assertions.assertEquals(row.get("userLastName"), "redacted");
        Assertions.assertEquals(row.get("userFirstName"), "andrew");
    }
    
    private String getXmlString() {
        StringBuffer sb = new StringBuffer();
        sb.append("<root xmlns=\"http://ojbc.org\">\n");
        sb.append("<child xmlns=\"http://ojbc.org\">Child contents</child>\n");
        sb.append("</root>\n");
        return sb.toString();
    }

    private Document buildDocumentObject() throws Exception {
        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        dbf.setNamespaceAware(true);
        Document doc = dbf.newDocumentBuilder().parse(new InputSource(new StringReader(getXmlString())));
        return doc;
    }

    private void assertDbContents() throws UnknownHostException, IOException, SAXException, ParserConfigurationException {
        
        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        dbf.setNamespaceAware(true);
        
        List<Map<String, Object>> rows = sqlLoggingProcessor.getJdbcTemplate().queryForList("select * from AuditLog");
        
        Assertions.assertEquals(1, rows.size());
        
        Map<String, Object> row = rows.get(0);
        
        Assertions.assertEquals(row.get("origin"), "http://www.ojbc.org/from");
        Assertions.assertEquals(row.get("destination"), "http://www.ojbc.org/to");
        Assertions.assertEquals(row.get("messageID"), "12345");
        Assertions.assertEquals(row.get("federationID"), "HIJIS:IDP:HCJDC:USER:admin");
        Assertions.assertEquals(row.get("employerName"), "Department of Attorney General");
        Assertions.assertEquals(row.get("employerSubUnitName"), "HCJDC ISDI");
        Assertions.assertEquals(row.get("userLastName"), "owen");
        Assertions.assertEquals(row.get("userFirstName"), "andrew");
        Assertions.assertEquals(row.get("identityProviderID"), "https://idp.ojbc-local.org:9443/idp/shibboleth");
        Assertions.assertEquals(row.get("camelContextID"), "SQLLoggingProcessorTest CamelContext");

        String hostAddress = (String) row.get("hostAddress");
        InetAddress ia = InetAddress.getByName(hostAddress);
        Assertions.assertTrue(ia.isReachable(1000));
        
        String messageDocS = (String) row.get("soapMessage");
        
        Document messageDoc = dbf.newDocumentBuilder().parse(new InputSource(new StringReader(messageDocS)));
        
        Element ee = messageDoc.getDocumentElement();
        Assertions.assertEquals("root", ee.getLocalName());
        Assertions.assertEquals("http://ojbc.org", ee.getNamespaceURI());
        NodeList nl = ee.getElementsByTagNameNS("http://ojbc.org", "child");
        ee = (Element) nl.item(0);
        Assertions.assertEquals("Child contents", ee.getTextContent());
        
        Date d = (Date) row.get("timestamp");
        DateTime dt = new DateTime(d);
        Assertions.assertEquals(0, Minutes.minutesBetween(new DateTime(), dt).getMinutes());
    }

    private Exchange setupExchange(Object messageDocument) throws ParserConfigurationException, Exception {

        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        dbf.setNamespaceAware(true);

        Exchange e = new DefaultExchange(camelContext);
        
        Document doc = dbf.newDocumentBuilder().newDocument();
        
        List<SoapHeader> soapHeaders = new ArrayList<SoapHeader>();
        soapHeaders.add(makeSoapHeader(doc, "http://www.w3.org/2005/08/addressing", "MessageID", "12345"));
        soapHeaders.add(makeSoapHeader(doc, "http://www.w3.org/2005/08/addressing", "From", "http://www.ojbc.org/from"));
        soapHeaders.add(makeSoapHeader(doc, "http://www.w3.org/2005/08/addressing", "To", "http://www.ojbc.org/to"));
        e.getIn().setHeader(Header.HEADER_LIST , soapHeaders);
        
        org.apache.cxf.message.Message message = new MessageImpl();
        
        Assertion samlToken = SAMLTokenUtils.createStaticAssertionWithCustomAttributes("https://idp.ojbc-local.org:9443/idp/shibboleth", SignatureConstants.ALGO_ID_C14N_EXCL_OMIT_COMMENTS, SignatureConstants.ALGO_ID_SIGNATURE_RSA_SHA1, true, true, null);
        SAMLTokenPrincipal principal = new SAMLTokenPrincipalImpl(new SamlAssertionWrapper(samlToken));
        message.put("wss4j.principal.result", principal);
        
        e.getIn().setHeader(CxfConstants.CAMEL_CXF_MESSAGE, message);
        e.getIn().setHeader(CxfConstants.OPERATION_NAME, "doit");
        e.getIn().setHeader(CxfConstants.OPERATION_NAMESPACE, "http://ojbc.org");

        e.getIn().setBody(messageDocument);
        return e;
    }
    
    private SoapHeader makeSoapHeader(Document doc, String namespace, String localName, String value) {
        Element messageId = doc.createElementNS(namespace, localName);
        messageId.setTextContent(value);
        SoapHeader soapHeader = new SoapHeader(new QName(namespace, localName), messageId);
        return soapHeader;
    }  
    
    
}
