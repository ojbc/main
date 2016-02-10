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
 * Copyright 2012-2015 Open Justice Broker Consortium
 */
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
	                        "/ssp/Juvenile_History_Services/artifacts/service_model/information_model/samples/JuvenileCasePlanHistoryResponse_Sample.xml"));
	            }
	            else if (request.getQueryType().equalsIgnoreCase("Hearing")){
                    return WebUtils.returnStringFromFilePath(getClass().getResourceAsStream(
                            "/ssp/Juvenile_History_Services/artifacts/service_model/information_model/samples/JuvenileHearingHistoryResponse_Sample.xml"));
	            }  
	            else if (request.getQueryType().equalsIgnoreCase("Intake")){
                    return WebUtils.returnStringFromFilePath(getClass().getResourceAsStream(
                            "/ssp/Juvenile_History_Services/artifacts/service_model/information_model/samples/JuvenileIntakeHistoryResponse_Sample.xml"));
	            }
	            else if (request.getQueryType().equalsIgnoreCase("Offense")){
                    return WebUtils.returnStringFromFilePath(getClass().getResourceAsStream(
                            "/ssp/Juvenile_History_Services/artifacts/service_model/information_model/samples/JuvenileOffenseHistoryResponse_Sample.xml"));
	            }
	            else if (request.getQueryType().equalsIgnoreCase("Placement")){
                    return WebUtils.returnStringFromFilePath(getClass().getResourceAsStream(
                            "/ssp/Juvenile_History_Services/artifacts/service_model/information_model/samples/JuvenilePlacementHistoryResponse_Sample.xml"));
	            }
	            else if (request.getQueryType().equalsIgnoreCase("Referral")){
                    return WebUtils.returnStringFromFilePath(getClass().getResourceAsStream(
                            "/ssp/Juvenile_History_Services/artifacts/service_model/information_model/samples/JuvenileReferralHistoryResponse_Sample.xml"));
	            }   
		}
		
		throw new RuntimeException("Unknown source: " + request.getIdentificationSourceText());

	}
}

