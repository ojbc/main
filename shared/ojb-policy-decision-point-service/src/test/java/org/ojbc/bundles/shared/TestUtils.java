package org.ojbc.bundles.shared;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.xml.namespace.QName;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.apache.cxf.binding.soap.SoapHeader;
import org.apache.cxf.endpoint.Client;
import org.apache.cxf.headers.Header;
import org.junit.Ignore;
import org.ojbc.util.camel.helper.OJBUtils;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

@Ignore
public class TestUtils {

    private static DocumentBuilder documentBuilder;


    public static Map<String, Object> createHeaders() throws Exception {
        
        if (documentBuilder == null) {
            DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory
                    .newInstance();
            documentBuilderFactory.setNamespaceAware(true);
            documentBuilder = documentBuilderFactory.newDocumentBuilder();
        }
        
        Map<String, Object> headers = new HashMap<String, Object>(); 
        //Set the WS-Address Message ID
        Map<String, Object> requestContext = OJBUtils.setWSAddressingMessageID("123456789");
        
        //Set the operation name and operation namespace for the CXF exchange
        headers.put(Client.REQUEST_CONTEXT , requestContext);
        
        Document doc = documentBuilder.newDocument();
        List<SoapHeader> soapHeaders = new ArrayList<SoapHeader>();
        soapHeaders.add(makeSoapHeader(doc, "http://www.w3.org/2005/08/addressing", "MessageID", "123456789"));
        soapHeaders.add(makeSoapHeader(doc, "http://www.w3.org/2005/08/addressing", "ReplyTo", "https://localhost:18311/OJB/WebApp/AccessControlResponseService"));
        headers.put(Header.HEADER_LIST , soapHeaders);
        return headers;
    }

    public static SoapHeader makeSoapHeader(Document doc, String namespace, String localName, String value) {
        Element messageId = doc.createElementNS(namespace, localName);
        messageId.setTextContent(value);
        SoapHeader soapHeader = new SoapHeader(new QName(namespace, localName), messageId);
        return soapHeader;
    }   

}
