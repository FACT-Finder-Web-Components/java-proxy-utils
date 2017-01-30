package de.factfinder.utils.api;

import java.util.HashMap;
import java.util.Map;

import org.apache.http.client.methods.HttpOptions;

import de.factfinder.utils.FFService;

/**
 * This is a abstraction of the HTTP OPTIONS Request. Used internaly for further
 * mapping and merging of header.
 * 
 * @author arno.pitters
 *
 */
public class OptionsRequest {

	private FFService			service;
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

	public FFService getService() {
		return service;
	}

	public void setService(FFService service) {
		this.service = service;
	}

}
