package org.ojbc.bundles.prototypes.bundlea.beans;

import org.apache.camel.Exchange;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.ojbc.bundles.prototypes.shared.interfaces.LogMessageHelper;

public class BundleABean4 implements LogMessageHelper {

	private static final Log LOGGER = LogFactory.getLog(BundleABean4.class);

	@Override
	public void writeLogMessage(Exchange e, String message) throws Exception {
		LOGGER.info("Exchange=" + e);
		LOGGER.info("Bundle A Bean 4 writing message: " + message);
	}

}
