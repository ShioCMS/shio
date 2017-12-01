package com.viglet.shiohara.jersey;

import org.glassfish.jersey.server.ResourceConfig;
import org.glassfish.jersey.media.multipart.MultiPartFeature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.viglet.shiohara.api.filter.ShCORSFilter;
import com.viglet.shiohara.api.post.ShPostAPI;
import com.viglet.shiohara.api.post.type.ShPostTypeAPI;
import com.viglet.shiohara.api.post.type.ShPostTypeAttrAPI;
import com.viglet.shiohara.api.region.ShRegionAPI;
import com.viglet.shiohara.api.site.ShSiteAPI;
import com.viglet.shiohara.api.user.ShUserAPI;
import com.viglet.shiohara.api.widget.ShWidgetAPI;

import javax.ws.rs.ApplicationPath;
import javax.ws.rs.ext.ContextResolver;
import javax.ws.rs.ext.Provider;

@Profile("production")
@Component
@ApplicationPath("/api")
public class JerseyConfig extends ResourceConfig {
	@Autowired
	public JerseyConfig(ObjectMapper objectMapper) {
		// register endpoints
		packages("com.shengwang.demo");
		// register jackson for json
		register(new ObjectMapperContextResolver(objectMapper));
		register(ShCORSFilter.class);
		register(ShPostAPI.class);
		register(ShPostTypeAPI.class);
		register(MultiPartFeature.class);
		register(ShPostTypeAttrAPI.class);
		register(ShRegionAPI.class);
		register(ShSiteAPI.class);
		register(ShUserAPI.class);
		register(ShWidgetAPI.class);	
	}

	@Provider
	public static class ObjectMapperContextResolver implements ContextResolver<ObjectMapper> {

		private final ObjectMapper mapper;

		public ObjectMapperContextResolver(ObjectMapper mapper) {
			this.mapper = mapper;
		}

		@Override
		public ObjectMapper getContext(Class<?> type) {
			return mapper;
		}
	}
}
