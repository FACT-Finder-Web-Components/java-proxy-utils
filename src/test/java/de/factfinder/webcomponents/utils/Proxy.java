package de.factfinder.webcomponents.utils;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.ServletHandler;

import de.factfinder.webcomponents.utils.FACTFinderSettings;
import de.factfinder.webcomponents.utils.ProxyUtils;

@SuppressWarnings("serial")
public class Proxy extends HttpServlet {

	/**
	 * Start a embedded Jetty Server with the Proxy Servlet.
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

	private ProxyUtils			utils;
	private FACTFinderSettings	settings;

	@Override
	public void init() throws ServletException {
		System.out.println("Settings");
		settings = new FACTFinderSettings();

		settings.setAccount("admin");
		settings.setPassword("adminpw");
		settings.setUrl("http://web-components.fact-finder.de/FACT-Finder-7.2");

		System.out.println(settings.getAccount());
		System.out.println(settings.getPassword());
		System.out.println(settings.getPostfix());
		System.out.println(settings.getPrefix());
		System.out.println(settings.getUrl());
		utils = new ProxyUtils(settings);
	}

	// route HTTP OPTIONS
	public void doOptions(HttpServletRequest req, HttpServletResponse resp) throws IOException {
		utils.redirectOPTIONS(req, resp);
	}

	// 1. just redirect
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		utils.redirectGET(req, resp);
	};

	// 2. Manually send request
	protected void doGet2(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		ProxyUtils.copyHeaders(req, resp);
		String json = utils.sendRequest(req).getData();
		// do something with response
		utils.writeResponse(resp, json);
	};

}
