package ru.raiffeisen.terminator.client.bean;

import java.util.Map;

/**
 * @author Roman Zaripov
 */
public class ClientFilters {
	
	public static boolean filterTokenRollbackMessages(Map<String, Object> client) {
		// Messages with timestamp less or equal WEBDB timestamp are not marked with event type
		return client.containsKey(EventTypeEnricher.EVENT_TYPE_KEY);
	}
}
