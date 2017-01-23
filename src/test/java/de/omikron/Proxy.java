package de.omikron;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.ServletHandler;

import de.omikron.helper.FFService;
import de.omikron.helper.FFSettings;
import de.omikron.helper.HelperSDK;
import de.omikron.helper.reponse.FFResponse;
import de.omikron.helper.reponse.SearchResponse;

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

	// 1. just redirect
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		sdk.redirectGET(req, resp);
	};

	// 2. Manually send request
	protected void doGet2(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		sdk.copyHeaders(req, resp);
		String json = sdk.sendRequest(req).getData();
		sdk.writeResponse(resp, json);
	};

	// 3.parse the result to Objects
	protected void doGet3(HttpServletRequest req, HttpServletResponse resp) throws Exception {
		sdk.copyHeaders(req, resp);
		String json = sdk.sendRequest(req).getData();
		long start = System.currentTimeMillis();
		SearchResponse parse = (SearchResponse) sdk.parse(json, null);
		System.out.println("parse time:" + (System.currentTimeMillis() - start));
		sdk.writeResponse(resp, sdk.asJson(parse));
	};

	// 4.parse the result to Objects
	protected void doGet4(HttpServletRequest req, HttpServletResponse resp) throws Exception {
		sdk.copyHeaders(req, resp);

		// extract service
		FFService service = HelperSDK.extractService(req);
		String json = sdk.sendRequest(req).getData();

		FFResponse parsedServiceResult = sdk.parse(json, service);
		sdk.writeResponse(resp, sdk.asJson(parsedServiceResult));
	};

	public void doOptions(HttpServletRequest req, HttpServletResponse resp) throws IOException {
		sdk.redirectOPTIONS(req, resp);
	}
}
