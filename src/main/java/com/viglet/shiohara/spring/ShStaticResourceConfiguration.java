package com.viglet.shiohara.spring;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.web.servlet.DispatcherServletAutoConfiguration;
import org.springframework.context.annotation.Configuration;

import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import com.viglet.shiohara.utils.ShStaticFileUtils;

@Configuration
@AutoConfigureAfter(DispatcherServletAutoConfiguration.class)
public class ShStaticResourceConfiguration implements WebMvcConfigurer {
	@Autowired
	private ShStaticFileUtils shStaticFileUtils;

	@Override
	public void addResourceHandlers(ResourceHandlerRegistry registry) {

		String resourceLocation = "file:" + shStaticFileUtils.getFileSource().getAbsolutePath();
		registry.addResourceHandler("/store/**").addResourceLocations(resourceLocation).setCachePeriod(3600 * 24);
		if (!registry.hasMappingForPattern("/thirdparty/**")) {
			registry.addResourceHandler("/thirdparty/**").addResourceLocations("classpath:/META-INF/resources/webjars/")
					.setCachePeriod(3600 * 24);
		}
	}

	@Override
	public void addCorsMappings(CorsRegistry registry) {
		registry.addMapping("/thirdparty/**").allowedOrigins("*").allowedMethods("PUT", "DELETE", "GET", "POST")
				.allowCredentials(false).maxAge(3600);
		registry.addMapping("/api/**").allowedOrigins("*").allowedMethods("PUT", "DELETE", "GET", "POST")
				.allowCredentials(false).maxAge(3600);
	}
	@Override
	public void addViewControllers(ViewControllerRegistry registry) {
	    registry.addViewController("/content").setViewName("forward:/content/index.html");
	    registry.addViewController("/welcome").setViewName("forward:/welcome/index.html");
	}
}
