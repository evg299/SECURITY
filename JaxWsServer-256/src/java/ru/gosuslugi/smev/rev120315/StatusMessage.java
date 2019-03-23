package ru.gosuslugi.smev.rev120315;

import javax.xml.bind.annotation.XmlEnum;

@XmlEnum()
public enum StatusMessage {
	ACCEPT, CANCEL, FAILURE, INVALID, NOTIFY, PACKET, PING, PROCESS, REJECT, REQUEST, RESULT, STATE
}
