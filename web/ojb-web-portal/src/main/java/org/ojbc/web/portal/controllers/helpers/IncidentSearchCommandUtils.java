package org.ojbc.web.portal.controllers.helpers;

import java.util.ArrayList;

import org.apache.commons.beanutils.BeanUtils;
import org.ojbc.web.model.incident.search.IncidentSearchRequest;
import org.ojbc.web.portal.controllers.dto.IncidentSearchCommand;
import org.springframework.stereotype.Service;

@Service
public class IncidentSearchCommandUtils {
	public IncidentSearchRequest getIncidentSearchRequest(IncidentSearchCommand incidentSearchCommand) {
		IncidentSearchRequest request = cloneIncidentSearchRequest(incidentSearchCommand.getAdvanceSearch());
		
		return request;
	}

	IncidentSearchRequest cloneIncidentSearchRequest(
			IncidentSearchRequest originalRequest) {

		try {
			IncidentSearchRequest cloneBean = (IncidentSearchRequest) BeanUtils.cloneBean(originalRequest);
			
			cloneBean.setSourceSystems(new ArrayList<String>(originalRequest.getSourceSystems()));
			return cloneBean;
		} catch (Exception ex) {
			throw new RuntimeException("Unable to clone IncidentSearchRequest", ex);
		}
	}
	
	
}
