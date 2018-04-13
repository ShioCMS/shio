package com.viglet.shiohara;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.embedded.undertow.UndertowServletWebServerFactory;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.jms.annotation.EnableJms;

import com.fasterxml.jackson.databind.Module;
import com.fasterxml.jackson.datatype.hibernate5.Hibernate5Module;

import io.undertow.UndertowOptions;

@SpringBootApplication
@EnableJms
@EnableCaching
public class Shiohara {

	public static void main(String[] args) throws Exception {
		SpringApplication.run(Shiohara.class, args);
	}

	@Bean
	public Module hibernate5Module() {
		return new Hibernate5Module();
	}

	@Bean
	UndertowServletWebServerFactory embeddedServletContainerFactory() {
		UndertowServletWebServerFactory factory = new UndertowServletWebServerFactory();
		factory.addBuilderCustomizers(builder -> builder.setServerOption(UndertowOptions.ENABLE_HTTP2, true));
		return factory;
	}

}
