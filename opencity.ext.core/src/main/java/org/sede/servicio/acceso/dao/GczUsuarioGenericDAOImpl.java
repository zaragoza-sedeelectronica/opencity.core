package org.sede.servicio.acceso.dao;

import java.math.BigDecimal;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.Table;
import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;

import net.sf.ehcache.Cache;
import net.sf.ehcache.CacheManager;
import net.sf.ehcache.Element;
import oracle.jdbc.OracleTypes;

import org.apache.commons.lang3.RandomStringUtils;
import org.hibernate.criterion.Restrictions;
import org.hibernate.jdbc.Work;
import org.sede.core.dao.JPAIgnoreTraversableResolver;
import org.sede.core.rest.Mensaje;
import org.sede.core.utils.AESSec;
import org.sede.core.utils.Funciones;
import org.sede.core.utils.MD5;
import org.sede.core.utils.PasswordGenerator;
import org.sede.core.utils.Propiedades;
import org.sede.core.utils.RandomGUID;
import org.sede.servicio.acceso.Constants;
import org.sede.servicio.acceso.entity.Credenciales;
import org.sede.servicio.acceso.entity.GczGrupoUsuario;
import org.sede.servicio.acceso.entity.GczUsuario;
import org.sede.servicio.acceso.entity.Grupo;
import org.sede.servicio.acceso.entity.Lider;
import org.sede.servicio.acceso.entity.Seccion;
import org.sede.servicio.acceso.entity.Servicio;
import org.sede.servicio.acceso.entity.Token;
import org.sede.servicio.acceso.entity.Usuario;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.googlecode.genericdao.dao.jpa.GenericDAOImpl;
// TODO: Auto-generated Javadoc

/**
 * The Class GczUsuarioGenericDAOImpl.
 * 
 * @autor Ayuntamiento de Zaragoza
 * 
 */
@Repository
@Transactional(Constants.TM)
public class GczUsuarioGenericDAOImpl extends GenericDAOImpl <GczUsuario, BigDecimal> implements GczUsuarioGenericDAO {
	
	/** Constant logger. */
	private static final Logger logger = LoggerFactory
			.getLogger(GczUsuarioGenericDAOImpl.class);
	
	/** Constant CODIGOGRUPOUSUARIO. */
	private static final int CODIGOGRUPOUSUARIO = 750;
	
	/** Constant SAFE. */
	public static final String SAFE = "SAFE";
	
	/**
	 * Sets the entity manager.
	 *
	 * @param entityManager the new entity manager
	 */
	@PersistenceContext(unitName=Constants.ESQUEMA)
	public void setEntityManager(EntityManager entityManager) {
		this.setEm(entityManager);
	}
	
	/**
	 * Gets the credenciales.
	 *
	 * @param login Login
	 * @return the credenciales
	 */
	public Credenciales getCredenciales(String login) {
		return getCredenciales(login, SAFE);
	}
	
	/**
	 * Validar.
	 *
	 * @param registro Registro
	 * @return sets the
	 */
	public Set<ConstraintViolation<Object>> validar(Object registro) {
		ValidatorFactory factory = Validation.byDefaultProvider().configure().traversableResolver(new JPAIgnoreTraversableResolver()).buildValidatorFactory();
		Validator validator = factory.getValidator();
		return validator.validate(registro);
	}
	
	/**
	 * Update visible.
	 *
	 * @param id Id
	 * @param value Value
	 * @return int
	 */
	public int updateVisible(BigDecimal id, String value) {
		String update = ", estado = 'P', bloqueado = 'S'";
		if ("S".equals(value)) {
			update = ", estado = 'B', bloqueado = 'N', num_intentos_fallidos = 0";
		}
		Query propWeb = em().createNativeQuery("update " + persistentClass.getAnnotation(Table.class).name() 
				+ " set gcz_publicado=?" + update + " where ID_USUARIO=?");
		propWeb.setParameter(1, value);
		propWeb.setParameter(2, id);
		return propWeb.executeUpdate();

	}
	
	/**
	 * New password.
	 *
	 * @param user User
	 * @return mensaje
	 */
	public Mensaje newPassword(GczUsuario user) {
		
		Query propWeb = em().createNativeQuery("update " + persistentClass.getAnnotation(Table.class).name() 
				+ " set CONTRASENNA=? where ID_USUARIO=?");
		
		PasswordGenerator oPasswordGenerator = new PasswordGenerator();
		String sPassword = oPasswordGenerator.generateIt();
		MD5 oMD5 = new MD5(sPassword);
		propWeb.setParameter(1, oMD5.compute());
		propWeb.setParameter(2, user.getId());
		int reg = propWeb.executeUpdate();
		if (reg > 0) {
			try {
				Funciones.sendMail("Web Municipal. Nueva contraseña", 
						"Su nueva contraseña como usuario: " + user.getLogin() + " es " + sPassword, 
						"API Ayuntamiento de Zaragoza<25232871@zaragoza.es>", 
						user.getCorreoElectronico(), "text/plain");
				return new Mensaje(HttpStatus.OK.value(), "Se ha generado y enviado la nueva contraseña al usuario " + user.getLogin());
			} catch (Exception e){
				return new Mensaje(HttpStatus.BAD_REQUEST.value(), "Se ha generado la nueva contraseña pero se ha producido un error al enviar el correo " + user.getCorreoElectronico() + ". " + e.getMessage()); 
			}
		} else {
			return new Mensaje(HttpStatus.BAD_REQUEST.value(), "No se ha modificado la contraseña");
		}

	}
	
	/**
	 * Gets the credenciales.
	 *
	 * @param login Login
	 * @param pwd Pwd
	 * @return the credenciales
	 */
	public Credenciales getCredenciales(String login, String pwd) {
		try {
			String estado = " and secc.estado='S'";
			GczUsuario gczUsuario;
	    	if (SAFE.equals(pwd)) {
	    		estado = "";
	    		if (obtenerDeCache(login) == null) {
	    			gczUsuario = (GczUsuario) this.getSession().createCriteria(GczUsuario.class)
		    			.add(Restrictions.eq("login", login))
		    			.add(Restrictions.eq("visible", "S"))
		    			.add(Restrictions.eq("bloqueado", "N"))
		    			.uniqueResult();
	    		} else {
	    			return obtenerDeCache(login);
	    		}
	    	} else {
	    		gczUsuario = (GczUsuario) this.getSession().createCriteria(GczUsuario.class)
		    			.add(Restrictions.eq("login", login))
		    			.add(Restrictions.eq("contrasenna", new MD5(pwd).compute()))
		    			.add(Restrictions.eq("visible", "S"))
		    			.add(Restrictions.eq("bloqueado", "N"))
		    			.uniqueResult();
	    	}
	    	Query queryPermisosPropios = this.em().createNativeQuery("select ser.codigo_servicio,ser.nombre as nombre_servicio, ser.codigo_seccion_defecto, "
	    			+ "secc.codigo_seccion, secc.nombre as nombre_seccion, g.codigo_grupo_operaciones, '' as codigo_grupo_usuario, '' as nombre_grupo_usuario " 
	    			+ "from gcz_servicio ser, gcz_seccion secc, gcz_grupo_operaciones g, gcz_perfil p, gcz_perfil_usuario pu, gcz_usuario u,GCZ_PERFIL_GRUPO_OPERACIONES operaci "
	    			+ "where ser.gcz_publicado='S'" + estado + " and secc.gcz_publicado='S' and g.gcz_publicado='S' and p.gcz_publicado='S' "
	    			+ "and secc.codigo_servicio=ser.codigo_servicio and g.codigo_seccion=secc.codigo_seccion and g.codigo_servicio=ser.codigo_servicio "
	    			+ "and operaci.id_perfil=p.id_perfil and operaci.id_grupo_operaciones=g.id_grupo_operaciones "
	    			+ "and g.codigo_servicio= p.codigo_servicio and pu.id_perfil= p.id_perfil and u.id_usuario= pu.id_usuario and u.login=? "
	    			+ "order by g.codigo_servicio, g.codigo_seccion");
	    	
	    	Query queryPermisosGrupo = this.em().createNativeQuery("select ser.codigo_servicio,ser.nombre as nombre_servicio, ser.codigo_seccion_defecto, "
	    			+ "secc.codigo_seccion, secc.nombre as nombre_seccion, g.codigo_grupo_operaciones, gu.codigo_grupo_usuario, gu.nombre "
	    			+ "from gcz_servicio ser, gcz_seccion secc, gcz_grupo_operaciones g, gcz_perfil p, gcz_perfil_grupo_usuario pu, "
	    			+ "gcz_grupo_usuario gu, gcz_usuario u, gcz_usuario_grupo_usuario ugu,GCZ_PERFIL_GRUPO_OPERACIONES gpu "
	    			+ "where gu.gcz_publicado='S' and ser.gcz_publicado='S'" + estado + " and secc.gcz_publicado='S' and g.gcz_publicado='S' and p.gcz_publicado='S' "
	    			+ "and secc.codigo_servicio=ser.codigo_servicio and g.codigo_seccion=secc.codigo_seccion and g.codigo_servicio=ser.codigo_servicio "
	    			+ "and g.codigo_servicio= p.codigo_servicio and pu.id_perfil= p.id_perfil and gu.id_grupo_usuario= pu.id_grupo_usuario "
	    			+ "and gpu.id_grupo_operaciones=g.id_grupo_operaciones and gpu.id_perfil=p.id_perfil "
	    			+ "and ugu.id_grupo_usuario= gu.id_grupo_usuario and ugu.id_usuario =u.id_usuario and u.login=? " 
	    			+ "order by g.codigo_servicio, g.codigo_seccion");
	    	
	    	Credenciales credenciales = new Credenciales();
	    	credenciales.setUsuario(gczUsuario.toUsuario());
	    	credenciales.getUsuario().setPropiedades(obtenerPropiedades(credenciales.getUsuario()));
	    	credenciales.setServicios(obtenerPermisos(queryPermisosPropios, login));
	    	credenciales.getServicios().addAll(obtenerPermisos(queryPermisosGrupo, login));
	    	credenciales.setLider(liderazgos(gczUsuario));
	    	credenciales.getGroup().addAll(grupos(gczUsuario.getGczGrupoUsuarios()));
	    	ponerEnCache(login, credenciales);
	        return credenciales;
	    } catch (NullPointerException e) {
	    	logger.error(e.getMessage());
	    	return new Credenciales();
	    } catch (Exception e) {
	    	logger.error(e.getMessage());
	    	return new Credenciales();
	    } 
    }

	/**
	 * Grupos.
	 *
	 * @param gczGrupoUsuarios Gcz grupo usuarios
	 * @return list
	 */
	private List<Grupo> grupos(
			List<GczGrupoUsuario> gczGrupoUsuarios) {
		List<Grupo> l = new ArrayList<Grupo>();
		for (GczGrupoUsuario g : gczGrupoUsuarios) {
			l.add(new Grupo(g.getCode(), g.getTitle(), g.getDescripcion()));
		}
		return l;
	}
	
	/**
	 * Liderazgos.
	 *
	 * @param gczUsuario Gcz usuario
	 * @return list
	 */
	private List<Lider> liderazgos(GczUsuario gczUsuario) {

		List<Lider> lider = new ArrayList<Lider>();

		StringBuilder grupos = new StringBuilder();
		for (GczGrupoUsuario grupo : gczUsuario.getGczGrupoUsuarios()) {
			if (grupos.length() > 0) {
				grupos.append(",");
			}
			grupos.append(grupo.getId());
		}

		if(grupos.length() > 0){
			Query q = em().createNativeQuery("select ID_LIDER, ID_ASOCIADO, TIPO_ASOCIADO, ID_USUARIO, TIPO_USUARIO "
					+ "from lideres "
					+ "where (id_usuario=" + gczUsuario.getId()
					+ " and tipo_usuario='" + GczUsuario.class.getName() + "') or "
					+ " (id_usuario in (" + grupos + ") and tipo_usuario = '" + GczGrupoUsuario.class.getName() + "')");
			@SuppressWarnings("unchecked")
			List<Object> lista = q.getResultList();
			for (Iterator<Object> iterador = lista.iterator(); iterador.hasNext();) {
				Object[] row = (Object[])iterador.next();
				Lider l = new Lider();
				l.setId((BigDecimal)row[0]);
				l.setAssociatedId((BigDecimal)row[1]);
				l.setAssociatedType((String)row[2]);
				l.setUserId((BigDecimal)row[3]);
				l.setUserType((String)row[4]);
				lider.add(l);
			}
		}

    	return lider;
	}
	
	/**
	 * Obtener propiedades.
	 *
	 * @param usuario Usuario
	 * @return hash map
	 */
	private HashMap<String, String> obtenerPropiedades(Usuario usuario) {
		Query queryPermisosGrupo = this.em().createNativeQuery("select etiqueta,valor from gcz_propiedad_usuario where id_usuario=?");
		@SuppressWarnings("unchecked")
		List<Object> permisos = queryPermisosGrupo.setParameter(1, usuario.getId()).getResultList();
		HashMap<String, String> propiedades = new HashMap<String, String>();
		for (Iterator<Object> iterador = permisos.iterator(); iterador.hasNext();) {
    		Object[] row = (Object[])iterador.next();
    		propiedades.put(row[0].toString(), row[1].toString());
		}
		if (!propiedades.isEmpty()) {
			return propiedades;
		} else {
			return null;
		}
	}

	/**
	 * Obtener de cache.
	 *
	 * @param id Id
	 * @return credenciales
	 */
	private Credenciales obtenerDeCache(String id) {
		CacheManager manager = CacheManager.getInstance();
		Cache cache = manager.getCache("usuarios");
		if (cache != null && cache.get(id) != null) {
			return (Credenciales) cache.get(id).getObjectValue();
		} else {
			return null;
		}
	}
	
	/**
	 * Borrar de cache.
	 *
	 * @param id Id
	 */
	private void borrarDeCache(String id) {
		CacheManager manager = CacheManager.getInstance();
		Cache cache = manager.getCache("usuarios");
		if (cache != null && cache.get(id) != null) {
			cache.remove(id);
		}
	}
	
	
	/**
	 * Poner en cache.
	 *
	 * @param id Id
	 * @param credenciales Credenciales
	 */
	private void ponerEnCache(String id, Credenciales credenciales) {
		CacheManager manager = CacheManager.getInstance();
		Cache cache = manager.getCache("usuarios");
		if (cache != null) {
			cache.put(new Element(id, credenciales));
		}
	}

	/**
	 * Obtener permisos.
	 *
	 * @param queryPermisosPropios Query permisos propios
	 * @param id Id
	 * @return list
	 */
	private List<Servicio> obtenerPermisos(Query queryPermisosPropios, String id) {
		
		@SuppressWarnings("unchecked")
		List<Object> permisos = queryPermisosPropios.setParameter(1, id).getResultList();
    	List<Servicio> listado = new ArrayList<Servicio>();
    	Servicio servicioActual = new Servicio("","","");
    	Seccion seccionActual = new Seccion("","","","","");
    	
    	for (Iterator<Object> iterador = permisos.iterator(); iterador.hasNext();) {
    		Object[] row = (Object[])iterador.next();
    		
    		if (!seccionActual.getCodigoSeccion().equals(row[3].toString())) {
    			if (!"".equals(seccionActual.getNombre())) {
    				servicioActual.getSecciones().add(seccionActual);
    			}
    			seccionActual = new Seccion(row[4].toString(), row[0].toString(), row[3].toString(), (row[6] == null ? null : row[6].toString()), (row[7] == null ? null : row[7].toString()));
    		}
			seccionActual.getPermisos().add(row[5].toString());
    		
    		
    		if (!servicioActual.getCodigoServicio().equals(row[0].toString())) {
    			if (!"".equals(servicioActual.getNombre())) {
    				listado.add(servicioActual);
    			}
    			servicioActual = new Servicio(row[1].toString(), row[0].toString(), row[2] == null ? null : row[2].toString());
    		}
		}
    	
    	if (!"".equals(servicioActual.getNombre())) {
    		servicioActual.getSecciones().add(seccionActual);
			listado.add(servicioActual);
		}
    	
    	return listado;
	}

	/**
	 * Sets the pk.
	 *
	 * @param id Id
	 * @param pk Pk
	 * @return true, if successful
	 */
	public boolean setPk(String id, String pk) {
		try {
			
			Query update = this.em().createNativeQuery("update gcz_usuario set secretKey = ? where login = ?");
			
			update.setParameter(1, AESSec.encrypt(pk));
			update.setParameter(2, id);
			if (update.executeUpdate() > 0) {
				borrarDeCache(id);
				return true;
			} else {
				return false;
			}
		} catch (Exception e) {
			logger.error(e.getMessage());
			return false;
		}
	}

	/**
	 * Update.
	 *
	 * @param id Id
	 * @param usuario Usuario
	 * @param password Password
	 * @return true, if successful
	 */
	public boolean update(String id, Usuario usuario, String password) {
		try {
			StringBuilder sqlUpdate = new StringBuilder();
			sqlUpdate.append("update gcz_usuario set ");
			if (usuario.getNombre() != null) {
				sqlUpdate.append(" NOMBRE = ? , ");
			}
			if (usuario.getApellido1() != null) {
				sqlUpdate.append(" APELLIDO_1 = ? , ");
			}
			
			if (usuario.getApellido2() != null) {
				sqlUpdate.append(" APELLIDO_2 = ? , ");
			}
			if (usuario.getEmail() != null) {
				sqlUpdate.append(" CORREO_ELECTRONICO = ? , ");
			}
			
			if (password != null && password.length() > 2) { 
				sqlUpdate.append(" CONTRASENNA = ? , ");
			}
			int len = sqlUpdate.length();
			if (len > 0) {
				int index = sqlUpdate.lastIndexOf(",");
				if (index == -1) {
					index = sqlUpdate.length();
				}
				String where = sqlUpdate.substring(0, index - 1);
				sqlUpdate = new StringBuilder(where);
			}
			sqlUpdate.append(" where login = ?");
			Query update = this.em().createNativeQuery(sqlUpdate.toString());
			int i = 0;
			if (usuario.getNombre() != null) {
				update.setParameter(++i, usuario.getNombre());
			}
			if (usuario.getApellido1() != null) {
				update.setParameter(++i, usuario.getApellido1());
			}
			if (usuario.getApellido2() != null) {
				update.setParameter(++i, usuario.getApellido2());	
			}
			if (usuario.getEmail() != null) {
				update.setParameter(++i, usuario.getEmail());
			}			
			if (password != null && password.length() > 2) { 
				update.setParameter(++i,  new MD5(password).compute());	
			}
			update.setParameter(++i, id);
			if (update.executeUpdate() > 0) {
				borrarDeCache(id);
				if (usuario.getNombre() != null) {
					Query deletePropWeb = this.em().createNativeQuery("delete from GCZ_PROPIEDAD_USUARIO where etiqueta='" + Usuario.PROPWEB + "' and id_usuario in (select id_usuario from gcz_usuario where login=?)");
					deletePropWeb.setParameter(1, id);
					deletePropWeb.executeUpdate();
					if (usuario.getPropiedades() != null && usuario.getPropiedades().get(Usuario.PROPWEB) != null && !"".equals(usuario.getPropiedades().get(Usuario.PROPWEB))) {
						Query propWeb = this.em().createNativeQuery("insert into GCZ_PROPIEDAD_USUARIO(id_usuario, etiqueta,valor) "
								+ "(select id_usuario,'" + Usuario.PROPWEB + "',? from gcz_usuario where login=?)");
						propWeb.setParameter(1, usuario.getPropiedades().get(Usuario.PROPWEB));
						propWeb.setParameter(2, id);
						if (propWeb.executeUpdate() > 0) {
							return true;
						} else {
							this.getSession().getTransaction().rollback();
							return false;
						}
					} else {
						return true;
					}
				} else {
					Query deleteToken = this.em().createNativeQuery("delete from GCZ_USUARIO_RECUPERAR_PASS where id_usuario in (select id_usuario from gcz_usuario where login=?)");
					deleteToken.setParameter(1, id);
					deleteToken.executeUpdate();
					return true;
				}
			} else {
				return false;
			}
		} catch (Exception e) {
			return false;
		}
	}
	
	
	/**
	 * Crear.
	 *
	 * @param usuario Usuario
	 * @param password Password
	 * @return string
	 */
	public String crear(Usuario usuario, String password) {
		
		try {
			
			if (!usuarioExistente(usuario)) {
			
				StringBuilder sqlUpdate = new StringBuilder();
				sqlUpdate.append("insert into gcz_usuario (ID_USUARIO,"
					+ "LOGIN,"
					+ "NOMBRE,"
					+ "APELLIDO_1,"
					+ "APELLIDO_2,"
					+ "CONTRASENNA,"
					+ "CORREO_ELECTRONICO,"
					+ "ESTADO,"
					+ "BLOQUEADO,"
					+ "CODIGO_SERVICIO_DEFECTO,"
					+ "CODIGO_SECCION_DEFECTO,"
					+ "NUM_INTENTOS_FALLIDOS,"
					+ "FECHA_ULTIMO_ACCESO,"
					+ "GCZ_PUBLICADO,"
					+ "GCZ_FECHAALTA,"
					+ "GCZ_FECHAMOD,"
					+ "GCZ_FECHAPUB,"
					+ "GCZ_USUARIOALTA,"
					+ "GCZ_USUARIOMOD,"
					+ "GCZ_USUARIOPUB,"
					+ "ADMINISTRADOR,"
					+ "SECRETKEY) values (GCZ_USUARIO_SEQ.nextval,?,?,?,?,?,?,'B','N','ERRONEA','ERRONEA',0,null,'S',sysdate,null,null,'admin',null,null,'N',?)");
				
				Query update = this.em().createNativeQuery(sqlUpdate.toString());
				int i = 0;
				update.setParameter(++i, usuario.getLogin());
				update.setParameter(++i, usuario.getNombre());
				update.setParameter(++i, usuario.getApellido1());
				update.setParameter(++i, usuario.getApellido2());
				update.setParameter(++i,  new MD5(password).compute());
				update.setParameter(++i, usuario.getEmail());	
				update.setParameter(++i, AESSec.encrypt(usuario.getSecretKey()));
				if (update.executeUpdate() > 0) {
					Query grupo = this.em().createNativeQuery("insert into GCZ_USUARIO_GRUPO_USUARIO (id_grupo_usuario,id_usuario) "
							+ "values(" + CODIGOGRUPOUSUARIO + ", GCZ_USUARIO_SEQ.currval)");
					if (grupo.executeUpdate() > 0) {
						
						if (usuario.getPropiedades() != null && usuario.getPropiedades().get(Usuario.PROPWEB) != null && !"".equals(usuario.getPropiedades().get(Usuario.PROPWEB))) {
							Query propWeb = this.em().createNativeQuery("insert into GCZ_PROPIEDAD_USUARIO(id_usuario, etiqueta,valor) values(GCZ_USUARIO_SEQ.currval,'" + Usuario.PROPWEB + "',?)");
							propWeb.setParameter(1, usuario.getPropiedades().get(Usuario.PROPWEB));
							if (propWeb.executeUpdate() > 0) {
								return "";
							} else {
								this.getSession().getTransaction().rollback();
								return "No se ha podido dar de alta al usuario";
							}
						} else {
							return "";
						}
					} else {
						this.getSession().getTransaction().rollback();
						return "No se ha podido dar de alta al usuario";
					}
				} else {
					this.getSession().getTransaction().rollback();
					return "No se ha podido dar de alta al usuario";
				}
			} else {
				return "Nombre de usuario existente, por favor,  escoja otro";
			}
		} catch (Exception e) {
			this.getSession().getTransaction().rollback();
			if (e.getCause().getMessage().indexOf("ORA-00001") >= 0) {
				return "Nombre de usuario existente, por favor,  escoja otro";
			}
			return "No se ha podido dar de alta al usuario";
		}
	}
	
	/**
	 * Removes the.
	 *
	 * @param login Login
	 * @return true, if successful
	 */
	public boolean remove(String login) {
		
		try {
			this.getSession().beginTransaction();
			
			Query grupo = this.em().createNativeQuery("delete from GCZ_USUARIO where login=?");
			grupo.setParameter(1, login);
			if (grupo.executeUpdate() > 0) {
				this.getSession().getTransaction().commit();
				return true;
			} else {
				this.getSession().getTransaction().rollback();
				return false;
			}
		
		} catch (Exception e) {
			this.getSession().getTransaction().rollback();
			return false;
		}
	}
	
	
	/**
	 * Usuario existente.
	 *
	 * @param usuario Usuario
	 * @return true, if successful
	 */
	public boolean usuarioExistente(Usuario usuario) {
		
		Query query = this.em().createNativeQuery("select login from GCZ_USUARIO where upper(login) like ?");
		@SuppressWarnings("unchecked")
		List<Object> lista = query.setParameter(1, usuario.getLogin().toUpperCase()).getResultList();
		if (!lista.isEmpty()) {
			return true;
		} else {
			return false;
		}
		
	}
	
	/**
	 * Generar token.
	 *
	 * @param clientId Client id
	 * @param idAplicacion Id aplicacion
	 * @return object
	 */
	public Object generarToken(final String clientId, final String idAplicacion) {
		// TODO comprobar que la aplicación existe y es del usuario y preparar anulación de acceso a aplicacion
		
		final StringBuilder mensaje = new StringBuilder();
		final Token token = new Token();
			
		this.getSession().doWork(new Work() {
			public void execute(Connection con) throws SQLException {
				CallableStatement st = null;
				String tk = RandomStringUtils.random(70, true, true);
				try {			
					st = con.prepareCall(crearStatement("TOKEN", "GENERAR", 4));
					st.setString(1, clientId);
					st.setString(2, tk);
					st.setString(3, idAplicacion);
					st.registerOutParameter(4, OracleTypes.DATE);
		            st.execute();
		            token.setVerifier(AESSec.encrypt(clientId + "###" + idAplicacion));
		            token.setAccess(tk);
		            token.setExpiration_date(st.getDate(4));
				} catch (SQLException e) {
					switch (e.getErrorCode()) {
	                case 20002:
	                	mensaje.append("Aplicacion inexistente");
	                	break;
	                default:
	                	mensaje.append(e.getMessage());
					}						
				} catch (Exception e) {
					logger.error(e.getMessage()); 
				} finally {
					if (st != null) {
						st.close();
					}
				
				}
			}
		});
		if (mensaje.length() > 0) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new Mensaje(HttpStatus.NOT_FOUND.value(), mensaje.toString()));
		} else {
			return token;
		}
		
	}
	
	/**
	 * Crear statement.
	 *
	 * @param modulo Modulo
	 * @param proc Proc
	 * @param numParametros Num parametros
	 * @return string
	 */
	protected String crearStatement(String modulo,String proc,int numParametros) {
		StringBuilder sb = new StringBuilder("{call ");
		sb.append("PCK_USUARIO_" + modulo);
		sb.append(".");
		sb.append(proc);
		sb.append("(");
		for (int i = 1; i <= numParametros; i++) {
			if (i > 1)
				sb.append(",");
			sb.append("?");
		}
		return sb.append(")}").toString();
		
	 }

	/**
	 * Enviar token recuperacion.
	 *
	 * @param email Email
	 * @return mensaje
	 */
	public Mensaje enviarTokenRecuperacion(String email) {
		String sql = "select id_usuario,login from gcz_usuario where trim(lower(correo_electronico)) = ?";
		Query query = this.em().createNativeQuery(sql);
		Object[] obj = (Object[])query.setParameter(1, email.toLowerCase()).getSingleResult();
		if (obj != null) {
			String token = new RandomGUID().toString();
			BigDecimal id = (BigDecimal) obj[0]; 
			String login = (String) obj[1];
			Query deletePropWeb = this.em().createNativeQuery("delete from gcz_usuario_recuperar_pass where id_usuario=?");
			deletePropWeb.setParameter(1, id);
			deletePropWeb.executeUpdate();

			Query propWeb = this.em().createNativeQuery("insert into gcz_usuario_recuperar_pass(id_usuario,token) values(?, ?)");
			propWeb.setParameter(1, id);
			propWeb.setParameter(2, token);
			propWeb.executeUpdate();
			
			String texto = "Nombre de usuario: " + login + ". \nAcceda a la siguiente url para reestablecer su contraseña: https://www.zaragoza.es/sede/acceso/reset?token=" + token;
			try {
				Funciones.sendMail("Plataforma de Gobierno Abierto de Zaragoza. Recuperación de contraseña", 
						texto, 
						email,
						"API Ayuntamiento de Zaragoza<" + Propiedades.getMailUser() + "@zaragoza.es>",
						"text/plain");
				return new Mensaje(HttpStatus.OK.value(), "Se ha enviado al correo electrónico un enlace para actualizar su contraseña");
				
			} catch (Exception e) {
				return new Mensaje(HttpStatus.BAD_REQUEST.value(), "Se produjo un error al enviar el correo electrónico");	
			}
		} else {
			return new Mensaje(HttpStatus.BAD_REQUEST.value(), "No existen usuarios con la dirección de correo indicada");
		}

		
	}

	/**
	 * Obtener login de token.
	 *
	 * @param token Token
	 * @return string
	 */
	public String obtenerLoginDeToken(String token) {

		String sql = "select u.login from gcz_usuario u, GCZ_USUARIO_RECUPERAR_PASS p where u.id_usuario=p.id_usuario and p.token=?";
		Query query = this.em().createNativeQuery(sql);
		String obj = (String)query.setParameter(1, token).getSingleResult();
		if (obj == null) {
			return "";
		} else {
			return obj;
		}
		
	}

	
}
