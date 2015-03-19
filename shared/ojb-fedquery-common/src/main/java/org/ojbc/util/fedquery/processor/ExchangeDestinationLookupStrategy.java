package org.ojbc.util.fedquery.processor;

import org.apache.camel.Exchange;

/**
 * This interface is use in the federated query to determine
 * what endpoint and endpoint address to use when call an adapter.
 * 
 * When used with PrepareFederatedQuery, the systemNameURI is provided
 * as a camel header.
 * 
 * @author yogeshchawla
 *
 */
public interface ExchangeDestinationLookupStrategy {

	public String getCXFEndpointName(Exchange exchange);
	public String getCXFEndpointAddress(Exchange exchange);
}
