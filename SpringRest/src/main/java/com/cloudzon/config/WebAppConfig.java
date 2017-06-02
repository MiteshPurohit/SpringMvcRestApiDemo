package com.cloudzon.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;
import org.springframework.http.MediaType;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.validation.Validator;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;
import org.springframework.web.servlet.ViewResolver;
import org.springframework.web.servlet.config.annotation.ContentNegotiationConfigurer;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;
import org.springframework.web.servlet.view.InternalResourceViewResolver;

import com.cloudzon.security.InMemoryTokenStore;

@Configuration
@EnableWebMvc
@EnableAsync
@EnableScheduling
@ComponentScan("com.cloudzon")
public class WebAppConfig extends WebMvcConfigurerAdapter {

	@Autowired
	private ApplicationConfig config;

	@Override
	public void addResourceHandlers(ResourceHandlerRegistry registry) {
		registry.addResourceHandler("/**").addResourceLocations("/resources/")
				.setCachePeriod(31556926);
	}

	@Override
	public void configureContentNegotiation(
			ContentNegotiationConfigurer configurer) {
		configurer.favorPathExtension(true).useJaf(false)
				.ignoreAcceptHeader(false)
				.mediaType("html", MediaType.TEXT_HTML)
				.mediaType("json", MediaType.APPLICATION_JSON)
				.mediaType("xml", MediaType.APPLICATION_XML)
				.defaultContentType(MediaType.TEXT_HTML);
	}

	@Override
	public Validator getValidator() {
		LocalValidatorFactoryBean validator = new LocalValidatorFactoryBean();
		validator.setValidationMessageSource(messageSource());
		return validator;
	}

	@Bean
	public ReloadableResourceBundleMessageSource messageSource() {
		ReloadableResourceBundleMessageSource messageSource = new ReloadableResourceBundleMessageSource();
		messageSource.setBasenames("classpath:message");
		return messageSource;
	}

	@Bean
	public ViewResolver getInternalViewResolver() {
		InternalResourceViewResolver viewResolver = new InternalResourceViewResolver();
		viewResolver.setPrefix("/WEB-INF/views/");
		viewResolver.setSuffix(".jsp");
		return viewResolver;
	}

	@Bean
	public BCryptPasswordEncoder bCryptPasswordEncode() {
		return new BCryptPasswordEncoder();
	}

	@Bean
	public InMemoryTokenStore tokenStore() {
		return new InMemoryTokenStore(60);
	}

}
