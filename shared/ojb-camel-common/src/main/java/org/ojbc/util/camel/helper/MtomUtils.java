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
package org.ojbc.util.camel.helper;

import java.io.IOException;

import javax.activation.DataHandler;

import org.apache.camel.Exchange;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.cxf.helpers.IOUtils;

public class MtomUtils {

	private static final Log log = LogFactory.getLog(MtomUtils.class);
	
	/**
	 * Get the MTOM attachment by attachmentId from an exchange. 
	 * @param exchange
	 * @param transactionNumber
	 * @param attachmentId
	 * @return
	 * @throws IOException
	 */
	public static byte[] getAttachment(Exchange exchange, String transactionNumber,
			String attachmentId) throws IOException {
		byte[] attachment;
		DataHandler dataHandler = exchange.getIn().getAttachment(StringUtils.substringAfter(attachmentId, "cid:"));
		
		if (dataHandler != null){
			attachment = IOUtils.readBytesFromStream(dataHandler.getInputStream());
		}
		else{
			log.error("No valid file found in the attachement for transaction " + 
					StringUtils.trimToEmpty(transactionNumber));
			throw new IllegalArgumentException("No file found in the attachement"); 
		}
		return attachment;
	}


}

