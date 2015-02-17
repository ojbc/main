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
import org.apache.http.impl.client.DefaultHttpClient;

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
		HttpClient client = new DefaultHttpClient();
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
