package org.sede.core.utils;

import java.math.BigInteger;

import org.apache.poi.xwpf.usermodel.BreakType;
import org.apache.poi.xwpf.usermodel.ParagraphAlignment;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFParagraph;
import org.apache.poi.xwpf.usermodel.XWPFRun;
import org.apache.poi.xwpf.usermodel.XWPFStyle;
import org.apache.poi.xwpf.usermodel.XWPFStyles;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTDecimalNumber;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTFonts;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTHpsMeasure;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTOnOff;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTPPr;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTRPr;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTString;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTStyle;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.STStyleType;

public class WordUtils {
//	Texto con diferentes estilos en línea http://stackoverflow.com/questions/35419619/how-can-i-set-background-colour-of-a-run-a-word-in-line-or-a-paragraph-in-a-do
	XWPFDocument document;
	private static final String ARIAL = "Arial";
	String heading1 = "Encabezado 1";
    String heading2 = "Encabezado 2";
    String heading3 = "Encabezado 3";   
    String heading4 = "Encabezado 4";
	
	public WordUtils() {
		document = new XWPFDocument();
		XWPFStyles styles = document.createStyles();
	    addCustomHeadingStyle(styles, heading1, 1, 36);
	    addCustomHeadingStyle(styles, heading2, 2, 28);
	    addCustomHeadingStyle(styles, heading3, 3, 24);
	    addCustomHeadingStyle(styles, heading4, 4, 20);
	    
	    CTFonts fonts = CTFonts.Factory.newInstance();
	    fonts.setEastAsia(ARIAL);
	    fonts.setHAnsi(ARIAL);
	    fonts.setAscii(ARIAL);
	    styles.setDefaultFonts(fonts);
		
	}
	public void parrafo(String text) {
		parrafo(text, false, ParagraphAlignment.LEFT);
	}
	public void parrafo(String text, boolean negrita, ParagraphAlignment alineacion) {
		
		XWPFParagraph pagina2 = document.createParagraph();
		pagina2.setAlignment(alineacion);
		XWPFRun run2 = pagina2.createRun();
		run2.setText(text);
		run2.setBold(negrita);
		

	}
	public void nuevaPagina() {
		XWPFParagraph pagina2 = document.createParagraph();
		XWPFRun run2 = pagina2.createRun();
		run2.addBreak(BreakType.PAGE);
	}
	public XWPFDocument getDocument() {
		return document;
	}

	public void setDocument(XWPFDocument document) {
		this.document = document;
	}
	public void h1(String texto) {
		XWPFParagraph paragraph = document.createParagraph();
	    paragraph.setStyle(heading1);
	    XWPFRun run = paragraph.createRun();
	    run.setText(texto);
		
	}
	public void h2(String texto) {
		XWPFParagraph paragraph = document.createParagraph();
	    paragraph.setStyle(heading2);
	    XWPFRun run = paragraph.createRun();
	    run.setText(texto);
		
	}
	public void h3(String texto) {
		XWPFParagraph paragraph = document.createParagraph();
	    paragraph.setStyle(heading3);
	    XWPFRun run = paragraph.createRun();
	    run.setText(texto);
		
	}
	public void h4(String texto) {
		XWPFParagraph paragraph = document.createParagraph();
	    paragraph.setStyle(heading4);
	    XWPFRun run = paragraph.createRun();
	    run.setText(texto);
		
	}
	
	
	private void addCustomHeadingStyle(XWPFStyles styles, String strStyleId, int headingLevel, int pointSize) {

	    CTStyle ctStyle = CTStyle.Factory.newInstance();
	    ctStyle.setStyleId(strStyleId);

	    CTString styleName = CTString.Factory.newInstance();
	    styleName.setVal(strStyleId);
	    ctStyle.setName(styleName);

	    CTDecimalNumber indentNumber = CTDecimalNumber.Factory.newInstance();
	    indentNumber.setVal(BigInteger.valueOf(headingLevel));

	    // lower number > style is more prominent in the formats bar
	    ctStyle.setUiPriority(indentNumber);

	    CTOnOff onoffnull = CTOnOff.Factory.newInstance();
	    ctStyle.setUnhideWhenUsed(onoffnull);

	    // style shows up in the formats bar
	    ctStyle.setQFormat(onoffnull);

	    // style defines a heading of the given level
	    CTPPr ppr = CTPPr.Factory.newInstance();
	    ppr.setOutlineLvl(indentNumber);
	    ctStyle.setPPr(ppr);

	    XWPFStyle style = new XWPFStyle(ctStyle);

	    CTHpsMeasure size = CTHpsMeasure.Factory.newInstance();
	    size.setVal(new BigInteger(String.valueOf(pointSize)));
	    CTHpsMeasure size2 = CTHpsMeasure.Factory.newInstance();
	    size2.setVal(BigInteger.valueOf(24));

	    CTRPr rpr = CTRPr.Factory.newInstance();
	    rpr.setSz(size);
	    rpr.setSzCs(size2);

	    style.getCTStyle().setRPr(rpr);

	    style.setType(STStyleType.PARAGRAPH);
	    styles.addStyle(style);
	}

		
}
