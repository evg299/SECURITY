package ru.gosuslugi.smev.handlers.utils;

import java.io.ByteArrayInputStream;

import javax.xml.namespace.QName;
import javax.xml.soap.MessageFactory;
import javax.xml.soap.SOAPException;
import javax.xml.soap.SOAPFactory;
import javax.xml.soap.SOAPFault;
import javax.xml.soap.SOAPMessage;
import javax.xml.ws.soap.SOAPFaultException;

public class SOAPUtil {

	public static SOAPFaultException createSOAPFaultException(String faultcode,
			String faultstring) {
		try {
			SOAPFactory fac = SOAPFactory.newInstance();
			SOAPFault sf = fac.createFault(faultstring, new QName(
					"http://schemas.xmlsoap.org/soap/envelope/", faultcode));
			return new SOAPFaultException(sf);
		} catch (SOAPException e) {
			e.printStackTrace();
		}

		return null;
	}

	public static SOAPMessage createSOAPMessage(String responseXml) {
		try {
			ByteArrayInputStream in = new ByteArrayInputStream(
					responseXml.getBytes("UTF8"));
			MessageFactory messageFactory = MessageFactory.newInstance();
			return messageFactory.createMessage(null, in);
		} catch (Exception e) {
			return null;
		}
	}
}
