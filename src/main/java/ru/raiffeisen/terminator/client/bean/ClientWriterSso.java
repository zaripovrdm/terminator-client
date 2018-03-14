package ru.raiffeisen.terminator.client.bean;

import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.integration.annotation.MessageEndpoint;
import org.springframework.integration.annotation.ServiceActivator;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.messaging.handler.annotation.Payload;

import ru.raiffeisen.terminator.client.model.Client;

/**
 * @author Roman Zaripov
 */
@MessageEndpoint
public class ClientWriterSso {
	
	@Autowired @Qualifier("ssoJdbcTemplate")
	private NamedParameterJdbcTemplate npjt;
	
	@Value("${query.client.sso.select}") private String selectClientQuery;
	@Value("${query.client.sso.insert}") private String insertClientQuery;
	@Value("${query.client.sso.update}") private String updateClientQuery;
	
	private static final Logger LOG = LoggerFactory.getLogger(ClientWriterSso.class);
	
	@ServiceActivator
	public void handle(@Payload Map<String, Object> client) {
		
		if (LOG.isTraceEnabled()) {
			LOG.info("Writing client info in SSO. Client ID: [{}]", client.get(Client.CNUM));
		}
		
		try {
			npjt.queryForObject(selectClientQuery, client, Integer.class);
			npjt.update(updateClientQuery, client);
		} catch (EmptyResultDataAccessException ex) {
			npjt.update(insertClientQuery, client);
		}
	}

}
