package com.viglet.shiohara.spring.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.web.csrf.CsrfFilter;
import org.springframework.security.web.csrf.CsrfTokenRepository;
import org.springframework.security.web.csrf.HttpSessionCsrfTokenRepository;

@Configuration
@EnableWebSecurity
@Profile("production")
public class ShSecurityConfigProduction extends WebSecurityConfigurerAdapter {
	@Autowired
	ShAccessDenied shAccessDenied;

	@Override
	protected void configure(HttpSecurity http) throws Exception {
		// Prevent the HTTP response header of "Pragma: no-cache".
		http.headers().cacheControl().disable();
		// TODO: Enable CSRF
	/*	http.csrf().disable().authorizeRequests().antMatchers("/", "/home", "/about").permitAll()
				.antMatchers("/api/v2/**").hasAnyRole("ADMIN")
				.antMatchers("/user/**").hasAnyRole("USER")
				.anyRequest().authenticated()
				.and()
				.formLogin().loginPage("/login").permitAll()
				.and()
				.logout().permitAll()
				.and()
				.exceptionHandling().accessDeniedHandler(shAccessDenied);*/
		 http
	      .httpBasic().and()
	      .authorizeRequests()
	        .antMatchers("/index.html", "/welcome/**", "/").permitAll().anyRequest()
	        .authenticated().and()
	      .addFilterAfter(new ShCsrfHeaderFilter(), CsrfFilter.class).csrf().csrfTokenRepository(csrfTokenRepository()).and()
	      .logout();

	}

	@Override
	public void configure(WebSecurity web) throws Exception {
		web.ignoring().antMatchers("/thirdparty/**", "/js/**","/css/**", "/template/**","/img/**","/sites/**","/Home/**", "/swagger-resources/**");
	}

	// create two users, admin and user
	@Autowired
	public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {

		auth.inMemoryAuthentication().withUser("user").password("{noop}user").roles("USER").and().withUser("admin")
				.password("{noop}admin").roles("ADMIN");
	}

	private CsrfTokenRepository csrfTokenRepository() {
		  HttpSessionCsrfTokenRepository repository = new HttpSessionCsrfTokenRepository();
		  repository.setHeaderName("X-XSRF-TOKEN");
		  return repository;
		}
}