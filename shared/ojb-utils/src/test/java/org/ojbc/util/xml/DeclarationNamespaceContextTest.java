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
