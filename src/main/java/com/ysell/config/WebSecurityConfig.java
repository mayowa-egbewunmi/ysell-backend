package com.ysell.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.ysell.config.jwt.dependencies.JwtExceptionEntryPoint;
import com.ysell.config.jwt.filter.JwtRequestFilter;
import com.ysell.modules.common.constants.ControllerConstants;

import lombok.AllArgsConstructor;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
@AllArgsConstructor
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {
	
	private final UserDetailsService jwtUserDetailsService;	
	private final PasswordEncoder passwordEncoder;	
	private final JwtExceptionEntryPoint jwtExceptionEntryPoint;	
	private final JwtRequestFilter jwtRequestFilter;
	
	public static final String[] ALLOWED_URLS = {
			ControllerConstants.VERSION_URL + "/users/authenticate", 
			ControllerConstants.VERSION_URL + "/users/code/initiate", 
			ControllerConstants.VERSION_URL + "/users/code/verify", 
			ControllerConstants.VERSION_URL + "/users/password/reset",
			"/h2/**",
			"/v2/api-docs",
            "/configuration/ui",
            "/swagger-resources/**",
            "/configuration/security",
            "/swagger-ui.html",
            "/webjars/**"
			};

	@Autowired
	public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
		auth.userDetailsService(jwtUserDetailsService).passwordEncoder(passwordEncoder);
	}

	@Bean
	@Override
	public AuthenticationManager authenticationManagerBean() throws Exception {
		return super.authenticationManagerBean();
	}

	@Override
	protected void configure(HttpSecurity httpSecurity) throws Exception {
		httpSecurity.csrf().disable()
			.authorizeRequests().antMatchers(ALLOWED_URLS).permitAll()
			.anyRequest().authenticated().and()
			.exceptionHandling().authenticationEntryPoint(jwtExceptionEntryPoint).and()
			.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);
		
		// enable access to h2 console frames
		httpSecurity.headers().frameOptions().sameOrigin();
		
		// add a filter to validate the tokens with every request
		httpSecurity.addFilterBefore(jwtRequestFilter, UsernamePasswordAuthenticationFilter.class);
	}
}
