package ru.evg299.jseq.jaxws.server.classes.spec255;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;

@XmlAccessorType(XmlAccessType.FIELD)
public class Include {
	@XmlAttribute(name = "href")
	private String href;

	public Include() {
		super();
	}

	public Include(String href) {
		super();
		this.href = href;
	}

	public void setHref(String href) {
		this.href = href;
	}

	public String getHref() {
		return href;
	}
}
