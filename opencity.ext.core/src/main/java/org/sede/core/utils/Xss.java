package org.sede.core.utils;

public class Xss {
	/**
	 * Constructor Privado para singleton
	 */
	private Xss() {
		super();
		
	}
	/**
	 * 
	 * @param dato String a validar
	 * @return string modificado sin problemas de XSS
	 * @throws CadenaInvalida Error Xss
	 */
	public static String comprobar(final String dato) {
		//Caraceres a validar "'", " + "
		//Mirar posibilidad de cambiar los caracteres < por &gt;
		if (dato != null) {
			if (dato.toLowerCase().indexOf("<script") >= 0 || dato.toLowerCase().indexOf("javascript:") >= 0) {
				throw new IllegalArgumentException("Error en los valores de entrada");	
			}	
		
			final int len = dato.length();
			final StringBuilder dest = new StringBuilder();
			for (int i = 0; i < (len); i++) {
				dest.append(Xss.comprobarCaracter(dato.charAt(i)));
			}
			return dest.toString();
		} else {
			return null;
		}
	}
	/**
	 * 
	 * @param dato String a validar
	 * @return string modificado sin problemas de XSS
	 * @throws CadenaInvalida Error Xss
	 */
	
	/**
	 * 
	 * @param dato String a validar en el caso de que no sea nulo
	 * @return string modificado sin problemas de XSS
	 * @throws CadenaInvalida Error Xss
	 */
	public static String comprobarNulo(final String dato) {
		String retorno;
		if (dato == null) {
			retorno = "";
		} else {
			retorno = Xss.comprobar(dato);
		}
		return retorno;
	}
	/**
	 * 
	 * @param valores Array de valores a comprobar
	 * @return string modificado sin problemas de XSS
	 * @throws CadenaInvalida Error Xss
	 */
	public static String[] comprobar(final String[] valores) {
		String[] nuevo = new String[valores.length];
		for (int i = 0; i < valores.length; i++) {
			nuevo[i] = Xss.comprobar(valores[i]);
		}
		return nuevo;
	}
	/**
	 * 
	 * @param dato String a validar en el caso de que no sea nulo
	 * @return string modificado sin problemas de XSS
	 * @throws CadenaInvalida Error Xss
	 */
	public static String toXSS(final String dato) {
		//Caraceres a validar "'", " + "
		//Mirar posibilidad de cambiar los caracteres < por &gt;
		final int len = dato.length();
		final StringBuilder dest = new StringBuilder(len);
		for (int i = 0; i < (len); i++) {
			dest.append(Xss.comprobarCaracter(dato.charAt(i)));
			
		}
		return dest.toString();
	}
	/**
	 * Convierte las entidades HTML de un texto a su equivalente en ASCII
	 * @param texto texto a convertir
	 * @return texto sin entidades html
	 */
	public static String pasarPdf(final String texto) {
		String txt = texto;
		txt = txt.replaceAll("&#38;", "&");
		txt = txt.replaceAll("&#40;", "(");
		txt = txt.replaceAll("&#41;", ")");
		txt = txt.replaceAll("&#35;", "#");
		return txt;
	}
	
	/**
	 * 
	 * @param dato String a validar
	 * @return string modificado sin problemas de XSS
	 * @throws CadenaInvalida Error Xss
	 */
	public static String comprobarParentesisValidos(final String dato) {
		final int len = dato.length();
		final StringBuilder dest = new StringBuilder(len);
		for (int i = 0; i < (len); i++) {
			final char caracter = dato.charAt(i);
			if (caracter == '\'') {
				dest.append("''");	
			} else if (caracter == '<') {
				dest.append("&lt;");
			} else if (caracter == '>') {
				dest.append("&gt;");
			} else if (caracter == '&') {
				dest.append("&#38;");
			} else if (caracter == '#') {
				dest.append("&#35;");
			} else if (caracter == '%') {
				dest.append("");
			} else {
				dest.append(caracter);
			}
		}
		return dest.toString();
	}
	/**
	 * Devuelve la Entidad HTML asociada a un carácter
	 * @param caracter caracter a comprobar
	 * @return StringBuilder modificado
	 */
	private static StringBuilder comprobarCaracter(final char caracter) {		
		final StringBuilder dest = new StringBuilder();
		//TODO mirar como comprobar xss!!!
		dest.append(caracter);
	/*	if (caracter == '\'') {
			dest.append("''");
		} else if (caracter == '(') {
			dest.append("&#40;");
		} else if (caracter == ')') {
			dest.append("&#41;");
		} else if (caracter == '<') {
			dest.append("&lt;");
		} else if (caracter == '>') {
			dest.append("&gt;");
		} else if (caracter == '&') {
			dest.append("&#38;");
		} else if (caracter == '#') {
			dest.append("&#35;");
		} else if (caracter == '%') {
			dest.append("");
		} else {
			dest.append(caracter);
		}*/
		return dest;
	}
}
