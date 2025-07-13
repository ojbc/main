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
package org.ojbc.util.helper;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.zip.DataFormatException;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.util.StringUtils;

public class ZipUtilsTest {
//	private static final Log log = LogFactory.getLog( ZipUtilsTest.class );

	@BeforeEach
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
		
		printCompressedHexString("FBICivilFingerPrints");
		printCompressedHexString("StateCivilFingerPrints");
		printCompressedHexString("Found a Match");
		printCompressedHexString("FBICivilRapSheet");
		printCompressedHexString("FBICivilRapSheet2");
		printCompressedHexString("StateCivilRapSheet");
		printCompressedHexString("StateCivilRapSheet2");
		printCompressedHexString("Match");
		printCompressedHexString("FBI Subsequent results.");
		printCompressedHexString("State Subsequent results.");
		printCompressedHexString("NSOR Demographics");
		printCompressedHexString("NSOR Demographics 1");
		printCompressedHexString("NSOR Search Results");
		printCompressedHexString("NSOR Search Results 1");
		
//		String fbiCriminalHistory = FileUtils.readFileToString(new File("src/test/resources/testData/fbi_criminal_history.txt"));
//		printCompressedHexString(fbiCriminalHistory);
//		
//		String fbiSearchResultFile = FileUtils.readFileToString(new File("src/test/resources/fbi_initial_result_match-no-match-bart.html"));
//		printCompressedHexString(fbiSearchResultFile);
//
//		String stateSearchResultFile = FileUtils.readFileToString(new File("src/test/resources/state_initial_result_match-no-match-bart.html"));
//		printCompressedHexString(stateSearchResultFile);
		
		
	}

	@Test
	public void testUnzip() throws IOException, DataFormatException {
		
//		String fbiSearchResultFile = FileUtils.readFileToString(new File("src/test/resources/fbi_initial_result_match-no-match-bart.html"));
//		printCompressedHexString(fbiSearchResultFile);
//
//		String stateSearchResultFile = FileUtils.readFileToString(new File("src/test/resources/state_initial_result_match-no-match-bart.html"));
//		printCompressedHexString(stateSearchResultFile);
		
		
	}
	
	private void printCompressedHexString(String originalData) throws IOException {
		byte[] compressedData = ZipUtils.zip(originalData.getBytes());
//		System.out.println(originalData + " compressed as "+ StringUtils.trimAllWhitespace(bytesToHexString(compressedData)));
		System.out.println(originalData + " compressed as "+ StringUtils.trimAllWhitespace(new String(compressedData, StandardCharsets.UTF_8)));
	}

	@SuppressWarnings("unused")
	private void printUnCompressedString(String compressedData) throws IOException {
		byte[] uncompressedData = ZipUtils.unzip(compressedData.getBytes());
		System.out.println(compressedData + " uncompressed as "+ StringUtils.trimAllWhitespace(uncompressedData.toString()));
	}
	
	private String bytesToHexString(byte[] compressedData) {
		StringBuilder sb = new StringBuilder();
		for (byte b: compressedData){
			sb.append(String.format("%02X ", b));
		}
		
		return sb.toString();
	}
	
}
