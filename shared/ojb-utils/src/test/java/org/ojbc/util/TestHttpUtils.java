package org.ojbc.util;

import static org.junit.Assert.assertEquals;

import java.io.IOException;

import org.apache.http.HttpException;
import org.apache.http.HttpRequest;
import org.apache.http.HttpResponse;
import org.apache.http.entity.StringEntity;
import org.apache.http.localserver.LocalTestServer;
import org.apache.http.protocol.HttpContext;
import org.apache.http.protocol.HttpRequestHandler;
import org.junit.Test;
import org.ojbc.util.helper.HttpUtils;

public class TestHttpUtils {
	
	@Test
	public void test() throws Exception {
		final String responseString = "yeppers";
		LocalTestServer server = new LocalTestServer(null, null);
		server.register("/test", new HttpRequestHandler() {
			@Override
			public void handle(HttpRequest request, HttpResponse response, HttpContext context) throws HttpException, IOException {
				response.setEntity(new StringEntity(responseString));
			}
		});
		server.start();
		String host = server.getServiceAddress().getHostName();
		int port = server.getServiceAddress().getPort();
		String response = HttpUtils.post("ok?", "http://" + host + ":" + port + "/test");
		assertEquals(responseString, response);
	}

}
