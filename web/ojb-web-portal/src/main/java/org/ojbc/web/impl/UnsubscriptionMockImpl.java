package org.ojbc.web.impl;

import java.util.logging.Logger;

import org.ojbc.web.UnsubscriptionInterface;
import org.springframework.stereotype.Service;
import org.w3c.dom.Element;

@Service
public class UnsubscriptionMockImpl implements UnsubscriptionInterface{

	private Logger logger = Logger.getLogger(UnsubscriptionMockImpl.class.getName());

	@Override
	public void unsubscribe(String subscriptionIdentificationId, String topic, String federatedQueryID,  Element samlToken)
			throws Exception {

		logger.info("TODO implement");
	}



	

}
