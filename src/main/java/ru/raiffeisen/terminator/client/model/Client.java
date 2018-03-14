package ru.raiffeisen.terminator.client.model;

import java.util.Date;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlSchemaType;
import javax.xml.bind.annotation.XmlType;

/**
 * @author Roman Zaripov
 */
@XmlType(name = "client")
@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "client")
public class Client {
	
	public static final String CNUM = "CNUM";	
	public static final String BRANCH_ID = "BRANCH_ID";
	public static final String CATEGORY_ID = "CATEGORY_ID";
	public static final String FIRST_NAME = "FIRST_NAME";
	public static final String LAST_NAME = "LAST_NAME";
	public static final String PATRONYMIC = "PATRONYMIC";
	public static final String TRANSLIT_NAME = "TRANSLIT_NAME";
	public static final String BIRTH_DATE = "BIRTH_DATE";
	public static final String BIRTH_PLACE = "BIRTH_PLACE";
	public static final String LEGAL_ADDRESS = "LEGAL_ADDRESS";
	public static final String INN = "INN";
	public static final String EMAIL = "EMAIL";
	public static final String MOBILE_PHONE = "MOBILE_PHONE";
	public static final String PASSPORT_NUMBER = "PASSPORT_NUMBER";
	public static final String PASSPORT_ISSUER = "PASSPORT_ISSUER";
	public static final String PASSPORT_DATE = "PASSPORT_DATE";
	public static final String RESIDENT = "RESIDENT";
	public static final String COMPLIANCE = "COMPLIANCE";
	public static final String PEP_TYPE_ID = "PEP_TYPE_ID";
	public static final String LASTMODIFIED = "LASTMODIFIED";
	
	@XmlElement(required = true, name = "id")
	private ClientId id;
	
	@XmlElement(required = false, name = "employerId")
	private Integer employerId;
	
	@XmlElement(required = false, name = "branchId")
	private Integer branchId;
	
	@XmlElement(required = true, name = "categoryId")
	private Integer categoryId;
	
	@XmlElement(required = true, name = "firstName")
	private String firstName;
	
	@XmlElement(required = true, name = "lastName")
	private String lastName;
	
	@XmlElement(required = false, name = "patronymic")
	private String patronymic;
	
	@XmlElement(required = false, name = "translitName")
	private String translitName;
	
	@XmlSchemaType(name = "date")
	@XmlElement(required = false,name = "birthDate")
	private Date birthDate;
	
	@XmlElement(required = false, name = "birthPlace")
	private String birthPlace;
	
	@XmlElement(required = false, name = "legalAddress")
	private String legalAddress;
	
	@XmlElement(required = false, name = "inn")
	private String inn;
	
	@XmlElement(required = false, name = "email")
	private String email;
	
	@XmlElement(required = false, name = "mobilePhone")
	private String mobilePhone;
	
	@XmlElement(required = true, name = "passportNumber")
	private String passportNumber;
	
	@XmlElement(required = false, name = "passportIssuer")
	private String passportIssuer;
	
	@XmlSchemaType(name = "date")
	@XmlElement(required = false, name = "passportDate")
	private Date passportDate;
	
	@XmlElement(required = true, name = "resident")
	private boolean resident;
	
	@XmlElement(required = true, name = "complience")
	private boolean complience;
	
	@XmlElement(required = false, name = "pepTypeId")
	private Integer pepTypeId;
	
	@XmlElement(required = true, name = "lastmodified")
	private Date lastmodified;
	
	// --- Getters / Setters ---

	public Integer getEmployerId() {
		return employerId;
	}

	public ClientId getId() {
		return id;
	}

	public void setId(ClientId id) {
		this.id = id;
	}

	public void setEmployerId(Integer employerId) {
		this.employerId = employerId;
	}

	public Integer getBranchId() {
		return branchId;
	}

	public void setBranchId(Integer branchId) {
		this.branchId = branchId;
	}

	public Integer getCategoryId() {
		return categoryId;
	}

	public void setCategoryId(Integer categoryId) {
		this.categoryId = categoryId;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public String getPatronymic() {
		return patronymic;
	}

	public void setPatronymic(String patronymic) {
		this.patronymic = patronymic;
	}

	public String getTranslitName() {
		return translitName;
	}

	public void setTranslitName(String translitName) {
		this.translitName = translitName;
	}

	public Date getBirthDate() {
		return birthDate;
	}

	public void setBirthDate(Date birthDate) {
		this.birthDate = birthDate;
	}

	public String getBirthPlace() {
		return birthPlace;
	}

	public void setBirthPlace(String birthPlace) {
		this.birthPlace = birthPlace;
	}

	public String getLegalAddress() {
		return legalAddress;
	}

	public void setLegalAddress(String legalAddress) {
		this.legalAddress = legalAddress;
	}

	public String getInn() {
		return inn;
	}

	public void setInn(String inn) {
		this.inn = inn;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getMobilePhone() {
		return mobilePhone;
	}

	public void setMobilePhone(String mobilePhone) {
		this.mobilePhone = mobilePhone;
	}

	public String getPassportNumber() {
		return passportNumber;
	}

	public void setPassportNumber(String passportNumber) {
		this.passportNumber = passportNumber;
	}

	public String getPassportIssuer() {
		return passportIssuer;
	}

	public void setPassportIssuer(String passportIssuer) {
		this.passportIssuer = passportIssuer;
	}

	public Date getPassportDate() {
		return passportDate;
	}

	public void setPassportDate(Date passportDate) {
		this.passportDate = passportDate;
	}

	public boolean isResident() {
		return resident;
	}

	public void setResident(boolean resident) {
		this.resident = resident;
	}

	public boolean isComplience() {
		return complience;
	}

	public void setComplience(boolean complience) {
		this.complience = complience;
	}

	public Integer getPepTypeId() {
		return pepTypeId;
	}

	public void setPepTypeId(Integer pepTypeId) {
		this.pepTypeId = pepTypeId;
	}

	public Date getLastmodified() {
		return lastmodified;
	}

	public void setLastmodified(Date lastmodified) {
		this.lastmodified = lastmodified;
	}
}
