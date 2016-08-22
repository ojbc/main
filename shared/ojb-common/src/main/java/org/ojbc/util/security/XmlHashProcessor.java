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
