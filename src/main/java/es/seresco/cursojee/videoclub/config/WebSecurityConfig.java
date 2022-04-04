package es.seresco.cursojee.videoclub.config;

import org.apache.commons.lang3.StringUtils;
import org.springframework.context.EnvironmentAware;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.annotation.web.configurers.ExceptionHandlingConfigurer;
import org.springframework.security.config.annotation.web.configurers.FormLoginConfigurer;
import org.springframework.security.config.annotation.web.configurers.HttpBasicConfigurer;
import org.springframework.security.config.annotation.web.configurers.LogoutConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

@Configuration
@EnableWebSecurity
@Setter
@Slf4j
public class WebSecurityConfig
		extends WebSecurityConfigurerAdapter
		implements EnvironmentAware
{

	private Environment environment;



	@Override
	protected void configure(AuthenticationManagerBuilder auth) throws Exception {
		log.info("On demand configuration of InMemory Authentication...");

		String password = environment.getProperty("appz.security.authentication.in-memory.password");
		if (StringUtils.isBlank(password)) { // fallback to encoded
			password = passwordEncoder().encode("01234");
		}

		auth
		// enable in memory based authentication with a user named "user" and "admin"
		.inMemoryAuthentication()
			.withUser("user").password(password).roles("USER")
			.and()
			.withUser("admin").password(password).roles("USER", "ADMIN");
	}

	// Expose the UserDetailsService as a Bean
	@Bean
	@Override
	public UserDetailsService userDetailsServiceBean() throws Exception {
		return super.userDetailsServiceBean();
	}

	// Expose the PasswordEncoder as a Bean
	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}




	@Override
	protected void configure(
			final HttpSecurity http)
					throws Exception
	{
		// avoid create sessions
		log.info("Configuring as stateless application...");
		http
			.sessionManagement()
				.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
			;

		log.info("On demand configuration of ORS and CSRF protections...");
		// add CORS y CSRF protections
		http
				.cors()
			.and()
			// CSRF disabled
				.csrf()
				.disable()
			;

		log.info("On demand configuration of request authorizations...");
		http
			.authorizeRequests()
			.antMatchers("/api/privado/**").authenticated()
			.anyRequest().permitAll()
			;

		// add support for REALM authentication
		configureHttpBasic(http);
		// add support for authentication usign a sign-up form
		configureHttpFormLogin(http);
		configureHttpLogout(http);
		// and configure exception handling
		configureExceptionHandling(http);
	}

	protected void configureHttpBasic(
			final HttpSecurity http)
					throws Exception
	{
		log.info("On demand configuration of the HTTP security basic mode...");
		if (!Boolean.TRUE.equals(environment.getProperty(
				"appz.security.http.basic.enabled", Boolean.class, Boolean.FALSE))) {
			http.httpBasic().disable();
			return;
		}
		HttpBasicConfigurer<HttpSecurity> configurer = http.httpBasic();
		String value;
		if (StringUtils.isNotBlank(value = environment.getProperty(
				"appz.security.http.basic.realm"))) {
			configurer.realmName(value);
			log.debug("HTTP security basic mode customized with REALM: {}", value);
		}
	}

	protected void configureHttpFormLogin(
			final HttpSecurity http)
					throws Exception
	{
		log.info("On demand configuration of the HTTP security form mode...");
		if (!Boolean.TRUE.equals(environment.getProperty(
				"appz.security.http.form.enabled", Boolean.class, Boolean.FALSE))) {
			http.formLogin().disable();
			return;
		}
		final FormLoginConfigurer<HttpSecurity> configurer = http.formLogin();

		String value;
		if (StringUtils.isNotBlank(value = environment.getProperty(
				"appz.security.http.form.login-page"))) {
			configurer.loginPage(value);
			log.debug("HTTP security form mode customized to render login page at: `{}`", value);
		}
		if (StringUtils.isNotBlank(value = environment.getProperty(
				"appz.security.http.form.login-processing-url"))) {
			configurer.loginProcessingUrl(value);
			log.debug("HTTP security form mode customized to process requests at: `{}`", value);
		}
		if (StringUtils.isNotBlank(value = environment.getProperty(
				"appz.security.http.form.authentication-success-url"))) {
			configurer.defaultSuccessUrl(value);
			log.debug("HTTP security form mode customized to redirect after success login to: `{}`", value);
		}
		if (StringUtils.isNotBlank(value = environment.getProperty(
				"appz.security.http.form.authentication-failure-url"))) {
			configurer.failureUrl(value);
			log.debug("HTTP security form mode customized to redirect after failure login to: `{}`", value);
		}
		if (StringUtils.isNotBlank(value = environment.getProperty(
				"appz.security.http.form.username-parameter"))) {
			configurer.usernameParameter(value);
			log.debug("HTTP security form mode customized to get credential UID from form field: `{}`", value);
		}
		if (StringUtils.isNotBlank(value = environment.getProperty(
				"appz.security.http.form.password-parameter"))) {
			configurer.passwordParameter(value);
			log.debug("HTTP security form mode customized to get password from form field: `{}`", value);
		}
		if (StringUtils.isNotBlank(value = environment.getProperty(
				"appz.security.http.form.success-forward-url"))) {
			configurer.successForwardUrl(value);
		}
		if (StringUtils.isNotBlank(value = environment.getProperty(
				"appz.security.http.form.failure-forward-url"))) {
			configurer.failureForwardUrl(value);
		}

		configurer.permitAll();
	}

	protected void configureHttpLogout(HttpSecurity http)
			throws Exception
	{
		log.info("On demand configuration of the HTTP security logout...");
		if (!Boolean.TRUE.equals(environment.getProperty(
				"appz.security.http.logout.enabled", Boolean.class, Boolean.FALSE))) {
			http.logout().disable();
			return;
		}
		LogoutConfigurer<HttpSecurity> configurer = http.logout();

		String value;
		if (StringUtils.isNotBlank(value = environment.getProperty(
				"appz.security.http.logout.processing-url"))) {
			configurer.logoutUrl(value);
			log.debug("HTTP security logouts attends to url: `{}`", value);
		}
		if (StringUtils.isNotBlank(value = environment.getProperty(
				"appz.security.http.logout.success-url"))) {
			configurer.logoutSuccessUrl(value);
			log.debug("HTTP security successfully logouts redirects to url: `{}`", value);
		}

		configurer.permitAll();
	}

	protected void configureExceptionHandling(final HttpSecurity http) throws Exception {
		log.info("On demand configuration of the security exception handling...");
		if (!Boolean.TRUE.equals(environment.getProperty(
				"appz.security.http.exception-handling.enabled", Boolean.class, Boolean.FALSE))) {
			http.exceptionHandling().disable();
			return;
		}
		ExceptionHandlingConfigurer<HttpSecurity> configurer = http.exceptionHandling();

		String value;
		if (StringUtils.isNotBlank(value = environment.getProperty(
				"appz.security.http.exception-handling.access-denied-page"))) {
			configurer.accessDeniedPage(value);
			log.debug("HTTP security access-denied-page points to: `{}`", value);
		}
	}

}
