package org.ojbc.processor.firearm.query;

import static org.junit.Assert.assertTrue;

import org.custommonkey.xmlunit.XMLUnit;
import org.junit.Before;
import org.junit.Test;
import org.ojbc.web.model.person.query.DetailsRequest;
import org.ojbc.web.util.RequestMessageBuilderUtilities;

public class FirearmQueryRequestTest {

	@Before
	public void setUp() throws Exception {
		//Tell XML Unit to ignore whitespace between elements and within elements
		XMLUnit.setIgnoreWhitespace(true);
		XMLUnit.setNormalizeWhitespace(true);
	}
	
	@Test
	public void testRequestForPerson(){
		
		DetailsRequest personQueryRequest = new DetailsRequest();
		
		personQueryRequest.setIdentificationSourceText(
				"{http://ojbc.org/Services/WSDL/FirearmRegistrationQueryRequestService/1.0}SubmitFirearmRegistrationQueryRequestByPerson");
		
		String sQueryRequest = RequestMessageBuilderUtilities.createFirearmQueryRequest(personQueryRequest);
				
		boolean containsRootNode = sQueryRequest.contains("PersonFirearmRegistrationRequest");
		
		assertTrue(containsRootNode);
		
		boolean containsIdNode = sQueryRequest.contains("PersonFirearmRegistrationIdentification");
		
		assertTrue(containsIdNode);
	}
	
	@Test
	public void testRequestForFirearm(){
		
		DetailsRequest firearmQueryRequest = new DetailsRequest();
		
		firearmQueryRequest.setIdentificationSourceText(
				"{http://ojbc.org/Services/WSDL/FirearmRegistrationQueryRequestService/1.0}SubmitFirearmRegistrationQueryRequestByFirearm");
		
		String sQueryRequest = RequestMessageBuilderUtilities.createFirearmQueryRequest(firearmQueryRequest);
		
		boolean containsRootNode = sQueryRequest.contains("FirearmRegistrationRequest");
		
		assertTrue(containsRootNode);
		
		boolean containsIdNode = sQueryRequest.contains("FirearmRegistrationIdentification");
		
		assertTrue(containsIdNode);
	}

	@Test
	public void testRequestForFirearmMaui() throws Exception{
		
		DetailsRequest firearmQueryRequest = new DetailsRequest();
		
		firearmQueryRequest.setIdentificationSourceText(
				"{http://ojbc.org/Services/WSDL/FirearmRegistrationQueryRequestService/1.0}SubmitFirearmRegistrationQueryRequestByFirearm-Maui");
		
		String sQueryRequest = RequestMessageBuilderUtilities.createFirearmQueryRequest(firearmQueryRequest);
		
		boolean containsRootNode = sQueryRequest.contains("FirearmRegistrationRequest");
		
		assertTrue(containsRootNode);
		
		boolean containsIdNode = sQueryRequest.contains("FirearmRegistrationIdentification");
		
		assertTrue(containsIdNode);

		//TODO: This validation test was throwing a 'stream closed' exception prior to actually validating. Figure out why
		// note maybe b/c path references using the classloader in xmlutils and resource resolver classes  are called from 
		// other projects and not from the connector.  Somehow the connector is not able to reference these paths in ojbresrouces		
//		Document resultingDoc = XMLUtils.parse(sQueryRequest);
//		XmlUtils.validateInstance("service-specifications/Firearm_Registration_Query_Request_Service/schema/information/Firearm_Registration_Query_Request_IEPD/xsd", "Subset/niem", "exchange_schema.xsd",resultingDoc);
		
	}

	@Test
	public void testRequestForPersonMaui(){
		
		DetailsRequest personQueryRequest = new DetailsRequest();
		
		personQueryRequest.setIdentificationSourceText(
				"{http://ojbc.org/Services/WSDL/FirearmRegistrationQueryRequestService/1.0}SubmitFirearmRegistrationQueryRequestByPerson-Maui");
		
		String sQueryRequest = RequestMessageBuilderUtilities.createFirearmQueryRequest(personQueryRequest);
				
		boolean containsRootNode = sQueryRequest.contains("PersonFirearmRegistrationRequest");
		
		assertTrue(containsRootNode);
		
		boolean containsIdNode = sQueryRequest.contains("PersonFirearmRegistrationIdentification");
		
		assertTrue(containsIdNode);
	}
	
}


