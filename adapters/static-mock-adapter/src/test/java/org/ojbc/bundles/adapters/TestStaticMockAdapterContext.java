package org.ojbc.bundles.adapters;

import org.apache.camel.test.junit4.CamelSpringTestSupport;
import org.junit.Test;
import org.springframework.context.support.AbstractApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class TestStaticMockAdapterContext extends CamelSpringTestSupport{

    @Override
    protected AbstractApplicationContext createApplicationContext() {
        return new ClassPathXmlApplicationContext(new String[]{"META-INF/spring/camel-context.xml"});
    }
    
    @Test
    public void testApplicationStartup()
    {
        assertTrue(true);
    }   

}
