package com.pivotal.cloudfoundry.service.springcloud.gemfire;

import java.net.InetSocketAddress;
import java.util.Arrays;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.service.AbstractServiceConnectorCreator;
import org.springframework.cloud.service.ServiceConnectorConfig;
import org.springframework.cloud.service.ServiceConnectorCreationException;
import org.springframework.data.gemfire.client.PoolFactoryBean;

public class PoolFactoryBeanCreator extends	AbstractServiceConnectorCreator<PoolFactoryBean, GemfireServiceInfo> {
	
	private static Logger LOG = LoggerFactory.getLogger(PoolFactoryBeanCreator.class);	
	
	public PoolFactoryBeanCreator() {
		super();
		LOG.info("Initialized PoolFactoryBeanCreator...");
		LOG.info("Connector Type: " + this.getServiceConnectorType().getName());
		LOG.info("Service Info Type: " + this.getServiceInfoType().getName());
	}

	@Override
	public PoolFactoryBean create(GemfireServiceInfo si, ServiceConnectorConfig scc) {
		LOG.info("Creating pool factory bean...");
		
		try {
			PoolFactoryBean bean = new PoolFactoryBean();
			if(LOG.isDebugEnabled()) LOG.info("locator address -- " + si.getHost() + "[" + si.getPort() + "]");
			bean.setLocators(Arrays.asList(new InetSocketAddress(si.getHost(), si.getPort())));
			
			//TODO: if the spring config has a named pool this won't work... how to figure out pool name if set in XML???
			// Possibly this is supposed to come through in the ServiceConnectorConfig but it isn't working???
			bean.setName("gemfirePool");
			
			bean.afterPropertiesSet();
			return bean;
			
		} catch(Exception e) {
			e.printStackTrace();
			throw new ServiceConnectorCreationException(e);
		} 
	}
}
