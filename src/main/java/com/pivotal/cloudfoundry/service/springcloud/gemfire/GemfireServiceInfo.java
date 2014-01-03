package com.pivotal.cloudfoundry.service.springcloud.gemfire;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.service.BaseServiceInfo;
import org.springframework.cloud.service.ServiceInfo.ServiceLabel;

@ServiceLabel("gemfire")
public class GemfireServiceInfo extends BaseServiceInfo {
	
	private static Logger LOG = LoggerFactory.getLogger(GemfireServiceInfo.class);	

	private String _host;
	private int _port;
	
	public GemfireServiceInfo(String id, String host, int port) {
		super(id);
		LOG.info("Creating Gemfire ServiceInfo: id[" + id + "] host[" + host + "] port[" + port + "]");
		_host = host;
		_port = port;
	}	
	
	@ServiceProperty(category="connection")
	public String getHost() {
		return _host;
	}
	
	@ServiceProperty(category="connection")
	public int getPort() {
		return _port;
	}

}
