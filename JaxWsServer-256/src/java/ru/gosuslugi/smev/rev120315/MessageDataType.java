package ru.gosuslugi.smev.rev120315;

import java.io.Serializable;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "MessageDataType", propOrder = { "appData", "appDocument" })
public class MessageDataType implements Serializable {

	private static final long serialVersionUID = 1L;

	@XmlElement(name = "AppData", namespace = XML_CONSTANTS.smev)
	private AppDataType appData;
	@XmlElement(name = "AppDocument", namespace = XML_CONSTANTS.smev)
	private AppDocumentType appDocument;

	public void setAppData(AppDataType appData) {
		this.appData = appData;
	}

	public AppDataType getAppData() {
		return appData;
	}

	public void setAppDocument(AppDocumentType appDocument) {
		this.appDocument = appDocument;
	}

	public AppDocumentType getAppDocument() {
		return appDocument;
	}

}
