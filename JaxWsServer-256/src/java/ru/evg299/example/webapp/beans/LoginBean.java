package ru.evg299.example.webapp.beans;

import ru.evg299.example.appconfig.AppConfigContainer;
import ru.evg299.example.appconfig.ConfigReader;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import java.io.IOException;


@ManagedBean
@SessionScoped
public class LoginBean {
    private String password;
    private boolean logined = false;

    // Авторизация
    public String loginAction() {
        AppConfigContainer appConfigContainer = ConfigReader.getInstance()
                .getAppConfigContainer();
        if (null != appConfigContainer
                && appConfigContainer.getPassword().equals(password)) {
            logined = true;
            return "configs?faces-redirect=true";
        }

        password = null;
        return null;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void isLogined() {
        if (!logined) {
            FacesContext fc = FacesContext.getCurrentInstance();
            try {
                fc.getExternalContext().redirect("/" + fc.getExternalContext().getContextName());
            } catch (IOException e) {
            }
        }
    }

}
