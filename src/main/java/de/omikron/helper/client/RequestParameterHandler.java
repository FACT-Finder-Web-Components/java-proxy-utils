package de.omikron.helper.client;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

/**
 * Helper class for analyzing factfinder specific request parameter.
 * 
 * @author marco.buczilowski
 * @author tobias.armbruster
 * 
 */
public class RequestParameterHandler implements Serializable {

	private static final long	serialVersionUID	= 6527616157957545773L;
	protected WebserviceAccess	webserviceAccess;

	/**
	 * Constructor
	 * 
	 */
	public RequestParameterHandler(final WebserviceAccess webserviceAccess) {
		this.webserviceAccess = webserviceAccess;
	}

	/**
	 * Collects all filter parameter.
	 * 
	 * @param request
	 * @return FilterParameters
	 */
	public List<String> getFilterParameter(final HttpServletRequest request) {
		final List<String> resultList = new ArrayList<String>();

		@SuppressWarnings("unchecked")
		final Enumeration<String> paramNames = request.getParameterNames();

		while (paramNames.hasMoreElements()) {
			final String paramName = paramNames.nextElement();

			if (paramName.startsWith("filter")) {
				/*
				 * Filterparam
				 */
				final String filterFieldName = paramName.substring("filter"
						.length());

				resultList.add(filterFieldName);
			}
		}
		return resultList;
	}

	/**
	 * Collects sort parameter.
	 * 
	 * @param request
	 * @return SortParameters
	 */
	public List<String> getSortParameter(final HttpServletRequest request) {
		final List<String> resultList = new ArrayList<String>();

		@SuppressWarnings("unchecked")
		final Enumeration<String> paramNames = request.getParameterNames();

		while (paramNames.hasMoreElements()) {
			final String paramName = paramNames.nextElement();

			if (paramName.startsWith("sort")) {
				/*
				 * Filterparam
				 */
				final String filterFieldName = paramName.substring("sort"
						.length());

				resultList.add(filterFieldName);
			}
		}
		return resultList;
	}

	/**
	 * Returns the value for request parameter "id".
	 * 
	 * The parameter will be checked for Javascript-Injection. For more details
	 * refer to de.factfinder.json.security.JavascriptParameterValidator.
	 * 
	 * @param request
	 * @return the id or NULL if nothing could be found
	 */
	public String getID(final HttpServletRequest request) {
		final String parameter = request.getParameter("id");
		if (parameter != null) {
			return JavascriptParameterValidator
					.cleanStringFromJavascript(parameter);
		}
		return parameter;
	}

	/**
	 * 
	 * Returns the value for request parameter "maxResults".
	 * 
	 * @param request
	 * @return NULL if the request parameter value isnt an valid Integer value
	 *         or the integer value of the maxResults paramter
	 */
	public Integer getMaxResults(final HttpServletRequest request) {
		final String parameter = request.getParameter("maxResults");
		try {
			return Integer.valueOf(parameter);
		} catch (final Exception e) {
			return null;
		}
	}

	/**
	 * 
	 * @param request
	 * @return NULL if the request parameter value isnt an valid boolean value
	 *         or the boolean value of the idsOnly paramter
	 */
	public Boolean getIdsOnly(final HttpServletRequest request) {
		final String parameter = request.getParameter("idsOnly");
		try {
			return Boolean.valueOf(parameter);
		} catch (final Exception e) {
			return null;
		}
	}

	/**
	 * Returns the search query or a default query.
	 * 
	 * The parameter will be checked for Javascript-Injection. For more details
	 * refer to de.factfinder.json.security.JavascriptParameterValidator.
	 * 
	 * @param request
	 * @return the query
	 */
	public String getQuery(final HttpServletRequest request) {
		final String query = request.getParameter("query");
		try {
			if (query.length() != 0)
				return JavascriptParameterValidator
						.cleanStringFromJavascript(query);
			else
				return "*";
		} catch (final Exception e) {
			return "*";
		}
	}

	/**
	 * Returns the search query or a default query.
	 * 
	 * The parameter will be checked for Javascript-Injection. For more details
	 * refer to de.factfinder.json.security.JavascriptParameterValidator.
	 * 
	 * @param query
	 * @return the query
	 */
	public String getQuery(final String query) {
		try {
			if (query.length() != 0)
				return JavascriptParameterValidator
						.cleanStringFromJavascript(query);
			else
				return "*";
		} catch (final Exception e) {
			return "*";
		}
	}

	/**
	 * Retrieves current page from request.
	 * 
	 * @param request
	 * @return the current page
	 */
	public Integer getPage(final HttpServletRequest request) {
		try {
			return Integer.parseInt(request.getParameter("page"));
		} catch (final Exception e) {
			return new Integer(1);
		}
	}

	/**
	 * SEO path. The parameter will be checked for Javascript-Injection. For
	 * more details refer to
	 * de.factfinder.json.security.JavascriptParameterValidator.
	 * 
	 * @param request
	 * @return the seopath
	 */
	public String getSeoPath(final HttpServletRequest request) {
		final String seo = request.getParameter("seoPath");

		if (seo == null || seo.length() == 0) {
			return "";
		}
		return JavascriptParameterValidator.cleanStringFromJavascript(seo);
	}

	/**
	 * Returns the search channel.
	 * 
	 * The parameter will be checked for Javascript-Injection. For more details
	 * refer to de.factfinder.json.security.JavascriptParameterValidator.
	 * 
	 * @param request
	 * @return the channel
	 */
	public String getChannel(final HttpServletRequest request) {
		final String channel = request.getParameter("channel");
		if (channel == null || channel.length() == 0) {
			return webserviceAccess.getServiceProperties().getProperty(
					"channel");
		}
		return JavascriptParameterValidator.cleanStringFromJavascript(channel);
	}

	/**
	 * Returns the search channel.
	 * 
	 * The parameter will be checked for Javascript-Injection. For more details
	 * refer to de.factfinder.json.security.JavascriptParameterValidator.
	 * 
	 * @param channel
	 * @return the channel
	 */
	public String getChannel(String channel) {
		if (channel == null || channel.length() == 0) {
			channel = webserviceAccess.getServiceProperties().getProperty(
					"channel");
			return channel;
		}
		return JavascriptParameterValidator.cleanStringFromJavascript(channel);
	}

	/**
	 * Returns products per page attribute.
	 * 
	 * @param request
	 * @return product per page value
	 */
	public String getProductsPerPage(final HttpServletRequest request) {
		String ppp = request.getParameter("productsPerPage");

		if (ppp == null) {
			ppp = webserviceAccess.getServiceProperties().getProperty(
					"productsPerPage");
		}
		return ppp;
	}
}
