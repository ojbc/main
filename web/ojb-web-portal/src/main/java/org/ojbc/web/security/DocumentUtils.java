package org.ojbc.web.security;

import java.io.StringReader;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.w3c.dom.Document;
import org.xml.sax.InputSource;

public class DocumentUtils {
    private final static Log log = LogFactory.getLog(DocumentUtils.class);
 
    
    /**
     * 
     * @param xmlString
     * @return Document representing the XML String. null if the XML String is not valid.
     */
    public static Document getDocumentFromXmlString(String xmlString) {
        InputSource inputSource  = new InputSource( new StringReader( xmlString )) ; 
        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        dbf.setNamespaceAware(true);
        
        Document document = null; 
        try {
            DocumentBuilder documentBuilder = dbf.newDocumentBuilder();
            document = documentBuilder.parse(inputSource);
        } catch (Exception e) {
            log.error("Faild to create a document out of the xml string: " 
                    + StringUtils.trimToEmpty(xmlString), e);
        }
 
        return document;
    }
}
