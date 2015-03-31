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
