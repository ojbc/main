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

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.junit.Before;
import org.junit.Test;
import org.ojbc.util.xml.XmlUtils;
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
    public void vehicleResponseTransform() throws Exception {

    	Document inputDocument = parse("xmlInstances/entityResolution/vehicleSearchResults.xml");
        Document xsltDocument = parse("xslt/VehicleSearchResultsToEntityResolutionRequest.xsl");

        Document resultDocument = transform(inputDocument, xsltDocument);
        //XmlUtils.printNode(inputDocument);

        Node requestNode = XmlUtils.xPathNodeSearch(resultDocument, "em-req-exc:EntityMergeRequestMessage");
        assertNotNull(requestNode);

        NodeList attributeParameterNodeList = XmlUtils.xPathNodeListSearch(requestNode, "em-req-exc:MergeParameters/er-ext:AttributeParameters/er-ext:AttributeParameter");
        assertEquals(2, attributeParameterNodeList.getLength());

        NodeList entityNodeList = XmlUtils.xPathNodeListSearch(requestNode, "em-req-exc:MergeParameters/er-ext:EntityContainer/er-ext:Entity");
        int length = entityNodeList.getLength();
        assertEquals(2, length);

        for (int i = 0; i < length; i++) {
            Node entityNode = entityNodeList.item(i);
            assertTrue(XmlUtils.nodeExists(entityNode, "@s:id"));
            assertTrue(XmlUtils.nodeExists(entityNode, "vsres:VehicleSearchResult/vsres:SourceSystemNameText/text()"));
            assertTrue(XmlUtils.nodeExists(entityNode, "vsres:VehicleSearchResult/intel:SystemIdentifier/nc:IdentificationID/text()"));
            assertTrue(XmlUtils.nodeExists(entityNode, "vsres:VehicleSearchResult/intel:SystemIdentifier/intel:SystemName/text()"));
           
            Node iXmlInDocVsrNode = XmlUtils.xPathNodeSearch(inputDocument, "/OJBAggregateResponseWrapper/vsres-exch:VehicleSearchResults/vsres:VehicleSearchResult[" + (i + 1) + "]");
            
            Node iVsrNode =  XmlUtils.xPathNodeSearch(entityNode, "vsres:VehicleSearchResult");
                        
             XmlUtils.compare(iVsrNode, iXmlInDocVsrNode);
        }

    }	


	
	@Test
	public void emptyResponseTransform() throws Exception{
        Document inputDocument = parse("xmlInstances/entityResolution/noResults_error.xml");
        Document xsltDocument = parse("xslt/SearchResultsErrorToMergeNotificationMessage.xsl");

        Document resultDocument = transform(inputDocument, xsltDocument);
        XmlUtils.printNode(resultDocument);

        Node errorNode = XmlUtils.xPathNodeSearch(resultDocument, "emrm-exc:EntityMergeResultMessage/emrm-exc:SearchResultsMetadataCollection/srm:SearchResultsMetadata/srer:SearchErrors");
        assertNotNull(errorNode);
        
        assertEquals("75660", XmlUtils.xPathStringSearch(errorNode, "srer:SearchResultsExceedThresholdError/srer:SearchResultsRecordCount"));
        assertEquals("VIBRS RMS", XmlUtils.xPathStringSearch(errorNode, "intel:SystemName"));

	}

    private Document transform(Document inputDocument, Document xsltDocument) throws TransformerFactoryConfigurationError, Exception {

        Transformer t = TransformerFactory.newInstance().newTransformer(new DOMSource(xsltDocument));
        t.setParameter("erAttributeParametersFilePath", "src/main/resources/xslt/erConfig/example/VehicleSearchAttributeParameters.xml");
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
