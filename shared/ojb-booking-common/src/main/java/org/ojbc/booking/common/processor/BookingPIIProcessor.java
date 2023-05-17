package org.ojbc.booking.common.processor;

import org.apache.camel.Header;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.ojbc.util.xml.XmlUtils;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class BookingPIIProcessor {
	
	private static final String CUSTODY_RELEASE_XPATH="/crr-exc:CustodyReleaseReport/crr-ext:Custody";
	private static final String CUSTODY_STATUS_CHANGE_XPATH="/cscr-doc:CustodyStatusChangeReport/cscr-ext:Custody";
	private static final String BOOKING_XPATH="/br-doc:BookingReport";

	private static final Log log = LogFactory.getLog( BookingPIIProcessor.class );
	
	public Document removePII(Document custodyBookingMessage, @Header(value="custodyBookingType") String custodyBookingType) throws Exception
	{
		String rootXpath = "";
		String personRef ="";
		
		//Determine root xpath based off inbound parameter
		if (custodyBookingType.equals("CustodyRelease"))
		{
			rootXpath = CUSTODY_RELEASE_XPATH;
			personRef = XmlUtils.xPathStringSearch(custodyBookingMessage, rootXpath + "/jxdm51:Booking/jxdm51:BookingSubject/nc30:RoleOfPerson/@s30:ref");
		} 
		else if (custodyBookingType.equals("CustodyStatusChange"))
		{
			rootXpath = CUSTODY_STATUS_CHANGE_XPATH;
			personRef = XmlUtils.xPathStringSearch(custodyBookingMessage, rootXpath + "/jxdm51:Booking/jxdm51:BookingSubject/nc30:RoleOfPerson/@s30:ref");
		}
		else if (custodyBookingType.equals("Booking"))
		{
			rootXpath = BOOKING_XPATH;
			personRef = XmlUtils.xPathStringSearch(custodyBookingMessage, rootXpath + "/jxdm51:Booking/jxdm51:BookingSubject/nc30:RoleOfPerson/@s30:ref");
			
		}	
		else
		{
			throw new IllegalStateException("Unable to determine message type");
		}	
		
		log.info("Booking Subject Ref: " + personRef);
		
		Node personNode = XmlUtils.xPathNodeSearch(custodyBookingMessage, rootXpath + "/nc30:Person[@s30:id='" + personRef + "']");

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
		NodeList personAliases = XmlUtils.xPathNodeListSearch(custodyBookingMessage, rootXpath + "/nc30:Identity");
		
		for (int i = 0; i < personAliases.getLength(); i++) {
		  Element alias = (Element)personAliases.item(i);
		  alias.getParentNode().removeChild(alias);
		}
		
		//Remove alias associations
		Node personAliasAssocationNode = XmlUtils.xPathNodeSearch(custodyBookingMessage, rootXpath + "/nc30:PersonAliasIdentityAssociation");
		
		if (personAliasAssocationNode != null)
		{	
			personAliasAssocationNode.getParentNode().removeChild(personAliasAssocationNode);
		}	

		
		return custodyBookingMessage;
	}
	
}
