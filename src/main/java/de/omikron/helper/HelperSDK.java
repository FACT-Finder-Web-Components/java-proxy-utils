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

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import de.omikron.helper.api.FFHttpResponse;
import de.omikron.helper.api.FFResponseHandler;
import de.omikron.helper.api.OptionsRequest;
import de.omikron.helper.api.OptionsResponse;
import de.omikron.helper.reponse.FFResponse;
import de.omikron.helper.reponse.ProductCampaignResponse;
import de.omikron.helper.reponse.RecommendationResponse;
import de.omikron.helper.reponse.SearchResponse;
import de.omikron.helper.reponse.SimilarRecordsResponse;
import de.omikron.helper.reponse.SuggestResponse;

public class HelperSDK {

	private static final String	ACCESS_CONTROL_ALLOW_ORIGIN			= "Access-Control-Allow-Origin";
	private static final String	ACCESS_CONTROL_ALLOW_HEADERS		= "Access-Control-Allow-Headers";
	private static final String	ACCESS_CONTROL_ALLOW_METHODS		= "Access-Control-Allow-Methods";
	private static final String	ACCESS_CONTROL_ALLOW_CREDENTIALS	= "Access-Control-Allow-Credentials";

	private static final String	ACCESS_CONTROL_REQUEST_HEADERS		= "Access-Control-Request-Headers";

	private FFSettings			settings;
	private Gson				gson;
	private HttpClient			client;

	public HelperSDK(FFSettings settings) {
		this.settings = settings;
		this.gson = new GsonBuilder().serializeNulls().create();
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
		// Build Request
		HttpGet get = new HttpGet(url);

		// Add Headers
		for (Entry<String, String> e : header.entrySet()) {
			get.addHeader(e.getKey(), e.getValue());
		}
		System.out.println("Request: " + get);
		return client.execute(get, new FFResponseHandler());
	}

	public FFHttpResponse sendRequest(HttpServletRequest request) throws IOException {
		String requestURL = buildRequestURL(request);
		Map<String, String> header = extractHeaders(request);
		return request(requestURL, header);
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
			String authString = FFSecurity.getAuthString(settings, FFSecurity.AUTH_ADVANCED);
			requestURL.append("&" + authString);
		}
		return requestURL.toString();
	}

	public void writeResponse(HttpServletResponse resp, String content) throws IOException {
		resp.getOutputStream().write(content.getBytes());
	}

	// ########################
	// ### PARSE (via Gson) ###
	// ########################

	@SuppressWarnings({ "unchecked", "rawtypes" })
	public FFResponse parse(String json, FFService service) throws Exception {
		Class serviceResponseClass = null;
		switch (service) {
		case Search:
			serviceResponseClass = SearchResponse.class;
			break;
		case Suggest:
			serviceResponseClass = SuggestResponse.class;
			break;
		case Recommender:
			serviceResponseClass = RecommendationResponse.class;
			break;
		case SimilarRecords:
			serviceResponseClass = SimilarRecordsResponse.class;
		case ProductCampaign:
			serviceResponseClass = ProductCampaignResponse.class;
		default:
			throw new Exception("No service for: " + service);
		}
		return gson.fromJson(json, serviceResponseClass);
	}

	public String asJson(FFResponse result) {
		return gson.toJson(result);
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

}
