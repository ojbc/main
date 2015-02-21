package org.ojbc.bundles.prototypes.shared.util;

import org.apache.camel.Exchange;
import org.apache.camel.Message;

/**
 * Simple demo processor used to support the prototype test.
 *
 */
public class MessageEditProcessor {
	
	public void appendMessage(Exchange e) throws Exception {
		
		Message m = e.getIn();
		
		String s = m.getHeader("s", String.class);
		
		String body = m.getBody(String.class);
		m.setBody(body + "\n" + s);
		
	}

}
