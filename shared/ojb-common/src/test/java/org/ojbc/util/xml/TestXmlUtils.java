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
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.io.File;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.joda.time.DateTime;
import org.joda.time.DateTimeConstants;
import org.joda.time.DateTimeZone;
import org.junit.Before;
import org.junit.Test;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class TestXmlUtils {

    private static final Log log = LogFactory.getLog(TestXmlUtils.class);

    private DocumentBuilder db;

    @Before
    public void setUp() throws ParserConfigurationException {
        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        dbf.setNamespaceAware(true);
        db = dbf.newDocumentBuilder();
    }

    @Test
    public void testParseXmlDateTime() {

        assertNull(XmlUtils.parseXmlDateTime(null));
        assertNull(XmlUtils.parseXmlDateTime(""));

        // note: this test could break under unusual circumstances, such as:
        // 1. Running it in a timezone with a positive offset to GMT greater than 12 (e.g., Kiribati, which is UTC+13:00)
        // 2. Running it in a timezone that has a fractional hour offset (e.g., India, which is UTC+5:30)
        // 3. Running it at exactly the time of change to or from DST
        // If you run it at normal times of normal days in most timezones--including all timezones in the US--it should work fine.

        assertDateValues(XmlUtils.parseXmlDateTime("2014-12-28T12:10:20"), 2014, 12, 28, 12, 10, 20, 0);
        assertDateValues(XmlUtils.parseXmlDateTime("2014-1-28T12:10:20"), 2014, 1, 28, 12, 10, 20, 0);
        assertDateValues(XmlUtils.parseXmlDateTime("2014-01-28T12:10:20"), 2014, 1, 28, 12, 10, 20, 0);
        assertDateValues(XmlUtils.parseXmlDateTime("2014-12-1T12:10:20"), 2014, 12, 1, 12, 10, 20, 0);
        assertDateValues(XmlUtils.parseXmlDateTime("2014-12-01T12:10:20"), 2014, 12, 1, 12, 10, 20, 0);
        assertDateValues(XmlUtils.parseXmlDateTime("2014-12-01T9:10:20"), 2014, 12, 1, 9, 10, 20, 0);
        assertDateValues(XmlUtils.parseXmlDateTime("2014-12-01T12:1:20"), 2014, 12, 1, 12, 1, 20, 0);
        assertDateValues(XmlUtils.parseXmlDateTime("2014-12-01T12:10:2"), 2014, 12, 1, 12, 10, 2, 0);
        assertDateValues(XmlUtils.parseXmlDateTime("2014-12-28T12:10:20.001"), 2014, 12, 28, 12, 10, 20, 1);
        assertDateValues(XmlUtils.parseXmlDateTime("2014-12-28T12:10:20.011"), 2014, 12, 28, 12, 10, 20, 11);
        assertDateValues(XmlUtils.parseXmlDateTime("2014-12-28T12:10:20.111"), 2014, 12, 28, 12, 10, 20, 111);

        DateTime dt = XmlUtils.parseXmlDateTime("2014-12-28T12:10:20Z");
        assertDateValues(dt, 2014, 12, 28, applyOffsetToHour(dt, 12), 10, 20, 0);
        dt = XmlUtils.parseXmlDateTime("2014-1-28T12:10:20Z");
        assertDateValues(dt, 2014, 1, 28, applyOffsetToHour(dt, 12), 10, 20, 0);
        dt = XmlUtils.parseXmlDateTime("2014-01-28T12:10:20Z");
        assertDateValues(dt, 2014, 1, 28, applyOffsetToHour(dt, 12), 10, 20, 0);
        dt = XmlUtils.parseXmlDateTime("2014-12-1T12:10:20Z");
        assertDateValues(dt, 2014, 12, 1, applyOffsetToHour(dt, 12), 10, 20, 0);
        dt = XmlUtils.parseXmlDateTime("2014-12-01T12:10:20Z");
        assertDateValues(dt, 2014, 12, 1, applyOffsetToHour(dt, 12), 10, 20, 0);
        dt = XmlUtils.parseXmlDateTime("2014-12-01T12:10:20.001Z");
        assertDateValues(dt, 2014, 12, 1, applyOffsetToHour(dt, 12), 10, 20, 1);

        dt = XmlUtils.parseXmlDateTime("2014-12-28T12:10:20-05:00");
        assertDateValues(dt, 2014, 12, 28, applyOffsetToHour(dt, 12) + 5, 10, 20, 0);
        dt = XmlUtils.parseXmlDateTime("2014-12-28T12:10:20.001-05:00");
        assertDateValues(dt, 2014, 12, 28, applyOffsetToHour(dt, 12) + 5, 10, 20, 1);

        // shouldn't ever have this, but just in case...
        dt = XmlUtils.parseXmlDateTime("-44-3-15T12:10:20.001Z"); // not sure if Caesar died at exactly this time, but...
        assertEquals(DateTimeConstants.BCE, dt.getEra());
        
        Exception ee = null;
        
        try {
            XmlUtils.parseXmlDateTime("invalid");
        } catch (Exception e) {
            ee = e;
        }
        
        assertNotNull(ee);
        assertTrue(ee instanceof IllegalArgumentException);
        assertTrue(ee.getMessage().contains("invalid"));

    }

    @Test
    public void testParseXmlDate() {

        assertNull(XmlUtils.parseXmlDate(null));
        assertNull(XmlUtils.parseXmlDate(""));

        assertDateValues(XmlUtils.parseXmlDate("2014-12-28"), 2014, 12, 28, 0, 0, 0, 0);

        Exception ee = null;
        
        try {
            XmlUtils.parseXmlDateTime("invalid");
        } catch (Exception e) {
            ee = e;
        }
        
        assertNotNull(ee);
        assertTrue(ee instanceof IllegalArgumentException);
        assertTrue(ee.getMessage().contains("invalid"));

    }

    
    private void assertDateValues(DateTime dt, int year, int month, int day, int hour, int minute, int second, int millis) {
        assertEquals(year, dt.getYear());
        assertEquals(month, dt.getMonthOfYear());
        assertEquals(day, dt.getDayOfMonth());
        assertEquals(hour, dt.getHourOfDay());
        assertEquals(minute, dt.getMinuteOfHour());
        assertEquals(second, dt.getSecondOfMinute());
        assertEquals(millis, dt.getMillisOfSecond());
        assertEquals(DateTimeConstants.CE, dt.getEra());
    }

    private int applyOffsetToHour(DateTime dt, int baseHour) {
        int hour = baseHour;
        DateTimeZone tz = DateTimeZone.getDefault();
        int localHourOffset = tz.getOffset(dt) / (60 * 60 * 1000);
        hour = baseHour + localHourOffset;
        hour += tz.isStandardOffset(dt.getMillis()) ? 0 : 1;
        return hour;
    }

    @Test
    public void testParseFile() throws Exception {
        Document d = XmlUtils.parseFileToDocument(new File("src/test/resources/xml/Person_Query_Results_-_All-instance.xml"));
        assertNotNull(d);
    }

    @Test
    public void testCompare() throws Exception {
        Document d = db.newDocument();
        Element e1 = d.createElementNS(OjbcNamespaceContext.NS_PERSON_SEARCH_RESULTS_EXT, "e1");
        d.appendChild(e1);
        Element e2 = d.createElementNS(OjbcNamespaceContext.NS_PERSON_SEARCH_RESULTS_EXT, "e2");
        e1.appendChild(e2);
        Element e = d.createElementNS(OjbcNamespaceContext.NS_PERSON_SEARCH_RESULTS_EXT, "e11");
        e1.appendChild(e);
        e.setTextContent("Text");
        Document d2 = db.newDocument();
        e1 = d2.createElementNS(OjbcNamespaceContext.NS_PERSON_SEARCH_RESULTS_EXT, "e1");
        d2.appendChild(e1);
        e2 = d2.createElementNS(OjbcNamespaceContext.NS_PERSON_SEARCH_RESULTS_EXT, "e2");
        e1.appendChild(e2);
        e = d2.createElementNS(OjbcNamespaceContext.NS_PERSON_SEARCH_RESULTS_EXT, "e11");
        e1.appendChild(e);
        e.setTextContent("Text");
        assertTrue(XmlUtils.compare(d.getDocumentElement(), d2.getDocumentElement()));
        e.setTextContent("New Text");
        assertFalse(XmlUtils.compare(d.getDocumentElement(), d2.getDocumentElement()));
        e.setTextContent("Text");
        assertTrue(XmlUtils.compare(d.getDocumentElement(), d2.getDocumentElement()));
        d2.renameNode(e, OjbcNamespaceContext.NS_PERSON_SEARCH_RESULTS_EXT, "newname");
        assertFalse(XmlUtils.compare(d.getDocumentElement(), d2.getDocumentElement()));
        d2.renameNode(e, OjbcNamespaceContext.NS_PERSON_SEARCH_RESULTS_EXT, "e11");
        assertTrue(XmlUtils.compare(d.getDocumentElement(), d2.getDocumentElement()));
        e = d2.createElementNS(OjbcNamespaceContext.NS_PERSON_SEARCH_RESULTS_EXT, "e12");
        e1.appendChild(e);
        assertFalse(XmlUtils.compare(d.getDocumentElement(), d2.getDocumentElement()));

    }

    @Test
    public void testNodeSearch() throws Exception {
        Document d = db.newDocument();
        Element e1 = d.createElementNS(OjbcNamespaceContext.NS_PERSON_SEARCH_RESULTS_EXT, "e1");
        d.appendChild(e1);
        Element e2 = d.createElementNS(OjbcNamespaceContext.NS_PERSON_SEARCH_RESULTS_EXT, "e2");
        e1.appendChild(e2);
        Node n = XmlUtils.xPathNodeSearch(d, OjbcNamespaceContext.NS_PREFIX_PERSON_SEARCH_RESULTS_EXT + ":e1");
        assertNotNull(n);
        assertTrue(n instanceof Element);
        Element ee = (Element) n;
        assertEquals(ee.getLocalName(), "e1");
        assertEquals(ee.getNamespaceURI(), OjbcNamespaceContext.NS_PERSON_SEARCH_RESULTS_EXT);
    }

    @Test
    public void testNodeExists() throws Exception {
        Document d = db.newDocument();
        Element e1 = d.createElementNS(OjbcNamespaceContext.NS_PERSON_SEARCH_RESULTS_EXT, "e1");
        d.appendChild(e1);
        Element e2 = d.createElementNS(OjbcNamespaceContext.NS_PERSON_SEARCH_RESULTS_EXT, "e2");
        e1.appendChild(e2);
        assertTrue(XmlUtils.nodeExists(d, OjbcNamespaceContext.NS_PREFIX_PERSON_SEARCH_RESULTS_EXT + ":e1"));
        assertFalse(XmlUtils.nodeExists(d, OjbcNamespaceContext.NS_PREFIX_PERSON_SEARCH_RESULTS_EXT + ":ex"));
    }

    @Test
    public void testNodeListSearch() throws Exception {
        Document d = db.newDocument();
        Element e1 = d.createElementNS(OjbcNamespaceContext.NS_PERSON_SEARCH_RESULTS_EXT, "e1");
        d.appendChild(e1);
        Element e2 = d.createElementNS(OjbcNamespaceContext.NS_PERSON_SEARCH_RESULTS_EXT, "e2");
        e1.appendChild(e2);
        Element e3 = d.createElementNS(OjbcNamespaceContext.NS_PERSON_SEARCH_RESULTS_EXT, "e2");
        e1.appendChild(e3);
        NodeList nl = XmlUtils.xPathNodeListSearch(d, OjbcNamespaceContext.NS_PREFIX_PERSON_SEARCH_RESULTS_EXT + ":e1");
        assertEquals(1, nl.getLength());
        Node nn = nl.item(0);
        nl = XmlUtils.xPathNodeListSearch(nn, OjbcNamespaceContext.NS_PREFIX_PERSON_SEARCH_RESULTS_EXT + ":e2");
        assertEquals(2, nl.getLength());
    }

    @Test
    public void testAppendElement() throws Exception {
        Document d = db.newDocument();
        Element e1 = d.createElementNS(OjbcNamespaceContext.NS_PERSON_SEARCH_RESULTS_EXT, "e1");
        d.appendChild(e1);
        XmlUtils.appendElement(e1, OjbcNamespaceContext.NS_PERSON_SEARCH_RESULTS_EXT, "e2");
        Node n = XmlUtils.xPathNodeSearch(e1, OjbcNamespaceContext.NS_PREFIX_PERSON_SEARCH_RESULTS_EXT + ":e2");
        assertNotNull(n);
        assertTrue(n instanceof Element);
        Element ee = (Element) n;
        assertEquals(ee.getLocalName(), "e2");
        assertEquals(ee.getNamespaceURI(), OjbcNamespaceContext.NS_PERSON_SEARCH_RESULTS_EXT);
    }

    @Test
    public void testInsertElement() throws Exception {
        Document d = db.newDocument();
        Element e1 = d.createElementNS(OjbcNamespaceContext.NS_PERSON_SEARCH_RESULTS_EXT, "e1");
        d.appendChild(e1);
        Element e2 = d.createElementNS(OjbcNamespaceContext.NS_PERSON_SEARCH_RESULTS_EXT, "e22");
        e1.appendChild(e2);
        e2 = XmlUtils.insertElementBefore(e1, e2, OjbcNamespaceContext.NS_PERSON_SEARCH_RESULTS_EXT, "e21");
        NodeList nodeList = e1.getElementsByTagNameNS(OjbcNamespaceContext.NS_PERSON_SEARCH_RESULTS_EXT, "*");
        assertEquals(2, nodeList.getLength());
        assertEquals("e21", nodeList.item(0).getLocalName());
        assertEquals("e22", nodeList.item(1).getLocalName());
    }

    @Test
    public void testAddAttribute() throws Exception {
        Document d = db.newDocument();
        Element e1 = d.createElementNS(OjbcNamespaceContext.NS_PERSON_SEARCH_RESULTS_EXT, "e1");
        d.appendChild(e1);
        XmlUtils.addAttribute(e1, "foo", "a", "baz");
        NamedNodeMap aa = e1.getAttributes();
        assertEquals(1, aa.getLength());
        Node n = aa.getNamedItemNS("foo", "a");
        assertNotNull(n);
        assertEquals("baz", n.getNodeValue());
    }

    @Test
    public void testValidateInstance() throws Exception {
        Document instance = db.parse("src/test/resources/test-iepd/Criminal_History-IEPD/xml/Criminal_History.xml");
        Exception ee = null;
        try {
            XmlUtils.validateInstance("test-iepd/Criminal_History-IEPD/xsd/", "NIEM_2.1", "Criminal_History.xsd", instance);
        } catch (Exception e) {
            ee = e;
            e.printStackTrace();
        }
        assertNull(ee);

    }

}
