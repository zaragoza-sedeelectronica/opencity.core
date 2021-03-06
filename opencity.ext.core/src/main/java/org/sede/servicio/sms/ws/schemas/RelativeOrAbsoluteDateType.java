/**
 * RelativeOrAbsoluteDateType.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package org.sede.servicio.sms.ws.schemas;


/**
 * Absolute and relative time
 */
@SuppressWarnings({"rawtypes", "unused"})
public class RelativeOrAbsoluteDateType  implements java.io.Serializable, org.apache.axis.encoding.SimpleType {
    private java.lang.String _value;
    public RelativeOrAbsoluteDateType() {
    }

    // Simple Types must have a String constructor
    public RelativeOrAbsoluteDateType(java.lang.String _value) {
        this._value = _value;
    }
    public RelativeOrAbsoluteDateType(java.util.Calendar _value) {
        setDateTimeValue(_value);
    }

    public RelativeOrAbsoluteDateType(org.apache.axis.types.Duration _value) {
        setDurationValue(_value);
    }

    // Simple Types must have a toString for serializing the value
    public java.lang.String toString() {
        return _value;
    }


    /**
     * Gets the dateTimeValue value for this RelativeOrAbsoluteDateType.
     * 
     * @return dateTimeValue
     */
    public java.util.Calendar getDateTimeValue() {
        return (java.util.Calendar) new org.apache.axis.encoding.ser.CalendarDeserializer(
                java.lang.String.class, org.apache.axis.Constants.XSD_STRING).makeValue(_value);
    }


    /**
     * Sets the _value value for this RelativeOrAbsoluteDateType.
     * 
     * @param _value
     */
    public void setDateTimeValue(java.util.Calendar _value) {
        this._value = _value == null ? null : new org.apache.axis.encoding.ser.CalendarSerializer().getValueAsString(_value, null);
    }


    /**
     * Gets the durationValue value for this RelativeOrAbsoluteDateType.
     * 
     * @return durationValue
     */
    public org.apache.axis.types.Duration getDurationValue() {
        return new org.apache.axis.types.Duration(_value);
    }


    /**
     * Sets the _value value for this RelativeOrAbsoluteDateType.
     * 
     * @param _value
     */
    public void setDurationValue(org.apache.axis.types.Duration _value) {
        this._value = _value == null ? null : _value.toString();
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof RelativeOrAbsoluteDateType)) return false;
        RelativeOrAbsoluteDateType other = (RelativeOrAbsoluteDateType) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true &&  this.toString().equals(obj.toString());
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
        if (this._value != null) {
            _hashCode += this._value.hashCode();
        }
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(RelativeOrAbsoluteDateType.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://www.telefonica.es/MI/InterfazSimplificado/schemas", "relativeOrAbsoluteDateType"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("dateTimeValue");
        elemField.setXmlName(new javax.xml.namespace.QName("", "dateTimeValue"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "dateTime"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("durationValue");
        elemField.setXmlName(new javax.xml.namespace.QName("", "durationValue"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "duration"));
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
          new  org.apache.axis.encoding.ser.SimpleSerializer(
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
          new  org.apache.axis.encoding.ser.SimpleDeserializer(
            _javaType, _xmlType, typeDesc);
    }

}
