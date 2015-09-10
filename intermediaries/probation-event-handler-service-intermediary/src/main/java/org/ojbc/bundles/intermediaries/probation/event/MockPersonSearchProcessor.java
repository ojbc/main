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
package org.ojbc.bundles.intermediaries.probation.event;

import java.io.InputStream;
import java.io.StringWriter;
import java.util.logging.Logger;

import org.apache.camel.Exchange;
import org.apache.commons.io.IOUtils;
import org.ojbc.util.xml.XmlUtils;
import org.w3c.dom.Document;


public class MockPersonSearchProcessor {
		
	private static final Logger logger = Logger.getLogger(MockPersonSearchProcessor.class.getName());
	
	
	public String mockPersonSearch(String personSearchRequest) throws Exception{
				
		InputStream responseInStream = getClass().getClassLoader().getResourceAsStream("mock/Person_EntityMergeResultMessage.xml");
		
		if(responseInStream == null){
			throw new Exception("responseInStream NULL!!!");
		}				
		
		StringWriter responseStringWriter = new StringWriter();
		
		IOUtils.copy(responseInStream, responseStringWriter);
		
		String personResponse = responseStringWriter.toString();
		
		logger.info("\n\n\n Mock Processor returning: \n\n" + personResponse);
				
		return personResponse;
	}
	  			
}
