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
#set( $symbol_pound = '#' )
#set( $symbol_dollar = '$' )
#set( $symbol_escape = '\' )
package ${package};

import java.io.InputStream;

import javax.xml.transform.Source;
import javax.xml.transform.sax.SAXSource;

import org.apache.camel.Exchange;

/**
 * An OJB connector processor intended to be used by a camel route
 * for custom processing of a message
 * 
 * @author SEARCH
 */
public class ConnectorFileProcessor{
				
	/**
	 * Perform any needed processing to reference or update the exhchange 
	 * headers or soap message body.
	 * 
	 * @param exchange
	 * 		The Camel Exchange object with the message payload
	 */
	public void processMessage(Exchange exchange) throws Exception{
					
		exchange.getOut().setBody(exchange.getIn().getBody());						
	}
						
}
