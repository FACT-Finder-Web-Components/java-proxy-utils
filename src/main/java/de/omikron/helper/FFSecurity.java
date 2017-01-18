package de.omikron.helper;

import java.util.Properties;

import org.apache.commons.codec.digest.DigestUtils;

import de.omikron.helper.client.WebserviceAccess;
import de.omikron.helper.settings.FFSettings;

public class FFSecurity {

	public static final String	AUTH_SIMPLE		= "simple";
	public static final String	AUTH_ADVANCED	= "advanced";

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
	public static String getAuthString(FFSettings settings, String type) {
		return getAuthString(type, settings.getAccount(), settings.getPassword(), settings.getPrefix(),
				settings.getPostfix());
	}

	public static String getAuthString(final String authentificationType, final String username, final String password,
			final String prefix, final String postfix) {
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

	private static String getAuthentificationString(final WebserviceAccess wsAccess) {
		Properties props = wsAccess.getServiceProperties();
		final String authentificationType = props.getProperty(WebserviceAccess.WEB_SERVICE_AUTH_TYPE);
		final String authentificationPrefix = props.getProperty(WebserviceAccess.WEB_SERVICE_AUTH_PREFIX);
		final String authentificationPostfix = props.getProperty(WebserviceAccess.WEB_SERVICE_AUTH_POSTFIX);
		final String authentificationUsername = props.getProperty(WebserviceAccess.WEB_SERVICE_AUTH_USERNAME);
		final String authentificationPassword = props.getProperty(WebserviceAccess.WEB_SERVICE_AUTH_PASSWORD);

		final String authString = getAuthString(authentificationType, authentificationUsername,
				authentificationPassword, authentificationPrefix, authentificationPostfix);
		return authString;
	}

}
