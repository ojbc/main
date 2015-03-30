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
