package ru.evg299.jseq.jaxws.server.classes.spec255;

import java.io.Serializable;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;

@XmlAccessorType(XmlAccessType.FIELD)
public class Reference implements Serializable {

	@XmlElement(name = "Include", namespace = XML_CONSTANTS.xop)
	private Include include;

	public Reference() {
		super();
	}

	public Reference(Include include) {
		super();
		this.include = include;
	}

	public void setInclude(Include include) {
		this.include = include;
	}

	public Include getInclude() {
		return include;
	}

}
