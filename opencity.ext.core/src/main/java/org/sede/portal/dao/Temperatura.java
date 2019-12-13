package org.sede.portal.dao;

import org.sede.core.utils.ConvertDate;

import javax.xml.bind.annotation.XmlRootElement;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@XmlRootElement(name = "temperatura")
public class Temperatura {

    public static final Map<String, String> alternativas = new HashMap<String, String>();
    static {
        alternativas.put("11", "Soleado");
        alternativas.put("12", "Poco Nuboso");
        alternativas.put("13", "Intervalos nubosos");
        alternativas.put("14", "Nuboso");
        alternativas.put("15", "Muy nuboso");
        alternativas.put("24", "Nuboso con lluvia");
        alternativas.put("25", "Muy nuboso con lluvia");
    }

    private int max;
    private int min;
    private int icon;
    private Date fecha;

    public Temperatura() {
    }

    public int getMax() {
        return max;
    }
    public void setMax(int max) {
        this.max = max;
    }
    public int getMin() {
        return min;
    }
    public void setMin(int min) {
        this.min = min;
    }
    public int getIcon() {
        return icon;
    }
    public void setIcon(int icon) {
        this.icon = icon;
    }
    public Date getFecha() {
        return fecha;
    }
    public void setFecha(Date fecha) {
        this.fecha = fecha;
    }

    public Temperatura(int max, int min, int icon, Date fecha) {
        super();
        this.max = max;
        this.min = min;
        this.icon = icon;
        this.fecha = fecha;
    }

    public String getDia() {
        return ConvertDate.date2String(new Date(), ConvertDate.TEXTO_DIA_NUM);
    }

    public String getDiaSemana() {
        return ConvertDate.date2String(new Date(), ConvertDate.TEXTO_DIA);
    }

    public String getPrimeraLetraMes() {
        Calendar  c = Calendar.getInstance();
        int mes = c.get(Calendar.MONTH);
        return ConvertDate.getMes(mes).substring(0, 3).toUpperCase() + ".";
    }

    public String getFechaFormateada() {
        return ConvertDate.date2String(new Date(), ConvertDate.DATE_FORMAT);
    }

    public String getAlternativa() {
        return alternativas.get("" + getIcon());

    }

}
