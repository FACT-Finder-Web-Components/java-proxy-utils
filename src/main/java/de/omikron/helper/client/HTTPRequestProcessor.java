package de.omikron.helper.client;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Properties;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.codec.digest.DigestUtils;

/**
 * HTTP request processing.
 * 
 * @author marco.buczilowski
 * 
 */
public class HTTPRequestProcessor {
	/*
	 * CONSTANTS
	 */
	private static final int	READ_TIMEOUT		= 1000;
	private static final int	CONNECTION_TIMEOUT	= 1100;

	/**
	 * Creates the HTTP connection.
	 * 
	 * @param url
	 * @return the HttpURLConnection
	 * @throws IOException
	 */
	protected static HttpURLConnection getConnection(final String url) throws IOException {

		final URL urlObj = new URL(url);
		final HttpURLConnection conn = (HttpURLConnection) urlObj.openConnection();
		/*
		 * connection settings
		 */
		conn.setDoInput(true);
		conn.setDoOutput(true);
		conn.setUseCaches(false);
		conn.setInstanceFollowRedirects(false);
		conn.setRequestMethod("GET");

		conn.setConnectTimeout(CONNECTION_TIMEOUT);
		conn.setReadTimeout(READ_TIMEOUT);

		return conn;
	}

	/**
	 * Authentification string.
	 * 
	 * @param authentificationType
	 * @param username
	 * @param password
	 * @param prefix
	 * @param postfix
	 * @return getAuthString
	 */
	protected static String getAuthString(final String authentificationType, final String username,
			final String password, final String prefix, final String postfix) {
		final StringBuffer authentificationInfo = new StringBuffer();

		if (authentificationType != null && authentificationType.toLowerCase().equals("simple")) {
			authentificationInfo.append("username=" + username);
			authentificationInfo.append("&password=" + DigestUtils.md5Hex(password));
		} else {
			authentificationInfo.append("username=" + username);
			final long time = System.currentTimeMillis();

			final String hashPassword = DigestUtils
					.md5Hex(prefix + Long.toString(time) + DigestUtils.md5Hex(password) + postfix);
			authentificationInfo.append("&password=" + hashPassword);
			authentificationInfo.append("&timestamp=" + time);
		}
		return authentificationInfo.toString();
	}

	/**
	 * Retrieves Suggest entries.
	 * 
	 * @param wsAccess
	 * @param request
	 * @return JSONSuggestResults
	 * @throws IOException
	 */
	public static String retrieveJSONResult(final WebserviceAccess wsAccess, final HttpServletRequest request)
			throws IOException {
		/*
		 * get http url
		 */
		String ffURL = wsAccess.getServiceProperties().getProperty(WebserviceAccess.WEB_SERVICE_HTTP_ACCESS_URL);
		/*
		 * Authentification information
		 */
		Properties props = wsAccess.getServiceProperties();
		final String authentificationType = props.getProperty(WebserviceAccess.WEB_SERVICE_AUTH_TYPE);
		final String authentificationPrefix = props.getProperty(WebserviceAccess.WEB_SERVICE_AUTH_PREFIX);
		final String authentificationPostfix = props.getProperty(WebserviceAccess.WEB_SERVICE_AUTH_POSTFIX);
		final String authentificationUsername = props.getProperty(WebserviceAccess.WEB_SERVICE_AUTH_USERNAME);
		final String authentificationPassword = props.getProperty(WebserviceAccess.WEB_SERVICE_AUTH_PASSWORD);

		final String authString = getAuthString(authentificationType, authentificationUsername,
				authentificationPassword, authentificationPrefix, authentificationPostfix);

		if (ffURL.charAt(ffURL.length() - 1) != '/') {
			ffURL += "/";
		}
		final StringBuffer requestURL = new StringBuffer();

		requestURL.append(ffURL);
		final String service = "";// get service -> "Suggest.ff?"

		requestURL.append(service);// Change Service from request

		// TODO: append parameters
		requestURL.append("&format=json");
		requestURL.append("&" + authString);

		final HttpURLConnection conn = getConnection(requestURL.toString());

		final int respCode = conn.getResponseCode();

		if (respCode == HttpURLConnection.HTTP_OK || respCode == HttpURLConnection.HTTP_NOT_MODIFIED) {
			int b;
			final ByteArrayOutputStream bOut = new ByteArrayOutputStream();
			final byte[] data = new byte[1024];
			final InputStream servIn = conn.getInputStream();

			while ((b = servIn.read(data)) != -1) {
				bOut.write(data, 0, b);
			}
			final byte[] bArray = bOut.toByteArray();
			final String textData = new String(bArray);
			System.out.println(textData);
			return textData;
		}
		return "";
	}
}
