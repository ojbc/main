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
 * Copyright 2012-2015 Open Justice Broker Consortium
 */
package org.ojbc.util.helper;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.StringWriter;

import org.apache.http.Consts;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClientBuilder;

/**
 * A set of utilities for dealing with Http streams.
 *
 */
public class HttpUtils {
	
	/**
	 * Send the specified payload to the specified http endpoint via POST.
	 * @param payload
	 * @param url
	 * @return the http response
	 * @throws Exception
	 */
	public static String post(String payload, String url) throws Exception {
		HttpClient client = HttpClientBuilder.create().build();
		HttpPost post = new HttpPost(url);
		post.setEntity(new StringEntity(payload, Consts.UTF_8));
		HttpResponse response = client.execute(post);
		HttpEntity reply = response.getEntity();
		StringWriter sw = new StringWriter();
		BufferedReader isr = new BufferedReader(new InputStreamReader(reply.getContent()));
		String line;
		while ((line = isr.readLine()) != null) {
			sw.append(line);
		}
		sw.close();
		return sw.toString();
	}

}
