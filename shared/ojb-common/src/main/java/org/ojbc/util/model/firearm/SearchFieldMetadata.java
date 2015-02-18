package org.ojbc.util.model.firearm;

public enum SearchFieldMetadata {

	ExactMatch("exact"), Partial("partial");

    private String metadata;

	private SearchFieldMetadata(String metadata) {
		this.metadata = metadata;
	}
	
	public String getMetadata() {
		return metadata;
	}

}
