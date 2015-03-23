package org.ojbc.web;

/**
 * This class assigns enumerations to static values from 
 * the UI layer.
 * 
 * A use case for this funcationality is translating from POJO to XML
 * 
 */
public enum SearchFieldMetadata {

	StartsWith("StartsWith"), Partial("partial"), ExactMatch("exact");

	private String metadata;

	private SearchFieldMetadata(String metadata) {
		this.metadata = metadata;
	}

	public String getMetadata() {
		return metadata;
	}

}
