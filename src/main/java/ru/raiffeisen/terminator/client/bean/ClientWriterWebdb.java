package ru.raiffeisen.terminator.client.bean;

import java.util.Arrays;
import java.util.Map;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.integration.annotation.MessageEndpoint;
import org.springframework.integration.annotation.ServiceActivator;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import ru.raiffeisen.terminator.client.model.Client;

/**
 * @author Roman Zaripov
 */
@MessageEndpoint
public class ClientWriterWebdb {
	
	@Autowired @Qualifier("webdbJdbcTemplate")
	private NamedParameterJdbcTemplate npjt;
	@Value("${query.client.merge}") private String mergeClientQuery;
	
	private static final Logger LOG = LoggerFactory.getLogger(ClientWriterWebdb.class);
	
	@ServiceActivator
	@Transactional(isolation = Isolation.READ_UNCOMMITTED)
	public /*Map<String, Object>[]*/ void handle(@Payload Map<String, Object>[] clients) throws InterruptedException {
		
		LOG.info("Writing clients info in WEBDB. Client IDs: [{}]", Arrays.stream(clients).map(m -> m.get(Client.CNUM)).map(Object::toString).collect(Collectors.joining(",")));
		
		npjt.batchUpdate(mergeClientQuery, clients);
			
		//return clients;
	}
}
