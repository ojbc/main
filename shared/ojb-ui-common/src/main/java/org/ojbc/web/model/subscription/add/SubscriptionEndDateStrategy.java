package org.ojbc.web.model.subscription.add;

import java.util.Date;


public interface SubscriptionEndDateStrategy {
	
	public boolean isEditable();
	
	public Date getDefaultValue();

}
