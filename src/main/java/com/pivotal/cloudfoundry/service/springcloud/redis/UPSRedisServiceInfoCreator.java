package com.pivotal.cloudfoundry.service.springcloud.redis;

import java.util.Map;

import org.springframework.cloud.cloudfoundry.CloudFoundryServiceInfoCreator;
import org.springframework.cloud.service.BaseServiceInfo;

public class UPSRedisServiceInfoCreator extends CloudFoundryServiceInfoCreator<UPSRedisServiceInfoCreator.UPSRedisServiceInfo> {

	public static final String VALUE = "redis";
	public static final String TAG = "tag";
	
	public UPSRedisServiceInfoCreator() {
		super("redis");
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public boolean accept(Map<String,Object> serviceData) {
		System.out.println("service info creator accept...");
		System.out.println("serviceData: " + serviceData);
        Map<String,Object> serviceDataMap = (Map<String,Object>) serviceData;
        Map<String,Object> credentials = (Map<String, Object>) serviceDataMap.get("credentials");
        
        if(credentials.containsKey(TAG) && VALUE.startsWith((String) credentials.get(TAG))) {
        	System.out.println("Accepting Service");
        	return true;
        } else {
        	System.out.println("NOT accepting Service");
        	return false;
        }
	}
	
	public UPSRedisServiceInfo createServiceInfo(Map<String,Object> serviceData) {
		@SuppressWarnings("unchecked")
		Map<String,Object> credentials = (Map<String, Object>) serviceData.get("credentials");
		
		String id = (String) serviceData.get("name");
		
		String host = (String) credentials.get("host");
		Integer port = Integer.parseInt(credentials.get("port").toString());
		String password = (String) credentials.get("password");

		return new UPSRedisServiceInfo(id, host, port, password);
	}
	
	public class UPSRedisServiceInfo extends BaseServiceInfo {

		private String _pwd, _host;
		private int _port;
		
		public UPSRedisServiceInfo(String id, String host, int port, String password) {
				super(id);
				_host = host;
				_port = port;
				_pwd = password;
		}
		
		@ServiceProperty(category="connection")
		public String getPassword() {
			return _pwd;
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
}
