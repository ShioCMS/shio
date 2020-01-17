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
package com.viglet.shiohara.spring.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.csrf.CookieCsrfTokenRepository;
import org.springframework.security.web.csrf.CsrfFilter;
import org.springframework.security.web.csrf.CsrfTokenRepository;
import org.springframework.security.web.csrf.HttpSessionCsrfTokenRepository;
import org.springframework.security.web.firewall.HttpFirewall;
import org.springframework.security.web.firewall.StrictHttpFirewall;

import com.viglet.shiohara.persistence.model.provider.auth.ShAuthProviderInstance;
import com.viglet.shiohara.persistence.repository.provider.auth.ShAuthProviderInstanceRepository;
import com.viglet.shiohara.provider.auth.ShAuthSystemProviderVendor;
import com.viglet.shiohara.provider.auth.ShAuthenticationProvider;

/**
 * @author Alexandre Oliveira
 */
@Configuration
@EnableWebSecurity
@Profile("production")
@ComponentScan(basePackageClasses = ShCustomUserDetailsService.class)
public class ShSecurityConfigProduction extends WebSecurityConfigurerAdapter {
	@Autowired
	private UserDetailsService userDetailsService;
	@Autowired
	private ShAuthenticationEntryPoint shAuthenticationEntryPoint;
	@Autowired
	private ShAuthProviderInstanceRepository shAuthProviderInstanceRepository;
	@Autowired
	private ApplicationContext context;

	@Override
	protected void configure(AuthenticationManagerBuilder auth) throws Exception {
		boolean hasAuthProvider = false;
		for (ShAuthProviderInstance instance : shAuthProviderInstanceRepository.findByEnabled(true)) {
			if (!hasAuthProvider) {
				hasAuthProvider = true;
				if (instance.getVendor().getId().equals(ShAuthSystemProviderVendor.NATIVE)) {
					super.configure(auth);
				} else {
					ShAuthenticationProvider shAuthenticationProvider = (ShAuthenticationProvider) context
							.getBean(Class.forName(instance.getVendor().getClassName()));
					shAuthenticationProvider.init(instance.getId());
					auth.authenticationProvider(shAuthenticationProvider);

				}
			}

		}
		if (!hasAuthProvider) {
			super.configure(auth);
		}
	}

	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http.headers().frameOptions().disable().cacheControl().disable();
		http.httpBasic().authenticationEntryPoint(shAuthenticationEntryPoint).and().authorizeRequests()
				.antMatchers("/index.html", "/welcome/**", "/", "/store/**", "/thirdparty/**", "/js/**", "/css/**",
						"/template/**", "/img/**", "/sites/**", "/__tur/**", "/swagger-resources/**", "/h2/**",
						"/image/**", "/login-page/**", "/logout-page/**")
				.permitAll().anyRequest().authenticated().and()
				.addFilterAfter(new ShCsrfHeaderFilter(), CsrfFilter.class).csrf()
				.csrfTokenRepository(CookieCsrfTokenRepository.withHttpOnlyFalse()).and().logout();
	}

	@Override
	public void configure(WebSecurity web) throws Exception {
		web.ignoring().antMatchers("/h2/**");
		super.configure(web);
		web.httpFirewall(allowUrlEncodedSlashHttpFirewall());
	}

	@Autowired
	public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
		auth.userDetailsService(userDetailsService).passwordEncoder(passwordencoder());
	}

	@SuppressWarnings("unused")
	private CsrfTokenRepository csrfTokenRepository() {
		HttpSessionCsrfTokenRepository repository = new HttpSessionCsrfTokenRepository();
		repository.setHeaderName("X-XSRF-TOKEN");
		return repository;
	}

	@Bean(name = "passwordEncoder")
	public PasswordEncoder passwordencoder() {
		return new BCryptPasswordEncoder();
	}

	@Bean
	public HttpFirewall allowUrlEncodedSlashHttpFirewall() {
		// Allow double slash in URL
		StrictHttpFirewall firewall = new StrictHttpFirewall();
		firewall.setAllowUrlEncodedSlash(true);
		return firewall;
	}
}