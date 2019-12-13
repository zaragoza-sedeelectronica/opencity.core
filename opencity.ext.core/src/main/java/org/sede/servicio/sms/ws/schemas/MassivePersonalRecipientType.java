/**
 * MassivePersonalRecipientType.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package org.sede.servicio.sms.ws.schemas;


/**
 * Recipient type for MassivePersonalSMSSubmit
 */
@SuppressWarnings({"rawtypes", "unused"})
public class MassivePersonalRecipientType  implements java.io.Serializable {
    private java.lang.String number;

    private java.lang.String key1;

    private java.lang.String key2;

    private java.lang.String key3;

    public MassivePersonalRecipientType() {
    }

    public MassivePersonalRecipientType(
           java.lang.String number,
           java.lang.String key1,
           java.lang.String key2,
           java.lang.String key3) {
           this.number = number;
           this.key1 = key1;
           this.key2 = key2;
           this.key3 = key3;
    }


    /**
     * Gets the number value for this MassivePersonalRecipientType.
     * 
     * @return number
     */
    public java.lang.String getNumber() {
        return number;
    }


    /**
     * Sets the number value for this MassivePersonalRecipientType.
     * 
     * @param number
     */
    public void setNumber(java.lang.String number) {
        this.number = number;
    }


    /**
     * Gets the key1 value for this MassivePersonalRecipientType.
     * 
     * @return key1
     */
    public java.lang.String getKey1() {
        return key1;
    }


    /**
     * Sets the key1 value for this MassivePersonalRecipientType.
     * 
     * @param key1
     */
    public void setKey1(java.lang.String key1) {
        this.key1 = key1;
    }


    /**
     * Gets the key2 value for this MassivePersonalRecipientType.
     * 
     * @return key2
     */
    public java.lang.String getKey2() {
        return key2;
    }


    /**
     * Sets the key2 value for this MassivePersonalRecipientType.
     * 
     * @param key2
     */
    public void setKey2(java.lang.String key2) {
        this.key2 = key2;
    }


    /**
     * Gets the key3 value for this MassivePersonalRecipientType.
     * 
     * @return key3
     */
    public java.lang.String getKey3() {
        return key3;
    }


    /**
     * Sets the key3 value for this MassivePersonalRecipientType.
     * 
     * @param key3
     */
    public void setKey3(java.lang.String key3) {
        this.key3 = key3;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof MassivePersonalRecipientType)) return false;
        MassivePersonalRecipientType other = (MassivePersonalRecipientType) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true && 
            ((this.number==null && other.getNumber()==null) || 
             (this.number!=null &&
              this.number.equals(other.getNumber()))) &&
            ((this.key1==null && other.getKey1()==null) || 
             (this.key1!=null &&
              this.key1.equals(other.getKey1()))) &&
            ((this.key2==null && other.getKey2()==null) || 
             (this.key2!=null &&
              this.key2.equals(other.getKey2()))) &&
            ((this.key3==null && other.getKey3()==null) || 
             (this.key3!=null &&
              this.key3.equals(other.getKey3())));
        __equalsCalc = null;
        return _equals;
    }

    private boolean __hashCodeCalc = false;
    public synchronized int hashCode() {
        if (__hashCodeCalc) {
            return 0;
        }
        __hashCodeCalc = true;
        int _hashCode = 1;
        if (getNumber() != null) {
            _hashCode += getNumber().hashCode();
        }
        if (getKey1() != null) {
            _hashCode += getKey1().hashCode();
        }
        if (getKey2() != null) {
            _hashCode += getKey2().hashCode();
        }
        if (getKey3() != null) {
            _hashCode += getKey3().hashCode();
        }
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(MassivePersonalRecipientType.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://www.telefonica.es/MI/InterfazSimplificado/schemas", "massivePersonalRecipientType"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("number");
        elemField.setXmlName(new javax.xml.namespace.QName("", "Number"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("key1");
        elemField.setXmlName(new javax.xml.namespace.QName("", "Key1"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("key2");
        elemField.setXmlName(new javax.xml.namespace.QName("", "Key2"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("key3");
        elemField.setXmlName(new javax.xml.namespace.QName("", "Key3"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
    }

    /**
     * Return type metadata object
     */
    public static org.apache.axis.description.TypeDesc getTypeDesc() {
        return typeDesc;
    }

    /**
     * Get Custom Serializer
     */
    public static org.apache.axis.encoding.Serializer getSerializer(
           java.lang.String mechType, 
           java.lang.Class _javaType,  
           javax.xml.namespace.QName _xmlType) {
        return 
          new  org.apache.axis.encoding.ser.BeanSerializer(
            _javaType, _xmlType, typeDesc);
    }

    /**
     * Get Custom Deserializer
     */
    public static org.apache.axis.encoding.Deserializer getDeserializer(
           java.lang.String mechType, 
           java.lang.Class _javaType,  
           javax.xml.namespace.QName _xmlType) {
        return 
          new  org.apache.axis.encoding.ser.BeanDeserializer(
            _javaType, _xmlType, typeDesc);
    }

}
