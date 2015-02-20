package org.ojbc.bundles.prototypes.shared.interfaces;
import org.apache.camel.Exchange;

/**
 * Simple little bean used by the prototype bundles.
 *
 */
public interface LogMessageHelper {
	
	public void writeLogMessage(Exchange e, String message) throws Exception;

}
