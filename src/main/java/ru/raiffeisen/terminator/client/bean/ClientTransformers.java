package ru.raiffeisen.terminator.client.bean;

import java.util.Date;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.integration.MessageRejectedException;
import org.springframework.integration.support.MessageBuilder;
import org.springframework.messaging.Message;
import org.springframework.util.StringUtils;

import ru.raiffeisen.terminator.client.model.Client;
import ru.raiffeisen.terminator.client.model.ClientId;
import ru.raiffeisen.terminator.common.CnumConverter;
import ru.raiffeisen.terminator.common.NumberConverter;

/**
 * @author Roman Zaripov
 */

public class ClientTransformers {
	
	private static final Logger LOG = LoggerFactory.getLogger(ClientTransformers.class);
	
	public static Map<String, Object> transformName(Map<String, Object> client) {
		client.put(Client.FIRST_NAME, normalizeName((String) client.get(Client.FIRST_NAME)));
		client.put(Client.LAST_NAME, normalizeName((String) client.get(Client.LAST_NAME)));
		client.put(Client.PATRONYMIC, normalizeName((String) client.get(Client.PATRONYMIC)));
				
		return client;		
	}
	
	public static Map<String, Object> transformPhone(Map<String, Object> client) {
		String phone = (String) client.get(Client.MOBILE_PHONE);
		
		if (phone == null) {
			return client;
		}		
		
		phone = phone.split(",")[0].replaceAll("\\D", "");
		
		if (phone.isEmpty() || phone.length() > 15) {
			LOG.warn("Invalid phone [{}] for client [{}]. Replace with null.", phone, client.get(Client.CNUM));
			client.put(Client.MOBILE_PHONE, null);
		} else {
			client.put(Client.MOBILE_PHONE, phone);
		}
		
		return client;
	}
	
	public static Map<String, Object> transformPassportNumber(Map<String, Object> client) {
		String number = (String) client.get(Client.PASSPORT_NUMBER);
		
		// CLEARED_PASSPORTNUMBER cannot be null in ICDB
		
		number = new StringBuilder(number).reverse().toString().replaceAll("[^А-Яа-яA-Za-z0-9]", "");
		
		if (number.isEmpty() || number.length() > 32) { // 32 is max column length
			throw new MessageRejectedException(null, "Cannot parse passport number [" + number + "] for client with ID [" + client.get(Client.CNUM) + "]. Skip replication");
		}
		
		client.put(Client.PASSPORT_NUMBER, number);
		
		return client;
	}
	
	public static Message<Client> transformToBean(Map<String, Object> map) {
		
		ClientId clientId = new ClientId();
		clientId.setCnum(NumberConverter.toLong(map.get(Client.CNUM)));
		
		Client client = new Client();		
		client.setId(clientId);
		client.setBranchId(NumberConverter.toInteger(map.get(Client.BRANCH_ID)));
		client.setCategoryId(NumberConverter.toInteger(map.get(Client.CATEGORY_ID)));
		client.setFirstName((String) map.get(Client.FIRST_NAME));
		client.setLastName((String) map.get(Client.LAST_NAME));
		client.setPatronymic((String) map.get(Client.PATRONYMIC));
		client.setTranslitName((String) map.get(Client.TRANSLIT_NAME));
		client.setBirthDate((Date) map.get(Client.BIRTH_DATE));
		client.setBirthPlace((String) map.get(Client.BIRTH_PLACE));
		client.setLegalAddress((String) map.get(Client.LEGAL_ADDRESS));
		client.setInn((String) map.get(Client.INN));
		client.setEmail((String) map.get(Client.EMAIL));
		client.setMobilePhone((String) map.get(Client.MOBILE_PHONE));
		client.setPassportNumber((String) map.get(Client.PASSPORT_NUMBER));
		client.setPassportIssuer((String) map.get(Client.PASSPORT_ISSUER));
		client.setPassportDate((Date) map.get(Client.PASSPORT_DATE));
		client.setResident(NumberConverter.toBoolean(map.get(Client.RESIDENT)));
		client.setComplience(NumberConverter.toBoolean(map.get(Client.COMPLIANCE)));
		client.setPepTypeId(NumberConverter.toInteger(map.get(Client.PEP_TYPE_ID)));
		client.setLastmodified((Date) map.get(Client.LASTMODIFIED));
				
		return MessageBuilder
			.withPayload(client)
			.setHeader("event", map.get(EventTypeEnricher.EVENT_TYPE_KEY))
			.setHeader("cnum", client.getId().getCnum())
			.setHeader("cnuma", CnumConverter.toCnuma(client.getId().getCnum()))
			.setHeader("object", "client")
			.build();
	}
	
	// Names has already passed some normalization, see SQL query
	private static String normalizeName(String name) {
		if (name == null) {
			return null;
		}
		
		name = name.replaceAll("\\s+", "-");
		name = StringUtils.capitalize(name.toLowerCase());
		
		if (name.contains("-")) {
			char[] arr = name.toCharArray();
			for (int i = 0; i < arr.length; i++) {
				if (arr[i] == '-') {
					arr[i + 1] = Character.toUpperCase(arr[i + 1]);
				}
			}
			name = new String(arr);
		}
		
		return name;
	}
}
