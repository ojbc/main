package org.ojbc.intermediaries.crimhistoryupdate;

import java.util.Map;

import org.apache.camel.Exchange;

/**
 * This class will read a map and set Camel headers accordingly.
 * The Camel Headers are used as parameters for XSLT calls.
 * 
 * 
 * @author yogeshchawla
 *
 */
public class CycleIdToNotifXsltParamMapper {
	
	private Map<String, String> cycleIdToNotifXsltParamMap;
			
	public void populateHeaderWithXsltParams(Exchange exchange) throws Exception {
		
		for(String paramNameForXslt : cycleIdToNotifXsltParamMap.keySet()){
					
			String valueOfXsltParam = cycleIdToNotifXsltParamMap.get(paramNameForXslt);
			
			exchange.getIn().setHeader(paramNameForXslt, valueOfXsltParam);
		}
		
	}

	public Map<String, String> getCycleIdToNotifXsltParamMap() {
		return cycleIdToNotifXsltParamMap;
	}

	public void setCycleIdToNotifXsltParamMap(
			Map<String, String> cycleIdToNotifXsltParamMap) {
		this.cycleIdToNotifXsltParamMap = cycleIdToNotifXsltParamMap;
	}	

}
