package de.omikron.helper.api;

import java.util.HashMap;
import java.util.Map;

/**
 * This is a abstraction of a HTTP Options result. Basicaly just the headers.
 * 
 * @author arno.pitters
 *
 */
public class OptionsResponse {

	private Map<String, String>	headers	= new HashMap<String, String>();
	private OptionsRequest		request;

	public Map<String, String> getHeaders() {
		return headers;
	}

	public void setHeaders(Map<String, String> headers) {
		this.headers = headers;
	}

	public void setRequest(OptionsRequest request) {
		this.request = request;
	}

	public OptionsRequest getRequest() {
		return request;
	}

}
