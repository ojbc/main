package org.ojbc.web.security;

import javax.servlet.http.HttpServletRequest;

import org.ojbc.web.portal.services.SamlService;
import org.springframework.stereotype.Service;
import org.w3c.dom.Element;

@Service("mockSamlService")
public class SamlServiceMockImpl implements SamlService{
	
	public Element getSamlAssertion(HttpServletRequest request) {
		// Note: This method currently gets a new SAML assertion every time it is called. In the future we may want to cache the
		// assertions and only get a new one if the cached one is expired.
		Element assertion = (Element)request.getAttribute("samlAssertion");
		return assertion;
	}
}
