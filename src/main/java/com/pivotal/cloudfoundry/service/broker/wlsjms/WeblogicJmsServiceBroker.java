package com.pivotal.cloudfoundry.service.broker.wlsjms;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.ImportResource;
import org.springframework.context.annotation.Profile;
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

@Profile({"wlsjms-service-broker"})
@Controller()
@RequestMapping("/v2")
@Configuration
@ImportResource("classpath:spring-wls-client.xml")
public class WeblogicJmsServiceBroker implements IServiceBroker {
	private static Logger LOG = LoggerFactory.getLogger(WeblogicJmsServiceBroker.class);
	@Resource(name="adminHost") private String _adminHost;
	@Resource(name="adminPort") private String _adminPort;
	@Autowired private WlsJmsFunctions _functionTemplate;
	
	@RequestMapping(value="/catalog", method=RequestMethod.GET)
	public @ResponseBody ServiceProvisionModel serviceCatalog() {
		LOG.info("Retrieving WLS JMS service broker catalog...");
    	ServiceProvisionModel model = new ServiceProvisionModel();
    	
    	//service
    	IService service =  new WlsJmsService();
    	service.addTag("weblogic");
    	service.addTag("jms");
		
    	//TODO: somehow externalize these.. probably stick into Gemfire
    	//Specific Plans
    	IPlan plan = new CloudFoundryPlan("4618z98-ab16-3t22-ba6e-1f258d3addz3","single-jms-server","Development JMS server");
    	
    	//plan.addMetadata("cost", "free");
    	plan.addMetadata("costs", Arrays.asList(new HashMap<String, Object>()  {
    			{ put("amount", new HashMap<String, Object>() {{ put("usd", new Integer(0)); }}); }
    			{ put("unit", "MONTH"); }}
    		));
    	plan.addMetadata("displayName", "Oracle Weblogic JMS");
    	plan.addMetadata("bullets", Arrays.asList("Shared JMS service","Weblogic 12.1.3"));
    	service.addPlan(plan);
    	
    	model.addService(service);
    	return model;
	}

	@ResponseBody
    @ResponseStatus(HttpStatus.CREATED)
    @RequestMapping(value="/service_instances/{id}", method=RequestMethod.PUT)
	public Map<String, String> provisionInstance(@PathVariable String id, @RequestBody String data) {
		if (id==null) id="dummy";
		LOG.info("Provisioning JMS service instance [id:" + id + "]");
    	LOG.debug(data);
    	
    	ServiceInstanceModel model = ServiceInstanceModel.build(data);
    	
    	try {
			_functionTemplate.createJmsQueue(model);
			LOG.info("Created jms server: " + id);
		} catch (Exception e) {
			LOG.error("error creating weblogic queue",e);
		}
    	
    	//place info about this region in admin info
    	//_provisionTemplate.put(id, model);
    	
    	return new HashMap<String, String>() {{ put("dashboard_url", "http://" + _adminHost + ":" + _adminPort + "/console"); }};
	}

	@ResponseBody
    @RequestMapping(value="/service_instances/{id}", method=RequestMethod.DELETE)
    public ResponseEntity<String> removeInstance(@PathVariable String id, @RequestParam String service_id, @RequestParam String plan_id) {
		ServiceInstanceModel model = new ServiceInstanceModel();
		model.setApp_guid(id);
		model.setService_id(service_id);
		model.setPlan_id(plan_id);
    	
    	try {
			_functionTemplate.removeJmsQueue(model);
			LOG.info("Removed jms server: " + id);
		} catch (Exception e) {
			LOG.error("error removing weblogic queue",e);
		}
    	return new ResponseEntity<String>(EMPTY_JSON, HttpStatus.OK);
	}

	@ResponseBody
    @RequestMapping(value="/service_instances/{instance_id}/service_bindings/{id}", method=RequestMethod.PUT)
    public ServiceBindResponseModel createBinding(@PathVariable String instance_id, @PathVariable String id,  @RequestBody String data) {
    	LOG.info("Creating Gemfire service binding [instance_id: " + instance_id + ", id:" + id + "]");
    	
    	ServiceInstanceModel model = ServiceInstanceModel.build(data);
    	
    	ServiceBindResponseModel resp = new ServiceBindResponseModel();
    	
    	resp.addCredential("wls-t3", "t3://"+_adminHost+":"+_adminPort);
    	resp.addCredential("connectionFactory", id+"-cf");
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
    		//return new ResponseEntity<String>(EMPTY_JSON, HttpStatus.NOT_FOUND);
    		//For now this is OK... need for testing
    		return new ResponseEntity<String>(EMPTY_JSON, HttpStatus.OK);
    	}
    }

}
