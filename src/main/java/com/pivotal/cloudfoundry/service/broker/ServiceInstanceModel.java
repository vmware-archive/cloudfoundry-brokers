package com.pivotal.cloudfoundry.service.broker;

import java.io.Serializable;

import org.codehaus.jackson.map.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ServiceInstanceModel implements Serializable {
	
	private static final long serialVersionUID = 1916067652382624221L;

	private static Logger LOG = LoggerFactory.getLogger(ServiceInstanceModel.class);
	
	private String _service_id, _plan_id, _org_id, _space_id, _app_guid;
	
	public String getServiceId() { return _service_id; }
	public void setService_id(String id) {_service_id = id; }
	
	public String getPlanId() { return _plan_id; }
	public void setPlan_id(String id) {_plan_id = id; }
	
	public String getOrganizationGuid() { return _org_id; }
	public void setOrganization_guid(String id) {_org_id = id; }
	
	public String getSpaceGuid() { return _space_id; }
	public void setSpace_guid(String id) {_space_id = id; }
	
	public String getAppGuid() { return _app_guid; }
	public void setApp_guid(String id) {_app_guid = id; }
	
	public static ServiceInstanceModel build(String json) {
		
		try {
			ServiceInstanceModel model =  new ObjectMapper().readValue(json, ServiceInstanceModel.class);
			if(LOG.isDebugEnabled()) {
	    		LOG.debug("service_id: " + model.getServiceId());
	    		LOG.debug("plan_id: " + model.getPlanId());
	    		LOG.debug("organization_guid: " + model.getOrganizationGuid());
	    		LOG.debug("space_guid: " + model.getSpaceGuid());
	    		LOG.debug("app_guid: " + model.getAppGuid());
	    	}
			return model;
		} catch(Exception ex) {
			ex.printStackTrace();
			return null;
		}
	}
}
