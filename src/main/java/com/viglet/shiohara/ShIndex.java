package com.viglet.shiohara;

import java.io.IOException;
import java.security.Principal;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class ShIndex {

	@RequestMapping("/")
	private void index(HttpServletRequest request, HttpServletResponse response, final Principal principal)
			throws IOException {
		if (principal != null) {
			response.sendRedirect("/content");
		} else {
			response.sendRedirect("/welcome");
		}

	}
}
