package com.viglet.shiohara.listener;

import javax.servlet.http.*;

import com.viglet.shiohara.listener.onstartup.ShWidgetOnStartup;
import com.viglet.shiohara.listener.onstartup.system.ShConfigVarOnStartup;
import com.viglet.shiohara.listener.onstartup.system.ShLocaleOnStartup;
import com.viglet.shiohara.persistence.service.system.ShConfigVarService;

import javax.servlet.*;

public class ShListener implements ServletContextListener, HttpSessionListener {
	final String FIRST_TIME = "FIRST_TIME";
	ServletContext servletContext;
	ShConfigVarService shConfigVarService = new ShConfigVarService();

	/* A listener class must have a zero-argument constructor: */
	public ShListener() {
	}

	/* Methods from the ServletContextListener */
	public void contextInitialized(ServletContextEvent sce) {
		if (shConfigVarService.get(FIRST_TIME) == null) {
			servletContext = sce.getServletContext();
			System.out.println("First Time Configuration ...");
			ShLocaleOnStartup.createDefaultRows();
			ShWidgetOnStartup.createDefaultRows();
			ShConfigVarOnStartup.createDefaultRows();
			System.out.println("Configuration finished.");
		}

	}

	public void contextDestroyed(ServletContextEvent sce) {
	}

	/* Methods for the HttpSessionListener */
	public void sessionCreated(HttpSessionEvent hse) {
		log("CREATE", hse);
	}

	public void sessionDestroyed(HttpSessionEvent hse) {

		HttpSession _session = hse.getSession();
		long _start = _session.getCreationTime();
		long _end = _session.getLastAccessedTime();
		String _counter = (String) _session.getAttribute("counter");
		log("DESTROY, Session Duration:" + (_end - _start) + "(ms) Counter:" + _counter, hse);
	}

	protected void log(String msg, HttpSessionEvent hse) {
		String _ID = hse.getSession().getId();
		log("SessionID:" + _ID + "    " + msg);
	}

	protected void log(String msg) {
		System.out.println("[" + getClass().getName() + "] " + msg);
	}
}