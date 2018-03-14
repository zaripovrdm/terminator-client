package ru.raiffeisen.terminator.client.bean;

import java.sql.Timestamp;
import java.util.Map;
import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.dao.EmptyResultDataAccessException;
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
public class EventTypeEnricher {
	
	@Autowired @Qualifier("webdbJdbcTemplate")
	private NamedParameterJdbcTemplate npjt;
	@Value("${query.client.get}") private String getClientQuery;
	
	static final String EVENT_TYPE_KEY = "_EVENT_TYPE";
		
	@ServiceActivator
	@Transactional(isolation = Isolation.READ_UNCOMMITTED)
	public Map<String, Object> enrich(@Payload Map<String, Object> client) {
		
		try {
			Timestamp lastmodified = npjt.queryForObject(getClientQuery, client, Timestamp.class);
			if (((Date) client.get(Client.LASTMODIFIED)).after(lastmodified)) {
				client.put(EVENT_TYPE_KEY, "modified");
			}
		} catch (EmptyResultDataAccessException ex) {;
			client.put(EVENT_TYPE_KEY, "new");
		}
		
		return client;
	}
}
