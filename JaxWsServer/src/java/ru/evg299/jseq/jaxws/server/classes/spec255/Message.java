package ru.evg299.jseq.jaxws.server.classes.spec255;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;
import java.io.Serializable;
import java.util.Calendar;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(namespace = XML_CONSTANTS.smev, propOrder = {"sender", "recipient",
        "originator", "serviceName", "typeCode", "status", "date", "exchangeType",
        "requestIdRef", "originRequestIdRef", "serviceCode", "caseNumber",
        "testMsg"})
public class Message implements Serializable {

    @XmlElement(name = "Sender", namespace = XML_CONSTANTS.smev)
    private OrganizationInfo sender;
    @XmlElement(name = "Recipient", namespace = XML_CONSTANTS.smev)
    private OrganizationInfo recipient;
    @XmlElement(name = "Originator", namespace = XML_CONSTANTS.smev)
    private OrganizationInfo originator;
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
    @XmlElement(name = "TestMsg", namespace = XML_CONSTANTS.smev)
    private Boolean testMsg;

    public Message() {
    }

    public Message(OrganizationInfo sender, OrganizationInfo recipient,
                   OrganizationInfo originator, TypeCode typeCode, Calendar date,
                   String requestIdRef, String originRequestIdRef, String serviceCode,
                   String caseNumber) {
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

    public OrganizationInfo getSender() {
        return sender;
    }

    public void setSender(OrganizationInfo sender) {
        this.sender = sender;
    }

    public OrganizationInfo getRecipient() {
        return recipient;
    }

    public void setRecipient(OrganizationInfo recipient) {
        this.recipient = recipient;
    }

    public OrganizationInfo getOriginator() {
        return originator;
    }

    public void setOriginator(OrganizationInfo originator) {
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

}
