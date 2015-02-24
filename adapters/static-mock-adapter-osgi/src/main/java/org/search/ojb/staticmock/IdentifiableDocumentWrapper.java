package org.search.ojb.staticmock;

import org.w3c.dom.Document;

/**
 * A wrapper class for a DOM Document that includes a unique identifier, which allows us to retrieve it later.
 *
 */
public class IdentifiableDocumentWrapper {
    
    private Document document;
    private String id;
    
    public IdentifiableDocumentWrapper(Document document, String id)
    {
        this.document = document;
        this.id = id;
    }

    public Document getDocument() {
        return document;
    }

    public String getId() {
        return id;
    }
    
}
