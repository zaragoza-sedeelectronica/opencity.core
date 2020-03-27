package org.sede.core.utils;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Reader;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.net.URL;
import java.net.URLDecoder;
import java.security.MessageDigest;
import java.sql.SQLException;
import java.text.Normalizer;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Random;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.activation.DataHandler;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.SendFailedException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import javax.mail.util.ByteArrayDataSource;
import javax.servlet.http.HttpServletRequest;
import javax.validation.ConstraintViolation;

import org.apache.commons.codec.CharEncoding;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.lang3.StringEscapeUtils;
import org.apache.commons.lang3.StringUtils;
import org.elasticsearch.action.get.GetResponse;
import org.sede.core.anotaciones.PathId;
import org.sede.core.anotaciones.ResponseClass;
import org.sede.core.entity.CalendarDay;
import org.sede.core.exception.InvalidImplementationException;
import org.sede.core.geo.ClaseGeojsonVacia;
import org.sede.core.rest.CheckeoParametros;
import org.sede.core.rest.Mensaje;
import org.sede.core.rest.Peticion;
import org.sede.core.rest.json.JSONArray;
import org.sede.servicio.acceso.entity.Ciudadano;
import org.sede.servicio.acceso.entity.Credenciales;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.googlecode.genericdao.search.SearchResult;

public class Funciones {
	private Funciones () {
		super();
	}
	private static final Logger logger = LoggerFactory.getLogger(Funciones.class);
	
	public static String escape(String s) {
		if (s == null) {
			return null;
		}
		StringBuilder sb = new StringBuilder();
		escape(s, sb);
		return sb.toString();
	}

	static void escape(String s, StringBuilder sb) {
		for (int i = 0; i < s.length(); i++) {
			char ch = s.charAt(i);
			switch (ch) {
			case '"':
				sb.append("\\\"");
				break;
			case '\\':
				sb.append("\\\\");
				break;
			case '\b':
				sb.append("\\b");
				break;
			case '\f':
				sb.append("\\f");
				break;
			case '\n':
				sb.append("\\n");
				break;
			case '\r':
				sb.append("\\r");
				break;
			case '\t':
				sb.append("\\t");
				break;
			default:
				// Reference: http://www.unicode.org/versions/Unicode5.1.0/
				if ((ch >= '\u0000' && ch <= '\u001F')
						|| (ch >= '\u007F' && ch <= '\u009F')
						|| (ch >= '\u2000' && ch <= '\u20FF')) {
					String ss = Integer.toHexString(ch);
					sb.append("\\u");
					for (int k = 0; k < 4 - ss.length(); k++) {
						sb.append('0');
					}
					sb.append(ss.toUpperCase());
				} else {
					sb.append(ch);
				}
			}
		}// for
	}
	
	public static Object retrieveObjectValue(Object obj, String property) {
		if (property.contains(".")) {
			// we need to recurse down to final object
			String[] props = property.split("\\.");
			try {
				Object ivalue = null;
				if (Map.class.isAssignableFrom(obj.getClass())) {
					Map<?,?> map = (Map<?,?>) obj;
					ivalue = map.get(props[0]);
				} else {
					Method method;
					if (obj.getClass().getCanonicalName().equals("org.elasticsearch.action.get.GetResponse")) {
						method = obj.getClass().getMethod("getSource");
					} else {
						method = obj.getClass().getMethod(getGetterMethodName(props[0]));
					}
					ivalue = method.invoke(obj);
				}
				
				if (ivalue == null) {
					return null;
				}
				return retrieveObjectValue(ivalue, property.substring(props[0].length() + 1));
			} catch (Exception e) {
				return null;
			}
		} else {
			// let's get the object value directly
			try {
				if (Map.class.isAssignableFrom(obj.getClass())) {
					Map<?,?> map = (Map<?,?>) obj;
					return HbUtils.deproxy(map.get(property));
				} else {
					Method method;
					if (obj.getClass().getCanonicalName().equals("org.elasticsearch.action.get.GetResponse")) {
						method = obj.getClass().getMethod("getSource");
					} else {
						method = obj.getClass().getMethod(getGetterMethodName(property));
					}
					
					return HbUtils.deproxy(method.invoke(obj));
				}
			} catch (Exception e) {
				return null;
			}
		}
	}

	public static String getGetterMethodName(String name) {
		return "get" + name.substring(0, 1).toUpperCase() + name.substring(1);
	}

	public static String obtenerPath(Class<?> clase) {
		if (clase.isAnnotationPresent(PathId.class)) {
			return (Propiedades.isLocal() ? "http://" : "https://") + Propiedades.getPath() + Propiedades.getContexto() + clase.getAnnotation(PathId.class).value() + "/";
		} else {
			return "";
		}
	}
	
	public static Class<?> descubrirClase(Object retorno) throws InvalidImplementationException {
		if (retorno instanceof SearchResult) {
			try {		
				return ((SearchResult<?>)retorno).getResult().get(0).getClass();
			} catch (IndexOutOfBoundsException e) {
				return ClaseGeojsonVacia.class;
			}
		} else {
			if (retorno.getClass().getCanonicalName().indexOf(retorno.getClass().getSuperclass().getCanonicalName()) >= 0) {
				return retorno.getClass().getSuperclass();
			} else {
				return retorno.getClass();
			}
		}
	}

	public static int copy(InputStream in, OutputStream out) throws IOException {
		try {
			int byteCount = 0;
			byte[] buffer = new byte[4096];
			int bytesRead = -1;
			while ((bytesRead = in.read(buffer)) != -1) {
				out.write(buffer, 0, bytesRead);
				byteCount += bytesRead;
			}
			out.flush();
			return byteCount;
		} finally {
			try {
				in.close();
			} catch (IOException ex) {
				logger.error(ex.getMessage());
			}
			try {
				out.close();
			} catch (IOException ex) {
				logger.error(ex.getMessage());
			}
		}
	}
	
	
	public static void sendMailCustomFrom(final String titulo, final String txtMensaje, 
			String from, final String destino, final String respuesta, 
			final String tipo) throws MessagingException {
		Funciones.sendMail(titulo, txtMensaje, from, 
				destino, respuesta, Propiedades.getMailUser(), Propiedades.getMailPass(), tipo);
	}
	
	public static void sendMail(final String titulo, final String txtMensaje, 
			final String destino, final String respuesta, 
			final String tipo) throws MessagingException {
//		Funciones.sendMail(titulo, txtMensaje, "Ayuntamiento de Zaragoza<" + Propiedades.getMailUser() + "@zaragoza.es>", 
//				destino, respuesta, Propiedades.getMailUser(), Propiedades.getMailPass(), tipo);
		Funciones.sendMail(titulo, txtMensaje, Propiedades.getMailUser() , 
				destino, respuesta, Propiedades.getMailUser(), Propiedades.getMailPass(), tipo);
	}
	public static void sendMailAdjunto(final String titulo, final String txtMensaje, 
			final String destino, final String respuesta, 
			final String tipo, byte[] adjunto, String nombreAdjunto, String mimetype) throws MessagingException {
		Funciones.sendMail(titulo, txtMensaje, Propiedades.getMailUser(), destino, respuesta, Propiedades.getMailUser(), Propiedades.getMailPass(), tipo, null, adjunto, nombreAdjunto, mimetype);
	}
	public static void sendMail(final String titulo, final String txtMensaje, final String origen, 
			final String destino, final String respuesta, final String usuario, final String password, 
			final String tipo) throws MessagingException {
		Funciones.sendMail(titulo, txtMensaje, origen, destino, respuesta, usuario, password, tipo, null);
	}
	public static void sendMail(final String titulo, final String txtMensaje, final String origen, 
			final String destino, final String respuesta, final String usuario, final String password, 
			final String tipo, String cc/*Listado de direcciones separado por comas*/) throws MessagingException {
		Funciones.sendMail(titulo, txtMensaje, origen, destino, respuesta, usuario, password, tipo, cc, null, null, null);
	}
	public static void sendMail(final String titulo, final String txtMensaje, final String origen, 
			final String destino, final String respuesta, final String usuario, final String password, 
			final String tipo, String cc/*Listado de direcciones separado por comas*/, byte[] adjunto, String nombreAdjunto, String mimetype) throws MessagingException {
		if (Propiedades.getMailServer() != null && Propiedades.getMailServer().length() > 0 && destino != null && destino.length() > 0) {
			try {
				final Properties props = new Properties();
				props.put("mail.smtp.host", Propiedades.getMailServer());
				props.put("mail.smtp.auth", "true");
				final Session ses = Session.getInstance(props, null);
				
				final MimeMessage message = new MimeMessage(ses);
				final InternetAddress from = new InternetAddress(origen);
				message.setFrom(from);
				message.setSentDate(new Date());
				if (!"".equals(respuesta)) {
					message.setReplyTo(new InternetAddress[]{new InternetAddress(respuesta)});
				}
				
				
				if (destino.indexOf(',') > 1) {
					String recipient = destino;
					String[] recipientList = recipient.split(",");
					InternetAddress[] recipientAddress = new InternetAddress[recipientList.length];
					int counter = 0;
					for (String dest : recipientList) {
					    recipientAddress[counter] = new InternetAddress(dest.trim());
					    counter++;
					}
					message.setRecipients(Message.RecipientType.TO, recipientAddress);
				} else {
					final InternetAddress para = new InternetAddress(destino);
					message.addRecipient(Message.RecipientType.TO, para);
				}
				if (cc != null) {
					message.addRecipients(Message.RecipientType.CC, InternetAddress.parse(cc));
				}
				message.setSubject(titulo);
				
				if (adjunto != null && adjunto.length > 0) {
					MimeBodyPart messageBodyPart = 	new MimeBodyPart();
					//fill message
					if ("HTML".equals(tipo)) {
						messageBodyPart.setContent(txtMensaje, "text/html");
					} else {
						messageBodyPart.setContent(txtMensaje, "text/plain");
					}
					final Multipart multipart = new MimeMultipart();
					multipart.addBodyPart(messageBodyPart);
					// Part two is attachment
					messageBodyPart = new MimeBodyPart();
					
					ByteArrayDataSource bds = new ByteArrayDataSource(adjunto, mimetype); 
					messageBodyPart.setDataHandler(new DataHandler(bds));
					messageBodyPart.setFileName(nombreAdjunto);
					messageBodyPart.setHeader("Content-Type", mimetype);
					multipart.addBodyPart(messageBodyPart);	
				    message.setContent(multipart);
				} else {
					if ("HTML".equals(tipo)) {
						message.setContent(txtMensaje, "text/html; charset=UTF-8");
					} else {
						message.setContent(txtMensaje, "text/plain; charset=UTF-8");
					}
					message.saveChanges();
				}
				
				final Transport transport = ses.getTransport("smtp");				
				transport.connect(Propiedades.getMailServer(), usuario, password);
				transport.sendMessage(message, message.getAllRecipients());
				transport.close();
			} catch (AddressException e) {
				throw new AddressException("Error en las direcciones de correo: " +  e.getMessage());
			} catch (SendFailedException e) {
				throw new SendFailedException("Error en el proceso de envío del mensaje: " +  e.getMessage());
			} catch (MessagingException e) {
				e.printStackTrace();
				throw new MessagingException("Error a la hora de enviar el mensaje: " +  e.getMessage());
			}
		} else {
			logger.error("No se ha especificado el servidor de correo electrónico:Destino: " + destino + " :Asunto:" + titulo + " :Cuerpo:" + txtMensaje);
		}
	}
	
	public static Class<?> obtenerClaseRespuesta(Method metodo) {
		Class<?> claseRespuesta;
		if (metodo.isAnnotationPresent(ResponseClass.class)) {
			claseRespuesta = metodo.getAnnotation(ResponseClass.class).value();
		} else {
			claseRespuesta = metodo.getReturnType();
		}
		return claseRespuesta;
	}

	public static boolean metodoDevuelveArray(Method metodo) {
		return metodo.getReturnType().equals(SearchResult.class) 
				|| metodo.getReturnType().equals(String[].class)
				|| metodo.getReturnType().equals(List.class);
	}

	public static String generarHash(String cadena) {
		return "" + cadena.hashCode();
	}
	public static String obtenerValorCampo(Object object, String fieldName) {
		Field field;
		try {
			field = object.getClass().getDeclaredField(fieldName);
			field.setAccessible(true);
			return field.get(object).toString();

		} catch (SecurityException e) {
			return null;
		} catch (NoSuchFieldException e) {
			return null;
		} catch (IllegalArgumentException e) {
			return null;
		} catch (IllegalAccessException e) {
			return null;
		}
	}
	public static Object getValueFromObject(Object object, String fieldName) {
	    Class<?> clazz = object != null ? object.getClass() : null;
	    if (clazz == null) {
	      return null;
	    }
	    String getterName = "get" + fieldName.substring(0, 1).toUpperCase() + fieldName.substring(1);
	    try {
	      Method method = clazz.getMethod(getterName);
	      Object valueObject = method.invoke(object, (Object[]) null);
	      return valueObject != null ? valueObject : "";
	    } catch (Exception e) {
	    	logger.error(e.getMessage());
	    }
	    return null;
	  }
	public static Object obtenerValorCampoObj(Object object, String fieldName) {
		Field field;
		try {
			field = object.getClass().getDeclaredField(fieldName);
			field.setAccessible(true);
			return field.get(object);

		} catch (SecurityException e) {
			return null;
		} catch (NoSuchFieldException e) {
			return null;
		} catch (IllegalArgumentException e) {
			return null;
		} catch (IllegalAccessException e) {
			return null;
		}
	}
	public static Peticion getPeticion() {
		HttpServletRequest curRequest = 
				((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes())
				.getRequest();
		return (Peticion)curRequest.getAttribute(CheckeoParametros.ATTRPETICION);
	}
	
	public static String getFullUri() {
		HttpServletRequest curRequest = 
				((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes())
				.getRequest();
		StringBuilder retorno = new StringBuilder();
		retorno.append(curRequest.getRequestURL().toString().replace(":80", ""));
		if (curRequest.getQueryString() != null && !curRequest.getQueryString().isEmpty()) {
			retorno.append("?");
			retorno.append(curRequest.getQueryString());
		}
		return retorno.toString();
	}
	
	public static String calculateUserPassword(final String clearPassword) {
		String md5Password = "";
		try {
			final MessageDigest digs = java.security.MessageDigest.getInstance("MD5");
			digs.reset();
			digs.update(clearPassword.getBytes());
			md5Password = "{MD5}" + Base64.encodeBase64String(digs.digest());
		} catch (Exception e) {
			logger.error(e.getMessage());
		}
		return md5Password;
	}
	public static String checkNulo(Object o) {
		if (o != null) {
			return o.toString();
		} else {
			return "";
		}
	}
	public static String removeHTMLEntity(String text) {
		return StringEscapeUtils.unescapeHtml4(StringUtils.trimToEmpty(text).replaceAll("\\<.*?\\>", " "));
	}
	public static String removeHTMLEntityAmp(String text) {
		return removeHTMLEntity(text).replace("&", "&#38;");
	}
	public static String formatear(String txt) throws IOException {
		ObjectMapper mapper = new ObjectMapper();
		JsonNode json = mapper.readTree(txt);
		return (mapper.enable(SerializationFeature.INDENT_OUTPUT).writeValueAsString(json));
	}
	public static JsonNode asJson(String txt) throws IOException {
		ObjectMapper mapper = new ObjectMapper();
		JsonNode json = mapper.readTree(txt);
		return json;
	}
	public static ResponseEntity<?> generarMensajeError(
			Set<ConstraintViolation<Object>> errores) {
		StringBuilder txt = new StringBuilder();
		txt.append("<ul>");
		for (ConstraintViolation<?> err : errores) {
			txt.append("<li>" + err.getPropertyPath() + ": " + err.getMessage() + "</li>");
		}
		txt.append("</ul>");
		Mensaje mensaje = new Mensaje(HttpStatus.BAD_REQUEST.value(), txt.toString());
		return ResponseEntity.badRequest().body(mensaje);
		
	}
	
	public static boolean isValidEmail(String email) {
		email = email.toLowerCase();
		final Pattern patron = Pattern
				.compile("^[0-9a-z_\\.-]+@(([0-9]{1,3}\\.){3}[0-9]{1,3}|([0-9a-z][0-9a-z-]*[0-9a-z]\\.)+[a-z]{2,7})$");
		final Matcher mat = patron.matcher(email);
		return mat.find();
	}	
	public static String convertClobToString(java.sql.Clob clob) throws IOException, SQLException {
		if (clob != null) {
			Reader reader = clob.getCharacterStream();
	        int c = -1;
	        StringBuilder sb = new StringBuilder();
	        while((c = reader.read()) != -1) {
	             sb.append(((char)c));
	        }
	
	        return sb.toString();
		} else {
			return null;
		}
	}

	public static String normalizar(String text) {
		String temp = Normalizer.normalize(text, Normalizer.Form.NFD);
		Pattern pattern = Pattern.compile("\\p{InCombiningDiacriticalMarks}+");
	    return pattern.matcher(temp).replaceAll("").replaceAll("&", "_").replaceAll(" ", "_").replaceAll("º", "_").replaceAll("ª", "_");
	}
	
	public static List<?> asList(JSONArray jsonArray) {
		List<Object> list = new ArrayList<Object>();
		for (int i = 0; i < jsonArray.length(); i++) {
		    list.add(jsonArray.get(i));
		}
		return list;
	}
	public static boolean isDate(final int dia, final int mes, final int agno) {
		boolean retorno = true;
		final Calendar cal = Calendar.getInstance();
		cal.setLenient(false);
		try {
			cal.set(agno, mes, dia);
			cal.getTime();
		} catch (IllegalArgumentException e) {
			retorno = false;
		}
		return retorno;
	}

	public static boolean tieneValorDia(int diaAct, List<CalendarDay> tablonDias) {
		if (tablonDias != null) {
			for (CalendarDay dia : tablonDias) {
				if (dia.getDay().intValue() == diaAct) {
					if (dia.getCount().intValue() > 0) {
						return true;
					} else {
						return false;
					}
				}
			}
		}
		return false;
	}
	public static String getTextoFichero(final String fichero) throws IOException {
		setProxy();
    	URL url = null;
    	BufferedReader input  = null;
    	final StringBuilder str = new StringBuilder();
    	try {
    		url = new URL(fichero);
    		input = new BufferedReader(new InputStreamReader(url.openStream(), "ISO-8859-1"));
    		String inputLine = input.readLine();
    		while (inputLine  != null) {
				str.append(inputLine);
    	        inputLine = input.readLine();
    		}
    	} catch (Exception e) {
    		throw new IOException(e);
    	} finally {
    		if (input != null) {
    			input.close();
    		}
    	}
    	return str.toString();    	
    }
	public static Double[][] stringToCoords(String lista) {
		String[] dat = lista.split(", ");
		Double[][] retorno = new Double[dat.length][2];
		for (int i = 0; i < dat.length; i++) {
			String[] coord = dat[i].split(",");
			retorno[i] = new Double[]{Double.parseDouble(coord[1]),Double.parseDouble(coord[0])};
		}
		return retorno;
	}

	public static void setProxy() {
		String proxy = Propiedades.getProxyHost();
		if (proxy.length() > 0) {
			System.setProperty("http.proxyHost", Propiedades.getProxyHost());
			System.setProperty("http.proxyPort", Propiedades.getProxyPort());
			System.setProperty("https.proxyHost", Propiedades.getProxyHost());
			System.setProperty("https.proxyPort", Propiedades.getProxyPort());
		}
	}
	
	public static void saveFile(String path, byte[] imageByte) throws IOException {
		
    	FileOutputStream fop = null;
    	try {
			File file = new File(path);
			fop = new FileOutputStream(file);
			fop.write(imageByte);
			fop.flush();
    	} finally {
    		if (fop != null) {
    			fop.close();
    		}
    	}
    }
	
	public static void saveFile(String path, String fileName, InputStream is) throws IOException {
		String name = Funciones.normalizar(fileName);
		if (new File(path + name).exists()) {
			saveFile(path, calculateNextFileName(name), is);
		} else {
			saveFileOverWrite(path, fileName, is);
		}
	}
	public static void saveFileOverWrite(String path, String fileName,
			InputStream is) throws IOException {
		String name = Funciones.normalizar(fileName);
		OutputStream os = null;
		try {
			os = new FileOutputStream(path + name);
	        byte[] buffer = new byte[1024];
	        int bytesRead;
	        while((bytesRead = is.read(buffer)) !=-1){
	            os.write(buffer, 0, bytesRead);
	        }
	        is.close();
	        os.flush();
		} finally {
			if (os != null) {
				os.close();
			}
		}
		
	}

	public static String calculateNextFileName(String name) {
		String[] valores = name.split("\\.");
		String fileName = "";
		String fileExtension = "";
		if (valores.length > 2) {
			// el nombre del fichero tiene . los quitamos
			for (int i = 0; i < valores.length; i++) {
				if (i + 1 == valores.length) {
					fileExtension = valores[i];
				} else {
					fileName = fileName + valores[i];
				}
			}
		} else {
			fileName = valores[0];
			fileExtension = valores[1];
		}
		if (fileName.indexOf('-') >= 1) {
			try {
				int numero = Integer.parseInt(fileName.substring(fileName.lastIndexOf('-') + 1, fileName.length()));
				fileName = fileName.substring(0, fileName.lastIndexOf('-') + 1) + (numero + 1);
			} catch (NumberFormatException e) {
				fileName = fileName + "-1";	
			}
		} else {
			fileName = fileName + "-1";
		}
		return fileName + "." + fileExtension;
	}

	public static void saveFile(String ruta, String xhtm) throws IOException {
		// FIXME revisar el salto de linea porque no coincide con la edicion desde editor externo
		OutputStreamWriter out = null;
		OutputStream fout = null;
		OutputStream bout = null;
		try {
			fout= new FileOutputStream(ruta);
	        bout= new BufferedOutputStream(fout);
	        out = new OutputStreamWriter(bout);
	        out.write(xhtm);
	        out.flush();
		} finally {
			if (bout != null) {
				bout.close();
			}
			if (fout != null) {
				fout.close();
			}
			if (out != null) {
				out.close();
			}
		}
	}

	public static String removeB64Prefix(String b64) {
		if (b64.indexOf("base64,") + 7 > 0) {
			b64 = b64.substring(b64.indexOf("base64,") + 7, b64.length());
		}
		return b64;
	}

	public static String htmlToTxt(String description) {
		String txt = description.replace("</p>", System.getProperty("line.separator"));
		txt = txt.replace("</div>", System.getProperty("line.separator"));
		txt = txt.replace("<li>", "- ");
		txt = txt.replace("</li>", System.getProperty("line.separator"));
		
		
		return removeHTMLEntity(txt).trim();
		
	}

	public static String removeCDATA(String description) {
		return description.replace("<![CDATA[", "").replace("]]>", "");
	}
	public static String escapeJson(String description) {
		return description.replace("\"", "\\\"").replace(System.getProperty("line.separator"), "\\n").replaceAll("\\r\\n|\\r|\\n", "\\n").replaceAll("\t", "\\t");
	}
	public static String generateRandomPassword() {
		String password = "";
		final int minPassLen = 8;
		final int startNumbers = 48;
		final int startUpperCase = 65;
		final int startLowerCase = 97;
		int ascii = 0;
		int randomChar = 0;
		final Random generator = new Random();

		for (int i = 0; i < minPassLen; i++) {
			randomChar = generator.nextInt(3);
			switch (randomChar) {
			case 0:
				ascii = generator.nextInt(10) + startNumbers; // numbers (0...9)
				break;
			case 1:
				ascii = generator.nextInt(26) + startUpperCase; // letters (A..Z)
				break;
			case 2:
				ascii = generator.nextInt(26) + startLowerCase; // letters (a..z)
				break;
			}

			password += (char) ascii;
		}
		return password;
	}	
	public static Ciudadano getUser(HttpServletRequest request) {
		return (Ciudadano)request.getSession().getAttribute(CheckeoParametros.SESSIONCIUDADANO);
	}

	public static String getAccountIdUserGestor(HttpServletRequest request) {
		Ciudadano c = (Ciudadano)request.getSession().getAttribute(CheckeoParametros.SESSIONCIUDADANO); 
		if (c == null) {
			Credenciales credenciales = (Credenciales) request.getSession().getAttribute(CheckeoParametros.SESSIONGCZ);
			if (credenciales == null) {
				return null;
			} else {
				return "" + credenciales.getUsuario().getId();
			}
		} else {
			return c.getAccount_id();
		}
	}

	
	public static String cleanNif(String nif) {
		
		return nif.toUpperCase().trim().replaceAll(" ", "").replaceAll("-", "").replace(".", "");
	}
	public static String cleanMobile(String mobile) {
		
		return mobile.toUpperCase().trim().replaceAll(" ", "").replaceAll("-", "");
	}
	public static String getIpUser(HttpServletRequest request) {
		String ipAddress = request.getHeader("X-FORWARDED-FOR");
		if (ipAddress == null) {
			ipAddress = request.getRemoteAddr();
		}
		return ipAddress;
	}

	public static Integer calculateRandomNumber() {
		return 100000 + new Random().nextInt(900000);
	}
	public static final String getStackTrace(Throwable t) {	
		java.io.StringWriter s = new java.io.StringWriter();
		t.printStackTrace(new java.io.PrintWriter(s));
		return s.toString();
	}
	public static String corregirUri(String uri) {
		String uriCor = Funciones.corregirUriRec(uri); 
		return uriCor.replace(":/", "://");
	}
	
	private static String corregirUriRec(String uri) {
		if (uri.indexOf("//") >= 0) {
			return corregirUriRec(uri.replaceAll("//", "/"));
		} else {
			return uri;
		}
		
	}

	public static Map<String, String> tratarQueryString(String queryString) throws UnsupportedEncodingException {
		HashMap<String, String> params = new HashMap<String, String>();
		String[] atributos = queryString.split("&");
		for (int i = 0; i < atributos.length; i ++) {
			String[] keys = atributos[i].split("=");
			if (keys.length > 1) {
				params.put(keys[0], URLDecoder.decode(keys[1], CharEncoding.UTF_8));
			}
		}
		return params;
	}

	public static String getReferer(String referer) {
		String retorno = referer;
		if (referer.indexOf("/acceso/requirements") >= 0) {
			retorno = referer.substring(referer.indexOf("?r=") + 3, referer.length());
			try {
				retorno = URLDecoder.decode(retorno, CharEncoding.UTF_8);
			} catch (UnsupportedEncodingException e) {
				retorno = referer;
			}
			retorno = retorno.replace("/sede", "");
		}
		return retorno;
	}
	
	public static String getPathSecure(HttpServletRequest httprequest) {
		if (Propiedades.isLocal()) {
			return httprequest.getContextPath();
		} else {
			return "https://" + Propiedades.getPath() + httprequest.getContextPath();
		}
	}

	public static String getPathSecureWithoutContext() {
		if (Propiedades.isLocal()) {
			return "";
		} else {
			return "https://" + Propiedades.getPath();
		}
	}

	public static String convert(String texto) {
		String str = texto.replace("%22", "\"").replace("%20", " ");
		str = str.replace("Ã¡", "á");
		str = str.replace("Ã©", "é");
		str = str.replace("Ã­", "í");
		str = str.replace("Ã³", "ó");
		str = str.replace("Ãº", "ú");
		str = str.replace("Ã±", "ñ");
		StringBuilder ostr = new StringBuilder();
		for (int i = 0; i < str.length(); i++) {
			char ch = str.charAt(i);
			// Does the char need to be converted to unicode?
			if ((ch >= 0x0020) && (ch <= 0x007e)) {
				ostr.append(ch); // No.
			} else { // Yes.
				ostr.append("\\u"); // standard unicode format.
				String hex = Integer.toHexString(str.charAt(i) & 0xFFFF); // Get hex value of the char.
				for (int j = 0; j < 4 - hex.length(); j++) {
					// Prepend zeros because unicode requires 4 digits
					ostr.append("0");
				}
				ostr.append(hex.toLowerCase()); // standard unicode format.
			}
		}
		return (new String(ostr)); // Return the StringBuilder cast as a string.
	}

	public static HttpServletRequest getRequest() {
		return ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes())
				.getRequest();
	}
	
}

