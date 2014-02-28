package com.pivotal.cloudfoundry.service.springcloud.gemfire;

import org.springframework.cloud.service.AbstractCloudServiceConnectorFactory;
import org.springframework.cloud.service.ServiceConnectorConfig;

import com.gemstone.gemfire.cache.client.Pool;

public class PoolFactory extends	AbstractCloudServiceConnectorFactory<Pool> {
	public PoolFactory(String serviceId, ServiceConnectorConfig serviceConnectorConfiguration) {
		super(serviceId, Pool.class, serviceConnectorConfiguration);
	}
}
