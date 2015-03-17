package org.ojbc.web.model.subscription.add;

import java.util.Date;

public class BlankEditableEndDateStrategy implements SubscriptionEndDateStrategy {

	public boolean isEditable() {
		
		return true;
	}

	public Date getDefaultValue() {
		
		return null;
	}

}
