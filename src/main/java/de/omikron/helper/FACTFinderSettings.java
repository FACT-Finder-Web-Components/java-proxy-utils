package de.omikron.helper;

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

	public void setUrl(String url) {
		this.url = url;
	}

	public String getAccount() {
		return account;
	}

	public void setAccount(String account) {
		this.account = account;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getPrefix() {
		return prefix;
	}

	public void setPrefix(String prefix) {
		this.prefix = prefix;
	}

	public String getPostfix() {
		return postfix;
	}

	public void setPostfix(String postfix) {
		this.postfix = postfix;
	}

}
