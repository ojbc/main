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
package org.ojbc.adapters.analyticsstaging.custody.dao.model;
import java.io.Serializable;
import java.math.BigDecimal;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;

public class BookingCharge implements Serializable
{
	private static final long serialVersionUID = -7705114517046869650L;
	private Integer bookingChargeId;
    private Integer bookingArrestId; 
    private String chargeCode; 
    private String chargeDisposition; 
	private Integer agencyId;
    private BigDecimal bondAmount;
    private KeyValue bondType;
    private Integer chargeClassTypeId;
    private Integer bondStatusTypeId;
    private Integer chargeJurisdictionTypeId;

	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this, ToStringStyle.MULTI_LINE_STYLE);	
	}

	public Integer getBookingChargeId() {
		return bookingChargeId;
	}

	public void setBookingChargeId(Integer bookingChargeId) {
		this.bookingChargeId = bookingChargeId;
	}

	public Integer getBookingArrestId() {
		return bookingArrestId;
	}

	public void setBookingArrestId(Integer bookingArrestId) {
		this.bookingArrestId = bookingArrestId;
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

	public String getChargeCode() {
		return chargeCode;
	}

	public void setChargeCode(String chargeCode) {
		this.chargeCode = chargeCode;
	}

	public Integer getChargeClassTypeId() {
		return chargeClassTypeId;
	}

	public void setChargeClassTypeId(Integer chargeClassTypeId) {
		this.chargeClassTypeId = chargeClassTypeId;
	}

	public Integer getBondStatusTypeId() {
		return bondStatusTypeId;
	}

	public void setBondStatusTypeId(Integer bondStatusTypeId) {
		this.bondStatusTypeId = bondStatusTypeId;
	}

	public Integer getChargeJurisdictionTypeId() {
		return chargeJurisdictionTypeId;
	}

	public void setChargeJurisdictionTypeId(Integer chargeJurisdictionTypeId) {
		this.chargeJurisdictionTypeId = chargeJurisdictionTypeId;
	}

	public String getChargeDisposition() {
		return chargeDisposition;
	}

	public void setChargeDisposition(String chargeDisposition) {
		this.chargeDisposition = chargeDisposition;
	}

}