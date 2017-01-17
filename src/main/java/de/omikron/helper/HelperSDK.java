package de.omikron.helper;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Enumeration;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.FutureTask;
import java.util.concurrent.TimeoutException;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import de.omikron.helper.client.HTTPRequestProcessor;
import de.omikron.helper.client.WebserviceAccess;
import de.omikron.helper.domain.Result;
import de.omikron.helper.settings.FFResponse;
import de.omikron.helper.settings.FFService;
import de.omikron.helper.settings.FFSettings;

public class HelperSDK {

	private static final String	ACCESS_CONTROL_ALLOW_HEADERS	= "Access-Control-Allow-Headers";
	private static final String	ACCESS_CONTROL_REQUEST_HEADERS	= "Access-Control-Request-Headers";

	private FFSettings			settings;

	public HelperSDK(FFSettings settings) {
		this.settings = settings;
	}

	// 0. manage preflight
	public void options(HttpServletRequest req, HttpServletResponse resp) {
		System.out.println(req);

		Collection<String> oldHeader = resp.getHeaders(ACCESS_CONTROL_ALLOW_HEADERS);
		System.out.println("old:" + oldHeader);
		List<String> list = Collections.list(req.getHeaders(ACCESS_CONTROL_REQUEST_HEADERS));
		System.out.println("new: " + list);
		list.addAll(oldHeader);
		System.out.println("retain: " + list);
		String respHeaders = "";
		for (int i = 0; i < list.size(); i++) {
			if (i != 0) {
				respHeaders += ",";
			}
			respHeaders += list.get(i);
		}
		System.out.println("result: " + respHeaders);

		resp.setHeader("Access-Control-Allow-Origin", "*");
		resp.setHeader("Access-Control-Allow-Methods", "GET, POST");
		resp.setHeader("Allow", "GET, HEAD, POST, TRACE, OPTIONS");
		resp.setHeader("Access-Control-Max-Age", "86400");
		resp.setHeader(ACCESS_CONTROL_ALLOW_HEADERS, respHeaders);
	}

	// 1. FACT-Finder Authentifizierung
	public FFResponse get(HttpServletRequest req) throws IOException {
		FFService service = extractService(req);
		String data = sendRequest(req);
		FFResponse ffResponse = new FFResponse(data, service);
		return ffResponse;
	}

	public Future<String> getAsync(final HttpServletRequest req)
			throws InterruptedException, ExecutionException, TimeoutException {

		FutureTask<String> task = new FutureTask<String>(new Callable<String>() {

			public String call() throws Exception {
				return sendRequest(req);
			}
		});

		return task;
	}

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
	public Result parse(String content) {
		// TODO parse stuff
		return null;
	}

	public Result parse(String content, FFService service) {
		// TODO parse stuff for that service
		return null;
	}

	public Result parse(FFResponse res) {
		// TODO parse stuff for that service
		return null;
	}

	public String asJson(Result result) {
		// TODO
		return "";
	}

	private FFService extractService(HttpServletRequest req) {
		// TODO
		return null;
	}
	
	public void copyHeaders(HttpServletRequest req, HttpServletResponse resp){
		resp.setHeader("Access-Control-Allow-Origin", "*");
		resp.setHeader("Access-Control-Allow-Methods", "GET, POST");
		resp.setHeader("Allow", "GET, HEAD, POST, TRACE, OPTIONS");
		resp.setHeader("Access-Control-Max-Age", "86400");
	}

}
