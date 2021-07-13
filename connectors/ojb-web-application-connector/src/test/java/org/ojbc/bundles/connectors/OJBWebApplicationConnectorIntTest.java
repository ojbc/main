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
package org.ojbc.bundles.connectors;

import java.util.HashMap;
import java.util.Map;

import org.apache.camel.model.ModelCamelContext;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.junit.Assert;
import org.junit.Ignore;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.ojbc.processor.person.query.JuvenileQueryRequestProcessor;
import org.ojbc.processor.person.search.PersonSearchRequestProcessor;
import org.ojbc.processor.policy.acknowledge.PolicyAcknowledgmentRecordingRequestProcessor;
import org.ojbc.processor.policy.query.IdentityBasedAccessControlRequestProcessor;
import org.ojbc.processor.subscription.query.SubscriptionQueryRequestProcessor;
import org.ojbc.processor.subscription.search.SubscriptionSearchRequestProcessor;
import org.ojbc.processor.subscription.subscribe.SubscriptionRequestProcessor;
import org.ojbc.processor.subscription.unsubscribe.UnsubscriptionRequestProcessor;
import org.ojbc.util.camel.security.saml.SAMLTokenUtils;
import org.ojbc.util.model.saml.SamlAttribute;
import org.ojbc.util.xml.subscription.Subscription;
import org.ojbc.util.xml.subscription.Unsubscription;
import org.ojbc.web.impl.DetailQueryDispatcher;
import org.ojbc.web.model.person.query.DetailsRequest;
import org.ojbc.web.model.person.search.PersonSearchRequest;
import org.ojbc.web.model.person.search.PersonSearchRequestTestUtils;
import org.ojbc.web.model.subscription.search.SubscriptionSearchRequest;
import org.opensaml.xmlsec.signature.support.SignatureConstants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.w3c.dom.Element;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(locations={"/META-INF/spring/spring-beans-ojb-web-application-connector-context.xml"})
@ActiveProfiles(profiles={"person-search", "incident-search", "vehicle-search", "firearms-search","person-vehicle-to-incident-search", 
		"warrants-query", "criminal-history-query", "firearms-query","incident-report-query", 
		"professional-license-query", "rapback-search", "arrest-search", "initial-results-query", "identification-results-modification", 
		"person-to-court-case-search" ,"cannabis-license-query", "wildlife-license-query", "court-case-query","person-to-custody-search",
		"custody-query", "vehicle-crash-query", "firearms-purchase-prohibition-query",
		"subscriptions", "policy-acknowledgement", "access-control", "juvenile-query"})
@Ignore //TODO I am confused about how the tests works without using advice.  I'll come back to this later. 
public class OJBWebApplicationConnectorIntTest{

	private static final Log log = LogFactory.getLog( OJBWebApplicationConnectorIntTest.class );
	
	@Autowired
	DetailQueryDispatcher detailQueryDispatcher;

	@Autowired
	private PersonSearchRequestProcessor personSearchRequestProcessor;

	@Autowired
	private SubscriptionSearchRequestProcessor subscriptionSearchRequestProcessor;

	@Autowired
	private SubscriptionQueryRequestProcessor subscriptionQueryRequestProcessor;

	@Autowired
	private UnsubscriptionRequestProcessor unsubscriptionRequestProcessor;

	@Autowired
	private SubscriptionRequestProcessor subscriptionRequestProcessor;
	
	@Autowired
	private IdentityBasedAccessControlRequestProcessor identityBasedAccessControlRequestProcessor;
	
	@Autowired
	private PolicyAcknowledgmentRecordingRequestProcessor policyAcknowledgingRequestProcessor;
	
    @Value("${policy.accesscontrol.requestedresource:}")
    private String policyAccessControlResourceURI;
	
	@Autowired
	private JuvenileQueryRequestProcessor juvenileCasePlanHistoryRequestProcessor;

	@Autowired
	private JuvenileQueryRequestProcessor juvenileOffenseHistoryRequestProcessor;

	@Autowired
	private JuvenileQueryRequestProcessor juvenilePlacementHistoryRequestProcessor;

	@Autowired
	private JuvenileQueryRequestProcessor juvenileReferralHistoryRequestProcessor;

	@Autowired
	private JuvenileQueryRequestProcessor juvenileHearingHistoryRequestProcessor;
	
	@Autowired
	private JuvenileQueryRequestProcessor juvenileIntakeHistoryRequestProcessor;

    @Autowired
    private ModelCamelContext camelContext;

    @BeforeEach
    public void setUp() throws Exception {
		camelContext.start();
		
		camelContext.getRouteController().startRoute("personMergeSearchResultsHandlerRoute");
		camelContext.getRouteController().startRoute("incidentSearchResultsHandlerRoute");
		camelContext.getRouteController().startRoute("vehicleSearchResultsHandlerRoute");
		camelContext.getRouteController().startRoute("firearmSearchResultsHandlerRoute");
		camelContext.getRouteController().startRoute("personVehicleToIncidentSearchResultsHandlerRoute");
		camelContext.getRouteController().startRoute("personQueryResultsHandlerWarrantsServiceRoute");
		camelContext.getRouteController().startRoute("personQueryResultsHandlerCriminalHistoryServiceRoute");
		camelContext.getRouteController().startRoute("incidentReportResultsHandlerServiceRoute");
		camelContext.getRouteController().startRoute("firearmRegistrationQueryResultsHandlerRoute");
		camelContext.getRouteController().startRoute("subscriptionSearchResultsHandlerRoute");
		camelContext.getRouteController().startRoute("subscriptionQueryResultsHandlerRoute");
		camelContext.getRouteController().startRoute("identityBasedAccessControlResultsHandlerRoute");
		camelContext.getRouteController().startRoute("policyAcknowledgementRecordingResultsHandlerRoute");
		camelContext.getRouteController().startRoute("juvenileCasePlanHistoryResultsHandlerRoute");
		camelContext.getRouteController().startRoute("juvenileHearingHistoryResultsHandlerRoute");
		camelContext.getRouteController().startRoute("juvenileOffenseHistoryResultsHandlerRoute");
		camelContext.getRouteController().startRoute("juvenileIntakeHistoryResultsHandlerRoute");
		camelContext.getRouteController().startRoute("juvenileReferralHistoryResultsHandlerRoute");
		camelContext.getRouteController().startRoute("juvenilePlacementHistoryResultsHandlerRoute");
    }
    
	@Test
	public void testJuvenileQueryServices() throws Exception
	{
		
		//Case Plan
		Assert.assertNotNull(juvenileCasePlanHistoryRequestProcessor);
		
		DetailsRequest jqr = new DetailsRequest();
		
		jqr.setIdentificationID("sample-1147519288796085535.xml");
		jqr.setIdentificationSourceText("{http://ojbc.org/Services/WSDL/JuvenileHistoryRequest/1.0}Person-Query-Service-JuvenileHistory");
    	
		//Add SAML token to request call
		Element samlToken = SAMLTokenUtils.createStaticAssertionAsElement("http://ojbc.org/ADS/AssertionDelegationService", SignatureConstants.ALGO_ID_C14N_EXCL_OMIT_COMMENTS, SignatureConstants.ALGO_ID_SIGNATURE_RSA_SHA1, true, true, null);
		
		String federatedQueryID = returnFederatedQueryGUID();
	
		
		String response = juvenileCasePlanHistoryRequestProcessor.invokeRequest(jqr, federatedQueryID, samlToken);

		Assert.assertTrue(StringUtils.contains(response, "FOUND"));
		
		//Intake
		Assert.assertNotNull(juvenileIntakeHistoryRequestProcessor);
		
		jqr = new DetailsRequest();
		
		jqr.setIdentificationID("sample-1147519288796085535.xml");
		jqr.setIdentificationSourceText("{http://ojbc.org/Services/WSDL/JuvenileHistoryRequest/1.0}Person-Query-Service-JuvenileHistory");
		
		federatedQueryID = returnFederatedQueryGUID();
		
		response = juvenileIntakeHistoryRequestProcessor.invokeRequest(jqr, federatedQueryID, samlToken);
		Assert.assertTrue(StringUtils.contains(response, "FOUND"));

		//Placement
		Assert.assertNotNull(juvenilePlacementHistoryRequestProcessor);
		
		jqr = new DetailsRequest();
		
		jqr.setIdentificationID("sample-1147519288796085535.xml");
		jqr.setIdentificationSourceText("{http://ojbc.org/Services/WSDL/JuvenileHistoryRequest/1.0}Person-Query-Service-JuvenileHistory");
		
		federatedQueryID = returnFederatedQueryGUID();
		
		response = juvenilePlacementHistoryRequestProcessor.invokeRequest(jqr, federatedQueryID, samlToken);
		Assert.assertTrue(StringUtils.contains(response, "FOUND"));

		//Referral
		Assert.assertNotNull(juvenileReferralHistoryRequestProcessor);
		
		jqr = new DetailsRequest();
		
		jqr.setIdentificationID("sample-1147519288796085535.xml");
		jqr.setIdentificationSourceText("{http://ojbc.org/Services/WSDL/JuvenileHistoryRequest/1.0}Person-Query-Service-JuvenileHistory");
		
		federatedQueryID = returnFederatedQueryGUID();
		
		response = juvenileReferralHistoryRequestProcessor.invokeRequest(jqr, federatedQueryID, samlToken);
		Assert.assertTrue(StringUtils.contains(response, "FOUND"));

		//Hearing
		Assert.assertNotNull(juvenileHearingHistoryRequestProcessor);
		
		jqr = new DetailsRequest();
		
		jqr.setIdentificationID("sample-1147519288796085535.xml");
		jqr.setIdentificationSourceText("{http://ojbc.org/Services/WSDL/JuvenileHistoryRequest/1.0}Person-Query-Service-JuvenileHistory");
		
		federatedQueryID = returnFederatedQueryGUID();
		
		response = juvenileHearingHistoryRequestProcessor.invokeRequest(jqr, federatedQueryID, samlToken);
		Assert.assertTrue(StringUtils.contains(response, "sample-1147519288796085535.xml"));

		
		//Offense
		Assert.assertNotNull(juvenileOffenseHistoryRequestProcessor);
		
		jqr = new DetailsRequest();
		
		jqr.setIdentificationID("sample-1147519288796085535.xml");
		jqr.setIdentificationSourceText("{http://ojbc.org/Services/WSDL/JuvenileHistoryRequest/1.0}Person-Query-Service-JuvenileHistory");
		
		federatedQueryID = returnFederatedQueryGUID();
		
		response = juvenileOffenseHistoryRequestProcessor.invokeRequest(jqr, federatedQueryID, samlToken);
		Assert.assertTrue(StringUtils.contains(response, "FOUND"));
	}

    @Test
    public void testCriminalHistoryQuery() throws Exception {
    	
    	//create and populate a criminal history POJO
		DetailsRequest chr = new DetailsRequest();
		
		chr.setIdentificationID("12345");
		chr.setIdentificationSourceText("{http://ojbc.org/Services/WSDL/Person_Query_Service-Criminal_History/1.0}Person-Query-Service---Criminal-History");
    	
		//Add SAML token to request call
		Element samlToken = SAMLTokenUtils.createStaticAssertionAsElement("http://ojbc.org/ADS/AssertionDelegationService", SignatureConstants.ALGO_ID_C14N_EXCL_OMIT_COMMENTS, SignatureConstants.ALGO_ID_SIGNATURE_RSA_SHA1, true, true, null);
		
		String federatedQueryID = returnFederatedQueryGUID();
		
		//Invoke the service
		String response = detailQueryDispatcher.invokeRequest(chr, federatedQueryID, samlToken);
    	
    	log.info("Body recieved by Service (truncated): " + StringUtils.abbreviate(response, 503));
    	
    	Assert.assertNotNull(response);
    }	

    @Test
    public void testWarrantsQuery() throws Exception {
    	
    	
    	//create and populate a warrants POJO
    	DetailsRequest warrantsRequest = new DetailsRequest();
		
		warrantsRequest.setIdentificationID("12345");
		warrantsRequest.setIdentificationSourceText("{http://ojbc.org/Services/WSDL/Person_Query_Service-Warrants/1.0}Person-Query-Service---Warrants");
    	
		//Add SAML token to request call
		Element samlToken = SAMLTokenUtils.createStaticAssertionAsElement("http://ojbc.org/ADS/AssertionDelegationService", SignatureConstants.ALGO_ID_C14N_EXCL_OMIT_COMMENTS, SignatureConstants.ALGO_ID_SIGNATURE_RSA_SHA1, true, true, null);

		String federatedQueryID = returnFederatedQueryGUID();
		
    	String response = detailQueryDispatcher.invokeRequest(warrantsRequest, federatedQueryID, samlToken);
    	
    	log.info("Body recieved by Service (truncated): " + StringUtils.abbreviate(response, 503));
    	
    	Assert.assertNotNull(response);
    	
    }	

    @Test
    public void testPersonSearchRequest() throws Exception {
    	
    	
    	//create and populate a person search request POJO
		PersonSearchRequest personSearchRequest = PersonSearchRequestTestUtils.createPersonSearchRequestModel();
    	
		//Add SAML token to request call
		Element samlToken = SAMLTokenUtils.createStaticAssertionAsElement("http://ojbc.org/ADS/AssertionDelegationService", SignatureConstants.ALGO_ID_C14N_EXCL_OMIT_COMMENTS, SignatureConstants.ALGO_ID_SIGNATURE_RSA_SHA1, true, true, null);

		String federatedQueryID = returnFederatedQueryGUID();
		
		//Invoke the service
		String response = personSearchRequestProcessor.invokePersonSearchRequest(personSearchRequest, federatedQueryID, samlToken);
    			
    	log.info("Body recieved by Service (truncated): " + StringUtils.abbreviate(response, 503));
    	
    	Assert.assertNotNull(response);
    	
    }	

    @Test
    public void testIncidentReportRequest() throws Exception {
    	
    	
    	//create and populate a person search request POJO
		DetailsRequest incidentReportRequest = new DetailsRequest();
		
		incidentReportRequest.setIdentificationID("{Citation}");
		incidentReportRequest.setIdentificationSourceText("{http://ojbc.org/Services/WSDL/IncidentReportRequestService/1.0}SubmitIncidentIdentiferIncidentReportRequest-DPS");
    	
		String federatedQueryID = returnFederatedQueryGUID();
		
		//Invoke the service
		String response = detailQueryDispatcher.invokeRequest(incidentReportRequest, federatedQueryID, null);
    			
    	log.info("Body recieved by Service (truncated): " + StringUtils.abbreviate(response, 503));
    	
    	Assert.assertNotNull(response);
    	
    }	
    
    @Test
    public void testIncidentSearchRequest() throws Exception {
    	
    	
    	//create and populate a person search request POJO
		DetailsRequest incidentReportRequest = new DetailsRequest();
		
		incidentReportRequest.setIdentificationID("12345");
		incidentReportRequest.setIdentificationSourceText("{http://ojbc.org/Services/WSDL/IncidentSearchRequestService/1.0}SubmitIncidentPersonSearchRequest-DPS");
    	
		String federatedQueryID = returnFederatedQueryGUID();
		
		//Invoke the service
		String response = detailQueryDispatcher.invokeRequest(incidentReportRequest, federatedQueryID, null);
    			
    	log.info("Body recieved by Service (truncated): " + StringUtils.abbreviate(response, 503));
    	
    	Assert.assertNotNull(response);
    	
    }	    

    @Test
    public void testSubscriptionSearchRequest() throws Exception {
    	
		//Add SAML token to request call
		Element samlToken = SAMLTokenUtils.createStaticAssertionAsElement("http://ojbc.org/ADS/AssertionDelegationService", SignatureConstants.ALGO_ID_C14N_EXCL_OMIT_COMMENTS, SignatureConstants.ALGO_ID_SIGNATURE_RSA_SHA1, true, true, null);

		//Invoke the service
		String response = subscriptionSearchRequestProcessor.invokeSubscriptionSearchRequest(new SubscriptionSearchRequest(), samlToken);
    			
    	log.info("Body recieved by Service (truncated): " + StringUtils.abbreviate(response, 503));
    	
    	Assert.assertNotNull(response);
    	
    }	

    @Test
    public void testSubscriptionQueryRequest() throws Exception {
    	
		//Add SAML token to request call
		Element samlToken = SAMLTokenUtils.createStaticAssertionAsElement("http://ojbc.org/ADS/AssertionDelegationService", SignatureConstants.ALGO_ID_C14N_EXCL_OMIT_COMMENTS, SignatureConstants.ALGO_ID_SIGNATURE_RSA_SHA1, true, true, null);

		String federatedQueryID = returnFederatedQueryGUID();
		
    	//create and populate a person search request POJO
		DetailsRequest subscriptionQueryRequest = new DetailsRequest();
		
		subscriptionQueryRequest.setIdentificationID("62720");
		subscriptionQueryRequest.setIdentificationSourceText("subscriptions");

		
		//Invoke the service
		String response = subscriptionQueryRequestProcessor.invokeRequest(subscriptionQueryRequest, federatedQueryID, samlToken);
    			
    	log.info("Body recieved by Service (truncated): " + StringUtils.abbreviate(response, 503));
    	
    	Assert.assertNotNull(response);
    	
    }	

    @Test
    public void testSubscriptionUnsubscribe() throws Exception {
    	
		//Add SAML token to request call
		Element samlToken = SAMLTokenUtils.createStaticAssertionAsElement("http://ojbc.org/ADS/AssertionDelegationService", SignatureConstants.ALGO_ID_C14N_EXCL_OMIT_COMMENTS, SignatureConstants.ALGO_ID_SIGNATURE_RSA_SHA1, true, true, null);

		String federatedQueryID = returnFederatedQueryGUID();
		
		Unsubscription unsubscription = new Unsubscription("123466", "topic", "reasonCode", null, null, null, null);
		
		//Invoke the service
		unsubscriptionRequestProcessor.unsubscribe(unsubscription, federatedQueryID, samlToken);
    			
    }	

    @Test
    public void testSubscriptionSubscribe() throws Exception {
    	
		//Add SAML token to request call
		Element samlToken = SAMLTokenUtils.createStaticAssertionAsElement("http://ojbc.org/ADS/AssertionDelegationService", SignatureConstants.ALGO_ID_C14N_EXCL_OMIT_COMMENTS, SignatureConstants.ALGO_ID_SIGNATURE_RSA_SHA1, true, true, null);

		String federatedQueryID = returnFederatedQueryGUID();
		
		Subscription subscription = new Subscription();
		
		//Invoke the service
		subscriptionRequestProcessor.subscribe(subscription, federatedQueryID, samlToken);    			
    }	

    @Test
    public void testIdentityBasedAccessControl() throws Exception {

        //Add SAML token to request call
        Element samlToken = SAMLTokenUtils.createStaticAssertionAsElement("http://ojbc.org/ADS/AssertionDelegationService", 
                SignatureConstants.ALGO_ID_C14N_EXCL_OMIT_COMMENTS, SignatureConstants.ALGO_ID_SIGNATURE_RSA_SHA1, true, true, null);

        String federatedQueryID = returnFederatedQueryGUID();
        
        //Invoke the service
        String response = identityBasedAccessControlRequestProcessor.invokeAccessControlRequest(federatedQueryID, samlToken,policyAccessControlResourceURI);
                
        log.info("Body recieved by Service (truncated): " + StringUtils.abbreviate(response, 503));
        
        Assert.assertNotNull(response);
    }
    
    @Test
    public void testPolicyAcknowledgementRecordingService() throws Exception {
        Map<SamlAttribute, String> customAttributes = new HashMap<SamlAttribute, String>();
        customAttributes.put(SamlAttribute.FederationId, "");
        
        //Add SAML token to request call
        Element samlToken = SAMLTokenUtils.createStaticAssertionAsElement("http://ojbc.org/ADS/AssertionDelegationService", 
                SignatureConstants.ALGO_ID_C14N_EXCL_OMIT_COMMENTS, SignatureConstants.ALGO_ID_SIGNATURE_RSA_SHA1, true, true, customAttributes);
        
        String federatedQueryID = returnFederatedQueryGUID();
        
        //Invoke the service
        //Invoke the service
        String response = policyAcknowledgingRequestProcessor.invokePolicyAcknowledgementRecordingRequest(federatedQueryID, samlToken);
        
        log.info("Body recieved by Service (truncated): " + StringUtils.abbreviate(response, 503));
        
        Assert.assertNotNull(response);
    }
    
    private String returnFederatedQueryGUID()
    {
    	return java.util.UUID.randomUUID().toString();
    }
    
}
