package org.ojbc.web.portal.controllers.config;

import javax.annotation.Resource;

import org.ojbc.web.DetailsQueryInterface;
import org.ojbc.web.FirearmSearchInterface;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

@Configuration
@Profile({"firearms-search"})
public class FirearmsControllerConfigBrokered implements
		FirearmsControllerConfigInterface {
	@Resource (name="firearmSearchRequestProcessor")
	FirearmSearchInterface firearmSearchInterface;

	@Resource (name="detailQueryDispatcher")
	DetailsQueryInterface detailsQueryInterface;
	
	@Override
	public FirearmSearchInterface getFirearmSearchBean() {
		return firearmSearchInterface;
	}

	@Override
	public DetailsQueryInterface getDetailsQueryBean() {
		return detailsQueryInterface;
	}

}
