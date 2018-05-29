package com.viglet.shiohara.spring.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.web.firewall.HttpFirewall;
import org.springframework.security.web.firewall.StrictHttpFirewall;

@Configuration
@EnableWebSecurity
@Profile("development")
public class ShSecurityConfigDevelopment extends WebSecurityConfigurerAdapter {
	   
	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http.headers().cacheControl().disable();
		http.csrf().disable();
		http.cors().disable();
	}

	@Override
	public void configure(WebSecurity web) throws Exception {

		super.configure(web);
		web.ignoring().antMatchers("/thirdparty/**", "/js/**", "/css/**", "/template/**", "/img/**", "/sites/**",
				"/swagger-resources/**", "/h2/**");
		web.httpFirewall(allowUrlEncodedSlashHttpFirewall());
	}

	// create two users, admin and user
	@Autowired
	public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {

		auth.inMemoryAuthentication().withUser("user").password("{noop}user").roles("USER").and().withUser("admin")
				.password("{noop}admin").roles("ADMIN");

	}

	@Bean
	public HttpFirewall allowUrlEncodedSlashHttpFirewall() {
		// Allow double slash in URL
		StrictHttpFirewall firewall = new StrictHttpFirewall();
		firewall.setAllowUrlEncodedSlash(true);
		return firewall;
	}
}