package org.ojbc.intermediaries.sn.util;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class SubscriptionResponseBuilderUtil {

	private static final String DATE_FORMAT_NOW = "yyyy-MM-dd'T'HH:mm:ss";
	
	// TODO: build XML with a proper DOM
	public static String createSubscribeResponse() {
		StringBuffer sb = new StringBuffer();

		sb.append("<wsnt:SubscribeResponse xmlns:wsnt=\"http://docs.oasis-open.org/wsn/b-2\" ");
		sb.append("    xmlns:exchange=\"http://ojbc.org/IEPD/Exchange/Subscription_Response/1.0\" ");
		sb.append("    xmlns:ext=\"http://ojbc.org/IEPD/Extension/Subscription_Response/1.0\" >");
		sb.append("	<wsnt:SubscriptionReference>");
		sb.append("		<wsa:Address  xmlns:wsa=\"http://www.w3.org/2005/08/addressing\">http://www.ojbc.org/SubcribeNotify/</wsa:Address>");
		sb.append("		<wsnt:CurrentTime>" + now() + "</wsnt:CurrentTime>");
		sb.append("	</wsnt:SubscriptionReference>");
		sb.append("	<exchange:SubscriptionResponseMessage>");
		sb.append("		<ext:SubscriptionCreatedIndicator>true</ext:SubscriptionCreatedIndicator>");
		sb.append("	</exchange:SubscriptionResponseMessage>");
		sb.append("</wsnt:SubscribeResponse>");

		return sb.toString();
	}

	public static String createUnsubscribeResponse() {
		StringBuffer sb = new StringBuffer();

		sb.append("<wsnt:UnsubscribeResponse xmlns:wsnt=\"http://docs.oasis-open.org/wsn/b-2\" />");

		return sb.toString();

	}
	
	private static String now() {
		Calendar cal = Calendar.getInstance();
		SimpleDateFormat sdf = new SimpleDateFormat(DATE_FORMAT_NOW);
		return sdf.format(cal.getTime());
	}

	
}
