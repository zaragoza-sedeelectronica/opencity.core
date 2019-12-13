/**
 * DeliveryReportType.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package org.sede.servicio.sms.ws.schemas;
@SuppressWarnings({"rawtypes", "unchecked"})
public class DeliveryReportType implements java.io.Serializable {
    private java.lang.String _value_;
    private static java.util.HashMap _table_ = new java.util.HashMap();

    // Constructor
    protected DeliveryReportType(java.lang.String value) {
        _value_ = value;
        _table_.put(_value_,this);
    }

    public static final java.lang.String _All = "All";
    public static final java.lang.String _Failure = "Failure";
    public static final java.lang.String _Success = "Success";
    public static final java.lang.String _None = "None";
    public static final DeliveryReportType All = new DeliveryReportType(_All);
    public static final DeliveryReportType Failure = new DeliveryReportType(_Failure);
    public static final DeliveryReportType Success = new DeliveryReportType(_Success);
    public static final DeliveryReportType None = new DeliveryReportType(_None);
    public java.lang.String getValue() { return _value_;}
    public static DeliveryReportType fromValue(java.lang.String value) {
        DeliveryReportType enumeration = (DeliveryReportType)
            _table_.get(value);
        if (enumeration==null) throw new java.lang.IllegalArgumentException();
        return enumeration;
    }
    public static DeliveryReportType fromString(java.lang.String value) {
        return fromValue(value);
    }
    public boolean equals(java.lang.Object obj) {return (obj == this);}
    public int hashCode() { return toString().hashCode();}
    public java.lang.String toString() { return _value_;}
    public java.lang.Object readResolve() throws java.io.ObjectStreamException { return fromValue(_value_);}
    public static org.apache.axis.encoding.Serializer getSerializer(
           java.lang.String mechType, 
           java.lang.Class _javaType,  
           javax.xml.namespace.QName _xmlType) {
        return 
          new org.apache.axis.encoding.ser.EnumSerializer(
            _javaType, _xmlType);
    }
    public static org.apache.axis.encoding.Deserializer getDeserializer(
           java.lang.String mechType, 
           java.lang.Class _javaType,  
           javax.xml.namespace.QName _xmlType) {
        return 
          new org.apache.axis.encoding.ser.EnumDeserializer(
            _javaType, _xmlType);
    }
    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(DeliveryReportType.class);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://www.telefonica.es/MI/InterfazSimplificado/schemas", "deliveryReportType"));
    }
    /**
     * Return type metadata object
     */
    public static org.apache.axis.description.TypeDesc getTypeDesc() {
        return typeDesc;
    }

}
