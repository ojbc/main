package org.search.ojb.samplegen;

import java.util.List;

import org.junit.Test;
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
        return "service-specifications/Person_Query_Results_Handler_Service_-_Warrants/artifacts/service-model/information-model/Person_Query_Results_-_Warrants-IEPD/xsd/";
    }

    @Override
    protected String getRootSchemaFileName() {
        return "Person_Query_Results_-_Warrants.xsd";
    }

}
