package ru.evg299.jseq.jaxws.server.wrappers;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;

/**
 * Created by Evgeny(e299792459@gmail.com) on 16.02.14.
 */
@XmlAccessorType(XmlAccessType.FIELD)
public class PowMethod {
    private Double result;
    private Double base;
    private Double exponent;

    public Double getResult() {
        return result;
    }

    public void setResult(Double result) {
        this.result = result;
    }

    public Double getBase() {
        return base;
    }

    public void setBase(Double base) {
        this.base = base;
    }

    public Double getExponent() {
        return exponent;
    }

    public void setExponent(Double exponent) {
        this.exponent = exponent;
    }
}
