package org.ojbc.web.impl;

import org.apache.commons.lang.StringUtils;
import org.ojbc.web.DetailsQueryInterface;
import org.ojbc.web.OJBCWebServiceURIs;
import org.ojbc.web.WebUtils;
import org.ojbc.web.model.person.query.DetailsRequest;
import org.springframework.stereotype.Service;
import org.w3c.dom.Element;

@Service
public class DetailsQueryInterfaceMockImpl implements DetailsQueryInterface {
	
	@Override
	public String invokeRequest(DetailsRequest request, String federatedQueryID, Element samlToken) throws Exception {

		if (StringUtils.isEmpty(federatedQueryID)) {
			throw new IllegalStateException("Federated Query ID not set");
		}

		if (OJBCWebServiceURIs.CRIMINAL_HISTORY.equals(request.getIdentificationSourceText().trim())) {
			return WebUtils.returnStringFromFilePath(getClass().getResourceAsStream(
					"/sampleResponses/criminalHistory/DetailedCJISResponse.out.xml"));
		} else if (OJBCWebServiceURIs.WARRANTS.equals(request.getIdentificationSourceText().trim())) {
			return WebUtils.returnStringFromFilePath(getClass().getResourceAsStream(
			        "/sampleResponses/warrants/Person_Query_Results_-_Warrants.xml"));
		} else if (OJBCWebServiceURIs.FIREARMS.equals(request.getIdentificationSourceText().trim())) {
			return WebUtils.returnStringFromFilePath(getClass().getResourceAsStream(
			        "/sampleResponses/firearms/FirearmRegistrationQueryResults-multiple.xml"));
		} else if (request.getIdentificationSourceText().trim().contains(OJBCWebServiceURIs.INCIDENT_REPORT)) {
			return WebUtils.returnStringFromFilePath(getClass().getResourceAsStream(
	        		"/sampleResponses/incidentReport/Incident_Sample.xml"));
		} else if (request.getIdentificationSourceText().trim().contains(OJBCWebServiceURIs.PERSON_TO_INCIDENT)) {
			return WebUtils.returnStringFromFilePath(getClass().getResourceAsStream(
	        		"/sampleResponses/personToIncident/Incident_Person_Search_Results.xml"));
		} else if (request.getIdentificationSourceText().trim().contains(OJBCWebServiceURIs.VEHICLE_TO_INCIDENT)) {
			return WebUtils.returnStringFromFilePath(getClass().getResourceAsStream(
	        		"/sampleResponses/vehicleToIncident/Incident_Vehicle_Search_Results.xml"));
		}else if(OJBCWebServiceURIs.JUVENILE_HISTORY.equals(request.getIdentificationSourceText().trim())) {
	          if (request.getQueryType() == null){
	                throw new RuntimeException("Query type required for Juvenile queries");
	            }
	            else if (request.getQueryType().equalsIgnoreCase("CasePlan")){
	                return WebUtils.returnStringFromFilePath(getClass().getResourceAsStream(
	                        "/service-specifications/Juvenile_History_Services/artifacts/service_model/information_model/samples/JuvenileCasePlanHistoryResponse_Sample.xml"));
	            }
	            else if (request.getQueryType().equalsIgnoreCase("Hearing")){
                    return WebUtils.returnStringFromFilePath(getClass().getResourceAsStream(
                            "/service-specifications/Juvenile_History_Services/artifacts/service_model/information_model/samples/JuvenileHearingHistoryResponse_Sample.xml"));
	            }  
	            else if (request.getQueryType().equalsIgnoreCase("Intake")){
                    return WebUtils.returnStringFromFilePath(getClass().getResourceAsStream(
                            "/service-specifications/Juvenile_History_Services/artifacts/service_model/information_model/samples/JuvenileIntakeHistoryResponse_Sample.xml"));
	            }
	            else if (request.getQueryType().equalsIgnoreCase("Offense")){
                    return WebUtils.returnStringFromFilePath(getClass().getResourceAsStream(
                            "/service-specifications/Juvenile_History_Services/artifacts/service_model/information_model/samples/JuvenileOffenseHistoryResponse_Sample.xml"));
	            }
	            else if (request.getQueryType().equalsIgnoreCase("Placement")){
                    return WebUtils.returnStringFromFilePath(getClass().getResourceAsStream(
                            "/service-specifications/Juvenile_History_Services/artifacts/service_model/information_model/samples/JuvenilePlacementHistoryResponse_Sample.xml"));
	            }
	            else if (request.getQueryType().equalsIgnoreCase("Referral")){
                    return WebUtils.returnStringFromFilePath(getClass().getResourceAsStream(
                            "/service-specifications/Juvenile_History_Services/artifacts/service_model/information_model/samples/JuvenileReferralHistoryResponse_Sample.xml"));
	            }   
		}
		
		throw new RuntimeException("Unknown source: " + request.getIdentificationSourceText());

	}
}

