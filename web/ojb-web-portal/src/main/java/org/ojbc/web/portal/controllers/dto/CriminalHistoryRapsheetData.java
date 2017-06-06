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
package org.ojbc.web.portal.controllers.dto;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;
import org.ojbc.web.model.person.search.PersonName;
import org.ojbc.web.portal.controllers.helpers.SubscribedPersonNames;

public class CriminalHistoryRapsheetData implements Serializable{
	
	private static final long serialVersionUID = 2989647661256727602L;
	
	private String fbiId;
	private SubscribedPersonNames personNames;
	private List<LocalDate> dobs; 

	public String getFormattedFbiId() {
		return fbiId == null? "N/A" : fbiId;
	}

	public String getFbiId() {
		return fbiId;
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
		
		if (personNames != null){
			if (personNames.getOriginalName() != null){
				allNames.add(personNames.getOriginalName().getFullName());
			}
			
			personNames.getAlternateNamesList()
				.forEach(item->allNames.add(item.getFullName()));
		}
		
		return allNames; 
	}

	public Map<String, String> getNamesMap(){
		
		Map<String, String> namesMap = new LinkedHashMap<>();
		
		namesMap.put("", "Name");
		
		List<String> allNames = getAllNames();
		
		if (!allNames.isEmpty()){
			allNames.forEach(name->namesMap.put(name, name));
		}
		
		return namesMap; 
	}
	
	public Map<String, String> getDobsMap(){
		
		Map<String, String> map = new LinkedHashMap<>();
		
		map.put("", "DOB");
		
		List<String> dobStrings = getDobStrings();
		
		if (null != dobStrings){
			dobStrings.forEach(dobString->map.put(dobString, dobString));
		}
		
		return map; 
	}
	
	public Map<String, PersonName> getfullNameToPersonNameMap(){
		Map<String, PersonName> fullNameToPersonNameMap = new HashMap<>();
		
		if (personNames != null){
			if (personNames.getOriginalName() != null){
				fullNameToPersonNameMap.put(personNames.getOriginalName().getFullName(), personNames.getOriginalName());
				
			}
			
			personNames.getAlternateNamesList()
				.forEach(personName -> fullNameToPersonNameMap.put(personName.getFullName(), personName));
		}
		
		return fullNameToPersonNameMap;
	}
	
	public List<LocalDate> getDobs() {
		return dobs;
	}

	public void setDobs(List<LocalDate> dobs) {
		this.dobs = dobs;
	}

	public List<String> getDobStrings(){
		if (dobs == null || dobs.isEmpty()) {
			return null; 
		}
		
		return dobs.stream()
				.map(item->item.format(DateTimeFormatter.ofPattern("MM/dd/yyyy")))
				.collect(Collectors.toList());
	}
	
}
