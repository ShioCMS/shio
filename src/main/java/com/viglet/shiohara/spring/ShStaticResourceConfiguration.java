package com.viglet.shiohara.spring;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.hibernate4.Hibernate4Module;
import com.viglet.shiohara.utils.ShStaticFileUtils;

@Configuration
@EnableWebMvc
public class ShStaticResourceConfiguration extends WebMvcConfigurerAdapter {
	@Autowired
	ShStaticFileUtils shStaticFileUtils;

	@Override
	public void addResourceHandlers(ResourceHandlerRegistry registry) {

		String resourceLocation = "file:" + shStaticFileUtils.getFileSource().getAbsolutePath();
		registry.addResourceHandler("/store/**").addResourceLocations(resourceLocation);
		if (!registry.hasMappingForPattern("/thirdparty/**")) {
			registry.addResourceHandler("/thirdparty/**")
					.addResourceLocations("classpath:/META-INF/resources/webjars/");
		}
	}

	@Override
	public void addCorsMappings(CorsRegistry registry) {
		registry.addMapping("/thirdparty/**").allowedOrigins("*").allowedMethods("PUT", "DELETE", "GET", "POST")
				.allowCredentials(false).maxAge(3600);
	}

	public MappingJackson2HttpMessageConverter jacksonMessageConverter() {
		MappingJackson2HttpMessageConverter messageConverter = new MappingJackson2HttpMessageConverter();

		ObjectMapper mapper = new ObjectMapper();
		// Registering Hibernate4Module to support lazy objects
		mapper.registerModule(new Hibernate4Module());

		messageConverter.setObjectMapper(mapper);
		return messageConverter;

	}

	@Override
	public void configureMessageConverters(List<HttpMessageConverter<?>> converters) {
		converters.add(jacksonMessageConverter());
		super.configureMessageConverters(converters);
	}

}