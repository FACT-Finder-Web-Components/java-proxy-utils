package de.omikron.helper;

import java.io.IOException;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpOptions;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import de.omikron.helper.api.OptionsRequest;
import de.omikron.helper.api.OptionsResponse;
import de.omikron.helper.dom.Campaign;
import de.omikron.helper.reponse.FFResponse;
import de.omikron.helper.reponse.RecommendationResponse;
import de.omikron.helper.reponse.SearchResponse;
import de.omikron.helper.reponse.SuggestResponse;

public class HelperSDK {

	private static final String	ACCESS_CONTROL_ALLOW_ORIGIN		= "Access-Control-Allow-Origin";
	private static final String	ACCESS_CONTROL_ALLOW_HEADERS	= "Access-Control-Allow-Headers";

	private static final String	ACCESS_CONTROL_REQUEST_HEADERS	= "Access-Control-Request-Headers";

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
		System.out.println(httpOptions);
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
				System.out.println("OPTIONS Response: " + response);
				OptionsResponse resp = new OptionsResponse();
				resp.setRequest(request);
				resp.setHeaders(mergeHeaders(request, response.getAllHeaders()));
				return resp;
			}
		});
	}

	/**
	 * Merge the headers from teh request back into the result.
	 * 
	 * @param req
	 * @param resp
	 * @return
	 */
	private Map<String, String> mergeHeaders(OptionsRequest req, Header[] respHeaders) {
		Map<String, String> reqHeaders = req.getHeaders();
		System.out.println("RequestHeader");
		for (Entry<String, String> header : reqHeaders.entrySet()) {
			System.out.println("\t" + header);
		}

		System.out.println("ResponseHeader");
		Map<String, String> responseMap = new HashMap<String, String>();
		for (Header header : respHeaders) {
			System.out.println("\t" + header);
			responseMap.put(header.getName(), header.getValue());
		}

		Map<String, String> headers = new HashMap<String, String>();
		headers.put(ACCESS_CONTROL_ALLOW_ORIGIN, responseMap.get(ACCESS_CONTROL_ALLOW_ORIGIN));
		headers.put(ACCESS_CONTROL_ALLOW_HEADERS, responseMap.get(ACCESS_CONTROL_ALLOW_HEADERS));
		// headers.putAll(reqHeaders);
		for (Entry<String, String> header : reqHeaders.entrySet()) {
			String key = header.getKey();
			if (key.startsWith("Access-Control-Request")) {
				// change to "Access-Control-Allow"
				String replace = key.replace("Access-Control-Request", "Access-Control-Allow");
				headers.put(replace, responseMap.get(key));
			}
		}
		// String value = headers.remove(ACCESS_CONTROL_REQUEST_HEADERS);
		// headers.put(ACCESS_CONTROL_ALLOW_HEADERS, value);
		// swapValue(headers, ACCESS_CONTROL_REQUEST_HEADERS,
		// ACCESS_CONTROL_ALLOW_HEADERS);

		System.out.println("After merge");
		for (Entry<String, String> header : headers.entrySet()) {
			System.out.println("\t" + header);
		}
		return headers;
	}

	public String merge(String a, String b) {
		Set<String> set = Collections.EMPTY_SET;
		set.addAll(Arrays.asList(a.split(",")));
		set.addAll(Arrays.asList(b.split(",")));
		StringBuilder sb = new StringBuilder();
		boolean first = true;
		for (String s : set) {
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
	// 1. FACT-Finder Authentifizierung
	// 2. absetzen von http request
	// 3. Bearbeiten der JSON-Response

	// set the payload of the old request as the new request
	public void redirectGET(HttpServletRequest req, HttpServletResponse resp) throws IOException {
		copyHeaders(req, resp);
		writeResponse(resp, sendRequest(req));
	}

	public void copyHeaders(HttpServletRequest req, HttpServletResponse resp) {
		setHeaders(resp, extractHeaders(req));
	}

	public String sendRequest(HttpServletRequest request) throws IOException {
		// Build request String
		final StringBuffer requestURL = new StringBuffer();
		requestURL.append(fixUrl(settings.getUrl()));
		requestURL.append(extractService(request));

		// Add Parameters
		String queryString = request.getQueryString();
		if (queryString != null) {
			requestURL.append("?");
			requestURL.append(request.getQueryString());
			String authString = FFSecurity.getAuthString(settings, FFSecurity.AUTH_ADVANCED);
			requestURL.append("&" + authString);
		}

		// Build Request
		HttpGet get = new HttpGet(requestURL.toString());

		// Add Headers
		Map<String, String> header = extractHeaders(request);
		for (Entry<String, String> e : header.entrySet()) {
			get.addHeader(e.getKey(), e.getValue());
		}

		System.out.println("Request: " + get);
		return client.execute(get, new ResponseHandler<String>() {

			public String handleResponse(final HttpResponse response) throws ClientProtocolException, IOException {
				int status = response.getStatusLine().getStatusCode();
				System.out.println("Response: " + status);
				if (status >= 200 && status < 300) {
					HttpEntity entity = response.getEntity();
					return entity != null ? EntityUtils.toString(entity) : null;
				} else {
					throw new ClientProtocolException("Unexpected response status: " + status);
				}
			}

		});
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
		case Recommendation:
			serviceResponseClass = RecommendationResponse.class;
			break;
		case Search:
			serviceResponseClass = SearchResponse.class;
			break;
		case Suggest:
			serviceResponseClass = SuggestResponse.class;
			break;
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
		String extractService = path.substring(path.lastIndexOf("/") + 1, path.length());
		return FFService.valueOf(extractService.replaceAll(".ff", "").toUpperCase());
	}

	public static Map<String, String> extractHeaders(final HttpServletRequest request) {
		Map<String, String> header = new HashMap<String, String>();
		List<String> list = Collections.list(request.getHeaderNames());
		for (String h : list) {
			header.put(h, request.getHeader(h));
			// header.put(h, request.getHeaders(h));
		}
		return header;
	}

	public static void setHeaders(HttpServletResponse resp, Map<String, String> headers) {
		for (Entry<String, String> header : headers.entrySet()) {
			resp.setHeader(header.getKey(), header.getValue());
		}
	}

}
