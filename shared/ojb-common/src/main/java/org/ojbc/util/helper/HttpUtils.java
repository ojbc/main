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

import java.nio.charset.StandardCharsets;

import org.apache.hc.client5.http.classic.methods.HttpPost;
import org.apache.hc.client5.http.impl.classic.BasicHttpClientResponseHandler;
import org.apache.hc.client5.http.impl.classic.CloseableHttpClient;
import org.apache.hc.client5.http.impl.classic.CloseableHttpResponse;
import org.apache.hc.client5.http.impl.classic.HttpClients;
import org.apache.hc.core5.http.ContentType;
import org.apache.hc.core5.http.io.HttpClientResponseHandler;
import org.apache.hc.core5.http.io.entity.EntityUtils;
import org.apache.hc.core5.http.io.entity.StringEntity;
import org.ojbc.util.helper.HttpUtils.ResponseWrapper;

/**
 * A set of utilities for dealing with Http streams.
 *
 */
public class HttpUtils {
    
    private static final CloseableHttpClient CLIENT = HttpClients.createDefault();
	/**
	 * Send the specified payload to the specified http endpoint via POST.
	 * @param payload
	 * @param url
	 * @return the http response
	 * @throws Exception
	 */
    public static String post(String payload, String url) throws Exception {
        HttpPost post = new HttpPost(url);
        post.setEntity(new StringEntity(payload, StandardCharsets.UTF_8));
        return CLIENT.execute(post, new BasicHttpClientResponseHandler());
    }

    /**
     * Posts a SOAP payload to the given URL.
     *
     * @param payload SOAP XML as String
     * @param url     endpoint URL
     * @return ResponseWrapper containing HTTP status and response body
     * @throws Exception
     */
    public static ResponseWrapper postSoap(String payload, String url) throws Exception {
        HttpPost post = new HttpPost(url);
        post.setEntity(new StringEntity(payload, ContentType.TEXT_XML.withCharset(StandardCharsets.UTF_8)));
        post.setHeader("Accept", "text/xml");
        post.setHeader("Content-Type", "text/xml; charset=UTF-8");

        HttpClientResponseHandler<ResponseWrapper> handler = response -> {
            int status = response.getCode();
            String body = response.getEntity() != null
                    ? org.apache.hc.core5.http.io.entity.EntityUtils.toString(response.getEntity(), StandardCharsets.UTF_8)
                            : "";
            return new ResponseWrapper(status, body);
        };
        return CLIENT.execute(post, handler);
    }
    

    public static class ResponseWrapper {
        private final int status;
        private final String body;

        public ResponseWrapper(int status, String body) {
            this.status = status;
            this.body = body;
        }

        public int getStatus() {
            return status;
        }

        public String getBody() {
            return body;
        }

        public boolean isSoapFault() {
            return body != null && body.contains("<soap:Fault>");
        }

        @Override
        public String toString() {
            return "ResponseWrapper{" +
                    "status=" + status +
                    ", body='" + body + '\'' +
                    ", isSoapFault=" + isSoapFault() +
                    '}';
        }
    }    
}
