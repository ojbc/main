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
package org.ojbc.web.impl;

import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.ojbc.processor.person.query.CourtCaseQueryRequestProcessor;
import org.ojbc.processor.person.query.CriminalHistoryRequestProcessor;
import org.ojbc.processor.person.query.CustodyQueryRequestProcessor;
import org.ojbc.processor.person.query.FirearmRegistrationQueryRequestProcessor;
import org.ojbc.processor.person.query.FirearmsPurchaseProhibitionRequestProcessor;
import org.ojbc.processor.person.query.IncidentReportRequestProcessor;
import org.ojbc.processor.person.query.JuvenileQueryRequestProcessor;
import org.ojbc.processor.person.query.PersonToCourtCaseSearchRequestProcessor;
import org.ojbc.processor.person.query.PersonToCustodySearchRequestProcessor;
import org.ojbc.processor.person.query.PersonVehicleToIncidentSearchRequestProcessor;
import org.ojbc.processor.person.query.VehicleCrashQueryRequestProcessor;
import org.ojbc.processor.person.query.WarrantsRequestProcessor;
import org.ojbc.web.DetailsQueryInterface;
import org.ojbc.web.OJBCWebServiceURIs;
import org.ojbc.web.model.person.query.DetailsRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.w3c.dom.Element;

/**
 * The Detail Query Dispatcher will dispatch query requests to 
 * the specific implementations of those services.  We set all
 * detail query beans to required = false in case the implementer
 * has decided not to implement that query in their configuration.
 * 
 * This allows the Connector to start up without throwing dependency
 * exceptions if a profile is not specified.
 * 
 *
 */

public class DetailQueryDispatcher implements DetailsQueryInterface{

	private static final Log log = LogFactory.getLog( DetailQueryDispatcher.class );
	
	@Autowired(required=false)
	private WarrantsRequestProcessor warrantsRequestProcessor;

	@Autowired(required=false)
	private CriminalHistoryRequestProcessor criminalHistoryRequestProcessor;
	
	@Autowired(required=false)
	private IncidentReportRequestProcessor incidentReportRequestProcessor;

	@Autowired(required=false)
	private PersonVehicleToIncidentSearchRequestProcessor personVehicleToIncidentSearchRequestProcessor;
	
	@Autowired(required=false)
	private PersonToCourtCaseSearchRequestProcessor personToCourtCaseSearchRequestProcessor;
	
	@Autowired(required=false)
	private CourtCaseQueryRequestProcessor courtCaseQueryRequestProcessor;
	
	@Autowired(required=false)
	private VehicleCrashQueryRequestProcessor vehicleCrashQueryRequestProcessor;

	@Autowired(required=false)
	private FirearmsPurchaseProhibitionRequestProcessor firearmsPurchaseProhibitionRequestProcessor;

	@Autowired(required=false)
	private PersonToCustodySearchRequestProcessor personToCustodySearchRequestProcessor;
	
	@Autowired(required=false)
	private CustodyQueryRequestProcessor custodyQueryRequestProcessor;
	
	@Autowired(required=false)
	private FirearmRegistrationQueryRequestProcessor firearmRegistrationQueryRequestProcessor;
	
	@Autowired(required=false)
	@Qualifier("juvenileCasePlanHistoryRequestProcessor")
	private JuvenileQueryRequestProcessor juvenileCasePlanHistoryRequestProcessor;

	@Autowired(required=false)
	@Qualifier("juvenileOffenseHistoryRequestProcessor")
	private JuvenileQueryRequestProcessor juvenileOffenseHistoryRequestProcessor;

	@Autowired(required=false)
	@Qualifier("juvenilePlacementHistoryRequestProcessor")
	private JuvenileQueryRequestProcessor juvenilePlacementHistoryRequestProcessor;

	@Autowired(required=false)
	@Qualifier("juvenileReferralHistoryRequestProcessor")
	private JuvenileQueryRequestProcessor juvenileReferralHistoryRequestProcessor;

	@Autowired(required=false)
	@Qualifier("juvenileHearingHistoryRequestProcessor")
	private JuvenileQueryRequestProcessor juvenileHearingHistoryRequestProcessor;
	
	@Autowired(required=false)
	@Qualifier("juvenileIntakeHistoryRequestProcessor")
	private JuvenileQueryRequestProcessor juvenileIntakeHistoryRequestProcessor;
	
	@Resource(name = "searchURIToQueryURIMap")
	private Map<String, String> searchURIToQueryURIMap;	
	
	public String invokeRequest(DetailsRequest request, String federatedQueryID, Element samlToken) throws Exception {

		log.debug("Invoking detail request in Conenctor");
		
		if (StringUtils.isEmpty(federatedQueryID)) {
			throw new IllegalStateException("Federated Query ID not set");
		}

		String requestIdSrcTxt = request.getIdentificationSourceText().trim();

		log.info("Identification Source text in request: " + StringUtils.trimToEmpty(requestIdSrcTxt));
		log.info("Identification ID in request: " + StringUtils.trimToEmpty(request.getIdentificationID()));
		
		//Check the map to see if there is a mapping of search URI to query URI
		if (searchURIToQueryURIMap != null)
		{
			if (searchURIToQueryURIMap.containsKey(requestIdSrcTxt))
			{
				request.setIdentificationSourceText(searchURIToQueryURIMap.get(requestIdSrcTxt));
				requestIdSrcTxt = searchURIToQueryURIMap.get(requestIdSrcTxt);
				log.info("Translating Identification Source text to: " + requestIdSrcTxt);
			}	
		}	

		
		if (OJBCWebServiceURIs.CRIMINAL_HISTORY.equals(requestIdSrcTxt)) {
			return criminalHistoryRequestProcessor.invokeRequest(request, federatedQueryID, samlToken);
		
		} else if (OJBCWebServiceURIs.WARRANTS.equals(requestIdSrcTxt)) {
			return warrantsRequestProcessor.invokeRequest(request, federatedQueryID, samlToken);
		
		} else if (requestIdSrcTxt.contains(OJBCWebServiceURIs.INCIDENT_REPORT)) {
			return incidentReportRequestProcessor.invokeRequest(request, federatedQueryID, samlToken);
		
		} else if (requestIdSrcTxt.contains(OJBCWebServiceURIs.PERSON_TO_INCIDENT)) {
			return personVehicleToIncidentSearchRequestProcessor.invokeRequest(request, federatedQueryID, samlToken);
		
		} else if (requestIdSrcTxt.contains(OJBCWebServiceURIs.VEHICLE_TO_INCIDENT)) {
			return personVehicleToIncidentSearchRequestProcessor.invokeRequest(request, federatedQueryID, samlToken);
		
		} else if (OJBCWebServiceURIs.FIREARMS.equals(requestIdSrcTxt)) {
			return firearmRegistrationQueryRequestProcessor.invokeRequest(request, federatedQueryID, samlToken);
			
		} else if(requestIdSrcTxt.contains(OJBCWebServiceURIs.FIREARMS_QUERY_REQUEST_BY_FIREARM)){
			return firearmRegistrationQueryRequestProcessor.invokeRequest(request, federatedQueryID, samlToken);
			
		} else if(requestIdSrcTxt.contains(OJBCWebServiceURIs.FIREARMS_QUERY_REQUEST_BY_PERSON)){
			
			return firearmRegistrationQueryRequestProcessor.invokeRequest(request, federatedQueryID, samlToken);
			
		} else if (OJBCWebServiceURIs.COURT_CASE.equals(requestIdSrcTxt)){
			
			return personToCourtCaseSearchRequestProcessor.invokeRequest(request, federatedQueryID, samlToken);
			
		} else if (OJBCWebServiceURIs.COURT_CASE_DETAIL.equals(requestIdSrcTxt)){
			
			return courtCaseQueryRequestProcessor.invokeRequest(request, federatedQueryID, samlToken);
			
		} else if (OJBCWebServiceURIs.JAIL.equals(requestIdSrcTxt)){
			
			return personToCustodySearchRequestProcessor.invokeRequest(request, federatedQueryID, samlToken);
			
		} else if (OJBCWebServiceURIs.JAIL_DETAIL.equals(requestIdSrcTxt)){
			
			return custodyQueryRequestProcessor.invokeRequest(request, federatedQueryID, samlToken);
			
		} else if (OJBCWebServiceURIs.VEHICLE_CRASH.equals(requestIdSrcTxt)){
			
			return vehicleCrashQueryRequestProcessor.invokeRequest(request, federatedQueryID, samlToken);
			
		} else if (OJBCWebServiceURIs.FIREARMS_PURCHASE_PROHIBITION.equals(requestIdSrcTxt)) {
			
			return firearmsPurchaseProhibitionRequestProcessor.invokeRequest(request, federatedQueryID, samlToken);
			
		} else if (requestIdSrcTxt.contains(OJBCWebServiceURIs.JUVENILE_HISTORY)) {
			
			log.info("Juvenile request query type: " + request.getQueryType());
			
			if (request.getQueryType() == null){
				throw new RuntimeException("Query type required for Juvenile queries");
			}	
			else if (request.getQueryType().equalsIgnoreCase("CasePlan")){
				return juvenileCasePlanHistoryRequestProcessor.invokeRequest(request, federatedQueryID, samlToken);
			}
			else if (request.getQueryType().equalsIgnoreCase("Hearing")){
				return juvenileHearingHistoryRequestProcessor.invokeRequest(request, federatedQueryID, samlToken);
			}  
			else if (request.getQueryType().equalsIgnoreCase("Intake")){
				return juvenileIntakeHistoryRequestProcessor.invokeRequest(request, federatedQueryID, samlToken);
			}
			else if (request.getQueryType().equalsIgnoreCase("Person")||request.getQueryType().equalsIgnoreCase("Offense") ){
				return juvenileOffenseHistoryRequestProcessor.invokeRequest(request, federatedQueryID, samlToken);
			}  
			else if (request.getQueryType().equalsIgnoreCase("Placement")){
				return juvenilePlacementHistoryRequestProcessor.invokeRequest(request, federatedQueryID, samlToken);
			}  
			else if (request.getQueryType().equalsIgnoreCase("Referral")){
				return juvenileReferralHistoryRequestProcessor.invokeRequest(request, federatedQueryID, samlToken);
			}	
		}		

		throw new RuntimeException("Unknown source: " + requestIdSrcTxt);

	}

	public WarrantsRequestProcessor getWarrantsRequestProcessor() {
		return warrantsRequestProcessor;
	}

	public void setWarrantsRequestProcessor(
			WarrantsRequestProcessor warrantsRequestProcessor) {
		this.warrantsRequestProcessor = warrantsRequestProcessor;
	}

	public CriminalHistoryRequestProcessor getCriminalHistoryRequestProcessor() {
		return criminalHistoryRequestProcessor;
	}

	public void setCriminalHistoryRequestProcessor(
			CriminalHistoryRequestProcessor criminalHistoryRequestProcessor) {
		this.criminalHistoryRequestProcessor = criminalHistoryRequestProcessor;
	}

	public IncidentReportRequestProcessor getIncidentReportRequestProcessor() {
		return incidentReportRequestProcessor;
	}

	public void setIncidentReportRequestProcessor(
			IncidentReportRequestProcessor incidentReportRequestProcessor) {
		this.incidentReportRequestProcessor = incidentReportRequestProcessor;
	}

	public PersonVehicleToIncidentSearchRequestProcessor getPersonVehicleToIncidentSearchRequestProcessor() {
		return personVehicleToIncidentSearchRequestProcessor;
	}

	public void setPersonVehicleToIncidentSearchRequestProcessor(
			PersonVehicleToIncidentSearchRequestProcessor personVehicleToIncidentSearchRequestProcessor) {
		this.personVehicleToIncidentSearchRequestProcessor = personVehicleToIncidentSearchRequestProcessor;
	}

	public FirearmRegistrationQueryRequestProcessor getFirearmRegistrationQueryRequestProcessor() {
		return firearmRegistrationQueryRequestProcessor;
	}

	public void setFirearmRegistrationQueryRequestProcessor(
			FirearmRegistrationQueryRequestProcessor firearmRegistrationQueryRequestProcessor) {
		this.firearmRegistrationQueryRequestProcessor = firearmRegistrationQueryRequestProcessor;
	}

	public JuvenileQueryRequestProcessor getJuvenileCasePlanHistoryRequestProcessor() {
		return juvenileCasePlanHistoryRequestProcessor;
	}

	public void setJuvenileCasePlanHistoryRequestProcessor(
			JuvenileQueryRequestProcessor juvenileCasePlanHistoryRequestProcessor) {
		this.juvenileCasePlanHistoryRequestProcessor = juvenileCasePlanHistoryRequestProcessor;
	}

	public JuvenileQueryRequestProcessor getJuvenileOffenseHistoryRequestProcessor() {
		return juvenileOffenseHistoryRequestProcessor;
	}

	public void setJuvenileOffenseHistoryRequestProcessor(
			JuvenileQueryRequestProcessor juvenileOffenseHistoryRequestProcessor) {
		this.juvenileOffenseHistoryRequestProcessor = juvenileOffenseHistoryRequestProcessor;
	}

	public JuvenileQueryRequestProcessor getJuvenilePlacementHistoryRequestProcessor() {
		return juvenilePlacementHistoryRequestProcessor;
	}

	public void setJuvenilePlacementHistoryRequestProcessor(
			JuvenileQueryRequestProcessor juvenilePlacementHistoryRequestProcessor) {
		this.juvenilePlacementHistoryRequestProcessor = juvenilePlacementHistoryRequestProcessor;
	}

	public JuvenileQueryRequestProcessor getJuvenileReferralHistoryRequestProcessor() {
		return juvenileReferralHistoryRequestProcessor;
	}

	public void setJuvenileReferralHistoryRequestProcessor(
			JuvenileQueryRequestProcessor juvenileReferralHistoryRequestProcessor) {
		this.juvenileReferralHistoryRequestProcessor = juvenileReferralHistoryRequestProcessor;
	}

	public JuvenileQueryRequestProcessor getJuvenileHearingHistoryRequestProcessor() {
		return juvenileHearingHistoryRequestProcessor;
	}

	public void setJuvenileHearingHistoryRequestProcessor(
			JuvenileQueryRequestProcessor juvenileHearingHistoryRequestProcessor) {
		this.juvenileHearingHistoryRequestProcessor = juvenileHearingHistoryRequestProcessor;
	}

	public JuvenileQueryRequestProcessor getJuvenileIntakeHistoryRequestProcessor() {
		return juvenileIntakeHistoryRequestProcessor;
	}

	public void setJuvenileIntakeHistoryRequestProcessor(
			JuvenileQueryRequestProcessor juvenileIntakeHistoryRequestProcessor) {
		this.juvenileIntakeHistoryRequestProcessor = juvenileIntakeHistoryRequestProcessor;
	}

	public Map<String, String> getSearchURIToQueryURIMap() {
		return searchURIToQueryURIMap;
	}

	public void setSearchURIToQueryURIMap(Map<String, String> searchURIToQueryURIMap) {
		this.searchURIToQueryURIMap = searchURIToQueryURIMap;
	}

}
