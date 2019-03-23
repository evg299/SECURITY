package ru.gosuslugi.smev.rev120315;

import java.io.Serializable;
import java.util.Calendar;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlType;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(namespace = XML_CONSTANTS.smev, propOrder = { "sender", "recipient",
		"originator", "serviceName", "typeCode", "status", "date",
		"exchangeType", "requestIdRef", "originRequestIdRef", "serviceCode",
		"caseNumber", "subMessages", "testMsg" })
public class MessageType implements Serializable {

	private static final long serialVersionUID = 1L;

	@XmlElement(name = "Sender", namespace = XML_CONSTANTS.smev)
	private OrganizationInfoType sender;
	@XmlElement(name = "Recipient", namespace = XML_CONSTANTS.smev)
	private OrganizationInfoType recipient;
	@XmlElement(name = "Originator", namespace = XML_CONSTANTS.smev)
	private OrganizationInfoType originator;
	@XmlElement(name = "ServiceName", namespace = XML_CONSTANTS.smev)
	private String serviceName;
	@XmlElement(name = "TypeCode", namespace = XML_CONSTANTS.smev)
	private TypeCode typeCode;
	@XmlElement(name = "Status", namespace = XML_CONSTANTS.smev)
	private StatusMessage status;
	@XmlElement(name = "Date", namespace = XML_CONSTANTS.smev)
	private Calendar date;
	@XmlElement(name = "RequestIdRef", namespace = XML_CONSTANTS.smev)
	private String requestIdRef;
	@XmlElement(name = "OriginRequestIdRef", namespace = XML_CONSTANTS.smev)
	private String originRequestIdRef;
	@XmlElement(name = "ServiceCode", namespace = XML_CONSTANTS.smev)
	private String serviceCode;
	@XmlElement(name = "CaseNumber", namespace = XML_CONSTANTS.smev)
	private String caseNumber;
	@XmlElement(name = "ExchangeType", namespace = XML_CONSTANTS.smev)
	private String exchangeType;
	@XmlElementWrapper(name = "SubMessages", namespace = XML_CONSTANTS.smev)
	@XmlElement(name = "SubMessage", namespace = XML_CONSTANTS.smev)
	private List<SubMessageType> subMessages;
	@XmlElement(name = "TestMsg", namespace = XML_CONSTANTS.smev)
	private Boolean testMsg;

	public MessageType() {
	}

	public MessageType(OrganizationInfoType sender,
			OrganizationInfoType recipient, OrganizationInfoType originator,
			TypeCode typeCode, Calendar date, String requestIdRef,
			String originRequestIdRef, String serviceCode, String caseNumber) {
		super();
		this.sender = sender;
		this.recipient = recipient;
		this.originator = originator;
		this.typeCode = typeCode;
		this.date = date;
		this.requestIdRef = requestIdRef;
		this.originRequestIdRef = originRequestIdRef;
		this.serviceCode = serviceCode;
		this.caseNumber = caseNumber;
	}

	public OrganizationInfoType getSender() {
		return sender;
	}

	public void setSender(OrganizationInfoType sender) {
		this.sender = sender;
	}

	public OrganizationInfoType getRecipient() {
		return recipient;
	}

	public void setRecipient(OrganizationInfoType recipient) {
		this.recipient = recipient;
	}

	public OrganizationInfoType getOriginator() {
		return originator;
	}

	public void setOriginator(OrganizationInfoType originator) {
		this.originator = originator;
	}

	public TypeCode getTypeCode() {
		return typeCode;
	}

	public void setTypeCode(TypeCode typeCode) {
		this.typeCode = typeCode;
	}

	public Calendar getDate() {
		return date;
	}

	public void setDate(Calendar date) {
		this.date = date;
	}

	public String getRequestIdRef() {
		return requestIdRef;
	}

	public void setRequestIdRef(String requestIdRef) {
		this.requestIdRef = requestIdRef;
	}

	public String getOriginRequestIdRef() {
		return originRequestIdRef;
	}

	public void setOriginRequestIdRef(String originRequestIdRef) {
		this.originRequestIdRef = originRequestIdRef;
	}

	public String getServiceCode() {
		return serviceCode;
	}

	public void setServiceCode(String serviceCode) {
		this.serviceCode = serviceCode;
	}

	public String getCaseNumber() {
		return caseNumber;
	}

	public void setCaseNumber(String caseNumber) {
		this.caseNumber = caseNumber;
	}

	public void setExchangeType(String exchangeType) {
		this.exchangeType = exchangeType;
	}

	public String getExchangeType() {
		return exchangeType;
	}

	public void setTestMsg(Boolean testMsg) {
		this.testMsg = testMsg;
	}

	public Boolean getTestMsg() {
		return testMsg;
	}

	public void setStatus(StatusMessage status) {
		this.status = status;
	}

	public StatusMessage getStatus() {
		return status;
	}

	public void setServiceName(String serviceName) {
		this.serviceName = serviceName;
	}

	public String getServiceName() {
		return serviceName;
	}

	public void setSubMessages(List<SubMessageType> subMessages) {
		this.subMessages = subMessages;
	}

	public List<SubMessageType> getSubMessages() {
		return subMessages;
	}

}
