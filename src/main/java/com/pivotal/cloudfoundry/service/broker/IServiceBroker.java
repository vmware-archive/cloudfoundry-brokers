package com.pivotal.cloudfoundry.service.broker;

import java.util.Map;

import org.springframework.http.ResponseEntity;


public interface IServiceBroker {
	
	public static final String EMPTY_JSON = "{}";
   
    public ServiceProvisionModel serviceCatalog();
    
    public Map<String,String> provisionInstance(String id, String data);
    
    public ResponseEntity<String> removeInstance(String id, String service_id, String plan_id);
    
    public ServiceBindResponseModel createBinding(String instance_id, String id, String data);
    
    public ResponseEntity<String> removeBinding(String instance_id, String id, String service_id, String plan_id);
}
