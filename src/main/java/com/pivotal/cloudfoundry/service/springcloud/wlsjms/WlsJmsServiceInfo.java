package com.pivotal.cloudfoundry.service.springcloud.wlsjms;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.service.BaseServiceInfo;
import org.springframework.cloud.service.ServiceInfo.ServiceProperty;


public class WlsJmsServiceInfo extends BaseServiceInfo {
	private static Logger LOG = LoggerFactory.getLogger(WlsJmsServiceInfo.class);	

	private String _host;
	private int _port;
	
	public WlsJmsServiceInfo(String id, String host, int port) {
		super(id);
		LOG.info("Creating WLS JMS ServiceInfo: id[" + id + "] host[" + host + "] port[" + port + "]");
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
