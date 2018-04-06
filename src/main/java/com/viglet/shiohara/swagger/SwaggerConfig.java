package com.viglet.shiohara.swagger;

import java.util.Collections;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@Configuration
@EnableSwagger2
public class SwaggerConfig {
	@Bean
	public Docket api() {
		ShCustomPathPrivider pathProvider = new ShCustomPathPrivider();

		return new Docket(DocumentationType.SWAGGER_2).pathProvider(pathProvider).select()
				.apis(RequestHandlerSelectors.basePackage("com.viglet.shiohara.api")).paths(PathSelectors.any()).build()
				.apiInfo(apiInfo());
	}

	private ApiInfo apiInfo() {
		return new ApiInfo("Viglet Shiohara", "An intuitive and simple Web Content Management", "2.0",
				"Terms of service",
				new Contact("Viglet Team", "http://www.viglet.com", "alexandre.oliveira@viglet.com"), "License of API",
				"API license URL", Collections.emptyList());
	}

}