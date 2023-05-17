package org.ojbc.adamscounty.personhealth.aggregator;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.camel.Exchange;
import org.apache.camel.component.cxf.CxfPayload;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.ojbc.util.fedquery.FederatedQueryProfile;
import org.ojbc.util.xml.OjbcNamespaceContext;
import org.ojbc.util.xml.XmlUtils;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class PersonHealthInformationAggregator {
	private static final Log log = LogFactory.getLog( PersonHealthInformationAggregator.class );
	private Map<String, List<FederatedQueryProfile>> federatedQueryManager;
	
	private static final String CUSTODY_RELEASE_XPATH="/crr-exc:CustodyReleaseReport/crr-ext:Custody";
	private static final String CUSTODY_STATUS_CHANGE_XPATH="/cscr-doc:CustodyStatusChangeReport/cscr-ext:Custody";
	private static final String BOOKING_XPATH="/br-doc:BookingReport";
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public void removePIIandEnrichWithHealthInformation(Exchange groupedExchange) throws Exception
	{
		List<Exchange> grouped = groupedExchange.getProperty(Exchange.GROUPED_EXCHANGE, List.class);
		
        List<String> endpointsThatDidNotRespond = new ArrayList<String>();
        
        Document originalCustodyBookingMessage = null;
        Document personHealthInformationResponse = null;
        String custodyBookingType = "";
        Boolean includeEthnicityInMessage = false;
        
		for (Exchange exchange : grouped)
		{
		
			//This exchange is the message sent to start the federated query timer, it is an exchange with a string that says 'START_QUERY_TIMER'	
			if (exchange.getIn().getBody().getClass().getName().equals("java.lang.String") && exchange.getIn().getBody().equals("START_QUERY_TIMER"))
			{
				 String startMessage = exchange.getIn().getBody(String.class); 
				 
				 String messageID = (String)exchange.getIn().getHeader("federatedQueryRequestGUID");

				 //Clear the request out of the hashmap by using the message ID
				 federatedQueryManager.remove(messageID);

				 //The new grouped exchange does not get the message headers from the original exchange so we manually copy the message ID
				 groupedExchange.getIn().setHeader("federatedQueryRequestGUID", messageID);
				 
				 //Set the operation name here in case no other responses were received so the message can be properly processed.
				 //This will be overwritten if we actually got responses
				 groupedExchange.getIn().setHeader("operationName", exchange.getIn().getHeader("operationName"));
				 
				 originalCustodyBookingMessage = (Document) exchange.getIn().getHeader("originalBoookingCustodyMessageBody");
				 
				 log.debug("Processing aggregator start message: " + startMessage);
				 
				 custodyBookingType = (String) exchange.getIn().getHeader("custodyBookingType");
				
				 continue; 
			}
			
			if (exchange.getIn().getBody().getClass().getName().equals("org.apache.camel.component.cxf.CxfPayload"))
			{
				
				//Uncomment the line below to see the individual aggregated message
				//log.debug("This is the body of the exchange in the exchange group: " + exchange.getIn().getBody());
				
				CxfPayload cxfPayload = (CxfPayload)exchange.getIn().getBody();
				List<Element> elementList = cxfPayload.getBody();
				
				personHealthInformationResponse = elementList.get(0).getOwnerDocument();
				
				if (StringUtils.isNotBlank((String)exchange.getIn().getHeader("includeEthnicityInMessage")))
				{
					includeEthnicityInMessage = true;
				}	 
				 
				//Remove booking number since it is no longer needed and only used to report errors
				String bookingNumber = (String) exchange.getIn().getHeader("bookingNumber");
				 
				if (StringUtils.isNotBlank(bookingNumber))
				{ 
					exchange.getIn().removeHeader("bookingNumber");
				}	 
				
				continue; 
			}	
			
		}	
		
		String aggregatedCompletedBy = (String)groupedExchange.getProperty(Exchange.AGGREGATED_COMPLETED_BY);
		
		//When there is a timeout, find out which endpoint timed out
		if (aggregatedCompletedBy != null && aggregatedCompletedBy.equals("timeout"))
		{
			log.info("Federated Query Completed by timeout.");
			
			Map<String, Boolean> endpointsCalled = null;
			
			Exchange timerExchange = grouped.get(0);
			
			//Look in the timer exchange for the original endpoints called
			if (timerExchange.getIn().getBody().getClass().getName().equals("java.lang.String") && timerExchange.getIn().getBody().equals("START_QUERY_TIMER"))
			{
				endpointsCalled = returnEndpointsCalled(timerExchange);   
			}	
			
			//Iterate through the exchanges and mark in the map which queries returned a response
			if (endpointsCalled != null)
			{
				for (Exchange exchange : grouped)
				{
					if (exchange.getIn().getBody().getClass().getName().equals("org.apache.camel.component.cxf.CxfPayload"))
					{
						String searchProfileInResponseExchange = (String) exchange.getIn().getHeader("searchProfile");
						
						log.info("Response Recieved from: " + searchProfileInResponseExchange);
						
						//We put the key/value in the hashmap and it will indicate that we received a response
						//It will overwrite the existing entry in the map
						endpointsCalled.put(searchProfileInResponseExchange, true);
					}	
				}	
			}
			
			//We add the endpoints that did not respond to a list
			//The list is then available as a Camel header
			for(Entry entry: endpointsCalled.entrySet()) 
			{
				  // get key
				  String searchProfileInResponseExchange = (String)entry.getKey();
				  // get value
				  Boolean responseReceived = (Boolean) entry.getValue();
				  
				  if (!responseReceived)
				  {
					  log.info(searchProfileInResponseExchange + " did not return a response.");
					  endpointsThatDidNotRespond.add(searchProfileInResponseExchange);
				  }	  
			}
			
			if (!endpointsThatDidNotRespond.isEmpty())
			{
				groupedExchange.getIn().setHeader("endpointsThatDidNotRespond", endpointsThatDidNotRespond);
			}	
			
		}
			
		Document enrichedCustodyBookingMessage = aggregatePersonHealthWithCustodyBooking(personHealthInformationResponse, originalCustodyBookingMessage, includeEthnicityInMessage, custodyBookingType);
		groupedExchange.getIn().setBody(enrichedCustodyBookingMessage);

	}

	private Document aggregatePersonHealthWithCustodyBooking(
			Document personHealthInformationResponse,
			Document originalCustodyBookingMessage,
			Boolean includeEthnicityInMessage,
			String messageType
			) throws Exception {
		
		String rootXpath = "";
		String extensionNamespace ="";
		String personRef ="";
		
		if (messageType.equals("CustodyRelease"))
		{
			rootXpath = CUSTODY_RELEASE_XPATH;
			extensionNamespace=OjbcNamespaceContext.NS_CUSTODY_RELEASE_REPORTING_EXT;
			personRef = XmlUtils.xPathStringSearch(originalCustodyBookingMessage, rootXpath + "/jxdm51:Booking/jxdm51:BookingSubject/nc30:RoleOfPerson/@s30:ref");
		} 
		else if (messageType.equals("CustodyStatusChange"))
		{
			rootXpath = CUSTODY_STATUS_CHANGE_XPATH;
			extensionNamespace=OjbcNamespaceContext.NS_CUSTODY_STATUS_CHANGE_EXT;
			personRef = XmlUtils.xPathStringSearch(originalCustodyBookingMessage, rootXpath + "/jxdm51:Booking/jxdm51:BookingSubject/nc30:RoleOfPerson/@s30:ref");
		}
		else if (messageType.equals("Booking"))
		{
			rootXpath = BOOKING_XPATH;
			extensionNamespace=OjbcNamespaceContext.NS_BOOKING_REPORTING_EXT;
			personRef = XmlUtils.xPathStringSearch(originalCustodyBookingMessage, rootXpath + "/jxdm51:Booking/jxdm51:BookingSubject/nc30:RoleOfPerson/@s30:ref");
			
		}	
		else
		{
			throw new IllegalStateException("Unable to determine message type");
		}	
		
		
		log.info("Booking Subject Ref: " + personRef);
		
		Node personNode = XmlUtils.xPathNodeSearch(originalCustodyBookingMessage, rootXpath + "/nc30:Person[@s30:id='" + personRef + "']");

		//Remove Name
		Node personName = XmlUtils.xPathNodeSearch(personNode, "nc30:PersonName");
		
		if (personName != null)
		{	
			personName.getParentNode().removeChild(personName);
		}	
		
		//Remove SID
		Node personFingerPrintNode = XmlUtils.xPathNodeSearch(personNode, "jxdm51:PersonAugmentation/jxdm51:PersonStateFingerprintIdentification");

		if (personFingerPrintNode != null)
		{
			personFingerPrintNode.getParentNode().removeChild(personFingerPrintNode);
		}	

		//Remove SSN
		Node personSSN = XmlUtils.xPathNodeSearch(personNode, "nc30:PersonSSNIdentification");
		
		if (personSSN != null)
		{	
			personSSN.getParentNode().removeChild(personSSN);
		}	

		//Remove Aliases
		NodeList personAliases = XmlUtils.xPathNodeListSearch(originalCustodyBookingMessage, rootXpath + "/nc30:Identity");
		
		for (int i = 0; i < personAliases.getLength(); i++) {
		  Element alias = (Element)personAliases.item(i);
		  alias.getParentNode().removeChild(alias);
		}
		
		//Remove alias associations
		Node personAliasAssocationNode = XmlUtils.xPathNodeSearch(originalCustodyBookingMessage, rootXpath + "/nc30:PersonAliasIdentityAssociation");
		
		if (personAliasAssocationNode != null)
		{	
			personAliasAssocationNode.getParentNode().removeChild(personAliasAssocationNode);
		}	
		
		//Conditionally add the ethnicity node
		if (includeEthnicityInMessage)
		{
			//Get person ethnicity from person search results:
			String personEthnicity = XmlUtils.xPathStringSearch(personHealthInformationResponse, "/phisres-doc:PersonHealthInformationSearchResults/nc30:Person/pc-phi-codes:PersonEthnicityCode");
			log.info("Person ethnicity code: " + personEthnicity);
			
			Element personEthnicityCode = XmlUtils.appendElement((Element)personNode, OjbcNamespaceContext.NS_PIMA_BOOKING_CODES, "pc-bkg-codes:PersonEthnicityCode");
			personEthnicityCode.setTextContent(personEthnicity);

		}
			
		//Create PersonPersistentIdentification and add value
		
		//Get person persistent identification from person search results:
		String personPersistentIdentification = XmlUtils.xPathStringSearch(personHealthInformationResponse, "/phisres-doc:PersonHealthInformationSearchResults/nc30:Person/phisres-ext:PersonPersistentIdentification/nc30:IdentificationID");
		log.info("Person persistent identification: " + personPersistentIdentification);
		
		//Insert as last person member (custody status release example below)
//		<cscr-ext:PersonPersistentIdentification>
//			<nc:IdentificationID>e807f1fcf82d132f9bb018ca6738a19f</nc:IdentificationID>
//		</cscr-ext:PersonPersistentIdentification>
		
		if (StringUtils.isNotBlank(personPersistentIdentification))
		{	
			Element personPersistentIdentificationCustody = XmlUtils.appendElement((Element)personNode, extensionNamespace, "PersonPersistentIdentification");
			
			Element identificationID = XmlUtils.appendElement(personPersistentIdentificationCustody, OjbcNamespaceContext.NS_NC_30, "IdentificationID");
			identificationID.setTextContent(personPersistentIdentification.trim());
		}	
			
		//Get behavior health node
		Node behaviorHealthInformationNode = XmlUtils.xPathNodeSearch(personHealthInformationResponse, "/phisres-doc:PersonHealthInformationSearchResults/phisres-ext:BehavioralHealthInformation");
		
		//Get care episode node
		Node careEpisodeNodeSearchResults = XmlUtils.xPathNodeSearch(personHealthInformationResponse, "/phisres-doc:PersonHealthInformationSearchResults/phisres-ext:CareEpisode");
		
//		<br-ext:PersonBehavioralHealthInformation
//		structures:ref="PBHI_01" />
//		<br-ext:PersonCareEpisode structures:ref="PCE_01" />
		
		//Append behavior health and care episode to document
		if (behaviorHealthInformationNode !=null || careEpisodeNodeSearchResults !=null)
		{

			Node parentNode = XmlUtils.xPathNodeSearch(originalCustodyBookingMessage, rootXpath);
			
			if (behaviorHealthInformationNode !=null)
			{
				Element personBehavioralHealthInformationNode = XmlUtils.appendElement((Element)personNode, extensionNamespace, "PersonBehavioralHealthInformation");
				XmlUtils.addAttribute(personBehavioralHealthInformationNode, OjbcNamespaceContext.NS_STRUCTURES_30, "ref", "PBH_01");
				
				renameNamespaceRecursive(behaviorHealthInformationNode, extensionNamespace);
				
			    Node behaviorHealthNodeBookingCustody = originalCustodyBookingMessage.importNode(behaviorHealthInformationNode, true);
			    XmlUtils.addAttribute((Element)behaviorHealthInformationNode, OjbcNamespaceContext.NS_STRUCTURES_30, "id", "PBH_01");
			    parentNode.appendChild(behaviorHealthNodeBookingCustody);
			}
			
			if (careEpisodeNodeSearchResults !=null)
			{
				Element personCareEpisodeNode = XmlUtils.appendElement((Element)personNode, extensionNamespace, "PersonCareEpisode");
				XmlUtils.addAttribute(personCareEpisodeNode, OjbcNamespaceContext.NS_STRUCTURES_30, "ref", "PCE_01");
				
				renameNamespaceRecursive(careEpisodeNodeSearchResults, extensionNamespace);
				
			    Node treatmentNodeCustodyBooking = originalCustodyBookingMessage.importNode(careEpisodeNodeSearchResults, true);
			    XmlUtils.addAttribute((Element)treatmentNodeCustodyBooking, OjbcNamespaceContext.NS_STRUCTURES_30, "id", "PCE_01");
			    parentNode.appendChild(treatmentNodeCustodyBooking);
			}	
			
			
		}	
		
		return originalCustodyBookingMessage;
	}
	
	/**
	 * Recursively renames the namespace of a node.
	 * @param node the starting node.
	 * @param namespace the new namespace. Supplying <tt>null</tt> removes the namespace.
	 * 
	 * FROM:
	 * https://blog.avisi.nl/2013/07/24/java-stripping-namespaces-from-xml-using-dom/
	 * 
	 */
	public static void renameNamespaceRecursive(Node node, String namespace) {
	    Document document = node.getOwnerDocument();
	    if (node.getNodeType() == Node.ELEMENT_NODE) {
	    	if (node.getNamespaceURI() == OjbcNamespaceContext.NS_PERSON_HEALTH_INFORMATION_SEARCH_RESULTS_EXT)
	    	{	
	    		document.renameNode(node, namespace, node.getNodeName());
	    	}	
	    }
	    NodeList list = node.getChildNodes();
	    for (int i = 0; i < list.getLength(); ++i) {
	        renameNamespaceRecursive(list.item(i), namespace);
	    }
	}	

	/**
	 * This method will return a map containing the endpoints called.  It will set the boolean value
	 * to false indicating that a response has not been received for them.  We will later iterate
	 * through the response exchanges to see which endpoints did return responses.
	 * 
	 * @param exchange
	 * @return
	 */

	protected Map<String, Boolean> returnEndpointsCalled(Exchange exchange) {
		NodeList federatedQueryEndpointsNodeList = (NodeList)exchange.getIn().getHeader("federatedQueryEndpointsNodeList");
		
		Map<String, Boolean> endpointsCalled = new HashMap<String, Boolean>();
		
		if (federatedQueryEndpointsNodeList != null && federatedQueryEndpointsNodeList.getLength() > 0) 
		{
		    for (int i = 0; i < federatedQueryEndpointsNodeList.getLength(); i++) 
		    {
		        if (federatedQueryEndpointsNodeList.item(i).getNodeType() == Node.ELEMENT_NODE) 
		        {
		            Element el = (Element) federatedQueryEndpointsNodeList.item(i);
		            log.info("Endpoint in original request: " + el.getTextContent());
		            
		            endpointsCalled.put(el.getTextContent(), false);
		        }
		    }
		}
		
		return endpointsCalled;
	}
	

	public Map<String, List<FederatedQueryProfile>> getFederatedQueryManager() {
		return federatedQueryManager;
	}

	public void setFederatedQueryManager(
			Map<String, List<FederatedQueryProfile>> federatedQueryManager) {
		this.federatedQueryManager = federatedQueryManager;
	} 
}
