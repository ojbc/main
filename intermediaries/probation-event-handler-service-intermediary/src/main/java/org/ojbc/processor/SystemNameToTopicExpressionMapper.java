package org.ojbc.processor;

import java.util.Map;

import org.apache.camel.Exchange;
import org.apache.camel.Header;

public class SystemNameToTopicExpressionMapper {
	
	private Map<String, String> systemNameToTopicMap;
	
	/**
	 * This method will set the topic expression that is passed into the XSLT 
	 * 
	 * @param exchange
	 * @throws Exception
	 */
	public void populateTopicExpressionHeader(Exchange exchange, @Header(value = "systemName") String systemName) throws Exception {
		exchange.getIn().setHeader("topicExpression", systemNameToTopicMap.get(systemName));
	}

	public Map<String, String> getSystemNameToTopicMap() {
		return systemNameToTopicMap;
	}

	public void setSystemNameToTopicMap(Map<String, String> systemNameToTopicMap) {
		this.systemNameToTopicMap = systemNameToTopicMap;
	}

}
