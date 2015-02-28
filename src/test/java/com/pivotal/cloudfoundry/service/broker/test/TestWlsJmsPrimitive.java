package com.pivotal.cloudfoundry.service.broker.test;

import static org.junit.Assert.*;

import javax.naming.Context;
import javax.naming.NamingException;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.pivotal.cloudfoundry.service.broker.ServiceInstanceModel;
import com.pivotal.cloudfoundry.service.broker.wlsjms.WlsJmsFunctions;

@ComponentScan("com.pivotal.cloudfoundry.service.broker")
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath:spring-wls-client.xml")
public class TestWlsJmsPrimitive {
	@Autowired
	protected WlsJmsFunctions weblogicJmsFunctions;
	
	@SuppressWarnings("SpringJavaAutowiringInspection")
	@Autowired
	protected ApplicationContext ac;

	@Before
	public void setUp() throws Exception {
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test(timeout=30000)
	public void testCreateWlsJms() {
		ServiceInstanceModel data = new ServiceInstanceModel();
		data.setApp_guid("12345");
		data.setPlan_id("abcdefg");
		data.setService_id("jklmnop");
	
		try {
			weblogicJmsFunctions.createJmsQueue(data);
		} catch (Exception e) {
			e.printStackTrace();
			fail("Error during test");
		}
		assertTrue(true);
	}
	
	@Test(timeout=30000)
	public void testRemoveWlsJms() {
		ServiceInstanceModel data = new ServiceInstanceModel();
		data.setApp_guid("12345");
		data.setPlan_id("abcdefg");
		data.setService_id("jklmnop");
	
		try {
			weblogicJmsFunctions.removeJmsQueue(data);
		} catch (Exception e) {
			e.printStackTrace();
			fail("Error during test");
		}
		assertTrue(true);
	}
	
	@Test(timeout=30000)
	public void testJNDIContext() {
		try {
			Context ctxt = weblogicJmsFunctions.getInitialContext();
		} catch (NamingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			fail("Error during jndi context test");
		}
		assertTrue(true);
	}

}
