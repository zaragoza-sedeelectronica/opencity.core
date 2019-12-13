package org.sede.core.utils;

import java.util.Random;

public class PasswordGenerator {

	/** Letras mayusculas **/
	private static final	String[]    upper	= {"A", "B", "C", "D", "E", "F", "G", "H", "I", "J", "K", 
"L", "M", "N", "O", "P", "Q", "R", "S", "T", "U", "V", "W", "X", "Y", "Z"};
	/** Letras minusculas **/
    private static final	String[]    lower	= {"a", "b", "c", "d", "e", "f", "g", "h", "i", "j", "k", 
"l", "m", "n", "o", "p", "q", "r", "s", "t", "u", "v", "w", "x", "y", "z"};
        /** Digitos **/
    private static final	String[]    digit	= {"0", "1", "2", "3", "4", "5", "6", "7", "8", "9"};
        /** Generador automático **/
        private Random      genNum 	= new Random();
        /** arrTam **/
        private int         arrTam	= 0;
        /** contL **/
        private int         contL	= 0;
        /** contU **/
        private int         contU	= 0;
        /** res **/
        private String      res  = "";
        /** pass **/
        private String      pass;
        /** iGeRaNu **/
        private int         iGeRaNu;
        /** sTam **/
        private String      sTam;
        /** sChar **/
        private String      sChar;
/**
* Constructor
*
*/
        public PasswordGenerator() {
        	super();
        }
/** 
* Generada la password
* @return password resultante
*/
        public String generateIt() {
                pass	= "";
                for (int i = 0; i < 8; i++) {
                        if ((contL == 2) || (contU) == 1) {
                                arrTam = 9;
                                //Adding a digit to password
                                pass = pass + getdigit(genNum());
                                contL = 0;
                                contU = 0;
                        } else {
                                arrTam = 26;
                                if (genNum() % 2 == 0) {
                                        //Adding a upper character to password
                                        pass = pass + getupper(genNum());
                                        contU++;
                                } else {
                                        //Adding a lower character to password
                                        pass = pass + getlower(genNum());
                                        contL++;
                                }
                        }
                }

                return pass;

        }

        /**
         * Genera número aleatorio
         * @return numero generado
         */
        int genNum() {
                iGeRaNu = genNum.nextInt() % arrTam;
                sTam = new Integer(iGeRaNu).toString();
                sChar = new String(String.valueOf(sTam.charAt(0)));
                //Converting the possible negative number to positive
                if (sChar.equals("-")) {
                	iGeRaNu = iGeRaNu * -1;
                }
                return iGeRaNu;
        }

/**
* Genera letra mayuscula
* @param val posición letra
* @return letra mayuscula
*/	        
        String getupper(int val) {
                res = upper[val];
                return res;

        }
        /**
         * Genera letra minuscula
         * @param val posición letra
         * @return letra minuscula
         */	        

        String getlower(int val) {
                res = lower[val];
                return res;
        }

        /**
         * Genera digito
         * @param val posición digito
         * @return digito
         */	        
        String getdigit(int val) {
                res = digit[val];
                return res;
        }


}