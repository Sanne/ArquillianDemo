package com.acme.ejb.nointerface;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import javax.ejb.EJB;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.spec.JavaArchive;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(Arquillian.class)
public class NoInterfaceEJBTestCase {

	@Deployment
	public static JavaArchive createDeployment() {
		return ShrinkWrap.create( JavaArchive.class, "test.jar" ).addClass( NoInterfaceEJB.class );
	}

	@EJB
	NoInterfaceEJB ejb;

	@Test
	public void shouldBeAbleToResolveAndInvokeNoInterfaceEJB() throws Exception {
		assertNotNull( "Verify that the ejb was injected", ejb );
		assertEquals( "Verify that the ejb returns correct value", "pong", ejb.ping() );
	}
}
