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
package org.ojbc.web.portal.controllers.config;

import javax.annotation.Resource;

import org.ojbc.web.DetailsQueryInterface;
import org.ojbc.web.VehicleSearchInterface;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

@Configuration
@Profile({"vehicle-search"})
public class VehiclesControllerConfigBrokered implements
		VehiclesControllerConfigInterface {
	
    /*
     * If vehicleSearchRequestProcessorBean is defined in cfg file, use the property value, 
     * Otherwise, use the default bean "vehicleSearchRequestProcessor"
     */
	@Resource (name="${vehicleSearchRequestProcessorBean:vehicleSearchRequestProcessor}")
	VehicleSearchInterface vehicleSearchInterface;

	@Resource (name="detailQueryDispatcher")
	DetailsQueryInterface detailsQueryInterface;

	@Override
	public VehicleSearchInterface getVehicleSearchBean() {
		return vehicleSearchInterface;
	}

	@Override
	public DetailsQueryInterface getDetailsQueryBean() {
		return detailsQueryInterface;
	}

}
