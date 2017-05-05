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
package org.ojbc.util.camel.helper;

import static org.apache.cxf.ws.addressing.JAXWSAConstants.CLIENT_ADDRESSING_PROPERTIES;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.io.StringWriter;
import java.net.URL;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.sax.SAXSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;

import org.apache.camel.Exchange;
import org.apache.camel.converter.jaxp.XmlConverter;
import org.apache.commons.codec.CharEncoding;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.cxf.binding.soap.SoapHeader;
import org.apache.cxf.headers.Header;
import org.apache.cxf.ws.addressing.AddressingProperties;
import org.apache.cxf.ws.addressing.AttributedURIType;
import org.apache.cxf.ws.addressing.EndpointReferenceType;
import org.apache.cxf.ws.addressing.ObjectFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.xml.sax.InputSource;

public class OJBUtils {

	private static final Log log = LogFactory.getLog(OJBUtils.class);
	private static final ObjectFactory WSA_OBJECT_FACTORY =  new ObjectFactory();

	/**
	 * This method returns a map with the following keys to get at WS-Addressing properties "MessageID", "ReplyTo", "From", "To"
	 * We can add to this method to return additional properties as they are needed.
	 * 
	 * @param exchange
	 * @return
	 */
	
	@SuppressWarnings("unchecked")
	public static HashMap<String, String> returnWSAddressingHeadersFromCamelSoapHeaders(Exchange exchange) {
		String messageID = null;
		String replyTo = null;
		String from = null;
		String to = null;
		
		HashMap<String, String> wsAddressingMessageProperties = new HashMap<String, String>();
		
		List<SoapHeader> soapHeaders = (List<SoapHeader>) exchange.getIn()
				.getHeader(Header.HEADER_LIST);

		for (SoapHeader soapHeader : soapHeaders) {
			log.debug("Soap Header: " + soapHeader.getName());
			log.debug("Soap Direction: " + soapHeader.getDirection());

			if (soapHeader.getName().toString()
					.equals("{http://www.w3.org/2005/08/addressing}MessageID")) {
				Element element = (Element) soapHeader.getObject();

				if (element != null) {
					messageID = element.getTextContent();
				}

				log.info("WS-Addressing Message ID: " + messageID);
				
				wsAddressingMessageProperties.put("MessageID", messageID);
			}

			if (soapHeader.getName().toString()
					.equals("{http://www.w3.org/2005/08/addressing}ReplyTo")) {
				Element element = (Element) soapHeader.getObject();

				if (element != null) {
					replyTo = element.getTextContent();
				}

				log.info("WS-Addressing ReplyTo: " + replyTo);
				wsAddressingMessageProperties.put("ReplyTo", replyTo);
			}

			if (soapHeader.getName().toString()
					.equals("{http://www.w3.org/2005/08/addressing}From")) {
				Element element = (Element) soapHeader.getObject();

				if (element != null) {
					from = element.getTextContent();
				}

				log.info("WS-Addressing From: " + from);
				wsAddressingMessageProperties.put("From", from);
			}

			if (soapHeader.getName().toString()
					.equals("{http://www.w3.org/2005/08/addressing}To")) {
				Element element = (Element) soapHeader.getObject();

				if (element != null) {
					to = element.getTextContent();
				}

				log.info("WS-Addressing To: " + to);
				wsAddressingMessageProperties.put("To", to);
			}

		
		}

		return wsAddressingMessageProperties;
	}
	
	/**
	 * This method will set the WS-Addressing Message Properties on the exchange prior to sending an outbound CXF message.
	 * It allows for 'MessageID' and 'ReplyTo'
	 * 
	 * @param senderExchange
	 * @param requestID
	 * @return
	 * @throws Exception
	 */
	
	public static Map<String, Object> setWSAddressingProperties(Map<String, String> wsAddressingMessageProperties) throws Exception {
		
		Map<String, Object> requestContext = null;
		
		if (!wsAddressingMessageProperties.isEmpty())
		{	
			// get Message Addressing Properties instance
	        AddressingProperties maps = new AddressingProperties();
	
	        String messageID = wsAddressingMessageProperties.get("MessageID");
	        
	        if (StringUtils.isNotEmpty(messageID))
	        {
		        // set MessageID property
		        AttributedURIType messageIDAttr =
		            WSA_OBJECT_FACTORY.createAttributedURIType();
		        messageIDAttr.setValue(messageID);
		        maps.setMessageID(messageIDAttr);
	        }
	        
	        String replyToString = wsAddressingMessageProperties.get("ReplyTo");

	        if (StringUtils.isNotEmpty(replyToString))
	        {
	        	AttributedURIType replyToAttr = new AttributedURIType(); 
	        	replyToAttr.setValue(replyToString); 
	        	
	        	EndpointReferenceType replyToRef = new EndpointReferenceType();
	        	replyToRef.setAddress(replyToAttr);

	        	maps.setReplyTo(replyToRef);
	        }
	        
	        String fromString = wsAddressingMessageProperties.get("From");

	        if (StringUtils.isNotEmpty(fromString))
	        {
	        	AttributedURIType fromAttr = new AttributedURIType(); 
	        	fromAttr.setValue(fromString); 
	        	
	        	EndpointReferenceType fromRef = new EndpointReferenceType();
	        	fromRef.setAddress(fromAttr);

	        	maps.setFrom(fromRef);
	        }	        

	        requestContext = new HashMap<String, Object>();
	        requestContext.put(CLIENT_ADDRESSING_PROPERTIES, maps);
	        
		}
		else
		{
			throw new Exception("WS-Addressing Message Properties can not be set.  Map is empty.");
		}	
		
		return requestContext;
	}
	
	/**
	 * This method will set the WS-Addressing Message ID on the exchange prior to sending an outbound CXF message.
	 * This method only allows for a MessageID for backwards compatibility.  Use 'setWSAddressingProperties' to see additional properties
	 * 
	 * @param senderExchange
	 * @param requestID
	 * @return
	 * @throws Exception
	 */
	
	public static Map<String, Object> setWSAddressingMessageID(String requestID) throws Exception {
		
		Map<String, Object> requestContext = null;
		
		if (StringUtils.isNotEmpty(requestID))
		{	
			// get Message Addressing Properties instance
	        AddressingProperties maps = new AddressingProperties();
	
	        // set MessageID property
	        AttributedURIType messageIDAttr =
	            WSA_OBJECT_FACTORY.createAttributedURIType();
	        messageIDAttr.setValue(requestID);
	        maps.setMessageID(messageIDAttr);
			
	        requestContext = new HashMap<String, Object>();
	        requestContext.put(CLIENT_ADDRESSING_PROPERTIES, maps);
	        
		}
		else
		{
			throw new Exception("WS-Addressing Message ID can not be set.  Request ID is empty.");
		}	
		
		return requestContext;
	}	
	
    public static String convertStreamToString(InputStream is) throws Exception {
	    BufferedReader reader = new BufferedReader(new InputStreamReader(is));
	    StringBuilder sb = new StringBuilder();
	    String line = null;
	    while ((line = reader.readLine()) != null) {
	      sb.append(line + "\n");
	    }
	    is.close();
	    return sb.toString();
    }

	//method to convert Document to String
	public static String getStringFromDocument(Document doc)
	{
	    try
	    {
	       DOMSource domSource = new DOMSource(doc);
	       StringWriter writer = new StringWriter();
	       StreamResult result = new StreamResult(writer);
	       TransformerFactory tf = TransformerFactory.newInstance();
	       Transformer transformer = tf.newTransformer();
		   transformer.setOutputProperty(OutputKeys.INDENT, "yes");
		   transformer.setOutputProperty(
					"{http://xml.apache.org/xslt}indent-amount", "4");
		   transformer.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "yes");
	       transformer.transform(domSource, result);
	       return writer.toString();
	    }
	    catch(TransformerException ex)
	    {
	       ex.printStackTrace();
	       return null;
	    }
	} 

	public static Document returnDocumentFromClasspath(String pathToFile) throws Exception
	{
		XmlConverter converter = new XmlConverter();
		DocumentBuilderFactory documentBuilderFactory = converter
				.getDocumentBuilderFactory();
		documentBuilderFactory.setNamespaceAware(true);

		URL staticFileURL = new URL("classpath:" + pathToFile);
		
		BufferedReader reader = new BufferedReader(new InputStreamReader(
				staticFileURL.openStream()));
		Document outDocument = converter.toDOMDocument(new StreamSource(reader));
		
		return outDocument;
	}

	/**
	 * This method accepts an XML string and return a namespace aware XML document
	 * 
	 */
    public static Document loadXMLFromString(String xml) throws Exception
    {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        factory.setNamespaceAware(true);
        
        DocumentBuilder builder = factory.newDocumentBuilder();
        InputSource is = new InputSource(new StringReader(xml));
        Document returnDoc = builder.parse(is);
        return returnDoc;
    }
    
	public static SAXSource createSaxSource(String xml) {
		
		InputSource inputSource = new InputSource(new ByteArrayInputStream(xml.getBytes()));
		inputSource.setEncoding(CharEncoding.UTF_8);
		
		return new SAXSource(inputSource);
	}
	
	public static SAXSource createSaxSource(InputStream inSream) {
		
		InputSource inputSource = new InputSource(inSream);
		inputSource.setEncoding(CharEncoding.UTF_8);
		
		return new SAXSource(inputSource);
	}
	
	
	public String generateUUID() {
						
		return UUID.randomUUID().toString().replaceAll("-", "");		  		  
	}
			
}

