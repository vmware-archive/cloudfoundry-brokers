package com.pivotal.cloudfoundry.service.broker.gemfire;

import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.ImportResource;
import org.springframework.context.annotation.Profile;
import org.springframework.data.gemfire.GemfireTemplate;
import org.springframework.data.gemfire.function.execution.GemfireOnServersFunctionTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import com.pivotal.cloudfoundry.service.broker.CloudFoundryPlan;
import com.pivotal.cloudfoundry.service.broker.IPlan;
import com.pivotal.cloudfoundry.service.broker.IService;
import com.pivotal.cloudfoundry.service.broker.IServiceBroker;
import com.pivotal.cloudfoundry.service.broker.ServiceBindResponseModel;
import com.pivotal.cloudfoundry.service.broker.ServiceInstanceModel;
import com.pivotal.cloudfoundry.service.broker.ServiceProvisionModel;

@Profile({"gemfire-service-broker"})
@Controller()
@RequestMapping("/v2")
@Configuration
@ImportResource("classpath:spring-gemfire-client.xml")
public class GemfireServiceBroker implements IServiceBroker {
	
	private static Logger LOG = LoggerFactory.getLogger(GemfireServiceBroker.class);	
	
	@Autowired private GemfireOnServersFunctionTemplate _functionTemplate;
	@Autowired private GemfireTemplate _template;
   
	@RequestMapping(value="/catalog", method=RequestMethod.GET)
    public @ResponseBody ServiceProvisionModel serviceCatalog() {
    	LOG.info("Retrieving Gemfire service broker catalog...");
    	ServiceProvisionModel model = new ServiceProvisionModel();
    	
    	//service
    	IService service =  new GemfireService();
    	service.addTag("gemfire");
    	service.addTag("nosql");
    	
    	//TODO: somehow externalize these.. probably stick into Gemfire
    	//Specific Plans
    	IPlan plan = new CloudFoundryPlan("4618z98-ab16-3t22-ba6e-1f258d3addz2","1GB-replicated","Multi-tenant Gemfire service; 1GB data storage replicated");
    	plan.addMetadata("cost", "free");
    	service.addPlan(plan);
    	
    	model.addService(service);
    	return model;
    }
    
	@ResponseBody
    @ResponseStatus(HttpStatus.CREATED)
    @RequestMapping(value="/service_instances/{id}", method=RequestMethod.PUT)
    public Map<String,String> provisionInstance(@PathVariable String id, @RequestBody String data) {
    	LOG.info("Provisioning Gemfire service instance [id:" + id + "]");
    	
    	ServiceInstanceModel model = ServiceInstanceModel.build(data);
    	
    	_functionTemplate.execute("provision", id);
    	LOG.info("Created gemfire region: " + id);
    	
    	//place info about this region in admin info
    	_template.put(id, model);
    	
    	return new HashMap<String, String>() {{ put("dashboard_url", "http://localhost:8080/pulse"); }};
    }
    
	@ResponseBody
    @RequestMapping(value="/service_instances/{id}", method=RequestMethod.DELETE)
    public ResponseEntity<String> removeInstance(@PathVariable String id, @RequestParam String service_id, @RequestParam String plan_id) {
    	LOG.info("Deleting Gemfire service instance [id:" + id + "]");
    	if(LOG.isDebugEnabled()) {
    		LOG.debug("service_id: " + service_id);
    		LOG.debug("plan_id: " + plan_id);
    	}
    	
    	if(_template.containsKeyOnServer(id)) {
    		_functionTemplate.execute("deprovision", id);
        	LOG.info("Destroyed gemfire region: " + id);
        	
        	_template.remove(id);
        	
        	return new ResponseEntity<String>(EMPTY_JSON, HttpStatus.OK);
    	} else {
    		return new ResponseEntity<String>(EMPTY_JSON, HttpStatus.NOT_FOUND);
    	}
    }
    
	@ResponseBody
    @RequestMapping(value="/service_instances/{instance_id}/service_bindings/{id}", method=RequestMethod.PUT)
    public ServiceBindResponseModel createBinding(@PathVariable String instance_id, @PathVariable String id,  @RequestBody String data) {
    	LOG.info("Creating Gemfire service binding [instance_id: " + instance_id + ", id:" + id + "]");
    	
    	ServiceInstanceModel model = ServiceInstanceModel.build(data);
    	
    	//TODO: What do we actually do to bind??  Anything??
    	
    	//Add any 'credentials' needed to access gemfire
    	ServiceBindResponseModel resp = new ServiceBindResponseModel();
    	resp.addCredential("uri", "http://testing.com");
    	return resp;
    }
   
	@ResponseBody
    @RequestMapping(value="/service_instances/{instance_id}/service_bindings/{id}", method=RequestMethod.DELETE)
    public ResponseEntity<String> removeBinding(@PathVariable String instance_id, @PathVariable String id,  
    		@RequestParam String service_id, @RequestParam String plan_id) {
    	LOG.info("Deleting Gemfire service binding [instance_id: " + instance_id + ", id:" + id + "]");
    	if(LOG.isDebugEnabled()) {
    		LOG.debug("service_id: " + service_id);
    		LOG.debug("plan_id: " + plan_id);
    	}

    	
    	//TODO: What does unbinding actually mean??
    	
    	//Eventually this will have to have logic to determine if the service binding still exists
    	if(!"testing".equals(id)) {
    		return new ResponseEntity<String>(EMPTY_JSON, HttpStatus.OK);
    	} else {
    		return new ResponseEntity<String>(EMPTY_JSON, HttpStatus.NOT_FOUND);
    	}
    }
}
