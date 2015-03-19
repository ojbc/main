package org.ojbc.intermediaries.juvenilehistory;

import java.util.HashMap;
import java.util.Map;

import org.apache.camel.Exchange;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.ojbc.util.fedquery.processor.ExchangeDestinationLookupStrategy;

/**
 * This is the default ExchangeDestinationLookupStrategy.  To CXF Endpoint Name and is based on the
 * incoming historyType which is determine on the initial inbound service request.
 * 
 * The CXF endpoint address is looked up in map using a combination of historyType - agencyName as the key.
 * 
 * The URI convention that will be used is:
 * {http://ojbc.org/Services/WSDL/JuvenileHistoryRequest/1.0}Person-Query-Service-JuvenileHistory-agencyName
 * 
 * This is needed because each service URL be unique per service and agency.
 * 
 * @author yogeshchawla
 *
 */
public class ServiceContextExchangeDestinationLookupStrategy implements ExchangeDestinationLookupStrategy{

	private static final Log log = LogFactory.getLog( ServiceContextExchangeDestinationLookupStrategy.class );
	
	private Map <String, String> historyTypeToAddressMap = new HashMap<String, String>();
	
	@Override
	public String getCXFEndpointName(Exchange exchange) {
		String systemName = (String)exchange.getIn().getHeader("systemNameURI");		
		log.debug("System name: " + systemName);
		
		String endpointName = "";
		
		String historyType = (String) exchange.getIn().getHeader("historyType");

		if (historyType.equals("CasePlan"))
		{
			endpointName="juvenileCasePlanHistoryRequestAdapterServiceEndpoint";
		}	

		if (historyType.equals("Hearing"))
		{
			endpointName="juvenileHearingHistoryRequestAdapterServiceEndpoint";
		}	

		if (historyType.equals("Intake"))
		{
			endpointName="juvenileIntakeHistoryRequestAdapterServiceEndpoint";
		}	

		if (historyType.equals("Offense"))
		{
			endpointName="juvenileOffenseHistoryRequestAdapterServiceEndpoint";
		}	

		if (historyType.equals("Placement"))
		{
			endpointName="juvenilePlacementHistoryRequestAdapterServiceEndpoint";
		}	

		if (historyType.equals("Referral"))
		{
			endpointName="juvenileReferralHistoryRequestAdapterServiceEndpoint";
		}	

		
		return endpointName;
	}

	@Override
	public String getCXFEndpointAddress(Exchange exchange) {
		
		String systemName = (String)exchange.getIn().getHeader("systemNameURI");		
		log.debug("System name: " + systemName);
		
		String agencyName = StringUtils.substringAfter(systemName, "{http://ojbc.org/Services/WSDL/JuvenileHistoryRequest/1.0}Person-Query-Service-JuvenileHistory");
		String historyType = (String) exchange.getIn().getHeader("historyType");

		String historyTypeToAddressMapKey = historyType + agencyName;
		
		log.debug("History Type to Address Key: " + historyTypeToAddressMapKey);
		
		String cxfEndpointAddress = "";
		
		if (StringUtils.isNotBlank(historyTypeToAddressMapKey))
		{
			if (historyTypeToAddressMap.containsKey(historyTypeToAddressMapKey))
			{
				log.debug("Map contains key: " + historyTypeToAddressMapKey +  ", returning endpoint address of: " + historyTypeToAddressMap.get(historyTypeToAddressMapKey));
				
				cxfEndpointAddress = historyTypeToAddressMap.get(historyTypeToAddressMapKey);
			}	
		}	

		return cxfEndpointAddress;	
		
	}

	public Map<String, String> getHistoryTypeToAddressMap() {
		return historyTypeToAddressMap;
	}

	public void setHistoryTypeToAddressMap(
			Map<String, String> historyTypeToAddressMap) {
		this.historyTypeToAddressMap = historyTypeToAddressMap;
	}

	
	
}
