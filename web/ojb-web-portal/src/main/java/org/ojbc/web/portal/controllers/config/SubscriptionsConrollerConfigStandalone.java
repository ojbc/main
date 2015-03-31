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
@Profile("standalone")
public class SubscriptionsConrollerConfigStandalone implements SubscriptionsControllerConfigInterface{
	
	@Resource
	SubscriptionSearchInterface subscriptionSearchInterface;
	
	@Resource
	SubscriptionQueryInterface subscriptionQueryInterface;
	
	@Resource
	SubscriptionInterface subscriptionSubscribeInterface;

	@Resource
	UnsubscriptionInterface unsubscriptionInterface;
	
	@Resource
	SubscriptionValidationInterface subscriptionValidationInterface; 
	
	
	@Override
	public SubscriptionSearchInterface getSubscriptionSearchBean() {
		return subscriptionSearchInterface;
	}
	
	@Override
	public SubscriptionQueryInterface getSubscriptionQueryBean(){
		return  subscriptionQueryInterface;
	}
	
	@Override
	public SubscriptionInterface getSubscriptionSubscribeBean() {
		return subscriptionSubscribeInterface;
	}

	@Override
	public UnsubscriptionInterface getUnsubscriptionBean() {
		return unsubscriptionInterface;
	}

	@Override
	public SubscriptionValidationInterface getSubscriptionValidationBean() {
		return subscriptionValidationInterface;
	}	

}
