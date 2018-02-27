package com.viglet.shiohara.staticfile;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

import com.viglet.shiohara.utils.ShStaticFileUtils;

@Configuration
public class StaticResourceConfiguration extends WebMvcConfigurerAdapter {
	@Autowired
	ShStaticFileUtils shStaticFileUtils;
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
    	
    		String resourceLocation = "file:" + shStaticFileUtils.getFileSource().getAbsolutePath();
        registry.addResourceHandler("/store/**").addResourceLocations(resourceLocation);
		if (!registry.hasMappingForPattern("/thirdparty/**")) {
			registry.addResourceHandler("/thirdparty/**").addResourceLocations(
                "classpath:/META-INF/resources/webjars/");
		}
    }
}