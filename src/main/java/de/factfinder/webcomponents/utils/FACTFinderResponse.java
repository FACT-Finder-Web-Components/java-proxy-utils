package de.factfinder.webcomponents.utils;

import java.util.Map;

/**
 * This is a abstraction of the response from FACTFinder service.
 * 
 * @author arno.pitters
 *
 */
public class FACTFinderResponse {

	private int					status;
	private Map<String, String>	headers;
	private String				data;

	public FACTFinderResponse() {
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public Map<String, String> getHeaders() {
		return headers;
	}

	public void setHeaders(Map<String, String> headers) {
		this.headers = headers;
	}

	public String getData() {
		return data;
	}

	public void setData(String data) {
		this.data = data;
	}

	@Override
	public String toString() {
		return "FFHttpResponse [status=" + status + ", headers=" + headers + ", data=" + data + "]";
	}

}
