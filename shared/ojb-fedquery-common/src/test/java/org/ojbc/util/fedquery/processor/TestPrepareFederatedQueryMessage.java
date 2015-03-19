package org.ojbc.util.fedquery.processor;

import static junit.framework.Assert.assertNotNull;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.xml.transform.Source;
import javax.xml.transform.dom.DOMSource;

import junit.framework.Assert;

import org.apache.camel.CamelContext;
import org.apache.camel.Exchange;
import org.apache.camel.component.cxf.CxfPayload;
import org.apache.camel.impl.DefaultCamelContext;
import org.apache.camel.impl.DefaultExchange;
import org.apache.cxf.endpoint.Client;
import org.apache.cxf.ws.addressing.JAXWSAConstants;
import org.apache.cxf.ws.addressing.impl.AddressingPropertiesImpl;
import org.junit.Test;
import org.ojbc.util.xml.XmlUtils;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

public class TestPrepareFederatedQueryMessage {

	@SuppressWarnings("unchecked")
	@Test
	public void testPrepareFederatedQueryMessage() throws Exception{

		//Set the body of the input message
	    CamelContext ctx = new DefaultCamelContext(); 
	    Exchange exchange = new DefaultExchange(ctx);

	    Document searchDocument = XmlUtils.parseFileToDocument(new File("src/test/resources/xml/PersonSearchRequest.xml"));
	    assertNotNull(searchDocument);

	    NodeList sourceSystems = XmlUtils.xPathNodeListSearch(searchDocument, "//psr:SourceSystemNameText");
	    assertNotNull(sourceSystems);

	    Assert.assertEquals(2, sourceSystems.getLength());
	    
	    Element sourceSystemNameElement = (Element)sourceSystems.item(0);
	    assertNotNull(sourceSystemNameElement);
	    
	    exchange.getIn().setBody(sourceSystemNameElement);
	    exchange.getIn().setHeader("federatedQueryRequestGUID", "12345");
	    exchange.getIn().setHeader("tokenID", "1234567");
	    exchange.getIn().setHeader("WSAddressingReplyTo", "http://myReplyToAddress");
	    
        exchange.getIn().setHeader("requestMessageBody", searchDocument);
	    
	    //Set up the prepare federated query bean
	    PrepareFederatedQueryMessage prepareFederatedQueryMessage = new PrepareFederatedQueryMessage();
	    
	    RecordSourceExchangeDestinationLookupStrategy recordSourceExchangeDestinationLookupStrategy = new RecordSourceExchangeDestinationLookupStrategy();
	    
	    //Set up Federated Query Endpoint map
	    Map<String, String> federatedQueryEndpointMap = new HashMap<String, String>();
	    
	    federatedQueryEndpointMap.put("{http://ojbc.org/Services/WSDL/Person_Search_Request_Service/Criminal_History/1.0}Submit-Person-Search---Criminal-History", "personSearchRequestServiceAdapterEndpoint");
	    federatedQueryEndpointMap.put("{http://ojbc.org/Services/WSDL/Person_Search_Request_Service/Warrants/1.0}Submit-Person-Search---Warrants", "personSearchRequestServiceAdapterEndpoint");
	    federatedQueryEndpointMap.put("{http://ojbc.org/Services/WSDL/PersonSearchRequestService/1.0}SubmitPersonSearchRequest-RMS","personSearchRequestServiceAdapterEndpoint");
	    federatedQueryEndpointMap.put("{http://ojbc.org/Services/WSDL/Person_Search_Request_Service/Firearms/1.0}Submit-Person-Search---Firearms","personSearchRequestServiceAdapterEndpoint");
	    
	    recordSourceExchangeDestinationLookupStrategy.setFederatedQueryEndpointMap(federatedQueryEndpointMap);
	    
	    //Set up adapterURItoAddressMap and processor
	    Map<String, String> adapterURItoAddressMap = new HashMap<String, String>();
	    
	    adapterURItoAddressMap.put("{http://ojbc.org/Services/WSDL/Person_Search_Request_Service/Criminal_History/1.0}Submit-Person-Search---Criminal-History", "http://crimhistoryAdapter");
	    adapterURItoAddressMap.put("{http://ojbc.org/Services/WSDL/Person_Search_Request_Service/Warrants/1.0}Submit-Person-Search---Warrants", "http://warrantsAdapter");
	    adapterURItoAddressMap.put("{http://ojbc.org/Services/WSDL/PersonSearchRequestService/1.0}SubmitPersonSearchRequest-RMS","http://rmsAdapter");
	    adapterURItoAddressMap.put("{http://ojbc.org/Services/WSDL/Person_Search_Request_Service/Firearms/1.0}Submit-Person-Search---Firearms","http://firearmsAdapter");
	    
	    recordSourceExchangeDestinationLookupStrategy.setAdapterURItoAddressMap(adapterURItoAddressMap);
	    
	    prepareFederatedQueryMessage.setExchangeDestinationLookupStrategy(recordSourceExchangeDestinationLookupStrategy);
	    
	    //Test prepare federated query processor - exchange properties
		prepareFederatedQueryMessage.process(exchange);
		
		Assert.assertEquals("12345", exchange.getIn().getHeader("federatedQueryRequestGUID"));
		Assert.assertEquals("http://rmsAdapter", exchange.getIn().getHeader(Exchange.DESTINATION_OVERRIDE_URL));
		Assert.assertEquals("personSearchRequestServiceAdapterEndpoint", exchange.getIn().getHeader("webServiceEndpointToCall"));
		
		HashMap<String, Object> requestContextMap = (HashMap<String, Object>)exchange.getIn().getHeader(Client.REQUEST_CONTEXT);

		AddressingPropertiesImpl clientAddressingpProps = (AddressingPropertiesImpl)requestContextMap.get(JAXWSAConstants.CLIENT_ADDRESSING_PROPERTIES);
		
		Assert.assertEquals("12345", clientAddressingpProps.getMessageID().getValue());
		Assert.assertEquals("http://myReplyToAddress", clientAddressingpProps.getReplyTo().getAddress().getValue());

		//Confirm we only have source system name of adapter we are calling
		Document updatedRequestBody = exchange.getIn().getBody(Document.class);
		XmlUtils.printNode(updatedRequestBody);
		NodeList sourceSystemsUpdated = XmlUtils.xPathNodeListSearch(updatedRequestBody, "//psr:SourceSystemNameText");
		
		Assert.assertEquals(1, sourceSystemsUpdated.getLength());
		Assert.assertEquals("{http://ojbc.org/Services/WSDL/PersonSearchRequestService/1.0}SubmitPersonSearchRequest-RMS", sourceSystemsUpdated.item(0).getTextContent());
		
		
	}	
	
}
