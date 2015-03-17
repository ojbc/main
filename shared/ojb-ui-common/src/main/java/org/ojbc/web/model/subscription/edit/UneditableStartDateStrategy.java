package org.ojbc.web.model.subscription.edit;

import java.util.Date;

import org.ojbc.web.model.subscription.add.SubscriptionStartDateStrategy;

public class UneditableStartDateStrategy implements SubscriptionStartDateStrategy{

	@Override
	public boolean isEditable() {
		return false;
	}

	@Override
	public Date getDefaultValue() {
		return null;
	}

}
