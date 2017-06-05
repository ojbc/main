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
package org.ojbc.web.portal.services;

import static org.junit.Assert.assertThat;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.xml.transform.sax.SAXSource;

import org.apache.commons.codec.CharEncoding;
import org.hamcrest.Matchers;
import org.junit.Before;
import org.junit.Test;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.xml.sax.InputSource;

public class XslFormattersTest{

	private Map<String, Object> params = new HashMap<String, Object>();
	private XsltTransformerService xsltTransformerService;

	@Before
	public void setup() {
		xsltTransformerService = new XsltTransformerService();
	}
	
	@Test
	public void formatDate() throws Exception{
		assertThatCodeEquals("xslRunners/formatDate.xsl", "2021-12-23", "12/23/2021");
	}
	
	@Test
	public void formatSSN() throws Exception{
		assertThatCodeEquals("xslRunners/formatSSN.xsl", "111223333", "111-22-3333");
	}

	@Test
	public void formatSSNWithDashes() throws Exception{
		assertThatCodeEquals("xslRunners/formatSSN.xsl", "111-22-3333", "111-22-3333");
	}
	
	@Test
	public void formatBlankSSN() throws Exception{
		assertThatCodeEquals("xslRunners/formatSSN.xsl", "", "");
	}
	
	@Test
	public void formatHeight() throws Exception {
		
		assertThatCodeEquals("xslRunners/formatHeight.xsl", "10", "10\"");
		assertThatCodeEquals("xslRunners/formatHeight.xsl", "12", "1'");
		assertThatCodeEquals("xslRunners/formatHeight.xsl", "13", "1' 1\"");
		assertThatCodeEquals("xslRunners/formatHeight.xsl", "   146 ", "12' 2\"");
		assertThatCodeEquals("xslRunners/formatHeight.xsl", "", "");
		assertThatCodeEquals("xslRunners/formatHeight.xsl", "0.5", "");
		assertThatCodeEquals("xslRunners/formatHeight.xsl", "25.7", "2' 1\"");
	}

	@Test
	public void formatRace() throws Exception{
		assertThatCodeEquals("xslRunners/formatRace.xsl", "A", "Asian");
		assertThatCodeEquals("xslRunners/formatRace.xsl", "B", "Black");
		assertThatCodeEquals("xslRunners/formatRace.xsl", "I", "American Indian");
		assertThatCodeEquals("xslRunners/formatRace.xsl", "W", "White");
		assertThatCodeEquals("xslRunners/formatRace.xsl", "U", "Unknown");
		assertThatCodeEquals("xslRunners/formatRace.xsl", " U ", "Unknown");
		assertThatCodeEquals("xslRunners/formatRace.xsl", "", "");
		assertThatCodeEquals("xslRunners/formatRace.xsl", "UnknownCode", "UnknownCode");
	}
	
	@Test
	public void formatSex() throws Exception{
		assertThatCodeEquals("xslRunners/formatSex.xsl", "M", "Male");
		assertThatCodeEquals("xslRunners/formatSex.xsl", "F", "Female");
		assertThatCodeEquals("xslRunners/formatSex.xsl", "U", "Unknown");
		assertThatCodeEquals("xslRunners/formatSex.xsl", " U ", "Unknown");
		assertThatCodeEquals("xslRunners/formatSex.xsl", "", "");
		assertThatCodeEquals("xslRunners/formatSex.xsl", "UnknownCode", "UnknownCode");
	}

	@Test
	public void formatHairColor() throws Exception{
		assertThatCodeEquals("xslRunners/formatHairColor.xsl", "BLK", "Black");
		assertThatCodeEquals("xslRunners/formatHairColor.xsl", "BLN", "Blond or Strawberry");
		assertThatCodeEquals("xslRunners/formatHairColor.xsl", "BLU", "Blue");
		assertThatCodeEquals("xslRunners/formatHairColor.xsl", "BRO", "Brown");
		assertThatCodeEquals("xslRunners/formatHairColor.xsl", "GRN", "Green");
		assertThatCodeEquals("xslRunners/formatHairColor.xsl", "GRY", "Gray or Partially Gray");
		assertThatCodeEquals("xslRunners/formatHairColor.xsl", "ONG", "Orange");
		assertThatCodeEquals("xslRunners/formatHairColor.xsl", "PLE", "Purple");
		assertThatCodeEquals("xslRunners/formatHairColor.xsl", "PNK", "Pink");
		assertThatCodeEquals("xslRunners/formatHairColor.xsl", "RED", "Red or Auburn");
		assertThatCodeEquals("xslRunners/formatHairColor.xsl", "SDY", "Sandy");
		assertThatCodeEquals("xslRunners/formatHairColor.xsl", "WHI", "White");
		assertThatCodeEquals("xslRunners/formatHairColor.xsl", "XXX", "Unknown or Completely Bald");
		
		assertThatCodeEquals("xslRunners/formatHairColor.xsl", "", "");
		assertThatCodeEquals("xslRunners/formatHairColor.xsl", "UnknownCode", "UnknownCode");
	}

	@Test
	public void formatEyeColor() throws Exception{
		assertThatCodeEquals("xslRunners/formatEyeColor.xsl", "BLK", "Black");
		assertThatCodeEquals("xslRunners/formatEyeColor.xsl", "BLU", "Blue");
		assertThatCodeEquals("xslRunners/formatEyeColor.xsl", "BRO", "Brown");
		assertThatCodeEquals("xslRunners/formatEyeColor.xsl", "GRN", "Green");
		assertThatCodeEquals("xslRunners/formatEyeColor.xsl", "GRY", "Gray");
		assertThatCodeEquals("xslRunners/formatEyeColor.xsl", "HAZ", "Hazel");
		assertThatCodeEquals("xslRunners/formatEyeColor.xsl", "MAR", "Maroon");
		assertThatCodeEquals("xslRunners/formatEyeColor.xsl", "MUL", "Multicolored");
		assertThatCodeEquals("xslRunners/formatEyeColor.xsl", "PNK", "Pink");
		assertThatCodeEquals("xslRunners/formatEyeColor.xsl", "XXX", "Unknown");
		
		assertThatCodeEquals("xslRunners/formatHairColor.xsl", "", "");
		assertThatCodeEquals("xslRunners/formatHairColor.xsl", "UnknownCode", "UnknownCode");
	}
	
	@Test
	public void formatVehicleColor() throws Exception{
		assertThatCodeEquals("xslRunners/formatVehicleColor.xsl", "AME", "Amethyst");
		assertThatCodeEquals("xslRunners/formatVehicleColor.xsl", "BGE", "Beige");
		assertThatCodeEquals("xslRunners/formatVehicleColor.xsl", "BLK", "Black");
		assertThatCodeEquals("xslRunners/formatVehicleColor.xsl", "BLU", "Blue");
		assertThatCodeEquals("xslRunners/formatVehicleColor.xsl", "BRO", "Brown");
		assertThatCodeEquals("xslRunners/formatVehicleColor.xsl", "BRZ", "Bronze");
		assertThatCodeEquals("xslRunners/formatVehicleColor.xsl", "CAM", "Camouflage");
		assertThatCodeEquals("xslRunners/formatVehicleColor.xsl", "COM", "Chrome, Stainless Steel");
		assertThatCodeEquals("xslRunners/formatVehicleColor.xsl", "CPR", "Copper");
		assertThatCodeEquals("xslRunners/formatVehicleColor.xsl", "CRM", "Cream, Ivory");
		assertThatCodeEquals("xslRunners/formatVehicleColor.xsl", "DBL", "Blue, Dark");
		assertThatCodeEquals("xslRunners/formatVehicleColor.xsl", "DGR", "Green, Dark");
		assertThatCodeEquals("xslRunners/formatVehicleColor.xsl", "GLD", "Gold");
		assertThatCodeEquals("xslRunners/formatVehicleColor.xsl", "GRN", "Green");
		assertThatCodeEquals("xslRunners/formatVehicleColor.xsl", "GRY", "Gray");
		assertThatCodeEquals("xslRunners/formatVehicleColor.xsl", "LAV", "Lavender (Purple)");
		assertThatCodeEquals("xslRunners/formatVehicleColor.xsl", "LBL", "Blue, Light");
		assertThatCodeEquals("xslRunners/formatVehicleColor.xsl", "LGR", "Green, Light");
		assertThatCodeEquals("xslRunners/formatVehicleColor.xsl", "MAR", "Maroon, Burgundy (Purple)");
		assertThatCodeEquals("xslRunners/formatVehicleColor.xsl", "MUL/COL", "Multicolored");
		assertThatCodeEquals("xslRunners/formatVehicleColor.xsl", "MVE", "Mauve");
		assertThatCodeEquals("xslRunners/formatVehicleColor.xsl", "ONG", "Orange");
		assertThatCodeEquals("xslRunners/formatVehicleColor.xsl", "PLE", "Purple");
		assertThatCodeEquals("xslRunners/formatVehicleColor.xsl", "PNK", "Pink");
		assertThatCodeEquals("xslRunners/formatVehicleColor.xsl", "RED", "Red");
		assertThatCodeEquals("xslRunners/formatVehicleColor.xsl", "SIL", "Silver");
		assertThatCodeEquals("xslRunners/formatVehicleColor.xsl", "TAN", "Tan");
		assertThatCodeEquals("xslRunners/formatVehicleColor.xsl", "TEA", "Teal");
		assertThatCodeEquals("xslRunners/formatVehicleColor.xsl", "TPE", "Taupe");
		assertThatCodeEquals("xslRunners/formatVehicleColor.xsl", "TRQ", "Turquoise (Blue)");
		assertThatCodeEquals("xslRunners/formatVehicleColor.xsl", "WHI", "White");
		assertThatCodeEquals("xslRunners/formatVehicleColor.xsl", "YEL", "Yellow");
		assertThatCodeEquals("xslRunners/formatVehicleColor.xsl", "XXX", "Unknown");
	}

	private void assertThatCodeEquals(String runnerXsl, String code, String expectedValue) throws IOException {
	    setParam("input", code);
		assertThat(transform(runnerXsl, params),Matchers.is(expectedValue));;
    }


	
	private void setParam(String key, Object value){
		params.clear();
		params.put(key, value);
	}
	
	private String transform(String runner, Map<String,Object> params) throws IOException {
		ClassPathResource xsl = new ClassPathResource(runner);

		return xsltTransformerService.transform(new SAXSource(new InputSource(new ByteArrayInputStream("<xml></xml>".getBytes()))), createSourceAndSetSystemId(xsl),params );
	}

	
	private SAXSource createSourceAndSetSystemId(Resource inputStream) {
		try {
			InputSource inputSource = new InputSource(inputStream.getInputStream());
			inputSource.setEncoding(CharEncoding.UTF_8);
			inputSource.setSystemId(inputStream.getURL().toExternalForm());
			return new SAXSource(inputSource);
		} catch (Exception e) {
			throw new RuntimeException( e);
		}
	}
	

}
