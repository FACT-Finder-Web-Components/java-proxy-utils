package de.omikron.helper.client;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Properties;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.codec.digest.DigestUtils;

public class JSONParameterParser implements Serializable {

	private static final long	serialVersionUID		= -710999851076432639L;
	// Get rid of parameters that should not be visible in the front end!
	private static String		blacklistedParameters	= "::username::password::timestamp::";
	public static final String	PARAMS_QUERY			= "query";
	public static final String	PARAMS_CHANNEL			= "channel";

	/**
	 * 
	 * @param authentificationType
	 *            Advanced or Simple
	 * @param username
	 *            username
	 * @param password
	 *            password
	 * @param prefix
	 *            normally FACT-FINDER
	 * @param postfix
	 *            normally FACT-FINDER
	 * @return the atuhentification String
	 */
	public static String getAuthenticationString(final String authenticationType, final String username,
			final String password, final String prefix, final String postfix) {
		final StringBuilder authenticationString = new StringBuilder();
		if (authenticationType != null && authenticationType.toLowerCase().equals("simple")) {
			authenticationString.append("&username=" + username);
			final String hashPassword = DigestUtils.md5Hex(password);
			authenticationString.append("&password=" + hashPassword);
		} else {
			authenticationString.append("&username=" + username);
			final long time = System.currentTimeMillis();
			final String hashPassword = DigestUtils
					.md5Hex(prefix + Long.toString(time) + DigestUtils.md5Hex(password) + postfix);
			authenticationString.append("&password=" + hashPassword);
			authenticationString.append("&timestamp=" + Long.toString(time));
		}
		return authenticationString.toString();
	}

	public static String getAuthenticationString(final Properties siteProperties) {
		/*
		 * Authentification information
		 */
		final String authentificationType = siteProperties.getProperty(WebserviceAccess.WEB_SERVICE_AUTH_TYPE);
		final String authentificationPrefix = siteProperties.getProperty(WebserviceAccess.WEB_SERVICE_AUTH_PREFIX);
		final String authentificationPostfix = siteProperties.getProperty(WebserviceAccess.WEB_SERVICE_AUTH_POSTFIX);
		final String authentificationUsername = siteProperties.getProperty(WebserviceAccess.WEB_SERVICE_AUTH_USERNAME);
		final String authentificationPassword = siteProperties.getProperty(WebserviceAccess.WEB_SERVICE_AUTH_PASSWORD);

		final String authenticationString = JSONParameterParser.getAuthenticationString(authentificationType,
				authentificationUsername, authentificationPassword, authentificationPrefix, authentificationPostfix);
		return authenticationString;
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	public static HashMap<String, String[]> retrieveParametersFromRequest(final HttpServletRequest request) {
		final HashMap<String, String[]> parameters = new HashMap<String, String[]>();
		final Map parms = request.getParameterMap();

		for (final Iterator iterator = parms.entrySet().iterator(); iterator.hasNext();) {
			final Map.Entry<String, String[]> entry = (Map.Entry<String, String[]>) iterator.next();
			final String paramName = entry.getKey();
			final String[] paramValues = entry.getValue();
			if (!blacklistedParameters.contains("::" + paramName + "::")) {
				parameters.put(paramName, paramValues);
			}
		}

		if (parameters.get("query") == null) {
			// No query found, probably the SEO path is used or we are in
			// navigation mode
			if ("true".equalsIgnoreCase(getSingleValue(parameters, "navigation"))
					|| "1".equalsIgnoreCase(getSingleValue(parameters, "navigation"))) {
				parameters.put("query", new String[] { "NAV" });
			} else if (parameters.get("seoPath") != null) {
				System.out.println(getSingleValue(parameters, "seoPath"));
				final String[] seoPath = getSingleValue(parameters, "seoPath").split("/");
				if (seoPath.length > 1) {
					parameters.put("query", new String[] { seoPath[1] });
				}
			}
		}

		return parameters;
	}

	public static String getSingleValue(final HashMap<String, String[]> parameters, final String paramName) {
		final Map<String, String[]> map = parameters;
		return getSingleValue(map, paramName);
	}

	public static String getSingleValue(final Map<String, String[]> parameters, final String paramName) {
		if (parameters.get(paramName) != null && parameters.get(paramName).length > 0) {
			return parameters.get(paramName)[0];
		} else {
			return "";
		}
	}

	public static String getParameterStringFromSearchParams(final String searchParams) {
		if (searchParams != null && searchParams.contains("?")) {
			return searchParams.substring(searchParams.indexOf("?"));
		} else {
			return "?";
		}
	}

	public static String getParameterStringFromSearchParams(final String searchParams, final String paramName) {
		String paramString = searchParams;
		if (paramString.contains("?")) {
			paramString = getParameterStringFromSearchParams(searchParams);
		}
		final String[] parameters = paramString.split("&");
		for (int i = 0; i < parameters.length; i++) {
			if (parameters[i].contains(paramName)) {
				return parameters[i].split("=")[1];
			}
		}
		return null;
	}

}
