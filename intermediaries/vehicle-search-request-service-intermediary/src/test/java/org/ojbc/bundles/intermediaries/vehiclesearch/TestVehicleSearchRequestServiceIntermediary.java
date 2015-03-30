package org.ojbc.bundles.intermediaries.vehiclesearch;

import static org.junit.Assert.assertTrue;

import org.apache.camel.test.junit4.CamelSpringJUnit4ClassRunner;
import org.apache.camel.test.spring.UseAdviceWith;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;

@UseAdviceWith	// NOTE: this causes Camel contexts to not start up automatically
@RunWith(CamelSpringJUnit4ClassRunner.class)
@ContextConfiguration(locations={
		"classpath:META-INF/spring/camel-context.xml", 
		"classpath:META-INF/spring/cxf-endpoints.xml",
		"classpath:META-INF/spring/extensible-beans.xml",		
		"classpath:META-INF/spring/local-osgi-context.xml",
		"classpath:META-INF/spring/properties-context.xml"}) 
public class TestVehicleSearchRequestServiceIntermediary {
	
	private static final Log log = LogFactory.getLog( TestVehicleSearchRequestServiceIntermediary.class );
	    	
    @Test
    public void testApplicationStartup() {
    	assertTrue(true);
    }	
    

    
}