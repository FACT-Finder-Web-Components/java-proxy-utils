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

import de.omikron.helper.api.FFHttpResponse;
import de.omikron.helper.api.FFResponseHandler;
import de.omikron.helper.api.OptionsRequest;
import de.omikron.helper.api.OptionsResponse;

public class HelperSDK {

	private static final String				ACCESS_CONTROL_ALLOW_ORIGIN			= "Access-Control-Allow-Origin";
	private static final String				ACCESS_CONTROL_ALLOW_HEADERS		= "Access-Control-Allow-Headers";
	private static final String				ACCESS_CONTROL_ALLOW_METHODS		= "Access-Control-Allow-Methods";
	private static final String				ACCESS_CONTROL_ALLOW_CREDENTIALS	= "Access-Control-Allow-Credentials";

	private static final String				ACCESS_CONTROL_REQUEST_HEADERS		= "Access-Control-Request-Headers";

	private FACTFinderSettings				settings;
	private HttpClient						client								= HttpClients.createDefault();
	private ResponseHandler<FFHttpResponse>	responsehandler						= new FFResponseHandler();

	public HelperSDK(FACTFinderSettings settings) {
		this.settings = settings;
	}

	/**
	 * Return the HttpClient used for all requests. You can configure it if you
	 * want here.
	 * 
	 * @return a HttpClient.
	 */
	public HttpClient getClient() {
		return client;
	}

	/**
	 * Sets a new HttpClient which will be used to send requests to FACTFinder.
	 * 
	 * @param client,
	 *            a HttpClient.
	 */
	public void setClient(HttpClient client) {
		this.client = client;
	}

	/**
	 * Return the FACTFinderSettings used for authentication.
	 * 
	 * @return the FACTFinderSettings.
	 */
	public FACTFinderSettings getSettings() {
		return settings;
	}

	/**
	 * Returns the ResponseHandler used for each request to FACTFinder.
	 * 
	 * @return a ResponseHandler<FFHttpResponse>
	 */
	public ResponseHandler<FFHttpResponse> getResponsehandler() {
		return responsehandler;
	}

	/**
	 * Sets a response handler for each request to FACTFinder. Default is the
	 * FFResponseHandler
	 * 
	 * @param responsehandler,
	 *            a new implementation of a ResponseHandler<FFHttpResponse>.
	 */
	public void setResponsehandler(ResponseHandler<FFHttpResponse> responsehandler) {
		this.responsehandler = responsehandler;
	}

	// ######################################
	// ####### SERVICE REQUESTS PREFLIGHT ###
	// ######################################

	/**
	 * Easy redirect of the HTTP OPTIONS request utilizing the helper methods.
	 * 
	 * @param req,
	 *            a HttpServletRequest from a Servlet.
	 * @param resp,
	 *            a HttpServletResponse from a Servlet.
	 * @throws IOException
	 */
	public void redirectOPTIONS(HttpServletRequest req, HttpServletResponse resp) throws IOException {
		writeOptionsResponse(resp, sendOptionsRequest(createOptionRequest(req)));
	}

	/**
	 * Create a OptionsRequest Object from a HttpServletRequest.
	 * 
	 * @param req,
	 *            the HttpServletRequest object form a webserver
	 * @return a OptionsRequest Object
	 */
	public OptionsRequest createOptionRequest(HttpServletRequest req) {
		return createOptionRequest(extractHeaders(req), extractService(req));
	}

	/**
	 * Create a Create a OptionsRequest Object from a Map of headers. Uses the
	 * search.ff service as default.
	 * 
	 * @param headers
	 *            , the map wit the Headers used in the Options request,
	 * @return a OptionsRequest Object
	 */
	public OptionsRequest createOptionRequest(Map<String, String> headers) {
		return createOptionRequest(headers, FFService.Search);
	}

	/**
	 * Create a OptionsResponse from for a specific Service and some Headers.
	 * 
	 * @param headers
	 *            , the map wit the Headers used in the Options request,
	 * @param service
	 *            , the FACTFinder Service to send the Request to.
	 * @return a OptionsRequest Object
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
	 * Send the OptionsRequest and returns a OptionsResponse.
	 * 
	 * @param request,
	 *            a OptionsRequest Object.
	 * @return a OptionsResponse.
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
	 * Write (the header's of) a OptionsResponse from FACTFinder to a
	 * HttpServletResponse.
	 * 
	 * @param resp,
	 *            the HttpServletResponse.
	 * @param options,
	 *            the OptionsResponse from FactFinder.
	 */
	public void writeOptionsResponse(HttpServletResponse resp, OptionsResponse options) {
		for (Entry<String, String> header : options.getHeaders().entrySet()) {
			resp.setHeader(header.getKey(), header.getValue());
		}
	}

	// ############################
	// ####### SERVICE REQUESTS ###
	// ############################

	/**
	 * Redirect a HttpServletRequest to the FACTFinder service and automatically
	 * write the response to a HttpServletResponse. This is useful if you just
	 * want to route a Request in your proxy to FACTFinder and don't bother
	 * about authentication.
	 * 
	 * @param req,
	 *            the HttpServletRequest.
	 * @param resp,
	 *            the HttpServletResponse.
	 * @throws IOException
	 */
	public void redirectGET(HttpServletRequest req, HttpServletResponse resp) throws IOException {
		copyHeaders(req, resp);
		FFHttpResponse sendRequest = sendRequest(req);
		setHeaders(resp, sendRequest.getHeaders());
		writeResponse(resp, sendRequest.getData());
	}

	/**
	 * Manually send a request to FACTFinder with URL and headers. This will
	 * send a GET request as it is! There are no authentication parameters added
	 * or header manipulation done here.
	 * 
	 * @param url,
	 *            the FACTFinder URL.
	 * @param header,
	 *            the headers to send the request with.
	 * @return a response from FACTFinder as FFHttpResponse .
	 * @throws ClientProtocolException
	 * @throws IOException
	 */
	public FFHttpResponse request(String url, Map<String, String> header) throws ClientProtocolException, IOException {
		HttpGet get = new HttpGet(url);
		for (Entry<String, String> e : header.entrySet()) {
			get.addHeader(e.getKey(), e.getValue());
		}
		return client.execute(get, responsehandler);
	}

	/**
	 * Send HttpServletRequest to FACTFinder and get the response as
	 * FFHttpResponse.
	 * 
	 * @param request,
	 *            the HttpServletRequest.
	 * @return a FFHttpResponse with status, headers and data.
	 * @throws IOException
	 */
	public FFHttpResponse sendRequest(HttpServletRequest request) throws IOException {
		return request(buildRequestURL(request), extractHeaders(request));
	}

	/**
	 * Builds a Request URL for FACTFinder with the right service, format and
	 * authentication parameters.
	 * 
	 * @param request,
	 *            the original HttpServletRequest.
	 * @return a URL as String for FACTFinder.
	 */
	public String buildRequestURL(HttpServletRequest request) {
		final StringBuffer requestURL = new StringBuffer();
		requestURL.append(fixUrl(settings.getUrl()));
		requestURL.append(extractService(request) + ".ff");

		// Add Parameters
		String queryString = request.getQueryString();
		if (queryString != null) {
			requestURL.append("?");
			requestURL.append(request.getQueryString());
			String authString = FACTFinderSecurity.getAuthString(settings, FACTFinderSecurity.ADVANCED);
			requestURL.append("&" + authString);
		}
		return requestURL.toString();
	}

	/**
	 * Write String data to a HttpServletResponse.
	 * 
	 * @param resp,
	 *            the HttpServletResponse.
	 * @param data,
	 *            the data to send.
	 * @throws IOException
	 */
	public void writeResponse(HttpServletResponse resp, String data) throws IOException {
		resp.getOutputStream().write(data.getBytes());
	}

	// #####################
	// ### Static Util's ###
	// #####################

	/**
	 * Helper method to fix a URL for the right format.
	 * 
	 * @param ffURL,
	 *            a URL to FACTFinder.
	 * @return the fixed URL
	 */
	public static String fixUrl(String ffURL) {
		if (ffURL.charAt(ffURL.length() - 1) != '/') {
			ffURL += "/";
		}
		return ffURL;
	}

	/**
	 * Extract the FACTFinder Service from a HttpServletRequest.
	 * 
	 * @param req,
	 *            the HttpServletRequest.
	 * @return the Service as FFService Enum.
	 */
	public static FFService extractService(HttpServletRequest req) {
		String path = req.getPathInfo();
		String servicePart = path.substring(path.lastIndexOf("/") + 1, path.length());
		return FFService.valueOf(servicePart.replaceAll(".ff", ""));
	}

	/**
	 * Extract the Headers from a HttpServletRequest as Map<String,String>
	 * 
	 * @param request,
	 *            the HttpServletRequest.
	 * @return a Map<String,String> of Headers.
	 */
	public static Map<String, String> extractHeaders(final HttpServletRequest request) {
		List<String> headerNames = Collections.list(request.getHeaderNames());
		Map<String, String> header = new HashMap<String, String>();
		for (String h : headerNames) {
			header.put(h, request.getHeader(h));
		}
		return header;
	}

	/**
	 * Add a Map of Header's to a HttpServletResponse
	 * 
	 * @param resp,
	 *            the HttpServletResponse.
	 * @param headers,
	 *            a Map of Header's
	 */
	public static void setHeaders(HttpServletResponse resp, Map<String, String> headers) {
		for (Entry<String, String> header : headers.entrySet()) {
			resp.setHeader(header.getKey(), header.getValue());
		}
	}

	/**
	 * Merge the headers from a HttpServletResponse with the OptionsRequest
	 * headers.
	 * 
	 * @param req,
	 *            the OptionsRequest.
	 * @param respHeaders,
	 *            the response headers from FACTFinder.
	 * @return a merged Map<String,String> of Headers.
	 */
	public static Map<String, String> mergeHeaders(OptionsRequest req, Header[] respHeaders) {
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

	/**
	 * Merges a Comma separated String with another comma separated String.
	 * 
	 * @param original,
	 *            the dominant original String.
	 * @param add,
	 *            the String to add.
	 * @return a merged Comma separated String.
	 */
	public static String merge(String original, String add) {
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

	/**
	 * Helper method to copy the headers from a HttpServletRequest to a
	 * HttpServletResponse
	 * 
	 * @param req,
	 *            the HttpServletRequest.
	 * @param resp,
	 *            the HttpServletResponse.
	 */
	public static void copyHeaders(HttpServletRequest req, HttpServletResponse resp) {
		setHeaders(resp, extractHeaders(req));
	}
}
