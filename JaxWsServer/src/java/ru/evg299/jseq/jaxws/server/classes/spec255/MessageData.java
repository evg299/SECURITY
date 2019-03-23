package ru.evg299.jseq.jaxws.server.classes.spec255;

import java.io.Serializable;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;

@XmlAccessorType(XmlAccessType.FIELD)
public class MessageData implements Serializable {

	@XmlElement(name = "AppData", namespace = XML_CONSTANTS.smev)
	private AppData appData;

	public void setAppData(AppData appData) {
		this.appData = appData;
	}

	public AppData getAppData() {
		return appData;
	}

}
