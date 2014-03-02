package com.pivotal.cloudfoundry.service.springcloud.gemfire;

import java.net.InetSocketAddress;
import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.service.AbstractServiceConnectorCreator;
import org.springframework.cloud.service.ServiceConnectorConfig;
import org.springframework.cloud.service.ServiceConnectorCreationException;
import org.springframework.data.gemfire.client.ClientCacheFactoryBean;
import org.springframework.util.CollectionUtils;

import com.gemstone.gemfire.cache.client.Pool;
import com.gemstone.gemfire.cache.client.PoolFactory;
import com.gemstone.gemfire.cache.client.PoolManager;
import com.gemstone.gemfire.distributed.DistributedSystem;
import com.gemstone.gemfire.distributed.internal.InternalDistributedSystem;

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
			//PoolFactory poolFactory = PoolManager.createFactory();
			//TODO: We need to update this to support multiple locators
			//poolFactory.addLocator(si.getHost(), si.getPort());
			//PoolManager.createFactory().create("myPool");
			//return poolFactory.create("gemfirePool");
			
			// eagerly initialize cache (if needed)
			if (InternalDistributedSystem.getAnyInstance() == null) {
				Properties props = new Properties();
				props.setProperty("mcast-port", "0");
				props.setProperty("locators", "");
				DistributedSystem.connect(props);
			}

			// first check the configured pools
			Pool existingPool = PoolManager.find("gemfirePool");
			if (existingPool != null) {
				return existingPool;
			} else {

				PoolFactory poolFactory = PoolManager.createFactory();
				poolFactory.addLocator(si.getHost(),si.getPort());

			 
				return poolFactory.create("gemfirePool");
			}
		} catch(Exception e) {
			e.printStackTrace();
			throw new ServiceConnectorCreationException(e);
		} 
	}
}
