package ru.raiffeisen.terminator.client;

import javax.jms.ConnectionFactory;
import javax.jms.Topic;

import org.apache.activemq.ActiveMQConnectionFactory;
import org.apache.activemq.command.ActiveMQTopic;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author Roman Zaripov
 */
@Configuration
public class TestJmsConfiguration {
	
	@Bean
	public ConnectionFactory connectionFactory() {	
		return new ActiveMQConnectionFactory("vm://localhost");
	}
	
	@Bean
	public Topic topic() {
		return new ActiveMQTopic("topic.terminator.object");
	}
}
