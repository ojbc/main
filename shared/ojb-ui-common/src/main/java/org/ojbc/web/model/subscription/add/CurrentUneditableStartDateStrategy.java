package org.ojbc.web.model.subscription.add;

import java.util.Date;

public class CurrentUneditableStartDateStrategy implements
		SubscriptionStartDateStrategy {

	public boolean isEditable() {

		return false;
	}

	public Date getDefaultValue() {
		
		//today's current date
		return new Date();
	}

}
