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
package org.ojbc.web.portal.arrest;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.ojbc.web.OjbcWebConstants.ArrestType;

public class ArrestCharge {
	private ArrestType arrestType; 
	private String arrestIdentification;
	private String arrestChargeIdentification;
	private String arrestChargeDescription;
	private Disposition disposition; 
    public ArrestCharge() {
    	super();
    }
	public ArrestCharge(ArrestType arrestType) {
		this();
		this.setArrestType(arrestType);
		disposition = new Disposition();
	}
	public String getArrestIdentification() {
		return arrestIdentification;
	}
	public void setArrestIdentification(String arrestIdentification) {
		this.arrestIdentification = arrestIdentification;
	}
	public String getArrestChargeIdentification() {
		return arrestChargeIdentification;
	}
	public void setArrestChargeIdentification(String arrestChargeIdentification) {
		this.arrestChargeIdentification = arrestChargeIdentification;
	}
    
	public String toString() {
		return ToStringBuilder.reflectionToString(this, ToStringStyle.MULTI_LINE_STYLE);
	}
	public ArrestType getArrestType() {
		return arrestType;
	}
	public void setArrestType(ArrestType arrestType) {
		this.arrestType = arrestType;
	}
	public String getArrestChargeDescription() {
		return arrestChargeDescription;
	}
	public void setArrestChargeDescription(String arrestChargeDescription) {
		this.arrestChargeDescription = arrestChargeDescription;
	}
	public Disposition getDisposition() {
		return disposition;
	}
	public void setDisposition(Disposition disposition) {
		this.disposition = disposition;
	}
}    
