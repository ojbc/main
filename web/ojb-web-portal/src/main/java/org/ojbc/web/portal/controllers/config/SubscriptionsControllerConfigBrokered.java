package org.ojbc.web.portal.controllers.config;

import javax.annotation.Resource;

import org.ojbc.web.SubscriptionQueryInterface;
import org.ojbc.web.SubscriptionSearchInterface;
import org.ojbc.web.SubscriptionInterface;
import org.ojbc.web.SubscriptionValidationInterface;
import org.ojbc.web.UnsubscriptionInterface;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

@Configuration
@Profile({"subscriptions"})
public class SubscriptionsControllerConfigBrokered implements SubscriptionsControllerConfigInterface {

	
	@Resource(name="subscriptionSearchRequestProcessor")
	SubscriptionSearchInterface subscriptionSearchInterface;
	
	@Resource(name="subscriptionQueryRequestProcessor")
	SubscriptionQueryInterface subscriptionQueryInterface;
	
	@Resource(name="subscriptionRequestProcessor")
	SubscriptionInterface subscriptionSubscribeInterface; 

	@Resource(name="unsubscriptionRequestProcessor")
	UnsubscriptionInterface unsubscriptionSubscribeInterface; 
	
	@Resource(name ="subscriptionValidationRequestProcessor")
	SubscriptionValidationInterface subscriptionValidationInterface;

	
	@Override
	public SubscriptionSearchInterface getSubscriptionSearchBean() {		
		return subscriptionSearchInterface;
	}

	@Override
	public SubscriptionQueryInterface getSubscriptionQueryBean(){
		return subscriptionQueryInterface;
	}
	
	@Override
	public SubscriptionInterface getSubscriptionSubscribeBean() {
		return subscriptionSubscribeInterface;
	}

	@Override
	public UnsubscriptionInterface getUnsubscriptionBean() {
		return unsubscriptionSubscribeInterface;
	}

	@Override
	public SubscriptionValidationInterface getSubscriptionValidationBean() {
		return subscriptionValidationInterface;
	}
	
}
