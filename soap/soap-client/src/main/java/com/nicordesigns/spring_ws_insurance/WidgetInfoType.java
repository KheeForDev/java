//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.2.7 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2022.07.20 at 10:03:01 AM SGT 
//


package com.nicordesigns.spring_ws_insurance;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for WidgetInfoType complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="WidgetInfoType">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="wgtAmount" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="wgtContractNumber" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "WidgetInfoType", propOrder = {
    "wgtAmount",
    "wgtContractNumber"
})
public class WidgetInfoType {

    @XmlElement(required = true)
    protected String wgtAmount;
    @XmlElement(required = true)
    protected String wgtContractNumber;

    /**
     * Gets the value of the wgtAmount property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getWgtAmount() {
        return wgtAmount;
    }

    /**
     * Sets the value of the wgtAmount property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setWgtAmount(String value) {
        this.wgtAmount = value;
    }

    /**
     * Gets the value of the wgtContractNumber property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getWgtContractNumber() {
        return wgtContractNumber;
    }

    /**
     * Sets the value of the wgtContractNumber property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setWgtContractNumber(String value) {
        this.wgtContractNumber = value;
    }

}
