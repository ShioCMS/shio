package com.viglet.shiohara.spring.security;

import org.springframework.beans.factory.annotation.Autowired;
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
import org.springframework.security.web.csrf.CsrfFilter;
import org.springframework.security.web.csrf.CsrfTokenRepository;
import org.springframework.security.web.csrf.HttpSessionCsrfTokenRepository;
import org.springframework.security.web.firewall.HttpFirewall;
import org.springframework.security.web.firewall.StrictHttpFirewall;

@Configuration
@EnableWebSecurity
@Profile("production")
@ComponentScan(basePackageClasses = ShCustomUserDetailsService.class)
public class ShSecurityConfigProduction extends WebSecurityConfigurerAdapter {
	@Autowired
	UserDetailsService userDetailsService;
	@Autowired
	ShAuthenticationEntryPoint shAuthenticationEntryPoint;

	@Override
	protected void configure(HttpSecurity http) throws Exception {
		// Prevent the HTTP response header of "Pragma: no-cache".
		http.headers().frameOptions().disable().cacheControl().disable();
		http.httpBasic().authenticationEntryPoint(shAuthenticationEntryPoint).and().authorizeRequests()
				.antMatchers("/index.html", "/welcome/**", "/", "/store/**", "/thirdparty/**", "/js/**", "/css/**",
						"/template/**", "/img/**", "/sites/**", "/swagger-resources/**", "/h2/**")
				.permitAll().anyRequest().authenticated().and()
				.addFilterAfter(new ShCsrfHeaderFilter(), CsrfFilter.class).csrf()
				.csrfTokenRepository(csrfTokenRepository()).and().logout();

	}

	@Override
	public void configure(WebSecurity web) throws Exception {
		// TODO Auto-generated method stub
		web.ignoring().antMatchers("/h2/**");
		super.configure(web);
		web.httpFirewall(allowUrlEncodedSlashHttpFirewall());
	}

	@Autowired
	public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
		auth.userDetailsService(userDetailsService).passwordEncoder(passwordencoder());
	}

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