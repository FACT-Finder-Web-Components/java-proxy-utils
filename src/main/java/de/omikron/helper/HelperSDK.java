package de.omikron.helper;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.http.Header;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpOptions;
import org.apache.http.impl.client.HttpClients;

import de.omikron.FFService;
import de.omikron.helper.api.FFHttpResponse;
import de.omikron.helper.api.FFResponseHandler;
import de.omikron.helper.api.OptionsRequest;
import de.omikron.helper.api.OptionsResponse;

public class HelperSDK {

	private static final String	ACCESS_CONTROL_ALLOW_ORIGIN			= "Access-Control-Allow-Origin";
	private static final String	ACCESS_CONTROL_ALLOW_HEADERS		= "Access-Control-Allow-Headers";
	private static final String	ACCESS_CONTROL_ALLOW_METHODS		= "Access-Control-Allow-Methods";
	private static final String	ACCESS_CONTROL_ALLOW_CREDENTIALS	= "Access-Control-Allow-Credentials";

	private static final String	ACCESS_CONTROL_REQUEST_HEADERS		= "Access-Control-Request-Headers";

	private FACTFinderSettings	settings;
	private HttpClient			client;

	public HelperSDK(FACTFinderSettings settings) {
		this.settings = settings;
		this.client = HttpClients.createDefault();
	}

	// ########################################
	// ####### SERVICE REQUESTS PREFLIGHT ###
	// ########################################
	// 0. manage preflight

	/**
	 * Easy redirect of the HTTP OPTIONS request utilising the helper methods.
	 * 
	 * @param req
	 * @param resp
	 * @throws IOException
	 */
	public void redirectOPTIONS(HttpServletRequest req, HttpServletResponse resp) throws IOException {
		writeOptionsResponse(resp, sendOptionsRequest(createOptionRequest(req)));
	}

	public OptionsRequest createOptionRequest(HttpServletRequest req) {
		return createOptionRequest(extractHeaders(req));
	}

	public OptionsRequest createOptionRequest(Map<String, String> headers) {
		return createOptionRequest(headers, FFService.Search);
	}

	/**
	 * Create a OptionsResponse from the Service and some Headers.
	 * 
	 * @param headers
	 * @param service
	 * @return
	 */
	public OptionsRequest createOptionRequest(Map<String, String> headers, FFService service) {
		OptionsRequest optionsRequest = new OptionsRequest();
		optionsRequest.setHeaders(headers);
		optionsRequest.setService(service);

		HttpOptions httpOptions = new HttpOptions(settings.getUrl() + "/" + service.name() + ".ff");
		for (Entry<String, String> header : optionsRequest.getHeaders().entrySet()) {
			httpOptions.setHeader(header.getKey(), header.getValue());
		}
		optionsRequest.setHttpRequest(httpOptions);
		return optionsRequest;
	}

	/**
	 * Actually send the options request.
	 * 
	 * @param request
	 * @return
	 * @throws ClientProtocolException
	 * @throws IOException
	 */
	public OptionsResponse sendOptionsRequest(final OptionsRequest request)
			throws ClientProtocolException, IOException {
		return client.execute(request.getHttpOptions(), new ResponseHandler<OptionsResponse>() {

			public OptionsResponse handleResponse(HttpResponse response) throws ClientProtocolException, IOException {
				OptionsResponse resp = new OptionsResponse();
				resp.setRequest(request);
				resp.setHeaders(mergeHeaders(request, response.getAllHeaders()));
				return resp;
			}
		});
	}

	/**
	 * Merge the headers from the request back into the result.
	 * 
	 * @param req
	 * @param resp
	 * @return
	 */
	private Map<String, String> mergeHeaders(OptionsRequest req, Header[] respHeaders) {
		Map<String, String> responseMap = new HashMap<String, String>();
		for (Header header : respHeaders) {
			responseMap.put(header.getName(), header.getValue());
		}

		Map<String, String> headers = new HashMap<String, String>();

		// direct copy from FF server response to proxy response
		headers.put("Server", responseMap.get("Server"));
		headers.put("Date", responseMap.get("Date"));
		headers.put("Content-Length", responseMap.get("Content-Length"));
		headers.put(ACCESS_CONTROL_ALLOW_ORIGIN, responseMap.get(ACCESS_CONTROL_ALLOW_ORIGIN));
		headers.put(ACCESS_CONTROL_ALLOW_METHODS, responseMap.get(ACCESS_CONTROL_ALLOW_METHODS));
		headers.put(ACCESS_CONTROL_ALLOW_CREDENTIALS, responseMap.get(ACCESS_CONTROL_ALLOW_CREDENTIALS));

		// merge
		String reqAllowHeaders = req.getHeaders().get(ACCESS_CONTROL_REQUEST_HEADERS);
		String ffAllowHeaders = responseMap.get(ACCESS_CONTROL_ALLOW_HEADERS);
		String merge = merge(ffAllowHeaders, reqAllowHeaders);
		headers.put(ACCESS_CONTROL_ALLOW_HEADERS, merge);

		return headers;
	}

	public void writeOptionsResponse(HttpServletResponse resp, OptionsResponse options) {
		for (Entry<String, String> header : options.getHeaders().entrySet()) {
			resp.setHeader(header.getKey(), header.getValue());
		}
	}

	// ############################
	// ####### SERVICE REQUESTS ###
	// ############################

	// set the payload of the old request as the new request
	public void redirectGET(HttpServletRequest req, HttpServletResponse resp) throws IOException {
		copyHeaders(req, resp);
		FFHttpResponse sendRequest = sendRequest(req);
		setHeaders(resp, sendRequest.getHeaders());
		writeResponse(resp, sendRequest.getData());
	}

	public void copyHeaders(HttpServletRequest req, HttpServletResponse resp) {
		setHeaders(resp, extractHeaders(req));
	}

	public FFHttpResponse request(String url, Map<String, String> header) throws ClientProtocolException, IOException {
		HttpGet get = new HttpGet(url);
		for (Entry<String, String> e : header.entrySet()) {
			get.addHeader(e.getKey(), e.getValue());
		}
		return client.execute(get, new FFResponseHandler());
	}

	public FFHttpResponse sendRequest(HttpServletRequest request) throws IOException {
		return request(buildRequestURL(request), extractHeaders(request));
	}

	public String buildRequestURL(HttpServletRequest request) {
		final StringBuffer requestURL = new StringBuffer();
		requestURL.append(fixUrl(settings.getUrl()));
		requestURL.append(extractService(request) + ".ff");

		// Add Parameters
		String queryString = request.getQueryString();
		if (queryString != null) {
			requestURL.append("?");
			requestURL.append(request.getQueryString());
			String authString = FACTFinderSecurity.getAuthString(settings, FACTFinderSecurity.AUTH_ADVANCED);
			requestURL.append("&" + authString);
		}
		return requestURL.toString();
	}

	public void writeResponse(HttpServletResponse resp, String content) throws IOException {
		resp.getOutputStream().write(content.getBytes());
	}

	// ################
	// ### Util's ###
	// ################

	public static String fixUrl(String ffURL) {
		if (ffURL.charAt(ffURL.length() - 1) != '/') {
			ffURL += "/";
		}
		return ffURL;
	}

	public static FFService extractService(HttpServletRequest req) {
		String path = req.getPathInfo();
		String servicePart = path.substring(path.lastIndexOf("/") + 1, path.length());
		return FFService.valueOf(servicePart.replaceAll(".ff", ""));
	}

	public static Map<String, String> extractHeaders(final HttpServletRequest request) {
		List<String> headerNames = Collections.list(request.getHeaderNames());
		Map<String, String> header = new HashMap<String, String>();
		for (String h : headerNames) {
			header.put(h, request.getHeader(h));
		}
		return header;
	}

	public static void setHeaders(HttpServletResponse resp, Map<String, String> headers) {
		for (Entry<String, String> header : headers.entrySet()) {
			resp.setHeader(header.getKey(), header.getValue());
		}
	}

	private static String merge(String original, String add) {
		List<String> list = new ArrayList<String>(Arrays.asList(original.split(",")));
		String[] split = add.split(",");
		for (String string : split) {
			if (!list.contains(string)) {
				list.add(string);
			}
		}

		StringBuilder sb = new StringBuilder();
		boolean first = true;
		for (String s : list) {
			if (!first) {
				sb.append(",");
			}
			first = false;
			sb.append(s);
		}
		return sb.toString();
	}

}
