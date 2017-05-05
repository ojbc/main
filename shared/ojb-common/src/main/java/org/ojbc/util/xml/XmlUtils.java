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
package org.ojbc.util.xml;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.StringReader;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.List;

import javax.xml.XMLConstants;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;
import javax.xml.validation.Validator;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathFactory;

import org.apache.commons.lang.StringUtils;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatterBuilder;
import org.joda.time.format.DateTimeParser;
import org.joda.time.format.ISODateTimeFormat;
import org.w3c.dom.Attr;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.w3c.dom.ls.LSResourceResolver;
import org.xml.sax.SAXException;

/**
 * XML Utility class. 
 * 
 * This class provides assistance with Xpath searching, document creation and NIEM document validation.
 * It works with the namespaces and prefixes provided by OjbcNamespaceContext. 
 * 
 * When creating DOM documents, this class should always be used first if possible.
 * 
 * The test class provides examples on how to properly use the methods provided.
 * 
 */
public class XmlUtils {

    public static final OjbcNamespaceContext OJBC_NAMESPACE_CONTEXT = new OjbcNamespaceContext();
    
    public static final DateTime parseXmlDateTime(String dateTime) {
        if (dateTime == null || dateTime.trim().equals("")) {
            return null;
        }
        DateTimeParser[] parsers = new DateTimeParser[] {
            DateTimeFormat.forPattern("yyyy-MM-dd'T'HH:mm:ss").getParser(),
            ISODateTimeFormat.dateTimeNoMillis().withOffsetParsed().getParser(),
            DateTimeFormat.forPattern("yyyy-MM-dd'T'HH:mm:ss.SSS").getParser(),
            ISODateTimeFormat.dateTime().withOffsetParsed().getParser(),
        };
        DateTimeFormatterBuilder dateTimeFormatterBuilder = new DateTimeFormatterBuilder();
        dateTimeFormatterBuilder.append(null, parsers);
        return dateTimeFormatterBuilder.toFormatter().parseDateTime(dateTime);
    }
    
    /**
     * This method accepts an XML date string and will return a JodaTime object.
     * It will need to be updated to support timezones.
     * 
     * @param date
     * @return
     */
    public static final DateTime parseXmlDate(String date) {
        if (date == null || date.trim().equals("")) {
            return null;
        }
        DateTimeParser[] parsers = new DateTimeParser[] {
            DateTimeFormat.forPattern("yyyy-MM-dd").getParser(),
            ISODateTimeFormat.dateTimeNoMillis().withOffsetParsed().getParser(),
        };
        DateTimeFormatterBuilder dateTimeFormatterBuilder = new DateTimeFormatterBuilder();
        dateTimeFormatterBuilder.append(null, parsers);
        return dateTimeFormatterBuilder.toFormatter().parseDateTime(date);
    }
    
    
    /**
     * Compares two DOM nodes for (deep) equality.
     * @param n1
     * @param n2
     * @return whether the nodes are equal
     */
    public static final boolean compare(Node n1, Node n2) {
        boolean ret = true;
        ret &= n1.getLocalName().equals(n2.getLocalName());
        ret &= n1.getNamespaceURI().equals(n2.getNamespaceURI());
        String text = n1.getTextContent();
        if (text != null) {
            ret &= text.equals(n2.getTextContent());
        }
        NodeList children = n1.getChildNodes();
        ret &= (children.getLength() == n2.getChildNodes().getLength());
        for (int i = 0; ret && i < children.getLength(); i++) {
            final Node child = children.item(i);
            if (child instanceof Element) {
                ret &= compare(child, n2.getChildNodes().item(i));
            }
        }
        return ret;
    }

    /**
     * Print the specified XML DOM node to System.out
     * 
     * @param n
     *            the node to print
     * @throws Exception
     */
    public static final void printNode(Node n) throws Exception {
        printNode(n, System.out);
    }

    
    /**
     * Original intent was to represent root node as a string without 
     * any xml comments at the top 
     */
    public static String getRootNodeAsString(String fileClasspath) throws Exception{
    	
        File file = new File(fileClasspath);    	
    	Document doc = XmlUtils.parseFileToDocument(file);    	
    	Element rootElement = doc.getDocumentElement();    	
    	String sRoot = XmlUtils.getStringFromNode(rootElement);
    	
    	return sRoot;
    }
    
    public static String getStringFromNode(Node node) throws Exception{
    	
    	StringWriter writer = new StringWriter();
    	Transformer transformer = TransformerFactory.newInstance().newTransformer();
    	transformer.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "yes");
    	transformer.transform(new DOMSource(node), new StreamResult(writer));
    	String xml = writer.toString();
    	
    	return xml;
    }
    
    /**
     * Print the specified XML DOM node to the specified output stream
     * 
     * @param n
     *            the node to print
     * @param os
     *            the output stream to print to
     * @throws Exception
     */
    public static void printNode(Node n, OutputStream os) throws Exception {
        TransformerFactory tf = TransformerFactory.newInstance();
        Transformer t = tf.newTransformer();
        t.setOutputProperty(OutputKeys.INDENT, "yes");
        t.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "yes");
        t.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "4");
        t.transform(new DOMSource(n), new StreamResult(os));
    }

    /**
     * Search the context node for a node that matches the specified xpath
     * 
     * @param context
     *            the node that's the context for the xpath
     * @param xPath
     *            the xpath query
     * @return the matching node, or null if no match
     * @throws Exception
     */
    public static final Node xPathNodeSearch(Node context, String xPath) throws Exception {
        if (xPath == null)
        {
            return null;
        }
        XPath xpath = XPathFactory.newInstance().newXPath();
        xpath.setNamespaceContext(OJBC_NAMESPACE_CONTEXT);
        XPathExpression expression = xpath.compile(xPath);
        return (Node) expression.evaluate(context, XPathConstants.NODE);
    }
    
    /**
     * Search the context node for a String that matches the specified xpath
     * 
     * @param context
     *            the node that's the context for the xpath
     * @param xPath
     *            the xpath query
     * @return the matching string, or null if no match
     * @throws Exception
     */
    public static final String xPathStringSearch(Node context, String xPath) throws Exception {
        if (xPath == null)
        {
            return null;
        }
        XPath xpath = XPathFactory.newInstance().newXPath();
        xpath.setNamespaceContext(OJBC_NAMESPACE_CONTEXT);
        XPathExpression expression = xpath.compile(xPath);
        return StringUtils.trimToNull((String) expression.evaluate(context, XPathConstants.STRING));
    }
    
    /**
     * Search the context node for a node set that matches the specified xpath
     * @param context the node that's the context for the xpath
     * @param xPath the xpath query
     * @return the matching node, or null if no match
     * @throws Exception
     */
    public static final NodeList xPathNodeListSearch(Node context, String xPath) throws Exception {
        if (xPath == null)
        {
            return null;
        }
        XPath xpath = XPathFactory.newInstance().newXPath();
        xpath.setNamespaceContext(OJBC_NAMESPACE_CONTEXT);
        XPathExpression expression = xpath.compile(xPath);
        return (NodeList) expression.evaluate(context, XPathConstants.NODESET);
        
    }

    /**
     * Returns true if the node specified by the xpath exists in the context, and false otherwise
     * 
     * @param context
     *            the context node from which to apply the xpath
     * @param xPath
     *            the query
     * @return true if the specified node exists, false otherwise
     * @throws Exception
     */
    public static final boolean nodeExists(Node context, String xPath) throws Exception {
        return xPathNodeSearch(context, xPath) != null;
    }

    /**
     * Create a new element with the specified namespace and name, append it to the specified parent, and return it
     * 
     * @param parent
     *            the parent to contain the new element
     * @param ns
     *            the namespace URI of the new element
     * @param elementName
     *            the name of the new element
     * @return the new element
     */
    public static final Element appendElement(Element parent, String ns, String elementName) {
        Document doc = parent.getOwnerDocument();
        Element ret = doc.createElementNS(ns, elementName);
        parent.appendChild(ret);
        ret.setPrefix(OJBC_NAMESPACE_CONTEXT.getPrefix(ns));
        return ret;
    }
    
    /**
     * Add an attribute to the specified element
     * @param parent the element to which we add the attribute
     * @param ns the namespace of the attribute
     * @param attributeName the name of the attribute
     * @param value the value of the attribute
     * @return the attribute
     */
    public static final Attr addAttribute(Element parent, String ns, String attributeName, String value) {
        Document doc = parent.getOwnerDocument();
        Attr ret = doc.createAttributeNS(ns, attributeName);
        ret.setTextContent(value);
        ret.setPrefix(OJBC_NAMESPACE_CONTEXT.getPrefix(ns));
        parent.setAttributeNode(ret);
        return ret;
    }
    
    /**
     * if textValue is not blank, create a new element under the parent element with the namespace and 
     * elementName, then set the textContent of the element to the textValue. 
     * 
     * if textValue is blank,  do nothing. 
     * 
     * @param parent
     * @param namespace
     * @param elementName
     * @param textValue
     */
    public static final void appendTextElement(Element parent, String namespace,
			String elementName, String textValue) {
		if (StringUtils.isNotBlank(textValue)){
			Element personEthnicityText = 
					XmlUtils.appendElement(parent, namespace, elementName);
			personEthnicityText.setTextContent(textValue);
		}
	}


   /**
     * Create a new element with the specified namespace and name, insert it under the specified parent but before the specified sibling, and return it
     * 
     * @param parent
     *            the parent
     * @param sibling
     *            the parent's current child, in front of which the new element is to be inserted
     * @param ns
     *            the namespace URI of the new element
     * @param elementName
     *            the name of the new element
     * @return the new element
     */
    public static final Element insertElementBefore(Element parent, Element sibling, String ns, String elementName) {
        Document doc = parent.getOwnerDocument();
        Element ret = doc.createElementNS(ns, elementName);
        ret.setPrefix(OJBC_NAMESPACE_CONTEXT.getPrefix(ns));
        parent.insertBefore(ret, sibling);
        return ret;
    }

    /**
     * Validates a document against an IEPD, using the default IEPD resource resolver. See expanded method below for more detail.
     * 
     * @param niemSchemaContainerFolderName
     *            the name of the folder that contains all of the SSGT-generated schemas. Typically "NIEM_x.y" or "niem".
     * @param rootSchemaFileName
     *            the name of the document/exchange schema
     * @param d
     *            the document to validate
     * @param d
     *            the document to validate
     * @return the document that was validated
     * @throws Exception
     *             if the document is not valid
     */
    public static final Document validateInstance(String iepdRootPath, String niemSchemaContainerFolderName, String rootSchemaFileName, Document d) throws Exception {
    	
    	return validateInstance(iepdRootPath, niemSchemaContainerFolderName, rootSchemaFileName, new ArrayList<String>(), d);
    }
    
    /**
     * Validates a document against an IEPD, using the default IEPD resource resolver. See expanded method below for more detail.
     * 
     * @param niemSchemaContainerFolderName
     *            the name of the folder that contains all of the SSGT-generated schemas. Typically "NIEM_x.y" or "niem".
     * @param rootSchemaFileName
     *            the name of the document/exchange schema
     * @param additionalSchemaRelativePaths
     *            additional schema paths, within iepdRootPath
     * @param d
     *            the document to validate
     * @return the document that was validated
     * @throws Exception
     *             if the document is not valid
     */
    public static final Document validateInstance(String iepdRootPath, String niemSchemaContainerFolderName, String rootSchemaFileName, List<String> additionalSchemaRelativePaths, Document d) throws Exception {
        return validateInstance(iepdRootPath, rootSchemaFileName, additionalSchemaRelativePaths, d, new IEPDResourceResolver(niemSchemaContainerFolderName, iepdRootPath));
    }
    
    public static final Document validateInstanceWithAbsoluteClasspaths(String rootNodeXsdFilePath, 
    		List<String> supportingXsdDirPathList, Document xmlDoc) throws Exception {
    	return validateInstanceWithAbsoluteClasspaths(rootNodeXsdFilePath, supportingXsdDirPathList, new ArrayList<String>(), xmlDoc);
    }
    
    public static final Document validateInstanceWithAbsoluteClasspaths(String rootNodeXsdFilePath, 
    		List<String> supportingXsdDirPathList, List<String> additionalSchemaRelativePaths, Document xmlDoc) throws Exception {
    	    	    	    	
    	List<String> resourcePathList = new ArrayList<String>();
    	resourcePathList.add(rootNodeXsdFilePath);
    	resourcePathList.addAll(supportingXsdDirPathList);
    	    	    	
    	IEPDFullPathResourceResolver fullPathResolver = new IEPDFullPathResourceResolver(resourcePathList);
    	
        return validateInstance(rootNodeXsdFilePath, xmlDoc, fullPathResolver, additionalSchemaRelativePaths);
    }
    
    public static Document validateInstance(String rootXsdPath, Document docXmlToValidate, 
    		IEPDFullPathResourceResolver fullPathResolver) throws Exception {
    	return validateInstance(rootXsdPath, docXmlToValidate, fullPathResolver, new ArrayList<String>());
    }

    public static Document validateInstance(String rootXsdPath, Document docXmlToValidate, 
    		IEPDFullPathResourceResolver fullPathResolver, List<String> additionalSchemaRelativePaths) throws Exception {
    	
    	List<String> schemaPaths = new ArrayList<String>();
    	schemaPaths.add(rootXsdPath);
    	schemaPaths.addAll(additionalSchemaRelativePaths);
    	
        SchemaFactory schemaFactory = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);
        schemaFactory.setResourceResolver(fullPathResolver);
        
        Source[] sources = new Source[schemaPaths.size()];
        int i=0;
        for (String path : schemaPaths) {
        	sources[i++] = new StreamSource(XmlUtils.class.getClassLoader().getResourceAsStream(path));
        }
        
        Schema schema = schemaFactory.newSchema(sources);
        
        Validator v = schema.newValidator();
        
        try {
            v.validate(new DOMSource(docXmlToValidate));
            
        } catch (Exception e) {
            try {
                e.printStackTrace();
                System.err.println("FAILED Input Doc: \n");
                XmlUtils.printNode(docXmlToValidate);
            } catch (Exception e1) {
                e1.printStackTrace();
            }
            throw e;
        }
        return docXmlToValidate;    	    	
	}

    /**
     * Convenience method doesn't require a null param for the niem dir string
     */
    public static final Document validateInstance(String iepdRootPath, String rootSchemaFileName, 
    		Document doc, LSResourceResolver iepdResourceResolver)
            throws Exception {
    	
		if (!iepdRootPath.endsWith("/")) {
			iepdRootPath = iepdRootPath + "/";
		}
		String fullRootSchemaPath = iepdRootPath + rootSchemaFileName;

		List<String> schemaPaths = new ArrayList<String>();
		schemaPaths.add(fullRootSchemaPath);

		validateInstance(doc, iepdResourceResolver, schemaPaths);
		
		return doc;
    }
    
    /**
     * Convenience method doesn't require a null param for the niem dir string
     */
    public static final Document validateInstance(String iepdRootPath, String rootSchemaFileName, 
    		List<String> additionalSchemaRelativePaths, Document d, LSResourceResolver iepdResourceResolver)
            throws Exception {
    	
    	if (!iepdRootPath.endsWith("/")) {
            iepdRootPath = iepdRootPath + "/";
        }
        String fullRootSchemaPath = iepdRootPath + rootSchemaFileName;
        
        List<String> schemaPaths = new ArrayList<String>();
    	schemaPaths.add(fullRootSchemaPath);
    	schemaPaths.addAll(additionalSchemaRelativePaths);
        
        validateInstance(d, iepdResourceResolver, schemaPaths);
    	
    	return d;  	
    }
    

	/**
     * Validate a document against an IEPD. Note that this does not require the xsi namespace location attributes to be set in the instance.
     * 
     * @param schemaPathList
     *            the paths to all schemas necessary to validate the instance; this is the equivalent of specifying these schemas in an xsi:schemaLocation attribute in the instance
     *                       
     * @param Never_Used_TODO_Remove 
     * 
     *            
     * @param rootSchemaFileName
     *            the name of the document/exchange schema
     * @param d
     *            the document to validate
     * @param iepdResourceResolver
     *            the resource resolver to use
     * @return the document that was validated
     * @throws Exception
     *             if the document is not valid
     */
	public static void validateInstance(Document d, LSResourceResolver iepdResourceResolver, List<String> schemaPathList) throws SAXException, Exception {
		SchemaFactory schemaFactory = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);
        schemaFactory.setResourceResolver(iepdResourceResolver);
                
        List<Source> sourceList = new ArrayList<Source>();
        
        for (String schemaPath : schemaPathList) {
        	
        	InputStream schemaInStream = XmlUtils.class.getClassLoader().getResourceAsStream(schemaPath);
        	
        	StreamSource schemaStreamSource = new StreamSource(schemaInStream);
        	
        	sourceList.add(schemaStreamSource);        	
        }
        
        Source[] schemaSourcesArray = sourceList.toArray(new Source[]{});
        
        try {        	
            Schema schema = schemaFactory.newSchema(schemaSourcesArray);        
            
            Validator validator = schema.newValidator();
            
            validator.validate(new DOMSource(d));
            
        } catch (Exception e) {
            try {
                e.printStackTrace();
                System.err.println("Input document:");
                XmlUtils.printNode(d);
            } catch (Exception e1) {
                e1.printStackTrace();
            }
            throw e;
        }
	}

    /**
     * This method will validate a non-niem xml instance.
     * 
     * @param xsdPath - this is a relative path to an xsd, typically in OJB_Utilies or in the enclosing project
     * @param xml - This is the XML document as a string
     * @throws Exception - typically a validation exception or a file path exception
     */
    
    public static void validateInstanceNonNIEMXsd(String xsdPath, String xml) throws Exception{
        
        SchemaFactory factory = 
                SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);
        Schema schema = factory.newSchema(new File(xsdPath));
        Validator validator = schema.newValidator();
        validator.validate(new StreamSource(new StringReader(xml)));
    }
    
    /**
     * Read the contents of the specified file into a DOM document
     * @param f the input XML file
     * @return the document
     * @throws Exception
     */
    public static final Document parseFileToDocument(File f) throws Exception {
        DocumentBuilderFactory docBuilderFact = DocumentBuilderFactory.newInstance();
        docBuilderFact.setNamespaceAware(true);
        DocumentBuilder docBuilder = docBuilderFact.newDocumentBuilder();
        Document document = docBuilder.parse(f);
        return document;
    }
    
    public static Source getDomSourceIgnoringDtd(String xmlContents) throws ParserConfigurationException, SAXException, IOException {
		
		InputStream inputStream = new ByteArrayInputStream(xmlContents.getBytes());
		
		Source source = getDomSourceIgnoringDtd(inputStream);

		return source;
	}	
	
	public static Source getDomSourceIgnoringDtd(File file) throws FileNotFoundException, SAXException, 
		IOException, ParserConfigurationException{		
		
		InputStream inputStream = new FileInputStream(file);
		
		Source source = getDomSourceIgnoringDtd(inputStream);
		
		return source;
	}
		
	public static Source getDomSourceIgnoringDtd(InputStream inputStream) throws ParserConfigurationException, 
		SAXException, IOException {		
		
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		
		// stop the loading of DTD files
		factory.setValidating(false);
		factory.setNamespaceAware(true);
		factory.setFeature("http://xml.org/sax/features/validation", false);
		factory.setFeature("http://apache.org/xml/features/nonvalidating/load-dtd-grammar", false);
		factory.setFeature("http://apache.org/xml/features/nonvalidating/load-external-dtd", false);
		
		DocumentBuilder docbuilder = factory.newDocumentBuilder();
						
		Document doc = docbuilder.parse(inputStream);
		
		Source domSource = new DOMSource(doc.getDocumentElement());
		
		return domSource;
	}
	
}
