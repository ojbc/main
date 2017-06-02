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
package org.ojbc.web.model.person.search;

import java.io.Serializable;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

public class Address implements Serializable
{
    private static final long serialVersionUID = -2328253045673210893L;
    private Long id = null;
	private String address1 = null;
	private String address2 = null;
	private String city = null;
	private String stateCode = null;
	private String stateDesc = null;
	private String zipCode = null;
	private String zipCodeExt = null;

	public Address() {
		super();
	}

	/**
	 * Public properties
	 */

	public Long getId() {
		return id;
	}
	public void setId(Long id) {
			this.id = id;
	}

	public String getAddress1() {
		return this.address1;
	}
	public void setAddress1(String address1) {
		this.address1 = address1;
	}

	public String getAddress2() {
		return this.address2;
	}
	public void setAddress2(String address2) {
		this.address2 = address2;
	}

	public String getCity() {
		return this.city;
	}
	public void setCity(String city) {
			this.city = city;
	}

	public String getStateCode() {
		return stateCode;
	}

	public void setStateCode(String stateCode) {
			this.stateCode = stateCode;
	}

	public String getStateDesc() {
		return stateDesc;
	}

	public void setStateDesc(String stateDesc) {
		this.stateDesc = stateDesc;
	}


	public String getZipCode() {
		return this.zipCode;
	}
	public void setZipCode(String zipCode) {
			this.zipCode = zipCode;
	}

	public String getZipCodeExt() {
		return this.zipCodeExt;
	}
	public void setZipCodeExt(String zipCodeExt) {
			this.zipCodeExt = zipCodeExt;
	}

	public String getFullAddress(){
        StringBuilder sb = new StringBuilder( 128 );
        sb.append(address1).append(" ");
        if (address2 != null) sb.append(address2).append(" ");
        sb.append(",").append(city).append(" " + stateCode).append(" " + zipCode);
        if (zipCodeExt != null) sb.append("-" + zipCodeExt);
        return sb.toString();
	}

	/**
	 * Add a <br> before City. 
	 * @return
	 */
	public String getFormatedAddress(){
        StringBuilder sb = new StringBuilder( 128 );
        sb.append(address1).append(" ");
        if (address2 != null) sb.append(address2).append(" ");
        sb.append("<br>").append(city).append(" " + stateCode).append(" " + zipCode);
        if (zipCodeExt != null) sb.append("-" + zipCodeExt);
        return sb.toString();
    }

	public boolean equals(Address address){
	    return StringUtils.equals( address1, address.getAddress1() ) &&
	            StringUtils.equals( address2, address.getAddress2() ) &&
	            StringUtils.equals( city, address.getCity() ) &&
	            StringUtils.equals( stateCode, address.getStateCode() ) &&
	            StringUtils.equals( zipCode, address.getZipCode()) &&
	            StringUtils.equals( zipCodeExt, address.getZipCodeExt() ); 
	}
	/**
	 * Formats the address into one string.
	 * @return
	 */
	@Override
	public String toString() {
	    return ToStringBuilder.reflectionToString(this, ToStringStyle.DEFAULT_STYLE); 
	}
}