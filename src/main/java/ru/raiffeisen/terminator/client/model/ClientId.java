package ru.raiffeisen.terminator.client.model;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;

/**
 * @author Roman Zaripov
 */
@XmlType(name = "clientId")
@XmlAccessorType(XmlAccessType.FIELD)
public class ClientId {
	
	@XmlElement(required = true, name = "cnum")
	private Long cnum;

	public Long getCnum() {
		return cnum;
	}

	public void setCnum(Long cnum) {
		this.cnum = cnum;
	}
}
