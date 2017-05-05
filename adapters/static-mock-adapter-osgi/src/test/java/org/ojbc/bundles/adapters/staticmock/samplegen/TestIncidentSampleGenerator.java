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
import org.joda.time.DateTime;
import org.junit.Before;
import org.junit.Test;
import org.ojbc.bundles.adapters.staticmock.samplegen.IncidentSampleGenerator;
import org.ojbc.util.xml.XmlUtils;
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
        XmlUtils.printNode(samples.get(0));
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
        return "ssp/Incident_Reporting/artifacts/service_model/information_model/IEPD/xsd/";
    }

    @Override
    protected String getRootSchemaFileName() {
        return "exchange_schema/Incident_Report.xsd";
    }

}
