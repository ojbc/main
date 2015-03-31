package org.ojbc.web.impl;

import org.ojbc.web.FirearmSearchInterface;
import org.ojbc.web.WebUtils;
import org.ojbc.web.model.firearm.search.FirearmSearchRequest;
import org.springframework.stereotype.Service;
import org.w3c.dom.Element;

@Service
public class FirearmSearchMockImpl implements FirearmSearchInterface {

	@Override
	public String invokeFirearmSearchRequest(
			FirearmSearchRequest firearmSearchRequest, String federatedQueryID,
			Element samlToken) throws Exception {
		
		System.out.println("FIREARM SEARCH - Federated Query ID: " + federatedQueryID);
		System.out.println("------------------------------------------------------------------");
		System.out.println(firearmSearchRequest);
		System.out.println("------------------------------------------------------------------");

		return WebUtils.returnStringFromFilePath(getClass().getResourceAsStream(
		        "/sampleResponses/firearmSearch/er_FirearmSearchResults.xml"));

	}

}
