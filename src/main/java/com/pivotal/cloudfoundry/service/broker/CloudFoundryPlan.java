package com.pivotal.cloudfoundry.service.broker;

import java.util.HashMap;
import java.util.Map;

public class CloudFoundryPlan implements IPlan {
	
	private String _id, _name, _desc;
	private Map<String, Object> _meta;
	
	public CloudFoundryPlan(String id, String name, String description) {
		_meta = new HashMap<String, Object>();
		_id = id;
		_name = name;
		_desc = description;
	}
	
	public String getId() {
		return _id;
	}

	public String getName() {
		return _name;
	}
	
	public void set_name(String _name) {
		this._name = _name;
	}
	
	public String getDescription() {
		return _desc;
	}

	public Map<String, Object> getMetadata() {
		return _meta;
	}
	
	public void addMetadata(String key, Object value) {
		_meta.put(key, value);
	}
}
