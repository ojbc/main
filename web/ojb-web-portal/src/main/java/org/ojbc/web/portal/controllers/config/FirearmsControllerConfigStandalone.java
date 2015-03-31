package org.ojbc.web.portal.controllers.config;

import javax.annotation.Resource;

import org.ojbc.web.DetailsQueryInterface;
import org.ojbc.web.FirearmSearchInterface;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

@Configuration
@Profile("standalone")
public class FirearmsControllerConfigStandalone implements
		FirearmsControllerConfigInterface {
	@Resource
	FirearmSearchInterface firearmSearchInterface;

	@Resource
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
