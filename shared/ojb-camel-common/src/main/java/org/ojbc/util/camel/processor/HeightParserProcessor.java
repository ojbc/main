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

import org.apache.camel.Exchange;
import org.apache.commons.lang3.StringUtils;
import org.ojbc.util.helper.HeightUtils;
import org.ojbc.util.xml.XmlUtils;
import org.w3c.dom.Document;

public class HeightParserProcessor {

	public void setHeightParametersForXSLT(Exchange exchange) throws Exception
	{
		
		Document personSearchDocument = (Document) exchange.getIn().getBody();
		
		String height = XmlUtils.xPathStringSearch(personSearchDocument,"/psr-doc:PersonSearchRequest/psr:Person/nc:PersonHeightMeasure/nc:MeasureText");
		String heightRangeStart= XmlUtils.xPathStringSearch(personSearchDocument,"/psr-doc:PersonSearchRequest/psr:Person/nc:PersonHeightMeasure/nc:MeasureRangeValue/nc:RangeMinimumValue");
		String heightRangeEnd = XmlUtils.xPathStringSearch(personSearchDocument,"/psr-doc:PersonSearchRequest/psr:Person/nc:PersonHeightMeasure/nc:MeasureRangeValue/nc:RangeMaximumValue");
		
		if (StringUtils.isNotBlank(height))
		{
			String heightArgument = returnHeightArgumentFromHeight(height);
			exchange.getIn().setHeader("heightArgument", heightArgument);			
		}	
		
		if (StringUtils.isNotBlank(heightRangeStart))
		{
			String heightArgument = returnHeightArgumentFromHeight(heightRangeStart);
			exchange.getIn().setHeader("heightStartRangeArgument", heightArgument);			
		}	

		if (StringUtils.isNotBlank(heightRangeEnd))
		{
			String heightArgument = returnHeightArgumentFromHeight(heightRangeEnd);
			exchange.getIn().setHeader("heightEndRangeArgument", heightArgument);			
		}	

	}
	
	private String returnHeightArgumentFromHeight(String height)
	{
		String heightInFeet = HeightUtils.returnFeetFromInches(Integer.valueOf(height));
		String heightRemainingInchesFromInches = HeightUtils.returnRemainingInchesFromInches(Integer.valueOf(height));
		
		String heightArgument = heightInFeet + "'" + heightRemainingInchesFromInches + "\"";
		
		return heightArgument;
	}
}
