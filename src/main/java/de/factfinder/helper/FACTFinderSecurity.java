package de.factfinder.helper;

import org.apache.commons.codec.digest.DigestUtils;

/**
 * A helper class to build FACTFinder authentication hashes.
 * 
 * @author arno.pitters
 *
 */
public class FACTFinderSecurity {

	public static final int	SIMPLE		= 0;
	public static final int	ADVANCED	= 1;

	/**
	 * Build a authentication String from the FACTFinderSettings. You can define
	 * the type for which authentication method to use. Options are
	 * FACTFinderSecurity.SIMPLE and FACTFinderSecurity.ADVANCED
	 * 
	 * @param settings
	 * @param type
	 * @return
	 */
	public static String getAuthString(FACTFinderSettings settings, int type) {
		return getAuthString(type, settings.getAccount(), settings.getPassword(), settings.getPrefix(),
				settings.getPostfix());
	}

	/**
	 * manually build a authentication String with the following parameters:
	 * 
	 * @param authentificationType,
	 *            can be FACTFinderSecurity.SIMPLE and
	 *            FACTFinderSecurity.ADVANCED
	 * @param username,
	 *            the username for the FACTFinder account to use.
	 * @param password,
	 *            the password for the the account.
	 * @param prefix,
	 *            a prefix to build the hash.
	 * @param postfix,
	 *            a postfix to build the hash.
	 * @return a ready to use authentication String as parameters pairs with the
	 *         structure: "username=user&password=hash[&timestamp&1234567890]".
	 */
	public static String getAuthString(int authentificationType, final String username, final String password,
			final String prefix, final String postfix) {
		final StringBuilder authentificationInfo = new StringBuilder();

		if (authentificationType == 0) {// simple
			authentificationInfo.append("username=" + username);
			authentificationInfo.append("&password=" + DigestUtils.md5Hex(password));
		} else if (authentificationType == 1) {// advanced
			authentificationInfo.append("username=" + username);
			final long time = System.currentTimeMillis();

			final String hashPassword = DigestUtils
					.md5Hex(prefix + Long.toString(time) + DigestUtils.md5Hex(password) + postfix);
			authentificationInfo.append("&password=" + hashPassword);
			authentificationInfo.append("&timestamp=" + time);
		} else {
			throw new RuntimeException(
					"Wrong Authentication type. Parameter authentificationType either msut be 0 (FACTFinderSecurity.SIMPLE) or 1 (FACTFinderSecurity.ADVANCED) but was: "
							+ authentificationType);
		}
		return authentificationInfo.toString();
	}

}
