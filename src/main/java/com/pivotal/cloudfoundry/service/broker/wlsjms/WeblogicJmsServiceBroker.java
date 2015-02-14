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
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import com.pivotal.cloudfoundry.service.broker.CloudFoundryPlan;
import com.pivotal.cloudfoundry.service.broker.IPlan;
import com.pivotal.cloudfoundry.service.broker.IService;
import com.pivotal.cloudfoundry.service.broker.IServiceBroker;
import com.pivotal.cloudfoundry.service.broker.ServiceBindResponseModel;
import com.pivotal.cloudfoundry.service.broker.ServiceInstanceModel;
import com.pivotal.cloudfoundry.service.broker.ServiceProvisionModel;

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
	public Map<String, String> provisionInstance(String id, String data) {
		LOG.info("Provisioning JMS service instance [id:" + id + "]");
    	
    	ServiceInstanceModel model = ServiceInstanceModel.build(data);
    	
    	//_functionTemplate.execute("provision", id);
    	
    	LOG.info("Created jms server: " + id);
    	
    	//place info about this region in admin info
    	//_provisionTemplate.put(id, model);
    	
    	return new HashMap<String, String>() {{ put("dashboard_url", "http://" + _adminHost + ":" + _adminPort + "/console"); }};
	}

	@Override
	public ResponseEntity<String> removeInstance(String id, String service_id,
			String plan_id) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ServiceBindResponseModel createBinding(String instance_id,
			String id, String data) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ResponseEntity<String> removeBinding(String instance_id, String id,
			String service_id, String plan_id) {
		// TODO Auto-generated method stub
		return null;
	}

}
