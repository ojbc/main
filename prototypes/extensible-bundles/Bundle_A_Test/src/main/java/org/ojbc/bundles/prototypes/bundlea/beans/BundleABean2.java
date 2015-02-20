package org.ojbc.bundles.prototypes.bundlea.beans;

import org.apache.camel.Exchange;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.ojbc.bundles.prototypes.shared.interfaces.LogMessageHelper;

public class BundleABean2 {
	
	private static final Log LOGGER = LogFactory.getLog(BundleABean2.class);
	
	private LogMessageHelper logMessageHelper;
	
	public LogMessageHelper getLogMessageHelper() {
		return logMessageHelper;
	}

	public void setLogMessageHelper(LogMessageHelper logMessageHelper) {
		this.logMessageHelper = logMessageHelper;
	}
	
	public void processExchangeWithHelp(Exchange e) throws Exception {
		LOGGER.info("BundleABean2 using helper to write message");
		logMessageHelper.writeLogMessage(e, "Message via helper");
	}

	public void processExchange(Exchange e) throws Exception {
		LOGGER.info("BundleABean2 message");
	}


}
