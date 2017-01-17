package de.omikron.helper;

import java.io.IOException;

public class backup {

	
	/**
	 * 
	 * HTTPRequestProcessor
	 * 
	 * Retrieves Suggest entries.
	 * 
	 * @param wsAccess
	 * @param request
	 * @return JSONSuggestResults
	 * @throws IOException
	 */
//	public static String retrieveJSONResult(final WebserviceAccess wsAccess, final HttpServletRequest request)
//			throws IOException {
//		/*
//		 * get http url
//		 */
//		String ffURL = wsAccess.getServiceProperties().getProperty(WebserviceAccess.WEB_SERVICE_HTTP_ACCESS_URL);
//		/*
//		 * Authentification information
//		 */
//		final String authString = getAuthentificationString(wsAccess);
//
//		if (ffURL.charAt(ffURL.length() - 1) != '/') {
//			ffURL += "/";
//		}
//		final StringBuffer requestURL = new StringBuffer();
//
//		requestURL.append(ffURL);
//		final String service = "";// get service -> "Suggest.ff?"
//
//		requestURL.append(service);// Change Service from request
//
//		// TODO: append parameters
//		requestURL.append("&format=json");
//		requestURL.append("&" + authString);
//
//		final HttpURLConnection conn = getConnection(requestURL.toString());
//
//		final int respCode = conn.getResponseCode();
//
//		if (respCode == HttpURLConnection.HTTP_OK || respCode == HttpURLConnection.HTTP_NOT_MODIFIED) {
//			int b;
//			final ByteArrayOutputStream bOut = new ByteArrayOutputStream();
//			final byte[] data = new byte[1024];
//			final InputStream servIn = conn.getInputStream();
//
//			while ((b = servIn.read(data)) != -1) {
//				bOut.write(data, 0, b);
//			}
//			final byte[] bArray = bOut.toByteArray();
//			final String textData = new String(bArray);
//			System.out.println(textData);
//			return textData;
//		}
//		return "";
//	}

	
}
