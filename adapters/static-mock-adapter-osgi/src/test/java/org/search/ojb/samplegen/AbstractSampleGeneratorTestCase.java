package org.search.ojb.samplegen;

import java.util.Arrays;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.junit.Before;
import org.ojbc.util.xml.XmlUtils;
import org.w3c.dom.Document;
import org.w3c.dom.Node;

import junit.framework.TestCase;

public abstract class AbstractSampleGeneratorTestCase extends TestCase {

    protected static final int INSTANCE_COUNT = 50;
    protected DocumentBuilder documentBuilder;
    protected DateTimeFormatter dateFormatter;
    
    @Before
    public void setUp() throws Exception {
        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        dbf.setNamespaceAware(true);
        documentBuilder = dbf.newDocumentBuilder();
        dateFormatter = DateTimeFormat.forPattern("MM-dd-yyyy");
    }

    protected final void assertNodeValueIn(Node context, String xPath, String... codes) throws Exception {
        Node result = XmlUtils.xPathNodeSearch(context, xPath);
        assertNotNull(result);
        String value = result.getNodeValue();
        assertTrue(Arrays.asList(codes).contains(value));
    }

    protected final void assertNodeValue(Node context, String expression, String value) throws Exception {
        Node result = XmlUtils.xPathNodeSearch(context, expression);
        assertEquals(value, result.getNodeValue());
    }

    protected final void assertNodeExists(Node context, String expression) throws Exception {
        Node result = XmlUtils.xPathNodeSearch(context, expression);
        assertNotNull(result);
    }

    /**
     * Subclasses implement to specify the name of the folder at the root of the IEPD...this is the folder that contains the xsd directory
     * @return the name of the root folder
     */
    protected abstract String getSchemaRootFolderName();

    /**
     * Subclasses implement to specify the path, on the classpath, of the IEPD.  This should live in the OJB_Resources bundle.
     * @return the path to the IEPD
     */
    protected abstract String getIEPDRootPath();

    /**
     * Subclasses implement to provide the name of the exchange schema file
     * @return the exchange schema file name
     */
    protected abstract String getRootSchemaFileName();

    protected final void validateDocumentList(List<Document> documentList) throws Exception {
        for (Document d : documentList) {
            XmlUtils.validateInstance(getIEPDRootPath(), getSchemaRootFolderName(), getRootSchemaFileName(), d);
        }
    }

}
