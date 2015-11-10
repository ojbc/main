package org.ojbc.adapters.ndex.submission.mock;


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

public class NdexSubmissionTransformTest {
			
	private XsltTransformer xsltTransformer;
	
	@Before
	public void setup() throws ParserConfigurationException{

		XMLUnit.setIgnoreWhitespace(true);
    	XMLUnit.setIgnoreAttributeOrder(true);
    	XMLUnit.setIgnoreComments(true);
    	XMLUnit.setXSLTVersion("2.0");
		
		xsltTransformer = new XsltTransformer();		
	}
			
	@Test
	public void jimsDispositionTest() throws Exception{
		
		DetailedDiff detailedDiff = runTransform("src/test/resources/xmlInstances/JIMS/JIMS-FTP680_1_1.xml",
				"src/main/resources/xsl/DispositionReportingTransform-JIMS.xsl",
				"src/test/resources/xmlInstances/output/JIMS-FTP680_1_1.out.xml");
		
		List<Difference> differenceList = detailedDiff.getAllDifferences();
        
        Assert.assertEquals(detailedDiff.toString(), 0, differenceList.size());					
	}	
	
	@Test
	public void jimsDispositionTest_1() throws Exception{
		
		DetailedDiff detailedDiff = runTransform("src/test/resources/xmlInstances/JIMS/incomingJimsFileWithCswTerm.xml",
				"src/main/resources/xsl/DispositionReportingTransform-JIMS.xsl",
				"src/test/resources/xmlInstances/output/incomingJimsFileWithCswTerm.out.xml");
		
		List<Difference> differenceList = detailedDiff.getAllDifferences();
        
        Assert.assertEquals(detailedDiff.toString(), 0, differenceList.size());					
	}	
	
	@Test
	public void jimsDispositionTest_2() throws Exception{
		
		DetailedDiff detailedDiff = runTransform("src/test/resources/xmlInstances/JIMS/FTP680_F0680_20150612_174421551771.xml",
				"src/main/resources/xsl/DispositionReportingTransform-JIMS.xsl",
				"src/test/resources/xmlInstances/output/FTP680_F0680_20150612_174421551771.out.xml");
		
		List<Difference> differenceList = detailedDiff.getAllDifferences();
        
        Assert.assertEquals(detailedDiff.toString(), 0, differenceList.size());					
	}	
	
	@Test
	public void jimsDispositionTest_3() throws Exception{
		
		DetailedDiff detailedDiff = runTransform("src/test/resources/xmlInstances/JIMS/FTP680_F0680_20150612_174421551771-ChargeID.xml",
				"src/main/resources/xsl/DispositionReportingTransform-JIMS.xsl",
				"src/test/resources/xmlInstances/output/FTP680_F0680_20150612_174421551771-ChargeID.out.xml");
		
		List<Difference> differenceList = detailedDiff.getAllDifferences();
        
        Assert.assertEquals(detailedDiff.toString(), 0, differenceList.size());					
	}
	
	@Test
	public void jimsDispositionTest_4() throws Exception{
		
		DetailedDiff detailedDiff = runTransform("src/test/resources/xmlInstances/JIMS/FTP680_F0680_20150612_174430868629.xml",
				"src/main/resources/xsl/DispositionReportingTransform-JIMS.xsl",
				"src/test/resources/xmlInstances/output/FTP680_F0680_20150612_174430868629.out.xml");
		
		List<Difference> differenceList = detailedDiff.getAllDifferences();
        
        Assert.assertEquals(detailedDiff.toString(), 0, differenceList.size());					
	}
	
	@Test
	public void jimsDispositionTest_5() throws Exception{
		
		DetailedDiff detailedDiff = runTransform("src/test/resources/xmlInstances/JIMS/FTP680_F0680_20150612_174422436215.xml",
				"src/main/resources/xsl/DispositionReportingTransform-JIMS.xsl",
				"src/test/resources/xmlInstances/output/FTP680_F0680_20150612_174422436215.out.xml");
		
		List<Difference> differenceList = detailedDiff.getAllDifferences();
        
        Assert.assertEquals(detailedDiff.toString(), 0, differenceList.size());					
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

