package org.ojbc.web.portal.controllers.helpers;

import java.util.ArrayList;

import org.apache.commons.beanutils.BeanUtils;
import org.ojbc.web.model.firearm.search.FirearmSearchRequest;
import org.ojbc.web.portal.controllers.dto.FirearmSearchCommand;
import org.springframework.stereotype.Service;

@Service
public class FirearmSearchCommandUtils {
	public FirearmSearchRequest getFirearmSearchRequest(FirearmSearchCommand firearmSearchCommand) {
		FirearmSearchRequest request = cloneFirearmSearchRequest(firearmSearchCommand.getAdvanceSearch());
		
		return request;
	}
	
	FirearmSearchRequest cloneFirearmSearchRequest(FirearmSearchRequest originalRequest) {
		try {
			FirearmSearchRequest cloneBean = (FirearmSearchRequest) BeanUtils.cloneBean(originalRequest);
			
			cloneBean.setSourceSystems(new ArrayList<String>(originalRequest.getSourceSystems()));
			return cloneBean;
		} catch (Exception ex) {
			throw new RuntimeException("Unable to clone FirearmSearchRequest", ex);
		}
	}
}
