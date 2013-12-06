package com.pivotal.cloudfoundry.service.broker;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;

@EnableAutoConfiguration
@ComponentScan(basePackageClasses=IServiceBroker.class)
public class JavaServiceBrokerApplication {
	
	private static Logger LOG = LoggerFactory.getLogger(JavaServiceBrokerApplication.class);
	
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

}
