package org.ojbc.web.portal.controllers.config;

import javax.annotation.Resource;

import org.ojbc.web.DetailsQueryInterface;
import org.ojbc.web.PersonSearchInterface;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

@Configuration
@Profile("standalone")
public class PeopleControllerConfigStandalone implements PeopleControllerConfigInterface {
    @Resource (name="${personSearchRequestProcessorBean:personSearchMockImpl}")
	PersonSearchInterface personSearchInterface;

	@Resource
	DetailsQueryInterface detailsQueryInterface;
	
	@Override
	public PersonSearchInterface getPersonSearchBean() {
		return personSearchInterface;
	}

	@Override
	public DetailsQueryInterface getDetailsQueryBean() {
		return detailsQueryInterface;
	}

}
