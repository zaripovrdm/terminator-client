package ru.raiffeisen.terminator.client;

import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;
import org.testng.annotations.Test;

/**
 * @author Roman Zaripov
 */
@ContextConfiguration(classes = {
	CoreConfiguration.class,
	ClientFlow.class,
	TestJmsConfiguration.class,
	DataSourceConfiguration.class,
	ExtSystemConfiguration.class
})
public class ClientFlowTest extends AbstractTestNGSpringContextTests {
	
	@Test
	public void test() throws InterruptedException { // should produce no errors
		Thread.sleep(30000);
	}
}
