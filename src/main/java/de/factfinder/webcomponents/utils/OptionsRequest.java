package de.factfinder.webcomponents.utils;

import java.util.HashMap;
import java.util.Map;

import org.apache.http.client.methods.HttpOptions;

/**
 * This is a abstraction of the HTTP OPTIONS Request. Used internally for further
 * mapping and merging of header.
 * 
 * @author arno.pitters
 *
 */
public class OptionsRequest {

	private FACTFinderService	service;
	private Map<String, String>	headers	= new HashMap<String, String>();
	private HttpOptions			httpOptions;

	public Map<String, String> getHeaders() {
		return headers;
	}

	public void setHeaders(Map<String, String> headers) {
		this.headers = headers;
	}

	public void setHttpRequest(HttpOptions httpOptions) {
		this.httpOptions = httpOptions;
	}

	public HttpOptions getHttpOptions() {
		return httpOptions;
	}

	public void setHttpOptions(HttpOptions httpOptions) {
		this.httpOptions = httpOptions;
	}

	public FACTFinderService getService() {
		return service;
	}

	public void setService(FACTFinderService service) {
		this.service = service;
	}

}
