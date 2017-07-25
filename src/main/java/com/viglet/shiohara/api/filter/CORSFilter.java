package com.viglet.shiohara.api.filter;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

import javax.servlet.ServletContext;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.ResponseBuilder;

import com.sun.jersey.spi.container.ContainerRequest;
import com.sun.jersey.spi.container.ContainerResponse;
import com.sun.jersey.spi.container.ContainerResponseFilter;

public class CORSFilter implements ContainerResponseFilter {
	@Context
	ServletContext context;
	URL vecchioURL = null;

	@Override
	public ContainerResponse filter(ContainerRequest containerRequest, ContainerResponse containerResponse) {

		int statusCode = (int) context.getAttribute("VecStatusCode");
		
		if (statusCode == 200) {
			ResponseBuilder responseBuilder = Response.fromResponse(containerResponse.getResponse());
			responseBuilder.header("Access-Control-Allow-Origin", "*");
			responseBuilder.header("Access-Control-Allow-Methods", "POST, GET, OPTIONS, PUT, DELETE");
			responseBuilder.header("Access-Control-Allow-Headers", "Content-type");
			responseBuilder.header("Access-Control-Max-Age", "86400");
			responseBuilder.header("Content-Length",
					Integer.getInteger(containerResponse.getHeaderValue("X-Content-Length")));
			containerResponse.setResponse(responseBuilder.build());
		} else {
			ResponseBuilder rb = Response.status(401);
			rb = rb.tag("Authorization Required");
			containerResponse.setResponse(rb.build());

		}

		System.out.println("VecResponse " + statusCode + " " + containerRequest.getRequestUri().getRawQuery() + " | "
				+ containerRequest.getBaseUri().getRawPath() + "||" + containerRequest.getPath() + "|||"
				+ containerRequest.getAbsolutePath().getPath());
		return containerResponse;
	}
}