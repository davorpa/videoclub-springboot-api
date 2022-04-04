package es.seresco.cursojee.videoclub.config;

import org.apache.commons.lang3.StringUtils;
import org.springframework.context.EnvironmentAware;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

@Configuration
@Setter
@Slf4j
public class WebMvcConfig
		implements WebMvcConfigurer, EnvironmentAware
{

	protected Environment environment;

	@Override
	public void addViewControllers(
			final ViewControllerRegistry registry)
	{
		addLoginViewController(registry);
	}

	protected void addLoginViewController(
			final ViewControllerRegistry registry)
	{
		log.info("On demand configuration of the login view controller...");
		if (!Boolean.TRUE.equals(environment.getProperty(
				"appz.security.http.form.enabled", Boolean.class, Boolean.FALSE))) {
			return;
		}
		String value;
		if (StringUtils.isNotBlank(value = environment.getProperty(
				"appz.security.http.form.login-page", "/login"))) {
			registry.addViewController(value).setViewName("login");
			log.info("Login view controller configured at: `{}`", value);
		}
	}

}
