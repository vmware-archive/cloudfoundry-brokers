package com.pivotal.cloudfoundry.service.springcloud.gemfire;

import org.springframework.cloud.service.AbstractCloudServiceConnectorFactory;
import org.springframework.cloud.service.ServiceConnectorConfig;
import org.springframework.data.gemfire.client.PoolFactoryBean;

public class PoolFactoryBeanFactory extends	AbstractCloudServiceConnectorFactory<PoolFactoryBean> {
	public PoolFactoryBeanFactory(String serviceId, ServiceConnectorConfig serviceConnectorConfiguration) {
		super(serviceId, PoolFactoryBean.class, serviceConnectorConfiguration);
	}
}
