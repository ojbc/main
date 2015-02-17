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
