package org.ojbc.processor;

import org.apache.camel.Exchange;

public class SimpleMessageProcessor {
	
	
	/**
	 * This method will use an existing exchange and set the 'out' message 
	 * 
	 * @param exchange
	 * @throws Exception
	 */
	public void prepareNewSimpleExchangeResponseMessage(Exchange exchange) throws Exception
	{
			
		exchange.getOut().setBody(exchange.getIn().getBody());
	}


}
