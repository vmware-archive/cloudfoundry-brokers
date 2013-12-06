package com.pivotal.cloudfoundry.service.springcloud.sqlfire;

import javax.sql.DataSource;

import org.springframework.beans.BeanWrapper;
import org.springframework.beans.BeanWrapperImpl;
import org.springframework.cloud.service.AbstractServiceConnectorCreator;
import org.springframework.cloud.service.ServiceConnectorConfig;
import org.springframework.cloud.service.ServiceConnectorCreationException;
import org.springframework.cloud.service.relational.DataSourceConfig;
import org.springframework.cloud.service.relational.DataSourceConfigurer;

public class SqlFireDataSourceCreator extends AbstractServiceConnectorCreator<DataSource, SqlFireServiceInfo> {
	
	private static final String SQLFIRE_DRIVER_CLASS_NAME = "com.vmware.sqlfire.jdbc.ClientDriver";
	private static final String VALIDATION_QUERY = "SELECT 1";
	private DataSourceConfigurer _configurer = new DataSourceConfigurer();
	
	@Override
	public DataSource create(SqlFireServiceInfo serviceInfo, ServiceConnectorConfig serviceConnectorConfig) {
		try {
			Class.forName(SQLFIRE_DRIVER_CLASS_NAME);
			
			org.apache.commons.dbcp.BasicDataSource ds = new org.apache.commons.dbcp.BasicDataSource();
			setBasicDataSourceProperties(ds, serviceInfo, serviceConnectorConfig, SQLFIRE_DRIVER_CLASS_NAME, VALIDATION_QUERY);
			return ds;
		} catch (Exception e) {
			throw new ServiceConnectorCreationException("Failed to created cloud datasource for " + serviceInfo.getId() + " service", e);
		}
	}

	protected void setBasicDataSourceProperties(DataSource basicDataSource, SqlFireServiceInfo serviceInfo,
			ServiceConnectorConfig serviceConnectorConfig,  String driverClassName, String validationQuery) {
		BeanWrapper target = new BeanWrapperImpl(basicDataSource);
		target.setPropertyValue("driverClassName", driverClassName);
		target.setPropertyValue("url", serviceInfo.getJdbcUrl());
		
		_configurer.configure(basicDataSource, (DataSourceConfig)serviceConnectorConfig);
	}

}
