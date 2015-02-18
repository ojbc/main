package org.ojbc.bundles.intermediaries;

import org.apache.camel.test.junit4.CamelSpringTestSupport;
import org.junit.Test;
import org.springframework.context.support.AbstractXmlApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class TestPersonQueryServiceWarrants  extends CamelSpringTestSupport{

    @Override
    protected AbstractXmlApplicationContext createApplicationContext() {
        return new ClassPathXmlApplicationContext(new String[]{"META-INF/spring/camel-context.xml"});
    }

    @Test
    public void testApplicationStartup()
    {
    	assertTrue(true);
    }	
}
