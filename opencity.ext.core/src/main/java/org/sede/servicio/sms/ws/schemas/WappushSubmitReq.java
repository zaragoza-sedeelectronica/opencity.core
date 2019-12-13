/**
 * WappushSubmitReq.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package org.sede.servicio.sms.ws.schemas;
@SuppressWarnings({"rawtypes", "unused"})
public class WappushSubmitReq  implements java.io.Serializable {
    private VersionType version;

    private byte[] authorization;

    private java.lang.String sender;

    private java.lang.String[] recipients;

    private java.lang.String WAPPushText;

    private org.apache.axis.types.URI WAPPushURL;

    private RelativeOrAbsoluteDateType earliestDeliveryTime;

    private RelativeOrAbsoluteDateType expiryDate;

    private DeliveryReportType deliveryReport;

    private org.apache.axis.types.URI deliveryReportURL;

    public WappushSubmitReq() {
    }

    public WappushSubmitReq(
           VersionType version,
           byte[] authorization,
           java.lang.String sender,
           java.lang.String[] recipients,
           java.lang.String WAPPushText,
           org.apache.axis.types.URI WAPPushURL,
           RelativeOrAbsoluteDateType earliestDeliveryTime,
           RelativeOrAbsoluteDateType expiryDate,
           DeliveryReportType deliveryReport,
           org.apache.axis.types.URI deliveryReportURL) {
           this.version = version;
           this.authorization = authorization;
           this.sender = sender;
           this.recipients = recipients;
           this.WAPPushText = WAPPushText;
           this.WAPPushURL = WAPPushURL;
           this.earliestDeliveryTime = earliestDeliveryTime;
           this.expiryDate = expiryDate;
           this.deliveryReport = deliveryReport;
           this.deliveryReportURL = deliveryReportURL;
    }


    /**
     * Gets the version value for this WappushSubmitReq.
     * 
     * @return version
     */
    public VersionType getVersion() {
        return version;
    }


    /**
     * Sets the version value for this WappushSubmitReq.
     * 
     * @param version
     */
    public void setVersion(VersionType version) {
        this.version = version;
    }


    /**
     * Gets the authorization value for this WappushSubmitReq.
     * 
     * @return authorization
     */
    public byte[] getAuthorization() {
        return authorization;
    }


    /**
     * Sets the authorization value for this WappushSubmitReq.
     * 
     * @param authorization
     */
    public void setAuthorization(byte[] authorization) {
        this.authorization = authorization;
    }


    /**
     * Gets the sender value for this WappushSubmitReq.
     * 
     * @return sender
     */
    public java.lang.String getSender() {
        return sender;
    }


    /**
     * Sets the sender value for this WappushSubmitReq.
     * 
     * @param sender
     */
    public void setSender(java.lang.String sender) {
        this.sender = sender;
    }


    /**
     * Gets the recipients value for this WappushSubmitReq.
     * 
     * @return recipients
     */
    public java.lang.String[] getRecipients() {
        return recipients;
    }


    /**
     * Sets the recipients value for this WappushSubmitReq.
     * 
     * @param recipients
     */
    public void setRecipients(java.lang.String[] recipients) {
        this.recipients = recipients;
    }


    /**
     * Gets the WAPPushText value for this WappushSubmitReq.
     * 
     * @return WAPPushText
     */
    public java.lang.String getWAPPushText() {
        return WAPPushText;
    }


    /**
     * Sets the WAPPushText value for this WappushSubmitReq.
     * 
     * @param WAPPushText
     */
    public void setWAPPushText(java.lang.String WAPPushText) {
        this.WAPPushText = WAPPushText;
    }


    /**
     * Gets the WAPPushURL value for this WappushSubmitReq.
     * 
     * @return WAPPushURL
     */
    public org.apache.axis.types.URI getWAPPushURL() {
        return WAPPushURL;
    }


    /**
     * Sets the WAPPushURL value for this WappushSubmitReq.
     * 
     * @param WAPPushURL
     */
    public void setWAPPushURL(org.apache.axis.types.URI WAPPushURL) {
        this.WAPPushURL = WAPPushURL;
    }


    /**
     * Gets the earliestDeliveryTime value for this WappushSubmitReq.
     * 
     * @return earliestDeliveryTime
     */
    public RelativeOrAbsoluteDateType getEarliestDeliveryTime() {
        return earliestDeliveryTime;
    }


    /**
     * Sets the earliestDeliveryTime value for this WappushSubmitReq.
     * 
     * @param earliestDeliveryTime
     */
    public void setEarliestDeliveryTime(RelativeOrAbsoluteDateType earliestDeliveryTime) {
        this.earliestDeliveryTime = earliestDeliveryTime;
    }


    /**
     * Gets the expiryDate value for this WappushSubmitReq.
     * 
     * @return expiryDate
     */
    public RelativeOrAbsoluteDateType getExpiryDate() {
        return expiryDate;
    }


    /**
     * Sets the expiryDate value for this WappushSubmitReq.
     * 
     * @param expiryDate
     */
    public void setExpiryDate(RelativeOrAbsoluteDateType expiryDate) {
        this.expiryDate = expiryDate;
    }


    /**
     * Gets the deliveryReport value for this WappushSubmitReq.
     * 
     * @return deliveryReport
     */
    public DeliveryReportType getDeliveryReport() {
        return deliveryReport;
    }


    /**
     * Sets the deliveryReport value for this WappushSubmitReq.
     * 
     * @param deliveryReport
     */
    public void setDeliveryReport(DeliveryReportType deliveryReport) {
        this.deliveryReport = deliveryReport;
    }


    /**
     * Gets the deliveryReportURL value for this WappushSubmitReq.
     * 
     * @return deliveryReportURL
     */
    public org.apache.axis.types.URI getDeliveryReportURL() {
        return deliveryReportURL;
    }


    /**
     * Sets the deliveryReportURL value for this WappushSubmitReq.
     * 
     * @param deliveryReportURL
     */
    public void setDeliveryReportURL(org.apache.axis.types.URI deliveryReportURL) {
        this.deliveryReportURL = deliveryReportURL;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof WappushSubmitReq)) return false;
        WappushSubmitReq other = (WappushSubmitReq) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true && 
            ((this.version==null && other.getVersion()==null) || 
             (this.version!=null &&
              this.version.equals(other.getVersion()))) &&
            ((this.authorization==null && other.getAuthorization()==null) || 
             (this.authorization!=null &&
              java.util.Arrays.equals(this.authorization, other.getAuthorization()))) &&
            ((this.sender==null && other.getSender()==null) || 
             (this.sender!=null &&
              this.sender.equals(other.getSender()))) &&
            ((this.recipients==null && other.getRecipients()==null) || 
             (this.recipients!=null &&
              java.util.Arrays.equals(this.recipients, other.getRecipients()))) &&
            ((this.WAPPushText==null && other.getWAPPushText()==null) || 
             (this.WAPPushText!=null &&
              this.WAPPushText.equals(other.getWAPPushText()))) &&
            ((this.WAPPushURL==null && other.getWAPPushURL()==null) || 
             (this.WAPPushURL!=null &&
              this.WAPPushURL.equals(other.getWAPPushURL()))) &&
            ((this.earliestDeliveryTime==null && other.getEarliestDeliveryTime()==null) || 
             (this.earliestDeliveryTime!=null &&
              this.earliestDeliveryTime.equals(other.getEarliestDeliveryTime()))) &&
            ((this.expiryDate==null && other.getExpiryDate()==null) || 
             (this.expiryDate!=null &&
              this.expiryDate.equals(other.getExpiryDate()))) &&
            ((this.deliveryReport==null && other.getDeliveryReport()==null) || 
             (this.deliveryReport!=null &&
              this.deliveryReport.equals(other.getDeliveryReport()))) &&
            ((this.deliveryReportURL==null && other.getDeliveryReportURL()==null) || 
             (this.deliveryReportURL!=null &&
              this.deliveryReportURL.equals(other.getDeliveryReportURL())));
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
        if (getVersion() != null) {
            _hashCode += getVersion().hashCode();
        }
        if (getAuthorization() != null) {
            for (int i=0;
                 i<java.lang.reflect.Array.getLength(getAuthorization());
                 i++) {
                java.lang.Object obj = java.lang.reflect.Array.get(getAuthorization(), i);
                if (obj != null &&
                    !obj.getClass().isArray()) {
                    _hashCode += obj.hashCode();
                }
            }
        }
        if (getSender() != null) {
            _hashCode += getSender().hashCode();
        }
        if (getRecipients() != null) {
            for (int i=0;
                 i<java.lang.reflect.Array.getLength(getRecipients());
                 i++) {
                java.lang.Object obj = java.lang.reflect.Array.get(getRecipients(), i);
                if (obj != null &&
                    !obj.getClass().isArray()) {
                    _hashCode += obj.hashCode();
                }
            }
        }
        if (getWAPPushText() != null) {
            _hashCode += getWAPPushText().hashCode();
        }
        if (getWAPPushURL() != null) {
            _hashCode += getWAPPushURL().hashCode();
        }
        if (getEarliestDeliveryTime() != null) {
            _hashCode += getEarliestDeliveryTime().hashCode();
        }
        if (getExpiryDate() != null) {
            _hashCode += getExpiryDate().hashCode();
        }
        if (getDeliveryReport() != null) {
            _hashCode += getDeliveryReport().hashCode();
        }
        if (getDeliveryReportURL() != null) {
            _hashCode += getDeliveryReportURL().hashCode();
        }
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(WappushSubmitReq.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://www.telefonica.es/MI/InterfazSimplificado/schemas", ">wappushSubmitReq"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("version");
        elemField.setXmlName(new javax.xml.namespace.QName("", "Version"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.telefonica.es/MI/InterfazSimplificado/schemas", "versionType"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("authorization");
        elemField.setXmlName(new javax.xml.namespace.QName("", "Authorization"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "base64Binary"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("sender");
        elemField.setXmlName(new javax.xml.namespace.QName("", "Sender"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("recipients");
        elemField.setXmlName(new javax.xml.namespace.QName("", "Recipients"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setNillable(false);
        elemField.setItemQName(new javax.xml.namespace.QName("", "To"));
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("WAPPushText");
        elemField.setXmlName(new javax.xml.namespace.QName("", "WAPPushText"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("WAPPushURL");
        elemField.setXmlName(new javax.xml.namespace.QName("", "WAPPushURL"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "anyURI"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("earliestDeliveryTime");
        elemField.setXmlName(new javax.xml.namespace.QName("", "EarliestDeliveryTime"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.telefonica.es/MI/InterfazSimplificado/schemas", "relativeOrAbsoluteDateType"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("expiryDate");
        elemField.setXmlName(new javax.xml.namespace.QName("", "ExpiryDate"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.telefonica.es/MI/InterfazSimplificado/schemas", "relativeOrAbsoluteDateType"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("deliveryReport");
        elemField.setXmlName(new javax.xml.namespace.QName("", "DeliveryReport"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.telefonica.es/MI/InterfazSimplificado/schemas", "deliveryReportType"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("deliveryReportURL");
        elemField.setXmlName(new javax.xml.namespace.QName("", "DeliveryReportURL"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "anyURI"));
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
