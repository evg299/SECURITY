package ru.evg299.example.appconfig;

import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;

public class AppConfigUtils {

	public static AppConfigContainer unmarshalFromInputStream(InputStream is)
			throws JAXBException, UnsupportedEncodingException {
		JAXBContext jaxbContext = JAXBContext.newInstance(AppConfigContainer.class);
		Unmarshaller unmarshaller = jaxbContext.createUnmarshaller();
		AppConfigContainer appConfigContainer = (AppConfigContainer) unmarshaller.unmarshal(is);
		return appConfigContainer;
	}

	public static void marshalFromObjectToOutputStream(
			AppConfigContainer appConfigContainer, OutputStream os)
			throws JAXBException {
		JAXBContext jaxbContext = JAXBContext.newInstance(AppConfigContainer.class);
		Marshaller m = jaxbContext.createMarshaller();
		// m.setProperty("jaxb.encoding", "UTF8");
		m.marshal(appConfigContainer, os);
	}
}
