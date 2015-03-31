package org.ojbc.web.portal.controllers.config;

import javax.annotation.Resource;

import org.ojbc.web.DetailsQueryInterface;
import org.ojbc.web.PersonSearchInterface;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

@Configuration
@Profile("person-search")
public class PeopleControllerConfigBrokered implements PeopleControllerConfigInterface {
	@Resource (name="${personSearchRequestProcessorBean:personSearchRequestProcessor}")
	PersonSearchInterface personSearchInterface;

	@Resource (name="${personSearchDetailsQueryBean:detailQueryDispatcher}")
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
