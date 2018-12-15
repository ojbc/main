package org.ojbc.web.portal.services;

import org.apache.commons.lang3.StringUtils;

import com.fasterxml.jackson.annotation.JsonIgnore;

public class CodeTableEntry {

	public String id;
	public String code;
	public String description;
	private String citation;
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
	public String getCitation() {
		return citation;
	}
	public void setCitation(String citation) {
		this.citation = citation;
	}
	
	@JsonIgnore
	public String getCitationDescription() {
		if (StringUtils.isNotBlank(citation)) {
			return citation + " " + description;  
		}
		else {
			return description;
		}
	}
	
}