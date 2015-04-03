package org.ojbc.policyacknowledgement.dao;

import java.util.Date;

/**
 * Data access object for policy
 */
public class Policy {
    private long id;
    private String policyUri;
	private String policyLocation;
	private Date updateDate;
    private boolean active; 
	
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public String getPolicyUri() {
		return policyUri;
	}
	public void setPolicyUri(String policyUri) {
		this.policyUri = policyUri;
	}
	public String getPolicyLocation() {
		return policyLocation;
	}
	public void setPolicyLocation(String policyLocation) {
		this.policyLocation = policyLocation;
	}
	public Date getUpdateDate() {
		return updateDate;
	}
	public void setUpdateDate(Date updateDate) {
		this.updateDate = updateDate;
	}
    public boolean isActive() {
        return active;
    }
    public void setActive(boolean active) {
        this.active = active;
    }
}
