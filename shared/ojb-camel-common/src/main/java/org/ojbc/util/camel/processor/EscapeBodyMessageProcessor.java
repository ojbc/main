package org.ojbc.util.camel.processor;

import org.apache.camel.Exchange;
import org.apache.commons.lang.StringEscapeUtils;

public class EscapeBodyMessageProcessor {

	public void escapeCamelBody(Exchange exchange)
	{
		
		String body = exchange.getIn().getBody(String.class);
		
		String escapedBody = StringEscapeUtils.escapeXml(body);
		
		exchange.getIn().setBody(escapedBody);
		
	}
	
}
