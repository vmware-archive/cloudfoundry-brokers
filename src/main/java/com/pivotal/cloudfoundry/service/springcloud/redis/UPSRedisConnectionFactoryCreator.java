package com.pivotal.cloudfoundry.service.springcloud.redis;

import static org.springframework.cloud.service.Util.hasClass;

import org.springframework.cloud.service.AbstractServiceConnectorCreator;
import org.springframework.cloud.service.PooledServiceConnectorConfig;
import org.springframework.cloud.service.ServiceConnectorConfig;
import org.springframework.cloud.service.ServiceConnectorCreationException;
import org.springframework.cloud.service.keyval.RedisConnectionFactoryConfigurer;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;

import com.pivotal.cloudfoundry.service.springcloud.redis.UPSRedisServiceInfoCreator.UPSRedisServiceInfo;

public class UPSRedisConnectionFactoryCreator extends AbstractServiceConnectorCreator<RedisConnectionFactory, UPSRedisServiceInfo> {

	private static final String REDIS_CLIENT_CLASS_NAME = "redis.clients.jedis.Jedis";

	RedisConnectionFactoryConfigurer configurer = new RedisConnectionFactoryConfigurer();

	@Override
	public RedisConnectionFactory create(UPSRedisServiceInfo serviceInfo, ServiceConnectorConfig serviceConnectorConfig) {
		if (hasClass(REDIS_CLIENT_CLASS_NAME)) {
			JedisConnectionFactory connectionFactory = new JedisConnectionFactory();
			connectionFactory.setHostName(serviceInfo.getHost());
			connectionFactory.setPort(serviceInfo.getPort());
			connectionFactory.setPassword(serviceInfo.getPassword());
			configurer.configure(connectionFactory, (PooledServiceConnectorConfig) serviceConnectorConfig);
			connectionFactory.afterPropertiesSet();
			return connectionFactory;
		} else {
			throw new ServiceConnectorCreationException("Failed to created cloud Redis connection factory for "
					+ serviceInfo.getId() + " service.  Jedis client implementation class ("
					+ REDIS_CLIENT_CLASS_NAME + ") not found");
		}
	}
}
