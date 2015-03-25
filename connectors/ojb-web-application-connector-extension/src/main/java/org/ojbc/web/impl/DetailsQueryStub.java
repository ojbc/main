package org.ojbc.web.impl;


import org.ojbc.web.DetailsQueryInterface;
import org.ojbc.web.model.person.query.DetailsRequest;
import org.w3c.dom.Element;

/**
 * This stub is provided to show an implementer how to override a bean from
 * the ojb-web-application-connector.  See the Spring context file
 * ojbc-web-application-connector-extension-beans.xml for more details. 
 * 
 */

public class DetailsQueryStub implements DetailsQueryInterface {
	
	@Override
	public String invokeRequest(DetailsRequest request, String federatedQueryID, Element samlToken) throws Exception {

		throw new RuntimeException("This is just a stub: " + request.getIdentificationSourceText());

	}
}

