package org.ojbc.util.fedquery.processor;

import java.util.HashMap;
import java.util.Map;

import junit.framework.Assert;

import org.apache.camel.CamelContext;
import org.apache.camel.Exchange;
import org.apache.camel.impl.DefaultCamelContext;
import org.apache.camel.impl.DefaultExchange;
import org.junit.Test;

public class TestURIToAdapterAddressProcessor {

	@Test
	public void testOverrideCXFAddress(){
	
		URIToAdapterAddressProcessor hashMapToAdapterAddressProcessor = new URIToAdapterAddressProcessor();
		
		Map<String, String> adapterURItoAddressMap = new HashMap<String, String>();
		adapterURItoAddressMap.put("http://www.ojbc.org/adapter", "http://localhost/myURL");
		
		hashMapToAdapterAddressProcessor.setAdapterURItoAddressMap(adapterURItoAddressMap);

		CamelContext ctx = new DefaultCamelContext(); 
		Exchange ex = new DefaultExchange(ctx);
		
		ex.getIn().setHeader("adapterURI", "http://www.ojbc.org/adapter");
		
		hashMapToAdapterAddressProcessor.overrideCXFAddress(ex);
		
		Assert.assertEquals("http://localhost/myURL", ex.getIn().getHeader(Exchange.DESTINATION_OVERRIDE_URL));

		Exchange ex2 = new DefaultExchange(ctx);
		ex2.getIn().setHeader("adapterURI", "http://www.ojbc.org/adapter2");
		
		hashMapToAdapterAddressProcessor.overrideCXFAddress(ex2);
		Assert.assertNull(ex2.getIn().getHeader(Exchange.DESTINATION_OVERRIDE_URL));

		
	}
	
}
