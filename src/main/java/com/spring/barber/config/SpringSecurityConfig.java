package com.spring.barber.config;

import com.spring.barber.auth.UserDetailsServiceImpl;
import com.spring.barber.user.Role;

import java.util.Arrays;

import com.spring.barber.auth.JwtAccessDeniedHandler;
import com.spring.barber.auth.JwtAuthenticationEntryPoint;
import com.spring.barber.auth.JwtAuthenticationFilter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

@Configuration
@EnableWebSecurity
public class SpringSecurityConfig extends WebSecurityConfigurerAdapter {

	private static final String[] AUTH_WHITELIST = {
		// -- Swagger UI v2
		"/v2/api-docs",
		"/swagger-resources",
		"/swagger-resources/**",
		"/configuration/ui",
		"/configuration/security",
		"/swagger-ui.html",
		"/webjars/**",
		// -- Swagger UI v3 (OpenAPI)
		"/v3/api-docs/**",
		"/swagger-ui/**"
	};

	@Autowired
	UserDetailsServiceImpl userDetailsService;

	@Autowired
	private JwtAuthenticationFilter jwtAuthenticationFilter;

	@Autowired
  private JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint;

	@Autowired
	private JwtAccessDeniedHandler accessDeniedHandler;

	@Bean
	public PasswordEncoder encoder() {
			return new BCryptPasswordEncoder();
	}

	@Override
	public void configure(AuthenticationManagerBuilder auth) throws Exception {
		auth.userDetailsService(userDetailsService).passwordEncoder(encoder());
	}

	@Bean
	@Override
	public AuthenticationManager authenticationManagerBean() throws Exception {
		return super.authenticationManagerBean();
	}

	@Bean
	public CorsConfigurationSource corsConfigurationSource() {
		CorsConfiguration configuration = new CorsConfiguration();
		configuration.setAllowedOrigins(Arrays.asList("*"));
		configuration.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "PATCH", "DELETE", "OPTIONS"));
		configuration.setAllowedHeaders(Arrays.asList("Authorization", "Content-Type", "X-Auth-Token"));
		configuration.setExposedHeaders(Arrays.asList("X-Auth-Token"));
		UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
		source.registerCorsConfiguration("/**", configuration);
		return source;
  }	

	@Override
	public void configure(HttpSecurity http) throws Exception {
		http.cors().and()
			.csrf().disable()
			.httpBasic().disable()
			.formLogin().disable()
		  .authorizeRequests()
			.antMatchers(AUTH_WHITELIST).permitAll()
		  .antMatchers(HttpMethod.POST, "/users", "/auth/sign-in").permitAll()
		  .antMatchers(HttpMethod.GET, "/locations", "/locations/*/timetable").permitAll()
		  .antMatchers(HttpMethod.POST, "/appointments").hasAuthority(Role.ROLE_USER.toString())
      .antMatchers(HttpMethod.GET, "/appointments", "/appointments/upcoming").hasAnyAuthority(Role.ROLE_USER.toString(), Role.ROLE_BUSINESS_USER.toString())
		  .antMatchers(HttpMethod.DELETE, "/appointments/{appointmentId}").hasAnyAuthority(Role.ROLE_USER.toString(), Role.ROLE_BUSINESS_USER.toString())
		  .antMatchers(HttpMethod.GET, "/appointments/{appointmentId}", "/locations/{locationId}/possiblehours/{date}").hasAnyAuthority(Role.ROLE_USER.toString(), Role.ROLE_BUSINESS_USER.toString())
		  .antMatchers(HttpMethod.POST, "/businesses", "/locations", "/services", "/timetables").hasAuthority(Role.ROLE_BUSINESS_USER.toString())
		  .antMatchers(HttpMethod.PATCH, "/locations/*/timetable", "/locations/{locationId}").hasAuthority(Role.ROLE_BUSINESS_USER.toString())
		  .antMatchers(HttpMethod.DELETE, "/locations/{locationId}", "/services/{serviceId}").hasAuthority(Role.ROLE_BUSINESS_USER.toString())
		  .antMatchers(HttpMethod.PUT, "/locations/{id}").hasAuthority(Role.ROLE_BUSINESS_USER.toString())
		  .antMatchers(HttpMethod.GET, "/users/{userId}/locations").hasAuthority(Role.ROLE_BUSINESS_USER.toString())
		  .antMatchers(HttpMethod.PATCH, "/appointments/{appointmentId}").hasAnyAuthority(Role.ROLE_USER.toString(), Role.ROLE_BUSINESS_USER.toString())
			.anyRequest().authenticated().and()
			.exceptionHandling().authenticationEntryPoint(jwtAuthenticationEntryPoint).and()
			.exceptionHandling().accessDeniedHandler(accessDeniedHandler).and()
			.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);

		http.addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);
	}
}