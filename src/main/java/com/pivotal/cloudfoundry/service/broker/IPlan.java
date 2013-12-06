package com.pivotal.cloudfoundry.service.broker;

import java.util.Map;

public interface IPlan {

	public String getId();

	public String getName();
	
	public String getDescription();

	public Map<String, Object> getMetadata();
	
	public void addMetadata(String key, Object value);
}
