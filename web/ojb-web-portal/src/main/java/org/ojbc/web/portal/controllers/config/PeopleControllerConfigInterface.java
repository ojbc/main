package org.ojbc.web.portal.controllers.config;

import org.ojbc.web.DetailsQueryInterface;
import org.ojbc.web.PersonSearchInterface;

public interface PeopleControllerConfigInterface {
	PersonSearchInterface getPersonSearchBean();
	DetailsQueryInterface getDetailsQueryBean();
}
