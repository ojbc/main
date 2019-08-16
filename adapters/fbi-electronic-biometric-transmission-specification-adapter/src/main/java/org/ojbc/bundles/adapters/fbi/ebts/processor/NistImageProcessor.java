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

import java.util.Base64;
import java.util.List;
import java.util.logging.Logger;

import org.apache.camel.Body;
import org.apache.camel.Exchange;
import org.apache.camel.Header;
import org.apache.commons.lang.StringUtils;
import org.jnbis.api.Jnbis;
import org.jnbis.api.model.Nist;
import org.jnbis.api.model.record.HighResolutionGrayscaleFingerprint;
import org.ojbc.util.xml.OjbcNamespaceContext;
import org.ojbc.util.xml.XmlUtils;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

public class NistImageProcessor {

	private static final Logger logger = Logger.getLogger(FbiEbtsResponseProcessor.class.getName());
	
	public Document insertPackageHighResolutionGrayscaleImageRecord(@Body Document civilRapbackRequest, @Header("civilFingerPrints") List<HighResolutionGrayscaleFingerprint> civilFingerPrints) throws Exception
	{
		
		Element transactionContentSummaryElement = (Element) XmlUtils.xPathNodeSearch(civilRapbackRequest, "/nistbio:NISTBiometricInformationExchangePackage/nistbio:PackageInformationRecord/nbio:Transaction/nbio:TransactionContentSummary");
		
		for (HighResolutionGrayscaleFingerprint fingerPrint : civilFingerPrints) {
		
			Element imageRecord = XmlUtils.appendElement(civilRapbackRequest.getDocumentElement(), OjbcNamespaceContext.NS_NIST_BIO, "nistbio:PackageHighResolutionGrayscaleImageRecord");
		
			XmlUtils.appendTextElement(imageRecord, OjbcNamespaceContext.NS_NIEM_BIO, "nbio:RecordCategoryCode", "04");
			
			Element imageReferenceIdentification = XmlUtils.appendElement(imageRecord, OjbcNamespaceContext.NS_NIEM_BIO, "nbio:ImageReferenceIdentification");
			XmlUtils.appendTextElement(imageReferenceIdentification, OjbcNamespaceContext.NS_NC, "nc20:IdentificationID", fingerPrint.getImageDesignationCharacter());
			
			Element fingerprintImage = XmlUtils.appendElement(imageRecord, OjbcNamespaceContext.NS_NIEM_BIO, "nbio:FingerprintImage");

	        String fingerprintAsJpeg = Base64.getEncoder().encodeToString(fingerPrint.getImageData());
			Element base64BinaryObject = XmlUtils.appendElement(fingerprintImage, OjbcNamespaceContext.NS_NC, "nc20:BinaryBase64Object");
			base64BinaryObject.setTextContent(fingerprintAsJpeg);
			
			Element imageCaptureDetail = XmlUtils.appendElement(fingerprintImage, OjbcNamespaceContext.NS_NIEM_BIO, "nbio:ImageCaptureDetail");
			XmlUtils.appendTextElement(imageCaptureDetail, OjbcNamespaceContext.NS_NIEM_BIO, "nbio:CaptureResolutionCode", fingerPrint.getImageScanningResolution());

			XmlUtils.appendTextElement(fingerprintImage, OjbcNamespaceContext.NS_NIEM_BIO, "nbio:ImageCompressionAlgorithmCode", fingerPrint.getCompressionAlgorithm());
			XmlUtils.appendTextElement(fingerprintImage, OjbcNamespaceContext.NS_NIEM_BIO, "nbio:ImageHorizontalLineLengthPixelQuantity", fingerPrint.getHorizontalLineLength());
			XmlUtils.appendTextElement(fingerprintImage, OjbcNamespaceContext.NS_NIEM_BIO, "nbio:ImageVerticalLineLengthPixelQuantity", fingerPrint.getVerticalLineLength());
			
			Element fingerprintImagePosition = XmlUtils.appendElement(fingerprintImage, OjbcNamespaceContext.NS_NIEM_BIO, "nbio:FingerprintImagePosition");
			XmlUtils.appendTextElement(fingerprintImagePosition, OjbcNamespaceContext.NS_NIEM_BIO, "nbio:FingerPositionCode", fingerPrint.getImageDesignationCharacter());
			
			XmlUtils.appendTextElement(fingerprintImage, OjbcNamespaceContext.NS_NIEM_BIO, "nbio:FingerprintImageImpressionCaptureCategoryCode", fingerPrint.getImpressionType());

//			<nbio:ContentRecordSummary>
//				<nbio:ImageReferenceIdentification>
//					<nc20:IdentificationID>01</nc20:IdentificationID>
//				</nbio:ImageReferenceIdentification>
//				<nbio:RecordCategoryCode>04</nbio:RecordCategoryCode>
//			</nbio:ContentRecordSummary>
			
			if (transactionContentSummaryElement != null)
			{
				Element contentRecordSummary = XmlUtils.appendElement(transactionContentSummaryElement, OjbcNamespaceContext.NS_NIEM_BIO, "nbio:ContentRecordSummary");
				
				Element imageReferenceIdentificationContentSummary = XmlUtils.appendElement(contentRecordSummary, OjbcNamespaceContext.NS_NIEM_BIO, "nbio:ImageReferenceIdentification");
				XmlUtils.appendTextElement(imageReferenceIdentificationContentSummary, OjbcNamespaceContext.NS_NC, "nc20:IdentificationID", fingerPrint.getImageDesignationCharacter());
				
				XmlUtils.appendTextElement(contentRecordSummary, OjbcNamespaceContext.NS_NIEM_BIO, "nbio:RecordCategoryCode", "04");
				
			}	
		}
		
		return civilRapbackRequest;
	}
	
	public void extractCivilFingerprints(@Body Document doc, Exchange ex)
	{
		byte[] binaryData = XmlUtils.getBinaryData(doc, "//submsg-ext:FingerprintDocument/nc:DocumentBinary/submsg-ext:Base64BinaryObject");
		
        Nist nist = Jnbis.nist().decode(binaryData);
        List<HighResolutionGrayscaleFingerprint> hiResGrayscaleFingerprints = nist.getHiResGrayscaleFingerprints();
      
        ex.getIn().setHeader("civilFingerPrints", hiResGrayscaleFingerprints);

        String nativeScanningResolution  = nist.getTransactionInfo().getNativeScanningResolution();
        String nominalTransmittingResolution  = nist.getTransactionInfo().getNominalTransmittingResolution();
        
        //We add one here because we also have a type 2 record which is the itl:PackageDescriptiveTextRecord. This field is the type 2 to type 99 recordsest
        Integer transactionContentSummaryContentRecordCountCivil = hiResGrayscaleFingerprints.size() + 1;

        logger.info("Native scanning resolution: " + nativeScanningResolution + ", nominal transmitting resolution: " + nominalTransmittingResolution + ", transaction content count: " + transactionContentSummaryContentRecordCountCivil);
        
        if (transactionContentSummaryContentRecordCountCivil != null)
        {
        	ex.getIn().setHeader("transactionContentSummaryContentRecordCountCivil", String.valueOf(transactionContentSummaryContentRecordCountCivil));
        }	
        
    	if (StringUtils.isNotBlank(nativeScanningResolution))
    	{
    		ex.getIn().setHeader("nativeScanningResolution", nativeScanningResolution);
    	}	
        
    	if (StringUtils.isNotBlank(nominalTransmittingResolution))
    	{
    		ex.getIn().setHeader("nominalTransmittingResolution", nominalTransmittingResolution);
    	}	

	}

}
