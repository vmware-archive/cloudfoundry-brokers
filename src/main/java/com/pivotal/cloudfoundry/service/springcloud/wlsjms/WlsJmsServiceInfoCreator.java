package com.pivotal.cloudfoundry.service.springcloud.wlsjms;

import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.cloudfoundry.CloudFoundryServiceInfoCreator;

public class WlsJmsServiceInfoCreator extends
		CloudFoundryServiceInfoCreator<WlsJmsServiceInfo> {
	private static Logger LOG = LoggerFactory.getLogger(WlsJmsServiceInfoCreator.class);	
	
	public WlsJmsServiceInfoCreator() {
		super("wlsjms");
		LOG.info("Initialized WlsJmsServiceInfoCreator...");
	}
	
	@Override
	public WlsJmsServiceInfo createServiceInfo(Map<String, Object> serviceData) {
		@SuppressWarnings("unchecked")
		Map<String,Object> credentials = (Map<String, Object>) serviceData.get("credentials");
		
		LOG.info("WLSJMS ServiceInfo credentials:");
		for(Map.Entry<String, Object> e : credentials.entrySet()) {
			LOG.info("\tCredential Entry - key: " + e.getKey() + " value: " + e.getValue());
		}
		
		String id = (String) serviceData.get("name");
		String host = (String) credentials.get("host");
		Integer port = Integer.parseInt(credentials.get("port").toString());
		
		
		return new WlsJmsServiceInfo(id, host, port);
	}
}
