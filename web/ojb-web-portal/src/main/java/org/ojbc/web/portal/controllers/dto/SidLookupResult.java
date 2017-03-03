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
package org.ojbc.web.portal.controllers.dto;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;
import org.ojbc.web.portal.controllers.helpers.SubscribedPersonNames;

public class SidLookupResult implements Serializable{
	
	private static final long serialVersionUID = 2989647661256727602L;
	
	private String fbiId;
	private SubscribedPersonNames personNames;

	public String getFbiId() {
		return fbiId == null? "N/A" : fbiId;
	}

	public void setFbiId(String fbiId) {
		this.fbiId = fbiId;
	}

	public SubscribedPersonNames getPersonNames() {
		return personNames;
	}

	public void setPersonNames(SubscribedPersonNames personNames) {
		this.personNames = personNames;
	}
	public String toString(){
		return ToStringBuilder.reflectionToString(this, ToStringStyle.MULTI_LINE_STYLE);
	}
	
	public List<String> getAllNames(){
		List<String> allNames = new ArrayList<>();
		
		if (personNames.getOriginalName() != null){
			allNames.add(personNames.getOriginalName());
		}
		
		personNames.getAlternateNamesList()
			.forEach(item->allNames.add(item));
		
		return allNames; 
	}
	
}
