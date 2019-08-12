package com.viglet.shiohara.tomcat;

import org.springframework.boot.web.embedded.tomcat.TomcatConnectorCustomizer;
import org.springframework.boot.web.embedded.tomcat.TomcatServletWebServerFactory;
import org.springframework.boot.web.server.WebServerFactoryCustomizer;
import org.springframework.stereotype.Component;

@Component
public class ShTomcatWebServerCustomizer implements WebServerFactoryCustomizer<TomcatServletWebServerFactory> {

	@Override
	public void customize(TomcatServletWebServerFactory factory) {
		factory.addConnectorCustomizers((TomcatConnectorCustomizer) connector -> {
			connector.setAttribute("relaxedPathChars", "<>[\\]^`{|}");
			connector.setAttribute("relaxedQueryChars", "<>[\\]^`{|}");
		});
	}
}