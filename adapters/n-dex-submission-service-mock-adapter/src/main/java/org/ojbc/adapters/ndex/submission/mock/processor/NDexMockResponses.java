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
package org.ojbc.adapters.ndex.submission.mock.processor;

import org.apache.camel.Exchange;

public class NDexMockResponses {

	public String returnSuccessResponse(Exchange ex)
	{
		StringBuffer sb = new StringBuffer();
		
	   sb.append("  <ns2:submitNDExIEPD1_0Response xmlns:ns2=\"urn://com.raytheon.ndex.ai.webservice\">");
	   sb.append("       <ns2:return>");
	   sb.append("          <SubmitResponseDetailsArray xmlns=\"java:com.raytheon.ndex.ai.webservice\">");
	   sb.append("               <MessageNumber>0</MessageNumber>");
	   sb.append("               <PackageNumber>0</PackageNumber>");
	   sb.append("               <PackageResultCode>0</PackageResultCode>");
	   sb.append("               <PackageResultMessage>success</PackageResultMessage>");
	   sb.append("           </SubmitResponseDetailsArray>");
	   sb.append("           <ns1:FileLevelResultCode xmlns:ns1=\"java:com.raytheon.ndex.ai.webservice\">0</ns1:FileLevelResultCode>");
	   sb.append("           <ns1:FileLevelResultMessage xmlns:ns1=\"java:com.raytheon.ndex.ai.webservice\">success</ns1:FileLevelResultMessage>");
	   sb.append("       </ns2:return>");
	   sb.append(" </ns2:submitNDExIEPD1_0Response>");
		
		return sb.toString();
	}

	public String returnErrorResponse(Exchange ex)
	{
		return null;
	}

}
