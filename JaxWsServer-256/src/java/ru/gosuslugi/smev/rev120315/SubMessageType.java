package ru.gosuslugi.smev.rev120315;

import java.io.Serializable;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlSchemaType;
import javax.xml.datatype.XMLGregorianCalendar;

@XmlAccessorType(XmlAccessType.FIELD)
public class SubMessageType implements Serializable {

	private static final long serialVersionUID = 1L;

	@XmlElement(name = "SubRequestNumber", required = true)
	protected String subRequestNumber;
	@XmlElement(name = "Status", required = true)
	protected StatusMessage status;
	@XmlElement(name = "Originator")
	protected OrganizationInfoType originator;
	@XmlElement(name = "Date", required = true)
	@XmlSchemaType(name = "dateTime")
	protected XMLGregorianCalendar date;
	@XmlElement(name = "RequestIdRef")
	protected String requestIdRef;
	@XmlElement(name = "OriginRequestIdRef")
	protected String originRequestIdRef;
	@XmlElement(name = "ServiceCode")
	protected String serviceCode;
	@XmlElement(name = "CaseNumber")
	protected String caseNumber;

	public String getSubRequestNumber() {
		return subRequestNumber;
	}

	public void setSubRequestNumber(String value) {
		this.subRequestNumber = value;
	}

	public StatusMessage getStatus() {
		return status;
	}

	public void setStatus(StatusMessage value) {
		this.status = value;
	}

	public OrganizationInfoType getOriginator() {
		return originator;
	}

	public void setOriginator(OrganizationInfoType value) {
		this.originator = value;
	}

	public XMLGregorianCalendar getDate() {
		return date;
	}

	public void setDate(XMLGregorianCalendar value) {
		this.date = value;
	}

	public String getRequestIdRef() {
		return requestIdRef;
	}

	public void setRequestIdRef(String value) {
		this.requestIdRef = value;
	}

	public String getOriginRequestIdRef() {
		return originRequestIdRef;
	}

	public void setOriginRequestIdRef(String value) {
		this.originRequestIdRef = value;
	}

	public String getServiceCode() {
		return serviceCode;
	}

	public void setServiceCode(String value) {
		this.serviceCode = value;
	}

	public String getCaseNumber() {
		return caseNumber;
	}

	public void setCaseNumber(String value) {
		this.caseNumber = value;
	}

}