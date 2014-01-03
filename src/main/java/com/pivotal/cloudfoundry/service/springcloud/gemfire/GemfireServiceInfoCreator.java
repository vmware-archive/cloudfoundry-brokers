package com.pivotal.cloudfoundry.service.springcloud.gemfire;

import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.cloudfoundry.CloudFoundryServiceInfoCreator;

public class GemfireServiceInfoCreator extends CloudFoundryServiceInfoCreator<GemfireServiceInfo> {
	
	private static Logger LOG = LoggerFactory.getLogger(GemfireServiceInfoCreator.class);	
	
	public GemfireServiceInfoCreator() {
		super("gemfire");
		LOG.info("Initialized GemfireServiceInfoCreator...");
	}

	@Override
	public GemfireServiceInfo createServiceInfo(Map<String, Object> serviceData) {
		@SuppressWarnings("unchecked")
		Map<String,Object> credentials = (Map<String, Object>) serviceData.get("credentials");
		
		LOG.info("Gemfire ServiceInfo credentials:");
		for(Map.Entry<String, Object> e : credentials.entrySet()) {
			LOG.info("\tCredential Entry - key: " + e.getKey() + " value: " + e.getValue());
		}
		
		String id = (String) serviceData.get("name");
		String host = (String) credentials.get("host");
		Integer port = Integer.parseInt(credentials.get("port").toString());
		
		
		return new GemfireServiceInfo(id, host, port);
	}
}
