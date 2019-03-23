package ru.gosuslugi.smev.rev120315;

import java.io.Serializable;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "AppDocumentType", propOrder = { "requestCode", "binaryData",
		"digestValue" })
public class AppDocumentType implements Serializable {

	private static final long serialVersionUID = 1L;

	@XmlElement(name = "RequestCode", required = true, namespace = XML_CONSTANTS.smev)
	protected String requestCode;
	@XmlElement(name = "BinaryData", namespace = XML_CONSTANTS.smev)
	protected byte[] binaryData;
	@XmlElement(name = "DigestValue", namespace = XML_CONSTANTS.smev)
	protected byte[] digestValue;

	public String getRequestCode() {
		return requestCode;
	}

	public void setRequestCode(String value) {
		this.requestCode = value;
	}

	public byte[] getBinaryData() {
		return binaryData;
	}

	public void setBinaryData(byte[] value) {
		this.binaryData = ((byte[]) value);
	}

	public byte[] getDigestValue() {
		return digestValue;
	}

	public void setDigestValue(byte[] value) {
		this.digestValue = ((byte[]) value);
	}

}
