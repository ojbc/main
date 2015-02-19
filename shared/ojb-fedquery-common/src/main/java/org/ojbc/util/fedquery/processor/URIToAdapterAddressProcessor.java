package org.ojbc.util.fedquery.processor;

import java.util.HashMap;
import java.util.Map;

import org.apache.camel.Exchange;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * This class will look up web service addresses based on entries in a URI table.
 * This is helpful so you can re-use a single CXF endpoint bean and set the 
 * Camel Destination Override URL properly to send to any endpoint that implements 
 * the adapter web service specification.  It is used in a federated query pattern.
 * 
 * @author yogeshchawla
 *
 */
public class URIToAdapterAddressProcessor {

	private Map <String, String> adapterURItoAddressMap = new HashMap<String, String>();
	
	private static final Log log = LogFactory.getLog( URIToAdapterAddressProcessor.class );
	
	public void overrideCXFAddress(Exchange exchange)
	{
		String adapterURI = (String) exchange.getIn().getHeader("adapterURI");
		
		if (StringUtils.isNotBlank(adapterURI))
		{
			if (adapterURItoAddressMap.containsKey(adapterURI))
			{
				log.debug("Map contains key: " + adapterURI +  ", setting destination override to: " + adapterURItoAddressMap.get(adapterURI));
				
				exchange.getIn().setHeader(Exchange.DESTINATION_OVERRIDE_URL, adapterURItoAddressMap.get(adapterURI));
			}	
		}	
	}

	public Map<String, String> getAdapterURItoAddressMap() {
		return adapterURItoAddressMap;
	}

	public void setAdapterURItoAddressMap(Map<String, String> adapterURItoAddressMap) {
		this.adapterURItoAddressMap = adapterURItoAddressMap;
	}


	
}
