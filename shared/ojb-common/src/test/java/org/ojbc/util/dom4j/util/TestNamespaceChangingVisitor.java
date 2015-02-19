/**
 * 
 */
package org.ojbc.util.dom4j.util;

import static org.junit.Assert.assertFalse;

import org.dom4j.Document;
import org.dom4j.Namespace;
import org.dom4j.Visitor;
import org.dom4j.io.SAXReader;
import org.junit.Test;
import org.ojbc.util.xml.NamespaceChangingVisitor;

/**
 * @author yogeshchawla
 *
 */
public class TestNamespaceChangingVisitor {


	/**
	 * Test method for {@link org.ojbc.dom4j.util.NamespaceChangingVisitor#visit(org.dom4j.Element)}.
	 */
	@Test
	public void testVisitElement() throws Exception
	{
	    Document doc = new SAXReader().read("src/test/resources/xml/Person_Query_Results_-_All-instance.xml");
	    Namespace oldNs = Namespace.get("http://niem.gov/niem/domains/jxdm/4.0");
	    Namespace newNs = Namespace.get("jxdm41", "http://niem.gov/niem/domains/jxdm/4.1");
	    Visitor visitor = new NamespaceChangingVisitor(oldNs, newNs);
	    doc.accept(visitor);
	    assertFalse(doc.asXML().contains("http://niem.gov/niem/domains/jxdm/4.0"));	
	    
	}

}
