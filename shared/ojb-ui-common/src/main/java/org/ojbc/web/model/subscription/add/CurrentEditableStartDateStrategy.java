package org.ojbc.web.model.subscription.add;

import java.util.Date;

public class CurrentEditableStartDateStrategy implements SubscriptionStartDateStrategy{

	public boolean isEditable() {

		return true;
	}

	public Date getDefaultValue() {

		// today's current date
		return new Date();
	}

}
