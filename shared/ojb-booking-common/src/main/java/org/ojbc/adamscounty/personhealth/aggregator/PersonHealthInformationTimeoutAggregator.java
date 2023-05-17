package org.ojbc.adamscounty.personhealth.aggregator;

import java.util.List;

import org.apache.camel.Exchange;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class PersonHealthInformationTimeoutAggregator {
	private static final Log log = LogFactory.getLog( PersonHealthInformationTimeoutAggregator.class );
	
	public void processPHIQueryTimeout(Exchange groupedExchange) throws Exception
	{
		@SuppressWarnings("unchecked")
		List<Exchange> grouped = groupedExchange.getIn().getBody(List.class);
		
		String aggregatedCompletedBy = (String)groupedExchange.getProperty(Exchange.AGGREGATED_COMPLETED_BY);
		
		//When there is a timeout, this means we have an error condition.  The PHI response service did not respond.
		//We get the booking number header to send in an email.
		if (aggregatedCompletedBy != null && aggregatedCompletedBy.equals("timeout"))
		{
			log.info("PHI Query Completed by timeout.");
			
			if (grouped.get(0) != null)
			{	
				String bookingNumber = (String)grouped.get(0).getIn().getHeader("bookingNumber");
				groupedExchange.getIn().setHeader("bookingNumber", bookingNumber);
			}	
		}
	}
}
