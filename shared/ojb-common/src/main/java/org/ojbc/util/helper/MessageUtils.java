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
