package de.omikron;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.ServletHandler;

import de.omikron.helper.HelperSDK;
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
		settings.setPassword("Norkimo12!");
		settings.setUrl("http://web-components.fact-finder.de/FACT-Finder-7.2");
		sdk = new HelperSDK(settings);
		super.init();
	}

	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		sdk.redirect(req, resp);
	};

	public void doOptions(HttpServletRequest req, HttpServletResponse resp) {
		sdk.options(req, resp);
	}
}
