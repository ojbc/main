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
package org.ojbc.bundles.adapters.fbi.ebts.util;

import org.apache.commons.lang.StringUtils;
import org.ojbc.util.xml.XmlUtils;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class FBIEbtsUtils {

	public static String processTransactionStatusText(Document doc) throws Exception
	{
		String transactionStatusText = "";
		
		NodeList transactionStatusTextNodes = XmlUtils.xPathNodeListSearch(doc, "/nistbio:NISTBiometricInformationExchangePackage/nistbio:PackageDescriptiveTextRecord/nistbio:UserDefinedDescriptiveDetail/ebts:DomainDefinedDescriptiveFields/ebts:RecordTransactionData/ebts:TransactionResponseData/ebts:TransactionStatusText");
		
		if (transactionStatusTextNodes != null) {
	        int length = transactionStatusTextNodes.getLength();
	        for (int i = 0; i < length; i++)
	        {
	            if (transactionStatusTextNodes.item(i).getNodeType() == Node.ELEMENT_NODE) 
	            {
	                Element el = (Element) transactionStatusTextNodes.item(i);
	                transactionStatusText = transactionStatusText + el.getTextContent() + " | "; 
	            }
	        }
	       
	        transactionStatusText = StringUtils.substringBeforeLast(transactionStatusText, "|").trim();
		}
		
		return transactionStatusText;
	}
	
}
