package org.sede.core.db;

import java.sql.SQLException;

import javax.naming.InitialContext;
import javax.naming.NamingException;

import oracle.jdbc.pool.OracleConnectionPoolDataSource;

import org.sede.core.PropiedadesTest;


public class DataSource {
	
	public String jndi;
	public String usuario;
	public String password;
	
	public DataSource(String jndi, String usuario, String passowrd) {
		super();
		this.jndi = jndi;
		this.usuario = usuario;
		this.password = passowrd;
	}
	
	
	public static OracleConnectionPoolDataSource crearDataSource(DataSource esquema) throws SQLException, NamingException{
		InitialContext ic = new InitialContext();
		try {
			ic.createSubcontext("java:");
            ic.createSubcontext("java:/comp");
            ic.createSubcontext("java:/comp/env");
            ic.createSubcontext("java:/comp/env/jdbc");
		} catch (Exception e) {
			;
		}
		// Construct DataSource
		OracleConnectionPoolDataSource ds = new OracleConnectionPoolDataSource();
//		if (DataSource.REGLIC.usuario.equals(esquema.usuario)) {
//			System.out.println(PropiedadesTest.getString("conexion.gurz.jdbc"));
//			ds.setURL(PropiedadesTest.getString("conexion.gurz.jdbc"));
//		} else {
			ds.setURL(PropiedadesTest.getString("conexion.jdbc"));
//		}
		ds.setUser(esquema.usuario);
		ds.setPassword(esquema.password);

		return ds;
	}
	
}
