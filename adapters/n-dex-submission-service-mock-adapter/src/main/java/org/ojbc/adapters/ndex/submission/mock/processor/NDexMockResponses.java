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
