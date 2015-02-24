package org.search.ojb.broker;

import org.apache.camel.Exchange;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.search.ojb.staticmock.StaticMockQuery;
import org.w3c.dom.Document;

public class StaticMockAdapterProcessor {

	private static final Log log = LogFactory.getLog( StaticMockAdapterProcessor.class );
	private StaticMockQuery staticMockQuery;
	
    public void search(Exchange exchange) throws Exception
    {
        log.debug("Enter method - Call Response Handler for service");
        Document requestDocument = exchange.getIn().getBody(Document.class);
        Document response = staticMockQuery.searchDocuments(requestDocument);
        exchange.getIn().setBody(response);
    }
    
    public void query(Exchange exchange) throws Exception
    {
        log.debug("Enter method - Call Response Handler for service");
        Document requestDocument = exchange.getIn().getBody(Document.class);
        Document response = staticMockQuery.queryDocuments(requestDocument).get(0).getDocument();
        exchange.getIn().setBody(response);
    }

	public StaticMockQuery getStaticMockQuery() {
		return staticMockQuery;
	}

	public void setStaticMockQuery(StaticMockQuery staticMockQuery) {
		this.staticMockQuery = staticMockQuery;
	}
}
