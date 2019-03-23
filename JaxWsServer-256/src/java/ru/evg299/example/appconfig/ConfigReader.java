package ru.evg299.example.appconfig;

import javax.xml.bind.JAXBException;
import java.io.*;
import java.net.URL;


public class ConfigReader {
    private static ConfigReader instance = null;
    private static String configFile = "/app-config.xml";

    public static ConfigReader getInstance() {
        if (null == instance)
            instance = new ConfigReader();

        return instance;
    }

    private AppConfigContainer appConfigContainer;

    private ConfigReader() {
        try {
            InputStream in = this.getClass().getResourceAsStream(configFile);
            this.appConfigContainer = AppConfigUtils.unmarshalFromInputStream(in);
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException("Проблемы с чтением файла " + configFile);
        } catch (JAXBException e) {
            e.printStackTrace();
            throw new RuntimeException("Проблемы с парсингом файла "
                    + configFile);
        }
    }

    public void updateFile() {
        try {
            URL resourceUrl = getClass().getResource(configFile);
            File file = new File(resourceUrl.toURI());
            OutputStream os = new FileOutputStream(file);
            AppConfigUtils.marshalFromObjectToOutputStream(appConfigContainer, os);
            os.close();
        } catch (Exception e) {
            throw new RuntimeException("Проблемы с записью файла " + configFile);
        }
    }

    public void setAppConfigContainer(AppConfigContainer appConfigContainer) {
        this.appConfigContainer = appConfigContainer;
    }

    public AppConfigContainer getAppConfigContainer() {
        return appConfigContainer;
    }

    public String getPropertyValue(String propertyName) {
        if (null != this.appConfigContainer) {
            for (AppConfigContainer.TripleProperty tripleProperty : this.appConfigContainer.getProperties()) {
                if (tripleProperty.getName().equals(propertyName)) {
                    return tripleProperty.getValue();
                }
            }
        }

        return null;
    }
}
