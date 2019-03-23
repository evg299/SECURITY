package ru.evg299.jseq.jaxws.server.classes.spec255;

import java.io.Serializable;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;

@XmlAccessorType(XmlAccessType.FIELD)
public class AppDocument implements Serializable {

	private static final long serialVersionUID = 2667463467391052781L;

	@XmlElement(name = "BinaryData", namespace = __XMLProps.smev)
	private String binaryData;
	@XmlElement(name = "Reference", namespace = __XMLProps.smev)
	private Reference reference;
	@XmlElement(name = "DigestValue", namespace = __XMLProps.smev)
	private String digestValue;
	@XmlElement(name = "RequestCode", namespace = __XMLProps.smev)
	private String requestCode;

	public AppDocument() {
		super();
	}

	public AppDocument(String binaryData, Reference reference,
			String digestValue, String requestCode) {
		super();
		this.binaryData = binaryData;
		this.reference = reference;
		this.digestValue = digestValue;
		this.requestCode = requestCode;
	}

	public void setBinaryData(String binaryData) {
		this.binaryData = binaryData;
	}

	public String getBinaryData() {
		return binaryData;
	}

	public void setReference(Reference reference) {
		this.reference = reference;
	}

	public Reference getReference() {
		return reference;
	}

	public void setDigestValue(String digestValue) {
		this.digestValue = digestValue;
	}

	public String getDigestValue() {
		return digestValue;
	}

	public void setRequestCode(String requestCode) {
		this.requestCode = requestCode;
	}

	public String getRequestCode() {
		return requestCode;
	}

}
