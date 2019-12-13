package org.ciudadesabiertas.utils;

import java.util.ArrayList;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.tuple.Pair;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DifferentSQLforDatabases {
	
	public static final String TRANSLATE_ORACLE = "TRANSLATE(UPPER(";
	public static final String TRANSLATE_SQLSERVER = "dbo.TRANSLATE(UPPER(";	
	public static final String TRANSLATE_END = "),'ÂÁÀÄÃÊÉÈËÎÍÌÏÔÓÒÖÕÛÚÙÜÑ', 'AAAAAEEEEIIIIOOOOOUUUUN')";	


	private static final Logger log = LoggerFactory.getLogger(DifferentSQLforDatabases.class);
		

	public static RegularExpressions regularExpressions=new RegularExpressions();
	
	public static String transForm (String where/*, String driver*/) {
		
		
		
		String result = "";
//		if (Util.getDatabaseTypeFromDriver(driver).equals(Constants.ORACLE)) {

			
			ArrayList<Pair<String, String>> changesFullDate = regularExpressions.extractChangesFullDate(where);
			
			String whereWithoutPreSubstitution=where;
			
			//Esto es para no repetir extracciones, lo que ya se ha sustituido previametne se cambia por --SubString--
			for (Pair<String,String> pair:changesFullDate)
			{
				whereWithoutPreSubstitution=whereWithoutPreSubstitution.replaceAll(pair.getKey(), "--SubString--");
			}
					
			
			ArrayList<Pair<String, String>> changesDate = regularExpressions.extractChangesDate(whereWithoutPreSubstitution);
						
			
			if (changesFullDate.size()>0 || (changesDate.size()>0))
			{
				log.info("before where: "+where);
			
				for (Pair<String,String> pair:changesFullDate)
				{
					where=where.replaceAll(pair.getKey(), pair.getValue());
				}
				
				for (Pair<String,String> pair:changesDate)
				{
					where=where.replaceAll(pair.getKey(), pair.getValue());
				}
			
				log.info("after where: "+where);
			}
			
			result = where;
			
//		}else if (Util.getDatabaseTypeFromDriver(driver).equals(Constants.MYSQL)) {			
//			result = where;
//			
//		}else if (Util.getDatabaseTypeFromDriver(driver).equals(Constants.SQLSERVER)) {			
//			result = where;
//		}else {			
//			result = where;
//		}
		
		return result;
		
	}
	
	
	public static String controlLikeConditions(String conditions/*, String driver*/)
	{
//		if ((driver.contains(Constants.ORACLE))||(driver.contains(Constants.SQLSERVER)))
//		{
			ArrayList<String> firstLoop = likesControl(conditions);
			String finalHaving = genWhereLike(firstLoop);
			return finalHaving;	
//		}
//		else
//		{
//			return conditions;
//		}
	}
	
	/*Funcion que añade la funcion para eliminar acentos y no distinguir entra mayusculas y minusculas en un array con likes*/
	private static String genWhereLike(ArrayList<String> firstLoop/*, String driver*/)
	{
		for (int i=1;i<firstLoop.size()-1;i++)
		{							
			if (firstLoop.get(i).equals("like"))
			{
//				if (driver.contains(Constants.ORACLE))
//				{
					//Tocamos el elemento anterior
					firstLoop.set(i-1,TRANSLATE_ORACLE+ firstLoop.get(i-1) +TRANSLATE_END);
					//Tocamos el elemento posterior
					firstLoop.set(i+1,TRANSLATE_ORACLE+ firstLoop.get(i+1) +TRANSLATE_END);
//				}
//				else if (driver.contains(Constants.SQLSERVER))
//				{
//					//Tocamos el elemento anterior
//					firstLoop.set(i-1,TRANSLATE_SQLSERVER+ firstLoop.get(i-1) +TRANSLATE_END);
//					//Tocamos el elemento posterior
//					firstLoop.set(i+1,TRANSLATE_SQLSERVER+ firstLoop.get(i+1) +TRANSLATE_END);
//				}
			}
		}
		String finalWhere="";
		for (String st:firstLoop)
		{
			finalWhere+=st+" ";
		}
		finalWhere=StringUtils.chop(finalWhere);
		return finalWhere;
	}

	
	/*Funcion que recibe un conjunto de condiciones (where, having) y devuelve una array agrupando los likes
	 * Ejemplo:  name like 'solid snake' and age>23 -> [name, like, solid snake, and, age, >, 23]*/
	private static ArrayList<String> likesControl(String where)
	{
		String[] splitted=where.split(" ");
		ArrayList<String> firstLoop=new ArrayList<String>();
		String aux="";
		for (String split:splitted)
		{
			if (split.contains("'"))
			{
				if ((split.startsWith("'"))&&(split.endsWith("'")))
				{
					firstLoop.add(split);	
				}
				else if ((split.startsWith("'"))&&(split.endsWith("'")==false))
				{
					aux=split;
				}
				else if (aux.equals("")==false) 
				{
					if (split.endsWith("'"))
					{
						aux+=" "+split;
						firstLoop.add(aux);	
						aux="";
					}else {
						aux+=" "+split;
					}
				}
			}
			else
			{
				if (aux.equals(""))
				{
					firstLoop.add(split);
				}else {
					aux+=" "+split;
				}
			}
		}
		return firstLoop;
	}
	

}
