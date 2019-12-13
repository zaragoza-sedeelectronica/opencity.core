package org.sede.servicio.padron.dao;


public interface PadronGenericDAO {
	public Boolean checkEmpadronado(String d, Integer a);
	public String showDistrito(String d, Integer a);
	public String showJunta(String d, Integer a);
}
