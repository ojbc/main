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
package org.ojbc.util.camel.processor;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertNotNull;
import static junit.framework.Assert.assertNull;

import java.io.File;

import org.apache.camel.CamelContext;
import org.apache.camel.Exchange;
import org.apache.camel.impl.DefaultCamelContext;
import org.apache.camel.impl.DefaultExchange;
import org.junit.Test;

public class TestFileSizeFilterProcessor {

	@Test
	public void testFilterFilesLessThanSpecifiedSize()
	{
		  CamelContext ctx = new DefaultCamelContext(); 
		  Exchange ex = new DefaultExchange(ctx);
		  
		  File inputFile = new File("src/test/resources/xmlInstances/booking/booking_9999999_20170323_110011192.xml");
		  
		  assertNotNull(inputFile);
		  
		  assertEquals(1085, inputFile.length());
		  
		  ex.getIn().setBody(inputFile);
		  
		  FileSizeFilterProcessor fileSizeFilterProcessor = new FileSizeFilterProcessor();
		  
		  //Confirm 0 will not set header
		  fileSizeFilterProcessor.setFileSizeInKilobytes(0);
		  fileSizeFilterProcessor.filterFilesLessThanSpecifiedSize(ex);
		  assertNull(ex.getIn().getHeader("fileSizeTooSmall"));
		  assertNull(ex.getIn().getHeader("fileSizeTooLarge"));
		  
		  //Confirm that file less than size will return header as true
		  fileSizeFilterProcessor.setFileSizeInKilobytes(2);
		  fileSizeFilterProcessor.filterFilesLessThanSpecifiedSize(ex);
		  assertEquals(true,ex.getIn().getHeader("fileSizeTooSmall"));	
		  
		  //Confirm that file less than size will return header as true
		  fileSizeFilterProcessor.setFileSizeInKilobytes(1);
		  fileSizeFilterProcessor.filterFilesGreaterThanSpecifiedSize(ex);
		  assertEquals(true,ex.getIn().getHeader("fileSizeTooLarge"));		  		  

		  ex.getIn().setHeader("fileSizeTooLarge", null);
		  
		  fileSizeFilterProcessor.setFileSizeInKilobytes(2);
		  fileSizeFilterProcessor.filterFilesGreaterThanSpecifiedSize(ex);
		  assertNull(ex.getIn().getHeader("fileSizeTooLarge"));	  		  

	}
	
}
