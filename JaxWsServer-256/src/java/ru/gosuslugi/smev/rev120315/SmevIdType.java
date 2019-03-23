package ru.gosuslugi.smev.rev120315;

import java.io.Serializable;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;

@XmlAccessorType(XmlAccessType.FIELD)
public class SmevIdType implements Serializable {

	private static final long serialVersionUID = 1L;

	@XmlElement(name = "MessageId", namespace = XML_CONSTANTS.smev)
	private String messageId;

	@XmlElement(name = "SubRequestNumber", namespace = XML_CONSTANTS.smev)
	private int subRequestNumber;

	public String getMessageId() {
		return messageId;
	}

	public void setMessageId(String messageId) {
		this.messageId = messageId;
	}

	public int getSubRequestNumber() {
		return subRequestNumber;
	}

	public void setSubRequestNumber(int subRequestNumber) {
		this.subRequestNumber = subRequestNumber;
	}

}
