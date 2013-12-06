package com.pivotal.cloudfoundry.service.broker;

import java.util.HashMap;
import java.util.Map;

public class ServiceBindResponseModel {

	private Map<String, Object> _creds;
	
	public ServiceBindResponseModel() {
		_creds = new HashMap<String, Object>();
	}
	
	public Map<String, Object> getCredentials() {
		return _creds;
	}
	
	public void addCredential(String key, String value) {
		_creds.put(key, value);
	}
	
}
