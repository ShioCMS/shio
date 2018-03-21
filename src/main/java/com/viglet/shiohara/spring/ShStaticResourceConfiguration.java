package com.viglet.shiohara.spring;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.web.DispatcherServletAutoConfiguration;
import org.springframework.context.annotation.Configuration;

import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

import com.viglet.shiohara.utils.ShStaticFileUtils;

@Configuration
@AutoConfigureAfter(DispatcherServletAutoConfiguration.class)
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
}