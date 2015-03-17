package org.ojbc.person.search;

public enum SearchFieldMetadata {

	StartsWith("StartsWith"), ExactMatch("ExactMatch");

	@SuppressWarnings("unused")
	private String metadata;

	private SearchFieldMetadata(String metadata) {
		this.metadata = metadata;
	}

}
