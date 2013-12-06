package com.pivotal.cloudfoundry.service.broker;

import java.util.ArrayList;
import java.util.List;

public class ServiceProvisionModel {
	
	private List<IService> _services;
	
	public ServiceProvisionModel() {
		_services = new ArrayList<IService>();
	}
	
	public List<IService> getServices() {
		return _services;
	}
	
	public void addService(IService service) {
		_services.add(service);
	}		
}
