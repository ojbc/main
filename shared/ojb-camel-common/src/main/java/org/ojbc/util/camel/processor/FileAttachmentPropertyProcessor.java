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
package org.ojbc.util.camel.processor;

import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * This processor works in tandem with the FileAttachmentProcessor.  It sets the currentXmlFilePath property
 * on the exchange so at a later point in the route, the XML can be attached in an email.
 * 
 */
public class FileAttachmentPropertyProcessor implements Processor
{
    Log log = LogFactory.getLog(this.getClass());

    @Override
    public void process(Exchange exchange) throws Exception
    {
        String currentXMLFilePath = (String) exchange.getIn().getHeader(Exchange.FILE_NAME_PRODUCED);
        log.debug("currentXMLFilePath=[" + currentXMLFilePath + "]");
        exchange.setProperty("currentXMLFilePath", currentXMLFilePath);
    }

}
