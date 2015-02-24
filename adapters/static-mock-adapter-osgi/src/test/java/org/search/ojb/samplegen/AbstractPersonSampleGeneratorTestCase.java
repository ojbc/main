package org.search.ojb.samplegen;

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
