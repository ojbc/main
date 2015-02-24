package org.search.ojb.samplegen;

import java.io.IOException;

import javax.xml.parsers.ParserConfigurationException;

import org.junit.Test;

import junit.framework.TestCase;

public class TestAbstractSampleGenerator extends TestCase {
    
    private static final class SampleGenerator extends AbstractSampleGenerator {

        public SampleGenerator() throws ParserConfigurationException, IOException {
            super();
            // TODO Auto-generated constructor stub
        }
        
    }
    
    @Test
    public void testGetRandomIdentity() throws Exception {
        SampleGenerator sg = new SampleGenerator();
        AbstractSampleGenerator.PersonElementWrapper pew = sg.getRandomIdentity(null);
        assertNotNull(pew);
        pew = sg.getRandomIdentity("CA");
        assertNotNull(pew);
        pew = sg.getRandomIdentity("XX");
        assertNull(pew);
    }

}
