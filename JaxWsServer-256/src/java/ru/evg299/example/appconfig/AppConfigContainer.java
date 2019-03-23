package ru.evg299.example.appconfig;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "app-config")
public class AppConfigContainer {
	@XmlAttribute()
	private String password;

	@XmlElementWrapper()
	@XmlElement(name = "property")
	private List<TripleProperty> properties;

	@XmlAccessorType(XmlAccessType.FIELD)
	public static class TripleProperty implements Comparable<TripleProperty> {
		@XmlAttribute()
		private String name;
		@XmlAttribute()
		private String caption;
		@XmlAttribute()
		private String value;

		public TripleProperty() {
			super();
		}

		public TripleProperty(String name, String caption, String value) {
			this.name = name;
			this.caption = caption;
			this.value = value;
		}

		@Override
		public int hashCode() {
			return this.name.hashCode();
		}

		@Override
		public boolean equals(Object obj) {
			if (obj instanceof TripleProperty) {
				TripleProperty tp = (TripleProperty) obj;
				return this.name.equals(tp.name);
			}

			return false;
		}

		@Override
		public int compareTo(TripleProperty o) {
			return this.name.compareTo(o.name);
		}

		public String getCaption() {
			return caption;
		}

		public void setCaption(String caption) {
			this.caption = caption;
		}

		public String getValue() {
			return value;
		}

		public void setValue(String value) {
			this.value = value;
		}

		public void setName(String name) {
			this.name = name;
		}

		public String getName() {
			return name;
		}
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getPassword() {
		return password;
	}

	public List<TripleProperty> getProperties() {
		if (null == properties)
			properties = new ArrayList<AppConfigContainer.TripleProperty>();
		return properties;
	}

}
