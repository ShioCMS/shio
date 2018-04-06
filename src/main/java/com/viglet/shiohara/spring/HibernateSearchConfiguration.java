package com.viglet.shiohara.spring;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.viglet.shiohara.persistence.repository.post.ShPostRepository;

@EnableAutoConfiguration
@Configuration
public class HibernateSearchConfiguration {
	@Autowired
	private ShPostRepository shPostRepository;

	@Bean
	boolean hibernateSearchService() {
		return shPostRepository.initializeHibernateSearch();
	}
}
