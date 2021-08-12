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
package org.ojbc.audit.enhanced.processor;

import java.util.ArrayList;
import java.util.List;

import org.apache.camel.Body;
import org.apache.camel.Exchange;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.ojbc.audit.enhanced.dao.model.VehicleSearchRequest;
import org.ojbc.util.xml.XmlUtils;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public abstract class AbstractVehicleSearchRequestProcessor {

	private static final Log log = LogFactory.getLog(AbstractVehicleSearchRequestProcessor.class);
	
	public abstract void auditVehicleSearchRequest(Exchange exchange, @Body Document document);
	
	VehicleSearchRequest processVehicleSearchRequest(Document document) throws Exception
	{
		VehicleSearchRequest vehicleSearchRequest = new VehicleSearchRequest();

        Node vehicleNode = XmlUtils.xPathNodeSearch(document, "/vsr-doc:VehicleSearchRequest/vsr:Vehicle");

        if (vehicleNode != null) {
        	
        	String vehicleColor = XmlUtils.xPathStringSearch(vehicleNode, "nc:VehicleColorPrimaryCode");
        	
        	if (StringUtils.isNotBlank(vehicleColor))
        	{
        		vehicleSearchRequest.setVehicleColor(vehicleColor);
        	}	
        	
        	String vehicleModel = XmlUtils.xPathStringSearch(vehicleNode, "nc:ItemModelName");

        	if (StringUtils.isNotBlank(vehicleModel))
        	{
        		vehicleSearchRequest.setVehicleModel(vehicleModel);
        	}	

        	String vehicleMake = XmlUtils.xPathStringSearch(vehicleNode, "vsr:VehicleMakeCode");

        	if (StringUtils.isNotBlank(vehicleMake))
        	{
        		vehicleSearchRequest.setVehicleMake(vehicleMake);
        	}	

        	String vehicleLicensePlate = XmlUtils.xPathStringSearch(vehicleNode, "nc:ConveyanceRegistrationPlateIdentification/nc:IdentificationID");

        	if (StringUtils.isNotBlank(vehicleLicensePlate))
        	{
        		vehicleSearchRequest.setVehicleLicensePlate(vehicleLicensePlate);
        	}	

        	String vehicleIdentificationNumber = XmlUtils.xPathStringSearch(vehicleNode, "nc:VehicleIdentification/nc:IdentificationID");

        	if (StringUtils.isNotBlank(vehicleIdentificationNumber))
        	{
        		vehicleSearchRequest.setVehicleIdentificationNumber(vehicleIdentificationNumber);
        	}	

        	String vehicleYearRangeStart = XmlUtils.xPathStringSearch(vehicleNode, "vsr:VehicleYearRange/nc:StartDate/nc:Year");

        	if (StringUtils.isNotBlank(vehicleYearRangeStart))
        	{
        		vehicleSearchRequest.setVehicleYearRangeStart(vehicleYearRangeStart);
        	}	

        	String vehicleYearRangeEnd = XmlUtils.xPathStringSearch(vehicleNode, "vsr:VehicleYearRange/nc:EndDate/nc:Year");

        	if (StringUtils.isNotBlank(vehicleYearRangeEnd))
        	{
        		vehicleSearchRequest.setVehicleYearRangeEnd(vehicleYearRangeEnd);
        	}	

        }
        
        NodeList sourceSystems = XmlUtils.xPathNodeListSearch(document, "/vsr-doc:VehicleSearchRequest/vsr:SourceSystemNameText");

        List<String> sourceSystemsList = new ArrayList<String>();
        
        if (sourceSystems != null) {
            int length = sourceSystems.getLength();
            for (int i = 0; i < length; i++) {
                if (sourceSystems.item(i).getNodeType() == Node.ELEMENT_NODE) {
                    Element sourceSystem = (Element) sourceSystems.item(i);
                    
                    if (StringUtils.isNotBlank(sourceSystem.getTextContent()))
                    {
                    	sourceSystemsList.add(sourceSystem.getTextContent());
                    }		
                }
            }
        }
        
        String onBehalfOfText = XmlUtils.xPathStringSearch(document, "/vsr-doc:VehicleSearchRequest/vsr:SearchMetadata/vsr:SearchRequestOnBehalfOfText");
        
        if (StringUtils.isNotEmpty(onBehalfOfText)) {
            vehicleSearchRequest.setOnBehalfOf(onBehalfOfText);
        }

        String purposeText = XmlUtils.xPathStringSearch(document, "/vsr-doc:VehicleSearchRequest/vsr:SearchMetadata/vsr:SearchPurposeText");
        
        if (StringUtils.isNotEmpty(purposeText)) {
        	vehicleSearchRequest.setPurpose(purposeText);
        }
        
        vehicleSearchRequest.setSourceSystemsList(sourceSystemsList);

        log.debug("Vehicle Search Request: " + vehicleSearchRequest.toString());

        return vehicleSearchRequest;
	}

}
