package de.factfinder.utils;

/**
 * The FACTFinderSettings are your base for configuration.
 * 
 * @author arno.pitters
 *
 */
public class FACTFinderSettings {

	public static final String	DEFAUL_PREFIX	= "FACT-FINDER";
	public static final String	DEFAUL_POSTFIX	= "FACT-FINDER";

	private String				url;

	private String				account;
	private String				password;

	private String				prefix			= DEFAUL_PREFIX;
	private String				postfix			= DEFAUL_POSTFIX;

	public FACTFinderSettings() {
	}

	public String getUrl() {
		return url;
	}

	/**
	 * The FACTFinder location as a URL String.
	 * 
	 * ex: http://mydomain.de/FACT-Finder-7.2
	 * 
	 * @param url
	 *            the FactFinder location.
	 * 
	 */
	public void setUrl(String url) {
		this.url = url;
	}

	/**
	 * The Account which is used for authentication.
	 * 
	 * @return the account name.
	 */
	public String getAccount() {
		return account;
	}

	/**
	 * The Account which is used for authentication.
	 * 
	 * @param account,
	 *            the account name.
	 */
	public void setAccount(String account) {
		this.account = account;
	}

	/**
	 * The Password which is used for authentication.
	 * 
	 * @return
	 */
	public String getPassword() {
		return password;
	}

	/**
	 * The Password which is used for authentication.
	 * 
	 * @param password
	 */
	public void setPassword(String password) {
		this.password = password;
	}

	/**
	 * The Prefix which is used to build your authentication string.
	 * 
	 * @return
	 */
	public String getPrefix() {
		return prefix;
	}

	/**
	 * The Prefix which is used to build your authentication string.
	 * 
	 * @param prefix
	 */
	public void setPrefix(String prefix) {
		this.prefix = prefix;
	}

	/**
	 * The Postfix which is used to build your authentication string.
	 * 
	 * @return
	 */
	public String getPostfix() {
		return postfix;
	}

	/**
	 * The Postfix which is used to build your authentication string.
	 * 
	 * @param postfix
	 */
	public void setPostfix(String postfix) {
		this.postfix = postfix;
	}

}
