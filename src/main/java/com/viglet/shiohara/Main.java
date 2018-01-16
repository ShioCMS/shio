package com.viglet.shiohara;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.jms.annotation.EnableJms;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.viglet.shiohara.sites.ShSites;

import javax.sql.DataSource;

@Controller
@SpringBootApplication
@EnableJms
public class Main {
	@Autowired
	DataSource dataSource;

	public static void main(String[] args) throws Exception {
		SpringApplication.run(Main.class, args);
	}

	// Register Servlet
	@Bean
	public ServletRegistrationBean servletRegistrationBean() {
		ServletRegistrationBean bean = new ServletRegistrationBean(new ShSites(), "/sites");
		return bean;
	}

	@RequestMapping("/")
	String index() {
		return "index";
	}

}
