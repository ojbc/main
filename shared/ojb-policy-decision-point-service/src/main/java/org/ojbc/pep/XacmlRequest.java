package org.ojbc.pep;

import org.w3c.dom.Document;

/**
 * A wrapper of a XACML request
 *
 */
public class XacmlRequest {
    
    private Document requestDocument;
    
    public XacmlRequest(Document requestDocument)
    {
        this.requestDocument = requestDocument;
    }
    
    public Document getRequestDocument() {
        return requestDocument;
    }

}
