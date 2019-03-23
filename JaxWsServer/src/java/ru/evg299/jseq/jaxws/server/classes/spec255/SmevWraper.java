package ru.evg299.jseq.jaxws.server.classes.spec255;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;

@XmlAccessorType(XmlAccessType.FIELD)
public class SmevWraper {

	@XmlElement(name = "Message", namespace = XML_CONSTANTS.smev)
	private Message message;
	@XmlElement(name = "MessageData", namespace = XML_CONSTANTS.smev)
	private MessageData messageData;

	public SmevWraper() {
		super();
	}

	public SmevWraper(Message message, MessageData messageData) {
		super();
		this.setMessage(message);
		this.setMessageData(messageData);
	}

	public void setMessage(Message message) {
		this.message = message;
	}

	public Message getMessage() {
		return message;
	}

	public void setMessageData(MessageData messageData) {
		this.messageData = messageData;
	}

	public MessageData getMessageData() {
		return messageData;
	}
}
