package org.ojbc.web.portal.controllers.config;

import org.ojbc.web.SubscriptionQueryInterface;
import org.ojbc.web.SubscriptionSearchInterface;
import org.ojbc.web.SubscriptionInterface;
import org.ojbc.web.SubscriptionValidationInterface;
import org.ojbc.web.UnsubscriptionInterface;

public interface SubscriptionsControllerConfigInterface {

	SubscriptionSearchInterface getSubscriptionSearchBean();
	
	SubscriptionQueryInterface getSubscriptionQueryBean();
	
	SubscriptionInterface getSubscriptionSubscribeBean();	
	
	UnsubscriptionInterface getUnsubscriptionBean();
	
	SubscriptionValidationInterface getSubscriptionValidationBean();
}
