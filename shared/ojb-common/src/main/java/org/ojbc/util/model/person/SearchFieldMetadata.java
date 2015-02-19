package org.ojbc.util.model.person;

public enum SearchFieldMetadata {

	StartsWith("startsWith"), ExactMatch("exact");

	private String metadata;
	
    private SearchFieldMetadata(String metadata) {
        this.metadata = metadata;
	}

}
