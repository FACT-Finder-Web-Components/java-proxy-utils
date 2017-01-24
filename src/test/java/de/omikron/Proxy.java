package de.omikron;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.ServletHandler;

import de.omikron.helper.FACTFinderSettings;
import de.omikron.helper.HelperSDK;

@SuppressWarnings("serial")
public class Proxy extends HttpServlet {

	/**
	 * Start a embedded Jetty Server with the Proxy Servlet
	 * 
	 * @param args
	 * @throws Exception
	 */
	public static void main(String[] args) throws Exception {
		Server server = new Server(8080);
		ServletHandler handler = new ServletHandler();
		handler.addServletWithMapping(Proxy.class, "/*");
		server.setHandler(handler);
		server.start();
		server.join();
	}

	private HelperSDK			sdk;
	private FACTFinderSettings	settings;

	@Override
	public void init() throws ServletException {
		// TODO: add property file loading
		// settings = FACTFinderSettings.load();

		settings = new FACTFinderSettings();
		settings.setAccount("admin");
		settings.setPassword("adminpw");
		settings.setUrl("http://web-components.fact-finder.de/FACT-Finder-7.2");

		sdk = new HelperSDK(settings);
		super.init();
	}

	// route HTTP OPTIONS
	public void doOptions(HttpServletRequest req, HttpServletResponse resp) throws IOException {
		sdk.redirectOPTIONS(req, resp);
	}

	// 1. just redirect
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		sdk.redirectGET(req, resp);
	};

	// 2. Manually send request
	protected void doGet2(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		sdk.copyHeaders(req, resp);
		String json = sdk.sendRequest(req).getData();
		// do something with response
		sdk.writeResponse(resp, json);
	};

}
