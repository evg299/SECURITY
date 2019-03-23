package ru.gosuslugi.smev.rev120315;

import java.io.Serializable;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;

import ru.gosuslugi.smev.methods.SayHello;

@XmlAccessorType(XmlAccessType.FIELD)
public class AppDataType implements Serializable {

	private static final long serialVersionUID = 1L;

	private SayHello sayHello;

	public void setSayHello(SayHello sayHello) {
		this.sayHello = sayHello;
	}

	public SayHello getSayHello() {
		return sayHello;
	}

}
