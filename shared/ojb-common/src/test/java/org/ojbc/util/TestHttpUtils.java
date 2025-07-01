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
package org.ojbc.util;


import static org.junit.jupiter.api.Assertions.assertEquals;

import java.io.IOException;

import org.apache.hc.core5.http.ClassicHttpRequest;
import org.apache.hc.core5.http.ClassicHttpResponse;
import org.apache.hc.core5.http.HttpException;
import org.apache.hc.core5.http.io.HttpRequestHandler;
import org.apache.hc.core5.http.io.entity.StringEntity;
import org.apache.hc.core5.http.protocol.HttpContext;
import org.apache.hc.core5.testing.classic.ClassicTestServer;
import org.junit.jupiter.api.Test;
import org.ojbc.util.helper.HttpUtils;

public class TestHttpUtils {
	
	@Test
	public void test() throws Exception {
		final String responseString = "yeppers";
		ClassicTestServer server = new ClassicTestServer();
		server.register("/test", new HttpRequestHandler() {
			@Override
			public void handle(ClassicHttpRequest request, ClassicHttpResponse response, HttpContext context) throws HttpException, IOException {
				response.setEntity(new StringEntity(responseString));
			}
		});
		server.start();
		String host = server.getInetAddress().getHostName();
		int port = server.getPort();
		String response = HttpUtils.post("ok?", "http://" + host + ":" + port + "/test");
		assertEquals(responseString, response);
	}

}
