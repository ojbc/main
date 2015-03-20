package org.ojbc.util.fedquery.processor;

import java.util.HashMap;
import java.util.Map;

import org.apache.camel.Exchange;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * This implementation expects two maps adapterURItoAddressMap and federatedQueryEndpointMap.
 * Both maps use the system URI as the key.  federatedQueryEndpointMap uses the CXF endpoint
 * as the value in the map.  adapterURItoAddressMap uses the adapter URI as the value in the map.
 * 
 * This implementation expects unique values for the systemURI in any given bundle.
 * 
 * 
 * @author yogeshchawla
 *
 */
public class RecordSourceExchangeDestinationLookupStrategy implements ExchangeDestinationLookupStrategy{

	private static final Log log = LogFactory.getLog( RecordSourceExchangeDestinationLookupStrategy.class );
	
	private Map <String, String> adapterURItoAddressMap = new HashMap<String, String>();
	private Map<String, String> federatedQueryEndpointMap = new HashMap<String, String>();
	
	@Override
	public String getCXFEndpointName(Exchange exchange) {

		String systemName = (String)exchange.getIn().getHeader("systemNameURI");
		
		String endpointName = federatedQueryEndpointMap.get(systemName);
		return endpointName;
	}

	@Override
	public String getCXFEndpointAddress(Exchange exchange) {

		String systemName = (String)exchange.getIn().getHeader("systemNameURI");
		
		String cxfEndpointAddress="";
		
		if (StringUtils.isNotBlank(systemName))
		{
			if (adapterURItoAddressMap.containsKey(systemName))
			{
				log.debug("Map contains key: " + systemName +  ", returning endpoint address of: " + adapterURItoAddressMap.get(systemName));
				
				cxfEndpointAddress = adapterURItoAddressMap.get(systemName);
			}	
		}	

		return cxfEndpointAddress;	
		
	}

	public Map<String, String> getAdapterURItoAddressMap() {
		return adapterURItoAddressMap;
	}

	public void setAdapterURItoAddressMap(Map<String, String> adapterURItoAddressMap) {
		this.adapterURItoAddressMap = adapterURItoAddressMap;
	}

	public Map<String, String> getFederatedQueryEndpointMap() {
		return federatedQueryEndpointMap;
	}

	public void setFederatedQueryEndpointMap(
			Map<String, String> federatedQueryEndpointMap) {
		this.federatedQueryEndpointMap = federatedQueryEndpointMap;
	}

}
