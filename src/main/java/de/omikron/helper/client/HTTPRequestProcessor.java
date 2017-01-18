package de.omikron.helper.client;

import java.io.IOException;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.servlet.http.HttpServletRequest;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

import de.omikron.helper.FFSecurity;
import de.omikron.helper.settings.FFSettings;

/**
 * HTTP request processing.
 * 
 * @author marco.buczilowski
 * 
 */
public class HTTPRequestProcessor {

	private static final int	READ_TIMEOUT		= 1000;
	private static final int	CONNECTION_TIMEOUT	= 1100;

	public static String retrieveResult(FFSettings settings, final HttpServletRequest request) throws IOException {

		// Build request String
		final StringBuffer requestURL = new StringBuffer();
		requestURL.append(trimUrl(settings.getUrl()));
		requestURL.append(extractService(request));

		// Add Parameters
		String queryString = request.getQueryString();
		if (queryString != null) {
			requestURL.append("?");
			requestURL.append(request.getQueryString());
			String authString = FFSecurity.getAuthString(settings, FFSecurity.AUTH_ADVANCED);
			requestURL.append("&" + authString);
		}

		// Build Request
		HttpGet get = new HttpGet(requestURL.toString());

		// Add Headers
		Map<String, String> header = extractHeader(request);
		for (Entry<String, String> e : header.entrySet()) {
			get.addHeader(e.getKey(), e.getValue());
		}

		ResponseHandler<String> responseHandler = new ResponseHandler<String>() {

			public String handleResponse(final HttpResponse response) throws ClientProtocolException, IOException {
				int status = response.getStatusLine().getStatusCode();
				System.out.println("Response: " + status);
				if (status >= 200 && status < 300) {
					HttpEntity entity = response.getEntity();
					return entity != null ? EntityUtils.toString(entity) : null;
				} else {
					throw new ClientProtocolException("Unexpected response status: " + status);
				}
			}

		};

		// request
		CloseableHttpClient httpclient = HttpClients.createDefault();
		System.out.println("Request: " + get);
		return httpclient.execute(get, responseHandler);
	}

	private static Map<String, String> extractHeader(final HttpServletRequest request) {
		Map<String, String> header = new HashMap<String, String>();
		List<String> list = Collections.list(request.getHeaderNames());
		for (String h : list) {
			header.put(h, request.getHeader(h));
			// header.put(h, request.getHeaders(h));
		}
		return header;
	}

	private static String trimUrl(String ffURL) {
		if (ffURL.charAt(ffURL.length() - 1) != '/') {
			ffURL += "/";
		}
		return ffURL;
	}

	public static String extractService(final HttpServletRequest request) {
		String path = request.getPathInfo();
		return path.substring(path.lastIndexOf("/") + 1, path.length());
	}

}
