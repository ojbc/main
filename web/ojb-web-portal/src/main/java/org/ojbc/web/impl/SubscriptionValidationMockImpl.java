package org.ojbc.web.impl;

import org.ojbc.web.SubscriptionValidationInterface;
import org.ojbc.web.model.subscription.response.common.FaultableSoapResponse;
import org.springframework.stereotype.Service;
import org.w3c.dom.Element;

@Service
public class SubscriptionValidationMockImpl implements SubscriptionValidationInterface{

    @Override
    public FaultableSoapResponse validate(String subscriptionIdentificationId, String topic,
            String federatedQueryID, Element samlToken) throws Exception {
        throw new UnsupportedOperationException("Method not implemented yet");
    }


}
