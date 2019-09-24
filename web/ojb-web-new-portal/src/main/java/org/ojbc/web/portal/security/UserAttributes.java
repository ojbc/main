package org.ojbc.web.portal.security;

import java.util.List;

public class UserAttributes {

	private List<String> roles;
	private List<String> oris;
	
	public List<String> getRoles() {
		return roles;
	}
	public void setRoles(List<String> roles) {
		this.roles = roles;
	}
	public List<String> getOris() {
		return oris;
	}
	public void setOris(List<String> oris) {
		this.oris = oris;
	}
	
}