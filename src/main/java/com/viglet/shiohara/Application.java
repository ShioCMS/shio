package com.viglet.shiohara;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.jms.annotation.EnableJms;
import org.springframework.web.bind.annotation.RequestMapping;

import com.fasterxml.jackson.databind.Module;
import com.fasterxml.jackson.datatype.hibernate5.Hibernate5Module;

import javax.sql.DataSource;

@SpringBootApplication
@EnableJms
@EnableCaching
public class Application {
	@Autowired
	DataSource dataSource;

	public static void main(String[] args) throws Exception {
		SpringApplication.run(Application.class, args);
	}

	@Bean
	public Module hibernate5Module() {
		return new Hibernate5Module();
	}	
	@RequestMapping("/")
	String index() {
		return "index";
	}

}
