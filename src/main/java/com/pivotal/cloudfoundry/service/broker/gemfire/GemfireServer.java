package com.pivotal.cloudfoundry.service.broker.gemfire;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.ImportResource;
import org.springframework.context.annotation.Profile;
import org.springframework.data.gemfire.function.config.EnableGemfireFunctions;

@EnableAutoConfiguration
@Configuration
@Profile("gemfire-server")
@ImportResource("classpath:spring-gemfire-server.xml")
@EnableGemfireFunctions
public class GemfireServer {
	
	private static Logger LOG = LoggerFactory.getLogger(GemfireServer.class);	
	
	public static void main(String[] args) throws Exception {		
		//start up container
		AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(GemfireServer.class);
		LOG.info("****************************");
		LOG.info("** Gemfire Server started **");
		LOG.info("****************************");
    }
}
