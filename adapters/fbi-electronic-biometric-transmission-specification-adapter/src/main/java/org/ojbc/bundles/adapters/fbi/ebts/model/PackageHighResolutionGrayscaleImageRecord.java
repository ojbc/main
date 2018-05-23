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
package org.ojbc.bundles.adapters.fbi.ebts.model;

public class PackageHighResolutionGrayscaleImageRecord {

	private String recordCategoryCode;
	private String imageReferenceIdentification;
	private byte[] imageBase64;
	private String captureResolutionCode;
	private String imageCompressionAlgorithmCode;
	private String imageHorizontalLineLengthPixelQuantity;
	private String imageVerticalLineLengthPixelQuantity;
	private String fingerPositionCode;
	private String fingerprintImageImpressionCaptureCategoryCode;
	
	public String getRecordCategoryCode() {
		return recordCategoryCode;
	}
	public void setRecordCategoryCode(String recordCategoryCode) {
		this.recordCategoryCode = recordCategoryCode;
	}
	public String getImageReferenceIdentification() {
		return imageReferenceIdentification;
	}
	public void setImageReferenceIdentification(String imageReferenceIdentification) {
		this.imageReferenceIdentification = imageReferenceIdentification;
	}
	public byte[] getImageBase64() {
		return imageBase64;
	}
	public void setImageBase64(byte[] imageBase64) {
		this.imageBase64 = imageBase64;
	}
	public String getCaptureResolutionCode() {
		return captureResolutionCode;
	}
	public void setCaptureResolutionCode(String captureResolutionCode) {
		this.captureResolutionCode = captureResolutionCode;
	}
	public String getImageCompressionAlgorithmCode() {
		return imageCompressionAlgorithmCode;
	}
	public void setImageCompressionAlgorithmCode(
			String imageCompressionAlgorithmCode) {
		this.imageCompressionAlgorithmCode = imageCompressionAlgorithmCode;
	}
	public String getImageHorizontalLineLengthPixelQuantity() {
		return imageHorizontalLineLengthPixelQuantity;
	}
	public void setImageHorizontalLineLengthPixelQuantity(
			String imageHorizontalLineLengthPixelQuantity) {
		this.imageHorizontalLineLengthPixelQuantity = imageHorizontalLineLengthPixelQuantity;
	}
	public String getImageVerticalLineLengthPixelQuantity() {
		return imageVerticalLineLengthPixelQuantity;
	}
	public void setImageVerticalLineLengthPixelQuantity(
			String imageVerticalLineLengthPixelQuantity) {
		this.imageVerticalLineLengthPixelQuantity = imageVerticalLineLengthPixelQuantity;
	}
	public String getFingerPositionCode() {
		return fingerPositionCode;
	}
	public void setFingerPositionCode(String fingerPositionCode) {
		this.fingerPositionCode = fingerPositionCode;
	}
	public String getFingerprintImageImpressionCaptureCategoryCode() {
		return fingerprintImageImpressionCaptureCategoryCode;
	}
	public void setFingerprintImageImpressionCaptureCategoryCode(
			String fingerprintImageImpressionCaptureCategoryCode) {
		this.fingerprintImageImpressionCaptureCategoryCode = fingerprintImageImpressionCaptureCategoryCode;
	}
}