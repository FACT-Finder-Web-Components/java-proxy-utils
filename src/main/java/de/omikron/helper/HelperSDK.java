package de.omikron.helper;

import java.io.IOException;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.FutureTask;
import java.util.concurrent.TimeoutException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import de.omikron.helper.client.HTTPRequestProcessor;
import de.omikron.helper.client.WebserviceAccess;
import de.omikron.helper.domain.Result;
import de.omikron.helper.settings.FFResponse;
import de.omikron.helper.settings.FFService;
import de.omikron.helper.settings.FFSettings;

public class HelperSDK {

	private FFSettings settings;

	public HelperSDK(FFSettings settings) {
		this.settings = settings;
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
		WebserviceAccess wsAccess = null;//TODO: change to settings
		String result = HTTPRequestProcessor.retrieveJSONResult(wsAccess, req);
		return result;
	}

	public void redirect(HttpServletRequest req, HttpServletResponse res) throws IOException {
		// set the payload of the old request as the new request
		writeResponse(res, sendRequest(req));
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

}
