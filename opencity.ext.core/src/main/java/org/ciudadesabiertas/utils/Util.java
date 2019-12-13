package org.ciudadesabiertas.utils;

import java.beans.BeanInfo;
import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.math.BigDecimal;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.servlet.http.HttpServletRequest;

import org.sede.core.rest.CheckeoParametros;
import org.sede.core.rest.Mensaje;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

public class Util
{
	private static final Logger logger = LoggerFactory.getLogger(Util.class);
	public static  SimpleDateFormat dateTimeFormatter = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
	public static Map<String,String> pageMetadataCalculation(HttpServletRequest request, long total, int pageSize)
	{
		PageInfo info=Util.getPageInfoFromURL(request,pageSize);
		
		//Numero de paginas
		long numPages=total/pageSize;
		long resto=total%pageSize;
		if (resto!=0)
		{
			numPages++;
		}
		
		HashMap<String,String> pagesMetadata=new HashMap<String,String>();
		
		//Pagina actual
		pagesMetadata.put(CheckeoParametros.SELF, info.toString());
		
		//Pagina inicial
		PageInfo aux=new PageInfo(info);
		aux.setPage(CheckeoParametros.defaultPage);		
		pagesMetadata.put(CheckeoParametros.FIRST, aux.toString());
		//Ultima página
		aux.setPage(numPages);		
		pagesMetadata.put(CheckeoParametros.LAST, aux.toString());
		//Pagina siguiente
		if (info.getPage()<=CheckeoParametros.defaultPage)
		{
			if (numPages>CheckeoParametros.defaultPage)
			{
				aux=new PageInfo(info);
				aux.setPage(CheckeoParametros.defaultPage+1);			
				pagesMetadata.put(CheckeoParametros.NEXT, aux.toString());
			}
		}else {
			aux=new PageInfo(info);					
			if (aux.getPage()+1<=numPages)
			{
				aux.setPage(aux.getPage()+1);				
				pagesMetadata.put(CheckeoParametros.NEXT, aux.toString());
			}
		}
		//Pagina anterior
		if (info.getPage()>CheckeoParametros.defaultPage)
		{
			aux=new PageInfo(info);		
			//Si me meten un pagina mayor al numero de paginas
			if (aux.getPage()>numPages)
			{						
				aux.setPage(numPages);
			}else {
				aux.setPage(aux.getPage()-1);	
			}
			pagesMetadata.put(CheckeoParametros.PREV, aux.toString());
		}
		
		
		return pagesMetadata;
	}
	
	
	public static HttpHeaders extractHeaders(Result<?> result)
	{
		HttpHeaders headers = new HttpHeaders();
		headers.add(CheckeoParametros.SELF, result.getSelf());
		headers.add(CheckeoParametros.FIRST, result.getFirst());
		headers.add(CheckeoParametros.LAST, result.getLast());
		if (Util.validValue(result.getNext())) {
			headers.add(CheckeoParametros.NEXT, result.getNext());
		}
		if (Util.validValue(result.getPrev())) {
			headers.add(CheckeoParametros.PREV, result.getPrev());
		}		
		
		headers.add(CheckeoParametros.PAGE, result.getPage()+"");
		headers.add(CheckeoParametros.PAGESIZE, result.getPageSize()+"");
		headers.add(CheckeoParametros.PAGERECORDS, result.getPageRecords()+"");
		headers.add(CheckeoParametros.TOTALRECORDS, result.getTotalRecords()+"");		
		headers.add(CheckeoParametros.STATUS, result.getStatus()+"");
		headers.add(CheckeoParametros.RESPONSEDATE, dateTimeFormatter.format(result.getResponseDate()));
		headers.add(CheckeoParametros.CONTENT_MD5, result.getContentMD5());
		
		return headers;
		
	}
	public static boolean validValue(String value)
	{
		if (value != null && !"".equals(value))
			return true;
		return false;
	}
	
	public static boolean validValue(Boolean value)
	{
		if (value != null )
			return true;
		return false;
	}
	
	

	public static boolean validValue(BigDecimal value)
	{
		if (value != null)
			return true;
		return false;
	}

	public static boolean validValue(Date value)
	{
		if (value != null )
			return true;
		return false;
	}
	
	public static boolean validValue(Integer value)
	{
		if (value != null )
			return true;
		return false;
	}
	
	public static boolean validValue(Object value)
	{
		if (value != null )
			return true;
		return false;
	}
public static PageInfo getPageInfoFromURL(HttpServletRequest request, int pageSize) {
	    
		PageInfo info=new PageInfo();
		if (Util.validValue(pageSize))
		{
			info.setPageSize(pageSize);
		}
		String extraParams="";
		
		info.setRequestURL(request.getRequestURL().toString());
		
		 Map<String, String[]> parameterNames = request.getParameterMap(); 
		 
		 for (Entry<String, String[]> entry : parameterNames.entrySet())
		 {
			 String value=getValueFromStringArray(entry.getValue());		
			 if (entry.getKey().equals("pageSize"))
			 {
				 info.setPageSize(Integer.parseInt(value));
			 }
			 else if (entry.getKey().equals("page"))
			 {
				 info.setPage(Integer.parseInt(value));
			 }
			 else if (entry.getKey().equals("fields"))
			 {
				 info.setFields(value);
			 }
			 else if (entry.getKey().equals("q"))
			 {
				 info.setQ(value);
			 }
			 else if (entry.getKey().equals("sort"))
			 {
				 info.setSort(value);
			 }
			 else {
				 if (extraParams.equals("")==false)
				 {
					 extraParams+="&";
				 }		 
				 extraParams+=entry.getKey()+"="+value;
			 }
		 }
		 	        
	    info.setExtraParams(extraParams);
		 
	    return info;
	    
	    
	}
	public static String getValueFromStringArray(String[] param)	
	{
		String value="";
		for (String v:param)
		{
			value+=v;
		}
		return value;
	}
	
	public static <T> String extractNameFromModelClass(Class<T> type)
	{
		String s="";
		if (type!=null)
		{
			String fullName=type.getName();			
			if (fullName.contains(".")) 
			{				
				s=fullName.substring(fullName.lastIndexOf(".")+1, fullName.length());
			}
			else 
			{
				s=fullName;
			}
		}
		return s;
	}
	public static <T> String extractKeyFromModelClass(Class<T> type)
	{
		String s="";
		if (type!=null)
		{
			String fullName=type.getName();
			fullName=fullName.toLowerCase();
			if (fullName.contains(".")) 
			{				
				s=fullName.substring(fullName.lastIndexOf(".")+1, fullName.length());
			}
			else 
			{
				s=fullName;
			}
		}
		return s;
	}

	public static String generateHash(String original) {
	
	    // log.info(original);
	
	    StringBuffer sb = new StringBuffer();
	    MessageDigest md = null;
	
	    try {
	        md = MessageDigest.getInstance("MD5");
	    } catch (NoSuchAlgorithmException e) {
	        logger.error("Error with hash algorith", e);
	    }
	
	    if (md != null) {
	        md.update(original.getBytes());
	        byte[] digest = md.digest();
	
	        for (byte b : digest) {
	            sb.append(String.format("%02x", b & 0xff));
	        }
	    }
	    return sb.toString();
	}
	public static <T> ArrayList<String> extractPropertiesFromBean(Class<T> beanClass)
	{
		ArrayList<String> availableFields = new ArrayList<String>();

		try
		{
			BeanInfo beanInfo = Introspector.getBeanInfo(beanClass);
			for (PropertyDescriptor propertyDesc : beanInfo.getPropertyDescriptors())
			{
				String propertyName = propertyDesc.getName();
				availableFields.add(propertyName);
			}
			if (availableFields.contains(CheckeoParametros.X.toLowerCase()))
			{
				//AÑADIMOS LOS CAMPOS NECESARIOS DE GEO
				availableFields.add(CheckeoParametros.XETRS89);
				availableFields.add(CheckeoParametros.YETRS89);
				availableFields.add(CheckeoParametros.DISTANCE);
			}
			
			
		} catch (IntrospectionException e)
		{
			logger.error("Error extracting fields from "+beanClass.getName(), e);
		}

		return availableFields;
	}
	/*Metodo para validar los parametros reibidos
	 * parameterMap: mapa con todos los parametros recibidos
	 * availableFields: campos propios del conjunto de datos a validar
	 * */
	public static ResponseEntity<?> validateParams(Map<String, String[]> parameterMap, List<String> availableFields) 
	{	
		List<String> allowedParams=new ArrayList<String>();		
		allowedParams.add(CheckeoParametros.FIELDS);
		allowedParams.add(CheckeoParametros.RSQL_Q);
		allowedParams.add(CheckeoParametros.PAGE);
		allowedParams.add(CheckeoParametros.PAGESIZE);		
		allowedParams.add(CheckeoParametros.SORT);
		allowedParams.add(CheckeoParametros.AJAX_PARAM);
		allowedParams.add(CheckeoParametros.SRID);
		allowedParams.add(CheckeoParametros.XETRS89);
		allowedParams.add(CheckeoParametros.YETRS89);
		allowedParams.add(CheckeoParametros.METERS);
		
		return validateParams(parameterMap, availableFields,allowedParams);
	}
	
	/*Metodo para validar los parametros reibidos
	 * parameterMap: mapa con todos los parametros recibidos
	 * availableFields: campos propios del conjunto de datos a validar
	 * */
	public static ResponseEntity<?> validateParams(Map<String, String[]> parameterMap, List<String> availableFields,List<String> allowedParams) 
	{	

		allowedParams.addAll(availableFields);
		List<String> wrongParamList=new ArrayList<String>();
		for (Map.Entry<String, String[]> entry : parameterMap.entrySet()) {
		    if (allowedParams.contains(entry.getKey())==false)
		    {
		    	wrongParamList.add(entry.getKey());
		    }		    
		}
		
		if (wrongParamList.size()>0)
		{
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new Mensaje(HttpStatus.BAD_REQUEST.value(), "Unknown parameters: "+wrongParamList));
		}
		
		return null;
	}
}
