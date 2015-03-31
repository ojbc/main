package org.ojbc.web.portal.controllers.helpers;


public class SamlTokenParsed {
		
	private String email;

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	@Override
	public String toString() {
		return "SamlTokenParsed [email=" + email + "]";
	}
	
}
