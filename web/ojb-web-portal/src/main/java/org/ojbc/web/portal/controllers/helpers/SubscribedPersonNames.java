package org.ojbc.web.portal.controllers.helpers;

import java.util.ArrayList;
import java.util.List;

public class SubscribedPersonNames {
	
	private String originalName;
	
	// non null by contract - avoid NPE's from getter
	private List<String> alternateNamesList = new ArrayList<String>();

	public String getOriginalName() {
		return originalName;
	}

	public List<String> getAlternateNamesList() {
		return alternateNamesList;
	}

	public void setOriginalName(String originalName) {
		this.originalName = originalName;
	}

	public void setAlternateNamesList(List<String> alternateNamesList) {
		this.alternateNamesList = alternateNamesList;
	}

	@Override
	public String toString() {
		return "SubscribedPersonNames [originalName=" + originalName
				+ ", alternateNamesList=" + alternateNamesList + "]";
	}
		
}
