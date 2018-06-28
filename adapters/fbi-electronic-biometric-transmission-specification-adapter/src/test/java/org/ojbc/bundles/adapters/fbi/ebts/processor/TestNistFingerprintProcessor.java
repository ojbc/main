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
package org.ojbc.bundles.adapters.fbi.ebts.processor;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;
import java.util.Base64;
import java.util.List;
import java.util.Map;

import org.apache.commons.io.FileUtils;
import org.jnbis.api.Jnbis;
import org.jnbis.api.model.Nist;
import org.jnbis.api.model.record.HighResolutionGrayscaleFingerprint;
import org.jnbis.api.model.record.UserDefinedDescriptiveText;
import org.junit.Assert;
import org.junit.Test;
import org.ojbc.util.xml.XmlUtils;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

public class TestNistFingerprintProcessor {

	@Test
	public void testInsertPackageHighResolutionGrayscaleImageRecord() throws Exception
	{
		NistImageProcessor nistImageProcessor = new NistImageProcessor();
		
        Nist nist = Jnbis.nist().decode(absolute("nist/0400024.eft"));
        List<HighResolutionGrayscaleFingerprint> hiResGrayscaleFingerprints = nist.getHiResGrayscaleFingerprints();
        Assert.assertEquals(14, hiResGrayscaleFingerprints.size());
        
        Document civilRapbackRequest = XmlUtils.parseFileToDocument(new File("src/test/resources/input/RBSCVL_NoFingerprints.xml"));
        
		nistImageProcessor.insertPackageHighResolutionGrayscaleImageRecord(civilRapbackRequest, hiResGrayscaleFingerprints);
		
		NodeList imageNodes = XmlUtils.xPathNodeListSearch(civilRapbackRequest, "/nistbio:NISTBiometricInformationExchangePackage/nbio:PackageHighResolutionGrayscaleImageRecord");
		
		Assert.assertEquals(14, imageNodes.getLength());
		
		Element firstImage =  (Element) imageNodes.item(0);
		
		Assert.assertEquals("04",XmlUtils.xPathStringSearch(firstImage, "nbio:RecordCategoryCode"));
		Assert.assertEquals("1",XmlUtils.xPathStringSearch(firstImage, "nbio:ImageReferenceIdentification/nc:IdentificationID"));
		
		Assert.assertNotNull(XmlUtils.xPathStringSearch(firstImage, "nbio:FingerprintImage/nc:BinaryBase64Object"));
	
		Assert.assertEquals("1",XmlUtils.xPathStringSearch(firstImage, "nbio:FingerprintImage/nbio:ImageCaptureDetail/nc:CaptureResolutionCode"));
		Assert.assertEquals("1",XmlUtils.xPathStringSearch(firstImage, "nbio:FingerprintImage/nc:ImageCompressionAlgorithmCode"));
		
		Assert.assertEquals("800",XmlUtils.xPathStringSearch(firstImage, "nbio:FingerprintImage/nc:ImageHorizontalLineLengthPixelQuantity"));
		Assert.assertEquals("750",XmlUtils.xPathStringSearch(firstImage, "nbio:FingerprintImage/nc:ImageVerticalLineLengthPixelQuantity"));
		
		Assert.assertEquals("1",XmlUtils.xPathStringSearch(firstImage, "nbio:FingerprintImage/nbio:FingerprintImagePosition/nc:FingerPositionCode"));
		Assert.assertEquals("1",XmlUtils.xPathStringSearch(firstImage, "nbio:FingerprintImage/nc:FingerprintImageImpressionCaptureCategoryCode"));
		
	}
	
	
    @Test
    public void testFingerPrintExtraction() throws Exception {
        Nist nist = Jnbis.nist().decode(absolute("nist/0400024.eft"));

        UserDefinedDescriptiveText userDefinedText = nist.getUserDefinedTexts().get(0);
        Map<Integer, String> userDefinedFields = userDefinedText.getUserDefinedFields();
        Assert.assertEquals("0367", userDefinedFields.get(1));
        Assert.assertEquals("00", userDefinedFields.get(2));
        
        List<HighResolutionGrayscaleFingerprint> hiResGrayscaleFingerprints = nist.getHiResGrayscaleFingerprints();

        Assert.assertEquals(14, hiResGrayscaleFingerprints.size());
        
        HighResolutionGrayscaleFingerprint fingerPrint1 = hiResGrayscaleFingerprints.get(0);
        
        saveImageToFileSystem(fingerPrint1, "1", false);
        
        Assert.assertEquals("1", fingerPrint1.getCompressionAlgorithm());
        Assert.assertEquals("800", fingerPrint1.getHorizontalLineLength());
        Assert.assertEquals("1", fingerPrint1.getImageDesignationCharacter());
        Assert.assertEquals("1", fingerPrint1.getImageScanningResolution());
        Assert.assertEquals("1", fingerPrint1.getImpressionType());
        Assert.assertEquals("40923", fingerPrint1.getLogicalRecordLength());
        Assert.assertEquals("750", fingerPrint1.getVerticalLineLength());
        
    }

	private void saveImageToFileSystem(HighResolutionGrayscaleFingerprint fingerPrint, String fingerFileName, boolean saveToFileSystem) throws FileNotFoundException, IOException {

		if (saveToFileSystem)
		{	
			byte[] pngArray = Jnbis.wsq()
	                .decode(fingerPrint.getImageData())
	                .toJpg()
	                .asByteArray();
	
	        FileOutputStream fos = new FileOutputStream("/tmp/" + fingerFileName + ".jpg");
	        fos.write(pngArray);
	        fos.close();
	        
	        String sBase64Rapsheet = Base64.getEncoder().encodeToString(pngArray);
	        Assert.assertNotNull(sBase64Rapsheet);
	        
		}  
        
	}
	
    public static String absolute(String name) {
        URL url = FileUtils.class.getClassLoader().getResource(name);
        if (url == null) {
            throw new RuntimeException("unexpected error: Null URL");
        }
        return url.getFile();
    }
}
