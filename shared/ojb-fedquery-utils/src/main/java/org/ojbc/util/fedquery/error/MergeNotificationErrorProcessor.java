package org.ojbc.util.fedquery.error;

public class MergeNotificationErrorProcessor {

	public static String returnMergeNotificationErrorMessage()
	{
		StringBuffer sb = new StringBuffer();
		
		sb.append("<exc:EntityMergeResultMessage");
		sb.append(" xmlns:exc=\"http://nij.gov/IEPD/Exchange/EntityMergeResultMessage/1.0\"");
		sb.append(" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\"");
		sb.append(" xmlns:srer=\"http://ojbc.org/IEPD/Extensions/SearchRequestErrorReporting/1.0\""); 
		sb.append(" xmlns:intel=\"http://niem.gov/niem/domains/intelligence/2.1\"");
		sb.append(" xmlns:srm=\"http://ojbc.org/IEPD/Extensions/SearchResultsMetadata/1.0\">");
		sb.append(" <exc:SearchResultsMetadataCollection>");
		sb.append(" 	<srm:SearchResultsMetadata>");
		sb.append(" 		<srer:SearchRequestError>");
		sb.append(" 			<srer:ErrorText>The source systems timed out or returned an error.  Please try your search again later.</srer:ErrorText>");
		sb.append(" 			<intel:SystemName>All Systems</intel:SystemName>");
		sb.append(" 		</srer:SearchRequestError>");
		sb.append(" 	</srm:SearchResultsMetadata>");
		sb.append(" </exc:SearchResultsMetadataCollection>");	
		sb.append("</exc:EntityMergeResultMessage>");
		
		return sb.toString();
	}
	
	public static String returnMergeNotificationErrorMessageEntityResolution()
	{
		StringBuffer sb = new StringBuffer();
		
		sb.append("<exc:EntityMergeResultMessage");
		sb.append(" xmlns:exc=\"http://nij.gov/IEPD/Exchange/EntityMergeResultMessage/1.0\"");
		sb.append(" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\"");
		sb.append(" xmlns:srer=\"http://ojbc.org/IEPD/Extensions/SearchRequestErrorReporting/1.0\""); 
		sb.append(" xmlns:intel=\"http://niem.gov/niem/domains/intelligence/2.1\"");
		sb.append(" xmlns:srm=\"http://ojbc.org/IEPD/Extensions/SearchResultsMetadata/1.0\">");
		sb.append(" <exc:SearchResultsMetadataCollection>");
		sb.append(" 	<srm:SearchResultsMetadata>");
		sb.append(" 		<srer:SearchRequestError>");
		sb.append(" 			<srer:ErrorText>The Entity Resolution Service timed out or had an error condition.  Please try your search again later.</srer:ErrorText>");
		sb.append(" 			<intel:SystemName>Entity Resolution</intel:SystemName>");
		sb.append(" 		</srer:SearchRequestError>");
		sb.append(" 	</srm:SearchResultsMetadata>");
		sb.append(" </exc:SearchResultsMetadataCollection>");	
		sb.append("</exc:EntityMergeResultMessage>");
		
		return sb.toString();
	}
}
