package org.sede.core.validator;

import java.util.Locale;

import org.apache.commons.validator.routines.EmailValidator;
import org.apache.commons.validator.routines.UrlValidator;
import org.apache.commons.validator.routines.checkdigit.IBANCheckDigit;

public class Utils {
	private Utils() {
		super();
	}
	public static boolean isEmail(String email) {
		return EmailValidator.getInstance().isValid(email);
    }
	public static boolean isNIF(String strNif) {
        final char[] arrLetra = new char[]{'T','R','W','A','G','M','Y','F','P','D','X','B','N','J','Z','S','Q','V','H','L','C','K','E'};
        
        if (strNif.length() != 9) return false;
        strNif = strNif.toUpperCase();
        int n;
        
        try {
            n = Integer.parseInt(strNif.substring(0, 8));
        } catch (NumberFormatException ex) {
            return false;
        }
        
        char letra = strNif.charAt(8);
        int position = n % 23;
        return arrLetra[position] == letra;
    }
	public static boolean isCif (final String ncif) {
		//	Devuelve verdadero si el cif que se le pasa es valido
		boolean respuesta = false; //Tabla de caracter de control para cif extranjeros, organismos estatales y locales
		//		en realidad la "J" debe estar en la ultima posición pero como
		final char[] tabla = {'J', 'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I'}; 
		int sumap = 0; //las tablas en java cominezan en '0' la paso a la primera posicion
		int sumai = 0;
		int pre;
		int red;
		int digcon;

		final String cif = ncif.toUpperCase(new Locale("es", "ES"));
		try {
			sumap = Integer.parseInt(cif.substring(2, 3)) + Integer.parseInt(cif.substring(4, 5)) 
								+ Integer.parseInt(cif.substring(6, 7));
			for (int i = 1; i <= 8; i++) {
				pre = 2 * Integer.parseInt(cif.substring(i, i + 1));
				if (pre > 9) {
					sumai += (pre / 10) + (pre % 10);
				} else {
					sumai += pre;
				}
				i++;
			}
			red = sumap + sumai; //R es el resulrtado de las sumas de los productos
			digcon = red % 10; //calculamos el digito de control que es el modulo de la suma de los productos
			digcon = 10 - digcon; //complemento a 10 del digito de control
			if (digcon == 10) { //si el digito de control es 10 se le asigna el 0
				digcon = 0;
			}

			if (Character.isLetter(cif.charAt(8))) { 
				// si es un caracter el ultimo digito se compara con la tabla que debe ocupar la posicion del dc
				if (tabla[digcon] == cif.charAt(8)) {
					respuesta = true;
				}
			} else { //si no es el caracter de contro que debe coincidir con el ultimo digito
				if (digcon == Integer.parseInt(cif.substring(8, 9))) {
					respuesta = true;
				}
			}
		} catch (Exception e) {
			return false;
		}
		return respuesta;
	}
    //Comprueba que un NIE sea correcto.
    public static boolean isNIE(String strNie) {
        if (strNie.length() < 9 || strNie.length() > 10) return false;
        char x = strNie.charAt(0);
        if (x != 'X' && x != 'Y' && x != 'Z') return false;
        String nif=null;
        if (x == 'X'){
            nif = /*"0" + */ strNie.substring(1,strNie.length());
            if (nif.length() == 8) {
                nif = "0" + nif;
            }
        }
        if (x == 'Y'){
            nif = /*"1" + */ strNie.substring(1,strNie.length());
            if (nif.length() == 8) {
                nif = "1" + nif;
            }
        }
        if (x == 'Z'){
            nif = /*"2" + */ strNie.substring(1,strNie.length());
            if (nif.length() == 8) {
                nif = "2" + nif;
            }
        }
        return isNIF(nif);
    }
	public static boolean isUrl(String url) {
		return UrlValidator.getInstance().isValid(url);
	}
	public static boolean isIban(String iban) {
		IBANCheckDigit c = new IBANCheckDigit();
		return c.isValid(iban);
	}
	public static boolean isNumeroEntero(String numero) {
		try {
			if((Integer.parseInt(numero)%1!=0)){
				return false;
			}else {
				return true;
			}
		}catch(Exception e) {
			return false;
		}
		
	}
}
