package com.viglet.shiohara.api.filter;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

import javax.servlet.ServletContext;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.ResponseBuilder;
import javax.ws.rs.ext.Provider;

import org.json.JSONObject;

import com.sun.jersey.spi.container.ContainerRequest;
import com.sun.jersey.spi.container.ContainerRequestFilter;

@Provider
public class VecchioFilter implements ContainerRequestFilter {
	@Context
	ServletContext context;
	URL vecchioURL = null;

	@Override
	public ContainerRequest filter(ContainerRequest containerRequest) {

		System.out.println("ContainerRequest");

		int statusCode = 0;

		String protocol = containerRequest.getAbsolutePath().getScheme();
		String host = containerRequest.getAbsolutePath().getHost();
		int port = containerRequest.getAbsolutePath().getPort();
		String vecServer = "http://localhost:2702";

		try {
			String vecContext = vecServer.concat("/api/token_validate");
			String vecFullURL = vecContext;

			if (containerRequest.getRequestUri() != null && containerRequest.getRequestUri().getRawQuery() != null) {
				vecFullURL = vecContext.concat("?" + containerRequest.getRequestUri().getRawQuery());
			}
			URL vecchioURL = new URL(vecFullURL);
			URLConnection vecConnection = vecchioURL.openConnection();
			HttpURLConnection connection = (HttpURLConnection) vecConnection;
			connection.setRequestProperty("VecContext", containerRequest.getAbsolutePath().getPath());
			connection.setUseCaches(false);
			connection.setDoInput(true);
			connection.setDoOutput(true);
			connection.setRequestMethod("GET");
			connection.connect();
			statusCode = connection.getResponseCode();
			if (statusCode == 200) {
				InputStreamReader in = new InputStreamReader((InputStream) connection.getContent());
				BufferedReader buff = new BufferedReader(in);
				StringBuffer sb = new StringBuffer();
				String line;
				do {
					line = buff.readLine();
					sb.append(line + "\n");
				} while (line != null);
				context.setAttribute("VecJSONResponse", new JSONObject(sb.toString()));
			} else {
				context.setAttribute("VecJSONResponse", null);
			}
			context.setAttribute("VecStatusCode", statusCode);

		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

		return containerRequest;
	}

}