package soa.lab4.organization.model;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class Coordinates {

    private Double x;
    private Double y;

    @XmlElement
    public Double getX() {
        return x;
    }

    public void setX(Double x) {
        this.x = x;
    }

    @XmlElement
    public Double getY() {
        return y;
    }

    public void setY(Double y) {
        this.y = y;
    }
}
