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

import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;

public class BookingArrest implements Serializable
{
	private static final long serialVersionUID = -7705114517046869650L;
	private Integer bookingArrestId;
    private Integer bookingId; 
    private String streetNumber; 
    private String streetName; 
    private String addressSecondaryUnit; 
    private String city; 
    private String state; 
    private String postalcode; 
	private BigDecimal arrestLocationLatitude; 
	private BigDecimal arrestLocationLongitude; 

	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this, ToStringStyle.MULTI_LINE_STYLE);	
	}

	public Integer getBookingId() {
		return bookingId;
	}

	public void setBookingId(Integer bookingId) {
		this.bookingId = bookingId;
	}

	public Integer getBookingArrestId() {
		return bookingArrestId;
	}

	public void setBookingArrestId(Integer bookingArrestId) {
		this.bookingArrestId = bookingArrestId;
	}

	public BigDecimal getArrestLocationLatitude() {
		return arrestLocationLatitude;
	}

	public void setArrestLocationLatitude(BigDecimal arrestLocationLatitude) {
		this.arrestLocationLatitude = arrestLocationLatitude;
	}

	public BigDecimal getArrestLocationLongitude() {
		return arrestLocationLongitude;
	}

	public void setArrestLocationLongitude(BigDecimal arrestLocationLongitude) {
		this.arrestLocationLongitude = arrestLocationLongitude;
	}

	public String getStreetNumber() {
		return streetNumber;
	}

	public void setStreetNumber(String streetNumber) {
		this.streetNumber = streetNumber;
	}

	public String getStreetName() {
		return streetName;
	}

	public void setStreetName(String streetName) {
		this.streetName = streetName;
	}

	public String getAddressSecondaryUnit() {
		return addressSecondaryUnit;
	}

	public void setAddressSecondaryUnit(String addressSecondaryUnit) {
		this.addressSecondaryUnit = addressSecondaryUnit;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

	public String getPostalcode() {
		return postalcode;
	}

	public void setPostalcode(String postalcode) {
		this.postalcode = postalcode;
	}

}