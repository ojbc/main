package org.ojbc.web;

import org.ojbc.web.model.firearm.search.FirearmSearchRequest;
import org.w3c.dom.Element;

public interface FirearmSearchInterface {
	public String invokeFirearmSearchRequest(FirearmSearchRequest firearmSearchRequest, String federatedQueryID, 
			Element samlToken) throws Exception;
}
