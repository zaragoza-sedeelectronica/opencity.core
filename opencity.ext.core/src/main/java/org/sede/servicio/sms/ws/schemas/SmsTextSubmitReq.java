/**
 * SmsTextSubmitReq.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package org.sede.servicio.sms.ws.schemas;
@SuppressWarnings({"rawtypes", "unused"})
public class SmsTextSubmitReq  implements java.io.Serializable {
    private VersionType version;

    private byte[] authorization;

    private java.lang.String sender;

    private java.lang.String[] recipients;

    private java.lang.String SMSText;

    private RelativeOrAbsoluteDateType earliestDeliveryTime;

    private RelativeOrAbsoluteDateType expiryDate;

    private DeliveryReportType deliveryReport;

    private org.apache.axis.types.URI deliveryReportURL;

    private SmsClassType SMSClass;

    public SmsTextSubmitReq() {
    }

    public SmsTextSubmitReq(
           VersionType version,
           byte[] authorization,
           java.lang.String sender,
           java.lang.String[] recipients,
           java.lang.String SMSText,
           RelativeOrAbsoluteDateType earliestDeliveryTime,
           RelativeOrAbsoluteDateType expiryDate,
           DeliveryReportType deliveryReport,
           org.apache.axis.types.URI deliveryReportURL,
           SmsClassType SMSClass) {
           this.version = version;
           this.authorization = authorization;
           this.sender = sender;
           this.recipients = recipients;
           this.SMSText = SMSText;
           this.earliestDeliveryTime = earliestDeliveryTime;
           this.expiryDate = expiryDate;
           this.deliveryReport = deliveryReport;
           this.deliveryReportURL = deliveryReportURL;
           this.SMSClass = SMSClass;
    }


    /**
     * Gets the version value for this SmsTextSubmitReq.
     * 
     * @return version
     */
    public VersionType getVersion() {
        return version;
    }


    /**
     * Sets the version value for this SmsTextSubmitReq.
     * 
     * @param version
     */
    public void setVersion(VersionType version) {
        this.version = version;
    }


    /**
     * Gets the authorization value for this SmsTextSubmitReq.
     * 
     * @return authorization
     */
    public byte[] getAuthorization() {
        return authorization;
    }


    /**
     * Sets the authorization value for this SmsTextSubmitReq.
     * 
     * @param authorization
     */
    public void setAuthorization(byte[] authorization) {
        this.authorization = authorization;
    }


    /**
     * Gets the sender value for this SmsTextSubmitReq.
     * 
     * @return sender
     */
    public java.lang.String getSender() {
        return sender;
    }


    /**
     * Sets the sender value for this SmsTextSubmitReq.
     * 
     * @param sender
     */
    public void setSender(java.lang.String sender) {
        this.sender = sender;
    }


    /**
     * Gets the recipients value for this SmsTextSubmitReq.
     * 
     * @return recipients
     */
    public java.lang.String[] getRecipients() {
        return recipients;
    }


    /**
     * Sets the recipients value for this SmsTextSubmitReq.
     * 
     * @param recipients
     */
    public void setRecipients(java.lang.String[] recipients) {
        this.recipients = recipients;
    }


    /**
     * Gets the SMSText value for this SmsTextSubmitReq.
     * 
     * @return SMSText
     */
    public java.lang.String getSMSText() {
        return SMSText;
    }


    /**
     * Sets the SMSText value for this SmsTextSubmitReq.
     * 
     * @param SMSText
     */
    public void setSMSText(java.lang.String SMSText) {
        this.SMSText = SMSText;
    }


    /**
     * Gets the earliestDeliveryTime value for this SmsTextSubmitReq.
     * 
     * @return earliestDeliveryTime
     */
    public RelativeOrAbsoluteDateType getEarliestDeliveryTime() {
        return earliestDeliveryTime;
    }


    /**
     * Sets the earliestDeliveryTime value for this SmsTextSubmitReq.
     * 
     * @param earliestDeliveryTime
     */
    public void setEarliestDeliveryTime(RelativeOrAbsoluteDateType earliestDeliveryTime) {
        this.earliestDeliveryTime = earliestDeliveryTime;
    }


    /**
     * Gets the expiryDate value for this SmsTextSubmitReq.
     * 
     * @return expiryDate
     */
    public RelativeOrAbsoluteDateType getExpiryDate() {
        return expiryDate;
    }


    /**
     * Sets the expiryDate value for this SmsTextSubmitReq.
     * 
     * @param expiryDate
     */
    public void setExpiryDate(RelativeOrAbsoluteDateType expiryDate) {
        this.expiryDate = expiryDate;
    }


    /**
     * Gets the deliveryReport value for this SmsTextSubmitReq.
     * 
     * @return deliveryReport
     */
    public DeliveryReportType getDeliveryReport() {
        return deliveryReport;
    }


    /**
     * Sets the deliveryReport value for this SmsTextSubmitReq.
     * 
     * @param deliveryReport
     */
    public void setDeliveryReport(DeliveryReportType deliveryReport) {
        this.deliveryReport = deliveryReport;
    }


    /**
     * Gets the deliveryReportURL value for this SmsTextSubmitReq.
     * 
     * @return deliveryReportURL
     */
    public org.apache.axis.types.URI getDeliveryReportURL() {
        return deliveryReportURL;
    }


    /**
     * Sets the deliveryReportURL value for this SmsTextSubmitReq.
     * 
     * @param deliveryReportURL
     */
    public void setDeliveryReportURL(org.apache.axis.types.URI deliveryReportURL) {
        this.deliveryReportURL = deliveryReportURL;
    }


    /**
     * Gets the SMSClass value for this SmsTextSubmitReq.
     * 
     * @return SMSClass
     */
    public SmsClassType getSMSClass() {
        return SMSClass;
    }


    /**
     * Sets the SMSClass value for this SmsTextSubmitReq.
     * 
     * @param SMSClass
     */
    public void setSMSClass(SmsClassType SMSClass) {
        this.SMSClass = SMSClass;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof SmsTextSubmitReq)) return false;
        SmsTextSubmitReq other = (SmsTextSubmitReq) obj;
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
            ((this.SMSText==null && other.getSMSText()==null) || 
             (this.SMSText!=null &&
              this.SMSText.equals(other.getSMSText()))) &&
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
              this.deliveryReportURL.equals(other.getDeliveryReportURL()))) &&
            ((this.SMSClass==null && other.getSMSClass()==null) || 
             (this.SMSClass!=null &&
              this.SMSClass.equals(other.getSMSClass())));
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
        if (getSMSText() != null) {
            _hashCode += getSMSText().hashCode();
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
        if (getSMSClass() != null) {
            _hashCode += getSMSClass().hashCode();
        }
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(SmsTextSubmitReq.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://www.telefonica.es/MI/InterfazSimplificado/schemas", ">smsTextSubmitReq"));
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
        elemField.setFieldName("SMSText");
        elemField.setXmlName(new javax.xml.namespace.QName("", "SMSText"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
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
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("SMSClass");
        elemField.setXmlName(new javax.xml.namespace.QName("", "SMSClass"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.telefonica.es/MI/InterfazSimplificado/schemas", "smsClassType"));
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
