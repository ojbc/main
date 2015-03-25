package org.ojbc.processor.person.query;

import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.concurrent.ConcurrentHashMap;

import org.apache.camel.CamelContext;
import org.apache.camel.spi.UuidGenerator;
import org.hamcrest.CoreMatchers;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.ojbc.util.camel.processor.MessageProcessor;
import org.ojbc.util.camel.security.saml.OJBSamlMap;
import org.ojbc.util.helper.OJBCXMLUtils;
import org.ojbc.web.model.person.query.DetailsRequest;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

public class PersonVehicleToIncidentSearchRequestProcessorTest {

	private PersonVehicleToIncidentSearchRequestProcessor unit;
	
	@Mock
	private MessageProcessor mockVehicleToIncidentMessageProcessor;
	
	@Mock
	private MessageProcessor mockPersonToIncidentMessageProcessor;
	
	@Mock
	private CamelContext mockCamelContext;
	
	@Mock
	private OJBSamlMap mockSamlMap;
	
	@Before
	public void setUp() throws Exception {
		MockitoAnnotations.initMocks(this);
		
		unit = new PersonVehicleToIncidentSearchRequestProcessor();
		unit.setAllowQueriesWithoutSAMLToken(true);
		unit.setCamelContext(mockCamelContext);
		unit.setOJBSamlMap(mockSamlMap);
		unit.setPersonToIncidentMessageProcessor(mockPersonToIncidentMessageProcessor);
		unit.setVehicleToIncidentMessageProcessor(mockVehicleToIncidentMessageProcessor);
		unit.setMaxPolls(1);
		
		ConcurrentHashMap<String, String> requestResponseMap = new ConcurrentHashMap<String, String>();
		unit.setRequestResponseMap(requestResponseMap);
	}

	@Test
	public void invokeRequest_personToIncident_timeout() throws Exception {
		UuidGenerator mockUuidGenerator = mock(UuidGenerator.class);
		when(mockUuidGenerator.generateUuid()).thenReturn("uuid");
		when(mockCamelContext.getUuidGenerator()).thenReturn(mockUuidGenerator);
		
		DetailsRequest detailRequest = new DetailsRequest();
		
		detailRequest.setIdentificationID("id");
		detailRequest.setIdentificationSourceText("{http://ojbc.org/Services/WSDL/IncidentSearchRequestService/1.0}SubmitIncidentPersonSearchRequest");
		
		Document doc = OJBCXMLUtils.createDocument();
		Element samlToken = OJBCXMLUtils.createElement(doc, "ns", "samlToken");
		
		String response = unit.invokeRequest(detailRequest, "queryId", samlToken);
		
		assertThat(response, CoreMatchers.containsString("<isres-doc:IncidentPersonSearchResults xmlns:isres-doc=\"http://ojbc.org/IEPD/Exchange/IncidentSearchResults/1.0\">"));
		assertThat(response, CoreMatchers.containsString("<srer:ErrorText>The source systems timed out or had an error condition.  Please try your search again later.</srer:ErrorText>"));
	}
	
	@Test
	public void invokeRequest_vehicleToIncident_timeout() throws Exception {
		UuidGenerator mockUuidGenerator = mock(UuidGenerator.class);
		when(mockUuidGenerator.generateUuid()).thenReturn("uuid");
		when(mockCamelContext.getUuidGenerator()).thenReturn(mockUuidGenerator);
		
		DetailsRequest detailRequest = new DetailsRequest();
		
		detailRequest.setIdentificationID("id");
		detailRequest.setIdentificationSourceText("{http://ojbc.org/Services/WSDL/IncidentSearchRequestService/1.0}SubmitIncidentVehicleSearchRequest");
		
		Document doc = OJBCXMLUtils.createDocument();
		Element samlToken = OJBCXMLUtils.createElement(doc, "ns", "samlToken");
		
		String response = unit.invokeRequest(detailRequest, "queryId", samlToken);
		
		assertThat(response, CoreMatchers.containsString("<isres-doc:IncidentVehicleSearchResults xmlns:isres-doc=\"http://ojbc.org/IEPD/Exchange/IncidentSearchResults/1.0\">"));
		assertThat(response, CoreMatchers.containsString("<srer:ErrorText>The source systems timed out or had an error condition.  Please try your search again later.</srer:ErrorText>"));
	}

}
