package org.ojbc.xslt;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Map;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMResult;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.sax.SAXSource;
import javax.xml.transform.stream.StreamSource;

import org.apache.commons.io.FileUtils;
import org.custommonkey.xmlunit.DetailedDiff;
import org.custommonkey.xmlunit.Diff;
import org.custommonkey.xmlunit.Difference;
import org.custommonkey.xmlunit.XMLUnit;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.ojbc.util.xml.XmlUtils;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

public class IncidentReportingTransformerServiceTest {

	XsltTransformerService unit;
	
	private Document incidentComplainantDoc;
	private Document incidentDateRangeDateTimeDoc;
	private Document incidentDateTimeDoc;
	private Document incidentDateRangeDateDoc;
	private Document incidentDateDoc;
	private Document incidentOffenseDoc;
	private XsltTransformerService xsltTransformer;
	
	private Source xsltSource;

	@Before
	public void setup() throws Exception{
		unit = new XsltTransformerService();
		
		//Tell XML Unit to ignore whitespace between elements and within elements
		XMLUnit.setIgnoreWhitespace(true);
		XMLUnit.setNormalizeWhitespace(true);
    	XMLUnit.setIgnoreAttributeOrder(true);
    	XMLUnit.setIgnoreComments(true);
    	xsltTransformer = new XsltTransformerService();
		
		DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        dbf.setNamespaceAware(true);
        DocumentBuilder db = dbf.newDocumentBuilder();
		
		incidentComplainantDoc = db.parse(new File("src/test/resources/xmlInstances/incidentReport/IncidentReport-complainant.xml"));
		incidentOffenseDoc = db.parse(new File("src/test/resources/xmlInstances/incidentReport/IncidentReport_OffenseCode.xml"));
		incidentDateRangeDateTimeDoc = db.parse(new File("src/test/resources/xmlInstances/incidentReport/IncidentReport-DateRange-DateTime.xml"));
		incidentDateTimeDoc = db.parse(new File("src/test/resources/xmlInstances/incidentReport/IncidentReport-Date-DateTime.xml"));
		incidentDateRangeDateDoc = db.parse(new File("src/test/resources/xmlInstances/incidentReport/IncidentReport-DateRange-Date.xml"));
		incidentDateDoc = db.parse(new File("src/test/resources/xmlInstances/incidentReport/IncidentReport-Date-Date.xml"));
		xsltSource = new StreamSource(new File("src/main/resources/xslt/incidentReportToNotifications.xsl"));			
	}
	
	@Test
	public void incidentReportToArrestReportTransform() throws Exception{
		
		File inputFile = new File("src/test/resources/xmlInstances/incidentReport/IncidentReport-Multiple_Arrest_Subjects.xml");		
		String inputXml = FileUtils.readFileToString(inputFile);
		SAXSource inputSaxSource = createSource(inputXml);
		
		File expectedOutputFile = new File("src/test/resources/xmlInstances/output/arrestReports/IncidentReport-Multiple_Arrest_Subjects._Output.xml");
		String expectedXml = FileUtils.readFileToString(expectedOutputFile);		
		
		File xsltFile = new File("src/main/resources/xslt/incidentReportToArrestReport.xsl");
		StreamSource xsltSaxSource = new StreamSource(xsltFile);
		
		String actualTransformedResultXml = xsltTransformer.transform(inputSaxSource, xsltSaxSource, null);
				
		DetailedDiff detailedDiff = new DetailedDiff(XMLUnit.compareXML(expectedXml, actualTransformedResultXml));
        
		@SuppressWarnings("unchecked")
		List<Difference> differenceList = detailedDiff.getAllDifferences();
        
        Assert.assertEquals(detailedDiff.toString(), 0, differenceList.size());	
	}
	
	@Test
	public void incidentReportToArrestReportNoPrefixesTransform() throws Exception{
		
		File inputFile = new File("src/test/resources/xmlInstances/incidentReport/IncidentReport-no-prefixes.xml");		
		String inputXml = FileUtils.readFileToString(inputFile);
		SAXSource inputSaxSource = createSource(inputXml);
		
		File expectedOutputFile = new File("src/test/resources/xmlInstances/output/arrestReports/No-namespace-prefixes-output.xml");
		String expectedXml = FileUtils.readFileToString(expectedOutputFile);		
		
		File xsltFile = new File("src/main/resources/xslt/incidentReportToArrestReport.xsl");
		StreamSource xsltSaxSource = new StreamSource(xsltFile);
		
		String actualTransformedResultXml = xsltTransformer.transform(inputSaxSource, xsltSaxSource, null);
				
		DetailedDiff detailedDiff = new DetailedDiff(XMLUnit.compareXML(expectedXml, actualTransformedResultXml));
        
		@SuppressWarnings("unchecked")
		List<Difference> differenceList = detailedDiff.getAllDifferences();
        
        Assert.assertEquals(detailedDiff.toString(), 0, differenceList.size());	
	}
	
	
	@Test
	public void incidentReportToNotificationsTransform() throws Exception{
		
		File inputFile = new File("src/test/resources/xmlInstances/incidentReport/IncidentReport.xml");		
		String inputXml = FileUtils.readFileToString(inputFile);
		SAXSource inputSaxSource = createSource(inputXml);
		
		File expectedOutputFile = new File("src/test/resources/xmlInstances/output/notifications/wrappedNotifications.xml");
		String expectedXml = FileUtils.readFileToString(expectedOutputFile);		
		
		File xsltFile = new File("src/main/resources/xslt/incidentReportToNotifications.xsl");
		StreamSource xsltSaxSource = new StreamSource(xsltFile);
		
		String actualTransformedResultXml = xsltTransformer.transform(inputSaxSource, xsltSaxSource, null);
			
		DetailedDiff detailedDiff = new DetailedDiff(XMLUnit.compareXML(expectedXml, actualTransformedResultXml));
		
		@SuppressWarnings("unchecked")	
		List<Difference> differenceList = detailedDiff.getAllDifferences();
        
        Assert.assertEquals(detailedDiff.toString(), 0, differenceList.size());	
	}


	@Test
	public void incidentReportUpdateToNotificationsTransform() throws Exception{
		
		File inputFile = new File("src/test/resources/xmlInstances/incidentReport/IncidentReportUpdate.xml");		
		String inputXml = FileUtils.readFileToString(inputFile);
		SAXSource inputSaxSource = createSource(inputXml);
		
		File expectedOutputFile = new File("src/test/resources/xmlInstances/output/notifications/wrappedNotificationsUpdate.xml");
		String expectedXml = FileUtils.readFileToString(expectedOutputFile);		
		
		File xsltFile = new File("src/main/resources/xslt/incidentReportToNotifications.xsl");
		StreamSource xsltSaxSource = new StreamSource(xsltFile);
		
		String actualTransformedResultXml = xsltTransformer.transform(inputSaxSource, xsltSaxSource, null);
				
		DetailedDiff detailedDiff = new DetailedDiff(XMLUnit.compareXML(expectedXml, actualTransformedResultXml));
        
		@SuppressWarnings("unchecked")	
		List<Difference> differenceList = detailedDiff.getAllDifferences();
        
        Assert.assertEquals(detailedDiff.toString(), 0, differenceList.size());	
	}

	@Test
	public void removeStructuredPayloadTransform() throws Exception{
	
		File inputFile = new File("src/test/resources/xmlInstances/incidentReport/IncidentReport.xml");		
		String inputXml = FileUtils.readFileToString(inputFile);
		SAXSource inputSaxSource = createSource(inputXml);
		
		File expectedOutputFile = new File("src/test/resources/xmlInstances/output/structuredPayloads/IncidentReportNoCustomStructuredPayloads.xml");
		String expectedXml = FileUtils.readFileToString(expectedOutputFile);		
		
		File xsltFile = new File("src/main/resources/xslt/removeStructuredPayload.xslt");
		StreamSource xsltSaxSource = new StreamSource(xsltFile);
		
		String actualTransformedResultXml = xsltTransformer.transform(inputSaxSource, xsltSaxSource, null);
				
		DetailedDiff detailedDiff = new DetailedDiff(XMLUnit.compareXML(expectedXml, actualTransformedResultXml));
        
		@SuppressWarnings("unchecked")	
		List<Difference> differenceList = detailedDiff.getAllDifferences();
        
        Assert.assertEquals(detailedDiff.toString(), 0, differenceList.size());	
	}
	
	@Test
	public void testIncidentReportComplainantToNotifyTransform() throws Exception{
				
		Document notifyDocTransformed = transformIncidentReportToNotifyDoc(incidentComplainantDoc);
		
		Node notifyNode = XmlUtils.xPathNodeSearch(notifyDocTransformed, "notifications/wrapper/b-2:Notify");
		
		Node notifyMsgNode = XmlUtils.xPathNodeSearch(notifyNode, "b-2:NotificationMessage");
		
		Node subRefNode = XmlUtils.xPathNodeSearch(notifyMsgNode, "b-2:SubscriptionReference");
		
		assertNotNull(subRefNode);
		
		Node messageNode = XmlUtils.xPathNodeSearch(notifyMsgNode, "b-2:Message");
		
		assertNotNull(messageNode);
		
		Node topicNode = XmlUtils.xPathNodeSearch(notifyMsgNode, "b-2:Topic[@Dialect='http://docs.oasis-open.org/wsn/t-1/TopicExpression/Concrete']");
		
		String topicValue = topicNode.getTextContent();
		
		assertEquals("topics:person/incident", topicValue);

		Node ojbNotificationMessage = XmlUtils.xPathNodeSearch(messageNode, "notfm-exch:NotificationMessage");
		assertNotNull(ojbNotificationMessage);
		
		Node activityInvolvedPersonAssociation = XmlUtils.xPathNodeSearch(ojbNotificationMessage, "nc:ActivityInvolvedPersonAssociation");
		assertNotNull(activityInvolvedPersonAssociation);
		
		Node personInvolvement = XmlUtils.xPathNodeSearch(activityInvolvedPersonAssociation, "nc:PersonActivityInvolvementText");
		String personInvolvementValue = personInvolvement.getTextContent();
		assertEquals("Complainant", personInvolvementValue);
				
		XmlUtils.printNode(notifyDocTransformed);				
	}
	
	
	@Test
	public void testIncidentDateRangeDateTime() throws Exception{
				
		Document notifyDocTransformed = transformIncidentReportToNotifyDoc(incidentDateRangeDateTimeDoc);
		
		Node incidentNode = XmlUtils.xPathNodeSearch(notifyDocTransformed, "notifications/wrapper/b-2:Notify/b-2:NotificationMessage/b-2:Message/notfm-exch:NotificationMessage/notfm-ext:NotifyingIncident/jxdm41:Incident");
		assertNotNull(incidentNode);
		
		Node incidentDate = XmlUtils.xPathNodeSearch(incidentNode, "nc:ActivityDate");
		assertNotNull(incidentDate);
		
		Node incidentDateTime = XmlUtils.xPathNodeSearch(incidentDate, "nc:DateTime");
		
		String incidentDateTimeValue = incidentDateTime.getTextContent();
		
		assertEquals("2014-01-01T04:43:56Z", incidentDateTimeValue);
				
		XmlUtils.printNode(notifyDocTransformed);				
	}

	@Test
	public void testIncidentDateTime() throws Exception{
				
		Document notifyDocTransformed = transformIncidentReportToNotifyDoc(incidentDateTimeDoc);
		
		Node incidentNode = XmlUtils.xPathNodeSearch(notifyDocTransformed, "notifications/wrapper/b-2:Notify/b-2:NotificationMessage/b-2:Message/notfm-exch:NotificationMessage/notfm-ext:NotifyingIncident/jxdm41:Incident");
		assertNotNull(incidentNode);
		
		Node incidentDate = XmlUtils.xPathNodeSearch(incidentNode, "nc:ActivityDate");
		assertNotNull(incidentDate);
		
		Node incidentDateTime = XmlUtils.xPathNodeSearch(incidentDate, "nc:DateTime");
		String incidentDateTimeValue = incidentDateTime.getTextContent();
		
		assertEquals("2014-01-01T04:43:56Z", incidentDateTimeValue);
				
		XmlUtils.printNode(notifyDocTransformed);				
	}
	
	@Test
	public void testIncidentDate() throws Exception{
				
		Document notifyDocTransformed = transformIncidentReportToNotifyDoc(incidentDateDoc);
		
		Node incidentNode = XmlUtils.xPathNodeSearch(notifyDocTransformed, "notifications/wrapper/b-2:Notify/b-2:NotificationMessage/b-2:Message/notfm-exch:NotificationMessage/notfm-ext:NotifyingIncident/jxdm41:Incident");
		assertNotNull(incidentNode);
		
		Node incidentDate = XmlUtils.xPathNodeSearch(incidentNode, "nc:ActivityDate");
		assertNotNull(incidentDate);
		
		Node incidentDateDate = XmlUtils.xPathNodeSearch(incidentDate, "nc:Date");
		String incidentDateDateValue = incidentDateDate.getTextContent();
		
		assertEquals("2014-01-01", incidentDateDateValue);
				
		XmlUtils.printNode(notifyDocTransformed);				
	}
	
	@Test
	public void testIncidentDateRangeDate() throws Exception{
				
		Document notifyDocTransformed = transformIncidentReportToNotifyDoc(incidentDateRangeDateDoc);
		
		Node incidentNode = XmlUtils.xPathNodeSearch(notifyDocTransformed, "notifications/wrapper/b-2:Notify/b-2:NotificationMessage/b-2:Message/notfm-exch:NotificationMessage/notfm-ext:NotifyingIncident/jxdm41:Incident");
		assertNotNull(incidentNode);
		
		Node incidentDate = XmlUtils.xPathNodeSearch(incidentNode, "nc:ActivityDate");
		assertNotNull(incidentDate);
		
		Node incidentDateDate = XmlUtils.xPathNodeSearch(incidentDate, "nc:Date");
		String incidentDateDateValue = incidentDateDate.getTextContent();
		
		assertEquals("2014-01-01", incidentDateDateValue);
				
		XmlUtils.printNode(notifyDocTransformed);				
	}
	
	@Test
	public void testIncidentOffense() throws Exception{
				
		Document notifyDocTransformed = transformIncidentReportToNotifyDoc(incidentOffenseDoc);
		
		Node firstOffenseNode = XmlUtils.xPathNodeSearch(notifyDocTransformed, "notifications/wrapper/b-2:Notify/b-2:NotificationMessage/b-2:Message/notfm-exch:NotificationMessage/notfm-ext:NotifyingIncident/jxdm41:Offense[1]");
		assertNotNull(firstOffenseNode);
		Node secondOffenseNode = XmlUtils.xPathNodeSearch(notifyDocTransformed, "notifications/wrapper/b-2:Notify/b-2:NotificationMessage/b-2:Message/notfm-exch:NotificationMessage/notfm-ext:NotifyingIncident/jxdm41:Offense[2]");
		assertNotNull(secondOffenseNode);
		
		Node firstActivityDescriptionText = XmlUtils.xPathNodeSearch(firstOffenseNode, "nc:ActivityDescriptionText");
		assertNotNull(firstActivityDescriptionText);
		String firstActivityDescriptionTextValue = firstActivityDescriptionText.getTextContent();
		assertEquals("Driving Under The Influence, First Offense 23 VSA 1201 90D", firstActivityDescriptionTextValue);
		
		Node secondActivityDescriptionText = XmlUtils.xPathNodeSearch(secondOffenseNode, "nc:ActivityDescriptionText");
		assertNotNull(secondActivityDescriptionText);
		String secondActivityDescriptionTextValue = secondActivityDescriptionText .getTextContent();
		assertEquals("Robbery", secondActivityDescriptionTextValue);
		
		Node firstOffenseNDExCode = XmlUtils.xPathNodeSearch(firstOffenseNode, "notfm-ext:OffenseFBINDEXCode");
		assertNotNull(firstOffenseNDExCode);
		String firstOffenseNDExCodeValue = firstOffenseNDExCode.getTextContent();
		assertEquals("Driving Under Influence", firstOffenseNDExCodeValue);
		
		Node secondOffenseNDExCode = XmlUtils.xPathNodeSearch(secondOffenseNode, "notfm-ext:OffenseFBINDEXCode");
		assertNotNull(secondOffenseNDExCode);
		String secondOffenseNDExCodeValue = secondOffenseNDExCode.getTextContent();
		assertEquals("Robbery", secondOffenseNDExCodeValue);
		
		Node firstOffenseCategoryText = XmlUtils.xPathNodeSearch(firstOffenseNode, "jxdm41:OffenseCategoryText");
		assertNotNull(firstOffenseCategoryText);
		String firstOffenseCategoryTextValue = firstOffenseCategoryText.getTextContent();
		assertEquals("DUI", firstOffenseCategoryTextValue);
		
		Node secondOffenseCategoryText = XmlUtils.xPathNodeSearch(secondOffenseNode, "jxdm41:OffenseCategoryText");
		assertNotNull(firstOffenseCategoryText);
		String secondOffenseCategoryTextValue = secondOffenseCategoryText.getTextContent();
		assertEquals("ROB", secondOffenseCategoryTextValue);
	
				
		XmlUtils.printNode(notifyDocTransformed);				
	}
	

	private SAXSource createSource(String xml) {
		InputSource inputSource = new InputSource(new ByteArrayInputStream(xml.getBytes()));
		inputSource.setEncoding(org.apache.commons.lang.CharEncoding.UTF_8);
		return new SAXSource(inputSource);
	}
		
	private Document transformIncidentReportToNotifyDoc(Document incidentReportDocument) throws Exception{
		
		TransformerFactory transformerFactory = TransformerFactory.newInstance();
		
		Transformer transformer = transformerFactory.newTransformer(xsltSource);
		
		DOMResult domResult = new DOMResult();
		
		DOMSource inDocDomSource = new DOMSource(incidentReportDocument);
		
		transformer.transform(inDocDomSource, domResult);
		
		Document resultDoc = (Document)domResult.getNode();
			
		return resultDoc;
	}
	
}
