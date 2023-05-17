package org.ojbc.booking.xslt;

import static org.junit.Assert.assertTrue;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.xml.transform.sax.SAXSource;

import org.apache.commons.io.FileUtils;
import org.apache.http.Consts;
import org.custommonkey.xmlunit.Diff;
import org.custommonkey.xmlunit.XMLUnit;
import org.junit.Before;
import org.junit.Test;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

public class BookingReportingTransformerServiceTest {

	XsltTransformerService unit;

	@Before
	public void setup() {
		unit = new XsltTransformerService();
		
		//Tell XML Unit to ignore whitespace between elements and within elements
		XMLUnit.setIgnoreWhitespace(true);
		XMLUnit.setNormalizeWhitespace(true);				
	}

	@Test
	public void custodyReleaseToPersonHealthRequest() throws Exception{
		String xml = FileUtils.readFileToString(new File( "src/test/resources/xmlInstances/BookingReport.xml"), Consts.UTF_8);
		String xslt = FileUtils.readFileToString(new File("src/main/resources/xslt/createPersonHealthInformationSearchRequest.xsl"), Consts.UTF_8);

		Map<String, Object> params = new HashMap<String, Object>();
		
		params.put("addBookingID", "false");
		
		transformAndValidate(xslt, xml,"src/test/resources/xmlInstances/output/PersonHealthInformationSearchRequest-PII.xml", params);
	}

	@Test
	public void custodyReleaseToPersonHealthRequestWithBookingID() throws Exception{
		String xml = FileUtils.readFileToString(
				new File( "src/test/resources/xmlInstances/BookingReport.xml"), Consts.UTF_8);
		String xslt = FileUtils.readFileToString(
				new File("src/main/resources/xslt/createPersonHealthInformationSearchRequest.xsl"), Consts.UTF_8);

		Map<String, Object> params = new HashMap<String, Object>();
		
		params.put("addBookingID", "true");
		 
		transformAndValidate(xslt, xml,"src/test/resources/xmlInstances/output/PersonHealthInformationSearchRequestBookingID-PII.xml", params);
	}

	private void transformAndValidate(String xslPath, String inputXmlPath, String expectedHtmlPath, Map<String,Object> params) throws IOException {

		String expectedXml = FileUtils.readFileToString(new File(expectedHtmlPath), Consts.UTF_8);
		String convertResult = unit.transform(createSource(inputXmlPath), createSource(xslPath), params);
		
		System.out.println(convertResult);
		
		// XMLUnit comparison: 	
		Diff myDiff = null;		
		try {
			myDiff = new Diff(expectedXml, convertResult);
		} catch (SAXException e) {
			e.printStackTrace();
		}
		
		assertTrue("XML similar " + myDiff.toString(), myDiff.similar());
		assertTrue("XML identical " + myDiff.toString(), myDiff.identical());		
	}

	private SAXSource createSource(String xml) {
		InputSource inputSource = new InputSource(new ByteArrayInputStream(xml.getBytes()));
		inputSource.setEncoding(Consts.UTF_8.name());
		return new SAXSource(inputSource);
	}
	
	
}
