package org.ojbc.web;

import org.w3c.dom.Element;

public interface IdentityBasedAccessControlService {
    public String invokeAccessControlRequest(String federatedQueryID, Element samlToken, String requestedResourceURI);
}
