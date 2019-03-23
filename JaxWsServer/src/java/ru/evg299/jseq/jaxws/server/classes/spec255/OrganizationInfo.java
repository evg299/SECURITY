package ru.evg299.jseq.jaxws.server.classes.spec255;

import java.io.Serializable;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;

@XmlAccessorType(XmlAccessType.FIELD)
public class OrganizationInfo implements Serializable {


	@XmlElement(name = "Code", namespace = XML_CONSTANTS.smev)
	private String code;
	@XmlElement(name = "Name", namespace = XML_CONSTANTS.smev)
	private String name;

	public OrganizationInfo() {
		super();
	}

	public OrganizationInfo(String code, String name) {
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
