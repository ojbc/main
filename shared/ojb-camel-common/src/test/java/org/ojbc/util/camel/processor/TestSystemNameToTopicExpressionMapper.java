package org.ojbc.util.camel.processor;

import static junit.framework.Assert.assertEquals;

import java.util.HashMap;
import java.util.Map;

import org.apache.camel.CamelContext;
import org.apache.camel.Exchange;
import org.apache.camel.impl.DefaultCamelContext;
import org.apache.camel.impl.DefaultExchange;
import org.junit.Test;

public class TestSystemNameToTopicExpressionMapper {

	@Test
	public void populateTopicExpressionHeader() throws Exception
	{
		SystemNameToTopicExpressionMapper systemNameToTopicExpressionMapper = new SystemNameToTopicExpressionMapper();
		
		Map<String, String> systemNameToTopicMap = new HashMap<String, String>();
		
		systemNameToTopicMap.put("{http://ojbc.org/ParoleCase/1.0}ParolingAuthority","topics:person/arrest");
		
		systemNameToTopicExpressionMapper.setSystemNameToTopicMap(systemNameToTopicMap );
		
		CamelContext ctx = new DefaultCamelContext(); 
		Exchange ex = new DefaultExchange(ctx);
		
		systemNameToTopicExpressionMapper.populateTopicExpressionHeader(ex, "{http://ojbc.org/ParoleCase/1.0}ParolingAuthority");
		
		assertEquals("topics:person/arrest", (String)ex.getIn().getHeader("topicExpression"));
	}

}
