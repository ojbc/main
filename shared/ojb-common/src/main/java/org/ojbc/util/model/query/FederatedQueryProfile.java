package org.ojbc.util.model.query;

public class FederatedQueryProfile {

	private String serviceName;
	private boolean responseReceived;

	
	public FederatedQueryProfile(String serviceName, boolean responseReceived) {
		super();
		this.serviceName = serviceName;
		this.responseReceived = responseReceived;
	}
	public String getServiceName() {
		return serviceName;
	}
	public void setServiceName(String serviceName) {
		this.serviceName = serviceName;
	}
	public boolean isResponseReceived() {
		return responseReceived;
	}
	public void setResponseReceived(boolean responseReceived) {
		this.responseReceived = responseReceived;
	}
	
	@Override
	public String toString() {
		return "FederatedQueryProfile [serviceName=" + serviceName
				+ ", responseReceived=" + responseReceived + "]";
	}
	
}