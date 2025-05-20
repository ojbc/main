package org.ojbc.util.helper;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;

import javax.xml.stream.XMLEventReader;
import javax.xml.stream.XMLEventWriter;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLOutputFactory;
import javax.xml.stream.XMLStreamConstants;
import javax.xml.stream.events.XMLEvent;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Component;

@Component
public class MessageUtils {

	private static final Log log = LogFactory.getLog(MessageUtils.class);
	
	public String removeAttachments(InputStream in) throws Exception
	{
	    XMLInputFactory inputFactory = XMLInputFactory.newInstance();
	    XMLOutputFactory outputFactory = XMLOutputFactory.newInstance();

	    XMLEventReader reader = inputFactory.createXMLEventReader(in);
	    
	    ByteArrayOutputStream baos = new ByteArrayOutputStream();
	    
	    XMLEventWriter writer = outputFactory.createXMLEventWriter(baos);
	    XMLEvent event;

	    boolean deleteSection = false;
	    while (reader.hasNext()) {
	      event = reader.nextEvent();
	      
	      if (event.getEventType() == XMLStreamConstants.START_ELEMENT
	          && event.asStartElement().getName().toString()
	              .equalsIgnoreCase("{http://ojbc.org/IEPD/Extensions/IncidentReportStructuredPayload/1.0}IncidentDocument")) {
	        deleteSection = true;
	        continue;
	      } else if (event.getEventType() == XMLStreamConstants.END_ELEMENT
	          && (event.asEndElement().getName().toString()
	              .equalsIgnoreCase("{http://ojbc.org/IEPD/Extensions/IncidentReportStructuredPayload/1.0}IncidentDocument"))) {
	        deleteSection = false;
	        continue;
	      } else if (deleteSection) {
	        continue;
	      } else {
	        writer.add(event);
	      }
	     }
	    
	    log.debug("writer to string: " + baos.toString());
	    
	    if (baos != null && baos.size() > 0)
	    {
	    	return baos.toString();
	    }	
	    else
	    {
	    	log.error("Resulting documents is null or empty: " + baos.toString());
	    	
	    	return "";
	    }
	}
}
