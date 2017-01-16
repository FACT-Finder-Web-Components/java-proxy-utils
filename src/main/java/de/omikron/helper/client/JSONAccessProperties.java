package de.omikron.helper.client;

import java.util.Properties;

/**
 * 
 * @author tobias.armbruster
 * 
 */
public class JSONAccessProperties extends Properties {

	private static final long	serialVersionUID	= -2676706702782009973L;

	public static final String	HTTP_ACCESS_URL		= "fact_finder_http_access";
	public static final String	CHANNEL				= "channel";

	public static final String	AUTH_TYPE			= "authentification_type";
	public static final String	AUTH_PREFIX			= "authentification_prefix";
	public static final String	AUTH_POSTFIX		= "authentification_postfix";
	public static final String	AUTH_USERNAME		= "authentification_username";
	public static final String	AUTH_PASSWORD		= "authentification_password";

	public static final String	PRODUCTS_PER_PAGE	= "productsPerPage";

	// Not used so far
	// public static final String COMP_REQUEST = "compress_requests";
	// public static final String COMP_RESPONSE = "compress_response";

	/**
	 * 
	 * @param properties
	 *            the properties which have to contain the needed values. For
	 *            more information refer to the other constructors of this class
	 */
	public JSONAccessProperties(final Properties properties) {
		if (properties.get(HTTP_ACCESS_URL) == null)
			throw new RuntimeException("ERROR:::: Property " + HTTP_ACCESS_URL + " is not set.");
		if (properties.get(AUTH_TYPE) == null)
			throw new RuntimeException("ERROR:::: Property " + AUTH_TYPE + " is not set.");
		if (properties.get(AUTH_PREFIX) == null)
			throw new RuntimeException("ERROR:::: Property " + AUTH_POSTFIX + " is not set.");
		if (properties.get(AUTH_POSTFIX) == null)
			throw new RuntimeException("ERROR:::: Property " + AUTH_POSTFIX + " is not set.");
		if (properties.get(AUTH_USERNAME) == null)
			throw new RuntimeException("ERROR:::: Property " + HTTP_ACCESS_URL + " is not set.");
		if (properties.get(AUTH_PASSWORD) == null)
			throw new RuntimeException("ERROR:::: Property " + HTTP_ACCESS_URL + " is not set.");
		if (properties.get(CHANNEL) == null)
			throw new RuntimeException("ERROR:::: Property " + CHANNEL + " is not set.");

		setProperty(HTTP_ACCESS_URL, (String) properties.get(HTTP_ACCESS_URL));
		setProperty(AUTH_TYPE, (String) properties.get(AUTH_TYPE));
		setProperty(AUTH_PREFIX, (String) properties.get(AUTH_PREFIX));
		setProperty(AUTH_POSTFIX, (String) properties.get(AUTH_POSTFIX));
		setProperty(AUTH_USERNAME, (String) properties.get(AUTH_USERNAME));
		setProperty(AUTH_PASSWORD, (String) properties.get(AUTH_PASSWORD));
		setProperty(CHANNEL, properties.getProperty(CHANNEL));
	}

	/**
	 * 
	 * @param http_access_url
	 *            the url of your factfinder
	 * @param authentification_type
	 *            can be simple or advanced(default in factfinder settings)
	 * @param authentification_prefix
	 *            the prefix to encrypt the password for authentification_type
	 *            advanced
	 * @param authentification_postfix
	 *            the postfix to encrypt the password for authentification_type
	 *            advanced
	 * @param authentification_username
	 *            the username of the factfinder user
	 * @param authentification_password
	 *            the password
	 * @param channel
	 *            the default which is used if no channel is specified in the
	 *            url
	 */
	public JSONAccessProperties(final String http_access_url, final String authentification_type,
			final String authentification_prefix, final String authentification_postfix, final String authentification_username,
			final String authentification_password, final String channel) {

		if (http_access_url == null)
			throw new RuntimeException("ERROR:::: Property " + HTTP_ACCESS_URL + " is not set.");
		if (authentification_type == null)
			throw new RuntimeException("ERROR:::: Property " + AUTH_TYPE + " is not set.");
		if (authentification_prefix == null)
			throw new RuntimeException("ERROR:::: Property " + AUTH_POSTFIX + " is not set.");
		if (authentification_postfix == null)
			throw new RuntimeException("ERROR:::: Property " + AUTH_POSTFIX + " is not set.");
		if (authentification_username == null)
			throw new RuntimeException("ERROR:::: Property " + HTTP_ACCESS_URL + " is not set.");
		if (authentification_password == null)
			throw new RuntimeException("ERROR:::: Property " + HTTP_ACCESS_URL + " is not set.");
		if (channel == null)
			throw new RuntimeException("ERROR:::: Property " + CHANNEL + " is not set.");

		setProperty(HTTP_ACCESS_URL, http_access_url);
		setProperty(AUTH_TYPE, authentification_type);
		setProperty(AUTH_PREFIX, authentification_prefix);
		setProperty(AUTH_POSTFIX, authentification_postfix);
		setProperty(AUTH_USERNAME, authentification_username);
		setProperty(AUTH_PASSWORD, authentification_password);
		setProperty(CHANNEL, channel);
	}
}
