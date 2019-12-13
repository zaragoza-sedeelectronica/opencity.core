package org.sede.core.rest.view;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;
import org.xml.sax.helpers.DefaultHandler;

public class ParserHtml2MarkDown extends DefaultHandler {

	/**
	 * Texto
	 */
	private StringBuilder texto = new StringBuilder(162);
	@Override
	public void characters(final char[] buf, final int offset, final int len) {

		String txt = new String(buf, offset, len);
		txt = txt.replaceAll("&amp;", "&");
		txt = txt.replaceAll("&", "&amp;");
		txt = txt.replaceAll("&amp;aacute;", "&aacute;");
		txt = txt.replaceAll("&amp;ordm;", "&ordm;");
		txt = txt.replaceAll("&amp;eacute;", "&eacute;");
		txt = txt.replaceAll("&amp;iacute;", "&iacute;");
		txt = txt.replaceAll("&amp;oacute;", "&oacute;");
		txt = txt.replaceAll("&amp;uacute;", "&uacute;");
		txt = txt.replaceAll("&amp;Aacute;", "&Aacute;");
		txt = txt.replaceAll("&amp;Eacute;", "&Eacute;");
		txt = txt.replaceAll("&amp;Iacute;", "&Iacute;");
		txt = txt.replaceAll("&amp;Oacute;", "&Oacute;");
		txt = txt.replaceAll("&amp;Uacute;", "&Uacute;");
		txt = txt.replaceAll("&amp;ntilde;", "&ntilde;");
		txt = txt.replaceAll("<", "&lt;");
		txt = txt.replaceAll(">", "&gt;");
		txt = txt.replaceAll("\"", "&quot;");
		texto.append(txt);

	}
	@Override
	public void error(final SAXParseException excp) throws SAXParseException {
		throw excp;
	}

	public java.lang.StringBuilder getSoloTexto() {
		return texto;
	}

	public void setTexto(final StringBuilder texto) {
		this.texto = texto;
	}
	@Override
	public void startElement(final String namespaceURI, final String lName,
			final String qName, final Attributes attrs) throws SAXException {
		final String eName = qName;
		if ("h1".equals(eName)) {
			getSoloTexto().append("#");
		} else if ("h2".equals(eName)) {
			getSoloTexto().append("##");
		} else if ("h3".equals(eName)) {
			getSoloTexto().append("###");
		} else if ("h4".equals(eName)) {
			getSoloTexto().append("####");
		} else if ("h5".equals(eName)) {
			getSoloTexto().append("#####");
		} else if ("h6".equals(eName)) {
			getSoloTexto().append("######");
		}
	}
	@Override
	public void endElement(final String namespaceURI, final String sName,
			final String eName) throws SAXException {
		if ("h1".equals(eName) 
				|| "h2".equals(eName)
				|| "h3".equals(eName)
				|| "h4".equals(eName)
				|| "h5".equals(eName)
				|| "h6".equals(eName)
				|| "p".equals(eName)
				|| "div".equals(eName)) {
			this.getSoloTexto().append("\n");
		} else if ("li".equals(eName)) {
			this.getSoloTexto().append("\n");
		}
	}

}
