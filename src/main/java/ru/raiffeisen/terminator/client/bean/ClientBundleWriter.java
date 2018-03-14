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
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import ru.raiffeisen.terminator.client.model.Client;

/**
 * @author Roman Zaripov
 */
@MessageEndpoint
public class ClientBundleWriter {
	
	@Autowired @Qualifier("webdbJdbcTemplate")
	private NamedParameterJdbcTemplate npjt;
	@Value("${query.client.bundle.update}") private String updateClientBundleQuery;
	
	private static final Logger LOG = LoggerFactory.getLogger(ClientBundleWriter.class);
	
	@ServiceActivator
	@Transactional(isolation = Isolation.READ_UNCOMMITTED, propagation = Propagation.REQUIRES_NEW)
	public void handle(@Payload Map<String, Object>[] clients) throws InterruptedException {
		
		if (LOG.isTraceEnabled()) {
			LOG.trace("Writing client bundle info in WEBDB. Client IDs: [{}]", Arrays.stream(clients).map(m -> m.get(Client.CNUM)).map(Object::toString).collect(Collectors.joining(",")));
		}
		
		try {
			npjt.batchUpdate(updateClientBundleQuery, clients);
		} catch (Exception ex) {
			LOG.error("Exception occured while updating client bundles", ex);
		}
	}
}
