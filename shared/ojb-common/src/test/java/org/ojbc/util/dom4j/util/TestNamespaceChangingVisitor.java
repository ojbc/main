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
