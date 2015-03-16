package org.ojbc.firearm.search;

public enum SearchFieldMetadata {

	Partial("partial"), ExactMatch("exact");

	private String metadata;

	private SearchFieldMetadata(String metadata) {
		this.metadata = metadata;
	}

	public String getMetadata() {
		return metadata;
	}

}
