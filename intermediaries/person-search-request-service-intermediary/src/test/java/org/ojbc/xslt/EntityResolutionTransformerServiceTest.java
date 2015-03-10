package org.ojbc.xslt;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.TransformerFactoryConfigurationError;
import javax.xml.transform.dom.DOMResult;
import javax.xml.transform.dom.DOMSource;

import org.ojbc.util.xml.XmlUtils;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.junit.Before;
import org.junit.Test;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class EntityResolutionTransformerServiceTest {

    private static final Log LOG = LogFactory.getLog(EntityResolutionTransformerServiceTest.class);

    private DocumentBuilder documentBuilder;

    @Before
    public void setup() throws ParserConfigurationException {
        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        dbf.setNamespaceAware(true);
        documentBuilder = dbf.newDocumentBuilder();
    }

    @Test
    public void personResponseTransform() throws Exception {

        Document inputDocument = parse("xmlInstances/personSearchResults/personSearchResults.xml");
        Document xsltDocument = parse("xslt/PersonSearchResultsToEntityResolutionRequest.xsl");

        Document resultDocument = transform(inputDocument, xsltDocument);
        //XmlUtils.printNode(inputDocument);

        Node requestNode = XmlUtils.xPathNodeSearch(resultDocument, "em-req-exc:EntityMergeRequestMessage");
        assertNotNull(requestNode);

        NodeList attributeParameterNodeList = XmlUtils.xPathNodeListSearch(requestNode, "em-req-exc:MergeParameters/er-ext:AttributeParameters/er-ext:AttributeParameter");
        assertEquals(13, attributeParameterNodeList.getLength());

        NodeList entityNodeList = XmlUtils.xPathNodeListSearch(requestNode, "em-req-exc:MergeParameters/er-ext:EntityContainer/er-ext:Entity");
        int length = entityNodeList.getLength();
        assertEquals(4, length);

        for (int i = 0; i < length; i++) {
            Node entityNode = entityNodeList.item(i);
            assertTrue(XmlUtils.nodeExists(entityNode, "@s:id"));
            assertTrue(XmlUtils.nodeExists(entityNode, "psres:PersonSearchResult/psres:SourceSystemNameText/text()"));
            assertTrue(XmlUtils.nodeExists(entityNode, "psres:PersonSearchResult/intel:SystemIdentifier/nc:IdentificationID/text()"));
            assertTrue(XmlUtils.nodeExists(entityNode, "psres:PersonSearchResult/intel:SystemIdentifier/intel:SystemName/text()"));
            XmlUtils.compare(XmlUtils.xPathNodeSearch(entityNode, "psres:PersonSearchResult"), XmlUtils.xPathNodeSearch(inputDocument, "/OJBAggregateResponseWrapper/psres-doc:PersonSearchResults/psres:PersonSearchResult[" + (i + 1) + "]"));

        }

    }

    @Test
    public void errorResponseTransform() throws Exception {

        Document inputDocument = parse("xmlInstances/entityResolution/noResults_error.xml");
        Document xsltDocument = parse("xslt/PersonSearchResultsErrorToMergeNotificationMessage.xsl");

        Document resultDocument = transform(inputDocument, xsltDocument);
        //XmlUtils.printNode(resultDocument);

        Node errorNode = XmlUtils.xPathNodeSearch(resultDocument, "emrm-exc:EntityMergeResultMessage/emrm-exc:SearchResultsMetadataCollection/srm:SearchResultsMetadata/srer:SearchRequestError");
        assertNotNull(errorNode);
        assertEquals("This is an error from Local.", XmlUtils.xPathStringSearch(errorNode, "srer:ErrorText"));
        assertEquals("Local Firearms", XmlUtils.xPathStringSearch(errorNode, "intel:SystemName"));

    }

    private Document transform(Document inputDocument, Document xsltDocument) throws TransformerFactoryConfigurationError, Exception {

        Transformer t = TransformerFactory.newInstance().newTransformer(new DOMSource(xsltDocument));
        t.setParameter("erAttributeParametersFilePath", "src/main/resources/xslt/erConfig/example/PersonSearchAttributeParameters.xml");
        t.setParameter("entityResolutionRecordThreshold", "150");

        DOMResult result = new DOMResult();
        t.transform(new DOMSource(inputDocument), result);
        Document resultDocument = (Document) result.getNode();
        return resultDocument;
    }

    private Document parse(String filePath) throws Exception {
        PathMatchingResourcePatternResolver resolver = new PathMatchingResourcePatternResolver();
        Resource r = resolver.getResource(filePath);
        Document d = documentBuilder.parse(r.getInputStream());
        return d;
    }

}
