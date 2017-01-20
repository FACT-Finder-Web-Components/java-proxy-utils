package de.omikron.helper;

import org.apache.commons.codec.digest.DigestUtils;

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

}
