package org.ojbc.web.impl;

import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.ojbc.processor.person.query.CriminalHistoryRequestProcessor;
import org.ojbc.processor.person.query.FirearmRegistrationQueryRequestProcessor;
import org.ojbc.processor.person.query.IncidentReportRequestProcessor;
import org.ojbc.processor.person.query.JuvenileQueryRequestProcessor;
import org.ojbc.processor.person.query.PersonVehicleToIncidentSearchRequestProcessor;
import org.ojbc.processor.person.query.WarrantsRequestProcessor;
import org.ojbc.web.DetailsQueryInterface;
import org.ojbc.web.OJBCWebServiceURIs;
import org.ojbc.web.model.person.query.DetailsRequest;
import org.springframework.beans.factory.annotation.Autowired;
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
	private FirearmRegistrationQueryRequestProcessor firearmRegistrationQueryRequestProcessor;
	
	@Autowired(required=false)
	private JuvenileQueryRequestProcessor juvenileCasePlanHistoryRequestProcessor;

	@Autowired(required=false)
	private JuvenileQueryRequestProcessor juvenileOffenseHistoryRequestProcessor;

	@Autowired(required=false)
	private JuvenileQueryRequestProcessor juvenilePlacementHistoryRequestProcessor;

	@Autowired(required=false)
	private JuvenileQueryRequestProcessor juvenileReferralHistoryRequestProcessor;

	@Autowired(required=false)
	private JuvenileQueryRequestProcessor juvenileHearingHistoryRequestProcessor;
	
	@Autowired(required=false)
	private JuvenileQueryRequestProcessor juvenileIntakeHistoryRequestProcessor;
	
	@Resource(name = "searchURIToQueryURIMap")
	private Map<String, String> searchURIToQueryURIMap;	
	
	public String invokeRequest(DetailsRequest request, String federatedQueryID, Element samlToken) throws Exception {

		log.debug("Invoking detail request in Conenctor");
		
		if (StringUtils.isEmpty(federatedQueryID)) {
			throw new IllegalStateException("Federated Query ID not set");
		}

		String requestIdSrcTxt = request.getIdentificationSourceText().trim();

		log.info("Identification Source text in request: " + requestIdSrcTxt);
		
		//Check the map to see if there is a mapping of search URI to query URI
		if (searchURIToQueryURIMap != null)
		{
			if (searchURIToQueryURIMap.containsKey(requestIdSrcTxt))
			{
				request.setIdentificationSourceText(searchURIToQueryURIMap.get(requestIdSrcTxt));
				requestIdSrcTxt = searchURIToQueryURIMap.get(requestIdSrcTxt);
				log.info("Translating Identification Source text to: " + searchURIToQueryURIMap.get(requestIdSrcTxt));
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
			
		} else if (OJBCWebServiceURIs.JUVENILE_HISTORY.equals(requestIdSrcTxt)) {
			
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
			else if (request.getQueryType().equalsIgnoreCase("Offense")){
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
