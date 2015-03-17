package org.ojbc.web.model.subscription.add;

import java.util.Date;


public interface SubscriptionStartDateStrategy {
	
	public boolean isEditable();
	
	public Date getDefaultValue();

}
