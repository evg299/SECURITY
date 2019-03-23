package ru.gosuslugi.smev.rev120315;

import java.io.Serializable;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;

@XmlAccessorType(XmlAccessType.FIELD)
public class OrganizationInfoType implements Serializable {

	private static final long serialVersionUID = 1L;

	@XmlElement(name = "Code", namespace = XML_CONSTANTS.smev)
	private String code;
	@XmlElement(name = "Name", namespace = XML_CONSTANTS.smev)
	private String name;

	public OrganizationInfoType() {
		super();
	}

	public OrganizationInfoType(String code, String name) {
		super();
		this.code = code;
		this.name = name;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

}
