package org.ojbc.processor.error;


import java.io.File;
import java.util.HashMap;

import junit.framework.Assert;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.cxf.helpers.XMLUtils;
import org.junit.Test;
import org.w3c.dom.Document;

public class TestMergeNotificationMaxRecordProcessor {

	private static final Log log = LogFactory.getLog(MergeNotificationMaxRecordProcessor.class);
	
	@Test
	public void testReturnMergeNotificationErrorMessageTooManyRecords() throws Exception
	{
		MergeNotificationMaxRecordProcessor mergeNotificationMaxRecordProcessor  = new MergeNotificationMaxRecordProcessor();
		
		HashMap<String, String> federatedQueryEndpointMap = new HashMap<String, String>();
		federatedQueryEndpointMap.put("{http://ojbc.org/Services/WSDL/FirearmSearchRequestService/1.0}SubmitFirearmSearchRequest-Maui","firearmSearchRequestServiceRMSEndpoint");
		federatedQueryEndpointMap.put("{http://ojbc.org/Services/WSDL/FirearmSearchRequestService/1.0}SubmitFirearmSearchRequest","firearmSearchRequestServiceRMSEndpoint");
		
		mergeNotificationMaxRecordProcessor.setFederatedQueryEndpointMap(federatedQueryEndpointMap);

		//Read the firearm search request file from the file system
	    File inputFile = new File("src/test/resources/xmlInstances/firearmSearchResults/firearmSearchResults.xml");

		Document resultsDocument = XMLUtils.parse(inputFile);
		
		String errorMessage = mergeNotificationMaxRecordProcessor.returnMergeNotificationErrorMessageTooManyRecords(resultsDocument, "1");
	
		log.debug(errorMessage);
		
		Assert.assertEquals("<exc:EntityMergeResultMessage xmlns:exc=\"http://nij.gov/IEPD/Exchange/EntityMergeResultMessage/1.0\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xmlns:srer=\"http://ojbc.org/IEPD/Extensions/SearchRequestErrorReporting/1.0\" xmlns:intel=\"http://niem.gov/niem/domains/intelligence/2.1\" xmlns:srm=\"http://ojbc.org/IEPD/Extensions/SearchResultsMetadata/1.0\"> <exc:SearchResultsMetadataCollection> 	<srm:SearchResultsMetadata> 		<srer:SearchRequestError> 			<srer:ErrorText>The search results contained too many records, please refine your search.  The maximum combined number of records that will display is 1.<br />The Some County search results contained 3 records.<br />The  search results contained 0 records.<br /></srer:ErrorText> 			<intel:SystemName>All Systems</intel:SystemName> 		</srer:SearchRequestError> 	</srm:SearchResultsMetadata> </exc:SearchResultsMetadataCollection></exc:EntityMergeResultMessage>",errorMessage);
	}
}
