package com.pivotal.cloudfoundry.service.broker;

import java.util.Arrays;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.context.embedded.EmbeddedServletContainerFactory;
import org.springframework.boot.context.embedded.tomcat.TomcatEmbeddedServletContainerFactory;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.web.servlet.View;
import org.springframework.web.servlet.ViewResolver;
import org.springframework.web.servlet.view.ContentNegotiatingViewResolver;
import org.springframework.web.servlet.view.json.MappingJacksonJsonView;

@EnableAutoConfiguration
@Configuration
@ComponentScan(basePackageClasses=IServiceBroker.class)
public class JavaServiceBrokerApplication {
	
	private static Logger LOG = LoggerFactory.getLogger(JavaServiceBrokerApplication.class);
	
	//For some reason this isn't working outside my IDE (e.g. from CLI starting boot app)
	//@Value(value="${broker.port}")
	//private Integer port;
	
	public static void main(String[] args) {
		 SpringApplication.run(JavaServiceBrokerApplication.class, args);
	 }
	
	@Bean
	protected ServletContextListener listener() {
		return new ServletContextListener() {			
			public void contextInitialized(ServletContextEvent sce) {
				LOG.info("ServletContext initialized");
			}			
			public void contextDestroyed(ServletContextEvent sce) {
				LOG.info("ServletContext destroyed");
			}
		};
	}
	
	@Bean
	 public ViewResolver viewResolver() {
		 ContentNegotiatingViewResolver contentNegotiatingViewResolver = new ContentNegotiatingViewResolver();
	     MappingJacksonJsonView jacksonView = new MappingJacksonJsonView();
	     jacksonView.setPrefixJson(true);	     
	     contentNegotiatingViewResolver.setDefaultViews(Arrays.asList( (View) jacksonView));
	     return contentNegotiatingViewResolver;
	 }
	 
	 @Bean
	 public EmbeddedServletContainerFactory servletContainer() {
		 //return new TomcatEmbeddedServletContainerFactory(port);
		 return new TomcatEmbeddedServletContainerFactory(8888);
	 }

}
