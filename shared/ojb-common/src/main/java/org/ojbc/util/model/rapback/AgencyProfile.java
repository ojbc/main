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
package org.ojbc.util.model.rapback;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

public class AgencyProfile {

	private String agencyOri;
	private String agencyName;
	private Boolean fbiSubscriptionQualification;
	private Boolean civilAgencyIndicator;
	
	public String getAgencyOri() {
		return agencyOri;
	}
	public void setAgencyOri(String agencyOri) {
		this.agencyOri = agencyOri;
	}
	public String getAgencyName() {
		return agencyName;
	}
	public void setAgencyName(String agencyName) {
		this.agencyName = agencyName;
	}
	public Boolean getFbiSubscriptionQualification() {
		return fbiSubscriptionQualification;
	}
	public void setFbiSubscriptionQualification(Boolean fbiSubscriptionQualification) {
		this.fbiSubscriptionQualification = fbiSubscriptionQualification;
	}
	public Boolean getCivilAgencyIndicator() {
		return civilAgencyIndicator;
	}
	public void setCivilAgencyIndicator(Boolean civilAgencyIndicator) {
		this.civilAgencyIndicator = civilAgencyIndicator;
	}
	
	public String toString(){
		return ToStringBuilder.reflectionToString(this, ToStringStyle.MULTI_LINE_STYLE);
	}
}
