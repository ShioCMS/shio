package com.viglet.shiohara.api.filter;

import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerResponseContext;
import javax.ws.rs.container.ContainerResponseFilter;

public class ShCORSFilter implements ContainerResponseFilter {

	@Override
	public void filter(ContainerRequestContext creq, ContainerResponseContext cres) {

		cres.getHeaders().add("Access-Control-Allow-Origin", "*");
		cres.getHeaders().add("Access-Control-Allow-Methods", "POST, GET, OPTIONS, PUT, DELETE");
		cres.getHeaders().add("Access-Control-Allow-Headers", "Content-type");
		cres.getHeaders().add("Access-Control-Max-Age", "86400");
		cres.getHeaders().add("Access-Control-Max-Age", "1209600");
		cres.getHeaders().add("Content-Length", Integer.getInteger(cres.getHeaderString("X-Content-Length")));
	}

}
