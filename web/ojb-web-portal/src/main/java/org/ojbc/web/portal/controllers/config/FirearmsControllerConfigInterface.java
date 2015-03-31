package org.ojbc.web.portal.controllers.config;

import org.ojbc.web.DetailsQueryInterface;
import org.ojbc.web.FirearmSearchInterface;

public interface FirearmsControllerConfigInterface {
	FirearmSearchInterface getFirearmSearchBean();
	DetailsQueryInterface getDetailsQueryBean();
}
