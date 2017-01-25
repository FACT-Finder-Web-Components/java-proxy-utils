package de.omikron.helper.api;

import java.util.HashMap;
import java.util.Map;

/**
 * This is a abstraction of a HTTP Options response. Basically just the headers
 * extracted and combined with the OptionsRequest object.
 * 
 * @author arno.pitters
 *
 */
public class OptionsResponse {

	private Map<String, String>	headers	= new HashMap<String, String>();
	private OptionsRequest		request;

	/**
	 * The Headers from the FACTFinder response.
	 * 
	 * @return a Map<String,String> full of headers.
	 */
	public Map<String, String> getHeaders() {
		return headers;
	}

	/**
	 * Set tze headers.
	 * 
	 * @param headers
	 */
	public void setHeaders(Map<String, String> headers) {
		this.headers = headers;
	}

	/**
	 * Set the original OptionsRequest.
	 * 
	 * @param request,
	 *            the OptionsRequest.
	 */
	public void setRequest(OptionsRequest request) {
		this.request = request;
	}

	/**
	 * get the original OptionsRequest.
	 * 
	 * @return the original OptionsRequest.
	 */
	public OptionsRequest getRequest() {
		return request;
	}

}
