package org.ojbc.util.camel.processor.audit;

import java.io.StringWriter;

import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.apache.camel.Header;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.ojbc.util.camel.security.saml.OJBSamlMap;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

public class FederatedQueryAuditLogger {

	private OJBSamlMap OJBSamlMap;
	
	private static final Log log = LogFactory.getLog(FederatedQueryAuditLogger.class);
	
	public void auditSearchParameters(@Header("tokenID") String tokenID)
	{
		//Get SAML token from hashmap
		Element assertion = (Element)OJBSamlMap.getToken(tokenID);

		if (assertion == null)
		{
			throw new IllegalStateException("No SAML token provided to log audit trail.");
		}
		
		StringBuffer sb = new StringBuffer();
		
		String fullAssertion = getStringFromDocument(assertion.getOwnerDocument());
		
		sb.append("Audit Message: " + fullAssertion);
		
		log.info(sb.toString());
		
	}

	public OJBSamlMap getOJBSamlMap() {
		return OJBSamlMap;
	}

	public void setOJBSamlMap(OJBSamlMap oJBSamlMap) {
		OJBSamlMap = oJBSamlMap;
	}
	
	//method to convert Document to String
	private String getStringFromDocument(Document doc)
	{
	    try
	    {
	       DOMSource domSource = new DOMSource(doc);
	       StringWriter writer = new StringWriter();
	       StreamResult result = new StreamResult(writer);
	       TransformerFactory tf = TransformerFactory.newInstance();
	       Transformer transformer = tf.newTransformer();
		   transformer.setOutputProperty(OutputKeys.INDENT, "yes");
		   transformer.setOutputProperty(
					"{http://xml.apache.org/xslt}indent-amount", "4");
		   transformer.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "yes");
	       transformer.transform(domSource, result);
	       return writer.toString();
	    }
	    catch(TransformerException ex)
	    {
	       ex.printStackTrace();
	       return null;
	    }
	} 
}
