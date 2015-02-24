package org.search.ojb.samplegen;

import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.joda.time.DateTime;
import org.junit.Before;
import org.junit.Test;
import org.w3c.dom.Document;

/**
 * Unit test for the incident instance generator.
 *
 */
public class TestIncidentSampleGenerator extends AbstractSampleGeneratorTestCase {

    private static final Log LOG = LogFactory.getLog(TestIncidentSampleGenerator.class);

    private IncidentSampleGenerator sampleGenerator;

    @Before
    public void setUp() throws Exception {
        sampleGenerator = IncidentSampleGenerator.getInstance();
    }

    @Test
    public void testBasic() throws Exception {
        List<Document> samples = sampleGenerator.generateSample(INSTANCE_COUNT, new DateTime(), "VT");
        assertEquals(INSTANCE_COUNT, samples.size());
        //XmlUtils.printNode(samples.get(0));
        // unfortunately, due to complexity of N-DEx IEPD, we are unable to validate instances against it via Java code
        // You can uncomment the above line, print the document to the console, paste it into a file, and validate that way...
    }

    // these methods do not work...see comment above about N-DEx IEPD...
    
    @Override
    protected String getSchemaRootFolderName() {
        return ".";
    }

    @Override
    protected String getIEPDRootPath() {
        return "service-specifications/Incident_Reporting_Service/artifacts/service_model/information_model/Incident_Report_IEPD/xsd/";
    }

    @Override
    protected String getRootSchemaFileName() {
        return "exchange_schema/Incident_Report.xsd";
    }

}
