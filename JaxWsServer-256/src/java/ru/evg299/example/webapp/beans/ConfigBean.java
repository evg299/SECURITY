package ru.evg299.example.webapp.beans;

import org.primefaces.event.RowEditEvent;
import ru.evg299.example.appconfig.AppConfigContainer;
import ru.evg299.example.appconfig.ConfigReader;

import javax.annotation.PostConstruct;
import javax.faces.bean.ApplicationScoped;
import javax.faces.bean.ManagedBean;
import javax.faces.event.ActionEvent;
import java.util.ArrayList;
import java.util.List;

@ManagedBean
@ApplicationScoped
public class ConfigBean {

    private AppConfigContainer appConfigContainer;
    private AppConfigContainer.TripleProperty newProperty = new AppConfigContainer.TripleProperty();

    @PostConstruct
    public void init() {
        appConfigContainer = ConfigReader.getInstance().getAppConfigContainer();
    }

    public void add(ActionEvent actionEvent) {
        if (null != newProperty.getName()
                && !"".equals(newProperty.getName().trim()))
            appConfigContainer.getProperties().add(newProperty);

        newProperty = new AppConfigContainer.TripleProperty();

        ConfigReader.getInstance().setAppConfigContainer(appConfigContainer);
        ConfigReader.getInstance().updateFile();
    }

    public void onEdit(RowEditEvent event) {
        List<AppConfigContainer.TripleProperty> newProps = new ArrayList<AppConfigContainer.TripleProperty>();
        for (AppConfigContainer.TripleProperty tp : appConfigContainer
                .getProperties()) {
            if (null != tp.getName() && !"".equals(tp.getName().trim())) {
                newProps.add(tp);
            }
        }
        appConfigContainer.getProperties().clear();
        appConfigContainer.getProperties().addAll(newProps);

        ConfigReader.getInstance().setAppConfigContainer(appConfigContainer);
        ConfigReader.getInstance().updateFile();
    }

    public AppConfigContainer getAppConfigContainer() {
        return appConfigContainer;
    }

    public AppConfigContainer.TripleProperty getNewProperty() {
        return newProperty;
    }
}
