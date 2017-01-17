package de.omikron.helper.client;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.servlet.http.HttpServletRequest;

import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

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
		final StringBuffer requestURL = new StringBuffer();
		requestURL.append(trimUrl(settings.getUrl()));
		requestURL.append(extractService(request));
		String queryString = request.getQueryString();
		if (queryString != null) {
			requestURL.append("?");
			requestURL.append(request.getQueryString());
		}
		HttpGet get = new HttpGet(requestURL.toString());
		Map<String, String> header = extractHeader(request);
		for (Entry<String, String> e : header.entrySet()) {
			get.addHeader(e.getKey(), e.getValue());
		}

		CloseableHttpClient httpclient = HttpClients.createDefault();
		ResponseHandler<String> responseHandler = new ResponseHandler<String>() {

			public String handleResponse(final HttpResponse response) throws ClientProtocolException, IOException {
				int status = response.getStatusLine().getStatusCode();
				if (status >= 200 && status < 300) {
					HttpEntity entity = response.getEntity();
					return entity != null ? EntityUtils.toString(entity) : null;
				} else {
					throw new ClientProtocolException("Unexpected response status: " + status);
				}
			}

		};
		System.out.println("request" + get);
		Header[] allHeaders = get.getAllHeaders();
		System.out.println(Arrays.toString(allHeaders));
		String result = httpclient.execute(get, responseHandler);
		System.out.println("result: " + result);
		return result;
	}

	private static Map<String, String> extractHeader(final HttpServletRequest request) {
		Map<String, String> header = new HashMap<String, String>();
		List<String> list = Collections.list(request.getHeaderNames());
		System.out.println("Header-------------");
		for (String h : list) {
			System.out.println(h + ": " + request.getHeader(h));
			header.put(h, request.getHeader(h));
			// header.put(h, request.getHeaders(h));
		}
		return header;
	}

	@Deprecated
	private static String copyParameter(final HttpServletRequest request) {
		System.out.println("QueryString: " + request.getQueryString());
		System.out.println("Params------------------");
		String[] split = request.getQueryString().split("&");

		List<String> params = new ArrayList<String>();
		for (int i = 0; i < split.length; i++) {
			String param = split[i];
			System.out.println("\t" + param);
			if (i % 2 == 0) {
				params.add(param);
			}
		}

		System.out.println(params);

		if (!params.isEmpty()) {

			final StringBuffer parameterString = new StringBuffer();
			parameterString.append("?");

			for (int i = 0; i < split.length; i++) {
				if ((i % 2) - 1 == 0)
					continue;
				if (i != 0) {
					parameterString.append("&");
				}
				String param = split[i];
				System.out.println(param + "=" + request.getParameter(param));
				parameterString.append(param + "=" + request.getParameter(param));

			}
			// parameterString.append("&" + authString);
			System.out.println("parameterString: " + parameterString);
			return parameterString.toString();
		} else {
			return "";
		}
	}

	private static String trimUrl(String ffURL) {
		if (ffURL.charAt(ffURL.length() - 1) != '/') {
			ffURL += "/";
		}
		return ffURL;
	}

	private static String extractService(final HttpServletRequest request) {
		String path = request.getPathInfo();
		return path.substring(path.lastIndexOf("/") + 1, path.length());
	}

	/**
	 * Creates the HTTP connection.
	 * 
	 * @param url
	 * @return the HttpURLConnection
	 * @throws IOException
	 */
	protected static HttpURLConnection getConnection(final String url, Map<String, String> headers) throws IOException {

		final URL urlObj = new URL(url);
		final HttpURLConnection conn = (HttpURLConnection) urlObj.openConnection();
		conn.setDoInput(true);
		conn.setDoOutput(true);
		conn.setUseCaches(false);
		conn.setInstanceFollowRedirects(false);
		conn.setRequestMethod("GET");

		// Copy headers
		System.out.println("Headers: -----------------------");
		for (Entry<String, String> header : headers.entrySet()) {
			System.out.println("\t" + header);
			conn.setRequestProperty(header.getKey(), header.getValue());
		}

		System.out.println("RequestProperties---------------------");
		Map<String, List<String>> requestProperties = conn.getRequestProperties();
		for (Entry<String, List<String>> e : requestProperties.entrySet()) {
			System.out.println(e.getKey() + ":\t" + e.getValue().toString());
		}
		System.out.println("/RequestProperties---------------------");
		conn.setConnectTimeout(CONNECTION_TIMEOUT);
		conn.setReadTimeout(READ_TIMEOUT);

		return conn;
	}

	private static String readResponse(final HttpURLConnection conn) throws IOException {
		System.out.println("HttpURLConnection" + conn.toString());
		final int respCode = conn.getResponseCode();
		System.out.println("respCode: " + respCode);
		if (respCode == HttpURLConnection.HTTP_OK || respCode == HttpURLConnection.HTTP_NOT_MODIFIED) {
			return readData(conn.getInputStream());
		}
		return "";
	}

	private static String readData(final InputStream servIn) throws IOException {
		int b;
		final ByteArrayOutputStream bOut = new ByteArrayOutputStream();
		final byte[] data = new byte[1024];

		while ((b = servIn.read(data)) != -1) {
			bOut.write(data, 0, b);
		}
		final byte[] bArray = bOut.toByteArray();
		final String result = new String(bArray);
		System.out.println(result);
		return result;
	}
}
