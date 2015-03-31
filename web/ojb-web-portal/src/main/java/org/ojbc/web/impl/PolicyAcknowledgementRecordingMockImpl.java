package org.ojbc.web.impl;

import org.ojbc.web.PolicyBasedAcknowledgementRecordingService;
import org.springframework.stereotype.Service;
import org.w3c.dom.Element;

@Service
public class PolicyAcknowledgementRecordingMockImpl implements PolicyBasedAcknowledgementRecordingService {

    @Override
    public String invokePolicyAcknowledgementRecordingRequest(String federatedQueryID,
            Element samlToken) throws Exception {
        // TODO Auto-generated method stub
        return null;
    }

}
