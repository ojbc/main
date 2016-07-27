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

import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;

public class CustodyStatusChangeArrest implements Serializable
{
	private static final long serialVersionUID = -7705114517046869650L;
	private Integer custodyStatusChangeArrestId;
    private Integer custodyStatusChangeId; 
    private Address address;

	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this, ToStringStyle.MULTI_LINE_STYLE);	
	}

	public Integer getCustodyStatusChangeArrestId() {
		return custodyStatusChangeArrestId;
	}

	public void setCustodyStatusChangeArrestId(
			Integer custodyStatusChangeArrestId) {
		this.custodyStatusChangeArrestId = custodyStatusChangeArrestId;
	}

	public Integer getCustodyStatusChangeId() {
		return custodyStatusChangeId;
	}

	public void setCustodyStatusChangeId(Integer custodyStatusChangeId) {
		this.custodyStatusChangeId = custodyStatusChangeId;
	}

	public Address getAddress() {
		return address;
	}

	public void setAddress(Address address) {
		this.address = address;
	}

}