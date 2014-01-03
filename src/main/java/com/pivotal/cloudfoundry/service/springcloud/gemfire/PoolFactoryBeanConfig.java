package com.pivotal.cloudfoundry.service.springcloud.gemfire;

import org.springframework.cloud.service.ServiceConnectorConfig;

public class PoolFactoryBeanConfig implements ServiceConnectorConfig {

	private String _host;
	private int _port;
	
	public PoolFactoryBeanConfig(String host, int port) {
		_host = host;
		_port = port;
	}
	
	public String getHost() {
		return _host;
	}
	
	public int getPort() {
		return _port;
	}

}
