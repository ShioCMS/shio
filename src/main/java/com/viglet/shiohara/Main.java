package com.viglet.shiohara;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.jms.annotation.EnableJms;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

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
	
	@RequestMapping("/")
	String index() {
		return "index";
	}

}
