package com.pivotal.cloudfoundry.service.broker;

import java.util.List;
import java.util.Map;

public interface IService {
	
	public List<IPlan> getPlans();
	
	public void addPlan(IPlan plan);

	public String getId();

	public String getName();

	public String getDescription();

	public boolean isBindable();

	public List<String> getTags();
	
	public void addTag(String tag);

	public Map<String, Object> getMetadata();
	
	public void addMetadata(String key, Object value);
}
