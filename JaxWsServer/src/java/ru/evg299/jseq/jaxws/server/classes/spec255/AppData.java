package ru.evg299.jseq.jaxws.server.classes.spec255;

import ru.evg299.jseq.jaxws.server.wrappers.PowMethod;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import java.io.Serializable;


@XmlAccessorType(XmlAccessType.FIELD)
public class AppData implements Serializable {
    private PowMethod powMethod;

    public PowMethod getPowMethod() {
        return powMethod;
    }

    public void setPowMethod(PowMethod powMethod) {
        this.powMethod = powMethod;
    }
}
