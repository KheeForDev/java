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
 * <p>Java class for InsuranceInfoType complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="InsuranceInfoType">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="product" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="converageType" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="coverageOption" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "InsuranceInfoType", propOrder = {
    "product",
    "converageType",
    "coverageOption"
})
public class InsuranceInfoType {

    @XmlElement(required = true)
    protected String product;
    @XmlElement(required = true)
    protected String converageType;
    @XmlElement(required = true)
    protected String coverageOption;

    /**
     * Gets the value of the product property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getProduct() {
        return product;
    }

    /**
     * Sets the value of the product property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setProduct(String value) {
        this.product = value;
    }

    /**
     * Gets the value of the converageType property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getConverageType() {
        return converageType;
    }

    /**
     * Sets the value of the converageType property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setConverageType(String value) {
        this.converageType = value;
    }

    /**
     * Gets the value of the coverageOption property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCoverageOption() {
        return coverageOption;
    }

    /**
     * Sets the value of the coverageOption property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCoverageOption(String value) {
        this.coverageOption = value;
    }

}
