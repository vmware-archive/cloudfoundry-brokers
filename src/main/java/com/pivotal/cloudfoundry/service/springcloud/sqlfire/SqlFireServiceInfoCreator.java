package com.pivotal.cloudfoundry.service.springcloud.sqlfire;

import java.util.Map;

import org.springframework.cloud.cloudfoundry.CloudFoundryServiceInfoCreator;

public class SqlFireServiceInfoCreator extends CloudFoundryServiceInfoCreator<SqlFireServiceInfo> {
	
	public static final String VALUE = "sqlfire";
	public static final String TAG = "tag";
	
	public SqlFireServiceInfoCreator() {
		super("sqlfire");
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public boolean accept(Map<String,Object> serviceData) {
        Map<String,Object> serviceDataMap = (Map<String,Object>) serviceData;
        Map<String,Object> credentials = (Map<String, Object>) serviceDataMap.get("credentials");
        
        //we match the prefix so that anything like sqlfire-dev or sqlfire will work
        if(credentials.containsKey(TAG) && VALUE.startsWith((String) credentials.get(TAG))) {
        	return true;
        } else {
        	return false;
        }
	}

	public SqlFireServiceInfo createServiceInfo(Map<String,Object> serviceData) {
		@SuppressWarnings("unchecked")
		Map<String,Object> credentials = (Map<String, Object>) serviceData.get("credentials");
		
		String id = (String) serviceData.get("name");
		String host = (String) credentials.get("host");
		int port = Integer.parseInt(credentials.get("port").toString());
		return new SqlFireServiceInfo(id, host, port);
	}


}
