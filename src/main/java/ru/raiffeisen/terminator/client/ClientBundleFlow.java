package ru.raiffeisen.terminator.client;

import java.util.concurrent.TimeUnit;

import javax.sql.DataSource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.integration.core.MessageSource;
import org.springframework.integration.dsl.IntegrationFlows;
import org.springframework.integration.dsl.StandardIntegrationFlow;
import org.springframework.integration.dsl.core.Pollers;
import org.springframework.integration.support.MessageBuilder;
import org.springframework.scheduling.support.PeriodicTrigger;

import ru.raiffeisen.terminator.client.bean.ClientBundleWriter;
import ru.raiffeisen.terminator.common.integration.BatchAggregator;
import ru.raiffeisen.terminator.common.jdbc.ResultSetMapIterator;
import ru.raiffeisen.terminator.common.jmx.PeriodicProcessJmxConsole;

/**
 * @author Roman Zaripov
 */
@Configuration
public class ClientBundleFlow {
	
	@Value("${terminator.client.bundle.poller.delay}") private int pollerDelay;
	
	@Autowired @Qualifier(DataSourceConfiguration.ICDB_DS)
	private DataSource icdbDs;
	@Value("${query.client.bundles.get}") private String getClientBundlesQuery;
	
	@Autowired private ClientBundleWriter clientBundleWriter;
	
	private static final Logger LOG = LoggerFactory.getLogger(ClientBundleFlow.class);
	
	@Bean
	public PeriodicTrigger clientBundleTrigger() {
		return new PeriodicTrigger(pollerDelay, TimeUnit.MINUTES);
	}
	
	@Bean
	public StandardIntegrationFlow clientBundleIntegrationFlow() {
		MessageSource<ResultSetMapIterator> source = () -> { 
			LOG.info("Starting client bundle replication...");
			ResultSetMapIterator iterator = new ResultSetMapIterator(icdbDs, getClientBundlesQuery);
			iterator.setLastMessageListener(() -> {
				LOG.info("Finishing client bundle replication");
				return null;
			});
			return MessageBuilder.withPayload(iterator).build();
		};
		
		return IntegrationFlows
			.from(source, c -> c.poller(Pollers.trigger(clientBundleTrigger())))
			.split(c -> c.applySequence(false))
			.handle(new BatchAggregator(256))
			.handle(clientBundleWriter)
			.get();
	}
	
	@Bean
	public PeriodicProcessJmxConsole clientBundleConsole() {
		return new PeriodicProcessJmxConsole(clientBundleTrigger(), clientBundleIntegrationFlow(), "Terminator:type=Object,name=Client Bundle");
	}
}
