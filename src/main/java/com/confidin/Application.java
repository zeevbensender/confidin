package com.confidin;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.web.csrf.CookieCsrfTokenRepository;

@SpringBootApplication
public class Application extends WebSecurityConfigurerAdapter {
	private final static Logger LOG = LoggerFactory.getLogger(Application.class);
	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http
				.antMatcher("/**")
				.authorizeRequests()
				.antMatchers("/", "/login", "/login**", "/public/**", "/profile/**")
				.permitAll()
				.anyRequest()
				.authenticated().and().logout().logoutSuccessUrl("/").permitAll().and().
				csrf().csrfTokenRepository(CookieCsrfTokenRepository.withHttpOnlyFalse());
		LOG.info("###### ENABLED PROTOCOLS ARE: {}", System.getProperty("https.protocols"));
	}
	public static void main(String[] args) {
		SpringApplication.run(Application.class, args);
	}
}
