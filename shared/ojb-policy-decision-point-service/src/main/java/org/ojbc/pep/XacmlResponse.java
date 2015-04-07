package org.ojbc.pep;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.w3c.dom.Document;

/**
 * A response to a XACML request.
 *
 */
public class XacmlResponse {
    
    private List<XacmlResult> results;
    private Document responseDocument;
    
    XacmlResponse() {
        results = new ArrayList<XacmlResult>();
    }
    
    void addResult(XacmlResult result)
    {
        results.add(result);
    }
    
    void setDocument(Document d)
    {
        responseDocument = d;
    }
    
    public List<XacmlResult> getResults()
    {
        return Collections.unmodifiableList(results);
    }
    
    public Document getResponseDocument()
    {
        return responseDocument;
    }

}
