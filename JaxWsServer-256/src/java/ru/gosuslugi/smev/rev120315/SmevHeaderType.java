package ru.gosuslugi.smev.rev120315;

import java.io.Serializable;
import java.util.Calendar;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlType;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "Header", namespace = XML_CONSTANTS.smev)
public class SmevHeaderType implements Serializable {

	private static final long serialVersionUID = 1L;

	@XmlElement(name = "NodeId", namespace = XML_CONSTANTS.smev)
	private String nodeId;
	@XmlElement(name = "MessageId", namespace = XML_CONSTANTS.smev)
	private String messageId;
	@XmlElement(name = "TimeStamp", namespace = XML_CONSTANTS.smev)
	private Calendar timeStamp;
	@XmlElement(name = "MessageClass", namespace = XML_CONSTANTS.smev)
	private StatusMessage messageClass;
	@XmlElementWrapper(name = "PacketIds", namespace = XML_CONSTANTS.smev)
	@XmlElement(name = "Id", namespace = XML_CONSTANTS.smev)
	private List<SmevIdType> packetIds;

	public String getNodeId() {
		return nodeId;
	}

	public void setNodeId(String nodeId) {
		this.nodeId = nodeId;
	}

	public String getMessageId() {
		return messageId;
	}

	public void setMessageId(String messageId) {
		this.messageId = messageId;
	}

	public Calendar getTimeStamp() {
		return timeStamp;
	}

	public void setTimeStamp(Calendar timeStamp) {
		this.timeStamp = timeStamp;
	}

	public StatusMessage getMessageClass() {
		return messageClass;
	}

	public void setMessageClass(StatusMessage messageClass) {
		this.messageClass = messageClass;
	}

	public List<SmevIdType> getPacketIds() {
		return packetIds;
	}

	public void setPacketIds(List<SmevIdType> packetIds) {
		this.packetIds = packetIds;
	}

}
