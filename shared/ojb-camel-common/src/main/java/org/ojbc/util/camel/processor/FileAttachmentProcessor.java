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

import java.io.File;

import javax.activation.DataHandler;
import javax.activation.FileDataSource;

import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * This camel processor will accept a message with the property currentXMLFilePath specified.
 * It will then add that file as aan attachment to the exchange.
 */
public class FileAttachmentProcessor implements Processor
{
    Log log = LogFactory.getLog(this.getClass());

    @Override
    public void process(Exchange exchange) throws Exception
    {
        String currentXMLFilePath = (String) exchange.getProperty("currentXMLFilePath");
        log.debug("Attaching file.");
        log.debug("currentXMLFilePath=[" + currentXMLFilePath + "]");
        exchange.getIn().addAttachment("Bad.xml", new DataHandler(new FileDataSource(new File(currentXMLFilePath))));
    }

}
