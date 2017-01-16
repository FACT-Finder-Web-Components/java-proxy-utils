package de.omikron.helper.client;

import java.io.IOException;
import java.io.Serializable;
import java.util.Properties;

/**
 * Service Helper functions.
 * 
 * @author marco.buczilowski
 * 
 */
public class WebserviceAccess implements Serializable {

	private static final long			serialVersionUID				= -8134350602209877407L;
	public static final String			WEB_SERVICE_ACCESS_URL			= "fact_finder_access";
	public static final String			WEB_SERVICE_HTTP_ACCESS_URL		= "fact_finder_http_access";

	public static final String			WEB_SERVICE_AUTH_TYPE			= "authentification_type";
	public static final String			WEB_SERVICE_AUTH_PREFIX			= "authentification_prefix";
	public static final String			WEB_SERVICE_AUTH_POSTFIX		= "authentification_postfix";
	public static final String			WEB_SERVICE_AUTH_USERNAME		= "authentification_username";
	public static final String			WEB_SERVICE_AUTH_PASSWORD		= "authentification_password";
	public static final String			WEB_SERVICE_PRODUCTS_PER_PAGE	= "productsPerPage";

	public static final String			WEB_SERVICE_COMP_REQUEST		= "compress_requests";
	public static final String			WEB_SERVICE_COMP_RESPONSE		= "compress_response";

	public static final String			PAGE_TITLE						= "page_title";

	protected Properties				siteProperties					= new Properties();
	protected RequestParameterHandler	requestHandler					= new RequestParameterHandler(
																				this);

	/**
	 * This Constructor try's to read and use the property-file under
	 * "de/factfinder/webclient/authentification.properties"
	 */
	public WebserviceAccess() {
		siteProperties = new Properties();
		try {
			siteProperties
					.load(getClass()
							.getClassLoader()
							.getResourceAsStream(
									"de/factfinder/webclient/authentification.properties"));
		} catch (final IOException e) {
			throw new RuntimeException(
					"No authentification.properties file found in directory \"de/factfinder/webclient\"");
		}
	}

	/**
	 * 
	 * This constructor is using the provided properties
	 * 
	 * @param properties
	 *            the provided properties
	 */
	public WebserviceAccess(final JSONAccessProperties properties) {
		if (properties != null)
			siteProperties = properties;
		else
			throw new RuntimeException(
					"WebserviceAccess: Property file is null");
	}

	/**
	 * Load service properties.
	 * 
	 * @return the configuartion properties for the connection to fact finder
	 */
	public Properties getServiceProperties() {
		return siteProperties;
	}

	/**
	 * Set service properties.
	 */
	public void setServiceProperties(final Properties properties) {
		siteProperties = properties;
	}

	/**
	 * 
	 * You can use this method to set the properties for the WebserviceAcess Use
	 * the STATIC constants to acces the properties
	 * 
	 * @param key
	 * @param value
	 */
	public void setProperty(final String key, final String value) {
		siteProperties.setProperty(key, value);
	}

	/**
	 * Should Webservice requests be compressed?
	 * 
	 * @return true if requestCompression is used, false if not
	 */
	public boolean useRequestCompression() {
		final String requestCompVal = siteProperties
				.getProperty(WEB_SERVICE_COMP_REQUEST);
		boolean compressRequests = false;
		if (requestCompVal != null) {
			compressRequests = Boolean.parseBoolean(requestCompVal);
		}
		return compressRequests;
	}

	/**
	 * Are Webservice responses compressed?
	 * 
	 * @return true if responseCompression is used, false if not
	 */
	public boolean useResponseCompression() {
		final String responseCompVal = siteProperties
				.getProperty(WEB_SERVICE_COMP_RESPONSE);
		boolean compressResponses = false;
		if (responseCompVal != null) {
			compressResponses = Boolean.parseBoolean(responseCompVal);
		}
		return compressResponses;
	}

	/**
	 * Returns the request handler.
	 * 
	 * @return the RequestParameterHandler for this WebserviceAccess object
	 */
	public RequestParameterHandler getRequestHandler() {
		return requestHandler;
	}

}
