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
package org.ojbc.util.xml;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.Iterator;

import javax.xml.namespace.NamespaceContext;

import org.junit.Test;

public class DeclarationNamespaceContextTest {
    
    @Test
    public void test() throws Exception {
        
        String declaration = "xmlns:xsl=\"http://www.w3.org/1999/XSL/Transform\" xmlns:ir=\"http://ojbc.org/IEPD/Exchange/IncidentReport/1.0\"" +
            "xmlns:nc=\"http://niem.gov/niem/niem-core/2.0\" xmlns:lexs=\"http://usdoj.gov/leisp/lexs/3.1\" xmlns:lexspd=\"http://usdoj.gov/leisp/lexs/publishdiscover/3.1\"";
        NamespaceContext nc = new DeclarationNamespaceContext(declaration);
        assertEquals(nc.getNamespaceURI("xsl"), "http://www.w3.org/1999/XSL/Transform");
        assertEquals(nc.getPrefix("http://www.w3.org/1999/XSL/Transform"), "xsl");
        assertEquals(nc.getNamespaceURI("ir"), "http://ojbc.org/IEPD/Exchange/IncidentReport/1.0");
        assertEquals(nc.getPrefix("http://ojbc.org/IEPD/Exchange/IncidentReport/1.0"), "ir");
        Iterator it = nc.getPrefixes("http://www.w3.org/1999/XSL/Transform");
        assertTrue(it.hasNext());
        String prefix = (String) it.next();
        assertEquals("xsl", prefix);
        assertFalse(it.hasNext());
    }

}
