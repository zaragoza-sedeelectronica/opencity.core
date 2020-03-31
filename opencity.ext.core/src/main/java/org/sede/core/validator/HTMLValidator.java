package org.sede.core.validator;


import java.io.IOException;
import java.io.StringReader;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.sede.core.anotaciones.ValidHTML;
import org.sede.core.exception.HtmlErrorHandler;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;


public class HTMLValidator implements ConstraintValidator<ValidHTML, String> {
	
	private static final String ENTIDADES ="<!ENTITY nbsp   \"&#160;\">"
					    + "<!ENTITY iexcl  \"&#161;\">"
					    + "<!ENTITY cent   \"&#162;\">"
					    + "<!ENTITY pound  \"&#163;\">"
					    + "<!ENTITY curren \"&#164;\">"
					    + "<!ENTITY yen    \"&#165;\">"
					    + "<!ENTITY brvbar \"&#166;\">"
					    + "<!ENTITY sect   \"&#167;\">"
					    + "<!ENTITY uml    \"&#168;\">"
					    + "<!ENTITY copy   \"&#169;\">"
					    + "<!ENTITY ordf   \"&#170;\">"
					    + "<!ENTITY laquo  \"&#171;\">"
					    + "<!ENTITY not    \"&#172;\">"
					    + "<!ENTITY shy    \"&#173;\">"
					    + "<!ENTITY reg    \"&#174;\">"
					    + "<!ENTITY macr   \"&#175;\">"
					    + "<!ENTITY deg    \"&#176;\">"
					    + "<!ENTITY plusmn \"&#177;\">"
					    + "<!ENTITY sup2   \"&#178;\">"
					    + "<!ENTITY sup3   \"&#179;\">"
					    + "<!ENTITY acute  \"&#180;\">"
					    + "<!ENTITY micro  \"&#181;\">"
					    + "<!ENTITY para   \"&#182;\">"
					    + "<!ENTITY middot \"&#183;\">"
					    + "<!ENTITY cedil  \"&#184;\">"
					    + "<!ENTITY sup1   \"&#185;\">"
					    + "<!ENTITY ordm   \"&#186;\">"
					    + "<!ENTITY raquo  \"&#187;\">"
					    + "<!ENTITY frac14 \"&#188;\">"
					    + "<!ENTITY frac12 \"&#189;\">"
					    + "<!ENTITY frac34 \"&#190;\">"
					    + "<!ENTITY iquest \"&#191;\">"
					    + "<!ENTITY Agrave \"&#192;\">"
					    + "<!ENTITY Aacute \"&#193;\">"
					    + "<!ENTITY Acirc  \"&#194;\">"
					    + "<!ENTITY Atilde \"&#195;\">"
					    + "<!ENTITY Auml   \"&#196;\">"
					    + "<!ENTITY Aring  \"&#197;\">"
					    + "<!ENTITY AElig  \"&#198;\">"
					    + "<!ENTITY Ccedil \"&#199;\">"
					    + "<!ENTITY Egrave \"&#200;\">"
					    + "<!ENTITY Eacute \"&#201;\">"
					    + "<!ENTITY Ecirc  \"&#202;\">"
					    + "<!ENTITY Euml   \"&#203;\">"
					    + "<!ENTITY Igrave \"&#204;\">"
					    + "<!ENTITY Iacute \"&#205;\">"
					    + "<!ENTITY Icirc  \"&#206;\">"
					    + "<!ENTITY Iuml   \"&#207;\">"
					    + "<!ENTITY ETH    \"&#208;\">"
					    + "<!ENTITY Ntilde \"&#209;\">"
					    + "<!ENTITY Ograve \"&#210;\">"
					    + "<!ENTITY Oacute \"&#211;\">"
					    + "<!ENTITY Ocirc  \"&#212;\">"
					    + "<!ENTITY Otilde \"&#213;\">"
					    + "<!ENTITY Ouml   \"&#214;\">"
					    + "<!ENTITY times  \"&#215;\">"
					    + "<!ENTITY Oslash \"&#216;\">"
					    + "<!ENTITY Ugrave \"&#217;\">"
					    + "<!ENTITY Uacute \"&#218;\">"
					    + "<!ENTITY Ucirc  \"&#219;\">"
					    + "<!ENTITY Uuml   \"&#220;\">"
					    + "<!ENTITY Yacute \"&#221;\">"
					    + "<!ENTITY THORN  \"&#222;\">"
					    + "<!ENTITY szlig  \"&#223;\">"
					    + "<!ENTITY agrave \"&#224;\">"
					    + "<!ENTITY aacute \"&#225;\">"
					    + "<!ENTITY acirc  \"&#226;\">"
					    + "<!ENTITY atilde \"&#227;\">"
					    + "<!ENTITY auml   \"&#228;\">"
					    + "<!ENTITY aring  \"&#229;\">"
					    + "<!ENTITY aelig  \"&#230;\">"
					    + "<!ENTITY ccedil \"&#231;\">"
					    + "<!ENTITY egrave \"&#232;\">"
					    + "<!ENTITY eacute \"&#233;\">"
					    + "<!ENTITY ecirc  \"&#234;\">"
					    + "<!ENTITY euml   \"&#235;\">"
					    + "<!ENTITY igrave \"&#236;\">"
					    + "<!ENTITY iacute \"&#237;\">"
					    + "<!ENTITY icirc  \"&#238;\">"
					    + "<!ENTITY iuml   \"&#239;\">"
					    + "<!ENTITY eth    \"&#240;\">"
					    + "<!ENTITY ntilde \"&#241;\">"
					    + "<!ENTITY ograve \"&#242;\">"
					    + "<!ENTITY oacute \"&#243;\">"
					    + "<!ENTITY ocirc  \"&#244;\">"
					    + "<!ENTITY otilde \"&#245;\">"
					    + "<!ENTITY ouml   \"&#246;\">"
					    + "<!ENTITY divide \"&#247;\">"
					    + "<!ENTITY oslash \"&#248;\">"
					    + "<!ENTITY ugrave \"&#249;\">"
					    + "<!ENTITY uacute \"&#250;\">"
					    + "<!ENTITY ucirc  \"&#251;\">"
					    + "<!ENTITY uuml   \"&#252;\">"
					    + "<!ENTITY yacute \"&#253;\">"
					    + "<!ENTITY thorn  \"&#254;\">"
					    + "<!ENTITY yuml   \"&#255;\">"
					    + "<!ENTITY bull   \"&#8226;\">"
					    + "<!ENTITY euro   \"&#8364;\">";
	public boolean isValid(String object,
			ConstraintValidatorContext constraintContext) {

		if (object == null || "".equals(object)) {
			return true;
		}

		try {
			SAXParserFactory factory = SAXParserFactory.newInstance();
			factory.setValidating(true);
			factory.setNamespaceAware(true);
	
			SAXParser parser = factory.newSAXParser();
			
			XMLReader reader = parser.getXMLReader();
			reader.setErrorHandler(new HtmlErrorHandler());
			reader.parse(new InputSource(new StringReader("<?xml version=\"1.0\" encoding=\"ISO-8859-1\"?>"
					// validar sólo con dtds locales
					+ "<!DOCTYPE html[" + ENTIDADES + "]>"+
					"<html xmlns=\"http://www.w3.org/1999/xhtml\" xml:lang=\"es\"><head><title>Ejemplo</title></head><body>" + object + "</body></html>")));
		} catch (ParserConfigurationException e) {
			constraintContext.disableDefaultConstraintViolation();
			constraintContext.buildConstraintViolationWithTemplate(e.getLocalizedMessage().replaceAll("\"", "'")).addConstraintViolation();
			return false;
		} catch (SAXException e) {
			constraintContext.disableDefaultConstraintViolation();
			constraintContext.buildConstraintViolationWithTemplate(e.getLocalizedMessage().replaceAll("\"", "'")).addConstraintViolation();
			return false;
		} catch (IOException e) {
			constraintContext.disableDefaultConstraintViolation();
			constraintContext.buildConstraintViolationWithTemplate(e.getLocalizedMessage().replaceAll("\"", "'")).addConstraintViolation();
			return false;
		}
		return true;
	}

	public void validate(String object) throws SAXException, ParserConfigurationException {
		SAXParserFactory factory = SAXParserFactory.newInstance();
		factory.setValidating(true);
		factory.setNamespaceAware(true);

		SAXParser parser = factory.newSAXParser();
		
		XMLReader reader = parser.getXMLReader();
		reader.setErrorHandler(new HtmlErrorHandler());
		object = object.replace("<!DOCTYPE html>", "");
		
		if (object.indexOf("<html") < 0) {
			object = "<html xmlns=\"http://www.w3.org/1999/xhtml\" xmlns:th=\"http://www.thymeleaf.org\" xmlns:sede=\"http://www.zaragoza.es\" lang=\"es\">"
					+ object 
					+ "</html>";
		}
		
		object = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>"
				+ "<!DOCTYPE html[" + ENTIDADES + "]>"
			    + object;
		try {
			reader.parse(new InputSource(new StringReader(object)));
		} catch (IOException e) {
			throw new SAXException(e);
		}
	}
	
	public void initialize(ValidHTML etiqueta) {

	}
}
