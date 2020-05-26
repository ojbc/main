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
import org.apache.camel.Header;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.ojbc.audit.enhanced.dao.model.CrashVehicle;
import org.ojbc.audit.enhanced.dao.model.VehicleCrashQueryResponse;
import org.ojbc.util.xml.XmlUtils;
import org.w3c.dom.Attr;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;

public abstract class AbstractVehicleCrashQueryResponseProcessor {

	private static final Log log = LogFactory.getLog(AbstractVehicleCrashQueryResponseProcessor.class);
	
	public abstract void auditVehicleCrashResponse(@Body Document document,  @Header(value = "federatedQueryRequestGUID")String messageID);
	
	VehicleCrashQueryResponse processVehicleCrashResponse(Document document) throws Exception
	{
		VehicleCrashQueryResponse vehicleCrashQueryResponse = new VehicleCrashQueryResponse();

		String errorText = XmlUtils.xPathStringSearch(document, "/vcq-res-doc:VehicleCrashQueryResults/qrm:QueryResultsMetadata/qrer:QueryRequestError/qrer:ErrorText");
		
		if (StringUtils.isNotBlank(errorText))
		{
			vehicleCrashQueryResponse.setQueryResultsErrorText(errorText);
			vehicleCrashQueryResponse.setQueryResultsErrorIndicator(true);
		}			

		vehicleCrashQueryResponse.setSystemName("Vehicle Crash");

		NodeList vehicleRefs = XmlUtils.xPathNodeListSearch(document, "/vcq-res-doc:VehicleCrashQueryResults/vcq-res-ext:VehicleCrashReport/jxdm51:Crash/jxdm51:CrashVehicle/nc30:RoleOfItem/@s30:ref");
		
		int length = vehicleRefs.getLength();
		
		List<CrashVehicle> crashVehicles = new ArrayList<CrashVehicle>();
		
		for( int i=0; i<length; i++) {
		    Attr attr = (Attr) vehicleRefs.item(i);
		    String vehicleRef = attr.getValue();
		    
		    CrashVehicle crashVehicle = new CrashVehicle();
		    
			String vehicleMake = XmlUtils.xPathStringSearch(document, "/vcq-res-doc:VehicleCrashQueryResults/vcq-res-ext:VehicleCrashReport/nc30:Vehicle[@s30:id='" + vehicleRef + "']/nc30:ItemMakeName");
			crashVehicle.setVehicleMake(vehicleMake);
			
			String vehicleModel = XmlUtils.xPathStringSearch(document, "/vcq-res-doc:VehicleCrashQueryResults/vcq-res-ext:VehicleCrashReport/nc30:Vehicle[@s30:id='" + vehicleRef + "']/nc30:ItemModelYearDate");
			crashVehicle.setVehicleModel(vehicleModel);
			
			String vehicleIdentificationNumber = XmlUtils.xPathStringSearch(document, "/vcq-res-doc:VehicleCrashQueryResults/vcq-res-ext:VehicleCrashReport/nc30:Vehicle[@s30:id='" + vehicleRef + "']/nc30:VehicleIdentification/nc30:IdentificationID");
			crashVehicle.setVehicleIdentificationNumber(vehicleIdentificationNumber);
			
			crashVehicles.add(crashVehicle);
		}
		
		vehicleCrashQueryResponse.setCrashVehicles(crashVehicles);
		
		log.debug("Vehicle Crash Response: " + vehicleCrashQueryResponse.toString());
		
        return vehicleCrashQueryResponse;
	}

}
