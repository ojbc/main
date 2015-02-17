package org.ojbc.util.fedquery;

import org.w3c.dom.Element;

/**
 * 
 * Interface for adapter data sources.  This allows a data source owner to handle search and query requests without having to
 * worry about implementing the web services protocol functionality.
 *
 */
public interface FederatedQueryDataSourceAdapter {
	
	/**
	 * Perform a search of the data source.  A search takes in broad parameters and returns a list of possible matches.  Consult
	 * the service specification for the specific adapter being developed for the structure of the DOM elements passed in and
	 * returned.
	 * @param userAssertion the SAML assertion, originally formed by the user's IDP, that the data source can use to make
	 * access control decisions
	 * @param searchRequest the search parameters
	 * @return the list of matching possibles
	 * @throws Exception
	 */
	public Element search(Element userAssertion, Element searchRequest) throws Exception;
	
	/**
	 * Perform a query of the data source.  A query takes in a unique identifier for a record and returns the detail for that record.
	 * Typically, a query results from a previous search, in which the user selects one record from the list of possibles and
	 * asks for details.  Consult the service specification for the specific adapter being developed for the structure of the DOM
	 * elements passed in and returned.
	 * @param userAssertion the SAML assertion, originally formed by the user's IDP, that the data source can use to make
	 * @param queryRequest the query parameters
	 * @return the detail for the requested record
	 * @throws Exception
	 */
	public Element query(Element userAssertion, Element queryRequest) throws Exception;

}
