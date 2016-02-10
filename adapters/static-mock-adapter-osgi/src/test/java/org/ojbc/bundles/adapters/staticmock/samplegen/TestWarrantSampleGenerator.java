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

import org.junit.Test;
import org.ojbc.bundles.adapters.staticmock.samplegen.AbstractPersonSampleGenerator;
import org.ojbc.bundles.adapters.staticmock.samplegen.WarrantSampleGenerator;
import org.ojbc.util.xml.XmlUtils;
import org.w3c.dom.Document;
import org.w3c.dom.Node;

/**
 * Test for the warrant sample generator
 *
 */
public class TestWarrantSampleGenerator extends AbstractPersonSampleGeneratorTestCase {

    @Test
    public void testCreateSampleForState() throws Exception {
        List<Document> documentList = sampleGenerator.generateSample(1, dateFormatter.parseDateTime("1-1-2013"), "HI");
        assertEquals(1, documentList.size());
        Document d = documentList.get(0);
        //XmlUtils.printNode(d);
        Node result = XmlUtils.xPathNodeSearch(d.getDocumentElement(), "/warrant:Warrants/warrant-ext:eBWResults/nc:Location/nc:LocationAddress/nc:StructuredAddress/nc:LocationPostalCode/text()");
        assertNotNull(result);
        String value = result.getNodeValue();
        int zipInt = Integer.parseInt(value);
        assertTrue(zipInt >= 96700 && zipInt <= 96899); // Hawaii zip code range
    }
    
    @Override
    protected AbstractPersonSampleGenerator createSampleGenerator() {
        return WarrantSampleGenerator.getInstance();
    }

    @Override
    protected String getSchemaRootFolderName() {
        return "NIEM_2.1";
    }

    @Override
    protected String getIEPDRootPath() {
        return "ssp/Person_Query_Results_Handler_Service_-_Warrants/artifacts/service-model/information-model/Person_Query_Results_-_Warrants-IEPD/xsd/";
    }

    @Override
    protected String getRootSchemaFileName() {
        return "Person_Query_Results_-_Warrants.xsd";
    }

}
