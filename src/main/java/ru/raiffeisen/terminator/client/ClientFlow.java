package ru.raiffeisen.terminator.client;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.TimeUnit;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.integration.core.MessageSource;
import org.springframework.integration.dsl.IntegrationFlows;
import org.springframework.integration.dsl.StandardIntegrationFlow;
import org.springframework.integration.dsl.channel.MessageChannels;
import org.springframework.integration.dsl.core.Pollers;
import org.springframework.integration.dsl.jms.Jms;
import org.springframework.integration.jms.DefaultJmsHeaderMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.scheduling.support.PeriodicTrigger;
import org.springframework.transaction.PlatformTransactionManager;

import ru.raiffeisen.terminator.client.bean.ClientFilters;
import ru.raiffeisen.terminator.client.bean.ClientTransformers;
import ru.raiffeisen.terminator.client.bean.ClientWriterRcas;
import ru.raiffeisen.terminator.client.bean.ClientWriterSso;
import ru.raiffeisen.terminator.client.bean.ClientWriterWebdb;
import ru.raiffeisen.terminator.client.bean.EventTypeEnricher;
import ru.raiffeisen.terminator.common.BlockingThreadPoolExecutor;
import ru.raiffeisen.terminator.common.integration.BatchAggregator;
import ru.raiffeisen.terminator.common.integration.MapIteratorMessageSource;
import ru.raiffeisen.terminator.common.jmx.PeriodicProcessJmxConsole;

/**
 * @author Roman Zaripov
 */
@Configuration
public class ClientFlow {
	
	@Value("${terminator.client.poller.delay}") private int pollerDelay;
		
	@Autowired @Qualifier("webdbJdbcTemplate")
	private NamedParameterJdbcTemplate webdbNpjt;
	
	@Autowired @Qualifier("icdbJdbcTemplate")
	private NamedParameterJdbcTemplate icdbNpjt;
	@Value("${query.client.lastmodified.max}") private String maxLastmodifiedQuery;
	@Value("${query.clients.get}") private String getClientsQuery;
	
	@Autowired private PlatformTransactionManager transactionManager;
	
	@Autowired private EventTypeEnricher eventTypeEnricher;
	
	@Autowired private ClientWriterWebdb clientWriterWebdb;
	@Autowired private ClientWriterRcas clientWriterRcas;
	@Autowired private ClientWriterSso clientWriterSso;
	
//	@Autowired private JmsTemplate jmsTemplate;
	
	@Autowired private ExecutorService executor;
	
	@Bean(destroyMethod = "shutdownNow")
	public ExecutorService clientExecutor() {
		return new BlockingThreadPoolExecutor(4);
	}
	
	@Bean
	public PeriodicTrigger clientTrigger() {
		return new PeriodicTrigger(pollerDelay, TimeUnit.MINUTES);
	}
	
	@Bean
	public StandardIntegrationFlow clientIntegrationFlow() {		
		MessageSource<?> source = new MapIteratorMessageSource("CLIENT", webdbNpjt, maxLastmodifiedQuery, getClientsQuery, icdbNpjt);
				
		return IntegrationFlows
			.from(source, c -> c.poller(Pollers.trigger(clientTrigger()).transactional(transactionManager)))
			.split(c -> c.applySequence(false))
			.channel(MessageChannels.executor(clientExecutor()))
			.transform(ClientTransformers::transformName)
			.transform(ClientTransformers::transformPhone)
			.transform(ClientTransformers::transformPassportNumber)
			.handle(eventTypeEnricher)
			.handle(new BatchAggregator(128))
			.publishSubscribeChannel(executor, c -> c
				// WEBDB
				.subscribe(f -> f
					.handle(clientWriterWebdb)
					.split(s -> s.applySequence(false))
					.channel(MessageChannels.executor(executor))
					.filter(ClientFilters::filterTokenRollbackMessages)
					.transform(ClientTransformers::transformToBean)
					.handle(Jms.outboundAdapter(jmsTemplate).headerMapper(new DefaultJmsHeaderMapper()))
				)
				// SSO
				.subscribe(f -> f
					.split(s -> s.applySequence(false)) // PostgreSql 9.4 does not support merge
					.handle(clientWriterSso)
				) 
				// RCAS
				.subscribe(f -> f.handle(clientWriterRcas))
			)
			.get();
	}
	
	@Bean
	public PeriodicProcessJmxConsole clientConsole() {
		return new PeriodicProcessJmxConsole(clientTrigger(), clientIntegrationFlow(), "Terminator:type=Object,name=Client");
	}
}
