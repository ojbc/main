package org.ojbc.web;

import org.w3c.dom.Element;

public interface PolicyBasedAcknowledgementRecordingService {
    public String invokePolicyAcknowledgementRecordingRequest(String federatedQueryID,
            Element samlToken) throws Exception;
}
