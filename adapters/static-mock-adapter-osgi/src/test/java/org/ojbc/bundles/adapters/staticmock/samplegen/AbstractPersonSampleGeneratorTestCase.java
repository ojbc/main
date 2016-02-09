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
 * Copyright 2012-2015 Open Justice Broker Consortium
 */
package org.ojbc.bundles.adapters.staticmock.samplegen;

import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.junit.Before;
import org.junit.Test;
import org.w3c.dom.Document;

/**
 * Abstract test case class that validates sample instances.
 *
 */
public abstract class AbstractPersonSampleGeneratorTestCase extends AbstractSampleGeneratorTestCase {

    private static final Log LOG = LogFactory.getLog( AbstractPersonSampleGeneratorTestCase.class );
    
    protected AbstractPersonSampleGenerator sampleGenerator;
    
    @Before
    public void setUp() throws Exception {
        sampleGenerator = createSampleGenerator();
        super.setUp();
    }
    
    /**
     * Subclasses implement to create the sample generator under test
     * @return the sample generator to test
     */
    protected abstract AbstractPersonSampleGenerator createSampleGenerator();
    
    @Test
    public final void testCreateSample() throws Exception {
    	
        final List<Document> list = sampleGenerator.generateSample(INSTANCE_COUNT, dateFormatter.parseDateTime("1-1-2013"));
        
        //XmlUtils.printNode(list.get(0));
        validateDocumentList(list);
    }

}
