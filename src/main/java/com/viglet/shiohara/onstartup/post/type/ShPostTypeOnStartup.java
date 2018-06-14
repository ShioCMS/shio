package com.viglet.shiohara.onstartup.post.type;

import java.io.IOException;
import java.io.InputStreamReader;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.viglet.shiohara.exchange.ShExchange;
import com.viglet.shiohara.exchange.post.type.ShPostTypeImport;
import com.viglet.shiohara.persistence.repository.post.type.ShPostTypeRepository;

@Component
public class ShPostTypeOnStartup {

	@Autowired
	private ShPostTypeRepository shPostTypeRepository;
	@Autowired
	private ResourceLoader resourceloader;
	@Autowired
	private ShPostTypeImport shPostTypeImport;

	public void createDefaultRows() throws IOException {

		if (shPostTypeRepository.findAll().isEmpty()) {

			InputStreamReader isr = new InputStreamReader(
					resourceloader.getResource("classpath:/import/post-types.json").getInputStream());

			ObjectMapper mapper = new ObjectMapper();

			ShExchange shExchange = mapper.readValue(isr, ShExchange.class);
			shPostTypeImport.importPostType(shExchange);

		}

	}
}
