package org.ojbc.util.model.accesscontrol;

public class AccessControlResponse {

	private boolean isAuthorized;
	private String accessControlResponseMessage;
	
	public boolean isAuthorized() {
		return isAuthorized;
	}
	public void setAuthorized(boolean isAuthorized) {
		this.isAuthorized = isAuthorized;
	}
	public String getAccessControlResponseMessage() {
		return accessControlResponseMessage;
	}
	public void setAccessControlResponseMessage(String accessControlResponseMessage) {
		this.accessControlResponseMessage = accessControlResponseMessage;
	}
	
}
