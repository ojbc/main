package org.ojbc.web;

public class SearchProfile {
	private String id;
	private String displayName;
	private boolean enabled;
	
	public SearchProfile(String id, String displayName, boolean enabled) {
		this.id = id;
		this.displayName = displayName;
		this.enabled = enabled;
	}
	
	public String getId() {
		return this.id;
	}
	
	public String getDisplayName() {
		return this.displayName;
	}

	public boolean isEnabled() {
		return enabled;
	}
	
}
