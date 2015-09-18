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
 * Copyright 2012-2015 Open Justice Broker Consortium
 */
package org.ojbc.adapters.rapbackdatastore.util;

import static org.junit.Assert.*;

import java.io.IOException;
import java.util.zip.DataFormatException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.junit.Before;
import org.junit.Test;
import org.springframework.util.StringUtils;

public class ZipUtilsTest {
	private static final Log log = LogFactory.getLog( ZipUtilsTest.class );

	@Before
	public void setUp() throws Exception {
	}

	@Test
	public void test() throws IOException, DataFormatException {
		byte[] originalData = "Test compressor with a looooooooooooooooooot of data".getBytes();
		byte[] compressedData = ZipUtils.zip("Test compressor with a looooooooooooooooooot of data".getBytes());
		byte[] decompressedData = ZipUtils.unzip(compressedData);
		
		assertArrayEquals(originalData, decompressedData);

	}
	
	@Test
	public void test1() throws IOException, DataFormatException {
		pringCompressedHexString("FBICivilFingerPrints");
		pringCompressedHexString("StateCivilFingerPrints");
		pringCompressedHexString("Found a Match");
		pringCompressedHexString("FBICivilRapSheet");
		pringCompressedHexString("FBICivilRapSheet2");
		pringCompressedHexString("StateCivilRapSheet");
		pringCompressedHexString("StateCivilRapSheet2");
		pringCompressedHexString("Match");
		
	}

	private void pringCompressedHexString(String originalData) throws IOException {
		byte[] compressedData = ZipUtils.zip(originalData.getBytes());
		log.info(originalData + " compressed as "+ StringUtils.trimAllWhitespace(bytesToHexString(compressedData)));
	}

	private String bytesToHexString(byte[] compressedData) {
		StringBuilder sb = new StringBuilder();
		for (byte b: compressedData){
			sb.append(String.format("%02X ", b));
		}
		
		return sb.toString();
	}
	
}
