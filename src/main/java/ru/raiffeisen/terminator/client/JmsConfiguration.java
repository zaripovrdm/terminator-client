package ru.raiffeisen.terminator.client;

import javax.jms.ConnectionFactory;
import javax.jms.JMSException;
import javax.jms.Topic;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jms.connection.CachingConnectionFactory;

import com.ibm.mq.MQException;
import com.ibm.mq.jms.MQConnectionFactory;
import com.ibm.mq.jms.MQTopic;
import com.ibm.msg.client.wmq.WMQConstants;

/**
 * @author Roman Zaripov
 */
@Configuration
public class JmsConfiguration {
	
	@Value("${object.topic.manager}") private String manager;
	@Value("${object.topic.channel}") private String channel;
	@Value("${object.topic.host}") private String host;
	@Value("${object.topic.port}") private int port;	
	@Value("${object.topic.name}") private String topicName;
	
	private static final Logger LOG = LoggerFactory.getLogger(JmsConfiguration.class);

    @Bean
    public ConnectionFactory connectionFactory() throws JMSException {
        MQConnectionFactory factory = new MQConnectionFactory();
        factory.setHostName(host);
        factory.setPort(port);
        factory.setQueueManager(manager);
        factory.setChannel(channel);
        factory.setClientReconnectOptions(WMQConstants.WMQ_CLIENT_RECONNECT);
        factory.setTransportType(WMQConstants.WMQ_CM_CLIENT);
        
        CachingConnectionFactory ccf = new CachingConnectionFactory(factory);
        ccf.setSessionCacheSize(5);
        ccf.setExceptionListener(ex -> LOG.error("JMS exception occured", ex));
        
        return ccf;
    }

    @Bean
    public Topic topic() throws JMSException, MQException {
        MQTopic topic = new MQTopic();
        topic.setBaseTopicName(topicName);
        topic.setTargetClient(WMQConstants.WMQ_CLIENT_JMS_COMPLIANT);
        return topic;
    }
}
