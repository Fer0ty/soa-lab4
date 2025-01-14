package soa.lab4.organization.model;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;

@XmlRootElement
public class Coordinates implements Serializable {

    @NotNull
    @Max(76)
    private Integer x;

    @NotNull
    @Min(-154)
    private Integer y;

    @XmlElement
    public Integer getX() {
        return x;
    }

    public void setX(Integer x) {
        this.x = x;
    }

    @XmlElement
    public Integer getY() {
        return y;
    }

    public void setY(Integer y) {
        this.y = y;
    }
}
