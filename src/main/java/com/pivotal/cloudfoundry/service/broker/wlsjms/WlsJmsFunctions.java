package com.pivotal.cloudfoundry.service.broker.wlsjms;

import java.util.Hashtable;
import java.util.Properties;

import javax.annotation.Resource;
import javax.management.MBeanServerConnection;
import javax.management.Notification;
import javax.management.NotificationListener;
import javax.management.ObjectName;
import javax.management.remote.JMXConnector;
import javax.management.remote.JMXConnectorFactory;
import javax.management.remote.JMXServiceURL;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;

import org.python.util.InteractiveInterpreter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.pivotal.cloudfoundry.service.broker.ServiceInstanceModel;

import weblogic.jms.common.JMSException;
import weblogic.jms.extensions.JMSModuleHelper;
import weblogic.management.MBeanHome;
import weblogic.management.configuration.DomainMBean;
import weblogic.management.configuration.JMSSystemResourceMBean;
import weblogic.management.configuration.ServerMBean;
import weblogic.management.mbeanservers.domainruntime.DomainRuntimeServiceMBean;
import weblogic.management.mbeanservers.edit.EditServiceMBean;
import weblogic.management.runtime.DeploymentManagerMBean;
import weblogic.management.scripting.utils.WLSTInterpreter;

public class WlsJmsFunctions {

	// Domain Runtime MBean Server connection
	MBeanServerConnection connection = null;
	// Deployment Manager JMX proxy
	DeploymentManagerMBean deploymentManager;
	DomainMBean domainMBean;

	private static Logger LOG = LoggerFactory.getLogger(WlsJmsFunctions.class);
	@Resource(name = "adminHost")
	private String _adminHost;
	@Resource(name = "adminPort")
	private String _adminPort;
	@Resource(name = "adminUsername")
	private String _adminUsername;
	@Resource(name = "adminPassword")
	private String _adminPassword;

	private InteractiveInterpreter interpreter;

	public void createJmsQueue(ServiceInstanceModel model) throws Exception {
		if (this.connection == null) {
			this.setUp();
		}
		// this.createJMSUsingJMSModuleHelper(model.getServiceId(),
		// model.getServiceId());
		this.createJMSUsingWLST(model);
	}
	
	public void removeJmsQueue(ServiceInstanceModel model) throws Exception {
		if (this.connection == null) {
			this.setUp();
		}
		// this.createJMSUsingJMSModuleHelper(model.getServiceId(),
		// model.getServiceId());
		this.removeJMSUsingWLST(model);
	}

	private void createJMSUsingWLST(ServiceInstanceModel model)
			throws Exception {
		this.connect(_adminUsername, _adminPassword, "t3://" + _adminHost + ":" + _adminPort);
		StringBuffer buf = new StringBuffer();
		buf.append(startTransaction());
		buf.append("myJmsSystemResource = '" + model.getServiceId() + "-jms'\n");
		buf.append("factoryName = '" + model.getServiceId() + "-cf'\n");
		buf.append("jmsServerName = '" + model.getServiceId() + "-jmsServer'\n");
		buf.append("queueName = '" + model.getServiceId() + "-queue'\n");
		buf.append("servermb=getMBean('Servers/myserver')\n");
		buf.append("jmsMySystemResource = create(myJmsSystemResource,'JMSSystemResource')\n");
		buf.append("jmsMySystemResource.addTarget(servermb)\n");

		// Step 4
		buf.append("theJMSResource = jmsMySystemResource.getJMSResource()\n");

		// Step 5
		buf.append("connfact1 = theJMSResource.createConnectionFactory(factoryName)\n");
		buf.append("jmsqueue1 = theJMSResource.createQueue(queueName)\n");

		// Step 6
		buf.append("connfact1.setJNDIName(factoryName)\n");
		buf.append("jmsqueue1.setJNDIName(queueName)\n");

		// Step 7
		buf.append("jmsqueue1.setSubDeploymentName('DeployToJMSServer1')\n");
		buf.append("connfact1.setSubDeploymentName('DeployToJMSServer1')\n");

		// Step 8
		buf.append("jmsserver1mb = create(jmsServerName,'JMSServer')\n");

		// Step 9
		buf.append("jmsserver1mb.addTarget(servermb)\n");

		// Step 10
		buf.append("subDep1mb = jmsMySystemResource.createSubDeployment('DeployToJMSServer1')\n");

		// Step 11
		buf.append("subDep1mb.addTarget(jmsserver1mb)\n");
		buf.append(endTransaction());
		// buf.append("print ‘Script ran successfully ...’ \n");
		interpreter.exec(buf.toString());
		
		this.disConnect(this.interpreter);
	}
	
	private void removeJMSUsingWLST(ServiceInstanceModel model) {
		this.connect(_adminUsername, _adminPassword, "t3://" + _adminHost + ":" + _adminPort);
		StringBuffer buf = new StringBuffer();
		buf.append(startTransaction());
		
		buf.append("myJmsSystemResource = '" + model.getAppGuid() + "-jms'\n");
		buf.append("factoryName = '" + model.getAppGuid() + "-cf'\n");
		buf.append("jmsServerName = '" + model.getAppGuid() + "-jmsServer'\n");
		buf.append("queueName = '" + model.getAppGuid() + "-queue'\n");
		
		buf.append("jmsMySystemResource = delete(myJmsSystemResource,'JMSSystemResource')\n");
				
		buf.append("jmsserver1mb = delete(jmsServerName,'JMSServer')\n");
		buf.append(endTransaction());
		// buf.append("print ‘Script ran successfully ...’ \n");
		interpreter.exec(buf.toString());
		
		this.disConnect(this.interpreter);
	}

	private static String startTransaction() {
		StringBuffer buf = new StringBuffer();
		buf.append("edit()\n");
		buf.append("startEdit()\n");
		return buf.toString();
	}

	private static String endTransaction() {
		StringBuffer buf = new StringBuffer();
		buf.append("save()\n");
		buf.append("activate(block='true')\n");
		return buf.toString();
	}

	private void setUp() throws Exception {
		LOG.info("*** Setting up...");

		interpreter = new WLSTInterpreter();

		this.validateConnection(_adminUsername, _adminPassword, "t3://"
				+ _adminHost + ":" + _adminPort);
		// Get connection to the Domain Runtime MBean Server.
		// For more information, see
		// "Make Remote Connections to an MBean Server"
		// in Developing Custom Management Utilities Using JMX for Oracle
		// WebLogic Server.
		// connection = getDomainRuntimeJMXConnection();

		// Get DeploymentManager JMX proxy.
		// For more information, see Oracle WebLogic Server MBean Reference.
		// EditServiceMBean svcBean = (EditServiceMBean)
		// weblogic.management.jmx.MBeanServerInvocationHandler
		// .newProxyInstance(connection, new ObjectName(
		// EditServiceMBean.OBJECT_NAME));
		// domainMBean = svcBean.getDomainConfiguration();

		// deploymentManager =
		// svcBean.getDomainRuntime().getDeploymentManager();

		// Add a JMX notification listener that outputs the JMX notifications
		// generated during deployment operations.
		// connection.addNotificationListener(new ObjectName(
		// "com.bea:Name=DeploymentManager,Type=DeploymentManager"),
		// new DeployListener(), null, null);
	}

	/*
	 * Demonstrates the notifications that are generated by WebLogic Server
	 * deployment operations.
	 */

	private class DeployListener implements NotificationListener {

		public void handleNotification(Notification notification,
				Object handback) {
			LOG.info(" Notification from DeploymentManagerMBean "
					+ "  notification type:  " + notification.getType());
			String userData = (String) notification.getUserData();
			LOG.info("  userData:  " + userData);
		}

	}

	private MBeanServerConnection getDomainRuntimeJMXConnection()
			throws Exception {

		JMXServiceURL serviceURL = new JMXServiceURL("iiop", _adminHost,
				Integer.parseInt(_adminPort), "/jndi/iiop://" + _adminHost
						+ ":" + _adminPort
						+ "/weblogic.management.mbeanservers.edit");

		Hashtable h = new Hashtable();
		h.put(Context.SECURITY_PRINCIPAL, _adminUsername);
		h.put(Context.SECURITY_CREDENTIALS, _adminPassword);
		h.put(JMXConnectorFactory.PROTOCOL_PROVIDER_PACKAGES,
				"weblogic.management.remote");
		h.put("jmx.remote.x.request.waiting.timeout", new Long(10000));

		JMXConnector connector = JMXConnectorFactory.connect(serviceURL, h);
		MBeanServerConnection connection = connector.getMBeanServerConnection();
		return connection;
	}

	public boolean validateConnection(String weblogicUser,
			String weblogicPassword, String weblogicURL) throws Exception {
		try {
			this.connect(weblogicUser, weblogicPassword, weblogicURL);
			this.disConnect(this.interpreter);
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	}

	private void createJMSUsingJMSModuleHelper(String queueName,
			String resourceName) {
		LOG.info("\n\n.... Configure JMS Resource via Service Broker ....\n\n");

		try {

			String domainMBeanName = domainMBean.getName();
			ServerMBean[] servers = domainMBean.getServers();

			String jmsServerName = "examplesJMSServer";

			//
			// create a JMSSystemResource "CapiTopic-jms"
			//

			Context domain;
			JMSModuleHelper.createQueue(domainMBean, resourceName,
					jmsServerName, queueName, "pcf." + queueName);
			JMSModuleHelper.createJMSSystemResource(domainMBean, resourceName,
					servers[0].getName());
			// JMSSystemResourceMBean jmsSR = JMSModuleHelper
			// .findJMSSystemResource(domainMBean, resourceName);
			// JMSBean jmsBean = jmsSR.getJMSResource();
			System.out.println("Created JMSSystemResource " + resourceName);

			//
			// create a JMSConnectionFactory "CConFac"
			//
			String factoryName = "myConnectionFactory";
			String jndiName = "myConnectionFactory";
			JMSModuleHelper.createConnectionFactory(domainMBean, resourceName,
					factoryName, jndiName, servers[0].getName());
			// JMSConnectionFactoryBean factory =
			// jmsBean.lookupConnectionFactory(factoryName);
			// System.out.println("Created Factory " + factory.getName());

			//
			// create a topic "CTopic"
			//
			JMSModuleHelper.createQueue(domainMBean, resourceName,
					jmsServerName, queueName, queueName);

			// TopicBean topic = jmsBean.lookupTopic(topicName);
			System.out.println("Created Queue " + queueName);
		} catch (Exception e) {
			System.out.println("Example configuration failed :"
					+ e.getMessage());
			e.printStackTrace();
		}
	}

	public void disConnect(InteractiveInterpreter interpreter) {
		StringBuffer buffer = new StringBuffer();
		buffer.append("disconnect(force='true')\n");
		interpreter.exec(buffer.toString());
	}

	public void connect(String weblogicUser, String weblogicPassword,
			String weblogicURL) {
		StringBuffer buffer = new StringBuffer();
		buffer.append("connect('" + weblogicUser + "','" + weblogicPassword
				+ "','" + weblogicURL + "')");
		this.interpreter.exec(buffer.toString());
	}

	public Context getInitialContext() throws NamingException {

		try {
			// if (System.getSecurityManager( ) == null)
			// System.setSecurityManager(new weblogic.rmi.RMISecurityManager(
			// ));
			// Get an InitialContext
			Hashtable env = new Hashtable();
			env.put(Context.SECURITY_PRINCIPAL, "admin");
			env.put(Context.SECURITY_CREDENTIALS, "newuser1");
			env.put(Context.INITIAL_CONTEXT_FACTORY,
					"weblogic.jndi.WLInitialContextFactory");
			env.put(Context.PROVIDER_URL, "t3://localhost:7001");
			return new InitialContext(env);

		} catch (NamingException ne) {
			System.out
					.println("We were unable to get a connection to the WebLogic server at "
							+ "iiop://localhost:7001");
			System.out.println("Please make sure that the server is running.");
			throw ne;
		}
	}
}
