package com.viglet.shiohara;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.embedded.undertow.UndertowServletWebServerFactory;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.jms.annotation.EnableJms;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.filter.CharacterEncodingFilter;

import com.fasterxml.jackson.databind.Module;
import com.fasterxml.jackson.datatype.hibernate5.Hibernate5Module;

import io.undertow.UndertowOptions;

@SpringBootApplication
@EnableJms
@EnableCaching
public class Shiohara {
	private static final Logger logger = LoggerFactory.getLogger(Shiohara.class);

	public static void main(String[] args) throws Exception {
		SpringApplication.run(Shiohara.class, args);
		logger.info("Viglet Shiohara started");
	}

	@Bean
	public FilterRegistrationBean<CharacterEncodingFilter> filterRegistrationBean() {
		FilterRegistrationBean<CharacterEncodingFilter> registrationBean = new FilterRegistrationBean<CharacterEncodingFilter>();
		CharacterEncodingFilter characterEncodingFilter = new CharacterEncodingFilter();
		characterEncodingFilter.setForceEncoding(true);
		characterEncodingFilter.setEncoding("UTF-8");
		registrationBean.setFilter(characterEncodingFilter);
		return registrationBean;
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

	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}
	
}
