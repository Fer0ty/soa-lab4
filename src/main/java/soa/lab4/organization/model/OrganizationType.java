package soa.lab4.organization.model;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlEnumValue;
import javax.xml.bind.annotation.XmlType;
import java.io.Serializable;

@XmlType
@XmlEnum
public enum OrganizationType implements Serializable {
    @XmlEnumValue("PUBLIC")
    PUBLIC,

    @XmlEnumValue("TRUST")
    TRUST,

    @XmlEnumValue("OPEN_JOINT_STOCK_COMPANY")
    OPEN_JOINT_STOCK_COMPANY
}
