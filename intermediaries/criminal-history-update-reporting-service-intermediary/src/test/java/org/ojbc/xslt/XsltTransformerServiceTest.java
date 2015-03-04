package org.ojbc.xslt;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.io.ByteArrayInputStream;

import javax.xml.transform.sax.SAXSource;

import org.apache.commons.codec.CharEncoding;
import org.junit.Before;
import org.junit.Test;
import org.xml.sax.InputSource;

public class XsltTransformerServiceTest {

	XsltTransformerService unit;

	@Before
	public void setup() {
		unit = new XsltTransformerService();
	}

	@Test
	public void returnNullWhenXmlIsNull() {
		String expectedXml = unit.transform(null, null,null);

		assertEquals(expectedXml, null);
	}

	@Test
	public void throwsExceptionWhenXmlIsBad() {
		try {
			unit.transform(createSource("A really bad xml"), null,null);
			fail();
		} catch (RuntimeException ex) {
			assertTrue(ex.getMessage().startsWith("An error occured when applying XSLT - "));			
		}

	}

	@Test
	public void identityTransformWhenXslIsNull() {
		SAXSource xml = createSource("<cars ></cars>");

		String expectedXml = unit.transform(xml, null,null);

		assertEquals(expectedXml, "<?xml version=\"1.0\" encoding=\"UTF-8\"?><cars/>");
	}

	
	private SAXSource createSource(String xml) {
		InputSource inputSource = new InputSource(new ByteArrayInputStream(xml.getBytes()));
		inputSource.setEncoding(CharEncoding.UTF_8);
		return new SAXSource(inputSource);
	}
	
}


