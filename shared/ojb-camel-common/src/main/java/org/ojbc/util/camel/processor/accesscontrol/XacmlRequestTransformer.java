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
package org.ojbc.util.camel.processor.accesscontrol;

import java.io.ByteArrayInputStream;

import javax.xml.transform.sax.SAXSource;

import org.apache.commons.codec.CharEncoding;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.ojbc.util.camel.helper.OJBUtils;
import org.ojbc.util.xml.XsltTransformer;
import org.w3c.dom.Document;
import org.xml.sax.InputSource;

public class XacmlRequestTransformer{ 

	private XsltTransformer xsltTransformer;
		
	private static final Log log = LogFactory.getLog(XacmlRequestTransformer.class);

	public XacmlRequestTransformer() {
		
		xsltTransformer = new XsltTransformer();
	}
	
	public Document getTransformedXacmlQueryRequest(String requestXmlContent, 
			String xslStringContent, String xslUrlExtFormForSystemId) throws Exception{
		
		return convertXmlRequestToXacmlRequest(requestXmlContent, xslStringContent, xslUrlExtFormForSystemId);
	}
			
	
	private Document convertXmlRequestToXacmlRequest(String pXmlRequestContentString, 
			String xslContent, String xslUrlExtFormForSystemId) throws Exception{
				
		SAXSource requestXmlSaxInputSource = getSaxSourceForXmlRequestContent(pXmlRequestContentString);
				
		SAXSource xslSaxSource = getSaxSourceForXsl(xslContent, xslUrlExtFormForSystemId);		
				
		String xacmlRequestString = xsltTransformer.transform(requestXmlSaxInputSource, 
				xslSaxSource, null); 
		
		Document xacmlRequestDoc = OJBUtils.loadXMLFromString(xacmlRequestString);
		
		return xacmlRequestDoc;
	}

		
	
	private SAXSource getSaxSourceForXmlRequestContent(String requestContent){
		
		ByteArrayInputStream requestByteInStream = new ByteArrayInputStream(requestContent.getBytes());		
		InputSource requestInputSource = new InputSource(requestByteInStream);
		requestInputSource.setEncoding(CharEncoding.UTF_8);		
		SAXSource requestXmlSaxInputSource = new SAXSource(requestInputSource);
		
		log.info("Got saxSource cor xml request string");
		
		return requestXmlSaxInputSource;
	}
	
	
	private SAXSource getSaxSourceForXsl(String xslFileStringContent, String xslUrlExtFormForSystemId) {
		
		try {			
			
			InputSource inputSource = new InputSource(xslFileStringContent);
			inputSource.setEncoding(CharEncoding.UTF_8);
			
			SAXSource saxInputSource = new SAXSource(inputSource);
			
			//need to setSystemId because xsl needs this set in order for to <import> to know where to look to load relative paths
			saxInputSource.setSystemId(xslUrlExtFormForSystemId);
			
			return saxInputSource;
			
		} catch (Exception e) {
			throw new RuntimeException("Unable to read XSL: " + xslFileStringContent , e);
		}
	}


}

