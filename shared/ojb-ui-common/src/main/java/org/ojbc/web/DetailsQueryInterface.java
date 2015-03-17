package org.ojbc.web;

import org.ojbc.web.model.person.query.DetailsRequest;
import org.w3c.dom.Element;

public interface DetailsQueryInterface {

	public String invokeRequest(DetailsRequest detailsRequest, String federatedQueryID, Element samlToken) throws Exception;

}
