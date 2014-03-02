package com.pivotal.cloudfoundry.service.springcloud.gemfire;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.service.AbstractServiceConnectorCreator;
import org.springframework.cloud.service.ServiceConnectorConfig;
import org.springframework.cloud.service.ServiceConnectorCreationException;

import com.gemstone.gemfire.cache.client.Pool;
import com.gemstone.gemfire.cache.client.PoolFactory;
import com.gemstone.gemfire.cache.client.PoolManager;

public class PoolCreator extends	AbstractServiceConnectorCreator<Pool, GemfireServiceInfo> {
	
	private static Logger LOG = LoggerFactory.getLogger(PoolCreator.class);	
	
	public PoolCreator() {
		super();
		LOG.info("Initialized PoolCreator...");
		LOG.info("Connector Type: " + this.getServiceConnectorType().getName());
		LOG.info("Service Info Type: " + this.getServiceInfoType().getName());
	}

	@Override
	public Pool create(GemfireServiceInfo si, ServiceConnectorConfig scc) {
		LOG.info("Creating pool factory bean...");
		
		try {
			if(LOG.isDebugEnabled()) LOG.info("locator address -- " + si.getHost() + "[" + si.getPort() + "]");
				PoolFactory poolFactory = PoolManager.createFactory();
				//TODO: We need to update this to support multiple locators
				poolFactory.addLocator(si.getHost(), si.getPort());
				PoolManager.createFactory().create("myPool");
				return poolFactory.create("gemfirePool");
		} catch(Exception e) {
			e.printStackTrace();
			throw new ServiceConnectorCreationException(e);
		} 
	}
}
