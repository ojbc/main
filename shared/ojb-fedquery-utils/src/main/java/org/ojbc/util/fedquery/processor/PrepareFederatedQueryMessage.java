package org.ojbc.util.fedquery.processor;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.apache.camel.component.cxf.CxfPayload;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.cxf.endpoint.Client;
import org.ojbc.util.camel.helper.OJBUtils;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

/**
 * This processor is used by the Federated Query Recipient List to prepare the message for processing
 * @author Yogesh Chawla
 *
 */
public class PrepareFederatedQueryMessage  implements Processor{

	private static final Log log = LogFactory.getLog( PrepareFederatedQueryMessage.class );
	private Map<String, String> federatedQueryEndpointMap;
	
    public void process(Exchange exchange) throws Exception {
		//Get message ID
    	String requestID = (String)exchange.getIn().getHeader("federatedQueryRequestGUID");
		
    	//If we are querying more than one source system, filter out the source system names not relevant to the source system
    	if (federatedQueryEndpointMap != null && federatedQueryEndpointMap.size() > 1)
    	{	
	    	//Get the 'To' endpoint from the camel exchange, this is the endpoint that the recipient list is currently sending to
	    	String camelToEndpoint = (String)exchange.getProperty(Exchange.TO_ENDPOINT);
	    	
	    	//Construct the endpoint name, we use a endpoint / cxf bean naming convention that allows for this
	    	String endpointName = StringUtils.substringBetween(camelToEndpoint, "cxf://bean:", "?") + "Endpoint";
	    	
	    	String endpointUriValue = "";
	    	
	    	//Iterate through the endpoint map so we can get the URI for the endpoint we are calling
	    	//This map is small so this technique will perform okay
	    	for (Entry<String, String> endpointEntryInMap : federatedQueryEndpointMap.entrySet()) {
	    	    
	    		String endpointNameInMap = endpointEntryInMap.getValue();
	    		
	    		if (endpointName.equals(endpointNameInMap))
	    		{
	    			endpointUriValue = endpointEntryInMap.getKey();
	    			break;
	    		}	
	    		
	    	}
	    	
	    	//Get the message body from the message exchange
	    	CxfPayload cxfPayload = (CxfPayload)exchange.getIn().getBody();
	    	Element body = (Element)cxfPayload.getBody().get(0);
	    	
	    	//We need to clone the node because the recipient list message is mutable
	    	Document requestDocument = (Document) body.getOwnerDocument().cloneNode(true);
	    	NodeList sourceSystems = requestDocument.getElementsByTagName("SourceSystemNameText");
	    	
	    	Set<Node> targetElementsToRemove = new HashSet<Node>();
	    	
	    	//Loop through the source systems and remove any source systems that don't match the system we are calling
	    	//We can't remove them from the live list so we put them in a set and remove later
	    	//http://stackoverflow.com/questions/1374088/removing-dom-nodes-when-traversing-a-nodelist
	    	 for(int s=0; s<sourceSystems.getLength(); s++){
	    		
	    		 Node sourceSystemNode = sourceSystems.item(s);
	    		 	    		 
	             if(sourceSystemNode.getNodeType() == Node.ELEMENT_NODE){
	            	 
	            	 String currentSourceSystemNode = sourceSystemNode.getTextContent();
	            	 
	            	 if (!currentSourceSystemNode.equals(endpointUriValue))
	            	 {
	            		 targetElementsToRemove.add(sourceSystemNode);
	            	 }	 
	             }	 
	            	 
	    	 }
	    	 
	    	 for (Node e: targetElementsToRemove) {
	    		  e.getParentNode().removeChild(e);
	    	 }
	    	
	    	 //Set the message body of the modified message
	    	 exchange.getIn().setBody(requestDocument);
    	}	 
	    	 
    	//Create a new map with WS Addressing message properties that we want to override
		HashMap<String, String> wsAddressingMessageProperties = new HashMap<String, String>();
		wsAddressingMessageProperties.put("MessageID",requestID);

		String replyTo = (String)exchange.getIn().getHeader("WSAddressingReplyTo");

		if (StringUtils.isNotEmpty(replyTo))
		{	
			log.debug("WS Addressing Reply To Camel Header: " + replyTo);
			wsAddressingMessageProperties.put("ReplyTo",replyTo);
		}

		
		//Call method to create proper request context map
		Map<String, Object> requestContext = OJBUtils.setWSAddressingProperties(wsAddressingMessageProperties);
		
		//Remove obsolete headers to create clean exchange
		exchange.getIn().removeHeaders("*", "federatedQueryRequestGUID", "operationName", "operationNamespace", "tokenID", Exchange.DESTINATION_OVERRIDE_URL);
		
		//Set WS-Addressing message properties header
		exchange.getIn().setHeader(Client.REQUEST_CONTEXT , requestContext);
		log.debug("Federated Query ID: " + exchange.getIn().getHeader("federatedQueryRequestGUID"));
    	
    }

	public Map<String, String> getFederatedQueryEndpointMap() {
		return federatedQueryEndpointMap;
	}

	public void setFederatedQueryEndpointMap(
			Map<String, String> federatedQueryEndpointMap) {
		this.federatedQueryEndpointMap = federatedQueryEndpointMap;
	}

}
