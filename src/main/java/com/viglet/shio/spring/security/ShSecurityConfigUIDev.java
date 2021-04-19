/*
 * Copyright (C) 2016-2020 the original author or authors. 
 * 
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */
package com.viglet.shio.spring.security;

import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;

/**
 * @author Alexandre Oliveira
 * @since 0.3.7
 */
@Configuration
@EnableWebSecurity
@Profile("ui-dev")
@ComponentScan(basePackageClasses = ShCustomUserDetailsService.class)
public class ShSecurityConfigUIDev extends ShSecurityConfigProduction {
	@Autowired
	private ShAuthenticationEntryPoint shAuthenticationEntryPoint;

	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http.headers().frameOptions().disable().cacheControl().disable();
		http.cors().and().httpBasic().authenticationEntryPoint(shAuthenticationEntryPoint).and().authorizeRequests()
				.antMatchers("/index.html", "/welcome/**", "/", "/store/**", "/thirdparty/**", "/js/**", "/css/**",
						"/template/**", "/img/**", "/sites/**", "/__tur/**", "/swagger-resources/**", "/h2/**",
						"/image/**", "/login-page/**", "/logout-page/**", "/graphql","/manifest.json",
						"/*.png", "/*.ico")
				.permitAll().anyRequest().authenticated().and().logout();

	}
}