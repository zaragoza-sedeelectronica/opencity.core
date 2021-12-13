package org.sede.core.utils;

import java.math.BigDecimal;
import java.text.Normalizer;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import info.debatty.java.stringsimilarity.NormalizedLevenshtein;

public class Similarity {

    public static List<String> STOPWORDS = Arrays.asList("a","aca","ahi","ajena","ajeno","ajenas","ajenos","al","algo","algun","alguna","alguno","algunas","algunos","alla","alli","ambos","ante","antes","aquel","aquella","aquello","aquellas","aquellos","aqui","arriba","asi","atras","aun","aunque","bajo","bastante","bien","cabe","cada","casi","cierto","cierta","ciertos","ciertas","como","con","conmigo","conseguimos","conseguir","consigo","consigue","consiguen","consigues","contigo","contra","cual","cuales","cualquier","cualquiera","cualquieras","cuan","cuando","cuanto","cuanta","cuantos","cuantas","de","dejar","del","demás","demasiada","demasiado","demasiadas","demasiados","dentro","desde","donde","dos","el","ella","ello","ellas","ellos","empleais","emplean","emplear","empleas","empleo","en","encima","entonces","entre","era","eras","eramos","eran","eres","es","esa","ese","eso","esas","esos","esta","estas","estaba","estado","estais","estamos","estan","estar","este","esto","estos","estoy","etc","fin","fue","fueron","fui","fuimos","gueno","ha","hace","haces","haceis","hacemos","hacen","hacer","hacia","hago","hasta","hay","incluso","intenta","intentas","intentais","intentamos","intentan","intentar","intento","ir","jamas","junto","juntos","la","lo","las","los","largo","mas","me","menos","mi","mis","mia","mias","mientras","mio","mios","misma","mismo","mismas","mismos","modo","mucha","muchas","muchisima","muchisimo","muchisimas","muchisimos","mucho","muchos","muy","nada","ni","ningun","ninguno","ninguna","ningunos","ningunas","no","nos","nosotras","nosotros","nuestra","nuestro","nuestras","nuestros","nunca","os","otra","otro","otras","otros","para","parecer","pero","poca","poco","pocas","pocos","podeis","podemos","poder","podria","podrias","podriais","podriamos","podrian","por","porque","primero","puede/n","puedo","pues","que","querer","quien","quienes","quienesquiera","quienquiera","quiza","quizas","sabe","sabes","saben","sabeis","sabemos","saber","se","segun","ser","si","siempre","siendo","sin","sino","so","sobre","sois","solamente","solo","sólo","somos","soy","sr","sra","sres","sta","su","sus","suya","suyo","suyos","tal","tales","tambien","tampoco","tan","tanta","tanto","tantas","tantos","te","teneis","tenemos","tener","tengo","ti","tiempo","tiene","tienen","toda","todo","todas","todos","tomar","trabaja","trabaja","trabajais","trabajamos","trabajan","trabajar","trabajas","tras","tu","tus","tuya","tuyo","tuyas","tuyos","ultimo","un","una","uno","unas","unos","usa","usas","usais","usamos","usan","usar","uso","usted","ustedes","va","van","vais","valor","vamos","varias","varias","vaya","verdadera","vosotras","vosotros","voy","vuestra","vuestro","vuestras","vuestros","y","ya","yo");
    
    
    /**
     * la consulta debe devolver la primera columna con un id numérico y la segunda columna con el texto a evaluar la similitud,
     * @param query
     * @param texto
     * @param em
     * @return Lista con los identificadores de registros que son similares al texto introducido
     */
    public static BigDecimal[] evaluar(String query, String texto, EntityManager em) {		
		List<BigDecimal> returnId = new ArrayList<BigDecimal>();
		NormalizedLevenshtein norm = new NormalizedLevenshtein();
		
		String textoProcesado = procesaCadena(texto, STOPWORDS);
		
		double limit = 0.3;
		double currentLimit = limit;
		Query q = em.createNativeQuery(query);		
		
		List<?> resultados = q.getResultList();
		for (Object obj:resultados) {
			List<?> list = new ArrayList();
			try {
				if (obj.getClass().isArray()) {
			        list = Arrays.asList((Object[])obj);
			    } else if (obj instanceof Collection<?>) {
			        list = new ArrayList<>((Collection<?>)obj);
			    }
				
			} catch (Exception e) {
 
			}
			BigDecimal id = (BigDecimal) list.get(0);
			String text = procesaCadena((String) list.get(1), STOPWORDS);
			double val = norm.distance(textoProcesado, text);
//			System.out.println(id + ":" +val+":"+text);
			if (val < limit && val < currentLimit) { 
				currentLimit = val;
				returnId.add(id);
			}
			
		}
		BigDecimal[] retorno = new BigDecimal[returnId.size()];
		return returnId.toArray(retorno);
	}
	
	/**
	 * Procesa cadena.
	 *
	 * @param input the input
	 * @param stopWords the stop words
	 * @return the string
	 */
	private static String procesaCadena(String input, List<String> stopWords) {
		String cadena = cleanString(input
				.replaceAll("\n", " ").replaceAll("[-+^¿?¡!,.;:-_'\"]*", "")
				.replaceAll("\\s{2,}", " ").trim());
		ArrayList<String> allWords = Stream.of(cadena.toLowerCase().split(" "))
				.collect(Collectors.toCollection(ArrayList<String>::new));
		allWords.removeAll(stopWords);

		cadena = allWords.stream().collect(Collectors.joining(" "));
		return cadena;
	}

	/**
	 * Clean string.
	 *
	 * @param texto the texto
	 * @return the string
	 */
	private static String cleanString(String texto) {
		texto = Normalizer.normalize(texto, Normalizer.Form.NFD);
		texto = texto.replaceAll("[\\p{InCombiningDiacriticalMarks}]", "");
		return texto;
	}
    
    
}