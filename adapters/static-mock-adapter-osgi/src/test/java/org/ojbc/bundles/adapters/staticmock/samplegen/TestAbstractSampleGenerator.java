/*
 * Unless explicitly acquired and licensed from Licensor under another license, the contents of
 * this file are subject to the Reciprocal Public License ("RPL") Version 1.5, or subsequent
 * versions as allowed by the RPL, and You may not copy or use this file in either source code
 * or executable form, except in compliance with the terms and conditions of the RPL
 *
 * All software distributed under the RPL is provided strictly on an "AS IS" basis, WITHOUT
 * WARRANTY OF ANY KIND, EITHER EXPRESS OR IMPLIED, AND LICENSOR HEREBY DISCLAIMS ALL SUCH
 * WARRANTIES, INCLUDING WITHOUT LIMITATION, ANY WARRANTIES OF MERCHANTABILITY, FITNESS FOR A
 * PARTICULAR PURPOSE, QUIET ENJOYMENT, OR NON-INFRINGEMENT. See the RPL for specific language
 * governing rights and limitations under the RPL.
 *
 * http://opensource.org/licenses/RPL-1.5
 *
 * Copyright 2012-2017 Open Justice Broker Consortium
 */
package org.ojbc.bundles.adapters.staticmock.samplegen;

import java.io.IOException;

import javax.xml.parsers.ParserConfigurationException;

import org.junit.Test;
import org.ojbc.bundles.adapters.staticmock.samplegen.AbstractSampleGenerator;

import junit.framework.TestCase;

public class TestAbstractSampleGenerator extends TestCase {
    
    private static final class SampleGenerator extends AbstractSampleGenerator {

        public SampleGenerator() throws ParserConfigurationException, IOException {
            super();
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
