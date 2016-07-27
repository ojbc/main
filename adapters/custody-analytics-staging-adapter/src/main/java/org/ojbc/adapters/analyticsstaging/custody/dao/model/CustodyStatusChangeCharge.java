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
package org.ojbc.adapters.analyticsstaging.custody.dao.model;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;

public class CustodyStatusChangeCharge implements Serializable
{
	private static final long serialVersionUID = 8493646293327612848L;
	private Integer custodyStatusChangeChargeId;
    private Integer custodyStatusChangeArrestId; 
    private KeyValue chargeType; 
	private Integer agencyId;
    private BigDecimal bondAmount; 
    private String nextCourtName; 
    private LocalDate nextCourtDate; 
    private KeyValue bondType;

	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this, ToStringStyle.MULTI_LINE_STYLE);	
	}

	public KeyValue getChargeType() {
		return chargeType;
	}

	public void setChargeType(KeyValue chargeType) {
		this.chargeType = chargeType;
	}

	public Integer getCustodyStatusChangeChargeId() {
		return custodyStatusChangeChargeId;
	}

	public void setCustodyStatusChangeChargeId(
			Integer custodyStatusChangeChargeId) {
		this.custodyStatusChangeChargeId = custodyStatusChangeChargeId;
	}

	public Integer getAgencyId() {
		return agencyId;
	}

	public void setAgencyId(Integer agencyId) {
		this.agencyId = agencyId;
	}

	public BigDecimal getBondAmount() {
		return bondAmount;
	}

	public void setBondAmount(BigDecimal bondAmount) {
		this.bondAmount = bondAmount;
	}

	public KeyValue getBondType() {
		return bondType;
	}

	public void setBondType(KeyValue bondType) {
		this.bondType = bondType;
	}

	public Integer getCustodyStatusChangeArrestId() {
		return custodyStatusChangeArrestId;
	}

	public void setCustodyStatusChangeArrestId(
			Integer custodyStatusChangeArrestId) {
		this.custodyStatusChangeArrestId = custodyStatusChangeArrestId;
	}

	public String getNextCourtName() {
		return nextCourtName;
	}

	public void setNextCourtName(String nextCourtName) {
		this.nextCourtName = nextCourtName;
	}

	public LocalDate getNextCourtDate() {
		return nextCourtDate;
	}

	public void setNextCourtDate(LocalDate nextCourtDate) {
		this.nextCourtDate = nextCourtDate;
	}

}