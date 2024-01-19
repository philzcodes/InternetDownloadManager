package com.philzcodes.idm.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.servlet.util.matcher.MvcRequestMatcher;
import org.springframework.web.servlet.handler.HandlerMappingIntrospector;

import com.philzcodes.idm.service.JpaUserDetailsService;



@Configuration
@EnableWebSecurity
public class SecurityConfig {
	
	private final JpaUserDetailsService jpaUserDetailsService;
	
		
	public SecurityConfig(JpaUserDetailsService jpaUserDetailsService) {
		this.jpaUserDetailsService = jpaUserDetailsService;
	}


	@Bean
	SecurityFilterChain securityFilterChain(HttpSecurity http, HandlerMappingIntrospector introspector) 
			throws Exception{
		MvcRequestMatcher.Builder mvcMatcherBuilder = new MvcRequestMatcher.Builder(introspector);
	return	http
			.authorizeHttpRequests((authorizeHttpRequests) ->
		 		authorizeHttpRequests
			 		.requestMatchers(mvcMatcherBuilder.pattern("/*"), mvcMatcherBuilder.pattern("/js/**"),
							mvcMatcherBuilder.pattern("/css/**"), mvcMatcherBuilder.pattern("/images/**"),
							mvcMatcherBuilder.pattern("/fonts/**"), mvcMatcherBuilder.pattern("/scss/**")).permitAll()
		 	)
//			.authorizeHttpRequests((auth)-> auth.requestMatchers("/admin/**").hasAnyAuthority("ROLE_ADMIN"))			
			.authorizeHttpRequests(
			(auth)-> auth.anyRequest().authenticated())
			.formLogin(((formLogin) ->
				{
					try {
						formLogin
							.usernameParameter("username")
							.passwordParameter("password")
							.loginPage("/login")
							.defaultSuccessUrl("/dashboard")
							.and()
							.logout()
							.logoutUrl("/logout")
							.logoutSuccessUrl("/login")
							.invalidateHttpSession(true)
							.deleteCookies("JSESSIONID");
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
//					.permitAll()
//					.failureUrl("/login?failed")
//					.loginProcessingUrl("/authentication/login/process")
					))
			.userDetailsService(jpaUserDetailsService)
			.build();
	
	
	
		
	}
	
	@Bean
	PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
		
	}

}
