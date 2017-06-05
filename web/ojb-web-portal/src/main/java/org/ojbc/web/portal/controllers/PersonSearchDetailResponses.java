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
package org.ojbc.web.portal.controllers;

import java.util.HashMap;
import java.util.Map;

import org.ojbc.web.model.person.query.DetailsRequest;

public class PersonSearchDetailResponses {
		
    private String identificationID;
    private String identificationSourceText;

    private Map<String, String> responseMap = new HashMap<String, String>();

    public PersonSearchDetailResponses(DetailsRequest detailsRequest) {
        super();
        this.identificationSourceText = detailsRequest.getIdentificationSourceText();
        this.identificationID = detailsRequest.getIdentificationID(); 
    }
    
    public String getIdentificationID() {
        return identificationID;
    }

    public void setIdentificationID(String identificationID) {
        this.identificationID = identificationID;
    }

    public String getIdentificationSourceText() {
        return identificationSourceText;
    }

    public String getDetailResponse(DetailsRequest detailsRequest) {
        if (detailsRequest.getIdentificationID().equals(identificationID) ) {
            return responseMap.get(detailsRequest.getQueryType().toUpperCase()); 
        }
        
        return null;
    }
    
    public void cacheDetailResponse(DetailsRequest detailsRequest, String response) {
        if (!detailsRequest.getIdentificationID().equals(identificationID) ) {
            this.identificationID = detailsRequest.getIdentificationID(); 
            responseMap.clear();
        }
        
        responseMap.put(detailsRequest.getQueryType().toUpperCase(), response); 
        if (detailsRequest.getQueryType().equalsIgnoreCase("Person")) {
            responseMap.put("OFFENSE", response); 
            this.identificationSourceText = detailsRequest.getIdentificationSourceText(); 
        }

    }
}


