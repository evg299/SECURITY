package ru.gosuslugi.smev.rev120315;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "SmevWraperType", propOrder = { "message", "messageData" })
public class SmevWraperType {
	private static final long serialVersionUID = 1L;

	@XmlElement(name = "Message", namespace = XML_CONSTANTS.smev)
	private MessageType message;
	@XmlElement(name = "MessageData", namespace = XML_CONSTANTS.smev)
	private MessageDataType messageData;

	public SmevWraperType() {
		super();
	}

	public SmevWraperType(MessageType message, MessageDataType messageData) {
		super();
		this.setMessage(message);
		this.setMessageData(messageData);
	}

	public void setMessage(MessageType message) {
		this.message = message;
	}

	public MessageType getMessage() {
		return message;
	}

	public void setMessageData(MessageDataType messageData) {
		this.messageData = messageData;
	}

	public MessageDataType getMessageData() {
		return messageData;
	}
}
