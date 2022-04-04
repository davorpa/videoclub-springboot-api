package es.seresco.cursojee.videoclub.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.method.configuration.GlobalMethodSecurityConfiguration;

@Configuration
@EnableGlobalMethodSecurity(
		// enable handling for @PreAuthorize/@PostAuthorize annotations
		prePostEnabled = true,
		// enable handling for @Secured annotations
		securedEnabled = true,
		// enable handling for @RoleAllowed annotations
		jsr250Enabled = true)
public class MethodSecurityConfig extends GlobalMethodSecurityConfiguration {

}
