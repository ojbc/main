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
package org.ojbc.connectors.custodyrelease;

import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.ojbc.util.camel.processor.FileResendProcessor;
import org.springframework.stereotype.Service;


@Service
public class ResendCustodyReleaseExchangeImpl implements ResendCustodyReleaseExchangeInterface {

	private final Log log = LogFactory.getLog(this.getClass());
	
	private String pathToInputFolder;
	
	private String pathToProcessedFolder;
	
	private String fileNamePrefix;
	
	@Override
	public Response resendExchange(String recordNumber) {

		log.info("Record number to process: " + recordNumber);
		
		try {
			FileResendProcessor.resendFile(recordNumber, pathToInputFolder, pathToProcessedFolder, fileNamePrefix);
		} catch (Exception e1) {
			e1.printStackTrace();
			return Response.status(Status.BAD_REQUEST).build();
		}
		
		return Response.status(Status.OK).build();
	}


	public String getPathToInputFolder() {
		return pathToInputFolder;
	}


	public void setPathToInputFolder(String pathToInputFolder) {
		this.pathToInputFolder = pathToInputFolder;
	}


	public String getPathToProcessedFolder() {
		return pathToProcessedFolder;
	}


	public void setPathToProcessedFolder(String pathToProcessedFolder) {
		this.pathToProcessedFolder = pathToProcessedFolder;
	}


	public String getFileNamePrefix() {
		return fileNamePrefix;
	}


	public void setFileNamePrefix(String fileNamePrefix) {
		this.fileNamePrefix = fileNamePrefix;
	}

}