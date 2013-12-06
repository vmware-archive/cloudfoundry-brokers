package com.pivotal.cloudfoundry.service.springcloud.sqlfire;

import org.springframework.cloud.service.BaseServiceInfo;
import org.springframework.cloud.service.ServiceInfo.ServiceLabel;

@ServiceLabel("sqlfire")
public class SqlFireServiceInfo extends BaseServiceInfo {

	private String _url;
	
	public SqlFireServiceInfo(String id, String host, int port) {
		super(id);
		_url = "jdbc:sqlfire://" + host + ":" + port;		
	}
	
	@ServiceProperty(category="connection")
	public String getJdbcUrl() {
		return _url;
	}

}
