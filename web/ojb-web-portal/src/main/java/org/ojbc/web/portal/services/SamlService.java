package org.ojbc.web.portal.services;

import javax.servlet.http.HttpServletRequest;

import org.w3c.dom.Element;

public interface SamlService {
	
	public Element getSamlAssertion(HttpServletRequest request); 
}
