package org.ojbc.bundles.prototypes.bundlea.beans;

import org.apache.camel.Exchange;
import org.apache.camel.Message;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.ojbc.bundles.prototypes.shared.interfaces.LogMessageHelper;

public class BundleABean3 implements LogMessageHelper {
	
	private static final Log LOGGER = LogFactory.getLog(BundleABean3.class);

	@Override
	public void writeLogMessage(Exchange e, String message) throws Exception {
		LOGGER.info("Exchange=" + e);
		LOGGER.info("Bundle A Bean 3 writing message: " + message);
		Message m = e.getIn();
		String body = m.getBody(String.class);
		StringBuffer messages = new StringBuffer();
		messages.append("m=").append(message);
		m.setBody(body + "\nBundle A Bean 3:" + messages);
	}

}
