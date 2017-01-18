package de.omikron.helper;

import java.io.IOException;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.gson.Gson;

import de.omikron.helper.client.HTTPRequestProcessor;
import de.omikron.helper.domain.Result;
import de.omikron.helper.reponse.SearchResponse;
import de.omikron.helper.settings.FFService;
import de.omikron.helper.settings.FFSettings;

public class HelperSDK {

	private static final String	ACCESS_CONTROL_ALLOW_HEADERS	= "Access-Control-Allow-Headers";
	private static final String	ACCESS_CONTROL_REQUEST_HEADERS	= "Access-Control-Request-Headers";

	private FFSettings			settings;
	private Gson				gson							= new Gson();

	public HelperSDK(FFSettings settings) {
		this.settings = settings;
	}

	// 0. manage preflight
	public void options(HttpServletRequest req, HttpServletResponse resp) {

		Collection<String> oldHeader = resp.getHeaders(ACCESS_CONTROL_ALLOW_HEADERS);
		List<String> list = Collections.list(req.getHeaders(ACCESS_CONTROL_REQUEST_HEADERS));
		list.addAll(oldHeader);
		String respHeaders = "";
		for (int i = 0; i < list.size(); i++) {
			if (i != 0) {
				respHeaders += ",";
			}
			respHeaders += list.get(i);
		}
		resp.setHeader("Access-Control-Allow-Origin", "*");
		resp.setHeader("Access-Control-Allow-Methods", "GET, POST");
		resp.setHeader("Allow", "GET, HEAD, POST, TRACE, OPTIONS");
		resp.setHeader("Access-Control-Max-Age", "86400");
		resp.setHeader(ACCESS_CONTROL_ALLOW_HEADERS, respHeaders);
	}

	// 1. FACT-Finder Authentifizierung
	// public FFResponse get(HttpServletRequest req) throws IOException {
	// return new FFResponse(sendRequest(req), extractService(req));
	// }

	// 2. absetzen von http request
	public String sendRequest(HttpServletRequest req) throws IOException {
		return HTTPRequestProcessor.retrieveResult(settings, req);
	}

	public void redirect(HttpServletRequest req, HttpServletResponse resp) throws IOException {
		// set the payload of the old request as the new request
		copyHeaders(req, resp);
		writeResponse(resp, sendRequest(req));
	}

	public void writeResponse(HttpServletResponse resp, String content) throws IOException {
		resp.getOutputStream().write(content.getBytes());
	}

	// 3. Bearbeiten der JSON-Response
	public SearchResponse parse(String rawJson) {
		return gson.fromJson(rawJson, SearchResponse.class);
	}

	// public Result parse(String rawJson, FFService service) {
	// return parse(rawJson);
	// }

	// public Result parse(FFResponse res) {
	// return parse(res.getContent());
	// }

	public String asJson(SearchResponse result) {
		return gson.toJson(result);
	}

	private FFService extractService(HttpServletRequest req) {
		String extractService = HTTPRequestProcessor.extractService(req);
		return FFService.valueOf(extractService.replaceAll(".ff", "").toUpperCase());
	}

	public void copyHeaders(HttpServletRequest req, HttpServletResponse resp) {
		resp.setHeader("Access-Control-Allow-Origin", "*");
		resp.setHeader("Access-Control-Allow-Methods", "GET, POST");
		resp.setHeader("Allow", "GET, HEAD, POST, TRACE, OPTIONS");
		resp.setHeader("Access-Control-Max-Age", "86400");
	}

}
