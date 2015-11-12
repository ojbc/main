package org.ojbc.bundles.adapters.personsearch.ndex;

import java.io.File;
import java.util.List;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.sax.SAXSource;
import javax.xml.transform.stream.StreamSource;

import org.apache.commons.io.FileUtils;
import org.custommonkey.xmlunit.DetailedDiff;
import org.custommonkey.xmlunit.Difference;
import org.custommonkey.xmlunit.XMLUnit;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.ojbc.util.camel.helper.OJBUtils;
import org.ojbc.util.xml.XsltTransformer;

public class NdexSearchTransformTest {
			
	private XsltTransformer xsltTransformer;
	
	@Before
	public void setup() throws ParserConfigurationException{

		XMLUnit.setIgnoreWhitespace(true);
    	XMLUnit.setIgnoreAttributeOrder(true);
    	XMLUnit.setIgnoreComments(true);
    	XMLUnit.setXSLTVersion("2.0");
		
		xsltTransformer = new XsltTransformer();		
	}
			
	@SuppressWarnings("unchecked")
	@Test
	public void searchTranformTest() throws Exception{
		
		DetailedDiff detailedDiff = runTransform("src/test/resources/xml/Request/PersonSearchRequest-SR_Example.xml",
				"src/main/resources/xsl/NDEX_Search_Request_Transform.xsl",
				"src/test/resources/xml/Request/output/PersonSearchRequest-SR_Example-out.xml");
		
		List<Difference> differenceList = detailedDiff.getAllDifferences();
        
        Assert.assertEquals(detailedDiff.toString(), 1, differenceList.size());
        
        Difference difference=differenceList.get(0);
        
        System.out.println("Difference xpath: " + difference.getControlNodeDetail().getXpathLocation());
	}	
	
	private DetailedDiff runTransform(String inputFileClasspath, String xsltClasspath, String expectedOutputFileClasspath) throws Exception{

		File inputFile = new File(inputFileClasspath);		
		String inputXml = FileUtils.readFileToString(inputFile);
		SAXSource inputSaxSource = OJBUtils.createSaxSource(inputXml);
		
		File expectedOutputFile = new File(expectedOutputFileClasspath);
		String expectedXml = FileUtils.readFileToString(expectedOutputFile);		
		
		File xsltFile = new File(xsltClasspath);
		StreamSource xsltSaxSource = new StreamSource(xsltFile);
		
		String actualTransformedResultXml = xsltTransformer.transform(inputSaxSource, xsltSaxSource, null);
		
		System.out.println(actualTransformedResultXml);
				
		DetailedDiff detailedDiff = new DetailedDiff(XMLUnit.compareXML(expectedXml, actualTransformedResultXml));	
		
		return detailedDiff;
		
	}
	
}

