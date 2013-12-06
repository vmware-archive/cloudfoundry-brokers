package com.pivotal.cloudfoundry.service.broker;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public abstract class CloudFoundryService implements IService {

	private List<IPlan> _plans;
	private String _id, _name, _desc;
	private boolean _bindable = true;
	private List<String> _tags;
	private Map<String, Object> _meta;	
	
	public CloudFoundryService(String id, String name, String description) {
		_plans = new ArrayList<IPlan>();
		_meta = new HashMap<String, Object>();
		_tags = new ArrayList<String>();
		_id = id;
		_name = name;
		_desc = description;
	}
	
	public List<IPlan> getPlans() {
		return _plans;
	}
	
	public void addPlan(IPlan plan) {
		_plans.add(plan);
	}


	public String getId() {
		return _id;
	}


	public String getName() {
		return _name;
	}


	public String getDescription() {
		return _desc;
	}

	public boolean isBindable() {
		return _bindable;
	}

	public List<String> getTags() {
		return _tags;
	}
	
	public void addTag(String tag) {
		_tags.add(tag);
	}
	
	public void addTags(List<String> tags) {
		_tags.addAll(tags);
	}

	public Map<String, Object> getMetadata() {
		return _meta;
	}
	
	public void addMetadata(String key, Object value) {
		_meta.put(key, value);
	}
}
