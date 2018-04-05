package com.viglet.shiohara;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.embedded.undertow.UndertowEmbeddedServletContainerFactory;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.jms.annotation.EnableJms;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.fasterxml.jackson.databind.Module;
import com.fasterxml.jackson.datatype.hibernate5.Hibernate5Module;

import io.undertow.UndertowOptions;

import javax.sql.DataSource;

@Controller
@SpringBootApplication
@EnableJms
@EnableCaching
public class Main {
	@Autowired
	DataSource dataSource;

	public static void main(String[] args) throws Exception {
		SpringApplication.run(Main.class, args);
	}

	@Bean
	public Module hibernate5Module() {
		return new Hibernate5Module();
	}
	@Bean
	UndertowEmbeddedServletContainerFactory embeddedServletContainerFactory() {
	    UndertowEmbeddedServletContainerFactory factory = new UndertowEmbeddedServletContainerFactory();
	    factory.addBuilderCustomizers(
	            builder -> builder.setServerOption(UndertowOptions.ENABLE_HTTP2, true));
	    return factory;
	}
	@RequestMapping("/")
	String index() {
		return "index";
	}

}
