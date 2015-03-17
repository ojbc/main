package org.ojbc.web;

import org.ojbc.web.model.person.search.PersonSearchRequest;
import org.w3c.dom.Element;

public interface PersonSearchInterface {

	public String invokePersonSearchRequest(PersonSearchRequest personSearchRequest, String federatedQueryID,
	        Element samlToken) throws Exception;

}
