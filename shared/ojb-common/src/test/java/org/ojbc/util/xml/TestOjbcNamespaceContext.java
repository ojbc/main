package org.ojbc.util.xml;

import static org.junit.Assert.*;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.junit.Before;
import org.junit.Test;
import org.w3c.dom.Attr;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;

public class TestOjbcNamespaceContext {
    
    private static final Log log = LogFactory.getLog(TestOjbcNamespaceContext.class);
    
    private DocumentBuilder db;

    @Before
    public void setUp() throws ParserConfigurationException {
        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        dbf.setNamespaceAware(true);
        db = dbf.newDocumentBuilder();
    }
    
    @Test
    public void testRootNamespacePopulation() throws Exception {
        
        Document d = db.newDocument();
        Element root = d.createElementNS(OjbcNamespaceContext.NS_PERSON_SEARCH_RESULTS_EXT, "e1");
        root.setPrefix(OjbcNamespaceContext.NS_PREFIX_PERSON_SEARCH_RESULTS_EXT);
        d.appendChild(root);
        Element child = XmlUtils.appendElement(root, OjbcNamespaceContext.NS_JXDM_41, "foo");
        XmlUtils.addAttribute(child, OjbcNamespaceContext.NS_STRUCTURES, "id", "I1");
        //XmlUtils.printNode(d);
        XmlUtils.OJBC_NAMESPACE_CONTEXT.populateRootNamespaceDeclarations(root);
        //XmlUtils.printNode(d);
        assertNotNull(XmlUtils.xPathNodeSearch(root, "namespace::xmlns:" + OjbcNamespaceContext.NS_PREFIX_PERSON_SEARCH_RESULTS_EXT));
        assertEquals(root.getAttributeNS("http://www.w3.org/2000/xmlns/", OjbcNamespaceContext.NS_PREFIX_PERSON_SEARCH_RESULTS_EXT), OjbcNamespaceContext.NS_PERSON_SEARCH_RESULTS_EXT);
        assertNotNull(XmlUtils.xPathNodeSearch(root, "namespace::xmlns:" + OjbcNamespaceContext.NS_PREFIX_STRUCTURES));
        assertEquals(root.getAttributeNS("http://www.w3.org/2000/xmlns/", OjbcNamespaceContext.NS_PREFIX_STRUCTURES), OjbcNamespaceContext.NS_STRUCTURES);
        assertNull(XmlUtils.xPathNodeSearch(root, "namespace::xmlns:" + OjbcNamespaceContext.NS_PREFIX_ANSI_NIST));
        assertNotNull(XmlUtils.xPathNodeSearch(child, "namespace::xmlns:" + OjbcNamespaceContext.NS_PREFIX_JXDM_41));
        assertNull(child.getAttributeNodeNS("http://www.w3.org/2000/xmlns/", OjbcNamespaceContext.NS_PREFIX_JXDM_41));
        assertNull(child.getAttributeNodeNS("http://www.w3.org/2000/xmlns/", OjbcNamespaceContext.NS_PREFIX_STRUCTURES));
        
    }

    @Test
    public void testRootNamespacePopulationWithEmbeddedNamespaceDeclarations() throws Exception {
        
        Document d = db.newDocument();
        Element root = d.createElementNS(OjbcNamespaceContext.NS_PERSON_SEARCH_RESULTS_EXT, "e1");
        root.setPrefix(OjbcNamespaceContext.NS_PREFIX_PERSON_SEARCH_RESULTS_EXT);
        d.appendChild(root);
        Element child = XmlUtils.appendElement(root, OjbcNamespaceContext.NS_JXDM_41, "foo");
        XmlUtils.addAttribute(child, OjbcNamespaceContext.NS_STRUCTURES, "id", "I1");
        child = XmlUtils.appendElement(child, OjbcNamespaceContext.NS_JXDM_41, "foo2");
        XmlUtils.addAttribute(child, OjbcNamespaceContext.NS_STRUCTURES, "id", "I2");
        XmlUtils.OJBC_NAMESPACE_CONTEXT.populateRootNamespaceDeclarations(child);
        //XmlUtils.printNode(d);
        XmlUtils.OJBC_NAMESPACE_CONTEXT.populateRootNamespaceDeclarations(root);
        //XmlUtils.printNode(d);
        NamedNodeMap attrs = root.getAttributes();
        for (int i=0;i < attrs.getLength();i++) {
        	assertFalse("null".equals(attrs.item(i).getLocalName()) && "xmlns".equals(attrs.item(i).getPrefix()));
        }
        
    }
}
