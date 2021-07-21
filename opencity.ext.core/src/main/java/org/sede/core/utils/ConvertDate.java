package org.sede.core.utils;

import java.lang.ref.SoftReference;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.TimeZone;

import org.apache.http.impl.cookie.DateUtils;

public class ConvertDate {
	 
    public static final String PATTERN_RFC1123 = "EEE, dd MMM yyyy HH:mm:ss zzz";
    public static final String PATTERN_RFC850 = "dd-MMM-yyyy HH:mm:ss zzz";
    public static final String PATTERN_RFC1036 = "EEEE, dd-MMM-yy HH:mm:ss zzz";
    public static final String PATTERN_ASCTIME = "EEE MMM d HH:mm:ss yyyy";
	
    public static final String DATEQUERY = "EEE MMM dd HH:mm:ss zzz yyyy";
    
    public static final String DATE_FORMAT = "dd-MM-yyyy";
    //MARCA SEDE_MODULARIZA AGENDACONSTITUCIONAL CLASE (AgendaInstitucionalController)
    public static final String DATE_FORMAT_PATTERN = "dd-MM-yyyy";
    public static final String DATETIME_FORMAT_PATTERN = "dd-MM-yyyy HH:mm";
    /** DATE_FORMAT */
    public static final String DATEEN_FORMAT = "yyyy-MM-dd";
    public static final String DATETIMEEN_FORMAT = "yyyy-MM-dd HH:mm";
    public static final String MONTH_FORMAT = "yyyy-MM";
    
    public static final String HOUR_FORMAT = "HH:mm";
    /** DATE_FORMAT */
    public static final String DATE_FORMAT_BARRA = "dd/MM/yyyy";
    public static final String DATE_FORMAT_VIRTUOSO = "yyyy-MM-dd";
    /** DATETIME_FORMAT */
    public static final String DATETIME_FORMAT = "dd-MM-yyyy HH:mm";
    public static final String DATETIME_FORMAT_BARRA = "dd/MM/yyyy HH:mm";
    public static final String DATETIMESIGE_FORMAT = "dd/MM/yyyy HH:mm:ss";
    /** DATEMONTHYEAR_FORMAT */
    public static final String DATEMONTHYEAR_FORMAT = "MM-yyyy";
    public static final String ISO8601_FORMAT = "yyyy-MM-dd'T'HH:mm:ss'Z'";
    public static final String ISO8601_FORMAT_SIN_ZONA = "yyyy-MM-dd'T'HH:mm:ss";
    
    public static final String ISO8601MS_FORMAT = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'";
    
    public static final String RFC_5545_FORMAT = "yyyyMMdd'T'HHmmss";

    public static final String FORMATLARGO = "yyyyMMdd-HHmmss";

    public static final String FORMATTEXTO = "MMMM yyyy";
    
    public static final String FORMATCITAPREVIA = "yyyyMMdd";
    
    public static final String DATE_FORMAT_FICHERO = "dd-MM-yyyy_HHmm";
    
    public static final String PERFILCONTRATANTE = "'Hasta las 'HH:mm' del 'dd-MM-yyyy";
    
    public static final String INFORME_SUBASTA = "dd 'de' MMMM 'de' yyyy', siendo las' HH:mm 'horas'";
    
    public static final String DF_DEFECTO = "dd MMMM yyyy";

    public static final String TEXTO = "EEEE d 'de' MMMM";
    
    public static final String TEXTO_DIA_DE_MES = " d 'de' MMMM";

    public static final String TEXTO_DIA_NUM = "dd";

    public static final String TEXTO_DIA = "EEEE";

    public static final String TEXTO_DIA_TXT_NUM = "EEEE dd";
    public static final String TEXTO_MES = "MMMM";
    public static final String YEAR = "yyyy";
    
    public static final String TEXTO_MES_YEAR = "MMMM yyyy";

    public static final String TEXTO_YEAR_SIN_DIA = "dd 'de' MMMM 'de' yyyy";
    public static final String TEXTO_YEAR = "EEEE dd 'de' MMMM 'de' yyyy";
    public static final String TEXTO_HOUR = "EEEE dd 'de' MMMM 'de' yyyy HH:mm";
    
    public static final TimeZone GMT = TimeZone.getTimeZone("GMT");
    private static final String[] DEFAULT_PATTERNS = new String[] {
    	PATTERN_RFC1036,
    	PATTERN_RFC1123,
        PATTERN_ASCTIME
    };
    private static final Date DEFAULT_TWO_DIGIT_YEAR_START;

    static {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeZone(GMT);
        calendar.set(2000, Calendar.JANUARY, 1, 0, 0, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        DEFAULT_TWO_DIGIT_YEAR_START = calendar.getTime();
    }
    /** This class should not be instantiated. */
    private ConvertDate() {
    }

    /**
     * Convierte una fecha a una cadena.
     *
     * @param date  Fecha
     * @param dt SimpleDateFormat
     * @return Cadena Fecha formateada
     */
    public static String date2String(Date date, String dateFormat) {
    	SimpleDateFormat dt = new SimpleDateFormat(dateFormat);
    	if (date != null) {
    		return dt.format(date);
    	} else {
    		return "";
    	}
    }
    public static String date2String(Date date, String dateFormat, Locale l) {
    	SimpleDateFormat dt = new SimpleDateFormat(dateFormat, l);
    	if (date != null) {
    		return dt.format(date);
    	} else {
    		return "";
    	}
    }
    public static String date2StringGmt(Date date) {
    	if (date != null) {
    		return DateUtils.formatDate(date);
    	} else {
    		return "";
    	}
    	
    }
    
    /**
     * Convierte una cadena a una fecha.
     *
     * @param dateString Cadena a convertir
     * @param dt SimpleDateFormat
     * @return  Fecha
     * @throws java.text.ParseException Si la cadena no puede ser formateada
     */
    public static Date string2Date(String dateString, String dateFormat) throws ParseException {
    	SimpleDateFormat dt = new SimpleDateFormat(dateFormat); 
    	if (dateString != null) {
	    	if (dateString.length() < 16 && dateFormat == DATETIME_FORMAT) {
	    		dateString = dateString.substring(0, 9) + " 00:00";
	    	} else if(dateString.length() < 29 && dateFormat == ISO8601MS_FORMAT) {
	    		dateString = dateString + "T23:59:00.000Z";
	    	}
	        return dt.parse(dateString);
    	} else {
    		return null;
    	}
    }
    
    /**
     * Añade dias a una fecha
     * @param dt fecha
     * @param days dias
     * @return  Date
     */
    public static Date addDaysToDate(Date dt, int days) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime (dt);
        calendar.add(Calendar.DAY_OF_YEAR, days);
        return calendar.getTime();
    }
    
    public static Date getDateHoy() {
        return Calendar.getInstance().getTime();
    }
    
    public static Date getFechaHoy() {
           final Calendar cal    = Calendar.getInstance();
           cal.set(Calendar.HOUR, 0);
           cal.set(Calendar.HOUR_OF_DAY, 0);
           cal.set(Calendar.MINUTE, 0);
           cal.set(Calendar.SECOND, 0);
           cal.set(Calendar.MILLISECOND, 0);
           return cal.getTime();

    }

    public static String getDiaSemana(int dia){
        String[] dias	 = new String[8];
        dias[1] = "Domingo";
        dias[2] = "Lunes";
        dias[3] = "Martes";
        dias[4] = "Miércoles";
        dias[5] = "Jueves";
        dias[6] = "Viernes";
        dias[7] = "Sábado";
        return dias[dia];
    }

    public static String getMes(final int mes) {
        String strReturn = "";
        switch (mes) {
            case 0:
                strReturn = "Enero";
                break;
            case 1:
                strReturn = "Febrero";
                break;
            case 2:
                strReturn = "Marzo";
                break;
            case 3:
                strReturn = "Abril";
                break;
            case 4:
                strReturn = "Mayo";
                break;
            case 5:
                strReturn = "Junio";
                break;
            case 6:
                strReturn = "Julio";
                break;
            case 7:
                strReturn = "Agosto";
                break;
            case 8:
                strReturn = "Septiembre";
                break;
            case 9:
                strReturn = "Octubre";
                break;
            case 10:
                strReturn = "Noviembre";
                break;
            case 11:
                strReturn = "Diciembre";
                break;
        }
        return strReturn;
    }
    
    /**
     * Parses a date value.  The formats used for parsing the date value are retrieved from
     * the default http params.
     *
     * @param dateValue the date value to parse
     *
     * @return the parsed date
     *
     * @throws DateParseException if the value could not be parsed using any of the
     * supported date formats
     */
    public static Date parseDate(String dateValue) throws ParseException {
        return parseDate(dateValue, null, null);
    }
    
    /**
     * Parses the date value using the given date formats.
     *
     * @param dateValue the date value to parse
     * @param dateFormats the date formats to use
     *
     * @return the parsed date
     *
     * @throws DateParseException if none of the dataFormats could parse the dateValue
     */
    public static Date parseDate(final String dateValue, String[] dateFormats)
        throws ParseException {
        return parseDate(dateValue, dateFormats, null);
    }

    /**
     * Parses the date value using the given date formats.
     *
     * @param dateValue the date value to parse
     * @param dateFormats the date formats to use
     * @param startDate During parsing, two digit years will be placed in the range
     * <code>startDate to startDate + 100 years. This value may
     * be <code>null. When null is given as a parameter, year
     * <code>2000 will be used.
     *
     * @return the parsed date
     *
     * @throws DateParseException if none of the dataFormats could parse the dateValue
     */
    public static Date parseDate(
        String dateValue,
        String[] dateFormats,
        Date startDate
    ) throws ParseException {

        if (dateValue == null) {
            throw new IllegalArgumentException("dateValue is null");
        }
        if (dateFormats == null) {
            dateFormats = DEFAULT_PATTERNS;
        }
        if (startDate == null) {
            startDate = DEFAULT_TWO_DIGIT_YEAR_START;
        }
        // trim single quotes around date if present
        // see issue #5279
        if (dateValue.length() > 1
            && dateValue.startsWith("'")
            && dateValue.endsWith("'")
        ) {
            dateValue = dateValue.substring (1, dateValue.length() - 1);
        }

        for (String dateFormat : dateFormats) {
            SimpleDateFormat dateParser = DateFormatHolder.formatFor(dateFormat);
            dateParser.set2DigitYearStart(startDate);

            try {
                return dateParser.parse(dateValue);
            } catch (ParseException pe) {
                // ignore this exception, we will try the next format
            }
        }

        // we were unable to parse the date
        throw new ParseException("Unable to parse the date " + dateValue, 0);
    }
    
    /**
     * Formats the given date according to the RFC 1123 pattern.
     *
     * @param date The date to format.
     * @return An RFC 1123 formatted date string.
     *
     * @see #PATTERN_RFC1123
     */
    public static String formatDate(Date date) {
        return formatDate(date, PATTERN_RFC1123);
    }
    
    /**
     * Formats the given date according to the specified pattern.  The pattern
     * must conform to that used by the {@link SimpleDateFormat simple date
     * format} class.
     *
     * @param date The date to format.
     * @param pattern The pattern to use for formatting the date.
     * @return A formatted date string.
     *
     * @throws IllegalArgumentException If the given date pattern is invalid.
     *
     * @see SimpleDateFormat
     */
    public static String formatDate(Date date, String pattern) {
        if (date == null) throw new IllegalArgumentException("date is null");
        if (pattern == null) throw new IllegalArgumentException("pattern is null");

        SimpleDateFormat formatter = DateFormatHolder.formatFor(pattern);
        return formatter.format(date);
    }
    
    /**
     * A factory for {@link SimpleDateFormat}s. The instances are stored in a
     * threadlocal way because SimpleDateFormat is not threadsafe as noted in
     * {@link SimpleDateFormat its javadoc}.
     * 
     */
    @SuppressWarnings("rawtypes")
    static final class DateFormatHolder {
    	private DateFormatHolder() {
    		super();
    	}
        private static final ThreadLocal<SoftReference> 
            THREADLOCAL_FORMATS = new ThreadLocal<SoftReference>() {

            @Override
            protected SoftReference<Map> initialValue() {
                return new SoftReference<Map>(
                        new HashMap<String, SimpleDateFormat>());
            }
            
        };

        /**
         * creates a {@link SimpleDateFormat} for the requested format string.
         * 
         * @param pattern
         *            a non-<code>null format String according to
         *            {@link SimpleDateFormat}. The format is not checked against
         *            <code>null since all paths go through
         *            {@link DateUtils}.
         * @return the requested format. This simple dateformat should not be used
         *         to {@link SimpleDateFormat#applyPattern(String) apply} to a
         *         different pattern.
         */
        @SuppressWarnings("unchecked")
        public static SimpleDateFormat formatFor(String pattern) {
            SoftReference<Map> ref = THREADLOCAL_FORMATS.get();
            Map<String, SimpleDateFormat> formats = ref.get();
            if (formats == null) {
                formats = new HashMap<String, SimpleDateFormat>();
                THREADLOCAL_FORMATS.set(
                        new SoftReference<Map>(formats));    
            }

            SimpleDateFormat format = formats.get(pattern);
            if (format == null) {
                format = new SimpleDateFormat(pattern, Locale.US);
                format.setTimeZone(TimeZone.getTimeZone("GMT"));
                formats.put(pattern, format);
            }

            return format;
        }
        
    }

    public static Date string2Date(String dateString, String dateFormat, Locale locale) throws ParseException {
    	SimpleDateFormat dt = new SimpleDateFormat(dateFormat, locale); 
    	if (dateString != null) {
	    	if (dateString.length() < 16 && dateFormat == DATETIME_FORMAT) {
	    		dateString = dateString.substring(0, 9) + " 00:00";
	    	} else if(dateString.length() < 29 && dateFormat == ISO8601MS_FORMAT) {
	    		dateString = dateString + "T23:59:00.000Z";
	    	}
	        return dt.parse(dateString);
    	} else {
    		return null;
    	}
    }
}


