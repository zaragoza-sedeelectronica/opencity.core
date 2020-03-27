
package org.sede.servicio.acceso.dao;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.PostLoad;

import org.sede.core.anotaciones.Esquema;
import org.sede.core.dao.AutowireHelper;
import org.sede.servicio.acceso.ConfigCiudadano;
import org.sede.servicio.acceso.entity.Ciudadano;
import org.sede.servicio.acceso.entity.GczGrupoUsuario;
import org.sede.servicio.acceso.entity.GczUsuario;
import org.sede.servicio.acceso.entity.Lider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * The listener interface for receiving lider events.
 * The class that is interested in processing a lider
 * event implements this interface, and the object created
 * with that class is registered with a component using the
 * component's <code>addLiderListener<code> method. When
 * the lider event occurs, that object's appropriate
 * method is invoked.
 *
 * @see LiderEvent
 * 
 * @autor Ayuntamiento de Zaragoza
 */
public class LiderListener {

	/** em noticias. */
	@PersistenceContext(unitName = ConfigCiudadano.ESQUEMA)
    EntityManager emNoticias;

	/** em movil. */
	@PersistenceContext(unitName = ConfigCiudadano.ESQUEMA)
    EntityManager emMovil;

	/** em actividades. */
	@PersistenceContext(unitName = Esquema.AGENDA)
    EntityManager emActividades;

	/** em intra. */
	@PersistenceContext(unitName = Esquema.INTRA)
    EntityManager emIntra;

    /** Constant logger. */
    private static final Logger logger = LoggerFactory.getLogger(LiderListener.class);

    /**
     * Pre persist.
     *
     * @param lider Lider
     */
    @PostLoad
    public void prePersist(Lider lider) {
        if (lider.getAssociatedId() != null) {
            try {
        		AutowireHelper.autowire(this, emNoticias);
        		AutowireHelper.autowire(this, emMovil);
                AutowireHelper.autowire(this, emActividades);
                AutowireHelper.autowire(this, emIntra);
// FIXME modularizacion
//                // Obtener el recurso liderado según la clase asociada
//            	if (Acto.class.getName().equals(lider.getAssociatedType())) {
//                    lider.setAssociatedResource(emActividades.find(Acto.class, lider.getAssociatedId()));
//            	}
//            	else if (Equipamiento.class.getName().equals(lider.getAssociatedType())) {
//                    lider.setAssociatedResource(emIntra.find(Equipamiento.class, lider.getAssociatedId()));
//            	}
//            	else if (CentroSubtema.class.getName().equals(lider.getAssociatedType())) {
//                    lider.setAssociatedResource(emIntra.find(CentroSubtema.class, lider.getAssociatedId()));
//            	}
//            	else if (GczGrupoUsuario.class.getName().equals(lider.getAssociatedType())) {
//            	    lider.setAssociatedResource(emMovil.find(GczGrupoUsuario.class, lider.getAssociatedId()));
//                }
//            	
            	// Obtener el usuario líder según la clase
            	if (Ciudadano.class.getName().equals(lider.getUserType())) {
                    lider.setUserResource(emNoticias.find(Ciudadano.class, lider.getUserId().intValue()));
            	}
            	else if (GczUsuario.class.getName().equals(lider.getUserType())) {
                    lider.setUserResource(emMovil.find(GczUsuario.class, lider.getUserId()));
            	}
            	else if (GczGrupoUsuario.class.getName().equals(lider.getUserType())) {
                    lider.setUserResource(emMovil.find(GczGrupoUsuario.class, lider.getUserId()));
            	}
            }
            catch (Exception e) {
            	logger.error(e.getMessage());
            }
        }
    }

}
