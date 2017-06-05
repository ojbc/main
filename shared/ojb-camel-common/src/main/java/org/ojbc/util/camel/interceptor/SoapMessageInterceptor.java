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
package org.ojbc.util.camel.interceptor;

import java.io.InputStream;

import org.apache.commons.io.IOUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.cxf.binding.soap.SoapMessage;
import org.apache.cxf.binding.soap.interceptor.AbstractSoapInterceptor;
import org.apache.cxf.interceptor.Fault;
import org.apache.cxf.io.CachedOutputStream;
import org.apache.cxf.phase.Phase;

/**
 * 
 * This CXF interceptor grabs the message and creates an exchange header called
 * 'entireSoapMessage' which will make the raw soap message available to other processors.
 * 
 * @author yogeshchawla
 *
 */
public class SoapMessageInterceptor extends AbstractSoapInterceptor
{
	private static final Log log = LogFactory.getLog(SoapMessageInterceptor.class);

    public SoapMessageInterceptor ()
    {
        super(Phase.RECEIVE);
    }

    @Override
    public void handleMessage ( SoapMessage message ) throws Fault
    {
    	
    	String entireSoapMessage = "";
    	
        try
        {
            // now get the request xml
            InputStream is = message.getContent ( InputStream.class );
            CachedOutputStream os = new CachedOutputStream ( );
            IOUtils.copy ( is, os );
            os.flush ( );
            message.setContent (  InputStream.class, os.getInputStream ( ) );
            is.close ( );
            
            entireSoapMessage = IOUtils.toString ( os.getInputStream ( ));

            log.debug ("The request is: " +  entireSoapMessage);
            os.close ( );
        }

        catch ( Exception ex )
        {
            ex.printStackTrace ( );
        }

    	message.getExchange().put("entireSoapMessage", entireSoapMessage);

    }
}
