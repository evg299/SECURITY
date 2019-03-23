package ru.evg299.jseq.jaxws.server.signature;

import java.io.IOException;
import java.util.Properties;

/**
 * Created by Evgeny(e299792459@gmail.com) on 17.02.14.
 */
public class ConfigSignUtil {

    public static class ConfigSign {
        private String certId;
        private String contName;
        private String contPass;
        private boolean checkInputSignature;

        public String getCertId() {
            return certId;
        }

        public void setCertId(String certId) {
            this.certId = certId;
        }

        public String getContName() {
            return contName;
        }

        public void setContName(String contName) {
            this.contName = contName;
        }

        public String getContPass() {
            return contPass;
        }

        public void setContPass(String contPass) {
            this.contPass = contPass;
        }

        public boolean isCheckInputSignature() {
            return checkInputSignature;
        }

        public void setCheckInputSignature(boolean checkInputSignature) {
            this.checkInputSignature = checkInputSignature;
        }
    }

    public static ConfigSign getConfigSign() {
        ConfigSign result = new ConfigSign();

        ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
        Properties prop = new Properties();

        try {
            prop.load(classLoader.getResourceAsStream("/ru/evg299/jseq/jaxws/server/signature/sign.properties"));
            result.setCertId(prop.getProperty("certId"));
            result.setContName(prop.getProperty("contName"));
            result.setContPass(prop.getProperty("contPass"));
            result.setCheckInputSignature(Boolean.getBoolean(prop.getProperty("checkInputSignature")));
        } catch (IOException e) {
            e.printStackTrace();
        }

        return result;
    }
}
