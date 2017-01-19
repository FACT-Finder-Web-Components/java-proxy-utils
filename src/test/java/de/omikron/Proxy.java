package de.omikron;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.ServletHandler;

import de.omikron.helper.HelperSDK;
import de.omikron.helper.reponse.SearchResponse;
import de.omikron.helper.settings.FFSettings;

@SuppressWarnings("serial")
public class Proxy extends HttpServlet {

	/**
	 * Start a jetty Server with the Proxy Servlet
	 * 
	 * @param args
	 * @throws Exception
	 */
	public static void main(String[] args) throws Exception {
		Server server = new Server(8080);
		ServletHandler handler = new ServletHandler();
		server.setHandler(handler);

		handler.addServletWithMapping(Proxy.class, "/*");
		server.start();
		server.join();
	}

	private HelperSDK	sdk;
	private FFSettings	settings;

	@Override
	public void init() throws ServletException {
		settings = new FFSettings();
		settings.setAccount("admin");
		settings.setPassword("adminpw");
		settings.setPrefix("FACT-FINDER");
		settings.setPostfix("FACT-FINDER");
		settings.setUrl("http://web-components.fact-finder.de/FACT-Finder-7.2");
		sdk = new HelperSDK(settings);
		super.init();
	}

	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		// 1. just redirect
		// sdk.redirect(req, resp);

		// 2. Manually send request
		// sdk.copyHeaders(req, resp);
		// String result = sdk.sendRequest(req);
		// sdk.writeResponse(resp, result);

		// 3. extract service from request
		// sdk.copyHeaders(req, resp);
		// FFResponse ffResponse = sdk.get(req);
		// System.out.println("Service: " + ffResponse.getService());
		// sdk.writeResponse(resp, ffResponse.getContent());

		// 4.parse the result to Objects and back to json
		sdk.copyHeaders(req, resp);
		String json = sdk.sendRequest(req);
		long start = System.currentTimeMillis();
		SearchResponse parse = sdk.parse(json);
		//TODO: add suggest response
		//TODO: add recommendation response
		System.out.println("parse time:" + (System.currentTimeMillis() - start));

		sdk.writeResponse(resp, sdk.asJson(parse));

		// sdk.writeResponse(resp, json);

		// Result result2 = sdk.parse(ffResponse.getContent());
		// Result result3 = sdk.parse(ffResponse.getContent(),
		// ffResponse.getService());

		// String asJson = sdk.asJson(result1);
		// System.out.println(asJson);
		// sdk.writeResponse(resp, asJson);
	};

	public void doOptions(HttpServletRequest req, HttpServletResponse resp) {
		sdk.options(req, resp);
	}
}
