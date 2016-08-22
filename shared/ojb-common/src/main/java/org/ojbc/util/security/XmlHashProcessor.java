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
package org.ojbc.util.security;

import org.apache.commons.lang.StringUtils;
import org.ojbc.util.helper.Hash;
import org.ojbc.util.xml.XmlUtils;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

public class XmlHashProcessor {

	private String salt;
	
	/**
	 * This method will hash the value of an XML element.  The xpath should resolve using namespaces
	 * found in the OJBC Namespace context.  
	 * 
	 * @param xmlDocument
	 * @param xPath
	 * @param hashType MD5,SHA-256 OR SHA1
	 * @throws Exception
	 */
	public void hashXMLandSaltElement(Document xmlDocument, String xPath, String hashType) throws Exception
	{
		if (StringUtils.isBlank(hashType))
		{
			throw new IllegalArgumentException("Hash Type is not valid.");	
		}	
		
		//Get element value
		String elementValue = XmlUtils.xPathStringSearch(xmlDocument, xPath);
		
		String hashedElementValue = "";
		
		//Hash the value
		if (StringUtils.equals(hashType, "SHA-256"))
		{		
			hashedElementValue = Hash.sha256WithSalt(elementValue, salt);
		} 
		else if (StringUtils.equals(hashType, "MD5"))
		{
			hashedElementValue = Hash.md5WithSalt(elementValue, salt);
		} 
		else if (StringUtils.equals(hashType, "SHA1"))
		{
			hashedElementValue = Hash.sha1WithSalt(elementValue, salt);
		} 
		else 
		{
			throw new IllegalArgumentException("Hash Type is not valid.");
		}
		
		//Update element value with hashed value
		Element elementToHash = (Element) XmlUtils.xPathNodeSearch(xmlDocument, xPath);
		
		elementToHash.setTextContent(hashedElementValue);
	}

	public String getSalt() {
		return salt;
	}

	public void setSalt(String salt) {
		this.salt = salt;
	}
	
}
