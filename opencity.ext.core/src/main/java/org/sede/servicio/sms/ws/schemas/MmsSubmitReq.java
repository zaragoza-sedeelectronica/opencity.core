/**
 * MmsSubmitReq.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package org.sede.servicio.sms.ws.schemas;
@SuppressWarnings({"rawtypes", "unused"})
public class MmsSubmitReq  implements java.io.Serializable {
    private VersionType version;

    private byte[] authorization;

    private java.lang.String sender;

    private java.lang.String[] recipients;

    private ContentType[] contents;

    private java.lang.String subject;

    private RelativeOrAbsoluteDateType earliestDeliveryTime;

    private RelativeOrAbsoluteDateType expiryDate;

    private DeliveryReportType deliveryReport;

    private org.apache.axis.types.URI deliveryReportURL;

    public MmsSubmitReq() {
    }

    public MmsSubmitReq(
           VersionType version,
           byte[] authorization,
           java.lang.String sender,
           java.lang.String[] recipients,
           ContentType[] contents,
           java.lang.String subject,
           RelativeOrAbsoluteDateType earliestDeliveryTime,
           RelativeOrAbsoluteDateType expiryDate,
           DeliveryReportType deliveryReport,
           org.apache.axis.types.URI deliveryReportURL) {
           this.version = version;
           this.authorization = authorization;
           this.sender = sender;
           this.recipients = recipients;
           this.contents = contents;
           this.subject = subject;
           this.earliestDeliveryTime = earliestDeliveryTime;
           this.expiryDate = expiryDate;
           this.deliveryReport = deliveryReport;
           this.deliveryReportURL = deliveryReportURL;
    }


    /**
     * Gets the version value for this MmsSubmitReq.
     * 
     * @return version
     */
    public VersionType getVersion() {
        return version;
    }


    /**
     * Sets the version value for this MmsSubmitReq.
     * 
     * @param version
     */
    public void setVersion(VersionType version) {
        this.version = version;
    }


    /**
     * Gets the authorization value for this MmsSubmitReq.
     * 
     * @return authorization
     */
    public byte[] getAuthorization() {
        return authorization;
    }


    /**
     * Sets the authorization value for this MmsSubmitReq.
     * 
     * @param authorization
     */
    public void setAuthorization(byte[] authorization) {
        this.authorization = authorization;
    }


    /**
     * Gets the sender value for this MmsSubmitReq.
     * 
     * @return sender
     */
    public java.lang.String getSender() {
        return sender;
    }


    /**
     * Sets the sender value for this MmsSubmitReq.
     * 
     * @param sender
     */
    public void setSender(java.lang.String sender) {
        this.sender = sender;
    }


    /**
     * Gets the recipients value for this MmsSubmitReq.
     * 
     * @return recipients
     */
    public java.lang.String[] getRecipients() {
        return recipients;
    }


    /**
     * Sets the recipients value for this MmsSubmitReq.
     * 
     * @param recipients
     */
    public void setRecipients(java.lang.String[] recipients) {
        this.recipients = recipients;
    }


    /**
     * Gets the contents value for this MmsSubmitReq.
     * 
     * @return contents
     */
    public ContentType[] getContents() {
        return contents;
    }


    /**
     * Sets the contents value for this MmsSubmitReq.
     * 
     * @param contents
     */
    public void setContents(ContentType[] contents) {
        this.contents = contents;
    }


    /**
     * Gets the subject value for this MmsSubmitReq.
     * 
     * @return subject
     */
    public java.lang.String getSubject() {
        return subject;
    }


    /**
     * Sets the subject value for this MmsSubmitReq.
     * 
     * @param subject
     */
    public void setSubject(java.lang.String subject) {
        this.subject = subject;
    }


    /**
     * Gets the earliestDeliveryTime value for this MmsSubmitReq.
     * 
     * @return earliestDeliveryTime
     */
    public RelativeOrAbsoluteDateType getEarliestDeliveryTime() {
        return earliestDeliveryTime;
    }


    /**
     * Sets the earliestDeliveryTime value for this MmsSubmitReq.
     * 
     * @param earliestDeliveryTime
     */
    public void setEarliestDeliveryTime(RelativeOrAbsoluteDateType earliestDeliveryTime) {
        this.earliestDeliveryTime = earliestDeliveryTime;
    }


    /**
     * Gets the expiryDate value for this MmsSubmitReq.
     * 
     * @return expiryDate
     */
    public RelativeOrAbsoluteDateType getExpiryDate() {
        return expiryDate;
    }


    /**
     * Sets the expiryDate value for this MmsSubmitReq.
     * 
     * @param expiryDate
     */
    public void setExpiryDate(RelativeOrAbsoluteDateType expiryDate) {
        this.expiryDate = expiryDate;
    }


    /**
     * Gets the deliveryReport value for this MmsSubmitReq.
     * 
     * @return deliveryReport
     */
    public DeliveryReportType getDeliveryReport() {
        return deliveryReport;
    }


    /**
     * Sets the deliveryReport value for this MmsSubmitReq.
     * 
     * @param deliveryReport
     */
    public void setDeliveryReport(DeliveryReportType deliveryReport) {
        this.deliveryReport = deliveryReport;
    }


    /**
     * Gets the deliveryReportURL value for this MmsSubmitReq.
     * 
     * @return deliveryReportURL
     */
    public org.apache.axis.types.URI getDeliveryReportURL() {
        return deliveryReportURL;
    }


    /**
     * Sets the deliveryReportURL value for this MmsSubmitReq.
     * 
     * @param deliveryReportURL
     */
    public void setDeliveryReportURL(org.apache.axis.types.URI deliveryReportURL) {
        this.deliveryReportURL = deliveryReportURL;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof MmsSubmitReq)) return false;
        MmsSubmitReq other = (MmsSubmitReq) obj;
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
            ((this.contents==null && other.getContents()==null) || 
             (this.contents!=null &&
              java.util.Arrays.equals(this.contents, other.getContents()))) &&
            ((this.subject==null && other.getSubject()==null) || 
             (this.subject!=null &&
              this.subject.equals(other.getSubject()))) &&
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
        if (getContents() != null) {
            for (int i=0;
                 i<java.lang.reflect.Array.getLength(getContents());
                 i++) {
                java.lang.Object obj = java.lang.reflect.Array.get(getContents(), i);
                if (obj != null &&
                    !obj.getClass().isArray()) {
                    _hashCode += obj.hashCode();
                }
            }
        }
        if (getSubject() != null) {
            _hashCode += getSubject().hashCode();
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
        new org.apache.axis.description.TypeDesc(MmsSubmitReq.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://www.telefonica.es/MI/InterfazSimplificado/schemas", ">mmsSubmitReq"));
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
        elemField.setFieldName("contents");
        elemField.setXmlName(new javax.xml.namespace.QName("", "Contents"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.telefonica.es/MI/InterfazSimplificado/schemas", "contentType"));
        elemField.setNillable(false);
        elemField.setItemQName(new javax.xml.namespace.QName("", "Content"));
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("subject");
        elemField.setXmlName(new javax.xml.namespace.QName("", "Subject"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
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
