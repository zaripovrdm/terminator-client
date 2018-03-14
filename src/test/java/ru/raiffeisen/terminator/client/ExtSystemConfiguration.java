package ru.raiffeisen.terminator.client;

import java.util.concurrent.Executors;

import javax.jms.ConnectionFactory;
import javax.jms.Topic;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.integration.dsl.IntegrationFlow;
import org.springframework.integration.dsl.IntegrationFlows;
import org.springframework.integration.dsl.jms.Jms;
import org.springframework.jms.listener.DefaultMessageListenerContainer;

/**
 * @author Roman Zaripov
 */
@SuppressWarnings("unused")
@Configuration
public class ExtSystemConfiguration {
	
	@Autowired private ConnectionFactory connectionFactory;
	@Autowired private Topic topic;
		
	@Bean
	public IntegrationFlow extFlow() {
		return IntegrationFlows.from(Jms.messageDrivenChannelAdapter(connectionFactory).destination(topic))
			.handle(m -> System.out.println(m))
			.get();
	}
}
