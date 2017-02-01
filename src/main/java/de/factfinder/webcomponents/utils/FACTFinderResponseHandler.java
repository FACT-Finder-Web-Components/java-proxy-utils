package de.factfinder.webcomponents.utils;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.ResponseHandler;
import org.apache.http.util.EntityUtils;

/**
 * This is the standard implementation of a ResponseHandler for a FACTFinder
 * Request.
 * 
 * @author arno.pitters
 *
 */
public class FACTFinderResponseHandler implements ResponseHandler<FACTFinderResponse> {

	public FACTFinderResponse handleResponse(HttpResponse response) throws ClientProtocolException, IOException {
		int status = response.getStatusLine().getStatusCode();
		Map<String, String> map = new HashMap<String, String>();
		Header[] allHeaders = response.getAllHeaders();
		for (Header header : allHeaders) {
			map.put(header.getName(), header.getValue());
		}

		if (status >= 200 && status < 300) {
			HttpEntity entity = response.getEntity();
			String data = entity != null ? EntityUtils.toString(entity) : null;
			FACTFinderResponse ffHttpResponse = new FACTFinderResponse();
			ffHttpResponse.setStatus(status);
			ffHttpResponse.setData(data);
			ffHttpResponse.setHeaders(map);
			return ffHttpResponse;
		} else {
			throw new ClientProtocolException("Unexpected response status: " + status);
		}
	}

}
