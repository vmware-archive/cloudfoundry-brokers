package com.pivotal.cloudfoundry.service.springcloud.gemfire;

import java.net.InetSocketAddress;
import java.util.Arrays;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.service.AbstractServiceConnectorCreator;
import org.springframework.cloud.service.ServiceConnectorConfig;
import org.springframework.cloud.service.ServiceConnectorCreationException;
import org.springframework.data.gemfire.client.PoolFactoryBean;

import com.gemstone.gemfire.cache.client.ClientCache;
import com.gemstone.gemfire.cache.client.ClientCacheFactory;
import com.gemstone.gemfire.cache.client.Pool;
import com.gemstone.gemfire.cache.client.PoolManager;
import com.gemstone.gemfire.internal.cache.PoolManagerImpl;

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
		LOG.info("Creating pool bean...");
		
		try {
			if(LOG.isDebugEnabled()) LOG.info("locator address -- " + si.getHost() + "[" + si.getPort() + "]");
//			Properties props = new Properties();
//			props.setProperty("mcast-port", "0");
//			props.setProperty("locators", si.getHost() + "[" + si.getPort() + "]");
//			//props.setProperty("locators", "");
//			DistributedSystem.connect(props);
		
			//TODO: We need to update this to support multiple locators
//			return PoolManager.createFactory().addLocator(si.getHost(), si.getPort()).create("gemfirePool");
			
//			ClientCache cc = new ClientCacheFactory()
//			   //	TODO: We need to update this to support multiple locators 
//		      .addPoolLocator(si.getHost(), si.getPort())
//		      .setPoolSubscriptionEnabled(true)
//		      .setPoolSubscriptionRedundancy(1)
//		      .set("log-level", "info")
//		      .create();
//			
//			if(LOG.isDebugEnabled()) LOG.info("Initializee Client Cache");
//			Pool p = PoolManager.find(si.getId());
//			//if(LOG.isDebugEnabled()) LOG.info("Gemfire Pool Object [" + si.getId() + "]: " + p);
//			//return (p != null) ? p : PoolManager.createFactory().addLocator(si.getHost(), si.getPort()).create(si.getId());
//			return p;
			
			PoolFactoryBean pfb = new PoolFactoryBean();
			pfb.setBeanName(si.getId());
			pfb.setLocators(Arrays.asList(new InetSocketAddress(si.getHost(), si.getPort())));
			if(LOG.isDebugEnabled()) LOG.info("Initializee Client Cache");
			pfb.afterPropertiesSet();
			if(LOG.isDebugEnabled()) LOG.info("Initializee Client Cache");
			return pfb.getObject();
			
		} catch(Exception e) {
			e.printStackTrace();
			throw new ServiceConnectorCreationException(e);
		} 
	}
}
