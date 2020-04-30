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
package org.ojbc.audit.enhanced.dao.model;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;

public class CrashVehicle {

		private Integer vehicleCrashQueryResultsId;
		
		private String vehicleMake;
		private String vehicleModel;
		private String vehicleIdentificationNumber;
		
		public String getVehicleMake() {
			return vehicleMake;
		}
		public void setVehicleMake(String vehicleMake) {
			this.vehicleMake = vehicleMake;
		}
		public String getVehicleModel() {
			return vehicleModel;
		}
		public void setVehicleModel(String vehicleModel) {
			this.vehicleModel = vehicleModel;
		}
		public String getVehicleIdentificationNumber() {
			return vehicleIdentificationNumber;
		}
		public void setVehicleIdentificationNumber(String vehicleIdentificationNumber) {
			this.vehicleIdentificationNumber = vehicleIdentificationNumber;
		}
		
		public Integer getVehicleCrashQueryResultsId() {
			return vehicleCrashQueryResultsId;
		}
		
		public void setVehicleCrashQueryResultsId(Integer vehicleCrashQueryResultsId) {
			this.vehicleCrashQueryResultsId = vehicleCrashQueryResultsId;
		}

		public String toString(){
			return ToStringBuilder.reflectionToString(this, ToStringStyle.MULTI_LINE_STYLE);
		}
	
}
