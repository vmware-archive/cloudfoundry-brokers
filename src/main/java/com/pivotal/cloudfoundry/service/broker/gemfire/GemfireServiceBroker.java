package com.pivotal.cloudfoundry.service.broker.gemfire;

import java.io.File;
import java.lang.ProcessBuilder.Redirect;
import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
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

@Profile("gemfire")
@Controller()
@RequestMapping("/v2")
public class GemfireServiceBroker implements IServiceBroker {
	
	private static Logger LOG = LoggerFactory.getLogger(GemfireServiceBroker.class);
	
	@Value("${gemfire.data}") private String _gfDataDir;	
	@Value("${gemfire.home}") private String _gfHome;
	
   
	@RequestMapping(value="/catalog", method=RequestMethod.GET)
    public @ResponseBody ServiceProvisionModel serviceCatalog() {
    	LOG.info("Retrieving Gemfire service broker catalog...");
    	ServiceProvisionModel model = new ServiceProvisionModel();
    	
    	//service
    	IService service =  new GemfireService();
    	service.addTag("gemfire");
    	service.addTag("nosql");
    	
    	//TODO: somehow externalize these.. probably investigate YAML
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

    	String dataDir = _gfDataDir + File.separator + id;
    	
    	//make server dir for the process
    	File f = new File(dataDir);
    	if(f.mkdir()) {
    		LOG.info("Successfully created data dir for instance [" + f.getPath() + "]");
    	} else {
    		LOG.info("Something went wrong!!");
    	}
    	
//    	NAME
//        start server
//    SYNOPSIS
//        Start a GemFire Cache Server.
//    SYNTAX
//        gfsh start server --name=value [--assign-buckets(=value)?] [--cache-xml-file=value] [--classpath=value]
//        [--disable-default-server(=value)?] [--disable-exit-when-out-of-memory(=value)?] [--enable-time-statistics(=value)?] [--force(=value)?]
//        [--properties-file=value] [--security-properties-file=value] [--group=value] [--license-application-cache=value]
//        [--license-data-management=value] [--locators=value] [--log-level=value] [--mcast-address=value] [--mcast-port=value]
//        [--memcached-port=value] [--memcached-protocol=value] [--rebalance(=value)?] [--server-bind-address=value] [--server-port=value]
//        [--statistic-archive-file=value] [--dir=value] [--initial-heap=value] [--max-heap=value] [--J=value(,value)*]    	
    	
    	//start GF process
    	ProcessBuilder pb = new ProcessBuilder("bin/gfsh", "start", "server", "--name=" + dataDir);
    	//pb.environment().put("TEST", "VALUE");
    	pb.directory(new File(_gfHome));
    	File log = new File(_gfHome + "/cf_service.out");
    	pb.redirectErrorStream(true);
    	pb.redirectOutput(Redirect.appendTo(log));
    	
    	
    	try {
    		Process p = pb.start();
       	 	assert pb.redirectInput() == Redirect.PIPE;
       	 	assert pb.redirectOutput().file() == log;
       	 	assert p.getInputStream().read() == -1;
    		
    	} catch(Exception ex) {
    		ex.printStackTrace();
    		throw new RuntimeException("ERROR!!!");
    	}
    	
    	
    	//create region
    	
    	//add entry into main datastore for tracking
    	
    	
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
    	
    	String dataDir = _gfDataDir + File.separator + id;
    	
    	//start GF process
    	ProcessBuilder pb = new ProcessBuilder("../bin/gfsh", "stop", "server");
    	//pb.environment().put("TEST", "VALUE");
    	pb.directory(new File(dataDir));
    	File log = new File(_gfHome + "/cf_service.out");
    	pb.redirectErrorStream(true);
    	pb.redirectOutput(Redirect.appendTo(log));
    	
    	
    	try {
    		Process p = pb.start();
       	 	assert pb.redirectInput() == Redirect.PIPE;
       	 	assert pb.redirectOutput().file() == log;
       	 	assert p.getInputStream().read() == -1;
    		
    	} catch(Exception ex) {
    		ex.printStackTrace();
    		//throw new RuntimeException("ERROR!!!");
    	}
    	
    	//Eventually this will have to have logic to determine if the service instance still exists
    	if(!"testing".equals(id)) {
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
