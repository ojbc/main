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

import java.io.File;

import org.apache.camel.Exchange;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class FileSizeFilterProcessor {

	private double fileSizeInKilobytes;
	
	private Log log = LogFactory.getLog(this.getClass());
	
	/**
	 * This method will filter files less than a certain size.  If file size is 0, we just return.
	 * This helps out in scenarios where the downloaded file is corrupted and for example only has a few lines
	 * in it.  This results in all their records getting unsubscribed.
	 * 
	 * This processor sets a header which triggers an email alert.
	 * 
	 * @param ex
	 */
	public void filterFilesLessThanSpecifiedSize(Exchange ex)
	{
		if (fileSizeInKilobytes == 0)
		{
			return;
		}	
		
		File fileToProcess = ex.getIn().getBody(File.class);
		
		double bytes = fileToProcess.length();
		double kilobytes = (bytes / 1024);
		
		if (kilobytes < fileSizeInKilobytes)
		{
			ex.getIn().setHeader("fileSizeTooSmall", true);
		}	

	}
	
	public void filterFilesGreaterThanSpecifiedSize(Exchange ex)
	{
		log.info("Max file size in kilobytes: " + fileSizeInKilobytes);
		
		if (fileSizeInKilobytes == 0)
		{
			return;
		}	
		
		File fileToProcess = ex.getIn().getBody(File.class);
		
		double bytes = fileToProcess.length();
		double kilobytes = (bytes / 1024);
		
		log.info("File size in kilobytes: " + kilobytes);
		
		if (kilobytes > fileSizeInKilobytes)
		{
			ex.getIn().setHeader("fileSizeTooLarge", true);
		}	

	}	

	public double getFileSizeInKilobytes() {
		return fileSizeInKilobytes;
	}

	public void setFileSizeInKilobytes(double fileSizeInKilobytes) {
		this.fileSizeInKilobytes = fileSizeInKilobytes;
	}
	
	
}
