package org.ojbc.web.impl;

import org.apache.commons.lang.StringUtils;
import org.ojbc.web.PersonSearchInterface;
import org.ojbc.web.WebUtils;
import org.ojbc.web.model.person.search.PersonSearchRequest;
import org.springframework.stereotype.Service;
import org.w3c.dom.Element;

@Service
public class JuvenilePersonSearchMockImpl implements PersonSearchInterface {

	@Override
	public String invokePersonSearchRequest(PersonSearchRequest personSearchRequest, String federatedQueryID,
	        Element samlToken) throws Exception {
		if (StringUtils.isEmpty(federatedQueryID)) {
			throw new IllegalStateException("Federated Query ID not set");
		}

		System.out.println("PEOPLE SEARCH - Federated Query ID: " + federatedQueryID);
		System.out.println("------------------------------------------------------------------");
		System.out.println(personSearchRequest);
		System.out.println("------------------------------------------------------------------");
		return WebUtils.returnStringFromFilePath(getClass().getResourceAsStream(
		        "/sampleResponses/personSearch/Juvenile_EntityMergeResultMessage.xml"));
	}

}
