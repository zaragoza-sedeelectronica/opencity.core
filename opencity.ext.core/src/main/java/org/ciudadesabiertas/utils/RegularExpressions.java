package org.ciudadesabiertas.utils;

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang3.tuple.MutablePair;
import org.apache.commons.lang3.tuple.Pair;

public class RegularExpressions
{
	//fecha en formato 'AAAA-MM-DD'
	private static final String SIMPLE_DATE_REGEX = "'\\d{4}-\\d{2}-\\d{2}'";
	//fecha en formato 'AAAA-MM-DD'T'HH:mm:ss' o 'AAAA-MM-DD HH:mm:ss'	
	private static final String COMPLEX_DATE_REGEX = "'\\d{4}-\\d{2}-\\d{2}T\\d{2}:\\d{2}:\\d{2}'|'\\d{4}-\\d{2}-\\d{2} \\d{2}:\\d{2}:\\d{2}'";
	
	private static Pattern patternFullDate=Pattern.compile(COMPLEX_DATE_REGEX);
	
	private static Pattern patternDate=Pattern.compile(SIMPLE_DATE_REGEX);
	
		
	
	public static ArrayList<Pair<String, String>> extractChangesFullDate(String text)
	{
		
		ArrayList<Pair<String, String>> changes=new ArrayList<Pair<String, String>>();
		
		Matcher matcherA = patternFullDate.matcher(text);			
		while (matcherA.find())
		{
			String original=matcherA.group();				
			String originalTratado=original.toUpperCase().replace("T", " ");
			String cambio="to_date("+originalTratado+",'YYYY-MM-DD HH24:MI:SS')";						
			Pair<String, String> pair = new MutablePair<String, String>(original, cambio);
			if (changes.contains(pair)==false)
				changes.add(pair);
		}
		return changes;
	}
	
	public static ArrayList<Pair<String, String>> extractChangesDate(String text)
	{
		
		ArrayList<Pair<String, String>> changes=new ArrayList<Pair<String, String>>();
		
		Matcher matcherA = patternDate.matcher(text);			
		while (matcherA.find())
		{
			String original=matcherA.group();				
			String originalTratado=original.toUpperCase().replace("T", " ");
			String cambio="to_date("+originalTratado+",'YYYY-MM-DD')";						
			Pair<String, String> pair = new MutablePair<String, String>(original, cambio);
			if (changes.contains(pair)==false)
				changes.add(pair);
		}
		return changes;
	}

	


	

}
